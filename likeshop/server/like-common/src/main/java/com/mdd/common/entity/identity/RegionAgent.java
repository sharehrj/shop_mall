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
@ApiModel("区域代理实体")
@TableName("la_region_agent")
public class RegionAgent implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty("代理用户ID")
    private Integer userId;

    @ApiModelProperty("区域ID")
    private Integer regionId;

    @ApiModelProperty("区域级别: [1=省, 2=市, 3=区县]")
    private Integer regionLevel;

    @ApiModelProperty("区域分红比例(%)")
    private BigDecimal ratio;

    @ApiModelProperty("到期时间，0=永久")
    private Long expireTime;

    @ApiModelProperty("状态: [0=禁用, 1=启用]")
    private Integer status;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("更新时间")
    private Long updateTime;

    @ApiModelProperty("删除时间")
    private Long deleteTime;
}
