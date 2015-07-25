package br.com.sharkweb.fbv.model;

/**
 * Created by Tiago on 19/07/2015.
 */
public class TimeUsuario {

    private int id;
    private int id_time;
    private int id_usuario;

    public TimeUsuario(int id, int id_time, int id_usuario) {
        this.id = id;
        this.id_time = id_time;
        this.id_usuario = id_usuario;
    }

    public TimeUsuario(int id_time, int id_usuario) {
        this.id_time = id_time;
        this.id_usuario = id_usuario;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_time() {
        return id_time;
    }

    public void setId_time(int id_time) {
        this.id_time = id_time;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }
}
