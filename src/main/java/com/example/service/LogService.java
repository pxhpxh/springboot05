package com.example.service;


import com.example.bean.LogPO;
import com.example.dto.grid.Pager;
import com.example.exception.BaseBusinessException;

import java.util.List;

public interface LogService {
    Pager<LogPO> getPage(Pager<LogPO> pager) throws BaseBusinessException;

    void saveLog(LogPO logPO) throws BaseBusinessException;

    void saveLogs(List<LogPO> logPOList) throws BaseBusinessException;

    void saveLogs(List<LogPO> logPOList, int batch) throws BaseBusinessException;
}
