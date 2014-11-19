package la.mingdao.common.entity;

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
@Table(name = "CONFERENCE")
public class Conference extends IdEntity implements Tracker {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String companyId;

    private String conferenceUID;

    private String subject;

    private String joinPassword;

    private String timingYn;

    private Date timingDate;

    private Integer timingHour;

    private Integer timingMinute;

    private Integer record;

    private Integer grab;

    private Integer seqSpeak;

    private String remarks;

    private String recordUrl;

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

    @Column(name = "COMPANY_ID")
    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }


    @Column(name = "SUBJECT")
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Column(name = "JOIN_PASSWORD")
    public String getJoinPassword() {
        return joinPassword;
    }

    public void setJoinPassword(String joinPassword) {
        this.joinPassword = joinPassword;
    }

    @Column(name = "REMARKS")
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Column(name = "TIMING_YN")
    public String getTimingYn() {
        return timingYn;
    }

    public void setTimingYn(String timingYn) {
        this.timingYn = timingYn;
    }

    @Column(name = "TIMING_DATE")
    public Date getTimingDate() {
        return timingDate;
    }

    public void setTimingDate(Date timingDate) {
        this.timingDate = timingDate;
    }

    @Column(name = "TIMING_HOUR")
    public Integer getTimingHour() {
        return timingHour;
    }

    public void setTimingHour(Integer timingHour) {
        this.timingHour = timingHour;
    }

    @Column(name = "TIMING_MINUTE")
    public Integer getTimingMinute() {
        return timingMinute;
    }

    public void setTimingMinute(Integer timingMinute) {
        this.timingMinute = timingMinute;
    }

    @Column(name = "CONFERENCE_UID")
    public String getConferenceUID() {
        return conferenceUID;
    }

    public void setConferenceUID(String conferenceUID) {
        this.conferenceUID = conferenceUID;
    }

    @Column(name = "RECORD")
    public Integer getRecord() {
        return record;
    }

    public void setRecord(Integer record) {
        this.record = record;
    }

    @Column(name = "GRAB")
    public Integer getGrab() {
        return grab;
    }

    public void setGrab(Integer grab) {
        this.grab = grab;
    }

    @Column(name = "SEQ_SPEAK")
    public Integer getSeqSpeak() {
        return seqSpeak;
    }

    public void setSeqSpeak(Integer seqSpeak) {
        this.seqSpeak = seqSpeak;
    }

    @Column(name = "RECORD_URL")
    public String getRecordUrl() {
        return recordUrl;
    }

    public void setRecordUrl(String recordUrl) {
        this.recordUrl = recordUrl;
    }
}
