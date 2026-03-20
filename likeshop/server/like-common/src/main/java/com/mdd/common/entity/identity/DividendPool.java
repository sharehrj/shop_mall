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
@ApiModel("分红池月度记录实体")
@TableName("la_dividend_pool")
public class DividendPool implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty("期次，格式 YYYY-MM")
    private String period;

    @ApiModelProperty("当期总销售额")
    private BigDecimal totalSales;

    @ApiModelProperty("分红池提取比例(%)")
    private BigDecimal poolRatio;

    @ApiModelProperty("分红池金额")
    private BigDecimal poolMoney;

    @ApiModelProperty("状态: [0=待分配, 1=已分配]")
    private Integer status;

    @ApiModelProperty("分配时间")
    private Long settleTime;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("更新时间")
    private Long updateTime;
}
