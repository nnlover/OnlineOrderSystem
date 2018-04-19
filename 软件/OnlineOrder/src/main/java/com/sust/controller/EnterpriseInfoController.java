package com.sust.controller;

import com.sust.model.TEnterpriseInfo;
import com.sust.service.EnterpriseInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * 对企业信息的管理
 * OnlineOrder
 * com.sust.controller
 * 2018/4/19
 */
@Controller
@RequestMapping("/enterpriseInfo")
public class EnterpriseInfoController {

    @Resource
    private EnterpriseInfoService enterpriseInfoService;

    @RequestMapping(value = "/enterpriseInfoList", method = RequestMethod.GET)
    public String enterpriseInfoList(Model model) {
        List<TEnterpriseInfo> tEnterpriseInfos = enterpriseInfoService.queryByPage();
        model.addAttribute("tEnterpriseInfos", tEnterpriseInfos);
        return "list";
    }

    @RequestMapping(value = "/enterpriseDedail", method = RequestMethod.GET)
    public String enterpriseInfoDedail(@RequestParam(value = "id", required = true) String id, Model model) {
        TEnterpriseInfo tEnterpriseInfos = enterpriseInfoService.queryById(id);
        model.addAttribute("tEnterpriseInfos", tEnterpriseInfos);
        return "list";
    }
}
