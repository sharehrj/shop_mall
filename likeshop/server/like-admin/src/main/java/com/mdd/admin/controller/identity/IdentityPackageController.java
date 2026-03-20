package com.mdd.admin.controller.identity;

import com.mdd.admin.service.IIdentityPackageService;
import com.mdd.admin.validate.commons.IdValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.identity.IdentityPackageCreateValidate;
import com.mdd.admin.validate.identity.IdentityPackageUpdateValidate;
import com.mdd.admin.vo.identity.IdentityPackageDetailVo;
import com.mdd.admin.vo.identity.IdentityPackageListedVo;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.validator.annotation.IDMust;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("api/identity/package")
@Api(tags = "身份套餐管理")
public class IdentityPackageController {

    @Resource
    IIdentityPackageService iIdentityPackageService;

    @GetMapping("/list")
    @ApiOperation("套餐列表")
    public AjaxResult<PageResult<IdentityPackageListedVo>> list(@Validated PageValidate pageValidate) {
        PageResult<IdentityPackageListedVo> list = iIdentityPackageService.list(pageValidate);
        return AjaxResult.success(list);
    }

    @PostMapping("/add")
    @ApiOperation("套餐新增")
    public AjaxResult<Object> add(@Validated @RequestBody IdentityPackageCreateValidate createValidate) {
        iIdentityPackageService.add(createValidate);
        return AjaxResult.success();
    }

    @PostMapping("/edit")
    @ApiOperation("套餐编辑")
    public AjaxResult<Object> edit(@Validated @RequestBody IdentityPackageUpdateValidate updateValidate) {
        iIdentityPackageService.edit(updateValidate);
        return AjaxResult.success();
    }

    @GetMapping("/detail")
    @ApiOperation("套餐详情")
    public AjaxResult<IdentityPackageDetailVo> detail(@Validated @IDMust() @RequestParam("id") Integer id) {
        IdentityPackageDetailVo vo = iIdentityPackageService.detail(id);
        return AjaxResult.success(vo);
    }

    @PostMapping("/del")
    @ApiOperation("套餐删除")
    public AjaxResult<Object> delete(@Validated @RequestBody IdValidate idValidate) {
        iIdentityPackageService.delete(idValidate.getId());
        return AjaxResult.success();
    }
}
