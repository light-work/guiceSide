package fund.mingdao.common;

import com.google.inject.Injector;
import fund.mingdao.sys.entity.SysUser;
import fund.mingdao.sys.entity.WfReqDataDom;
import fund.mingdao.sys.service.SysUserService;
import fund.mingdao.sys.service.WfReqDataDomService;
import org.guiceside.commons.Page;
import org.guiceside.persistence.WorkManager;
import org.guiceside.persistence.entity.search.SelectorUtils;
import org.guiceside.persistence.hibernate.dao.hquery.Selector;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhenjiaWang on 14-10-10.
 */
public class TradeYesterdayTask {
    private ScheduledExecutorService scheduExec;

    private SysUserService sysUserService;

    private WfReqDataDomService wfReqDataDomService;


    private Injector injector;

    private TradeYesterdayTask() {

    }

    public TradeYesterdayTask(ScheduledExecutorService scheduExec, Injector injector) {
        this.scheduExec=scheduExec;
        this.injector=injector;
        this.sysUserService=injector.getInstance(SysUserService.class);
        this.wfReqDataDomService=injector.getInstance(WfReqDataDomService.class);

    }

    public void start(){
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    injector.getInstance(WorkManager.class).beginWork();
                    List<Selector> selectorList=new ArrayList<Selector>();
                    selectorList.add(SelectorUtils.$eq("createdBy","3d572277-3d45-418b-8f71-5d6bb1b501b2"));
                    List<WfReqDataDom> dataDoms= wfReqDataDomService.searchIndex(selectorList,"apple");
                    if (dataDoms != null && !dataDoms.isEmpty()) {
                        System.out.println(dataDoms.size());
                        for (WfReqDataDom wfReqDataDom : dataDoms) {
                            System.out.println(wfReqDataDom.getCreatedBy());
                            System.out.println(wfReqDataDom.getReqJsonData());
                        }
                    }
//                    FullTextSession fullTextSession = Search.getFullTextSession(session);
//                    fullTextSession.createIndexer().startAndWait();
                    injector.getInstance(WorkManager.class).endWork();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        scheduExec.scheduleWithFixedDelay(task, 1000 * 1, 1000*60*60*1,
                TimeUnit.MILLISECONDS);
    }
}
