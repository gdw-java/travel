package com.heima.travel.web;

import com.heima.travel.service.CategoryService;
import com.heima.travel.service.imp.CategoryServicImp;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 旅游线路分类信息servlet
 */
@WebServlet("/category")
public class CategoryServlet extends BaseServlet {
    private  CategoryService service=new CategoryServicImp();

    public void getCategorys(HttpServletRequest request,HttpServletResponse response) throws IOException {

       String jsonString= service.findCategorys();
       response.getWriter().print(jsonString);
    }

}
