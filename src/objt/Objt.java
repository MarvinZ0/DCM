package objt;

import services.WS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Zheng on 04/03/2017.
 */
public class Objt {

    private HashMap<String, String> record = new HashMap<String,String>();
    public List<Objt> obj = new ArrayList<Objt>();


    public Objt(){
    }

    public HashMap<String, String> getRecord(){
        return this.record;
    }

    public List<Objt> getSubObjt(){
        return this.obj;
    }

    public void setMap(String[] parms,String[] args){
        if(parms.length!=args.length){
            System.out.println("Out of bounds");
            System.exit(-1);
        }
        for (int i = 0; i < parms.length; i++) {
            this.record.put(parms[i],args[i]);
        }

    }


    public void setSubObjt(String[] parms, String[] args){
        Objt sub = new Objt();
        sub.setMap(parms,args);
        this.obj.add(sub);
    }

    public void printObjt(int i){
        String ess = new String(new char[i]).replace("\0", "    ");
        System.out.println(ess + this.getRecord());
        if(this.getSubObjt()!=null){
            for(Objt o:this.getSubObjt()){
                o.printObjt(i+1);
            }

        }
    }

    public void locateRecord(String key,String value){

        for(Objt o:this.getSubObjt()){
            if(o.record.containsKey(key)){
                if(o.record.get(key).equals(value))
                    System.out.println(o.record);
            }
            else{
                if(!o.obj.isEmpty())
                    o.locateRecord(key,value);
            }

        }
    }

    public void autoSetSubObjt(String functionName,String[] params,String key) throws Exception {
        if(this.record.containsKey(key)){
            String input = this.record.get(key);
            String[] functioncall = new String[]{functionName,input};
            WS ws = new WS(functioncall);
            ArrayList<String[]> res = ws.listOfTupleResult;
            for(String[] t : res){
                this.setSubObjt(params,t);
            }

        }
        else{
            if(!this.obj.isEmpty()) {
                for (Objt ob : this.obj) {
                    ob.autoSetSubObjt(functionName, params, key);
                }
            }
        }

    }



    public static void main(String[] args) throws Exception {
        String[] ar = new String[]{"ab","bc","cd","de"};
        String[] par = new String[]{"xx","yy","zz","tt"};
        Objt ob = new Objt();
        ob.setMap(par,ar);
        ob.setSubObjt(par,ar);
        ob.printObjt(0);

    }
}
