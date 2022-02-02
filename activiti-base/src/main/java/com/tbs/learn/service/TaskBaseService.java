package com.tbs.learn.service;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author by tbs-jyy
 * @classname TaskBaseService
 * @description
 * @date 2022/2/1 20:52
 */
@Service
public class TaskBaseService {

    @Autowired
    private TaskService taskService;

    /**
     * 根据流程实例id,查询相关的所有执行中的任务 , 根据任务创建时间降序排列
     *
     * @param processInstanceId 流程实例id
     * @return 任务列表
     */
    public List<Task> queryTasksByProcessInstanceId(String processInstanceId) {
        return taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .orderByTaskCreateTime()
                .desc()
                .includeProcessVariables()
                .includeTaskLocalVariables()
                .list();
    }

    public void completedTask(Task task) {
        taskService.complete(task.getId());
    }


}
