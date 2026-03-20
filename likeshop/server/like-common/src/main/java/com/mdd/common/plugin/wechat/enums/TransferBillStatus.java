package com.mdd.common.plugin.wechat.enums;

import com.google.gson.annotations.SerializedName;

/**
 * 转账单状态枚举
 *
 * @author mjf
 */
public enum TransferBillStatus {
    
    @SerializedName("ACCEPTED")
    ACCEPTED("ACCEPTED", "已受理"),
    
    @SerializedName("PROCESSING")
    PROCESSING("PROCESSING", "处理中"),
    
    @SerializedName("WAIT_USER_CONFIRM")
    WAIT_USER_CONFIRM("WAIT_USER_CONFIRM", "等待用户确认"),
    
    @SerializedName("TRANSFERING")
    TRANSFERING("TRANSFERING", "转账中"),
    
    @SerializedName("SUCCESS")
    SUCCESS("SUCCESS", "转账成功"),
    
    @SerializedName("FAIL")
    FAIL("FAIL", "转账失败"),
    
    @SerializedName("CANCELING")
    CANCELING("CANCELING", "取消中"),
    
    @SerializedName("CANCELLED")
    CANCELLED("CANCELLED", "已取消");
    
    private final String code;
    private final String desc;
    
    TransferBillStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDesc() {
        return desc;
    }
}