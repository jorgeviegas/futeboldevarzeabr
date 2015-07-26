package br.com.sharkweb.fbv.adapter;

/**
 * Created by Tiago on 23/07/2015.
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.sharkweb.fbv.R;
import br.com.sharkweb.fbv.controller.PosicaoController;
import br.com.sharkweb.fbv.controller.TipoUsuarioController;
import br.com.sharkweb.fbv.model.Time;
import br.com.sharkweb.fbv.model.Usuario;
import android.content.Intent;

/**
 * @author Tiago Klein
 * Monta os itens da lista de Escolha de loja.
 */
public class UsuarioListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<Usuario> usuarios;
    private PosicaoController posicaoControl;
    private TipoUsuarioController tipousuarioControl;

    public UsuarioListAdapter(Context context, ArrayList<Usuario> listaUsuarios) {
        this.usuarios = listaUsuarios;
        mInflater = LayoutInflater.from(context);
        posicaoControl = new PosicaoController(context);
        tipousuarioControl = new TipoUsuarioController(context);
    }

    public int getCount() {
        return usuarios.size();
    }

    public Usuario getItem(int position) {
        return usuarios.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        ItemSuporte itemHolder;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_usuariolist, null);

            itemHolder = new ItemSuporte();
            itemHolder.tvUsuarioNome = ((TextView) view.findViewById(R.id.usuariolist_nomeTime));
            itemHolder.tvUsuarioPosicao = ((TextView) view.findViewById(R.id.usuariolist_posicao));
            itemHolder.tvTipoUsuario = ((TextView) view.findViewById(R.id.usuariolist_tipoUsuario));
            itemHolder.ivImagemUsuario = ((ImageView) view.findViewById(R.id.usuariolist_imagemUsuario));

            view.setTag(itemHolder);
        }
        else {
            itemHolder = (ItemSuporte) view.getTag();
        }

        Usuario user = usuarios.get(position);
        itemHolder.tvUsuarioNome.setText(user.getNome());
        itemHolder.tvUsuarioPosicao.setText(posicaoControl.selectPosicaoPorId(user.getId_posicao()).get(0).getNome());
        itemHolder.tvTipoUsuario.setText(tipousuarioControl.selectTiposUsuariosPorId(user.getId_tipo()).get(0).getTipo());
        itemHolder.ivImagemUsuario.setImageResource(R.drawable.parson);
        return view;
    }

    private class ItemSuporte {
        public TextView tvUsuarioNome;
        public TextView tvUsuarioPosicao;
        public TextView tvTipoUsuario;
        public ImageView ivImagemUsuario;

    }
}
