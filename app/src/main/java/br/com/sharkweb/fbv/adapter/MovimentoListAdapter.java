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
import java.util.GregorianCalendar;
import java.util.List;

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
    private List<ParseObject> movimentos;
    private MovimentoController movimentoControl;
    private Funcoes funcoes;
    private Context context;

    public MovimentoListAdapter(Context context, List<ParseObject> listaMovimentos) {
        this.movimentos = listaMovimentos;
        mInflater = LayoutInflater.from(context);
        movimentoControl = new MovimentoController(context);
        funcoes = new Funcoes(context);
        this.context = context;
    }

    public int getCount() {
        return movimentos.size();
    }

    public ParseObject getItem(int position) {
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

        if (movimentos.size() > 0) {

            itemHolder.tvData.setText(funcoes.transformarDataEmString(movimentos.get(position).getCreatedAt()).trim());
            itemHolder.tvHistorico.setText(movimentos.get(position).getString("historico").trim());
            itemHolder.tvValor.setText(funcoes.formatarNumeroComVirgula(movimentos.get(position).getDouble("valor")).trim());

            if (movimentos.get(position).getString("tipo").equals("E") ||
                    movimentos.get(position).getString("tipo").equals("M")) {
                itemHolder.ivTipoMov.setImageResource(R.drawable.plus_blue32);
                itemHolder.tvValor.setTextColor(context.getResources().getColor(R.color.AzulPrincipal));
            } else if (movimentos.get(position).getString("tipo").equals("R")) {
                itemHolder.ivTipoMov.setImageResource(R.drawable.minus_red_32);
                itemHolder.tvValor.setTextColor(context.getResources().getColor(R.color.vermelhoEscuro));
            } else if (movimentos.get(position).getString("tipo").equals("A")) {
                if (movimentos.get(position).getDouble("valor") < 0) {
                    itemHolder.ivTipoMov.setImageResource(R.drawable.minus_red_32);
                    itemHolder.tvValor.setTextColor(context.getResources().getColor(R.color.vermelhoEscuro));
                } else {
                    itemHolder.ivTipoMov.setImageResource(R.drawable.plus_blue32);
                    itemHolder.tvValor.setTextColor(context.getResources().getColor(R.color.AzulPrincipal));
                }
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
