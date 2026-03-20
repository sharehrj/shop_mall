package com.mdd.admin.vo.identity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("身份套餐列表Vo")
public class IdentityPackageListedVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty("关联商品ID")
    private Integer goodsId;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品封面图")
    private String goodsImage;

    @ApiModelProperty("商品价格")
    private String goodsMinPrice;

    @ApiModelProperty("升级目标等级ID")
    private Integer levelId;

    @ApiModelProperty("等级名称")
    private String levelName;

    @ApiModelProperty("是否续费包: [0=首购, 1=续费]")
    private Integer isRenew;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("状态: [0=下架, 1=上架]")
    private Integer status;

    @ApiModelProperty("创建时间")
    private String createTime;
}
