package br.com.sharkweb.fbv.model;

/**
 * Created by Tiago on 05/10/2015.
 */
public class Mensalidade {

    private int id;
    private int id_time;
    private int id_usuario;
    private String data;
    private int pagamento;
    private double valor;
    private double valor_pago;

    public Mensalidade(int id, int id_time,int id_usuario, String data, int pagamento, double valor, double valor_pago) {
        this.id = id;
        this.id_usuario = id_usuario;
        this.id_time = id_time;
        this.data = data;
        this.pagamento = pagamento;
        this.valor = valor;
        this.valor_pago = valor_pago;
    }
    public Mensalidade(int id_time, int id_usuario, String data, int pagamento, double valor, double valor_pago) {
        this.id_time = id_time;
        this.id_usuario = id_usuario;
        this.data = data;
        this.pagamento = pagamento;
        this.valor = valor;
        this.valor_pago = valor_pago;
    }

    public Mensalidade(){
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getPagamento() {
        return pagamento;
    }

    public void setPagamento(int pagamento) {
        this.pagamento = pagamento;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getValor_pago() {
        return valor_pago;
    }

    public void setValor_pago(double valor_pago) {
        this.valor_pago = valor_pago;
    }
}
