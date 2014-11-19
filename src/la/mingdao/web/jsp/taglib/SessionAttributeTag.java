package la.mingdao.web.jsp.taglib;

import la.mingdao.common.UserSession;
import org.guiceside.web.jsp.taglib.BaseTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @version 1.0 2008-12-9
 * @since JDK1.5
 */
public class SessionAttributeTag extends BaseTag {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String value;

    @Override
    public int doEndTag() throws JspException {
        return Tag.EVAL_PAGE;
    }

    @Override
    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext
                .getRequest();
        Object result = null;
        try {
            result = UserSession.getAttribute(request, value);
        } catch (Exception e) {
            result = null;
        }
        outprint(result != null ? result.toString() : "");
        return Tag.SKIP_BODY;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}