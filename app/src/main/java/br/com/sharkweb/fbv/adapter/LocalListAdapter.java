package br.com.sharkweb.fbv.adapter;

/**
 * Created by Tiago on 23/07/2015.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.sharkweb.fbv.R;
import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.controller.LocalController;
import br.com.sharkweb.fbv.controller.UFController;
import br.com.sharkweb.fbv.controller.UsuarioController;
import br.com.sharkweb.fbv.model.Local;
import br.com.sharkweb.fbv.model.Time;

/**
 * @author Tiago Klein
 * Monta os itens da lista de Escolha de loja.
 */
public class LocalListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<Local> locais;
    private LocalController localControl;
    private UFController ufControl;
    public LocalListAdapter(Context context, ArrayList<Local> listaLocais) {
        this.locais = listaLocais;
        mInflater = LayoutInflater.from(context);
        localControl = new LocalController(context);
        ufControl = new UFController(context);
    }

    public int getCount() {
        return locais.size();
    }

    public Local getItem(int position) {
        return locais.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        ItemSuporte itemHolder;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_locallist, null);

            itemHolder = new ItemSuporte();
            itemHolder.tvNomeLocal = ((TextView) view.findViewById(R.id.locallist_nomelocal));
            itemHolder.tvEndereco = ((TextView) view.findViewById(R.id.locallist_endereco));
            itemHolder.ivSeparador  = ((ImageView) view.findViewById(R.id.locallist_imagemlocal));

            view.setTag(itemHolder);
        }
        else {
            itemHolder = (ItemSuporte) view.getTag();
        }

        Local local = locais.get(position);
        if (local.getId() > 0){
            itemHolder.ivSeparador.setImageResource(R.drawable.yellow_divider);
            itemHolder.tvNomeLocal.setText(local.getNome());
            itemHolder.tvEndereco.setText(local.getEndereco().trim().toUpperCase()
                    +", "+local.getCidade().trim().toUpperCase()
                    +", "+ufControl.selectUFPorId(local.getId_uf()).get(0).getNome().trim());

        }else {
            itemHolder.ivSeparador.setImageResource(IGNORE_ITEM_VIEW_TYPE);
            itemHolder.tvNomeLocal.setText("");
            itemHolder.tvEndereco.setText("");
        }


        return view;
    }

    private class ItemSuporte {
        public TextView tvNomeLocal;
        public TextView tvEndereco;
        public ImageView ivSeparador;

    }
}
