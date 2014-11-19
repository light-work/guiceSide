package org.guiceside.support.limit;


import java.util.Collection;
import java.util.List;




/**
 * 自定义限制结果集List
 * @author zhenjia
 *
 */
public class ListLimit<T extends Object> implements Ilimit<T> {

	private int begin = 0;

	private int max = 0;

	private List<T> list = null;

	public ListLimit() {

	}

	public ListLimit(List<T> list) {
		max = list.size();
		this.list = list;
	}
	
	/**
	 * 得到记录总数
	 */
	public int getRows() {
		return list == null ? 0 : list.size();
	}

	/**
	 * 获取限制条件以后的List
	 */
	public List<T> getResult() {
		return list.subList(begin, (begin + max > list.size() ? list.size()
				: begin + max));
	}

	/**
	 * 获取设置限制条件以后的List
	 */
	public List<T> getResult(int begin, int max) {
		setFirstResult(begin);
		setMaxResults(max);
		return getResult();
	}

	/**
	 * 设置开始索引
	 */
	public void setFirstResult(int begin) {
		this.begin = list.size() < begin ? list.size() : begin;
	}

	/**
	 * 设置获取最大值
	 */
	public void setMaxResults(int max) {
		this.max = max;
	}

	public void setParaValues(Collection<?> collection) {
		// TODO Auto-generated method stub

	}
	
	

}
