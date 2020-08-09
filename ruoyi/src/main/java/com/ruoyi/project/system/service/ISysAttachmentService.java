package com.ruoyi.project.system.service;

import java.util.List;
import com.ruoyi.project.system.domain.SysAttachment;
import com.ruoyi.project.system.domain.SysDictData;

/**
 * 附件信息Service接口
 *
 * @author ruoyi
 * @date 2020-06-07
 */
public interface ISysAttachmentService
{
    /**
     * 查询附件信息
     *
     * @param id 附件信息ID
     * @return 附件信息
     */
    public SysAttachment selectSysAttachmentById(Long id);

    /**
     * 查询附件信息列表
     *
     * @param sysAttachment 附件信息
     * @return 附件信息集合
     */
    public List<SysAttachment> selectSysAttachmentList(SysAttachment sysAttachment);

    public List<SysDictData> selectSysAttAsSysDict(SysAttachment sysAttachment);

    /**
     * 新增附件信息
     *
     * @param sysAttachment 附件信息
     * @return 结果
     */
    public int insertSysAttachment(SysAttachment sysAttachment);

    /**
     * 修改附件信息
     *
     * @param sysAttachment 附件信息
     * @return 结果
     */
    public int updateSysAttachment(SysAttachment sysAttachment);

    /**
     * 批量删除附件信息
     *
     * @param ids 需要删除的附件信息ID
     * @return 结果
     */
    public int deleteSysAttachmentByIds(Long[] ids);

    /**
     * 删除附件信息信息
     *
     * @param id 附件信息ID
     * @return 结果
     */
    public int deleteSysAttachmentById(Long id);
}
