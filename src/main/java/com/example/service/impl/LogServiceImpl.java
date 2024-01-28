package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bean.LogPO;
import com.example.dto.grid.Filter;
import com.example.dto.grid.Pager;
import com.example.exception.BaseBusinessException;
import com.example.mapper.LogDao;
import com.example.service.LogService;
import com.example.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl extends ServiceImpl<LogDao, LogPO> implements LogService {
    private LoginService loginService;

    public Pager<LogPO> getPage(Pager<LogPO> pager) throws BaseBusinessException {
        LambdaQueryWrapper<LogPO> lqw = new LambdaQueryWrapper<>();
        List<Filter> filters = pager.getFilters();
        for (Filter filter : filters) {
            if ("type".equals(filter.getFieldName())) {
                lqw.like(LogPO::getType, filter.getParValue1());
            }
        }
        lqw.and((wrapper) -> {
            wrapper.eq(LogPO::getGroupId, 0L).or().eq(LogPO::getGroupId, loginService.getCurrentGroupId());
        });
        lqw.and((wrapper) -> {
            wrapper.eq(LogPO::getCorpId, 0L).or().eq(LogPO::getCorpId, loginService.getCurrentCorpId());
        });
        lqw.orderByDesc(LogPO::getCreateTime);
        this.page(pager, lqw);
        return pager;
    }

    @Override
    public void saveLog(LogPO logPO) throws BaseBusinessException {
        this.save(logPO);
    }

    @Override
    public void saveLogs(List<LogPO> logPOList) throws BaseBusinessException {
        this.saveBatch(logPOList, 50);
    }

    @Override
    public void saveLogs(List<LogPO> logPOList, int batch) throws BaseBusinessException {
        this.saveBatch(logPOList, batch);
    }

    @Autowired
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }
}
