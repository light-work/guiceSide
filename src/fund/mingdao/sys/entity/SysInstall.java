package fund.mingdao.sys.entity;

import org.guiceside.persistence.entity.IdEntity;
import org.guiceside.persistence.entity.Tracker;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 12-8-15
 * Time: 下午4:26
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "SYS_INSTALL")
public class SysInstall extends IdEntity implements Tracker {

    private static final long serialVersionUID = 1L;

    private Long id;

    private SysCompany cpnyId;

    private String userId;

    private String userName;

    private String confirmYn;

    private String uninstallYn;

    private String uninstallUserId;

    private String uninstallUserName;

    private Date uninstallDate;

    private Date created;

    private String createdBy;

    private Date updated;

    private String updatedBy;

    private String useYn;

    private String tip;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Column(name = "CREATED", updatable = false)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Column(name = "CREATED_BY")
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "UPDATED")
    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Column(name = "UPDATED_BY")
    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Column(name = "USE_YN")
    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }



    @Column(name = "USER_ID")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "USER_NAME")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "CONFIRM_YN")
    public String getConfirmYn() {
        return confirmYn;
    }

    public void setConfirmYn(String confirmYn) {
        this.confirmYn = confirmYn;
    }

    @Transient
    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    @Column(name = "UNINSTALL_YN")
    public String getUninstallYn() {
        return uninstallYn;
    }

    public void setUninstallYn(String uninstallYn) {
        this.uninstallYn = uninstallYn;
    }

    @Column(name = "UNINSTALL_USER_ID")
    public String getUninstallUserId() {
        return uninstallUserId;
    }

    public void setUninstallUserId(String uninstallUserId) {
        this.uninstallUserId = uninstallUserId;
    }

    @Column(name = "UNINSTALL_USER_NAME")
    public String getUninstallUserName() {
        return uninstallUserName;
    }

    public void setUninstallUserName(String uninstallUserName) {
        this.uninstallUserName = uninstallUserName;
    }

    @Column(name = "UNINSTALL_DATE")
    public Date getUninstallDate() {
        return uninstallDate;
    }

    public void setUninstallDate(Date uninstallDate) {
        this.uninstallDate = uninstallDate;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CPNY_ID")
    public SysCompany getCpnyId() {
        return cpnyId;
    }

    public void setCpnyId(SysCompany cpnyId) {
        this.cpnyId = cpnyId;
    }
}
