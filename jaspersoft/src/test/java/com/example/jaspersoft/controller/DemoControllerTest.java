package com.example.jaspersoft.controller;

import com.example.jaspersoft.request.IReportReq;
import com.example.jaspersoft.request.IReportReq.Content;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by martin on 2017/7/29.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoControllerTest {

    private static final String baseUri = "/demo/new-image";

    private MockMvc mockMvc;
    protected MediaType mediaTypeJson = MediaType.APPLICATION_JSON_UTF8;
    @Autowired
    private HttpMessageConverter<Object> mappingJackson2HttpMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver;
    // 提取分页参数解析器

    @Autowired
    private DemoController demoController;

    /**
     * 转为json字符串
     */
    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                demoController).setCustomArgumentResolvers(
                pageableHandlerMethodArgumentResolver).build();
    }

    @Test
    public void generateImageTest() throws Exception {

        IReportReq reportReq1 = generateIReport();
        IReportReq reportReq2 = generateIReport();
        reportReq1.setTitle("个人简历.1");
        reportReq2.setTitle("个人简历.2");
        List<IReportReq> iReportReqs = new ArrayList<>();
        iReportReqs.add(reportReq1);
        iReportReqs.add(reportReq2);

        //expect
        MockHttpServletRequestBuilder storeRequest = post(baseUri);
        String prescriptionJson = json(iReportReqs);
        storeRequest.contentType(mediaTypeJson).content(prescriptionJson);
        mockMvc.perform(storeRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(mediaTypeJson))
                .andReturn();
    }

    private IReportReq generateIReport() {
        IReportReq iReportReq = new IReportReq();
        iReportReq.setTitle("个人简历");
        iReportReq.setAge("29");
        iReportReq.setTitleId(201700111);
        iReportReq.setGender("1");
        iReportReq.setName("martin6699");
        iReportReq.setSpecification(
                "由于集成技术的发展，半导体芯片的集成度更高，每块芯片可容纳数万乃至数百万个晶体管，并且可以把运算器和控制器都集中在一个芯片上、从而出现了微处理器，并且可以用微处理器和大规模、超大规模集成电路组装成微型计算机，就是我们常说的微电脑或PC机。微型计算机体积小，价格便宜，使用方便，但它的功能和运算速度已经达到甚至超过了过去的大型计算机。另一方面，利用大规模、超大规模集成电路制造的各种逻辑芯片，已经制成了体积并不很大，但运算速度可达一亿甚至几十亿次的巨型计算机。我国继1983年研制成功每秒运算一亿次的银河Ⅰ这型巨型机以后，又于1993年研制成功每秒运算十亿次的银河Ⅱ型通用并行巨型计算机。这一时期还产生了新一代的程序设计语言以及数据库管理系统和网络软件等。");
        Content content1 = new Content();
        content1.setId(1);
        content1.setSkill("会点PHP Laravel composer MYSQL Redis Linux");
        content1.setData("到底会不会，不会拉倒");

        Content content2 = new Content();
        content2.setId(2);
        content2.setSkill("会点JAVA Spring Boot Mybatis jenkins ");
        content2.setData("到底会不会，不会拉倒");

        Content content3 = new Content();
        content3.setId(3);
        content3.setSkill("会算吧，。。。。。哈哈哈");
        content3.setData("到底会不会，不会拉倒");

        List<Content> contentList = new ArrayList<>();
        contentList.add(content1);
        contentList.add(content2);
        contentList.add(content3);
        iReportReq.setContentList(contentList);

        return iReportReq;
    }
}
