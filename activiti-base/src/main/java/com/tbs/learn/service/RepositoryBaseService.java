package com.tbs.learn.service;

import com.tbs.learn.enums.BaseProcessEnum;
import com.tbs.learn.enums.BaseUserEnum;
import com.tbs.learn.listener.TaskLocalListener;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.BaseTaskListener;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author by tbs-jyy
 * @classname RepositoryBaseService
 * @description 使用repositoryService的一些demo
 * @date 2022/1/30 19:55
 */
@Service
@Slf4j
public class RepositoryBaseService {

    @Autowired
    private RepositoryService repositoryService;


    /***
     * 使用代码创建一个简单的流程定义
     * START -> a审批 -> b审批 -> END
     * 配合使用特定的监听器进行监听任务
     * @return processDefinitionKey
     */
    public String createSimpleProcessDefinition() {

        BpmnModel bpmnModel = new BpmnModel();
        Process process = createAutoSimpleProcess();
        bpmnModel.addProcess(process);

        // 把bpmnModel转为xml输出,方便排查问题
        String bpmnXml = new String(new BpmnXMLConverter().convertToXML(bpmnModel));
        log.info("AutoSimpleProcess:xml:[{}]", bpmnXml);

        repositoryService.createDeployment()
                .addBpmnModel(BaseProcessEnum.SIMPLE_PROCESS.getProcessDefinitionKey() + ".bpmn20.xml", bpmnModel)
                .deploy();

        // process的id就是流程定义中的key
        return process.getId();
    }

    /**
     * 使用代码创建一个使用流程变量 + 一个排他网关的流程 , 同 VariableProcess.bpmn20.xml
     *
     * @return processDefinitionKey
     */
    public String createVariableProcessDefinition() {

        BpmnModel bpmnModel = new BpmnModel();
        Process process = createAutoVariableProcess();
        bpmnModel.addProcess(process);

        // 把bpmnModel转为xml输出,方便排查问题
        String bpmnXml = new String(new BpmnXMLConverter().convertToXML(bpmnModel));
        log.info("AutoVariableProcess:xml:[{}]", bpmnXml);

        repositoryService.createDeployment()
                .addBpmnModel(BaseProcessEnum.VARIABLE_PROCESS.getProcessDefinitionKey() + ".bpmn20.xml", bpmnModel)
                .deploy();

        // process的id就是流程定义中的key
        return process.getId();

    }

    /***
     * 构建AutoSimpleProcess的流程定义
     * START -> a审批 -> b审批 -> END
     * @return process
     */
    private Process createAutoVariableProcess() {
        Process process = new Process();
        process.setId(BaseProcessEnum.VARIABLE_PROCESS.getProcessDefinitionKey());
        process.setName(BaseProcessEnum.VARIABLE_PROCESS.getProcessDefinitionKey());

        // 构建五个元素
        StartEvent startEvent = new StartEvent();
        startEvent.setId(autoNodeId());

        ExclusiveGateway gateway = new ExclusiveGateway();
        gateway.setId(autoNodeId());

        UserTask timeGt3 = new UserTask();
        timeGt3.setId(autoNodeId());
        timeGt3.setName("time大于3的审批流程");

        UserTask timele3 = new UserTask();
        timele3.setId(autoNodeId());
        timele3.setName("time小于等于3的审批流程");

        EndEvent endEvent = new EndEvent();
        endEvent.setId(autoNodeId());

        // 增加连线
        SequenceFlow sequenceFlow1 = new SequenceFlow(startEvent.getId(), gateway.getId());
        sequenceFlow1.setId(autoNodeId());

        // 连接大于3的分支, 增加条件判断
        SequenceFlow sequenceFlow2 = new SequenceFlow(gateway.getId(), timeGt3.getId());
        sequenceFlow2.setId(autoNodeId());
        sequenceFlow2.setConditionExpression("${time>3}");

        // 连接小于2的分支 , 增加条件判断
        SequenceFlow sequenceFlow3 = new SequenceFlow(gateway.getId(), timele3.getId());
        sequenceFlow3.setId(autoNodeId());
        sequenceFlow3.setConditionExpression("${time<=3}");

        // 2条结束的线
        SequenceFlow sequenceFlow4 = new SequenceFlow(timele3.getId(), endEvent.getId());
        sequenceFlow4.setId(autoNodeId());

        SequenceFlow sequenceFlow5 = new SequenceFlow(timeGt3.getId(),endEvent.getId());
        sequenceFlow5.setId(autoNodeId());

        // 全部添加进来
        process.addFlowElement(startEvent);
        process.addFlowElement(gateway);
        process.addFlowElement(timeGt3);
        process.addFlowElement(timele3);
        process.addFlowElement(endEvent);
        process.addFlowElement(sequenceFlow1);
        process.addFlowElement(sequenceFlow2);
        process.addFlowElement(sequenceFlow3);
        process.addFlowElement(sequenceFlow4);
        process.addFlowElement(sequenceFlow5);

        return process;
    }

    /***
     * 构建AutoSimpleProcess的流程定义
     * START -> a审批 -> b审批 -> END
     * @return process
     */
    private Process createAutoSimpleProcess() {
        Process process = new Process();
        process.setId(BaseProcessEnum.SIMPLE_PROCESS.getProcessDefinitionKey());
        process.setName(BaseProcessEnum.SIMPLE_PROCESS.getProcessDefinitionKey());

        // 创建基础的4个节点
        StartEvent startEvent = new StartEvent();
        startEvent.setId(autoNodeId());

        UserTask aUserTask = new UserTask();
        aUserTask.setId(autoNodeId());
        aUserTask.setAssignee(BaseUserEnum.A.getCode());

        // 给这个a任务额外添加一个监听器
        aUserTask.setTaskListeners(Collections.singletonList(TaskLocalListener.createActivitiListener(BaseTaskListener.EVENTNAME_ALL_EVENTS)));

        UserTask bUserTask = new UserTask();
        bUserTask.setId(autoNodeId());
        bUserTask.setAssignee(BaseUserEnum.B.getCode());

        EndEvent endEvent = new EndEvent();
        endEvent.setId(autoNodeId());

        // 之后使用连接吧他们连起来
        SequenceFlow sequenceFlow1 = new SequenceFlow(startEvent.getId(), aUserTask.getId());
        sequenceFlow1.setId(autoNodeId());

        SequenceFlow sequenceFlow2 = new SequenceFlow(aUserTask.getId(), bUserTask.getId());
        sequenceFlow2.setId(autoNodeId());

        SequenceFlow sequenceFlow3 = new SequenceFlow(bUserTask.getId(), endEvent.getId());
        sequenceFlow3.setId(autoNodeId());

        // 把所有节点存进process
        process.addFlowElement(startEvent);
        process.addFlowElement(aUserTask);
        process.addFlowElement(bUserTask);
        process.addFlowElement(endEvent);

        process.addFlowElement(sequenceFlow1);
        process.addFlowElement(sequenceFlow2);
        process.addFlowElement(sequenceFlow3);

        return process;
    }

    private String autoNodeId() {
        return "auto-" + UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 查询所有
     *
     * @return 所有的流程定义
     */
    public List<ProcessDefinition> queryAllProcessDefinition() {
        return repositoryService.createProcessDefinitionQuery()
                // 每个一样的流程图就取最新的版本
                .latestVersion()
                .list();
    }


    /**
     * 根据流程定义的key获取流程定义
     *
     * @param processKey 流程定义的key
     * @return 流程定义
     */
    public ProcessDefinition queryProcessDefinitionByProcessKey(String processKey) {
        return repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processKey)
                .latestVersion()
                .singleResult();
    }
}
