package com.mdd.front.validate.withdraw;

import com.mdd.common.validator.annotation.IDMust;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("提现接收确认参数")
public class WithdrawApplyConfirmValidate implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "id参数必传")
    @IDMust(message = "id参数必传且需大于0")
    @ApiModelProperty(value = "提现申请ID")
    private Integer id;

    @ApiModelProperty(value = "用户ID")
    private Integer userId;
}