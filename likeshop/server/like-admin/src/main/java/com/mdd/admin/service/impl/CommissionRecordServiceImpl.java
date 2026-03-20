package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.service.ICommissionRecordService;
import com.mdd.admin.validate.identity.CommissionRecordSearchValidate;
import com.mdd.admin.vo.identity.CommissionRecordListedVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.distribution.DistributionLevel;
import com.mdd.common.entity.identity.CommissionRecord;
import com.mdd.common.entity.user.User;
import com.mdd.common.mapper.distribution.DistributionLevelMapper;
import com.mdd.common.mapper.identity.CommissionRecordMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.UrlUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommissionRecordServiceImpl implements ICommissionRecordService {

    private static final Map<Integer, String> COMMISSION_TYPE_MAP = new HashMap<>();
    private static final Map<Integer, String> STATUS_MAP = new HashMap<>();

    static {
        COMMISSION_TYPE_MAP.put(1, "直推");
        COMMISSION_TYPE_MAP.put(2, "复购");
        COMMISSION_TYPE_MAP.put(3, "价差");
        COMMISSION_TYPE_MAP.put(4, "培育");
        COMMISSION_TYPE_MAP.put(5, "区域分红");

        STATUS_MAP.put(0, "待结算");
        STATUS_MAP.put(1, "已结算");
        STATUS_MAP.put(2, "已取消");
    }

    @Resource
    CommissionRecordMapper commissionRecordMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    DistributionLevelMapper distributionLevelMapper;

    @Override
    public PageResult<CommissionRecordListedVo> list(CommissionRecordSearchValidate searchValidate) {
        QueryWrapper<CommissionRecord> wrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(searchValidate.getKeyword())) {
            List<Integer> userIds = getUserIdsByKeyword(searchValidate.getKeyword());
            if (userIds.isEmpty()) {
                return PageResult.iPageHandle(0L, (long) searchValidate.getPageNo(),
                        (long) searchValidate.getPageSize(), new ArrayList<>());
            }
            wrapper.in("user_id", userIds);
        }
        if (searchValidate.getCommissionType() != null) {
            wrapper.eq("commission_type", searchValidate.getCommissionType());
        }
        if (searchValidate.getStatus() != null) {
            wrapper.eq("status", searchValidate.getStatus());
        }
        if (StringUtils.isNotBlank(searchValidate.getStartTime())) {
            wrapper.ge("create_time", TimeUtils.dateToTimestamp(searchValidate.getStartTime()));
        }
        if (StringUtils.isNotBlank(searchValidate.getEndTime())) {
            wrapper.le("create_time", TimeUtils.dateToTimestamp(searchValidate.getEndTime()));
        }
        wrapper.orderByDesc("id");

        IPage<CommissionRecord> iPage = commissionRecordMapper.selectPage(
                new Page<>(searchValidate.getPageNo(), searchValidate.getPageSize()), wrapper);

        List<CommissionRecordListedVo> list = new ArrayList<>();
        for (CommissionRecord item : iPage.getRecords()) {
            CommissionRecordListedVo vo = new CommissionRecordListedVo();
            vo.setId(item.getId());
            vo.setUserId(item.getUserId());
            vo.setFromUserId(item.getFromUserId());
            vo.setOrderId(item.getOrderId());
            vo.setCommissionType(item.getCommissionType());
            vo.setCommissionTypeName(COMMISSION_TYPE_MAP.getOrDefault(item.getCommissionType(), "未知"));
            vo.setRatio(item.getRatio());
            vo.setGoodsMoney(item.getGoodsMoney());
            vo.setCommissionMoney(item.getCommissionMoney());
            vo.setStatus(item.getStatus());
            vo.setStatusName(STATUS_MAP.getOrDefault(item.getStatus(), "未知"));
            vo.setCreateTime(TimeUtils.timestampToDate(item.getCreateTime()));
            if (item.getSettleTime() != null && item.getSettleTime() > 0) {
                vo.setSettleTime(TimeUtils.timestampToDate(item.getSettleTime()));
            }

            // 获佣用户信息
            User user = userMapper.selectById(item.getUserId());
            if (user != null) {
                vo.setUserNickname(user.getNickname());
                vo.setUserAvatar(UrlUtils.toAbsoluteUrl(user.getAvatar()));
                vo.setUserMobile(user.getMobile());
            }

            // 消费用户信息
            if (item.getFromUserId() != null && item.getFromUserId() > 0) {
                User fromUser = userMapper.selectById(item.getFromUserId());
                if (fromUser != null) {
                    vo.setFromUserNickname(fromUser.getNickname());
                }
            }

            // 等级名称
            if (item.getLevelId() != null && item.getLevelId() > 0) {
                DistributionLevel level = distributionLevelMapper.selectById(item.getLevelId());
                if (level != null) {
                    vo.setLevelName(level.getName());
                }
            }

            list.add(vo);
        }

        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), list);
    }

    private List<Integer> getUserIdsByKeyword(String keyword) {
        List<User> users = userMapper.selectList(new QueryWrapper<User>()
                .eq("is_delete", 0)
                .and(w -> w.like("nickname", keyword)
                        .or().like("mobile", keyword)
                        .or().like("username", keyword)));
        List<Integer> ids = new ArrayList<>();
        for (User u : users) {
            ids.add(u.getId());
        }
        return ids;
    }
}
