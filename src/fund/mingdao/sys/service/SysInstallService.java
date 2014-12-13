package fund.mingdao.sys.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fund.mingdao.sys.entity.SysCompany;
import fund.mingdao.sys.entity.SysInstall;
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
public class SysInstallService extends HQuery {

    @Inject
    private SysCompanyService sysCompanyService;
    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public SysInstall getById(Long id) {
        return $(id).get(SysInstall.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<SysInstall> getList(List<Selector> selectorList) {
        return $(selectorList).list(SysInstall.class);
    }


    @Transactional(type = TransactionType.READ_ONLY)
    public SysInstall getListCompany(Long cpnyId) {
        return $($eq("cpnyId.id", cpnyId)).get(SysInstall.class);
    }

    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(SysInstall sysInstall) {
        $(sysInstall).save();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(SysCompany sysCompany,SysInstall sysInstall) {
        if(sysCompany!=null){
            this.sysCompanyService.save(sysCompany);
        }
        $(sysInstall).save();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<SysInstall> sysInstallList) {
        $(sysInstallList).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(SysInstall sysInstall) {
        $(sysInstall).delete();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<SysInstall> sysInstallList) {
        $(sysInstallList).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(SysInstall.class);
    }
}
