package com.mdd.admin.vo.identity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("分佣记录列表Vo")
public class CommissionRecordListedVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty("获佣用户ID")
    private Integer userId;

    @ApiModelProperty("获佣用户昵称")
    private String userNickname;

    @ApiModelProperty("获佣用户头像")
    private String userAvatar;

    @ApiModelProperty("获佣用户手机号")
    private String userMobile;

    @ApiModelProperty("消费用户ID")
    private Integer fromUserId;

    @ApiModelProperty("消费用户昵称")
    private String fromUserNickname;

    @ApiModelProperty("来源订单ID")
    private Integer orderId;

    @ApiModelProperty("佣金类型: [1=直推, 2=复购, 3=价差, 4=培育, 5=区域分红]")
    private Integer commissionType;

    @ApiModelProperty("佣金类型名称")
    private String commissionTypeName;

    @ApiModelProperty("等级名称")
    private String levelName;

    @ApiModelProperty("佣金比例(%)")
    private BigDecimal ratio;

    @ApiModelProperty("商品金额")
    private BigDecimal goodsMoney;

    @ApiModelProperty("佣金金额")
    private BigDecimal commissionMoney;

    @ApiModelProperty("状态: [0=待结算, 1=已结算, 2=已取消]")
    private Integer status;

    @ApiModelProperty("状态名称")
    private String statusName;

    @ApiModelProperty("结算时间")
    private String settleTime;

    @ApiModelProperty("创建时间")
    private String createTime;
}
