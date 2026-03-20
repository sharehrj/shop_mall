package com.mdd.admin.service;

import com.mdd.admin.validate.identity.RegionAgentCreateValidate;
import com.mdd.admin.validate.identity.RegionAgentSearchValidate;
import com.mdd.admin.validate.identity.RegionAgentUpdateValidate;
import com.mdd.admin.vo.identity.RegionAgentDetailVo;
import com.mdd.admin.vo.identity.RegionAgentListedVo;
import com.mdd.common.core.PageResult;

public interface IRegionAgentService {

    PageResult<RegionAgentListedVo> list(RegionAgentSearchValidate searchValidate);

    void add(RegionAgentCreateValidate createValidate);

    void edit(RegionAgentUpdateValidate updateValidate);

    RegionAgentDetailVo detail(Integer id);

    void delete(Integer id);
}
