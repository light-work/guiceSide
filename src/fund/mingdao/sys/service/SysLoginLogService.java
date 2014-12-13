package fund.mingdao.sys.service;

import com.google.inject.Singleton;
import fund.mingdao.sys.entity.SysLoginLog;
import org.guiceside.commons.Page;
import org.guiceside.persistence.TransactionType;
import org.guiceside.persistence.Transactional;
import org.guiceside.persistence.hibernate.dao.hquery.HQuery;
import org.guiceside.persistence.hibernate.dao.hquery.Selector;

import java.util.List;

/**
 * @author zhenjiaWang
 * @version 1.0 2012-05
 * @since JDK1.5
 */
@Singleton
public class SysLoginLogService extends HQuery {


    @Transactional(type = TransactionType.READ_ONLY)
    public Page<SysLoginLog> getPageList(int start,
                                         int limit, List<Selector> selectorList) {
        return $(selectorList).page(SysLoginLog.class, start, limit);
    }


    @Transactional(type = TransactionType.READ_ONLY)
    public List<SysLoginLog> getList(List<Selector> selectorList) {
        return $(selectorList).list(SysLoginLog.class);
    }

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public SysLoginLog getById(Long id) {
        return $(id).get(SysLoginLog.class);
    }

    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(SysLoginLog sysLoginLog) {
        $(sysLoginLog).save();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<SysLoginLog> loginLogList) {
        $(loginLogList).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(SysLoginLog sysLoginLog) {
        $(sysLoginLog).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(SysLoginLog.class);
    }
}
