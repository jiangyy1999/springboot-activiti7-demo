package com.tbs.learn;

import com.tbs.learn.enums.BaseProcessEnum;
import com.tbs.learn.enums.BaseUserEnum;
import com.tbs.learn.service.HistoryBaseService;
import com.tbs.learn.service.RepositoryBaseService;
import com.tbs.learn.service.RuntimeBaseService;
import com.tbs.learn.service.TaskBaseService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * @author by tbs-jyy
 * @classname Actviti7BaseTest
 * @description
 * @date 2022/2/1 19:26
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class Actviti7BaseTest {

    @Autowired
    private RepositoryBaseService repositoryBaseService;

    @Autowired
    private RuntimeBaseService runtimeBaseService;

    @Autowired
    private HistoryBaseService historyBaseService;

    @Autowired
    private TaskBaseService taskBaseService;

    @Test
    // 使用代码创建2个流程定义 , BaseProcessEnum.SIMPLE_PROCESS 以及 BaseProcessEnum.VARIABLE_PROCESS
    public void createNewProcessDefinitions() {
        String simpleProcessDefinitionKey = repositoryBaseService.createSimpleProcessDefinition();
        String variableProcessDefinitionKey = repositoryBaseService.createVariableProcessDefinition();
        log.info("simpleProcessDefinitionKey=[{}]", simpleProcessDefinitionKey);
        log.info("variableProcessDefinitionKey=[{}]", variableProcessDefinitionKey);

        // 输出当前仓库所有的最新流程定义图
        printAllProcessDefinition();
    }

    @Test
    public void runXmlSimpleProcessTest() {
        // 测试简单的一条流程
        testProcess(BaseUserEnum.SUBMIT, BaseProcessEnum.XML_SIMPLE_PROCESS, Collections.emptyMap());

        // 放进一些流程变量参数, 看是否正确打印
        Map<String, Object> varMap = new HashMap<>();
        varMap.put("state", 1);
        varMap.put("aaa", "test");
        testProcess(BaseUserEnum.SUBMIT, BaseProcessEnum.XML_SIMPLE_PROCESS, varMap);
    }

    @Test
    public void runSimpleProcessTest() {
        /**
         * 该流程定义中,aUserTask加入了监听,发现了个东西, 任务从创建到完成
         * 局部监听器的事件顺序是 : 分配受让人事件, 创建事件, 完成事件 ,删除事件
         * 和全局的事件监听混合到一起之后
         * 完整的顺序是: 局部监听受让人事件 , 局部监听任务创建事件 , 全局任务创建事件 , 全局任受让人事件 , 局部完成,全局完成,局部删除
         */
        testProcess(BaseUserEnum.SUBMIT, BaseProcessEnum.SIMPLE_PROCESS, Collections.emptyMap());
    }

    @Test
    public void runVariableProcessTest() {
        Map<String, Object> varMap = new HashMap<>();
        varMap.put("time", 1);
        // 测试俩条分支
        testProcess(BaseUserEnum.SUBMIT, BaseProcessEnum.XML_VARIABLE_PROCESS, varMap);

        varMap.put("time", 10);
        testProcess(BaseUserEnum.SUBMIT, BaseProcessEnum.XML_VARIABLE_PROCESS, varMap);
    }


    /**
     * 完整的调用一遍一个流程
     *
     * @param startUser   发起人
     * @param processEnum 流程实例
     * @param variableMap 流程变量map
     */
    private void testProcess(BaseUserEnum startUser, BaseProcessEnum processEnum, Map<String, Object> variableMap) {

        // 开始一个流程实例
        String processInstanceId = runtimeBaseService.startProcessInstance(startUser.getCode(), processEnum, variableMap);

        // 手动修改流程变量, 观察历史中是否发生变化
        runtimeBaseService.updateProcessVariable(processInstanceId, "changeModify", "modifyOk");

        // 查询流程实例的相关信息
        printProcessInstanceDetail(processInstanceId);

        // 查询所有相关任务, 直到处理完所有
        while (true) {
            List<Task> tasks = taskBaseService.queryTasksByProcessInstanceId(processInstanceId);
            if (null == tasks || tasks.size() == 0) {
                break;
            }
            for (Task task : tasks) {
                printTask(task);
                taskBaseService.completedTask(task);
            }
        }

        printProcessInstanceDetail(processInstanceId);
    }

    private void printTask(Task task) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("================taskInstance==================\n")
                .append("taskId=[{}]\n")
                .append("taskName=[{}]\n")
                .append("taskAssignee=[{}]\n")
                .append("taskBusinessKey=[{}]\n")
                .append("taskProcessVariables=[{}]\n")
                .append("taskLocalVariables=[{}]\n")
                .append("================taskInstance==================\n");
        log.info(stringBuilder.toString(), task.getId(), task.getName(), task.getAssignee(), task.getBusinessKey(), task.getProcessVariables(), task.getTaskLocalVariables());
    }

    // 输出全部流程定义
    private void printAllProcessDefinition() {
        repositoryBaseService.queryAllProcessDefinition().forEach(this::printProcessDefinition);
    }

    /**
     * 输出一个流程实例的详细信息 , 包括运行中和历史的, 也包括流程变量
     *
     * @param processInstanceId 流程实例id
     */
    private void printProcessInstanceDetail(String processInstanceId) {
        Optional.ofNullable(runtimeBaseService.queryByProcessInstanceId(processInstanceId)).ifPresent(this::printProcessInstance);
        Optional.ofNullable(historyBaseService.queryByProcessInstanceId(processInstanceId)).ifPresent(this::printHistoryProcessInstance);
    }

    /**
     * 输出流程实例的信息
     */
    private void printProcessDefinition(ProcessDefinition processDefinition) {
        log.info("====================ProcessDefinition===================\n" +
                        "processDefinitionId=[{}}\n" +
                        "processDefinitionKey=[{}}\n" +
                        "processDefinitionName=[{}}\n" +
                        "processDefinitionDeploymentId=[{}]\n" +
                        "====================ProcessDefinition===================",
                processDefinition.getId(), processDefinition.getKey(), processDefinition.getName(), processDefinition.getDeploymentId());
    }

    /**
     * 输出流程实例的信息
     */
    private void printProcessInstance(ProcessInstance processInstance) {
        StringBuilder builder = new StringBuilder();
        builder.append("======================ProcessInstance===============\n")
                .append("processInstanceId=[{}]\n")
                .append("processBusinessKey=[{}]\n")
                .append("processVariables=[{}]\n")
                .append("======================ProcessInstance===============\n");

        log.info(builder.toString(), processInstance.getId(), processInstance.getBusinessKey(), processInstance.getProcessVariables());
    }

    /**
     * 输出流程实例的信息
     */
    private void printHistoryProcessInstance(HistoricProcessInstance processInstance) {
        StringBuilder builder = new StringBuilder();
        builder.append("======================HistoricProcessInstance===============\n")
                .append("hisProcessInstanceId=[{}]\n")
                .append("hisProcessBusinessKey=[{}]\n")
                .append("hisProcessVariables=[{}]\n")
                .append("======================HistoricProcessInstance===============\n");

        log.info(builder.toString(), processInstance.getId(), processInstance.getBusinessKey(), processInstance.getProcessVariables());
    }
}
