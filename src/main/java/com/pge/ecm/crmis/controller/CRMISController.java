package com.pge.ecm.crmis.controller;

import com.pge.ecm.crmis.service.SearchService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class CRMISController {

    @Value("${repository.name}")
    public String repository;

    @Value("${repository.user}")
    public String repository_user;

    @Value("${repository.password}")
    public String repository_password;

    @Value("${query.select.list}")
    public String select_list;

    SearchService searchService;
    @RequestMapping("/version")
    public String CRMISLite() {
        return "Welcome to CRM Integration Service Light-Weight version 1.0.0.3";
    }

    @RequestMapping("/query")
    public List<Map<String, String>> query(
            @RequestParam String type,
            @RequestParam(required = false)String sdc,
            @RequestParam(required = false)String where
    ) throws Exception {
        System.out.println("Got search request");
        searchService = new SearchService(repository, repository_user, repository_password);
        return searchService.execute(type, sdc, where, select_list);
    }
}
