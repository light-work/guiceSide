package org.guiceside.commons;

import java.util.List;


/**
 * <p>
 * 封装了Page对象的类
 * 提供完整的Page描述
 * </p>
 * @author zhenjia  <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 * 
 */
public class Page<T> {

	/** 是否有上一页 */
	private boolean hasPrePage;

	/** 是否有下一页 */
	private boolean hasNextPage;

	/** 每一页的大小 */
	private int everyPage;

	/** 总页数 */
	private int totalPage;

	/** 当前页数 */
	private int currentPage;

    private int preIndex;

    private int nextIndex;

	/** 开始索引 */
	private int beginIndex;
	
	/**总记录数*/
	private int totalRecord;
	
	private int pageBeginIndex;
	
	private List<T> resultList;

	
	/** 默认构造函数 */
	public Page() {

	}

	/**
	 * 每页大小构造函数
	 * 
	 * @param everyPage
	 */
	public Page(int everyPage) {
		setEveryPage(everyPage);
	}
	
	public Page(int everyPage,int beginIndex) {
		setEveryPage(everyPage);
		setBeginIndex(beginIndex);
	}

	/**
	 * 全部属性构造函数
	 * @param hasPrePage
	 * @param hasNextPage
	 * @param everyPage
	 * @param totalPage
	 * @param currentPage
	 * @param beginIndex
	 */
	public Page(boolean hasPrePage, boolean hasNextPage, int everyPage,
			int totalPage, int currentPage, int beginIndex,int totalRecord,int pageBeginIndex) {
		setHasPrePage(hasPrePage);
		setHasNextPage(hasNextPage);
		setEveryPage(everyPage);
		setTotalPage(totalPage);
		setCurrentPage(currentPage);
		setBeginIndex(beginIndex);
		setTotalRecord(totalRecord);
		setPageBeginIndex(pageBeginIndex);
	}

	/**
	 * 
	 * @return 返回当前记录开始索引
	 */
	public int getBeginIndex() {
		return beginIndex;
	}

	public void setBeginIndex(int beginIndex) {
		this.beginIndex = beginIndex;
	}

	/**
	 * 
	 * @return 返回当前开始页面索引
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * 
	 * @return 返回当前每页大小
	 */
	public int getEveryPage() {
		return everyPage;
	}

	public void setEveryPage(int everyPage) {
		this.everyPage = everyPage;
	}

	/**
	 * 
	 * @return 是否存在下一页
	 */
	public boolean isHasNextPage() {
		return hasNextPage;
	}

	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	/**
	 * 
	 * @return 是否存在上一页
	 */
	public boolean isHasPrePage() {
		return hasPrePage;
	}

	public void setHasPrePage(boolean hasPrePage) {
		this.hasPrePage = hasPrePage;
	}

	/**
	 * 
	 * @return 返回总页数
	 */
	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	/**
	 * 
	 * @return 返回总记录数
	 */
	public int getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}

	/**
	 * 
	 * @return 返回当前页面开始索引
	 */
	public int getPageBeginIndex() {
		return pageBeginIndex;
	}

	public void setPageBeginIndex(int pageBeginIndex) {
		this.pageBeginIndex = pageBeginIndex;
	}

	/**
	 * 
	 * @return 返回result
	 */
	public List<T> getResultList() {
		return resultList;
	}

	public void setResultList(List<T> resultList) {
		this.resultList = resultList;
	}

    public int getPreIndex() {
        return beginIndex<=0?0:beginIndex-everyPage;
    }


    public int getNextIndex() {
        return beginIndex+everyPage;
    }



}
