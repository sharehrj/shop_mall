package com.mdd.admin.validate.identity;

import com.mdd.admin.validate.commons.PageValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("分佣记录查询参数")
public class CommissionRecordSearchValidate extends PageValidate implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("关键词（用户昵称/手机号）")
    private String keyword;

    @ApiModelProperty("佣金类型: [1=直推, 2=复购, 3=价差, 4=培育, 5=区域分红]")
    private Integer commissionType;

    @ApiModelProperty("状态: [0=待结算, 1=已结算, 2=已取消]")
    private Integer status;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;
}
