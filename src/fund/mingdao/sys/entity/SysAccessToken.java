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
@Table(name = "SYS_ACCESS_TOKEN")
public class SysAccessToken extends IdEntity implements Tracker {

    private static final long serialVersionUID = 1L;

    private Long id;

    private SysCompany cpnyId;

    private SysUser userId;


    private String token;

    private String refreshToken;

    private Date created;

    private String createdBy;

    private Date updated;

    private String updatedBy;

    private String useYn;

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


    @Column(name = "TOKEN")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Column(name = "REFRESH_TOKEN")
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CPNY_ID")
    public SysCompany getCpnyId() {
        return cpnyId;
    }

    public void setCpnyId(SysCompany cpnyId) {
        this.cpnyId = cpnyId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    public SysUser getUserId() {
        return userId;
    }

    public void setUserId(SysUser userId) {
        this.userId = userId;
    }
}
