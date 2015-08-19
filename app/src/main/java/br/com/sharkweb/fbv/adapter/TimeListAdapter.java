package br.com.sharkweb.fbv.adapter;

/**
 * Created by Tiago on 23/07/2015.
 */

import java.util.ArrayList;
        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.sharkweb.fbv.R;
import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.controller.UsuarioController;
import br.com.sharkweb.fbv.model.Time;

/**
 * @author Tiago Klein
 * Monta os itens da lista de Escolha de loja.
 */
public class TimeListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<Time> times;
    private UsuarioController usuarioControl;

    public TimeListAdapter(Context context, ArrayList<Time> listaTimes) {
        this.times = listaTimes;
        mInflater = LayoutInflater.from(context);
        usuarioControl = new UsuarioController(context);
    }

    public int getCount() {
        return times.size();
    }

    public Time getItem(int position) {
        return times.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        ItemSuporte itemHolder;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_timelist, null);

            itemHolder = new ItemSuporte();
            itemHolder.tvEscolhaTimeNome = ((TextView) view.findViewById(R.id.timelist_nometime));
            itemHolder.tvLocalTime = ((TextView) view.findViewById(R.id.timelist_local));
            itemHolder.ivFavorito  = ((ImageView) view.findViewById(R.id.timelist_imgfavorito));
            itemHolder.ivSeparador  = ((ImageView) view.findViewById(R.id.timelist_imagemtime));

            view.setTag(itemHolder);
        }
        else {
            itemHolder = (ItemSuporte) view.getTag();
        }

        Time time = times.get(position);
        itemHolder.tvEscolhaTimeNome.setText(time.getNome().toUpperCase());

        if (time.getId() > 0){
        itemHolder.ivSeparador.setImageResource(R.drawable.yellow_divider);
        itemHolder.tvLocalTime.setText(time.getCidade().trim().toUpperCase() + " - " +
                time.getUf().trim().toUpperCase());

        if(Constantes.getUsuarioLogado().getId_time() > 0 &&
                (Constantes.getUsuarioLogado().getId_time() == time.getId())) {
            itemHolder.ivFavorito.setImageResource(R.drawable.favorite_star);
        }else{
            itemHolder.ivFavorito.setImageResource(IGNORE_ITEM_VIEW_TYPE);
        }

        }else{
            itemHolder.ivFavorito.setImageResource(IGNORE_ITEM_VIEW_TYPE);
            itemHolder.tvLocalTime.setText("");
            itemHolder.ivSeparador.setImageResource(IGNORE_ITEM_VIEW_TYPE);
        }

        return view;
    }

    private class ItemSuporte {
        public TextView tvEscolhaTimeNome;
        public TextView tvLocalTime;
        public ImageView ivFavorito;
        public ImageView ivSeparador;

    }
}
