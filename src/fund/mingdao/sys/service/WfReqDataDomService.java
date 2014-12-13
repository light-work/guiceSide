package fund.mingdao.sys.service;

import com.google.inject.Singleton;
import fund.mingdao.sys.entity.WfReqDataDom;
import org.guiceside.commons.Page;
import org.guiceside.persistence.SearchType;
import org.guiceside.persistence.TransactionType;
import org.guiceside.persistence.Transactional;
import org.guiceside.persistence.hibernate.dao.hquery.HQuery;
import org.guiceside.persistence.hibernate.dao.hquery.Selector;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import java.util.List;

/**
 * @author zhenjiaWang
 * @version 1.0 2012-05
 * @since JDK1.5
 */
@Singleton
public class WfReqDataDomService extends HQuery {

    @Transactional(type = TransactionType.READ_ONLY,searchType = SearchType.FULL_TEXT_INDEX)
    public List<WfReqDataDom> searchIndex(List<Selector> selectors,String key) throws Exception{
       return  $(selectors,key,"reqJsonData").listFullTextIndex(WfReqDataDom.class);
    }


    @Transactional(type = TransactionType.READ_ONLY)
    public void reIndex() throws Exception{
        FullTextSession fullTextSession = Search.getFullTextSession(getSession());
        fullTextSession.createIndexer().startAndWait();
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqDataDom getById(Long id) {
        return $(id).get(WfReqDataDom.class);
    }

    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqDataDom wfReqDataDom) {
        $(wfReqDataDom).save();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<WfReqDataDom> reqDataDomList) {
        $(reqDataDomList).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(WfReqDataDom wfReqDataDom) {
        $(wfReqDataDom).delete();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<WfReqDataDom> reqDataDomList) {
        $(reqDataDomList).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(WfReqDataDom.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqDataDom getByReqId(Long reqId) {
        return $($eq("reqId.id", reqId), $eq("useYn", "Y")).get(WfReqDataDom.class);
    }


}
