package la.mingdao.common.service;

import com.google.inject.Singleton;
import la.mingdao.common.entity.Conference;
import org.guiceside.persistence.TransactionType;
import org.guiceside.persistence.Transactional;
import org.guiceside.persistence.hibernate.dao.hquery.HQuery;

import java.util.Date;
import java.util.List;

/**
 * @author zhenjiaWang
 * @version 1.0 2012-05
 * @since JDK1.5
 */
@Singleton
public class ConferenceService extends HQuery {


    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public Conference getById(Long id) {
        return $(id).get(Conference.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Conference getByUID(String conferenceUID) {
        return $($eq("conferenceUID",conferenceUID)).get(Conference.class);
    }


    @Transactional(type = TransactionType.READ_ONLY)
    public List<Conference> getListByTimingHour(Date timingDate,Integer timingHour) {
        return $($eq("timingDate",timingDate),$eq("timingHour",timingHour)).list(Conference.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<Conference> getListByTiming(Date timingDate,Integer timingHour,Integer timingMinute) {
        return $($eq("timingDate",timingDate),$eq("timingHour",timingHour),$eq("timingMinute",timingMinute)).list(Conference.class);
    }


    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(Conference conference) {
        $(conference).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(Conference conference) {
        $(conference).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(Conference.class);
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<Conference> conferenceList) {
        $(conferenceList).save();
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<Conference> conferenceList) {
        $(conferenceList).delete();
    }


}
