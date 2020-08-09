package com.ruoyi.project.invoice.service;

import com.ruoyi.project.invoice.domain.OaTicket;
import java.util.List;

public interface  IOaTicketService {

    public List<OaTicket> selectOaTicketListByUuid(String uuid);
}
