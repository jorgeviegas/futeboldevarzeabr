package br.com.sharkweb.fbv.Util;

import com.parse.ParseObject;

import br.com.sharkweb.fbv.model.Usuario;

/**
 * Created by Tiago on 23/07/2015.
 */
public class Constantes {

    public static Usuario usuarioLogado;
    public static String PARSE_APPLICATION_ID;
    public static String PARSE_CLIENT_KEY;
    public static ParseObject timeSelecionado;

    public Constantes() {
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static void setUsuarioLogado(Usuario usuarioLogado) {
        Constantes.usuarioLogado = usuarioLogado;
    }

    public static String getParseApplicationId() {
        return PARSE_APPLICATION_ID;
    }

    public static void setParseApplicationId(String parseApplicationId) {
        PARSE_APPLICATION_ID = parseApplicationId;
    }

    public static String getParseClientKey() {
        return PARSE_CLIENT_KEY;
    }

    public static void setParseClientKey(String parseClientKey) {
        PARSE_CLIENT_KEY = parseClientKey;
    }

    public static ParseObject getTimeSelecionado() {
        return timeSelecionado;
    }

    public static void setTimeSelecionado(ParseObject timeSelecionado) {
        Constantes.timeSelecionado = timeSelecionado;
    }
}
