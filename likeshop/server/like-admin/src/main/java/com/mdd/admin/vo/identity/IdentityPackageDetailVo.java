package com.mdd.admin.vo.identity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("身份套餐详情Vo")
public class IdentityPackageDetailVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty("关联商品ID")
    private Integer goodsId;

    @ApiModelProperty("升级目标等级ID")
    private Integer levelId;

    @ApiModelProperty("是否续费包: [0=首购, 1=续费]")
    private Integer isRenew;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("状态: [0=下架, 1=上架]")
    private Integer status;
}
