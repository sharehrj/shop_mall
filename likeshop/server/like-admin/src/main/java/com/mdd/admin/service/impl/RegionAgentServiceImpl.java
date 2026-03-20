package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.service.IRegionAgentService;
import com.mdd.admin.validate.identity.RegionAgentCreateValidate;
import com.mdd.admin.validate.identity.RegionAgentSearchValidate;
import com.mdd.admin.validate.identity.RegionAgentUpdateValidate;
import com.mdd.admin.vo.identity.RegionAgentDetailVo;
import com.mdd.admin.vo.identity.RegionAgentListedVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.DevRegion;
import com.mdd.common.entity.identity.RegionAgent;
import com.mdd.common.entity.user.User;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.DevRegionMapper;
import com.mdd.common.mapper.identity.RegionAgentMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.UrlUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegionAgentServiceImpl implements IRegionAgentService {

    private static final Map<Integer, String> REGION_LEVEL_MAP = new HashMap<>();

    static {
        REGION_LEVEL_MAP.put(1, "省");
        REGION_LEVEL_MAP.put(2, "市");
        REGION_LEVEL_MAP.put(3, "区县");
    }

    @Resource
    RegionAgentMapper regionAgentMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    DevRegionMapper devRegionMapper;

    @Override
    public PageResult<RegionAgentListedVo> list(RegionAgentSearchValidate searchValidate) {
        QueryWrapper<RegionAgent> wrapper = new QueryWrapper<RegionAgent>()
                .eq("delete_time", 0);

        if (searchValidate.getRegionLevel() != null) {
            wrapper.eq("region_level", searchValidate.getRegionLevel());
        }
        if (searchValidate.getStatus() != null) {
            wrapper.eq("status", searchValidate.getStatus());
        }

        List<Integer> userIds = null;
        if (StringUtils.isNotBlank(searchValidate.getKeyword())) {
            userIds = getUserIdsByKeyword(searchValidate.getKeyword());
            if (userIds.isEmpty()) {
                return PageResult.iPageHandle(0L, (long) searchValidate.getPageNo(),
                        (long) searchValidate.getPageSize(), new ArrayList<>());
            }
            wrapper.in("user_id", userIds);
        }

        wrapper.orderByDesc("id");

        IPage<RegionAgent> iPage = regionAgentMapper.selectPage(
                new Page<>(searchValidate.getPageNo(), searchValidate.getPageSize()), wrapper);

        List<RegionAgentListedVo> list = new ArrayList<>();
        for (RegionAgent item : iPage.getRecords()) {
            RegionAgentListedVo vo = new RegionAgentListedVo();
            vo.setId(item.getId());
            vo.setUserId(item.getUserId());
            vo.setRegionId(item.getRegionId());
            vo.setRegionLevel(item.getRegionLevel());
            vo.setRegionLevelName(REGION_LEVEL_MAP.getOrDefault(item.getRegionLevel(), "未知"));
            vo.setRatio(item.getRatio());
            vo.setStatus(item.getStatus());
            vo.setCreateTime(TimeUtils.timestampToDate(item.getCreateTime()));
            if (item.getExpireTime() != null && item.getExpireTime() > 0) {
                vo.setExpireTime(TimeUtils.timestampToDate(item.getExpireTime()));
            } else {
                vo.setExpireTime("永久");
            }

            User user = userMapper.selectById(item.getUserId());
            if (user != null) {
                vo.setUserNickname(user.getNickname());
                vo.setUserAvatar(UrlUtils.toAbsoluteUrl(user.getAvatar()));
                vo.setUserMobile(user.getMobile());
            }

            DevRegion region = devRegionMapper.selectById(item.getRegionId());
            if (region != null) {
                vo.setRegionName(region.getName());
            }

            list.add(vo);
        }

        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), list);
    }

    @Override
    public void add(RegionAgentCreateValidate createValidate) {
        User user = userMapper.selectOne(new QueryWrapper<User>()
                .eq("id", createValidate.getUserId())
                .eq("is_delete", 0)
                .last("limit 1"));
        Assert.notNull(user, "用户不存在");

        // 检查该用户是否已是该区域代理
        RegionAgent exist = regionAgentMapper.selectOne(new QueryWrapper<RegionAgent>()
                .eq("user_id", createValidate.getUserId())
                .eq("region_id", createValidate.getRegionId())
                .eq("delete_time", 0)
                .last("limit 1"));
        if (exist != null) {
            throw new OperateException("该用户已是此区域代理");
        }

        RegionAgent agent = new RegionAgent();
        agent.setUserId(createValidate.getUserId());
        agent.setRegionId(createValidate.getRegionId());
        agent.setRegionLevel(createValidate.getRegionLevel());
        agent.setRatio(createValidate.getRatio());
        agent.setExpireTime(createValidate.getExpireTime() != null ? createValidate.getExpireTime() : 0L);
        agent.setStatus(createValidate.getStatus() != null ? createValidate.getStatus() : 1);
        agent.setCreateTime(System.currentTimeMillis() / 1000);
        agent.setUpdateTime(System.currentTimeMillis() / 1000);
        agent.setDeleteTime(0L);
        regionAgentMapper.insert(agent);
    }

    @Override
    public void edit(RegionAgentUpdateValidate updateValidate) {
        RegionAgent agent = regionAgentMapper.selectOne(new QueryWrapper<RegionAgent>()
                .eq("id", updateValidate.getId())
                .eq("delete_time", 0)
                .last("limit 1"));
        Assert.notNull(agent, "代理信息不存在");

        agent.setRegionId(updateValidate.getRegionId());
        agent.setRegionLevel(updateValidate.getRegionLevel());
        agent.setRatio(updateValidate.getRatio());
        agent.setExpireTime(updateValidate.getExpireTime() != null ? updateValidate.getExpireTime() : 0L);
        if (updateValidate.getStatus() != null) {
            agent.setStatus(updateValidate.getStatus());
        }
        agent.setUpdateTime(System.currentTimeMillis() / 1000);
        regionAgentMapper.updateById(agent);
    }

    @Override
    public RegionAgentDetailVo detail(Integer id) {
        RegionAgent agent = regionAgentMapper.selectOne(new QueryWrapper<RegionAgent>()
                .eq("id", id)
                .eq("delete_time", 0)
                .last("limit 1"));
        Assert.notNull(agent, "代理信息不存在");

        RegionAgentDetailVo vo = new RegionAgentDetailVo();
        vo.setId(agent.getId());
        vo.setUserId(agent.getUserId());
        vo.setRegionId(agent.getRegionId());
        vo.setRegionLevel(agent.getRegionLevel());
        vo.setRatio(agent.getRatio());
        vo.setExpireTime(agent.getExpireTime());
        vo.setStatus(agent.getStatus());

        User user = userMapper.selectById(agent.getUserId());
        if (user != null) {
            vo.setUserNickname(user.getNickname());
        }

        return vo;
    }

    @Override
    public void delete(Integer id) {
        RegionAgent agent = regionAgentMapper.selectOne(new QueryWrapper<RegionAgent>()
                .eq("id", id)
                .eq("delete_time", 0)
                .last("limit 1"));
        Assert.notNull(agent, "代理信息不存在");

        agent.setDeleteTime(System.currentTimeMillis() / 1000);
        agent.setUpdateTime(System.currentTimeMillis() / 1000);
        regionAgentMapper.updateById(agent);
    }

    private List<Integer> getUserIdsByKeyword(String keyword) {
        List<User> users = userMapper.selectList(new QueryWrapper<User>()
                .eq("is_delete", 0)
                .and(w -> w.like("nickname", keyword)
                        .or().like("mobile", keyword)));
        List<Integer> ids = new ArrayList<>();
        for (User u : users) {
            ids.add(u.getId());
        }
        return ids;
    }
}
