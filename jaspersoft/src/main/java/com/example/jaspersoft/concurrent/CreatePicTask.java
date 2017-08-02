package com.example.jaspersoft.concurrent;

import com.example.jaspersoft.common.SpringTool;
import com.example.jaspersoft.request.IReportReq;
import com.example.jaspersoft.response.PicResp;
import com.example.jaspersoft.service.JasperService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * Created by martin on 2017/8/2.
 */
public class CreatePicTask extends RecursiveTask<List<PicResp>> {
    private final static Logger logger = LoggerFactory.getLogger(CreatePicTask.class);

    private List<IReportReq> reportReqList = new ArrayList<>();

    public CreatePicTask(List<IReportReq> reportReqList) {
        this.reportReqList = reportReqList;
    }

    @Override protected List<PicResp> compute() {
        List<PicResp> respList = new ArrayList<>();
        int listSize = reportReqList.size();

        if (listSize == 0) {
            return null;
        }

        if (listSize == 1) {
            JasperService jasperService = SpringTool.getBean(JasperService.class);
            PicResp picResp = new PicResp();
            picResp.setTitle(reportReqList.get(0).getTitle());
            try {
                String pic = jasperService.createPic(reportReqList.get(0));

                picResp.setUrl(pic);
            } catch (Exception e) {
                logger.error("创建图片失败");
                picResp.setUrl("");
            } finally {
                    respList.add(picResp);

            }
        } else {

            int firstFlag = listSize / 2;
            List<IReportReq> reportReqList1 = splitReq(reportReqList,0, firstFlag);
            List<IReportReq> reportReqList2 = splitReq(reportReqList,firstFlag,listSize);

            CreatePicTask createPicTask1 = new CreatePicTask(reportReqList1);
            CreatePicTask createPicTask2 = new CreatePicTask(reportReqList2);

            invokeAll(createPicTask1,createPicTask2);

            List<PicResp> respList1 = createPicTask1.join();
            List<PicResp> respList2 = createPicTask2.join();

            respList.addAll(respList1);
            respList.addAll(respList2);

        }

        return respList;
    }

    private List<IReportReq> splitReq(List<IReportReq> reqList, Integer start, Integer end){
        List<IReportReq> newReqList = new ArrayList<>();

        for(int i = start;i < end; i++){
            newReqList.add(reqList.get(i));
        }

        return newReqList;
    }

}
