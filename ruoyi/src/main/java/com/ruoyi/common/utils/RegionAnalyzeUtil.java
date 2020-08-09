package com.ruoyi.common.utils;

import com.ruoyi.common.utils.job.AbstractQuartzJob;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.project.system.service.impl.SysRegionServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 解析地区code为地址
 *
 */
public class RegionAnalyzeUtil {
    private static final Logger log = LoggerFactory.getLogger(AbstractQuartzJob.class);

	private static List<JsonTreeData> regionList;
	private static List<String> delist = new ArrayList<String>();
	private static String resCode;
	private static SysRegionServiceImpl regionService;

	public static void initRegion() {
        regionService = SpringUtils.getBean(SysRegionServiceImpl.class);
		try {
			regionList = TreeNodeUtil.getfatherNode(regionService.getRegionTree());
		} catch (Exception e) {
            log.error("运行时出错:",e);
		}
	}


	public synchronized static String regionAnalyze(String regionCode) {
		regionList = getRegionList();
		decomposeRegionCode(regionCode);
		for (JsonTreeData tmp1 : regionList) {
			if (tmp1.getValue().equals(delist.get(2))) {
				resCode = tmp1.getLabel();
				if (tmp1.getChildren() != null) {
					for (JsonTreeData tmp2 : tmp1.getChildren()) {
						if (tmp2.getValue().equals(delist.get(1))) {
							resCode += tmp2.getLabel();
							if (tmp2.getChildren() != null) {
								for (JsonTreeData tmp3 : tmp2.getChildren()) {
									if (tmp3.getValue().equals(delist.get(0))) {
										resCode += tmp3.getLabel();
									}
								}
							}
						}
					}
				}
			}
		}
		return resCode;
	}

	private static List<String> decomposeRegionCode(String regionCode) {
		delist.clear();
        delist= Arrays.asList(regionCode.split("-"));
		return delist;
	}

	public static List<JsonTreeData> getRegionList() {
		if (regionList == null) {
			initRegion();
		}
		return regionList;
	}

	/**
	 *  sql中like使用
	 */
	public static String getAbbrRegionCode(String code){
		if(StringUtils.isEmpty(code))return null;
		code=code.replaceAll("0+?$", "");
		if(code.length()%2!=0)
			code+="0";
		return code;
	}
}
