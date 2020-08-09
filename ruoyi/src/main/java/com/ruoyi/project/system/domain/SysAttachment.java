package com.ruoyi.project.system.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 附件信息对象 sys_attachment
 *
 * @author ruoyi
 * @date 2020-06-07
 */
@Data
public class SysAttachment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 关联信息UUID */
    @Excel(name = "关联信息UUID")
    private String uuid;

    /** 附件字典类型 */
    @Excel(name = "附件字典类型")
    private String fileDictType;

    /** 附件字典值 */
    @Excel(name = "附件字典值")
    private String fileDictValue;

    /** 是否字典类型附件  (0:不是 1:是)*/
    private String fileDicFlag;

    /** 文件名 */
    @Excel(name = "文件名")
    private String fileName;

    /** 文件路径 */
    @Excel(name = "文件路径")
    private String filePath;



}
