package com.ruoyi.common.utils;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TreeNodeUtil {

    /**
    * @Title: getfatherNode
    * @Description 方法描述: 父节点
    * @param 设定文件： @param treeDataList
    * @param 设定文件： @return
    * @return 返回类型：List<JsonTreeData>
    * @throws
    * @date 最后修改时间：2015年6月9日 下午6:39:26
    */
    public final static List<JsonTreeData> getfatherNode(List<JsonTreeData> treeDataList) {
        List<JsonTreeData> newTreeDataList = new ArrayList<JsonTreeData>();
        for (JsonTreeData jsonTreeData : treeDataList) {
            if(jsonTreeData.getParentValue().equals("100000")) {
                //获取父节点下的子节点
                jsonTreeData.setChildren(getChildrenNode(jsonTreeData,treeDataList));
                //jsonTreeData.setState("open");
                newTreeDataList.add(jsonTreeData);
            }
        }
        return newTreeDataList;
    }

    /**
    * @Title: getChildrenNode
    * @Description 方法描述: 子节点
    * @param 设定文件： @param pid
    * @param 设定文件： @param treeDataList
    * @param 设定文件： @return
    * @return 返回类型：List<JsonTreeData>
    * @throws
    * @date 最后修改时间：2015年6月9日 下午6:39:50
    */
    private final static List<JsonTreeData> getChildrenNode(JsonTreeData fatherNode, List<JsonTreeData> treeDataList) {
        List<JsonTreeData> newTreeDataList = new ArrayList<JsonTreeData>();
        for (JsonTreeData jsonTreeData : treeDataList) {
            if(jsonTreeData.getParentValue() == null){
            	continue;
            }
            //这是一个子节点
            if(jsonTreeData.getParentValue().equals(fatherNode.getValue())){
                //递归获取子节点下的子节点
                jsonTreeData.setChildren(getChildrenNode(jsonTreeData , treeDataList));
                newTreeDataList.add(jsonTreeData);
            }
        }
        return newTreeDataList;
    }
}
