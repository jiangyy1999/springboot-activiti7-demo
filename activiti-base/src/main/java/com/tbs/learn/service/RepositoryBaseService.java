package com.tbs.learn.service;

import org.activiti.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author by tbs-jyy
 * @classname RepositoryBaseService
 * @description 使用repositoryService的一些demo
 * @date 2022/1/30 19:55
 */
@Service
public class RepositoryBaseService {

    @Autowired
    private RepositoryService repositoryService;


}
