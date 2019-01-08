package com.myhopu.utils;

import java.util.List;

public class PageBean<T> {
	private List<T> list;// 当前页面的业务数据

	private int pageNo;// 页码
	private int pageCount;// 总页数
	private int rowCount;// 总记录数
	private int pageSize = 5;// 每页显示几条数据

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getPageNo() {
		return pageNo = pageNo == 0 ? 1 : pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageCount() {
		return pageCount = getRowCount() % pageSize == 0 ? rowCount / pageSize : rowCount / pageSize + 1;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPrevPage() {
		return getPageNo() - 1 == 0 ? 1 : getPageNo() - 1;
	}

	public int getNextPage() {
		return getPageNo() + 1 > getPageCount() ? pageCount : pageNo + 1;
	}
}
