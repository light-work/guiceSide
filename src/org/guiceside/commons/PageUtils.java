package org.guiceside.commons;



/**
 * <p>
 * 分页工具类 包含了分页的具体算法
 * 该类所有方法均为静态方法
 * 返回Page对象
 * </p>
 * @author zhenjia  <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 *
 */
public class PageUtils {
	

	/**
	 * 
	 * 得到Page对象
	 * 
	 * @param page  Page对象
	 * @param totalRecord  总记录数
	 * @return 返回page
	 */
	public static Page createPage(Page page, int totalRecord) {
		return createPage(page.getEveryPage(), page.getCurrentPage(), totalRecord);
	}

	/**
	 * 
	 * 得到Page对象
	 * 
	 * @param everyPage 每页显示大小
	 * @param currentPage 当前页索引
	 * @param totalRecord 总记录数
	 * @return 返回page
	 */
	public static Page createPage(int everyPage, int currentPage,
			int totalRecord) {
		everyPage = getEveryPage(everyPage);
		currentPage = getCurrentPage(currentPage);
		int beginIndex = getBeginIndex(everyPage, currentPage);
		int totalPage = getTotalPage(everyPage, totalRecord);
		boolean hasNextPage = hasNextPage(currentPage, totalPage);
		boolean hasPrePage = hasPrePage(currentPage);
		int pageBeginIndex=getPageBeginIndex(currentPage,everyPage);
		return new Page(hasPrePage, hasNextPage, everyPage, totalPage,
				currentPage, beginIndex,totalRecord,pageBeginIndex);
	}
	
	

	/**
	 * 
	 * 通过算法得到每页大小
	 * 默认为20
	 * 
	 * @param everyPage
	 * @return 返回每页大小
	 */
	private static int getEveryPage(int everyPage) {
		return everyPage == 0 ? 20 : everyPage;
	}

	/**
	 * 
	 * 通过算法得到当前页索引
	 * 默认为1
	 * 
	 * @param currentPage
	 * @return 返回当前页索引
	 */
	private static int getCurrentPage(int currentPage) {
		return currentPage == 0 ? 1 : currentPage;
	}

	/**
	 * 
	 * 通过算法得到数据索引
	 * 
	 * @param everyPage
	 * @param currentPage
	 * @return 返回当前数据索引
	 */
	private static int getBeginIndex(int everyPage, int currentPage) {
		return (currentPage - 1) * everyPage;
	}

	/**
	 * 
	 * 通过算法得到页的总数
	 * 
	 * @param everyPage
	 * @param totalRecords
	 * @return 返回页总数
	 */
	private static int getTotalPage(int everyPage, int totalRecords) {
		int totalPage = 0;

		if (totalRecords % everyPage == 0)
			totalPage = totalRecords / everyPage;
		else
			totalPage = totalRecords / everyPage + 1;

		return totalPage;
	}

	/**
	 * 
	 * 通过算法判断是否存在上一页
	 * 
	 * @param currentPage
	 * @return 是否存在上一页
	 */
	private static boolean hasPrePage(int currentPage) {
		return currentPage == 1 ? false : true;
	}

	/**
	 * 
	 * 通过算法判断是否存在下一页
	 * 
	 * @param currentPage
	 * @param totalPage
	 * @return 是否存在下一页
	 */
	private static boolean hasNextPage(int currentPage, int totalPage) {
		return currentPage == totalPage || totalPage == 0 ? false : true;
	}
	
	/**
	 * 
	 * @param currentPage
	 * @param everyPage
	 * @return 返回当前开始页索引
	 */
	@SuppressWarnings("unused")
	private static int getPageBeginIndex(int currentPage,int everyPage){
		if(currentPage/everyPage==0&&currentPage%everyPage<everyPage){
			return 0;
		}else if(currentPage/everyPage>0&currentPage%everyPage==0){
			return currentPage/everyPage-1;
		}else if(currentPage/everyPage>0&&currentPage%everyPage<10){
			return currentPage/everyPage;
		}
		return 0;
	}
}
