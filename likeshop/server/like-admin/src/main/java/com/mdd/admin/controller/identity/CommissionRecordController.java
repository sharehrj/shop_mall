package com.mdd.admin.controller.identity;

import com.mdd.admin.service.ICommissionRecordService;
import com.mdd.admin.validate.identity.CommissionRecordSearchValidate;
import com.mdd.admin.vo.identity.CommissionRecordListedVo;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("api/identity/commission")
@Api(tags = "分佣记录管理")
public class CommissionRecordController {

    @Resource
    ICommissionRecordService iCommissionRecordService;

    @GetMapping("/list")
    @ApiOperation("分佣记录列表")
    public AjaxResult<PageResult<CommissionRecordListedVo>> list(@Validated CommissionRecordSearchValidate searchValidate) {
        PageResult<CommissionRecordListedVo> list = iCommissionRecordService.list(searchValidate);
        return AjaxResult.success(list);
    }
}
