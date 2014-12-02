package fund.mingdao.sys.entity.service;

import com.google.inject.Singleton;
import fund.mingdao.sys.entity.SysUser;
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
public class SysUserService extends HQuery {

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public SysUser getById(Long id) {
        return $(id).get(SysUser.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public SysUser getByCpnyUserId(Long cpnyId,String userId) {
        return $($eq("cpnyId.id",cpnyId),$eq("userId",userId)).get(SysUser.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Page<SysUser> getPageList(int start,
                                     int limit, List<Selector> selectorList) {
        return $(selectorList).page(SysUser.class, start, limit);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<SysUser> getListByCpnyId(Long cpnyId) {
        return $($eq("cpnyId.id",cpnyId)).list(SysUser.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<SysUser> getListByUserId(Long userId) {
        return $($eq("userId", userId)).list(SysUser.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Long getMaxId() {
        return $($max("id")).value(SysUser.class, Long.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Long getCountId() {
        return $($count("id")).value(SysUser.class, Long.class);
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void add(SysUser sysUser) {
        $(sysUser).add();
    }

    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(SysUser sysUser) {
        $(sysUser).save();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<SysUser> userList) {
        $(userList).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(SysUser sysUser) {
        $(sysUser).delete();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<SysUser> userList) {
        $(userList).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(SysUser.class);
    }
}
