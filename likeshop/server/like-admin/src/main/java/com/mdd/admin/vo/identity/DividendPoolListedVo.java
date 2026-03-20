package com.mdd.admin.vo.identity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("分红池列表Vo")
public class DividendPoolListedVo implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty("状态名称")
    private String statusName;

    @ApiModelProperty("分配时间")
    private String settleTime;

    @ApiModelProperty("创建时间")
    private String createTime;
}
