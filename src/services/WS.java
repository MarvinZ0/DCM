package services;

import download.WebService;
import java.util.ArrayList;


import parsers.ParseResultsForWS;
import parsers.WebServiceDescription;


/**
 * Created by Zheng on 03/03/2017.
 */
public class WS {
    public ArrayList<String[]> listOfTupleResult;
    public WS(String[] args) throws Exception {
        WebService ws=WebServiceDescription.loadDescription("mb_" + args[0]);

        String fileWithCallResult = ws.getCallResult(args[1]);
        if(fileWithCallResult!="null"){
//            System.out.println("The call is   **"+fileWithCallResult+"**");
            String fileWithTransfResults = ws.getTransformationResult(fileWithCallResult);
            listOfTupleResult = ParseResultsForWS.showResults(fileWithTransfResults, ws);
        }
        else{
            listOfTupleResult = null;
        }

    }
}