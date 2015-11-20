package br.com.sharkweb.fbv.model;

/**
 * Created by Tiago on 03/11/2015.
 */
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseClassName;

@ParseClassName("time")
public class TimeP extends ParseObject {

    public String getNome() {
        return getString("nome");
    }

    public void setDisplayName(String value) {
        put("displayName", value);
    }
}