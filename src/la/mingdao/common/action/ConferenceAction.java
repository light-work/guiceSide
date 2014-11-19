package la.mingdao.common.action;

import com.google.inject.Inject;
import la.mingdao.common.entity.Conference;
import la.mingdao.common.service.ConferenceService;
import la.mingdao.web.support.ActionSupport;
import org.guiceside.commons.collection.RequestData;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.web.annotation.*;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjiaWang
 * Date: 12-7-12
 * Time: 下午9:49
 * To change this template use File | Settings | File Templates.
 */
@Action(name = "index", namespace = "/conference")
public class ConferenceAction extends ActionSupport<Conference> {

    @Inject
    private ConferenceService conferenceService;


    @ReqGet
    @ModelDriver
    @ReqSet
    private Conference conference;


    @ReqGet
    @ReqSet
    private String memberIds;

    @ReqSet
    private String fileKey;

    @ReqSet
    private String upToken;

    @ReqGet
    @ReqSet
    private Long id;

    @ReqGet
    @ReqSet
    private String conferenceUID;

    @ReqGet
    @ReqSet
    private String conferenceCallUID;

    @ReqGet
    @ReqSet
    private String voipAccount;

    @ReqGet
    @ReqSet
    private String volumeChangeOpt;


    @ReqGet
    @ReqSet
    private String callId;

    @ReqGet
    @ReqSet
    private Long conferenceId;

    private final String ATT_KEY = "CONFERENCE";

    private final String CONFERENCE_NO = "conf4001015692";

    @ReqSet
    private String errorCode;

    @ReqGet
    @ReqSet
    private String recordOp;

    @ReqGet
    private String password;

    @Override
    @PageFlow(result = {@Result(name = "success", path = "/view/conference/index.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        System.out.println("execute");
        RequestData requestData=getRequestData();
        if(requestData!=null){
            Set<String> set=requestData.keySet();
            if(set!=null&&!set.isEmpty()){
                for(String key:set){
                    System.out.println("params:"+key+" value="+requestData.get(key));
                }
            }
        }
        return null;
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/conference/index.ftl", type = Dispatcher.FreeMarker)})
    public String abc() throws Exception {
        System.out.println("abc");
        RequestData requestData=getRequestData();
        if(requestData!=null){
            Set<String> set=requestData.keySet();
            if(set!=null&&!set.isEmpty()){
                for(String key:set){
                    System.out.println("params:"+key+" value="+requestData.get(key));
                }
            }
        }
        return null;
    }


}
