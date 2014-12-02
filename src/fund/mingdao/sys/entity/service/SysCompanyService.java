package fund.mingdao.sys.entity.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fund.mingdao.sys.entity.SysAccessToken;
import fund.mingdao.sys.entity.SysCompany;
import fund.mingdao.sys.entity.SysLoginLog;
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
public class SysCompanyService extends HQuery {

    @Inject
    private SysLoginLogService sysLoginLogService;

    @Inject
    private SysCompanyService sysCompanyService;

    @Inject
    private SysUserService sysUserService;

    @Inject
    private SysAccessTokenService sysAccessTokenService;

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public SysCompany getById(Long id) {
        return $(id).get(SysCompany.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Page<SysCompany> getPageList(int start,
                                        int limit, List<Selector> selectorList) {
        return $(selectorList).page(SysCompany.class, start, limit);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public SysCompany getByCompanyId(String companyId) {
        return $($eq("companyId", companyId)).get(SysCompany.class);
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void add(SysCompany sysCompany) {
        $(sysCompany).add();
    }

    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(SysCompany sysCompany) {
        $(sysCompany).save();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(SysCompany sysCompany,SysUser sysUser,SysAccessToken sysAccessToken,SysLoginLog sysLoginLog) {
        if(sysCompany!=null){
            $(sysCompany).save();
            if(sysUser!=null){
                this.sysUserService.save(sysUser);
                if(sysAccessToken!=null){
                    this.sysAccessTokenService.save(sysAccessToken);
                    if(sysLoginLog!=null){
                        this.sysLoginLogService.save(sysLoginLog);
                    }
                }
            }
        }

    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<SysCompany> companyList) {
        $(companyList).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(SysCompany sysCompany) {
        $(sysCompany).delete();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<SysCompany> companyList) {
        $(companyList).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(SysCompany.class);
    }
}
