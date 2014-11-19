/**
 * Copyright (C) 2006 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.guiceside.web.dispatcher;

import com.google.inject.OutOfScopeException;
import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



/**
 * <p>
 * GuiceSideFilter将ServletContext压入ThreadLocal提供给Guice调用
 * 根据初始化参数设置encoding
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 *
 */
public class GuiceSideFilter implements Filter {

	protected FilterConfig filterConfig;

	private String encoding;
	
	static final ThreadLocal<Context> localContext = new ThreadLocal<Context>();

	/**
	 * 设置servletContext<br/>
	 * 根据Filter参数设置characterEncoding<br/>
	 * @param servletRequest
	 * @param servletResponse
	 * @param filterChain
	 */
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		ServletContext servletContext = filterConfig.getServletContext();
		Context previous = localContext.get();
		try {
			HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
			HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
			if(StringUtils.isNotBlank(encoding)){
				httpServletRequest.setCharacterEncoding(encoding);
				httpServletResponse.setCharacterEncoding(encoding);
			}
            httpServletResponse.setHeader("P3P", "CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
			localContext.set(new Context(servletContext));
			filterChain.doFilter(servletRequest, servletResponse);
		} finally {
			localContext.set(previous);

		}
	}


	public static ServletContext getServletContext() {
		return getContext().getServletContext();
	}
	
	
	static Context getContext() {
		Context context = localContext.get();
		if (context == null) {
			throw new OutOfScopeException(
					"Cannot access scoped object. Either we"
							+ " are not currently inside an HTTP Servlet request, or you may"
							+ " have forgotten to apply "
							+ GuiceSideFilter.class.getName()
							+ " as a servlet filter for this request.");
		}
		return context;
	}
	
	
	static class Context {
		final ServletContext servletContext;
		
		Context(ServletContext servletContext) {
			this.servletContext = servletContext;
		}

		ServletContext getServletContext() {
			return servletContext;
		}
		

	}
	
	

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		encoding=filterConfig.getInitParameter("encoding");
	}

	public void destroy() {
	}

	
}
