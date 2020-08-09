package com.ruoyi.common.utils;

import java.util.List;

public class JsonTreeData {

    public String value;       //json id 节点ID，对加载远程数据很重要。
    public String parentValue;      //
    public String label;     //json 显示节点文本。
    public List<JsonTreeData> children;    //子节点

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getParentValue() {
        return parentValue;
    }

    public void setParentValue(String parentValue) {
        this.parentValue = parentValue;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<JsonTreeData> getChildren() {
        return children;
    }

    public void setChildren(List<JsonTreeData> children) {
        this.children = children;
    }
}
