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
        import android.widget.TextView;

import br.com.sharkweb.fbv.R;
import br.com.sharkweb.fbv.model.Time;

/**
 * @author Tiago Klein
 * Monta os itens da lista de Escolha de loja.
 */
public class TimeListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<Time> times;

    public TimeListAdapter(Context context, ArrayList<Time> listaTimes) {
        this.times = listaTimes;
        mInflater = LayoutInflater.from(context);
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
            itemHolder.tvEscolhaTimeNome = ((TextView) view.findViewById(R.id.timelist_nomeTime));

            view.setTag(itemHolder);
        }
        else {
            itemHolder = (ItemSuporte) view.getTag();
        }

        Time time = times.get(position);
        itemHolder.tvEscolhaTimeNome.setText(time.getNome());

        return view;
    }

    private class ItemSuporte {
        public TextView tvEscolhaTimeNome;
    }
}
