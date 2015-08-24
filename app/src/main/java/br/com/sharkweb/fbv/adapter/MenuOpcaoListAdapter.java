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

import br.com.sharkweb.fbv.NavigationDrawerFragment;
import br.com.sharkweb.fbv.R;
import br.com.sharkweb.fbv.model.OpcaoMenu;

/**
 * @author Tiago Klein
 * Monta os itens da lista de Escolha de loja.
 */
public class MenuOpcaoListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    ArrayList<OpcaoMenu> listaOpcaoMenu = new ArrayList<>();

    public MenuOpcaoListAdapter(Context context, ArrayList<OpcaoMenu> listaMenu) {

        mInflater = LayoutInflater.from(context);
        listaOpcaoMenu = listaMenu;
    }

    public int getCount() {
        return listaOpcaoMenu.size();
    }

    public OpcaoMenu getItem(int position) {
        return listaOpcaoMenu.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        ItemSuporte itemHolder;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_menulist, null);

            itemHolder = new ItemSuporte();
            itemHolder.tvDescricao = ((TextView) view.findViewById(R.id.menulist_descricaoOpcao));
            itemHolder.tvOpcao = ((TextView) view.findViewById(R.id.menulist_nomeOpcao));
            itemHolder.ivIconeOpcao  = ((ImageView) view.findViewById(R.id.menulist_imagemOpcao));

            view.setTag(itemHolder);
        }
        else {
            itemHolder = (ItemSuporte) view.getTag();
        }

        OpcaoMenu opcao = listaOpcaoMenu.get(position);
        itemHolder.tvOpcao.setText(opcao.getOpcao().trim());
        if (!opcao.getDescricao().isEmpty()) {
            itemHolder.tvDescricao.setText(opcao.getDescricao().trim());
        }
        if (opcao.getIconeOpcao() == 0) {
            itemHolder.ivIconeOpcao.setImageResource(IGNORE_ITEM_VIEW_TYPE);
        }
        else {
            itemHolder.ivIconeOpcao.setImageResource(opcao.getIconeOpcao());
        }

        return view;
    }

    private class ItemSuporte {
        public TextView tvOpcao;
        public TextView tvDescricao;
        public ImageView ivIconeOpcao;
    }
}
