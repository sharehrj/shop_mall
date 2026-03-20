package com.mdd.admin.controller.identity;

import com.mdd.admin.service.IRegionAgentService;
import com.mdd.admin.validate.commons.IdValidate;
import com.mdd.admin.validate.identity.RegionAgentCreateValidate;
import com.mdd.admin.validate.identity.RegionAgentSearchValidate;
import com.mdd.admin.validate.identity.RegionAgentUpdateValidate;
import com.mdd.admin.vo.identity.RegionAgentDetailVo;
import com.mdd.admin.vo.identity.RegionAgentListedVo;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.validator.annotation.IDMust;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("api/identity/region-agent")
@Api(tags = "区域代理管理")
public class RegionAgentController {

    @Resource
    IRegionAgentService iRegionAgentService;

    @GetMapping("/list")
    @ApiOperation("代理列表")
    public AjaxResult<PageResult<RegionAgentListedVo>> list(@Validated RegionAgentSearchValidate searchValidate) {
        PageResult<RegionAgentListedVo> list = iRegionAgentService.list(searchValidate);
        return AjaxResult.success(list);
    }

    @PostMapping("/add")
    @ApiOperation("新增代理")
    public AjaxResult<Object> add(@Validated @RequestBody RegionAgentCreateValidate createValidate) {
        iRegionAgentService.add(createValidate);
        return AjaxResult.success();
    }

    @PostMapping("/edit")
    @ApiOperation("编辑代理")
    public AjaxResult<Object> edit(@Validated @RequestBody RegionAgentUpdateValidate updateValidate) {
        iRegionAgentService.edit(updateValidate);
        return AjaxResult.success();
    }

    @GetMapping("/detail")
    @ApiOperation("代理详情")
    public AjaxResult<RegionAgentDetailVo> detail(@Validated @IDMust() @RequestParam("id") Integer id) {
        RegionAgentDetailVo vo = iRegionAgentService.detail(id);
        return AjaxResult.success(vo);
    }

    @PostMapping("/del")
    @ApiOperation("删除代理")
    public AjaxResult<Object> delete(@Validated @RequestBody IdValidate idValidate) {
        iRegionAgentService.delete(idValidate.getId());
        return AjaxResult.success();
    }
}
