## 项目介绍
> 本项目是一个使用SpringSecurity+Activiti7的简单demo
> 
> 旨在帮助第一次接触activiti的新人快速有个入门的概念
> 
---
**相关模块:**
+ activiti-db: 该模块主要存放一些实体类,以及db的配置数据
+ activiti-base: 该模块是使用activiti的一些基础api,简单的进行使用.流程的完整调用可以见test目录下进行debug.
  + activiti7提供的RuntimeService,RepositoryService,HistoryService,TaskService的简单使用
  + 全局任务监听,以及局部需要进行配置的任务监听
  + 使用代码进行流程图的定义,发布等
+ activiti-cmd: 该模块利用activiti核心的执行器实现基本的业务操作
  + 强制流程中止
    + 提出人进行撤回
    + 审批人进行驳回中止
  + 跳转到对应的流程节点
    + 审批人进行驳回,使流程重新开始
    + 审批人进行驳回,使流程回归到提交人手里,提交人进行审批通过之后继续回到当前审批人手里


---
**使用的依赖版本**

所用springboot版本: 2.3.1.RELEASE

所用springboot-activiti版本:  7.1.0.M6

所用mybatis-plus版本: 3.5.1



