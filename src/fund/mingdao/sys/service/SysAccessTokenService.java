package fund.mingdao.sys.service;

import com.google.inject.Singleton;
import fund.mingdao.sys.entity.SysAccessToken;
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
public class SysAccessTokenService extends HQuery {

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public SysAccessToken getById(Long id) {
        return $(id).get(SysAccessToken.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Page<SysAccessToken> getPageList(int start,
                                            int limit, List<Selector> selectorList) {
        return $(selectorList).page(SysAccessToken.class, start, limit);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public SysAccessToken getByCpnyUserId(Long cpnyId, Long userId) {
        return $($eq("cpnyId.id", cpnyId), $eq("userId.id", userId)).get(SysAccessToken.class);
    }

    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(SysAccessToken sysAccessToken) {
        $(sysAccessToken).save();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<SysAccessToken> accessTokenList) {
        $(accessTokenList).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(SysAccessToken sysAccessToken) {
        $(sysAccessToken).delete();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<SysAccessToken> accessTokenList) {
        $(accessTokenList).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(SysAccessToken.class);
    }
}
