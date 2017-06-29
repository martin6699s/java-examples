package main.com.qlk;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 通过注解WebServlet, 配置Servlet,注册到web容器
 */
@WebServlet(name="AnnotationServlet", urlPatterns = { "/AnnotationServlet"})
public class AnnotationServlet extends HttpServlet {
    private static final long serialVersionUID = -1L;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            // Write some content
            out.println("<html>");
            out.println("<head>");
            out.println("<title>AnnotationServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h2>Servlet AnnotationServlet at " + request.getContextPath() + "</h2>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        //Do some other work
    }

    @Override
    public String getServletInfo() {
        return "AnnotationServlet";
    }

}
