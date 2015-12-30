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

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

import br.com.sharkweb.fbv.R;
import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.controller.LocalController;
import br.com.sharkweb.fbv.controller.UFController;
import br.com.sharkweb.fbv.controller.UsuarioController;
import br.com.sharkweb.fbv.model.Local;
import br.com.sharkweb.fbv.model.Time;

/**
 * @author Tiago Klein
 *         Monta os itens da lista de Escolha de loja.
 */
public class LocalListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<ParseObject> locais;
    private UFController ufControl;

    public LocalListAdapter(Context context, List<ParseObject> listaLocais) {
        this.locais = listaLocais;
        mInflater = LayoutInflater.from(context);
        ufControl = new UFController(context);
    }

    public int getCount() {
        return locais.size();
    }

    public ParseObject getItem(int position) {
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
            itemHolder.ivSeparador = ((ImageView) view.findViewById(R.id.locallist_imagemlocal));

            view.setTag(itemHolder);
        } else {
            itemHolder = (ItemSuporte) view.getTag();
        }

        if (locais != null && !locais.isEmpty()) {
            itemHolder.tvNomeLocal.setText(locais.get(position).getString("nome").trim());
            itemHolder.ivSeparador.setBackgroundColor(Color.YELLOW);

            itemHolder.tvEndereco.setText(locais.get(position).getString("endereco").trim().toUpperCase()
                    + ", " + locais.get(position).getString("municipio").trim().toUpperCase()
                    + ", " + ufControl.selectUFPorId(locais.get(position).getInt("id_uf")).get(0).getNome().trim());

        } else {
            itemHolder.ivSeparador.setImageResource(IGNORE_ITEM_VIEW_TYPE);
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
