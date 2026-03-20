package com.mdd.admin.controller;

import com.alibaba.fastjson2.JSONObject;
import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.admin.aop.Log;
import com.mdd.admin.service.IFinanceWithdrawService;
import com.mdd.admin.service.ISettingPaymentService;
import com.mdd.admin.service.IUserService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.finance.FinanceWithdrawTransferValidate;
import com.mdd.admin.validate.user.*;
import com.mdd.admin.vo.user.InviterVo;
import com.mdd.admin.vo.user.UserVo;
import com.mdd.common.aop.NotLogin;
import com.mdd.common.aop.NotPower;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.setting.DevPayConfig;
import com.mdd.common.entity.withdraw.WithdrawApply;
import com.mdd.common.enums.PaymentEnum;
import com.mdd.common.exception.OperateException;
import com.mdd.common.plugin.wechat.AesUtil;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.RedisUtils;
import com.mdd.common.util.ToolUtils;
import com.mdd.common.validator.annotation.IDMust;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("api/wx")
@Api(tags = "微信数据管理")
public class WechatController {

    private static final Logger log = LoggerFactory.getLogger(WechatController.class);
    private static final String REDIS_LOG_KEY = "wechat:callback:logs";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Resource
    ISettingPaymentService iSettingPaymentService;

    @Resource
    IFinanceWithdrawService iFinanceWithdrawService;

    /**
     * 记录日志到Redis
     * @author damonyuan
     * @param level 日志级别
     * @param message 日志消息
     * @param requestId 请求唯一标识
     */
    private void logToRedis(String level, String message, String requestId) {
        try {
            String timestamp = LocalDateTime.now().format(formatter);
            String logEntry = String.format("[%s] [%s] %s", timestamp, level, message);
            String logKey = String.format("wechat:callback:logs:%s", requestId + ToolUtils.makeUUID());
            RedisUtils.set(logKey, logEntry, 86400L); // 保存24小时
        } catch (Exception e) {
            // 如果Redis记录失败，仍然使用原始日志
            log.error("Redis日志记录失败: {}", e.getMessage());
        }
    }

    @PostMapping("/withdrawNotify")
    @NotPower
    @NotLogin
    @ApiOperation("提现回调")
    public void withdrawNotify(@RequestBody String jsonData, HttpServletRequest request) {
        // 生成唯一的请求ID
        String requestId = System.currentTimeMillis() + "_" + Thread.currentThread().getId();

        try {
            logToRedis("INFO", "收到微信转账回调: " + jsonData, requestId);

            // 获取请求头信息（用于日志记录）
            String signature = request.getHeader("wechatpay-signature");
            String nonce = request.getHeader("wechatpay-nonce");
            String serial = request.getHeader("wechatpay-serial");
            String timestamp = request.getHeader("wechatpay-timestamp");

            logToRedis("INFO", String.format("回调头信息 - Serial: %s, Timestamp: %s, Nonce: %s", serial, timestamp, nonce), requestId);

            // 获取微信支付配置
            DevPayConfig devPayConfig = iSettingPaymentService.detail(PaymentEnum.WX_PAY.getCode());
            String apiV3Key = JSONObject.parseObject(JSONObject.toJSONString(devPayConfig.getParams())).getString("pay_sign_key");

            // 解析回调数据
            JSONObject callbackData = JSONObject.parseObject(jsonData);
            String eventType = callbackData.getString("event_type");

            // 验证事件类型
            if (!"MCHTRANSFER.BILL.FINISHED".equals(eventType)) {
                logToRedis("INFO", "非转账完成通知，忽略处理，事件类型: " + eventType, requestId);
                return;
            }

            // 解密数据
            JSONObject resource = callbackData.getJSONObject("resource");
            String decryptedData = decryptResource(resource, apiV3Key, requestId);
            logToRedis("INFO", "解密后的数据: " + decryptedData, requestId);

            JSONObject transferResult = JSONObject.parseObject(decryptedData);
            processTransferResult(transferResult, requestId);

            logToRedis("INFO", "微信转账回调处理完成", requestId);

        } catch (Exception e) {
            logToRedis("ERROR", "处理微信转账回调异常: " + e.getMessage(), requestId);
            e.printStackTrace();
        }
    }

    /**
     * 解密回调数据
     */
    private String decryptResource(JSONObject resource, String apiV3Key, String requestId) throws Exception {
        String ciphertext = resource.getString("ciphertext");
        String nonce = resource.getString("nonce");
        String associatedData = resource.getString("associated_data");

        logToRedis("INFO", String.format("开始解密 - AssociatedData: %s, Nonce: %s", associatedData, nonce), requestId);

        // 使用AES解密
        AesUtil aesUtil = new AesUtil(apiV3Key.getBytes(StandardCharsets.UTF_8));
        return aesUtil.decryptToString(
                associatedData.getBytes(StandardCharsets.UTF_8),
                nonce.getBytes(StandardCharsets.UTF_8),
                ciphertext
        );
    }

    /**
     * 处理转账结果
     */
    private void processTransferResult(JSONObject transferResult, String requestId) {
        try {
            String outBillNo = transferResult.getString("out_detail_no"); // 商户转账单号
            String status = transferResult.getString("status"); // 转账状态
            String transferBillNo = transferResult.getString("transfer_bill_no"); // 微信转账单号
            Integer transferAmount = transferResult.getInteger("transfer_amount"); // 转账金额（分）
            String openid = transferResult.getString("openid"); // 用户openid
            String mchId = transferResult.getString("mch_id"); // 商户号
            String failReason = transferResult.getString("fail_reason"); // 失败原因
            String createTime = transferResult.getString("create_time"); // 创建时间
            String updateTime = transferResult.getString("update_time"); // 更新时间

            logToRedis("INFO", String.format("处理转账结果 - 商户单号: %s, 状态: %s, 微信单号: %s, 商户号: %s", outBillNo, status, transferBillNo, mchId), requestId);

            // 根据状态更新提现记录
            switch (status) {
                case "SUCCESS":
                    logToRedis("INFO", String.format("转账成功 - 金额: %s分, 用户: %s, 创建时间: %s, 更新时间: %s", transferAmount, openid, createTime, updateTime), requestId);
                    updateWithdrawSuccess(outBillNo, transferBillNo, transferResult, requestId);
                    break;
                case "CANCELLED":
                    logToRedis("WARN", String.format("转账已取消 - 单号: %s, 更新时间: %s", outBillNo, updateTime), requestId);
                    updateWithdrawFailed(outBillNo, "转账已取消", transferResult, requestId);
                    break;
                case "FAIL":
                    logToRedis("ERROR", String.format("转账失败 - 单号: %s, 原因: %s, 更新时间: %s", outBillNo, failReason, updateTime), requestId);
                    updateWithdrawFailed(outBillNo, failReason, transferResult, requestId);
                    break;
                default:
                    logToRedis("WARN", String.format("未知转账状态: %s, 单号: %s", status, outBillNo), requestId);
                    break;
            }

        } catch (Exception e) {
            logToRedis("ERROR", "处理转账结果异常: " + e.getMessage(), requestId);
        }
    }

    /**
     * 更新提现成功状态
     */
    private void updateWithdrawSuccess(String outDetailNo, String detailId, JSONObject transferResult, String requestId) {
        WithdrawApply withdrawApply = iFinanceWithdrawService.getBySn(outDetailNo);
        if (StringUtils.isNull(withdrawApply)) {
            throw new OperateException("提现申请不存在");
        }
        FinanceWithdrawTransferValidate transferValidate = new FinanceWithdrawTransferValidate();
        transferValidate.setId(withdrawApply.getId());
        transferValidate.setStatus(1);
        iFinanceWithdrawService.transfer(transferValidate);

        logToRedis("INFO", String.format("更新提现成功: %s -> %s", outDetailNo, detailId), requestId);
    }

    /**
     * 更新提现失败状态
     */
    private void updateWithdrawFailed(String outDetailNo, String failReason, JSONObject transferResult, String requestId) {
        WithdrawApply withdrawApply = iFinanceWithdrawService.getBySn(outDetailNo);
        if (StringUtils.isNull(withdrawApply)) {
            throw new OperateException("提现申请不存在");
        }
        FinanceWithdrawTransferValidate transferValidate = new FinanceWithdrawTransferValidate();
        transferValidate.setId(withdrawApply.getId());
        transferValidate.setStatus(2);
        transferValidate.setTransferRemark(failReason);
        iFinanceWithdrawService.transfer(transferValidate);

        logToRedis("INFO", String.format("更新提现失败: %s -> %s", outDetailNo, failReason), requestId);
    }

}