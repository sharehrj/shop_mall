package com.mdd.common.entity.identity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("身份套餐实体")
@TableName("la_identity_package")
public class IdentityPackage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty("关联商品ID")
    private Integer goodsId;

    @ApiModelProperty("购买后升级到的等级ID")
    private Integer levelId;

    @ApiModelProperty("是否续费包: [0=首购, 1=续费]")
    private Integer isRenew;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("状态: [0=下架, 1=上架]")
    private Integer status;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("更新时间")
    private Long updateTime;

    @ApiModelProperty("删除时间")
    private Long deleteTime;
}
