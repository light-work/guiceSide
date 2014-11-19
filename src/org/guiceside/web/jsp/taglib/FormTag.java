package org.guiceside.web.jsp.taglib;

import org.apache.commons.lang.StringUtils;
import org.guiceside.commons.lang.BeanUtils;
import org.guiceside.commons.lang.DateFormatUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.lang.reflect.Field;
import java.util.Date;

public class FormTag extends BodyTagSupport {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String beanName = null;

    private String scope = null;

    private Object bean = null;

    private boolean fromRequest = false;

    /**
     * Sets bean names with value of the "bean" attribute.
     *
     * @param v bean names
     */
    public void setBean(String v) {
        beanName = v;
    }

    /**
     * Gets bean names.
     *
     * @return bean names
     */
    public String getBean() {
        return beanName;
    }

    /**
     * Sets the value of "scope" attribute, that represent beans scope.
     *
     * @param v
     */
    public void setScope(String v) {
        scope = v;
    }

    public void setFromRequest(boolean fromRequest) {
        this.fromRequest = fromRequest;
    }

    /**
     * Return value of the "scope" attribute
     *
     * @return bean scopes
     */
    public String getScope() {
        return scope;
    }


    /**
     * Copies properties of all specified bean into one map.
     *
     * @return EVAL_BODY_AGAIN
     */
    public int doStartTag() {
        if (!fromRequest) {
            if (getScope() == null) {
                scope = "";
            }
            scope.toLowerCase();

            HttpServletRequest request = (HttpServletRequest) pageContext
                    .getRequest();
            HttpSession session = (HttpSession) pageContext.getSession();
            if (scope.equals("page")) {
                bean = pageContext.getAttribute(beanName);
            } else if (scope.equals("request")) {
                bean = request.getAttribute(beanName);
            } else if (scope.equals("session")) {
                bean = session.getAttribute(beanName);
            }

        }
        return EVAL_BODY_AGAIN;
    }

    /**
     * Performs smart form population.
     *
     * @return SKIP_BODY
     */
    public int doAfterBody() {
        BodyContent body = getBodyContent();

        try {
            JspWriter out = body.getEnclosingWriter();
            String bodytext = body.getString();

            if (bean != null || fromRequest == true) {
                bodytext = populateForm(bodytext, bean);
            }

            out.print(bodytext);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return SKIP_BODY;
    }

    /**
     * End of tag.
     *
     * @return EVAL_PAGE
     */
    public int doEndTag() {
        return EVAL_PAGE;
    }

    private String populateForm(String html, Object bean) {
        int i = 0;
        int s = 0;
        StringBuffer result = new StringBuffer(html.length());
        String currentSelectName = null;
        HttpServletRequest request = (HttpServletRequest) pageContext
                .getRequest();

        while (true) {
            // find starting tag
            i = html.indexOf('<', s);

            if (i == -1) {
                result.append(html.substring(s));

                break; // input tag not found
            }

            result.append(html.substring(s, i)); // tag found, all before tag
            // is stored
            s = i;

            // find closing tag
            i = html.indexOf('>', i);

            if (i == -1) {
                result.append(html.substring(s));

                break; // closing tag not found
            }

            i++;

            // match tags
            String tag = html.substring(s, i);
            String tagName = HtmlUtil.getTagName(tag);
            String tagValueTemp = null;
            if (tagName.equalsIgnoreCase("input") == true) {
                String tagType = HtmlUtil.getAttribute(tag, "type");
                if (tagType != null) {
                    String name = HtmlUtil.getAttribute(tag, "name");
                    name = getOgnlExpression(name);
                    String value = null;
                    try {
                        if (!fromRequest) {
                            value = BeanUtils.getProperty(bean, name);
                            Field field = BeanUtils.getDeclaredField(bean, name);
                            if (field != null) {
                                Class classz = field.getType();
                                if (classz.getName().equals(Date.class.getName())) {
                                    Object co = BeanUtils.getValue(bean, name);
                                    if (co != null) {
                                        Date cd = BeanUtils.convertValue(co, Date.class);
                                        if (cd != null) {
                                            int h = DateFormatUtil.getDayInHour(cd);
                                            int m = DateFormatUtil.getDayInMinute(cd);
                                            if (h <= 0 && m <= 0) {
                                                value = DateFormatUtil.format(cd, DateFormatUtil.YEAR_MONTH_DAY_PATTERN);
                                            }
                                        }
                                    }
                                } else {
                                    value = BeanUtils.getProperty(bean, name);
                                }
                            }
                        } else {
                            value = request.getParameter(name);
                        }
                    } catch (Exception e) {

                    }
                    if (value != null) {
                        tagType = tagType.toLowerCase();

                        if (tagType.equals("text")) {
                            tagValueTemp = HtmlUtil.getAttribute(tag, "value");
                            if (StringUtils.isBlank(tagValueTemp)) {
                                tag = HtmlUtil
                                        .addAttribute(tag, "value", value);
                            }
                        }

                        if (tagType.equals("hidden")) {
                            tagValueTemp = HtmlUtil.getAttribute(tag, "value");
                            if (StringUtils.isBlank(tagValueTemp)) {
                                tag = HtmlUtil
                                        .addAttribute(tag, "value", value);
                            }
                        }

                        if (tagType.equals("image")) {
                            tag = HtmlUtil.addAttribute(tag, "value", value);
                        }

                        if (tagType.equals("password")) {
                            tag = HtmlUtil.addAttribute(tag, "value", value);
                        }

                        if (tagType.equals("checkbox")) {
                            String tagValue = HtmlUtil.getAttribute(tag,
                                    "value");

                            if (tagValue == null) {
                                tagValue = "true";
                            }

                            if (tagValue.equals(value)) {
                                tag = HtmlUtil.addAttribute(tag, "checked");
                            }
                        }

                        if (tagType.equals("radio")) {
                            String tagValue = HtmlUtil.getAttribute(tag,
                                    "value");

                            if (tagValue != null) {
                                if (tagValue.equals(value)) {
                                    tag = HtmlUtil.addAttribute(tag, "checked");
                                }
                            }
                        }
                    }
                }
            } else if (tagName.equalsIgnoreCase("textarea") == true) {
                String name = HtmlUtil.getAttribute(tag, "name");
                name = getOgnlExpression(name);
                String value = null;
                try {
                    if (!fromRequest)
                        value = BeanUtils.getProperty(bean, name);
                    else
                        value = request.getParameter(name);
                } catch (Exception e) {

                }
                if (value != null) {
                    tag += HtmlEncoder.encode(value);
                }
            } else if (tagName.equalsIgnoreCase("select") == true) {
                currentSelectName = HtmlUtil.getAttribute(tag, "name");
                currentSelectName = getOgnlExpression(currentSelectName);
            } else if (tagName.equalsIgnoreCase("/select") == true) {
                currentSelectName = null;
            } else if (tagName.equalsIgnoreCase("option") == true) {
                if (currentSelectName != null) {
                    String tagValue = HtmlUtil.getAttribute(tag, "value");

                    String value = null;
                    try {

                        if (!fromRequest)
                            value = BeanUtils.getProperty(bean,
                                    currentSelectName);
                        else
                            value = request.getParameter(currentSelectName);
                    } catch (Exception e) {

                    }
                    if (tagValue != null) {
                        if (value != null) {
                            if (value.equals(tagValue)) {
                                tag = HtmlUtil.addAttribute(tag, "selected");
                            }
                        }
                    }
                }
            }
            result.append(tag);
            s = i;
        }

        return result.toString();
    }

    private String getOgnlExpression(String name) {
        if (name != null) {
            String subKey = beanName + ".";
            if (name.startsWith(subKey)) {
				name = name.substring(subKey.length());
			}
		}
		return name;
	}
}
