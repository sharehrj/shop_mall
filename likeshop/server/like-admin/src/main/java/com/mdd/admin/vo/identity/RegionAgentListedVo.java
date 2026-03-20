package com.mdd.admin.vo.identity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("区域代理列表Vo")
public class RegionAgentListedVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty("代理用户ID")
    private Integer userId;

    @ApiModelProperty("用户昵称")
    private String userNickname;

    @ApiModelProperty("用户头像")
    private String userAvatar;

    @ApiModelProperty("用户手机号")
    private String userMobile;

    @ApiModelProperty("区域ID")
    private Integer regionId;

    @ApiModelProperty("区域名称")
    private String regionName;

    @ApiModelProperty("区域级别: [1=省, 2=市, 3=区县]")
    private Integer regionLevel;

    @ApiModelProperty("区域级别名称")
    private String regionLevelName;

    @ApiModelProperty("区域分红比例(%)")
    private BigDecimal ratio;

    @ApiModelProperty("到期时间")
    private String expireTime;

    @ApiModelProperty("状态: [0=禁用, 1=启用]")
    private Integer status;

    @ApiModelProperty("创建时间")
    private String createTime;
}
