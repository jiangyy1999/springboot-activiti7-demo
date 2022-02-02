package com.tbs.learn.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tbs.learn.enums.BaseProcessEnum;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author by tbs-jyy
 * @classname RuntimeBaseService
 * @description
 * @date 2022/2/1 20:12
 */
@Service
public class RuntimeBaseService {

    @Autowired
    private RuntimeService runtimeService;


    /**
     * 开始一个新的流程实例
     *
     * @param startUser   发起人
     * @param processEnum 流程枚举
     * @param variableMap 流程变量
     * @return 流程实例的id
     */
    public String startProcessInstance(String startUser, BaseProcessEnum processEnum, Map<String, Object> variableMap) {

        // 这边为了由于使用的是test进行操作的, 这块进行设置当前的用户信息,  正常来说,与security结合,应该放在某一层filter进行设置.
        Authentication.setAuthenticatedUserId(startUser);

        return runtimeService.createProcessInstanceBuilder()
                .processDefinitionKey(processEnum.getProcessDefinitionKey())
                // 该属性可以和一些具有业务场景 或者 具有业务逻辑的数据绑定在一起
                .businessKey(processEnum.getProcessDefinitionKey())
                .variables(variableMap)
                .start().getId();

    }

    /***
     * 根据流程定义的类型分页获取流程实例
     *
     * @param baseProcessEnum 流程定义枚举
     * @param currentPage >= 1
     * @param pageSize 最大不超过200
     * @return 相关流程实例
     */
    public IPage<ProcessInstance> queryAllProcessInstance(BaseProcessEnum baseProcessEnum, int currentPage, int pageSize) {
        currentPage = Math.max(1, currentPage);
        pageSize = Math.max(1, pageSize);
        pageSize = Math.min(200, pageSize);

        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(baseProcessEnum.getProcessDefinitionKey())
                // 假如需要带上流程变量
                .includeProcessVariables()
                .processInstanceBusinessKey(baseProcessEnum.getProcessDefinitionKey())

                // 默认按照流程实例id的降序进行
                .orderByProcessInstanceId()
                .desc();

        long count = processInstanceQuery.count();

        IPage<ProcessInstance> processInstanceIPage = new Page<>(currentPage, pageSize, count);

        if (count != 0) {

            processInstanceIPage.setRecords(processInstanceQuery.listPage((currentPage - 1) * pageSize, currentPage * pageSize));

        }

        return processInstanceIPage;

    }


    /**
     * 根据流程实例id返回流程实例
     *
     * @param processInstanceId 流程实例id
     * @return 流程实例
     */
    public ProcessInstance queryByProcessInstanceId(String processInstanceId) {
        return runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .includeProcessVariables()
                .singleResult();
    }

    /**
     * 手动修改流程变量的值
     *
     * @param processId   流程实例id
     * @param variableKey 变量key
     * @param value       变量值
     */
    public void updateProcessVariable(String processId, String variableKey, Object value) {
        runtimeService.setVariable(processId, variableKey, value);
    }


}
