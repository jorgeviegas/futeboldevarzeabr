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

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import br.com.sharkweb.fbv.R;
import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.controller.UFController;
import br.com.sharkweb.fbv.controller.UsuarioController;
import br.com.sharkweb.fbv.model.Time;

/**
 * @author Tiago Klein
 *         Monta os itens da lista de Escolha de loja.
 */
public class TimeListAdapterParse extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<ParseObject> times;
    private UFController ufControl;
    private Context context;

    public TimeListAdapterParse(Context context, List<ParseObject> time) {
        mInflater = LayoutInflater.from(context);
        ufControl = new UFController(context);
        this.times = time;
        this.context = context;
    }

    public int getCount() {
        return times.size();
    }

    public ParseObject getItem(int position) {
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
            itemHolder.ivFavorito = ((ImageView) view.findViewById(R.id.timelist_imgfavorito));
            itemHolder.ivSeparador = ((ImageView) view.findViewById(R.id.timelist_imagemtime));

            view.setTag(itemHolder);
        } else {
            itemHolder = (ItemSuporte) view.getTag();
        }
        itemHolder.tvEscolhaTimeNome.setText(times.get(position).getString("nome").toUpperCase());

        if (!times.get(position).getObjectId().isEmpty()) {

            itemHolder.ivSeparador.setBackgroundColor(context.getResources().getColor(R.color.AzulPrincipal));
            itemHolder.tvLocalTime.setText(times.get(position).getString("cidade").trim().toUpperCase() + " - " +
                    ufControl.selectUFPorId(times.get(position).getInt("id_uf")).get(0).getNome().trim());

            /*if (ParseUser.getCurrentUser().getRelation("timeFavorito"). > 0 &&
                    (Constantes.getUsuarioLogado().getId_time() == times.get(position).getInt("id_uf"))) {
                itemHolder.ivFavorito.setImageResource(R.drawable.favorite_star);
            } else {
                itemHolder.ivFavorito.setImageResource(IGNORE_ITEM_VIEW_TYPE);
            }*/

        } else {
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
