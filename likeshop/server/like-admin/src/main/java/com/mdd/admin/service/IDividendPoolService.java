package com.mdd.admin.service;

import com.mdd.admin.validate.identity.DividendPoolSearchValidate;
import com.mdd.admin.vo.identity.DividendPoolListedVo;
import com.mdd.common.core.PageResult;

public interface IDividendPoolService {

    PageResult<DividendPoolListedVo> list(DividendPoolSearchValidate searchValidate);

    void settle(Integer id);
}
