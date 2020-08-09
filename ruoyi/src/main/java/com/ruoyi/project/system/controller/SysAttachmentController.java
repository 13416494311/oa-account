package com.ruoyi.project.system.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.FileUtil;
import com.ruoyi.common.utils.Pdf2PngUtil;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.security.service.TokenService;
import com.ruoyi.project.system.domain.SysDictData;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysDictDataService;
import org.apache.commons.io.IOUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.system.domain.SysAttachment;
import com.ruoyi.project.system.service.ISysAttachmentService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 附件信息Controller
 *
 * @author ruoyi
 * @date 2020-06-07
 */
@RestController
@RequestMapping("/system/attachment")
public class SysAttachmentController extends BaseController
{
    @Autowired
    private ISysAttachmentService sysAttachmentService;
    @Autowired
    private ISysDictDataService dictDataService;

    private static Pattern linePattern = Pattern.compile("_(\\w)");

    /**
     * 查询附件信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SysAttachment sysAttachment)
    {
        startPage();
        List<SysAttachment> list = sysAttachmentService.selectSysAttachmentList(sysAttachment);
        return getDataTable(list);
    }

    /**
     * 导出附件信息列表
     */
    @Log(title = "附件信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysAttachment sysAttachment)
    {
        List<SysAttachment> list = sysAttachmentService.selectSysAttachmentList(sysAttachment);
        ExcelUtil<SysAttachment> util = new ExcelUtil<SysAttachment>(SysAttachment.class);
        return util.exportExcel(list, "attachment");
    }

    /**
     * 获取附件信息详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sysAttachmentService.selectSysAttachmentById(id));
    }

    /**
     * 新增附件信息
     */
    @Log(title = "附件信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysAttachment sysAttachment)
    {
        return toAjax(sysAttachmentService.insertSysAttachment(sysAttachment));
    }

    /**
     * 修改附件信息
     */
    @Log(title = "附件信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysAttachment sysAttachment)
    {
        return toAjax(sysAttachmentService.updateSysAttachment(sysAttachment));
    }

    /**
     * 删除附件信息
     */
    @Log(title = "附件信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id)
    {
        return toAjax(sysAttachmentService.deleteSysAttachmentById(id));
    }


    @GetMapping(value = "/reimbursement")
    public AjaxResult dictType(SysAttachment sysAttachment)
    {
        if(StringUtils.isEmpty(sysAttachment.getFileDictType())){
            return AjaxResult.success(null);
        }else{
            List<SysDictData> dictDataList = dictDataService.selectDictDataByType(sysAttachment.getFileDictType());
            dictDataList.addAll(sysAttachmentService.selectSysAttAsSysDict(sysAttachment));
            for(SysDictData sysDictData:dictDataList){
                sysAttachment.setFileDictValue(sysDictData.getDictValue());
                sysDictData.setAttList(sysAttachmentService.selectSysAttachmentList(sysAttachment));
            }
            return AjaxResult.success(dictDataList);
        }
    }

    @PostMapping("/upload")
    public AjaxResult upload(@RequestParam("file") MultipartFile file,
                             SysAttachment sysAttachment) throws IOException
    {
        if (!file.isEmpty())
        {

            String filePath = FileUploadUtils.upload(RuoYiConfig.getProfile()+
                            "/"+ StringUtils.lineToHump(sysAttachment.getFileDictType())+"/"+sysAttachment.getFileDictValue(),
                    file);
            sysAttachment.setFilePath(filePath);
            sysAttachment.setFileName(file.getOriginalFilename());
            sysAttachmentService.insertSysAttachment(sysAttachment);
            return AjaxResult.success(sysAttachment);
        }else{
            return AjaxResult.error("上传图片异常，请联系管理员!");
        }

    }

    @PostMapping("/uploadPdfToPng")
    public AjaxResult uploadPdfToPng(@RequestParam("file") MultipartFile file,
                             SysAttachment sysAttachment) throws IOException
    {
        if (!file.isEmpty())
        {

            File pngFile = FileUtil.multipartFileToFile(file);
            String fileName = pngFile.getName();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            if("pdf".equals(suffix.toLowerCase())){
                List<File> pngFileList = Pdf2PngUtil.pdf2png(pngFile,0,1);
                if(StringUtils.isNotEmpty(pngFileList)){
                    pngFile=pngFileList.get(0);
                }
            }
            String filePath = FileUploadUtils.upload(RuoYiConfig.getProfile()+
                            "/"+ StringUtils.lineToHump(sysAttachment.getFileDictType())+"/"+sysAttachment.getFileDictValue(),
                    FileUtil.fileToMultipartFile(pngFile));
            sysAttachment.setFilePath(filePath);
            sysAttachment.setFileName(pngFile.getName());
            sysAttachmentService.insertSysAttachment(sysAttachment);
            if(pngFile.exists()){
                pngFile.delete();
            }
            return AjaxResult.success(sysAttachment);
        }else{
            return AjaxResult.error("上传图片异常，请联系管理员!");
        }

    }

    @RequestMapping(value = "/download/{id}")
    public void download(@PathVariable Long id, HttpServletResponse response)
    {
        SysAttachment sysAttachment = sysAttachmentService.selectSysAttachmentById(id);
        // 本地资源路径
        String localPath = RuoYiConfig.getProfile();
        String downloadPath = localPath + StringUtils.substringAfter(sysAttachment.getFilePath(),
                Constants.RESOURCE_PREFIX);
        File file =new File(downloadPath);
        String fileName= null;
        try {
            fileName = URLEncoder.encode(sysAttachment.getFileName(), "utf-8");
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.addHeader("Content-Length", "" + FileUtil.getBytesByFile(file).length);
            response.setContentType("application/octet-stream; charset=UTF-8");
            IOUtils.write(FileUtil.getBytesByFile(file), response.getOutputStream());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }


    }


}
