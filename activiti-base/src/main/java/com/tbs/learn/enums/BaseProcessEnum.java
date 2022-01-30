package com.tbs.learn.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author by tbs-jyy
 * @classname BaseProcessEnum
 * @description 基本流程枚举
 * @date 2022/1/30 20:08
 */
@Getter
@AllArgsConstructor
public enum BaseProcessEnum {

    XML_SIMPLE_PROCESS("SimpleProcess", "由xml配置而成的流程定义,简单且固定的二级审批,对应使用代码生成的流程定义SIMPLE_PROCESS"),
    SIMPLE_PROCESS("AutoSimpleProcess", "使用代码构建流程定义,使用api进行手动发布,简单且固定的二级审批(该流程中展示了特定的监听器)"),
    XML_VARIABLE_PROCESS("VariableProcess", "由xml配置而成的流程定义,使用流程变量来进行流程的控制,对应使用代码生成的流程定义VARIABLE_PROCESS"),
    VARIABLE_PROCESS("AutoVariableProcess", "同上"),
    XML_DYNAMIC_ASSIGNEE("DynamicAssignee", "利用流程变量动态安排受让人的流程定义(包含展示了本地变量)"),


    ;
    private final String processDefinitionKey;
    private final String desc;
}
