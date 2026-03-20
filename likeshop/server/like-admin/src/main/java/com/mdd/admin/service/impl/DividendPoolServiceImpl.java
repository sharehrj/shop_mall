package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.service.IDividendPoolService;
import com.mdd.admin.validate.identity.DividendPoolSearchValidate;
import com.mdd.admin.vo.identity.DividendPoolListedVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.identity.DividendPool;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.identity.DividendPoolMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class DividendPoolServiceImpl implements IDividendPoolService {

    @Resource
    DividendPoolMapper dividendPoolMapper;

    @Override
    public PageResult<DividendPoolListedVo> list(DividendPoolSearchValidate searchValidate) {
        QueryWrapper<DividendPool> wrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(searchValidate.getPeriod())) {
            wrapper.eq("period", searchValidate.getPeriod());
        }
        if (searchValidate.getStatus() != null) {
            wrapper.eq("status", searchValidate.getStatus());
        }
        wrapper.orderByDesc("id");

        IPage<DividendPool> iPage = dividendPoolMapper.selectPage(
                new Page<>(searchValidate.getPageNo(), searchValidate.getPageSize()), wrapper);

        List<DividendPoolListedVo> list = new ArrayList<>();
        for (DividendPool item : iPage.getRecords()) {
            DividendPoolListedVo vo = new DividendPoolListedVo();
            vo.setId(item.getId());
            vo.setPeriod(item.getPeriod());
            vo.setTotalSales(item.getTotalSales());
            vo.setPoolRatio(item.getPoolRatio());
            vo.setPoolMoney(item.getPoolMoney());
            vo.setStatus(item.getStatus());
            vo.setStatusName(item.getStatus() == 1 ? "已分配" : "待分配");
            vo.setCreateTime(TimeUtils.timestampToDate(item.getCreateTime()));
            if (item.getSettleTime() != null && item.getSettleTime() > 0) {
                vo.setSettleTime(TimeUtils.timestampToDate(item.getSettleTime()));
            }
            list.add(vo);
        }

        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), list);
    }

    @Override
    public void settle(Integer id) {
        DividendPool pool = dividendPoolMapper.selectOne(new QueryWrapper<DividendPool>()
                .eq("id", id)
                .last("limit 1"));
        Assert.notNull(pool, "分红池记录不存在");

        if (pool.getStatus() == 1) {
            throw new OperateException("该期次已分配，不可重复操作");
        }

        pool.setStatus(1);
        pool.setSettleTime(System.currentTimeMillis() / 1000);
        pool.setUpdateTime(System.currentTimeMillis() / 1000);
        dividendPoolMapper.updateById(pool);
    }
}
