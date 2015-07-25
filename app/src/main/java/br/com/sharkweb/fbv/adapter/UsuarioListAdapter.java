package br.com.sharkweb.fbv.adapter;

/**
 * Created by Tiago on 23/07/2015.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.sharkweb.fbv.R;
import br.com.sharkweb.fbv.model.Time;
import br.com.sharkweb.fbv.model.Usuario;

/**
 * @author Tiago Klein
 * Monta os itens da lista de Escolha de loja.
 */
public class UsuarioListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<Usuario> usuarios;

    public UsuarioListAdapter(Context context, ArrayList<Usuario> listaUsuarios) {
        this.usuarios = listaUsuarios;
        mInflater = LayoutInflater.from(context);
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

            view.setTag(itemHolder);
        }
        else {
            itemHolder = (ItemSuporte) view.getTag();
        }

        Usuario user = usuarios.get(position);
        itemHolder.tvUsuarioNome.setText(user.getNome());

        return view;
    }

    private class ItemSuporte {
        public TextView tvUsuarioNome;
    }
}
