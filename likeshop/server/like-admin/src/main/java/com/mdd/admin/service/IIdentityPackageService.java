package com.mdd.admin.service;

import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.identity.IdentityPackageCreateValidate;
import com.mdd.admin.validate.identity.IdentityPackageUpdateValidate;
import com.mdd.admin.vo.identity.IdentityPackageDetailVo;
import com.mdd.admin.vo.identity.IdentityPackageListedVo;
import com.mdd.common.core.PageResult;

public interface IIdentityPackageService {

    PageResult<IdentityPackageListedVo> list(PageValidate pageValidate);

    void add(IdentityPackageCreateValidate createValidate);

    void edit(IdentityPackageUpdateValidate updateValidate);

    IdentityPackageDetailVo detail(Integer id);

    void delete(Integer id);
}
