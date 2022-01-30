package com.tbs.learn.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author by tbs-jyy
 * @classname BaseUserEnum
 * @description
 * @date 2022/1/30 20:06
 */
@Getter
@AllArgsConstructor
public enum BaseUserEnum {
    A("a用户", "审批人a"),
    B("b用户", "审批人b"),

    SUBMIT("c用户", "提交人"),

    ;
    private final String code;
    private final String desc;
}
