package br.com.sharkweb.fbv.model;

/**
 * Created by Tiago on 19/07/2015.
 */
public class Login {

    private int id;
    private String idParse;

    public Login(int id, String idParse) {
        this.id = id;
        this.idParse = idParse;
    }

    public Login(String idParse) {
        this.idParse = idParse;
    }

    public Login() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdParse() {
        return idParse;
    }

    public void setIdParse(String idParse) {
        this.idParse = idParse;
    }
}
