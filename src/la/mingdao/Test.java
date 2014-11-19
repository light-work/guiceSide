package la.mingdao;

/**
 * Created by zhenjiaWang on 14-6-17.
 */
public class Test {
    public static void main(String[] aa){
        String tempUri = "conference/index/";
        String[] urlBuild=tempUri.split("/");
        if(urlBuild!=null&&urlBuild.length>=2){
            System.out.println(urlBuild[0]);
            System.out.println(urlBuild[1]);
            System.out.println(urlBuild[2]);
        }
    }
}
