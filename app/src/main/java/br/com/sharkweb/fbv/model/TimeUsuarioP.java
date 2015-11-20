package br.com.sharkweb.fbv.model;

/**
 * Created by Tiago on 03/11/2015.
 */

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseClassName;

@ParseClassName("time_usuario")
public class TimeUsuarioP extends ParseObject {

    public String getTime() {
        return getString("displayName");
    }

    public void setDisplayName(String value) {
        put("displayName", value);
    }
}