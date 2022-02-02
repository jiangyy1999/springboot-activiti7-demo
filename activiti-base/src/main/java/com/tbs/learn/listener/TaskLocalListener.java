package com.tbs.learn.listener;

import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.bpmn.model.ImplementationType;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @author by tbs-jyy
 * @classname TaskLocalListener
 * @description
 * @date 2022/1/31 16:10
 */
@Slf4j
public class TaskLocalListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {

        switch (delegateTask.getEventName()) {
            case EVENTNAME_CREATE:
                log.info("TaskLocalListener监听到任务创建事件,taskId=[{}],processInstanceId=[{}]", delegateTask.getId(), delegateTask.getProcessInstanceId());
                break;
            case EVENTNAME_ASSIGNMENT:
                log.info("TaskLocalListener监听到任务分配受让人事件,taskId=[{}],processInstanceId=[{}],assignment=[{}]", delegateTask.getId(), delegateTask.getProcessInstanceId(), delegateTask.getAssignee());
                break;
            case EVENTNAME_COMPLETE:
                log.info("TaskLocalListener监听到任务完成事件,taskId=[{}],processInstanceId=[{}]", delegateTask.getId(), delegateTask.getProcessInstanceId());
                break;
            case EVENTNAME_DELETE:
                log.info("TaskLocalListener监听到任务删除事件,taskId=[{}],processInstanceId=[{}]", delegateTask.getId(), delegateTask.getProcessInstanceId());
                break;
        }

    }

    /**
     * 包装一个ActivitiListener,使用的实例类型
     *
     * @param eventType
     * @return
     */
    public static ActivitiListener createActivitiListener(String eventType) {
        ActivitiListener activitiListener = new ActivitiListener();
        activitiListener.setImplementation(TaskLocalListener.class.getName());
        activitiListener.setEvent(eventType);
        activitiListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_CLASS);
        return activitiListener;
    }


}
