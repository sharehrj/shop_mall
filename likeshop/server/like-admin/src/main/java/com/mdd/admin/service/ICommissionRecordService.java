package com.mdd.admin.service;

import com.mdd.admin.validate.identity.CommissionRecordSearchValidate;
import com.mdd.admin.vo.identity.CommissionRecordListedVo;
import com.mdd.common.core.PageResult;

public interface ICommissionRecordService {

    PageResult<CommissionRecordListedVo> list(CommissionRecordSearchValidate searchValidate);
}
