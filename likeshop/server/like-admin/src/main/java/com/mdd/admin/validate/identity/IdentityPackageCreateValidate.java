package com.mdd.admin.validate.identity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("身份套餐新增参数")
public class IdentityPackageCreateValidate implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "商品ID不能为空")
    @ApiModelProperty(value = "关联商品ID", required = true)
    private Integer goodsId;

    @NotNull(message = "等级ID不能为空")
    @ApiModelProperty(value = "升级目标等级ID", required = true)
    private Integer levelId;

    @NotNull(message = "套餐类型不能为空")
    @ApiModelProperty(value = "是否续费包: [0=首购, 1=续费]", required = true)
    private Integer isRenew;

    @ApiModelProperty("排序")
    private Integer sort = 0;

    @ApiModelProperty("状态: [0=下架, 1=上架]")
    private Integer status = 1;
}
