package br.com.sharkweb.fbv.Util;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tiago on 28/10/2015.
 */
public class FuncoesParse {

    public FuncoesParse() {

    }

    public static ArrayList<ParseObject> listToArray(List listParse) {
        ArrayList<ParseObject> retorno = new ArrayList<ParseObject>();
        for (int i = 0; i < listParse.size(); i++) {
            ParseObject p = retorno.get(i);
            retorno.add(p);
        }
        return retorno;
    }
}
