package br.com.sharkweb.fbv.model;

/**
 * Created by Tiago on 05/10/2015.
 */
public class Movimento {

    private int id;
    private int id_caixa;
    private String historico;
    private String data;
    private double valor;
    private String tipo;
    private int id_usuario;

    public Movimento(int id, int id_caixa, String historico, String data, double valor, String tipo, int id_usuario) {
        this.id_caixa = id_caixa;
        this.historico = historico;
        this.data = data;
        this.valor = valor;
        this.tipo = tipo;
        this.id_usuario = id_usuario;
        this.id = id;
    }

    public Movimento(int id_caixa, String historico, String data, double valor, String tipo, int id_usuario) {
        this.id_caixa = id_caixa;
        this.historico = historico;
        this.data = data;
        this.valor = valor;
        this.tipo = tipo;
        this.id_usuario = id_usuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_caixa() {
        return id_caixa;
    }

    public void setId_caixa(int id_caixa) {
        this.id_caixa = id_caixa;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }
}
