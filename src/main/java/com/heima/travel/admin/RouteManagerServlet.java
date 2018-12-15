package com.heima.travel.admin;

import com.heima.travel.model.Route;
import com.heima.travel.service.RouteService;
import com.heima.travel.service.imp.RouteServiceImp;
import com.heima.travel.utils.ExcelUtil;
import com.heima.travel.web.BaseServlet;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

@WebServlet("/routeManager")
public class RouteManagerServlet extends BaseServlet {
    private RouteService routeService = new RouteServiceImp();

    //获取旅游线路列表
    public void getRouteList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int curPage = 1;
        String curPageStr = request.getParameter("curPage");
        if (StringUtils.isNotEmpty(curPageStr)) {
            curPage = Integer.parseInt(curPageStr);

        }
        String json = routeService.getRouteList(curPage);
        response.getWriter().write(json);
    }

    //daochuexcel
    public void export(HttpServletRequest req, HttpServletResponse resp) {
        List<Route> routeList = routeService.queryRoute();
        //excel标题
        String[] title = {"旅游线路", "价格", "描述", "线路", "收藏数", "上架时间"};

        //excel文件名
        String fileName = "黑马旅游" + System.currentTimeMillis() + ".xls";

        //sheet名
        String sheetName = "黑马旅游线路表";
        String[][] content = new String[20][];
        for (int i = 0; i < routeList.size(); i++) {
            content[i] = new String[title.length];
            Route obj = routeList.get(i);
            content[i][0] = obj.getRname();
            content[i][1] = String.valueOf(obj.getPrice());
            content[i][2] = obj.getRouteIntroduce();
            if (obj.getCid() == 5) {
                content[i][3] = "国内游";
            }
            content[i][4] = String.valueOf(obj.getCount());
            content[i][5] = obj.getRdate();
        }

        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);

        //响应到客户端
        try {
            this.setResponseHeader(resp, fileName);
            OutputStream os = resp.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setResponseHeader(HttpServletResponse resp, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            resp.setContentType("application/octet-stream;charset=ISO8859-1");
            resp.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            resp.addHeader("Pargam", "no-cache");
            resp.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

