package com.example.jaspersoft.service;

import com.example.jaspersoft.concurrent.CreatePicTask;
import com.example.jaspersoft.request.IReportReq;
import com.example.jaspersoft.response.PicResp;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.export.JRGraphics2DExporterParameter;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

import javax.imageio.ImageIO;

import static org.apache.commons.lang3.StringUtils.isNoneBlank;

@Service
public class JasperService implements InitializingBean, DisposableBean {

    private JasperReport jasperReport;

    @Value("${spring.ireports.image.path}")
    private String imagePath;

    private String tempPath = "/app/data/images";

    /**
     * fork/join invokAll 并发生成图片
     * @param iReportReqs
     * @return
     */
    public List<PicResp> concurrentCreatePic(List<IReportReq> iReportReqs){

        if(iReportReqs.isEmpty()){
            return null;
        }
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        CreatePicTask createPicTask = new CreatePicTask(iReportReqs);

        List<PicResp> picRespList = forkJoinPool.invoke(createPicTask);

        forkJoinPool.shutdown();

        forkJoinPool = null;

        return picRespList;

    }

    public String createPic(IReportReq iReportReq) throws Exception {
        String DBPath = "";
        BufferedImage bufferedImage = null;
        try {
            // Parameters for report
            //Parameters 会传输到jasper的对应parameter
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("title", iReportReq.getTitle());
            parameters.put("titleId",iReportReq.getTitleId());
            parameters.put("name", isNoneBlank(iReportReq.getName()) ? iReportReq.getName() : "");
            String gender = "";
            if (isNoneBlank(iReportReq.getGender())) {
                if ("0".equals(iReportReq.getGender())) {
                    gender = "女";
                } else {
                    gender = "男";
                }
            }
            parameters.put("gender", gender);
            parameters.put("age", iReportReq.getAge());
            parameters.put(
                    "time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                            null == iReportReq.getTime() ? new Date() : iReportReq.getTime()));

            parameters.put("specification",iReportReq.getSpecification());

            //JRBeanCollectionDataSource(list),直接传到jasper的fields
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    parameters,
                    new JRBeanCollectionDataSource(
                            iReportReq.getContentList())
            );
            JRGraphics2DExporter exporter = new JRGraphics2DExporter();//创建graphics输出器
            //创建一个影像对象
            bufferedImage = new BufferedImage(
                    jasperPrint.getPageWidth() * 4, jasperPrint.getPageHeight() * 4,
                    BufferedImage.TYPE_INT_RGB
            );
            //取graphics
            Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
            //设置相应参数信息
            exporter.setParameter(JRGraphics2DExporterParameter.GRAPHICS_2D, g);
            exporter.setParameter(JRGraphics2DExporterParameter.ZOOM_RATIO, Float.valueOf(4));
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.exportReport();
            g.dispose();//释放资源信息


            String path =  isNoneBlank(imagePath)? imagePath : tempPath;//文件路径


            Random rand = new Random();
            Integer fileName = rand.nextInt(1000);
            String pic = fileName.toString() + "dd.jpg";//图片生成名称

            String fullname = path +"/" + pic;//完整路径名
            DBPath = fullname;
            //创建文件夹
            File f = new File(path);
            if (!f.exists()) {
                f.mkdirs();
            }
            //这里的bufferedImage就是最终的影像图像信息,可以通过这个对象导入到cm中了.
            ImageIO.write(bufferedImage, "JPEG", new File(fullname));
        } catch (Exception e) {
            throw new Exception("生成图片失败:  " + e.getMessage());
        } finally {
            bufferedImage.flush();
        }
        return DBPath;
    }

    @Override public void destroy() throws Exception {
        // 销毁该bean时执行该方法
        if (jasperReport != null) {
            jasperReport = null;
        }
    }

    @Override public void afterPropertiesSet() throws Exception {
        // 初始化该bean时 执行该方法
        jasperReport = JasperCompileManager.compileReport(
                JasperService.class.getResourceAsStream("/jrxml/ireport.jrxml"));
    }
}
