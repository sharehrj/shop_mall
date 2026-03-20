package com.mdd.admin.validate.identity;

import com.mdd.common.validator.annotation.IDMust;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("区域代理编辑参数")
public class RegionAgentUpdateValidate implements Serializable {

    private static final long serialVersionUID = 1L;

    @IDMust(message = "id参数必传且需大于0")
    @ApiModelProperty(value = "ID", required = true)
    private Integer id;

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
    private Integer status;
}
