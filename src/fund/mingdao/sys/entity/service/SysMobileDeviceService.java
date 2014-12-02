package fund.mingdao.sys.entity.service;

import com.google.inject.Singleton;
import fund.mingdao.sys.entity.SysMobileDevice;
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
public class SysMobileDeviceService extends HQuery {

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public SysMobileDevice getById(Long id) {
        return $(id).get(SysMobileDevice.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Page<SysMobileDevice> getPageList(int start,
                                            int limit, List<Selector> selectorList) {
        return $(selectorList).page(SysMobileDevice.class, start, limit);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<SysMobileDevice> getListByToken(String deviceToken) {
        return $($eq("deviceToken", deviceToken)).list(SysMobileDevice.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<SysMobileDevice> getListByUserId(String companyId, String userId) {
        return $($eq("companyId", companyId), $eq("userId", userId)).list(SysMobileDevice.class);
    }

    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(SysMobileDevice sysMobileDevice) {
        $(sysMobileDevice).save();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<SysMobileDevice> sysMobileDeviceList) {
        $(sysMobileDeviceList).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(SysMobileDevice sysMobileDevice) {
        $(sysMobileDevice).delete();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<SysMobileDevice> sysMobileDeviceList) {
        $(sysMobileDeviceList).delete();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<SysMobileDevice> delDeviceList,SysMobileDevice sysMobileDevice) {
        if(delDeviceList!=null&&!delDeviceList.isEmpty()){
            this.delete(delDeviceList);
        }
        $(sysMobileDevice).save();
    }

    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(SysMobileDevice.class);
    }
}
