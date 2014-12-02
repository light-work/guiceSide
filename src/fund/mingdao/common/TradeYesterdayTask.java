package fund.mingdao.common;

import com.google.inject.Injector;
import fund.mingdao.sys.entity.SysUser;
import fund.mingdao.sys.entity.service.SysUserService;
import org.guiceside.commons.Page;
import org.guiceside.persistence.WorkManager;
import org.guiceside.persistence.entity.search.SelectorUtils;
import org.guiceside.persistence.hibernate.dao.hquery.Selector;

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



    private Injector injector;

    private TradeYesterdayTask() {

    }

    public TradeYesterdayTask(ScheduledExecutorService scheduExec, Injector injector) {
        this.scheduExec=scheduExec;
        this.injector=injector;
        this.sysUserService=injector.getInstance(SysUserService.class);

    }

    public void start(){
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    injector.getInstance(WorkManager.class).beginWork();
                    List<Selector> selectorList=new ArrayList<Selector>();
//                    Page<SysUser> userPage= sysUserService.getPageList(0, 20, selectorList);
//                    if(userPage!=null){
//                        List<SysUser> userList = userPage.getResultList();
//                        if (userList != null && !userList.isEmpty()) {
//                            for (SysUser sysUser : userList) {
//                                System.out.println(sysUser.getUserName());
//                            }
//                        }
//                    }
                    System.out.println(sysUserService.getMaxId());
                    System.out.println(sysUserService.getCountId());
                    List<SysUser> userList = sysUserService.getListByCpnyId(1l);
                    if (userList != null && !userList.isEmpty()) {
                        for (SysUser sysUser : userList) {
                            System.out.println(sysUser.getUserName());
                        }
                    }

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
