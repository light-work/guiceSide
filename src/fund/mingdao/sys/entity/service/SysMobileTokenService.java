package fund.mingdao.sys.entity.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fund.mingdao.sys.entity.SysAccessToken;
import fund.mingdao.sys.entity.SysMobileToken;
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
public class SysMobileTokenService extends HQuery {

    @Inject
    private SysMobileTokenService sysMobileTokenService;

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public SysMobileToken getById(Long id) {
        return $(id).get(SysMobileToken.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Page<SysMobileToken> getPageList(int start,
                                            int limit, List<Selector> selectorList) {
        return $(selectorList).page(SysMobileToken.class, start, limit);
    }


    @Transactional(type = TransactionType.READ_ONLY)
    public SysMobileToken getByCpnyUserId(Long cpnyId,Long userId) {
        return $($eq("cpnyId.id", cpnyId), $eq("userId.id", userId)).get(SysMobileToken.class);
    }

    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(SysMobileToken sysMobileToken) {
        $(sysMobileToken).save();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(SysMobileToken sysMobileToken,SysAccessToken sysAccessToken) {
        $(sysMobileToken).save();
        if(sysAccessToken!=null){
            this.sysMobileTokenService.save(sysMobileToken);
        }
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<SysMobileToken> sysMobileTokenList) {
        $(sysMobileTokenList).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(SysMobileToken sysMobileToken) {
        $(sysMobileToken).delete();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<SysMobileToken> sysMobileTokenList) {
        $(sysMobileTokenList).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(SysMobileToken.class);
    }
}
