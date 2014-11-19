package org.guiceside.web.jsp.taglib;


import ognl.OgnlException;
import org.guiceside.commons.lang.BeanUtils;
import org.guiceside.commons.lang.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import java.util.Collection;

/**
 * 
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 2008-10-14
 * 
 **/
public class PropertyTag extends BaseTag {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String value;

	
	
	private boolean emptyStr = false;

	@Override
	public int doStartTag() throws JspException {
		String result=null;
		
		if(StringUtils.isNotBlank(value)){
			int signBefore=value.indexOf("[");
			int signAfter=value.indexOf("]");
			if(signBefore!=-1&&signAfter!=-1){
				String strIndex=value.substring(signBefore+1,signAfter);
				Integer index=BeanUtils.convertValue(strIndex, Integer.class);
				if(index!=null){
					int i=index.intValue();
					String beforeExpression=value.substring(0,signBefore);
					String afterExpression=value.substring(signAfter+2);
					String[] propertys=beforeExpression.split("\\.");
					Object root=null;
					if(propertys!=null&&propertys.length>0){
						String first=propertys[0];
						root=getHttpServletRequest().getAttribute(first);
						if(root!=null){
							beforeExpression=beforeExpression.substring(first.length()+1);
							try {
								root=BeanUtils.getValue(root, beforeExpression);
							} catch (OgnlException e) {
								root=null;
							}
						}
					}else{
						root=beforeExpression;
					}
					if(root!=null){
						Object[] arrayObject=null;
						if(root instanceof Collection<?>){
							Collection<?> collection=(Collection<?>) root;
							arrayObject=collection.toArray();
							if(arrayObject!=null&&arrayObject.length>0){
								try {
									result=BeanUtils.getValue(arrayObject, "["+i+"]."+afterExpression,String.class);
								} catch (OgnlException e) {
									result=null;
								}
							}
						}else if(root instanceof Object[]){
							try {
								result=BeanUtils.getValue(root, "["+i+"]."+afterExpression,String.class);
							} catch (OgnlException e) {
								result=null;
							}
						}
					}
				}
			}else{
				String[] propertys=value.split("\\.");
				Object root=null;
				if(propertys!=null&&propertys.length>0){
					String first=propertys[0];
					root=getHttpServletRequest().getAttribute(first);
					if(root!=null){
						value=value.substring(first.length()+1);
						try {
							root=BeanUtils.getValue(root, value);
						} catch (OgnlException e) {
							root=null;
						}
					}
				}
			}
		}
		result=StringUtils.defaultIfEmpty(result);
		this.outprint(result);
		return Tag.SKIP_BODY;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isEmptyStr() {
		return emptyStr;
	}

	public void setEmptyStr(boolean emptyStr) {
		this.emptyStr = emptyStr;
	}

	
}
