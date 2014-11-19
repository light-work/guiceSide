package la.mingdao.common;



import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @version 1.0 2008-10-30
 * @since JDK1.5
 */
public class UserInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String DEFAULT_LANGUAGE_PREFERENCE = "zh";

    public static final String DEFAULT_COUNTRY_PREFERENCE = "CN";

    private String languagePreference = DEFAULT_LANGUAGE_PREFERENCE;

    private String countryPreference = DEFAULT_COUNTRY_PREFERENCE;

    public static final Locale DEFAULT_Locale = new Locale(
            DEFAULT_LANGUAGE_PREFERENCE, DEFAULT_COUNTRY_PREFERENCE);

    private boolean authorize = false;

    private boolean loggedIn = false;

    private String appKey;

    private String appSecret;

    private String redirectUri;

    private String sessionId;

    private String userId;

    private String userName;

    private String department;

    private String job;

    private String companyName;

    private String companyNameEn;

    private String companyId;

    private String workSite;

    private String jobNumber;

    private String mobilePhone;

    private String workPhone;

    private String avstar100;

    private String avstar;

    private String logo;

    private String accessToken;

    private String topMenuCss;

    private String menuCss;

    private Integer taskUnRead;

    private Integer taskUnApprove;

    private Integer executeUnRead;

    private Integer executeUnState;

    private Integer executeUnConfirm;

    private Integer reqPassed;

    private Integer reqRejected;

    private Integer reqConfirm;

    private ResourceBundle zhCNResourceBundle;


    private ResourceBundle enUSResourceBundle;


    private Locale locale;

    private String viewCompanyId;

    private String viewUserId;

    private Long viewReqId;

    private Long viewTaskId;

    private Long viewExecuteId;

    private Integer applyCount;

    private String themeType;

    private boolean admin;

    private boolean crmAdmin;

    private String mobileTab;

    private String headerHtml;

    private String leftHtml;

    private String footerHtml;

    private Integer authorizeDay;

    private Integer tryDay;

    private Integer diffDay;

    private Integer packageId;


    private String conferenceUID;

    private Long conferenceId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isAuthorize() {
        return authorize;
    }

    public void setAuthorize(boolean authorize) {
        this.authorize = authorize;
    }


    public String getLanguagePreference() {
        return languagePreference;
    }

    public void setLanguagePreference(String languagePreference) {
        this.languagePreference = languagePreference;
    }

    public String getCountryPreference() {
        return countryPreference;
    }

    public void setCountryPreference(String countryPreference) {
        this.countryPreference = countryPreference;
    }


    public Locale getLocale() {
        if (locale == null) {
            locale = new Locale(this.getLanguagePreference(), this
                    .getCountryPreference());
            return locale;
        } else {
            return locale;
        }
    }


    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTopMenuCss() {
        return topMenuCss;
    }

    public void setTopMenuCss(String topMenuCss) {
        this.topMenuCss = topMenuCss;
    }

    public String getMenuCss() {
        return menuCss;
    }

    public void setMenuCss(String menuCss) {
        this.menuCss = menuCss;
    }

    public String getWorkSite() {
        return workSite;
    }

    public void setWorkSite(String workSite) {
        this.workSite = workSite;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getAvstar100() {
        return avstar100;
    }

    public void setAvstar100(String avstar100) {
        this.avstar100 = avstar100;
    }

    public String getAvstar() {
        return avstar;
    }

    public void setAvstar(String avstar) {
        this.avstar = avstar;
    }

    public String getCompanyNameEn() {
        return companyNameEn;
    }

    public void setCompanyNameEn(String companyNameEn) {
        this.companyNameEn = companyNameEn;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public ResourceBundle getZhCNResourceBundle() {
        return zhCNResourceBundle;
    }

    public void setZhCNResourceBundle(ResourceBundle zhCNResourceBundle) {
        this.zhCNResourceBundle = zhCNResourceBundle;
    }

    public ResourceBundle getEnUSResourceBundle() {
        return enUSResourceBundle;
    }

    public void setEnUSResourceBundle(ResourceBundle enUSResourceBundle) {
        this.enUSResourceBundle = enUSResourceBundle;
    }

    public Integer getTaskUnRead() {
        return taskUnRead;
    }

    public void setTaskUnRead(Integer taskUnRead) {
        this.taskUnRead = taskUnRead;
    }

    public Integer getTaskUnApprove() {
        return taskUnApprove;
    }

    public void setTaskUnApprove(Integer taskUnApprove) {
        this.taskUnApprove = taskUnApprove;
    }

    public Integer getReqPassed() {
        return reqPassed;
    }

    public void setReqPassed(Integer reqPassed) {
        this.reqPassed = reqPassed;
    }

    public Integer getReqRejected() {
        return reqRejected;
    }

    public void setReqRejected(Integer reqRejected) {
        this.reqRejected = reqRejected;
    }

    public Long getViewReqId() {
        return viewReqId;
    }

    public void setViewReqId(Long viewReqId) {
        this.viewReqId = viewReqId;
    }

    public Long getViewTaskId() {
        return viewTaskId;
    }

    public void setViewTaskId(Long viewTaskId) {
        this.viewTaskId = viewTaskId;
    }


    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getThemeType() {
        return themeType;
    }

    public void setThemeType(String themeType) {
        this.themeType = themeType;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public Integer getExecuteUnRead() {
        return executeUnRead;
    }

    public void setExecuteUnRead(Integer executeUnRead) {
        this.executeUnRead = executeUnRead;
    }

    public Integer getExecuteUnState() {
        return executeUnState;
    }

    public void setExecuteUnState(Integer executeUnState) {
        this.executeUnState = executeUnState;
    }

    public Long getViewExecuteId() {
        return viewExecuteId;
    }

    public void setViewExecuteId(Long viewExecuteId) {
        this.viewExecuteId = viewExecuteId;
    }

    public String getMobileTab() {
        return mobileTab;
    }

    public void setMobileTab(String mobileTab) {
        this.mobileTab = mobileTab;
    }

    public String getHeaderHtml() {
        return headerHtml;
    }

    public void setHeaderHtml(String headerHtml) {
        this.headerHtml = headerHtml;
    }

    public String getLeftHtml() {
        return leftHtml;
    }

    public void setLeftHtml(String leftHtml) {
        this.leftHtml = leftHtml;
    }

    public String getFooterHtml() {
        return footerHtml;
    }

    public void setFooterHtml(String footerHtml) {
        this.footerHtml = footerHtml;
    }



    public Integer getApplyCount() {
        if (applyCount > 0) {
            int mu = applyCount / 4;
            int mo = applyCount % 4;
            if (mo > 0) {
                mu += 1;
            }
            return mu;
        } else {
            return 1;
        }
    }

    public void setApplyCount(Integer applyCount) {
        this.applyCount = applyCount;
    }


    public String getViewCompanyId() {
        return viewCompanyId;
    }

    public void setViewCompanyId(String viewCompanyId) {
        this.viewCompanyId = viewCompanyId;
    }

    public String getViewUserId() {
        return viewUserId;
    }

    public void setViewUserId(String viewUserId) {
        this.viewUserId = viewUserId;
    }

    public Integer getAuthorizeDay() {
        return authorizeDay;
    }

    public void setAuthorizeDay(Integer authorizeDay) {
        this.authorizeDay = authorizeDay;
    }

    public Integer getTryDay() {
        return tryDay;
    }

    public void setTryDay(Integer tryDay) {
        this.tryDay = tryDay;
    }

    public Integer getDiffDay() {
        return diffDay;
    }

    public void setDiffDay(Integer diffDay) {
        this.diffDay = diffDay;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public Integer getExecuteUnConfirm() {
        return executeUnConfirm;
    }

    public void setExecuteUnConfirm(Integer executeUnConfirm) {
        this.executeUnConfirm = executeUnConfirm;
    }

    public Integer getReqConfirm() {
        return reqConfirm;
    }

    public void setReqConfirm(Integer reqConfirm) {
        this.reqConfirm = reqConfirm;
    }

    public boolean isCrmAdmin() {
        return crmAdmin;
    }

    public void setCrmAdmin(boolean crmAdmin) {
        this.crmAdmin = crmAdmin;
    }

    public String getConferenceUID() {
        return conferenceUID;
    }

    public void setConferenceUID(String conferenceUID) {
        this.conferenceUID = conferenceUID;
    }

    public Long getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(Long conferenceId) {
        this.conferenceId = conferenceId;
    }
}
