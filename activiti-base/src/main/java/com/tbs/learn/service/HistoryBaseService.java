package com.tbs.learn.service;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author by tbs-jyy
 * @classname HistoryBaseService
 * @description
 * @date 2022/2/1 20:46
 */
@Service
public class HistoryBaseService {

    @Autowired
    private HistoryService historyService;

    /**
     * 根据流程实例id , 查询历史
     *
     * @param processInstanceId 流程实例id
     * @return 流程实例
     */
    public HistoricProcessInstance queryByProcessInstanceId(String processInstanceId) {
        return historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .includeProcessVariables()
                .singleResult();
    }

}
