package com.ruoyi.project.system.service.impl;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.framework.security.LoginUser;
    import com.ruoyi.framework.security.service.TokenService;
import com.ruoyi.project.system.domain.SysDictData;
import com.ruoyi.project.system.domain.SysUser;
            import com.ruoyi.common.utils.ServletUtils;
    import com.ruoyi.common.utils.spring.SpringUtils;
    import com.ruoyi.framework.security.LoginUser;
    import com.ruoyi.framework.security.service.TokenService;
    import com.ruoyi.project.system.domain.SysUser;
    import com.ruoyi.common.utils.DateUtils;
    import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.mapper.SysAttachmentMapper;
import com.ruoyi.project.system.domain.SysAttachment;
import com.ruoyi.project.system.service.ISysAttachmentService;

/**
 * 附件信息Service业务层处理
 *
 * @author ruoyi
 * @date 2020-06-07
 */
@Service
public class SysAttachmentServiceImpl implements ISysAttachmentService
{
    @Autowired
    private SysAttachmentMapper sysAttachmentMapper;

    /**
     * 查询附件信息
     *
     * @param id 附件信息ID
     * @return 附件信息
     */
    @Override
    public SysAttachment selectSysAttachmentById(Long id)
    {
        return sysAttachmentMapper.selectSysAttachmentById(id);
    }

    /**
     * 查询附件信息列表
     *
     * @param sysAttachment 附件信息
     * @return 附件信息
     */
    @Override
    public List<SysAttachment> selectSysAttachmentList(SysAttachment sysAttachment)
    {
        return sysAttachmentMapper.selectSysAttachmentList(sysAttachment);
    }

    @Override
    public List<SysDictData> selectSysAttAsSysDict(SysAttachment sysAttachment){
        List<SysAttachment> list = sysAttachmentMapper.selectSysAttAsSysDict(sysAttachment);
        List<SysDictData> sysDictList = list.stream().map( (item) ->{
            SysDictData sysDictData =new SysDictData();
            sysDictData.setDictType(item.getFileDictType());
            sysDictData.setDictLabel(item.getFileDictValue());
            sysDictData.setDictValue(item.getFileDictValue());
            return sysDictData;
        }).collect(Collectors.toList());
        return sysDictList;
    }
    /**
     * 新增附件信息
     *
     * @param sysAttachment 附件信息
     * @return 结果
     */
    @Override
    public int insertSysAttachment(SysAttachment sysAttachment)
    {
        sysAttachment.setCreateTime(DateUtils.getNowDate());
        return sysAttachmentMapper.insertSysAttachment(sysAttachment);
    }

    /**
     * 修改附件信息
     *
     * @param sysAttachment 附件信息
     * @return 结果
     */
    @Override
    public int updateSysAttachment(SysAttachment sysAttachment)
    {
        return sysAttachmentMapper.updateSysAttachment(sysAttachment);
    }

    /**
     * 批量删除附件信息
     *
     * @param ids 需要删除的附件信息ID
     * @return 结果
     */
    @Override
    public int deleteSysAttachmentByIds(Long[] ids)
    {
        return sysAttachmentMapper.deleteSysAttachmentByIds(ids);
    }

    /**
     * 删除附件信息信息
     *
     * @param id 附件信息ID
     * @return 结果
     */
    @Override
    public int deleteSysAttachmentById(Long id)
    {
        SysAttachment sysAttachment = selectSysAttachmentById(id);

        // 本地资源路径
        String localPath = RuoYiConfig.getProfile();
        String filePath = localPath + StringUtils.substringAfter(sysAttachment.getFilePath(),
                Constants.RESOURCE_PREFIX);
        File file =new File(filePath);
        if(file.exists()){
            file.delete();
        }
        return sysAttachmentMapper.deleteSysAttachmentById(id);
    }
}
