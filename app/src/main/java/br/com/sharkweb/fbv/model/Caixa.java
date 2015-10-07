package br.com.sharkweb.fbv.model;

/**
 * Created by Jorge on 30/09/2015.
 */
public class Caixa {

    private int id;
    private int id_time;
    private double saldo;
    private int visivel;

    public Caixa(){

    }
    public Caixa(int id, int id_time, double saldo, int visivel) {
        this.id = id;
        this.id_time = id_time;
        this.saldo = saldo;
        this.visivel = visivel;
    }
    public Caixa(int id_time, double saldo, int visivel) {
        this.id_time = id_time;
        this.saldo = saldo;
        this.visivel = visivel;
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

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public int getVisivel() {
        return visivel;
    }

    public void setVisivel(int visivel) {
        this.visivel = visivel;
    }
}
