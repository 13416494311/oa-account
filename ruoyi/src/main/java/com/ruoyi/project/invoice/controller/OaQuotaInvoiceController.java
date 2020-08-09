package com.ruoyi.project.invoice.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.utils.*;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.project.invoice.domain.OaOtherInvoice;
import com.ruoyi.project.invoice.service.IOaOtherInvoiceService;
import com.ruoyi.project.reimbursement.domain.OaReimbursementApply;
import com.ruoyi.project.reimbursement.service.IOaReimbursementApplyService;
import com.ruoyi.project.system.domain.SysAttachment;
import com.ruoyi.project.system.service.ISysAttachmentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.invoice.domain.OaQuotaInvoice;
import com.ruoyi.project.invoice.service.IOaQuotaInvoiceService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 定额发票Controller
 *
 * @author ruoyi
 * @date 2020-06-20
 */
@RestController
@RequestMapping("/invoice/quota")
public class OaQuotaInvoiceController extends BaseController
{
    @Autowired
    private IOaQuotaInvoiceService oaQuotaInvoiceService;
    @Autowired
    private ISysAttachmentService sysAttachmentService;
    @Autowired
    private IOaReimbursementApplyService oaReimbursementApplyService;
    @Autowired
    private IOaOtherInvoiceService oaOtherInvoiceService;

    /**
     * 查询定额发票列表
     */
    @GetMapping("/list")
    public TableDataInfo list(OaQuotaInvoice oaQuotaInvoice)
    {
        startPage();
        List<OaQuotaInvoice> list = oaQuotaInvoiceService.selectOaQuotaInvoiceList(oaQuotaInvoice);
        return getDataTable(list);
    }

    /**
     * 导出定额发票列表
     */
    @Log(title = "定额发票", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(OaQuotaInvoice oaQuotaInvoice)
    {
        List<OaQuotaInvoice> list = oaQuotaInvoiceService.selectOaQuotaInvoiceList(oaQuotaInvoice);
        ExcelUtil<OaQuotaInvoice> util = new ExcelUtil<OaQuotaInvoice>(OaQuotaInvoice.class);
        return util.exportExcel(list, "quota");
    }

    /**
     * 获取定额发票详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(oaQuotaInvoiceService.selectOaQuotaInvoiceById(id));
    }

    /**
     * 新增定额发票
     */
    @Log(title = "定额发票", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OaQuotaInvoice oaQuotaInvoice)
    {
        return toAjax(oaQuotaInvoiceService.insertOaQuotaInvoice(oaQuotaInvoice));
    }

    /**
     * 修改定额发票
     */
    @Log(title = "定额发票", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OaQuotaInvoice oaQuotaInvoice)
    {
        return toAjax(oaQuotaInvoiceService.updateOaQuotaInvoice(oaQuotaInvoice));
    }

    /**
     * 删除定额发票
     */
    @Log(title = "定额发票", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(oaQuotaInvoiceService.deleteOaQuotaInvoiceByIds(ids));
    }

    /**
     * 扫描方式-新增定额发票
     */
    @Log(title = "新增定额发票-扫描附件方式", businessType = BusinessType.INSERT)
    @PostMapping(value = "/addByAtt")
    public AjaxResult addByAtt(@RequestParam("file") MultipartFile file,
                               @RequestParam("uuid") String uuid,
                               SysAttachment sysAttachment) throws IOException {
        String quotaInvoicUuid = UUID.randomUUID().toString();

        File quotaInvoiceFile = FileUtil.multipartFileToFile(file);
        String fileName = quotaInvoiceFile.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if("pdf".equals(suffix.toLowerCase())){
            List<File> pngFileList = Pdf2PngUtils.pdf2png(quotaInvoiceFile,0,1);
            if(StringUtils.isNotEmpty(pngFileList)){
                quotaInvoiceFile=pngFileList.get(0);
            }
        }
        String filePath = FileUploadUtils.upload(RuoYiConfig.getProfile()+
                        "/"+ StringUtils.lineToHump(sysAttachment.getFileDictType())+"/"+sysAttachment.getFileDictValue(),
                FileUtil.fileToMultipartFile(quotaInvoiceFile));

        String result = BaiduApi.vatInvoice(quotaInvoiceFile,sysAttachment.getFileDictValue()).replace("*","");
        Map<String,Object> resultMap = JSON.parseObject(result);
        Map<String,Object> oaQuotaInvoiceMap = (Map<String, Object>) resultMap.get("words_result");
        if(oaQuotaInvoiceMap!=null){
            sysAttachment.setFilePath(filePath);
            sysAttachment.setUuid(quotaInvoicUuid);
            sysAttachment.setFileName(quotaInvoiceFile.getName());
            sysAttachmentService.insertSysAttachment(sysAttachment);

            OaQuotaInvoice oaQuotaInvoice = JSON.parseObject(JSON.toJSONString(oaQuotaInvoiceMap), OaQuotaInvoice.class);
            oaQuotaInvoice.setInvoiceScanNumber(oaQuotaInvoice.getInvoiceNumber());
            /*if(checkQuotaInvoiceExist(oaQuotaInvoice.getInvoiceNumber())){
                oaQuotaInvoice.setRepeatFlag("1");
            }*/

            oaQuotaInvoice.setSysAttId(sysAttachment.getId());
            oaQuotaInvoice.setInvoiceUuid(quotaInvoicUuid);
            oaQuotaInvoice.setUuid(uuid);
            oaQuotaInvoice.setAccessWay("autoScan");
            oaQuotaInvoice.setInvoiceMoney(ChineseAmountUtil.chinese2Number(oaQuotaInvoice.getInvoiceRate()).doubleValue());

            oaQuotaInvoice.setRepeatFlag("0");
            String checkResult = checkQuotaInvoiceResult(oaQuotaInvoice);
            if(checkResult.indexOf("已报销")!=-1){
                return AjaxResult.error("该发票号码已报销;请确认！");
            }
            if(checkResult.indexOf("已上传")!=-1){
                oaQuotaInvoice.setAbnormalFlag("1");
            }

            oaQuotaInvoiceService.insertOaQuotaInvoice(oaQuotaInvoice);
            return AjaxResult.success(oaQuotaInvoice);
        } else {
            return AjaxResult.error("无法读取定额发票信息！");
        }
    }

    private boolean checkQuotaInvoiceExist(String invoiceNumber){
        OaQuotaInvoice oaQuotaInvoice = new OaQuotaInvoice();
        oaQuotaInvoice.setInvoiceNumber(invoiceNumber);
        oaQuotaInvoice.setRepeatFlag("0");
        List<OaQuotaInvoice> oaQuotaInvoiceList = oaQuotaInvoiceService.selectOaQuotaInvoiceList(oaQuotaInvoice);
        return StringUtils.isNotEmpty(oaQuotaInvoiceList);
    }

    /**
     * 检查定额发票是否已存在
     */
    @PostMapping(value = "checkQuotaInvoiceExist")
    public AjaxResult checkQuotaInvoice(@RequestBody OaQuotaInvoice oaQuotaInvoice)
    {
        return AjaxResult.success(checkQuotaInvoiceResult(oaQuotaInvoice));
    }

    private String checkQuotaInvoiceResult(OaQuotaInvoice oaQuotaInvoice){
        String result = "" ;
        if(StringUtils.isNotNull(oaQuotaInvoice.getInvoiceScanNumber())&&
                !oaQuotaInvoice.getInvoiceScanNumber().equals(oaQuotaInvoice.getInvoiceNumber())){
            result+="该发票号码非自动扫描发票号码;";
        }

        Boolean reimbursement = false ;
        Boolean upload = false ;

        //校验定额发票中是否已上传、已报销
        OaQuotaInvoice oaQuotaInvoiceParam = new OaQuotaInvoice();
        oaQuotaInvoiceParam.setInvoiceNumber(oaQuotaInvoice.getInvoiceNumber());
        oaQuotaInvoiceParam.setRepeatFlag("0");
        List<OaQuotaInvoice> oaQuotaInvoiceList = oaQuotaInvoiceService.
                selectOaQuotaInvoiceList(oaQuotaInvoiceParam);
        for(int i=0;i<oaQuotaInvoiceList.size();i++){
            if(StringUtils.isNotNull(oaQuotaInvoice.getId())
                    && oaQuotaInvoice.getId() == oaQuotaInvoiceList.get(i).getId()){
                continue;
            }
            OaReimbursementApply apply = oaReimbursementApplyService.
                    selectOaReimbursementApplyByUuid(oaQuotaInvoiceList.get(i).getUuid());
            if(StringUtils.isNotNull(apply)){
                if("2".equals(apply.getAuditState())){  //已报销
                    reimbursement = true ;
                }else{           //已上传
                    upload = true ;
                }
            }
        }
        //校验其他发票中是否已上传、已报销
        OaOtherInvoice oaOtherInvoiceParam = new OaOtherInvoice();
        oaOtherInvoiceParam.setInvoiceNumber(oaQuotaInvoice.getInvoiceNumber());
        List<OaOtherInvoice> oaOtherInvoiceList = oaOtherInvoiceService.
                selectOaOtherInvoiceList(oaOtherInvoiceParam);
        for(int i=0;i<oaOtherInvoiceList.size();i++){
            OaReimbursementApply apply = oaReimbursementApplyService.
                    selectOaReimbursementApplyByUuid(oaOtherInvoiceList.get(i).getUuid());
            if(StringUtils.isNotNull(apply)){
                if("2".equals(apply.getAuditState())){  //已报销
                    reimbursement = true ;
                }else{           //已上传
                    upload = true ;
                }
            }
        }
        if(upload){
            result+= "该发票号码已上传;" ;
        }
        if(reimbursement){
            result+= "该发票号码已报销;" ;
        }
        return result;

    }
}
