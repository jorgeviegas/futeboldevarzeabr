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
import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.controller.LocalController;
import br.com.sharkweb.fbv.controller.MovimentoController;
import br.com.sharkweb.fbv.controller.UFController;
import br.com.sharkweb.fbv.model.Local;
import br.com.sharkweb.fbv.model.Movimento;

/**
 * @author Tiago Klein
 *         Monta os itens da lista de Escolha de loja.
 */
public class MovimentoListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<Movimento> movimentos;
    private MovimentoController movimentoControl;
    private Funcoes funcoes;

    public MovimentoListAdapter(Context context, ArrayList<Movimento> listaMovimentos) {
        this.movimentos = listaMovimentos;
        mInflater = LayoutInflater.from(context);
        movimentoControl = new MovimentoController(context);
        funcoes = new Funcoes(context);
    }

    public int getCount() {
        return movimentos.size();
    }

    public Movimento getItem(int position) {
        return movimentos.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        ItemSuporte itemHolder;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_movimentos, null);

            itemHolder = new ItemSuporte();
            itemHolder.tvData = ((TextView) view.findViewById(R.id.movimentos_data));
            itemHolder.tvHistorico = ((TextView) view.findViewById(R.id.movimentos_historico));
            itemHolder.tvValor = ((TextView) view.findViewById(R.id.movimentos_valor));
            itemHolder.ivTipoMov = ((ImageView) view.findViewById(R.id.movimentos_imgtipomovimento));

            view.setTag(itemHolder);
        } else {
            itemHolder = (ItemSuporte) view.getTag();
        }

        Movimento movimento = movimentos.get(position);
        if (movimento != null && movimento.getId() > 0) {
            itemHolder.tvData.setText(movimento.getData().trim());
            itemHolder.tvHistorico.setText(movimento.getHistorico().trim());
            itemHolder.tvValor.setText(funcoes.formatarNumeroComVirgula(movimento.getValor()).trim());

            if (movimento.getTipo().equals("E") ||
                    movimento.getTipo().equals("M")) {
                itemHolder.ivTipoMov.setImageResource(R.drawable.plus_blue32);
                itemHolder.tvValor.setTextColor(Color.BLUE);
            } else if (movimento.getTipo().equals("R")) {
                itemHolder.ivTipoMov.setImageResource(R.drawable.minus_red_32);
                itemHolder.tvValor.setTextColor(Color.RED);
            }
        } else {
            itemHolder.tvData.setText("");
            itemHolder.tvHistorico.setText("Nenhum movimento encontrado.");
            itemHolder.tvValor.setText("");
        }

        return view;
    }

    private class ItemSuporte {
        public TextView tvData;
        public TextView tvHistorico;
        public TextView tvValor;
        public ImageView ivTipoMov;

    }
}
