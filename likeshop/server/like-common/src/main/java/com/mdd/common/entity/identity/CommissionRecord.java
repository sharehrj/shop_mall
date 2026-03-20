package com.mdd.common.entity.identity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("分佣记录实体")
@TableName("la_commission_record")
public class CommissionRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty("获佣用户ID")
    private Integer userId;

    @ApiModelProperty("消费用户ID")
    private Integer fromUserId;

    @ApiModelProperty("来源订单ID")
    private Integer orderId;

    @ApiModelProperty("来源订单商品ID")
    private Integer orderGoodsId;

    @ApiModelProperty("佣金类型: [1=直推, 2=复购, 3=价差, 4=培育, 5=区域分红]")
    private Integer commissionType;

    @ApiModelProperty("获佣时的等级ID")
    private Integer levelId;

    @ApiModelProperty("佣金比例(%)")
    private BigDecimal ratio;

    @ApiModelProperty("商品金额")
    private BigDecimal goodsMoney;

    @ApiModelProperty("佣金金额")
    private BigDecimal commissionMoney;

    @ApiModelProperty("状态: [0=待结算, 1=已结算, 2=已取消]")
    private Integer status;

    @ApiModelProperty("结算时间")
    private Long settleTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("更新时间")
    private Long updateTime;
}
