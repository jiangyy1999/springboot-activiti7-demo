package com.tbs.learn.listener;

import lombok.extern.slf4j.Slf4j;
import org.activiti.api.task.runtime.events.*;
import org.activiti.api.task.runtime.events.listener.TaskRuntimeEventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author by tbs-jyy
 * @classname TaskGlobalListener
 * @description 全局任务事件监听
 * @date 2022/2/2 13:49
 */
@Configuration
@Slf4j
public class TaskGlobalListener {

    /**
     * TASK_ASSIGNED,
     * TASK_COMPLETED,
     * TASK_CREATED,
     * TASK_UPDATED,
     * TASK_ACTIVATED,
     * TASK_SUSPENDED,
     * TASK_CANCELLED;
     */

    @Bean
    public TaskRuntimeEventListener<TaskCreatedEvent> createdEventTaskRuntimeEventListener() {
        // 全局监听任务创建事件
        return e -> {
            log.info("============全局监听任务创建事件============\n" +
                    "e=[{}]\n" +
                    "============全局监听任务创建事件============\n", e);
        };
    }

    @Bean
    public TaskRuntimeEventListener<TaskCompletedEvent> completedEventTaskRuntimeEventListener() {
        // 全局监听任务创建事件
        return e -> {
            log.info("============全局监听任务完成事件============\n" +
                    "e=[{}]\n" +
                    "============全局监听任务完成事件============\n", e);
        };
    }

    @Bean
    public TaskRuntimeEventListener<TaskAssignedEvent> assignedEventTaskRuntimeEventListener() {
        // 全局监听任务创建事件
        return e -> {
            log.info("============全局监听任务分配受让人事件============\n" +
                    "e=[{}]\n" +
                    "============全局监听任务分配受让人事件============\n", e);
        };
    }

    @Bean
    public TaskRuntimeEventListener<TaskUpdatedEvent> updatedEventTaskRuntimeEventListener() {
        // 全局监听任务创建事件
        return e -> {
            log.info("============全局监听任务更新事件============\n" +
                    "e=[{}]\n" +
                    "============全局监听任务更新事件============\n", e);
        };
    }

    @Bean
    public TaskRuntimeEventListener<TaskCancelledEvent> cancelledEventTaskRuntimeEventListener() {
        // 全局监听任务创建事件
        return e -> {
            log.info("============全局监听任务取消事件============\n" +
                    "e=[{}]\n" +
                    "============全局监听任务取消事件============\n", e);
        };
    }

}
