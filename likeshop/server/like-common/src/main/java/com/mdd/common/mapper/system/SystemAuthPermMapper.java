package com.mdd.common.mapper.system;

import com.mdd.common.core.basics.IBaseMapper;
import com.mdd.common.entity.system.SystemAuthPerm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色菜单Mapper
 */
@Mapper
public interface SystemAuthPermMapper extends IBaseMapper<SystemAuthPerm> {
    /**
     * 批量插入角色权限
     * @author damonyuan
     * @date 2024-12-19
     * @param permList 权限列表
     * @return 插入数量
     */
    int batchInsertPerms(@Param("list") List<SystemAuthPerm> permList);
}
