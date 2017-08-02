package com.example.jaspersoft.controller;

import com.example.jaspersoft.request.IReportReq;
import com.example.jaspersoft.request.IReportReq.Content;
import com.example.jaspersoft.response.DefaultResp;
import com.example.jaspersoft.response.PicResp;
import com.example.jaspersoft.service.JasperService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 2017/7/29.
 */
@RestController
@RequestMapping(value = "/demo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class DemoController {

    @Autowired
    private JasperService jasperService;


    @RequestMapping(value = "/new-image", method = RequestMethod.POST)
    public DefaultResp genernateImage(@RequestBody List<IReportReq> iReportReqs) {

        DefaultResp result = new DefaultResp();
        List<PicResp> respList = new ArrayList<>();
        try {
              respList = jasperService.concurrentCreatePic(iReportReqs);
        } catch (Exception e) {
            System.out.println("jasper 错误：" + e.getMessage());
            result.setCode("10002");
            result.setMessage("图片生成失败");
            result.setData(e.getMessage());
            return result;
        }
        result.setCode("10001");
        result.setMessage("成功");
        result.setData(respList);
        return result;
    }
}
