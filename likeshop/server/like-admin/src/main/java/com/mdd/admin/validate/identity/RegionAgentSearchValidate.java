package com.mdd.admin.validate.identity;

import com.mdd.admin.validate.commons.PageValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("区域代理查询参数")
public class RegionAgentSearchValidate extends PageValidate implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("关键词（用户昵称/手机号）")
    private String keyword;

    @ApiModelProperty("区域级别: [1=省, 2=市, 3=区县]")
    private Integer regionLevel;

    @ApiModelProperty("状态: [0=禁用, 1=启用]")
    private Integer status;
}
