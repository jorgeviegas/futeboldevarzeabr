package br.com.sharkweb.fbv.adapter;

/**
 * Created by Tiago on 23/07/2015.
 */

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.sharkweb.fbv.R;
import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.controller.JogoController;
import br.com.sharkweb.fbv.controller.TimeController;
import br.com.sharkweb.fbv.model.Jogo;
import br.com.sharkweb.fbv.model.Time;

/**
 * @author Tiago Klein
 *         Monta os itens da lista de Escolha de loja.
 */
public class JogoListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<ParseObject> jogos;
    private Context context;
    private Funcoes funcoes;

    public JogoListAdapter(Context context, List<ParseObject> listaJogos) {
        this.jogos = listaJogos;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        funcoes = new Funcoes(context);
    }

    public int getCount() {
        return jogos.size();
    }

    public ParseObject getItem(int position) {
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
        } else {
            itemHolder = (ItemSuporte) view.getTag();
        }

        if (jogos != null) {
            itemHolder.tvNomeTimes.setText(jogos.get(position).getString("nomeTime").trim().toUpperCase() + " X " +
                    jogos.get(position).getString("nomeTime2").trim().toUpperCase());
            itemHolder.tvDataHora.setText(funcoes.transformarDataEmString(jogos.get(position).getDate("data")).trim() + " - " +
                    jogos.get(position).getString("hora").trim()
                    + " até " + jogos.get(position).getString("horaFinal").trim());
            itemHolder.ivJogo.setBackgroundColor(context.getResources().getColor(R.color.AzulPrincipal));

            //Se o jogo já venceu, coloca uma tarja vermelha.
            if (jogos.get(position).getDate("data").after(funcoes.getDate()))
                itemHolder.ivJogo.setBackgroundColor(context.getResources().getColor(R.color.vermelhoEscuro));

        } else {
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
