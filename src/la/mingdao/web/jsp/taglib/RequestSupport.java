package la.mingdao.web.jsp.taglib;


import org.apache.commons.lang.StringUtils;
import org.guiceside.web.jsp.taglib.BaseTag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;


public class RequestSupport extends BaseTag {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String key;

    @Override
    public int doEndTag() throws JspException {
        return Tag.EVAL_PAGE;
    }

    @Override
    public int doStartTag() throws JspException {
        String str = null;

        if (StringUtils.isBlank(getKey())) {
            str = "";
        } else {
            if (getKey().equals("host")) {
                str = pageContext.getRequest().getServerName() + ":" + pageContext.getRequest().getServerPort();
            } else if (getKey().equals("remoteAddr")) {
                str = pageContext.getRequest().getRemoteAddr();
            }
        }
        outprint(str);
        return Tag.SKIP_BODY;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}