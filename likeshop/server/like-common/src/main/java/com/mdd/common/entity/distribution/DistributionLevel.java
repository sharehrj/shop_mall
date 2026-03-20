package com.mdd.common.entity.distribution;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("分销会员等级实体")
public class DistributionLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value="id", type= IdType.AUTO)
    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty("等级名称")
    private String name;

    @ApiModelProperty("等级权重")
    private Integer weights;

    @ApiModelProperty("一级佣金比例")
    private Double firstRatio;

    @ApiModelProperty("二级佣金比例")
    private Double secondRatio;

    @ApiModelProperty("自购佣金比例")
    private Double selfRatio;

    @ApiModelProperty("等级图标")
    private String icon;

    @ApiModelProperty("等级背景图")
    private String image;

    @ApiModelProperty("是否默认等级: [0-否 1-是]")
    private Integer isDefault;

    @ApiModelProperty("等级描述")
    private String remark;

    @ApiModelProperty("等级标识码 如 VIP1/VIP2")
    private String levelCode;

    @ApiModelProperty("直推佣金比例(%)")
    private Double directRatio;

    @ApiModelProperty("复购佣金比例(%)")
    private Double repurchaseRatio;

    @ApiModelProperty("价差佣金比例(%)")
    private Double priceDiffRatio;

    @ApiModelProperty("培育佣金比例(%)")
    private Double cultivateRatio;

    @ApiModelProperty("升级类型: [1-OR关系 2-AND关系]")
    private Integer updateType;

    @ApiModelProperty("删除状态: [1=是 0=否]")
    private Integer isDelete;

    @ApiModelProperty("修改时间")
    private Long createTime;

    @ApiModelProperty("创建时间")
    private Long updateTime;

    @ApiModelProperty("删除时间")
    private Long deleteTime;
}
