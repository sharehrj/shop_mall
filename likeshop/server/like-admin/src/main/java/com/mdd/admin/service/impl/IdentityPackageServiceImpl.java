package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.service.IIdentityPackageService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.identity.IdentityPackageCreateValidate;
import com.mdd.admin.validate.identity.IdentityPackageUpdateValidate;
import com.mdd.admin.vo.identity.IdentityPackageDetailVo;
import com.mdd.admin.vo.identity.IdentityPackageListedVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.distribution.DistributionLevel;
import com.mdd.common.entity.goods.Goods;
import com.mdd.common.entity.identity.IdentityPackage;
import com.mdd.common.mapper.distribution.DistributionLevelMapper;
import com.mdd.common.mapper.goods.GoodsMapper;
import com.mdd.common.mapper.identity.IdentityPackageMapper;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.UrlUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class IdentityPackageServiceImpl implements IIdentityPackageService {

    @Resource
    IdentityPackageMapper identityPackageMapper;

    @Resource
    GoodsMapper goodsMapper;

    @Resource
    DistributionLevelMapper distributionLevelMapper;

    @Override
    public PageResult<IdentityPackageListedVo> list(PageValidate pageValidate) {
        IPage<IdentityPackage> iPage = identityPackageMapper.selectPage(
                new Page<>(pageValidate.getPageNo(), pageValidate.getPageSize()),
                new QueryWrapper<IdentityPackage>()
                        .eq("delete_time", 0)
                        .orderByAsc("sort")
                        .orderByDesc("id"));

        List<IdentityPackageListedVo> list = new ArrayList<>();
        for (IdentityPackage item : iPage.getRecords()) {
            IdentityPackageListedVo vo = new IdentityPackageListedVo();
            vo.setId(item.getId());
            vo.setGoodsId(item.getGoodsId());
            vo.setLevelId(item.getLevelId());
            vo.setIsRenew(item.getIsRenew());
            vo.setSort(item.getSort());
            vo.setStatus(item.getStatus());
            vo.setCreateTime(TimeUtils.timestampToDate(item.getCreateTime()));

            Goods goods = goodsMapper.selectOne(new QueryWrapper<Goods>()
                    .eq("id", item.getGoodsId())
                    .eq("is_delete", 0)
                    .last("limit 1"));
            if (goods != null) {
                vo.setGoodsName(goods.getName());
                vo.setGoodsImage(UrlUtils.toAbsoluteUrl(goods.getImage()));
                vo.setGoodsMinPrice(goods.getMinPrice() != null ? goods.getMinPrice().toPlainString() : "0.00");
            }

            DistributionLevel level = distributionLevelMapper.selectOne(new QueryWrapper<DistributionLevel>()
                    .eq("id", item.getLevelId())
                    .eq("is_delete", 0)
                    .last("limit 1"));
            if (level != null) {
                vo.setLevelName(level.getName());
            }

            list.add(vo);
        }

        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), list);
    }

    @Override
    public void add(IdentityPackageCreateValidate createValidate) {
        Goods goods = goodsMapper.selectOne(new QueryWrapper<Goods>()
                .eq("id", createValidate.getGoodsId())
                .eq("is_delete", 0)
                .last("limit 1"));
        Assert.notNull(goods, "商品不存在");

        DistributionLevel level = distributionLevelMapper.selectOne(new QueryWrapper<DistributionLevel>()
                .eq("id", createValidate.getLevelId())
                .eq("is_delete", 0)
                .last("limit 1"));
        Assert.notNull(level, "等级不存在");

        IdentityPackage pkg = new IdentityPackage();
        pkg.setGoodsId(createValidate.getGoodsId());
        pkg.setLevelId(createValidate.getLevelId());
        pkg.setIsRenew(createValidate.getIsRenew());
        pkg.setSort(createValidate.getSort() != null ? createValidate.getSort() : 0);
        pkg.setStatus(createValidate.getStatus() != null ? createValidate.getStatus() : 1);
        pkg.setCreateTime(System.currentTimeMillis() / 1000);
        pkg.setUpdateTime(System.currentTimeMillis() / 1000);
        pkg.setDeleteTime(0L);
        identityPackageMapper.insert(pkg);
    }

    @Override
    public void edit(IdentityPackageUpdateValidate updateValidate) {
        IdentityPackage pkg = identityPackageMapper.selectOne(new QueryWrapper<IdentityPackage>()
                .eq("id", updateValidate.getId())
                .eq("delete_time", 0)
                .last("limit 1"));
        Assert.notNull(pkg, "套餐信息不存在");

        pkg.setGoodsId(updateValidate.getGoodsId());
        pkg.setLevelId(updateValidate.getLevelId());
        pkg.setIsRenew(updateValidate.getIsRenew());
        pkg.setSort(updateValidate.getSort() != null ? updateValidate.getSort() : 0);
        pkg.setStatus(updateValidate.getStatus() != null ? updateValidate.getStatus() : 1);
        pkg.setUpdateTime(System.currentTimeMillis() / 1000);
        identityPackageMapper.updateById(pkg);
    }

    @Override
    public IdentityPackageDetailVo detail(Integer id) {
        IdentityPackage pkg = identityPackageMapper.selectOne(new QueryWrapper<IdentityPackage>()
                .eq("id", id)
                .eq("delete_time", 0)
                .last("limit 1"));
        Assert.notNull(pkg, "套餐信息不存在");

        IdentityPackageDetailVo vo = new IdentityPackageDetailVo();
        vo.setId(pkg.getId());
        vo.setGoodsId(pkg.getGoodsId());
        vo.setLevelId(pkg.getLevelId());
        vo.setIsRenew(pkg.getIsRenew());
        vo.setSort(pkg.getSort());
        vo.setStatus(pkg.getStatus());
        return vo;
    }

    @Override
    public void delete(Integer id) {
        IdentityPackage pkg = identityPackageMapper.selectOne(new QueryWrapper<IdentityPackage>()
                .eq("id", id)
                .eq("delete_time", 0)
                .last("limit 1"));
        Assert.notNull(pkg, "套餐信息不存在");

        pkg.setDeleteTime(System.currentTimeMillis() / 1000);
        pkg.setUpdateTime(System.currentTimeMillis() / 1000);
        identityPackageMapper.updateById(pkg);
    }
}
