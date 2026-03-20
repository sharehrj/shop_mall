package com.mdd.admin.controller.identity;

import com.mdd.admin.service.IDividendPoolService;
import com.mdd.admin.validate.commons.IdValidate;
import com.mdd.admin.validate.identity.DividendPoolSearchValidate;
import com.mdd.admin.vo.identity.DividendPoolListedVo;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("api/identity/dividend-pool")
@Api(tags = "分红池管理")
public class DividendPoolController {

    @Resource
    IDividendPoolService iDividendPoolService;

    @GetMapping("/list")
    @ApiOperation("分红池列表")
    public AjaxResult<PageResult<DividendPoolListedVo>> list(@Validated DividendPoolSearchValidate searchValidate) {
        PageResult<DividendPoolListedVo> list = iDividendPoolService.list(searchValidate);
        return AjaxResult.success(list);
    }

    @PostMapping("/settle")
    @ApiOperation("执行分配")
    public AjaxResult<Object> settle(@Validated @RequestBody IdValidate idValidate) {
        iDividendPoolService.settle(idValidate.getId());
        return AjaxResult.success();
    }
}
