package com.heima.travel.admin;


import com.heima.travel.model.Route;
import com.heima.travel.model.RouteImg;
import com.heima.travel.service.RouteService;
import com.heima.travel.service.imp.RouteServiceImp;
import com.heima.travel.utils.UuidUtil;
import com.heima.travel.web.BaseServlet;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/upload")
public class UploadImageServlet extends BaseServlet {
    RouteService routeService = new RouteServiceImp();

    public void uploadRouteImage(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        //解析request请求
        List<FileItem> parseRequest = upload.parseRequest(req);
        System.out.println(parseRequest);
        int a = 0;
        int b = 0;
        //创建一个map集合用来接受表单数据
        Map<String, String> map = new HashMap<String, String>();
        for (FileItem fileItem : parseRequest) {
            if (fileItem.isFormField()) {
                //如果是表单数据
                //获取表单数据的name值
                String key = fileItem.getFieldName();
                //获取表单数据的value值
                String value = fileItem.getString("utf-8");
                map.put(key, value);
            } else {
                //不是表单数据，是文件
                /*文件如果被保存到当前项目中，每当修改代码，项目被重新发布，保存的文件就会丢失，
                 * 所以将文件保存到tomcat的其他项目中，这里保存到img项目中
                 *
                 *
                 * */
                //获取tomcat下的img项目的绝对路径，
                /* String diskPath = getServletContext().getRealPath("").replace("estore", "img");*/
                String diskPath = req.getServletContext().getRealPath("./");
                if ("rimage".equals(fileItem.getFieldName())) {
                    //给文件名加上uuid，保证保存的文件名字都是不一样的。
                    String filename = UuidUtil.getUuid() + fileItem.getName();
                    //获取保存的文件的绝对路径
                    diskPath += "img" + "\\" + filename;
                    System.out.println("图片被保存到" + diskPath);
                    //保存文件
                    fileItem.write(new File(diskPath));

                    //获取保存到数据库的图片的路径
                    String imgurl = "/img/" + filename;
                    map.put("rimage" + a, imgurl);
                    a++;
                } else if ("imgList".equals(fileItem.getFieldName())) {
                    //给文件名加上uuid，保证保存的文件名字都是不一样的。
                    String filename = UuidUtil.getUuid() + fileItem.getName();
                    //获取保存的文件的绝对路径
                    diskPath += "img" + "\\" + filename;
                    System.out.println("图片被保存到" + diskPath);
                    //保存文件
                    fileItem.write(new File(diskPath));

                    //获取保存到数据库的图片的路径
                    String imgurl = "/img/" + filename;
                    map.put("imgList" + b, imgurl);
                    b++;
                }
            }

        }
        //将需要保存到数据库的商品的数据封装到good对象中
        Route route = new Route();

        List<RouteImg> routeImgList = new ArrayList<>();
        for (String key : map.keySet()) {
            if (key.contains("rimage")) {
                RouteImg routeImg = new RouteImg();
                String s = key;
                String[] cs = s.split("e");
                int i = Integer.parseInt(cs[1]);
                routeImg.setBigPic(map.get(key));
                routeImgList.add(routeImg);
            } else if (key.contains("imgList")) {
                String s = key;
                String[] cs = s.split("t");
                int i = Integer.parseInt(cs[1]);
                if (routeImgList.size() < i) {
                    RouteImg routeImg = new RouteImg();
                    routeImg.setBigPic(map.get(key));
                    routeImgList.add(routeImg);
                } else {
                    routeImgList.get(i).setSmallPic(map.get(key));
                }

            }
        }
        route.setRouteImgList(routeImgList);
        BeanUtils.populate(route, map);
        routeService.addRoute(route);
        resp.getWriter().write("1");

    }

}
