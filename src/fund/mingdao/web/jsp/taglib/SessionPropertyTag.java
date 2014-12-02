package fund.mingdao.web.jsp.taglib;


import org.guiceside.commons.lang.BeanUtils;
import org.guiceside.web.jsp.taglib.BaseTag;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import java.io.IOException;

/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @version 1.0 2008-12-9
 * @since JDK1.5
 */
public class SessionPropertyTag extends BaseTag {

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
        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
//////        UserInfo userInfo = null;
//////        try {
//////            userInfo = UserSession.getUserInfo(request);
//////        } catch (UserSessionException e) {
//////            try {
//////                response.sendRedirect("/view/common/urlGoLogin.jsp");
//////            } catch (IOException e1) {
//////                // TODO Auto-generated catch block
//////                e1.printStackTrace();
//////            }
//////        } catch (Exception e) {
//////            try {
//////                request.getRequestDispatcher("/view/common/error.jsp").forward(
//////                        request, pageContext.getResponse());
//////            } catch (ServletException e1) {
//////                // TODO Auto-generated catch block
//////                e1.printStackTrace();
//////            } catch (IOException e1) {
//////                // TODO Auto-generated catch block
//////                e1.printStackTrace();
//////            }
//////        }
////
////        Object result = null;
////        try {
////            result = BeanUtils.getValue(userInfo, value);
////        } catch (Exception e) {
////            result = null;
////        }
//        outprint(result != null ? result.toString() : "");
        return Tag.SKIP_BODY;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}