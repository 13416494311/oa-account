package com.ruoyi.project.invoice.service.impl;


import com.ruoyi.project.invoice.domain.*;
import com.ruoyi.project.invoice.service.*;
import com.ruoyi.project.system.domain.SysAttachment;
import com.ruoyi.project.system.service.ISysAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 票据信息Service业务层处理
 *
 * @author ruoyi
 * @date 2020-06-21
 */
@Service
public class OaTicketServiceImpl implements IOaTicketService {

    @Autowired
    private IOaInvoiceService oaInvoiceService;
    @Autowired
    private IOaQuotaInvoiceService oaQuotaInvoiceService;
    @Autowired
    private ISysAttachmentService sysAttachmentService;
    @Autowired
    private IOaTrainTicketService oaTrainTicketService;
    @Autowired
    private IOaAirTicketService oaAirTicketService;
    @Autowired
    private IOaTaxiTicketService oaTaxiTicketService;
    @Autowired
    private IOaOtherInvoiceService oaOtherInvoiceService;


    @Override
    public List<OaTicket> selectOaTicketListByUuid(String uuid) {

        List<OaTicket> list = new ArrayList<OaTicket>();

        //增值税发票
        OaInvoice oaInvoice =new OaInvoice();
        oaInvoice.setUuid(uuid);
        oaInvoice.setRepeatFlag("0");
        List<OaInvoice> oaInvoiceList = oaInvoiceService.selectOaInvoiceList(oaInvoice);
        List<OaTicket> invoiceToTicketList = invoiceToTicket(oaInvoiceList);
        list.addAll(invoiceToTicketList);

        //定额发票
        OaQuotaInvoice oaQuotaInvoice =new OaQuotaInvoice();
        oaQuotaInvoice.setUuid(uuid);
        oaQuotaInvoice.setRepeatFlag("0");
        List<OaQuotaInvoice> oaQuotaInvoiceList = oaQuotaInvoiceService.selectOaQuotaInvoiceList(oaQuotaInvoice);
        List<OaTicket> quotaInvoiceToTicketList = quotaInvoiceToTicket(oaQuotaInvoiceList);
        list.addAll(quotaInvoiceToTicketList);

        //火车票
        OaTrainTicket oaTrainTicket = new OaTrainTicket();
        oaTrainTicket.setUuid(uuid);
        oaTrainTicket.setRepeatFlag("0");
        List<OaTrainTicket> trainTicketList = oaTrainTicketService.selectOaTrainTicketList(oaTrainTicket);
        List<OaTicket> trainTicketToTicketList = trainTicketToTicket(trainTicketList);
        list.addAll(trainTicketToTicketList);

        //机票行程单
        OaAirTicket oaAirTicket = new OaAirTicket();
        oaAirTicket.setUuid(uuid);
        oaAirTicket.setRepeatFlag("0");
        List<OaAirTicket> airTicketList = oaAirTicketService.selectOaAirTicketList(oaAirTicket);
        List<OaTicket> airTicketToTicketList = airTicketToTicket(airTicketList);
        list.addAll(airTicketToTicketList);

        //出租车发票
        OaTaxiTicket oaTaxiTicket = new OaTaxiTicket();
        oaTaxiTicket.setUuid(uuid);
        oaTaxiTicket.setRepeatFlag("0");
        List<OaTaxiTicket> taxiTicketList = oaTaxiTicketService.selectOaTaxiTicketList(oaTaxiTicket);
        List<OaTicket> taxiTicketToTicketList = taxiTicketToTicket(taxiTicketList);
        list.addAll(taxiTicketToTicketList);

        //其他发票
        OaOtherInvoice oaOtherInvoice =new OaOtherInvoice();
        oaOtherInvoice.setUuid(uuid);
        List<OaOtherInvoice> oaOtherInvoiceList = oaOtherInvoiceService.selectOaOtherInvoiceList(oaOtherInvoice);
        List<OaTicket> otherInvoiceToTicketList = otherInvoiceToTicket(oaOtherInvoiceList);
        list.addAll(otherInvoiceToTicketList);


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (list.size() > 1) {
            list.sort((oaTicket1, oaTicket2) -> {
                return oaTicket1.getCreateTime().compareTo(oaTicket2.getCreateTime());
            });
        }
        return list;
    }

    //其他发票转ticket
    private List<OaTicket> otherInvoiceToTicket(List<OaOtherInvoice> oaOtherInvoiceList){
        List<OaTicket> list =oaOtherInvoiceList.stream().map(oaOtherInvoice->{
            OaTicket oaTicket =new OaTicket();
            oaTicket.setId(oaOtherInvoice.getId());
            oaTicket.setUuid(oaOtherInvoice.getUuid());
            oaTicket.setAccessWay(oaOtherInvoice.getAccessWay());
            oaTicket.setTicketType("6");
            oaTicket.setTicketSubType(oaOtherInvoice.getInvoiceType());
            oaTicket.setTicketNum(oaOtherInvoice.getInvoiceNumber());
            oaTicket.setTicketDate(oaOtherInvoice.getInvoiceDate());
            oaTicket.setTotalAmount(oaOtherInvoice.getInvoiceMoney());
            oaTicket.setAbnormalFlag(oaOtherInvoice.getAbnormalFlag());
            oaTicket.setCreateTime(oaOtherInvoice.getCreateTime());
            List<SysAttachment>  sysAttachments= new ArrayList<SysAttachment>();
            sysAttachments.add(sysAttachmentService.selectSysAttachmentById(oaOtherInvoice.getSysAttId()));
            oaTicket.setSysAtts(sysAttachments);
            return oaTicket;
        }).collect(Collectors.toList());
        return list;
    }
    //机票转ticket
    private List<OaTicket> taxiTicketToTicket(List<OaTaxiTicket> taxiTicketlist){
        List<OaTicket> list =taxiTicketlist.stream().map(taxiTicket->{
            OaTicket oaTicket =new OaTicket();
            oaTicket.setId(taxiTicket.getId());
            oaTicket.setUuid(taxiTicket.getUuid());
            oaTicket.setAccessWay(taxiTicket.getAccessWay());
            oaTicket.setTicketType("7");
            oaTicket.setTicketSubType("出租车发票");
            oaTicket.setTicketNum(taxiTicket.getInvoiceNum());
            oaTicket.setTicketScanNum(taxiTicket.getInvoiceScanNum());
            oaTicket.setTicketDate(taxiTicket.getDateTimeStart());
            oaTicket.setTotalAmount(taxiTicket.getFare());
            List<String> content = new ArrayList<String>();
            content.add(taxiTicket.getTaxiNum());
            oaTicket.setContent(content);
            oaTicket.setAbnormalFlag(taxiTicket.getAbnormalFlag());
            oaTicket.setCreateTime(taxiTicket.getCreateTime());

            List<SysAttachment>  sysAttachments= new ArrayList<SysAttachment>();
            String[] attIds = taxiTicket.getSysAttId().split(",");
            for(String attId:attIds){
                sysAttachments.add(sysAttachmentService.selectSysAttachmentById(Long.parseLong(attId)));
            }
            oaTicket.setSysAtts(sysAttachments);

            return oaTicket;
        }).collect(Collectors.toList());
        return list;

    }

    //机票转ticket
    private List<OaTicket> airTicketToTicket(List<OaAirTicket> airTicketlist){
        List<OaTicket> list =airTicketlist.stream().map(airTicket->{
            OaTicket oaTicket =new OaTicket();
            oaTicket.setId(airTicket.getId());
            oaTicket.setUuid(airTicket.getUuid());
            oaTicket.setAccessWay(airTicket.getAccessWay());
            oaTicket.setTicketType("5");
            oaTicket.setTicketSubType("机票行程单");
            oaTicket.setTicketNum(airTicket.getTicketNumber());
            oaTicket.setTicketScanNum(airTicket.getTicketScanNumber());
            oaTicket.setTicketDate(airTicket.getDateTime());
            oaTicket.setTotalAmount(airTicket.getTicketRates());
            oaTicket.setPurchaserName(airTicket.getName());
            oaTicket.setPurchaserNum(airTicket.getIdNum());
            List<String> content = new ArrayList<String>();
            content.add(airTicket.getFlight());
            oaTicket.setContent(content);
            oaTicket.setAbnormalFlag(airTicket.getAbnormalFlag());
            oaTicket.setSellerName(airTicket.getStartingStation());
            oaTicket.setSellerNum(airTicket.getDestinationStation());
            oaTicket.setCreateTime(airTicket.getCreateTime());

            List<SysAttachment>  sysAttachments= new ArrayList<SysAttachment>();
            String[] attIds = airTicket.getSysAttId().split(",");
            for(String attId:attIds){
                sysAttachments.add(sysAttachmentService.selectSysAttachmentById(Long.parseLong(attId)));
            }
            oaTicket.setSysAtts(sysAttachments);

            return oaTicket;
        }).collect(Collectors.toList());
        return list;

    }
    //火车票转ticket
    private List<OaTicket> trainTicketToTicket(List<OaTrainTicket> trainTicketlist){
        List<OaTicket> list =trainTicketlist.stream().map(trainTicket->{
            OaTicket oaTicket =new OaTicket();
            oaTicket.setId(trainTicket.getId());
            oaTicket.setUuid(trainTicket.getUuid());
            oaTicket.setAccessWay(trainTicket.getAccessWay());
            oaTicket.setTicketType("4");
            oaTicket.setTicketSubType("火车票");
            oaTicket.setTicketNum(trainTicket.getTicketNum());
            oaTicket.setTicketScanNum(trainTicket.getTicketScanNum());
            oaTicket.setTicketDate(trainTicket.getDateTime());
            oaTicket.setTotalAmount(trainTicket.getTicketRates());
            oaTicket.setPurchaserName(trainTicket.getName());
            oaTicket.setPurchaserNum(trainTicket.getIdNum());
            List<String> content = new ArrayList<String>();
            content.add(trainTicket.getTrainNum());
            oaTicket.setContent(content);
            oaTicket.setAbnormalFlag(trainTicket.getAbnormalFlag());
            oaTicket.setSellerName(trainTicket.getStartingStation());
            oaTicket.setSellerNum(trainTicket.getDestinationStation());
            oaTicket.setCreateTime(trainTicket.getCreateTime());
            List<SysAttachment>  sysAttachments= new ArrayList<SysAttachment>();
            sysAttachments.add(sysAttachmentService.selectSysAttachmentById(trainTicket.getSysAttId()));
            oaTicket.setSysAtts(sysAttachments);
            return oaTicket;
        }).collect(Collectors.toList());
        return list;

    }
    //定额发票转ticket
    private List<OaTicket> quotaInvoiceToTicket(List<OaQuotaInvoice> oaQuotaInvoiceList){
        List<OaTicket> list =oaQuotaInvoiceList.stream().map(oaQuotaInvoice->{
            OaTicket oaTicket =new OaTicket();
            oaTicket.setId(oaQuotaInvoice.getId());
            oaTicket.setUuid(oaQuotaInvoice.getUuid());
            oaTicket.setAccessWay(oaQuotaInvoice.getAccessWay());
            oaTicket.setTicketType("2");
            oaTicket.setTicketSubType("定额发票");
            oaTicket.setTicketNum(oaQuotaInvoice.getInvoiceNumber());
            oaTicket.setTicketScanNum(oaQuotaInvoice.getInvoiceScanNumber());
            oaTicket.setTotalAmount(oaQuotaInvoice.getInvoiceMoney());
            oaTicket.setAbnormalFlag(oaQuotaInvoice.getAbnormalFlag());
            oaTicket.setCreateTime(oaQuotaInvoice.getCreateTime());
            List<SysAttachment>  sysAttachments= new ArrayList<SysAttachment>();
            sysAttachments.add(sysAttachmentService.selectSysAttachmentById(oaQuotaInvoice.getSysAttId()));
            oaTicket.setSysAtts(sysAttachments);
            return oaTicket;
        }).collect(Collectors.toList());
        return list;
    }
    // 增值税发票发票转ticket实体
    private List<OaTicket> invoiceToTicket(List<OaInvoice> oaInvoiceList){
        List<OaTicket> list = oaInvoiceList.stream().map(oaInvoice->{
            OaTicket oaTicket =new OaTicket();
            oaTicket.setId(oaInvoice.getId());
            oaTicket.setUuid(oaInvoice.getUuid());
            oaTicket.setAccessWay(oaInvoice.getAccessWay());
            oaTicket.setTicketType("1");
            oaTicket.setTicketSubType(oaInvoice.getInvoiceType());
            oaTicket.setTicketNum(oaInvoice.getInvoiceNum());
            oaTicket.setTicketScanNum(oaInvoice.getInvoiceScanNum());
            oaTicket.setTicketDate(oaInvoice.getInvoiceDate());
            oaTicket.setTotalAmount(oaInvoice.getAmountInFiguers());
            oaTicket.setPurchaserName(oaInvoice.getPurchaserName());
            oaTicket.setPurchaserNum(oaInvoice.getPurchaserRegisterNum());
            List<String> content = new ArrayList<String>();
            for(OaInvoiceCommodity commodity:oaInvoice.getOaInvoiceCommodity()){
                content.add(commodity.getName());
            }
            oaTicket.setContent(content);
            oaTicket.setAbnormalFlag(oaInvoice.getAbnormalFlag());
            oaTicket.setSellerName(oaInvoice.getSellerName());
            oaTicket.setSellerNum(oaInvoice.getSellerRegisterNum());
            oaTicket.setCreateTime(oaInvoice.getCreateTime());
            List<SysAttachment>  sysAttachments= new ArrayList<SysAttachment>();
            sysAttachments.add(sysAttachmentService.selectSysAttachmentById(oaInvoice.getSysAttId()));
            oaTicket.setSysAtts(sysAttachments);
            return oaTicket;
        }).collect(Collectors.toList());
        return list;
    }
}
