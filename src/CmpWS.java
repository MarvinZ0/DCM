import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import objt.Objt;
import services.WS;





/**
 * Created by Zheng on 03/03/2017.
 */
public class CmpWS {
    public static String[] parserQuery(String string) {
        String[] q = string.split("#");
        return q;
    }
    public static String getFunction(String string){
        String[] f = string.split("[()]");
        return f[0];
    }
    public static String[] getParams(String string){
        String f = string.split("[()]")[1];
        String[] p = f.split(",");
        Pattern pat = Pattern.compile("=.*", Pattern.CASE_INSENSITIVE);
        for(int i=0;i < p.length;i++){
            Matcher m = pat.matcher(p[i]);
            p[i] = m.replaceAll("");
        }
        return p;
    }

    public static String getInput(String string){
        Pattern pat = Pattern.compile("name=(.*?),", Pattern.CASE_INSENSITIVE);
        Matcher m = pat.matcher(string);
        if(m.find())
            return m.group(1);
        else
            return "None";
    }

    public static final void main(String[] args) throws Exception{

        /**Before running, remember to modifier the path in src/constants/Settings**/

        String arg = "getArtistInfoByName(?name=u2,?artistId,beginDate,endDate)#getAlbumByArtistId(?artistId,eventDate,albumId,albumName)";
//        String arg = "getArtistInfoByName(?name=u2,?artistId,beginDate,endDate)";


        String[] services = parserQuery(arg);
        Objt result = new Objt();

        if(services.length>0){


            /**WebService1: getArtistInfoByname(?name,?artistId,?beginData,?endDate)
             * Input: ?name
             * Output: ?artistId,?beginData,?endDate
             * baseline function
             * **/
            String function1 = getFunction(services[0]);
            String[] params1 = getParams(services[0]);
            String input1 = getInput(services[0]);
            String[] functioncall = new String[]{function1,input1};
            WS ws1 = new WS(functioncall);
            ArrayList<String[]> res1 = ws1.listOfTupleResult;
            for(String[] t : res1){
                result.setSubObjt(params1,t);
            }



            /**WebService2: getAlbumByArtistId(?artistId,?eventDate,?albumId,?albumName)
             * Input: ?artistId
             * Output: ?eventDate,?albumId,?albumName
             * extend function(s)
             * **/

            for(int i=0; i<services.length;i++){
                if(i!=0){
                    String function2 = getFunction(services[i]);
                    String[] params2 = getParams(services[i]);
                    String inputkey = params2[0];
                    for(Objt o:result.obj){
                        o.autoSetSubObjt(function2,params2,inputkey);
                    }
                }
            }
            result.printObjt(0);


        }

        else{
            System.out.println("Query incorrect");
            System.exit(-1);
        }

//        result.locateRecord("beginDate","1976");






    }
}
