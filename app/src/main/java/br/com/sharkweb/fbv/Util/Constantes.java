package br.com.sharkweb.fbv.Util;

import br.com.sharkweb.fbv.model.Usuario;

/**
 * Created by Tiago on 23/07/2015.
 */
public class Constantes {

    public static Usuario usuarioLogado;

    public Constantes() {
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static void setUsuarioLogado(Usuario usuarioLogado) {
        Constantes.usuarioLogado = usuarioLogado;
    }
}
