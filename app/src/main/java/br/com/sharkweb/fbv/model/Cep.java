package br.com.sharkweb.fbv.model;

/**
 * Created by Tiago on 19/07/2015.
 */
public class Cep {

    private int resultado;
    private String resultado_text;
    private String uf;
    private String cidade;
    private String bairro;
    private String tipo_logradouro;
    private String logradouro;


    public Cep(int resultado, String resultado_text, String uf, String cidade, String bairro, String tipo_logradouro, String logradouro) {
        this.resultado = resultado;
        this.resultado_text = resultado_text;
        this.uf = uf;
        this.cidade = cidade;
        this.bairro = bairro;
        this.tipo_logradouro = tipo_logradouro;
        this.logradouro = logradouro;
    }

    public int getResultado() {
        return resultado;
    }

    public void setResultado(int resultado) {
        this.resultado = resultado;
    }

    public String getResultado_text() {
        return resultado_text;
    }

    public void setResultado_text(String resultado_text) {
        this.resultado_text = resultado_text;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getTipo_logradouro() {
        return tipo_logradouro;
    }

    public void setTipo_logradouro(String tipo_logradouro) {
        this.tipo_logradouro = tipo_logradouro;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }
}
