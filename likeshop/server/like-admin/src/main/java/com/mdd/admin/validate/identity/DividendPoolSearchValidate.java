package com.mdd.admin.validate.identity;

import com.mdd.admin.validate.commons.PageValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("分红池查询参数")
public class DividendPoolSearchValidate extends PageValidate implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("期次，格式 YYYY-MM")
    private String period;

    @ApiModelProperty("状态: [0=待分配, 1=已分配]")
    private Integer status;
}
