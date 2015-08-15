/**
 * 
 */
package org.beetl.sql.core.db;

import java.io.Serializable;
import java.util.List;

/**
 * @author suxj
 *
 */
public class Page<T> implements Serializable{

	private static final long serialVersionUID = -7523359884334787081L;
	
	private List<T> list;		//分页结果List
	private int pageNumber;		//页数
	private int pageSize;		//每页记录数
	private int totalPage;		//总页数
	private int totalRow;		//总行数
	
	public Page(List<T> list, int pageNumber, int pageSize, int totalPage, int totalRow){
		this.list = list;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.totalPage = totalPage;
		this.totalRow = totalRow;
	}
	
	public List<T> getList() {
		return list;
	}
	
	public int getPageNumber() {
		return pageNumber;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	
	public int getTotalPage() {
		return totalPage;
	}
	
	public int getTotalRow() {
		return totalRow;
	}
	
	public boolean isFirstPage() {
		return pageNumber == 1;
	}
	
	public boolean isLastPage() {
		return pageNumber == totalPage;
	}

}
