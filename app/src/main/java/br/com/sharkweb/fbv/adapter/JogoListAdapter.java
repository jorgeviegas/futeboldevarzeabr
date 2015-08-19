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
import br.com.sharkweb.fbv.controller.TimeController;
import br.com.sharkweb.fbv.model.Jogo;
import br.com.sharkweb.fbv.model.Time;

/**
 * @author Tiago Klein
 * Monta os itens da lista de Escolha de loja.
 */
public class JogoListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<Jogo> jogos;
    private TimeController timeControl;

    public JogoListAdapter(Context context, ArrayList<Jogo> listaJogos) {
        this.jogos = listaJogos;
        mInflater = LayoutInflater.from(context);
        timeControl = new TimeController(context);
    }

    public int getCount() {
        return jogos.size();
    }

    public Jogo getItem(int position) {
        return jogos.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        ItemSuporte itemHolder;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_jogoslist, null);

            itemHolder = new ItemSuporte();
            itemHolder.tvNomeTimes = ((TextView) view.findViewById(R.id.jogoslist_nometimes));
            itemHolder.tvDataHora = ((TextView) view.findViewById(R.id.jogoslist_horario));
            itemHolder.ivJogo = (ImageView) view.findViewById(R.id.jogoslist_imagemtime);
            itemHolder.tvSeiLa = ((TextView) view.findViewById(R.id.jogoslist_seila));
            view.setTag(itemHolder);
        }
        else {
            itemHolder = (ItemSuporte) view.getTag();
        }

        Jogo jogo = jogos.get(position);
        if (jogo.getId_time() > 0 && jogo.getId_time2() > 0){
            Time time = timeControl.selectTimePorId(jogo.getId_time()).get(0);
            Time time2 = timeControl.selectTimePorId(jogo.getId_time2()).get(0);

            itemHolder.tvNomeTimes.setText(time.getNome().trim().toUpperCase()+" X "+time2.getNome().trim().toUpperCase());
            itemHolder.tvDataHora.setText(jogo.getData()+" - "+jogo.getHora().trim()
                    +" até "+jogo.getHoraFinal().trim());
            itemHolder.ivJogo.setImageResource(R.drawable.blue_divider);
        }else{
            itemHolder.tvNomeTimes.setText("");
            itemHolder.tvDataHora.setText("");
            itemHolder.ivJogo.setVisibility(View.GONE);
        }


        itemHolder.tvSeiLa.setText("");

        return view;
    }

    private class ItemSuporte {
        public TextView tvNomeTimes;
        public TextView tvSeiLa;
        public TextView tvDataHora;
        public ImageView ivJogo;

    }
}
