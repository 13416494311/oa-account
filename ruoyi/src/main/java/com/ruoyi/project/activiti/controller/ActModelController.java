package com.ruoyi.project.activiti.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.PageDomain;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.framework.web.page.TableSupport;
import com.ruoyi.project.activiti.domain.ModelEntityDto;
import com.ruoyi.project.activiti.service.ActModelService;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.persistence.entity.ModelEntity;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static org.activiti.editor.constants.ModelDataJsonConstants.MODEL_DESCRIPTION;
import static org.activiti.editor.constants.ModelDataJsonConstants.MODEL_NAME;

/**
 * 模型管理 操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/activiti/model")
public class ActModelController extends BaseController
{
	private String prefix = "activiti/model";

	@Autowired
	private ActModelService actModelService;

	@Autowired
	private ObjectMapper objectMapper;


	@PreAuthorize("@ss.hasPermi('activiti:model:list')")
	@GetMapping("/list")
	public TableDataInfo list(ModelEntityDto modelEntityDto)
	{

		ModelQuery modelQuery = actModelService.selectModelList(modelEntityDto);

		PageDomain pageDomain = TableSupport.buildPageRequest();
		Integer pageNum = pageDomain.getPageNum();
		Integer pageSize = pageDomain.getPageSize();
		if (pageNum == null||pageNum < 0)
		{
			pageNum = 0;
		}
		Integer pageFirstRowNum = (pageNum - 1) * pageSize;

		TableDataInfo data = new TableDataInfo();
		data.setTotal(modelQuery.count());
		data.setRows(modelQuery.orderByLastUpdateTime().desc().listPage(pageFirstRowNum, pageSize));
		data.setCode(HttpStatus.SUCCESS);

		return data;
	}

	@PreAuthorize("@ss.hasPermi('activiti:model:add')")
	@GetMapping("/add")
	public AjaxResult add() throws UnsupportedEncodingException {
		ModelEntity model = new ModelEntity();
		String name = "new-process";
		String description = "新的流程";
		int revision = 1;
		String key = "process";
		ObjectNode modelNode = objectMapper.createObjectNode();
		modelNode.put(ModelDataJsonConstants.MODEL_NAME, name);
		modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
		modelNode.put(ModelDataJsonConstants.MODEL_REVISION, revision);
		model.setName(name);
		model.setKey(key);
		model.setMetaInfo(modelNode.toString());
		String modelId = actModelService.createModel(model);
		//todo
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		map.put("id", modelId);
		return AjaxResult.success(map);
	}

	@GetMapping("/{modelId}/json")
	@ResponseBody
	public ObjectNode getEditorJson(@PathVariable String modelId)
	{
		ObjectNode modelNode = actModelService.selectWrapModelById(modelId);
		return modelNode;
	}


	@GetMapping(value = "/editor/stencilset", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String getStencilset() throws IOException
	{
		InputStream stencilsetStream = this.getClass().getClassLoader().getResourceAsStream("stencilset.json");
		return IOUtils.toString(stencilsetStream, "utf-8");
	}

	@Log(title = "删除模型", businessType = BusinessType.DELETE)
	@PreAuthorize("@ss.hasPermi('activiti:model:remove')")
	@DeleteMapping("/remove/{ids}")
	public AjaxResult remove(@PathVariable String[] ids)
	{
		actModelService.deleteModelIds(ids);
		return AjaxResult.success();
	}

	/**
	 * 发布流程
	 * @param modelId
	 * @return
	 * @throws Exception
	 */
	@Log(title = "发布流程", businessType = BusinessType.UPDATE)
	@PreAuthorize("@ss.hasPermi('activiti:model:deploy')")
	@GetMapping("/deploy/{modelId}")
	@ResponseBody
	public AjaxResult deploy(@PathVariable("modelId") String modelId) throws Exception
	{
		return actModelService.deployProcess(modelId);
	}

	@Log(title = "模型管理", businessType = BusinessType.UPDATE)
	@PutMapping(value = "/{modelId}/save")
	public void save(@PathVariable String modelId, String name, String description, String json_xml,
					 String svg_xml) throws IOException, TranscoderException
	{
		Model model = actModelService.selectModelById(modelId);
		ObjectNode modelJson = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
		modelJson.put(MODEL_NAME, name);
		modelJson.put(MODEL_DESCRIPTION, description);
		model.setMetaInfo(modelJson.toString());
		model.setName(name);
		actModelService.update(model, json_xml, svg_xml);

	}

	@Log(title = "导出指定模型", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('activiti:model:export')")
	@RequestMapping("/export/{modelId}")
	public void exportToXml(@PathVariable("modelId") String modelId, HttpServletResponse response)
	{
		try
		{
			Model modelData = actModelService.selectModelById(modelId);
			BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
			JsonNode editorNode = new ObjectMapper().readTree(actModelService.getModelEditorSource(modelData.getId()));
			BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
			BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
			byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);

			String filename = bpmnModel.getMainProcess().getId() + ".bpmn20.xml";

			response.reset();
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			response.addHeader("Content-Length", "" + bpmnBytes.length);
			response.setContentType("application/octet-stream; charset=UTF-8");
			IOUtils.write(bpmnBytes, response.getOutputStream());
		}
		catch (Exception e)
		{
			throw new ActivitiException("导出model的xml文件失败，模型ID=" + modelId, e);
		}
	}
}
