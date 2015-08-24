package br.com.sharkweb.fbv.model;

/**
 * Created by Tiago on 19/07/2015.
 */
public class Jogo {

    private int id;
    private int id_time;
    private int id_time2;
    private int id_local;
    private String data;
    private String hora;
    private String horaFinal;
    private int inativo;
    private int id_juiz;

    public Jogo(int id, int id_time, int id_time2, int id_local, String data, String hora,String horaFinal, int inativo, int id_juiz) {
        this.id = id;
        this.id_time = id_time;
        this.id_time2 = id_time2;
        this.id_local = id_local;
        this.data = data;
        this.hora = hora;
        this.horaFinal = horaFinal;
        this.inativo = inativo;
        this.id_juiz = id_juiz;
    }

    public Jogo(int id_time, int id_time2, int id_local, String data, String hora,String horaFinal, int inativo,int id_juiz) {
        this.id_time = id_time;
        this.id_time2 = id_time2;
        this.id_local = id_local;
        this.data = data;
        this.hora = hora;
        this.horaFinal = horaFinal;
        this.inativo = inativo;
        this.id_juiz = id_juiz;
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

    public int getId_time2() {
        return id_time2;
    }

    public void setId_time2(int id_time2) {
        this.id_time2 = id_time2;
    }

    public int getId_local() {
        return id_local;
    }

    public void setId_local(int id_local) {
        this.id_local = id_local;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getInativo() {
        return inativo;
    }

    public void setInativo(int inativo) {
        this.inativo = inativo;
    }

    public String getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(String horaFinal) {
        this.horaFinal = horaFinal;
    }

    public int getId_juiz() {
        return id_juiz;
    }

    public void setId_juiz(int id_juiz) {
        this.id_juiz = id_juiz;
    }
}
