package com.mdd.admin.validate.identity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("区域代理新增参数")
public class RegionAgentCreateValidate implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "用户ID不能为空")
    @ApiModelProperty(value = "代理用户ID", required = true)
    private Integer userId;

    @NotNull(message = "区域ID不能为空")
    @ApiModelProperty(value = "区域ID", required = true)
    private Integer regionId;

    @NotNull(message = "区域级别不能为空")
    @ApiModelProperty(value = "区域级别: [1=省, 2=市, 3=区县]", required = true)
    private Integer regionLevel;

    @NotNull(message = "分红比例不能为空")
    @ApiModelProperty(value = "区域分红比例(%)", required = true)
    private BigDecimal ratio;

    @ApiModelProperty("到期时间，0=永久")
    private Long expireTime = 0L;

    @ApiModelProperty("状态: [0=禁用, 1=启用]")
    private Integer status = 1;
}
