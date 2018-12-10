package com.heima.travel.model;

import java.util.List;

/**
 * 分页bean
 */
public class PageBean {
    private int currentPage;//当前页
    private int prePage;//上一页
    private int nextPage;//下一页
    private  int totalPage;//总页码
    private int pageSize;//每页显示的条数
    private int totalCount; //数据总条数
    private int startPage;//前端分页条显示的开始页
    private int endPage;//前端分页条显示的结束页
    private List<Route> routeList;

    public int getCurrentPage() {
        return currentPage;
    }
    //设置当前页和分页起始结束
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage; //设置当前页
        prePage=currentPage-1;      //设置上一页
        nextPage=currentPage+1;     //设置下一页
       //计算开始页和结束页
        if (totalPage <= 10) {
            startPage = 1;
            endPage = totalPage;
        } else {
            startPage=currentPage-5;
            endPage=currentPage+4;
            if (startPage < 1) {
                startPage=1;
                endPage=10;
            }
            if (endPage > totalPage) {
                endPage=totalPage;
                startPage=totalPage-9;
            }
        }
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    public List<Route> getRouteList() {
        return routeList;
    }

    public void setRouteList(List<Route> routeList) {
        this.routeList = routeList;
    }
}
