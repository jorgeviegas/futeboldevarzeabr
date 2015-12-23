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
import java.util.Date;
import java.util.List;

import br.com.sharkweb.fbv.R;
import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.controller.MensalidadeController;
import br.com.sharkweb.fbv.controller.MovimentoController;
import br.com.sharkweb.fbv.controller.UsuarioController;
import br.com.sharkweb.fbv.model.Mensalidade;
import br.com.sharkweb.fbv.model.Movimento;
import br.com.sharkweb.fbv.model.Usuario;

/**
 * @author Tiago Klein
 *         Monta os itens da lista de Escolha de loja.
 */
public class MensalidadeListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<ParseObject> mensalidades;
    private MensalidadeController mensalidadeControl;
    private UsuarioController usuarioControl;
    private Funcoes funcoes;
    private Context context;

    public MensalidadeListAdapter(Context context, List<ParseObject> listaMensalidades) {
        this.mensalidades = listaMensalidades;
        mInflater = LayoutInflater.from(context);
        mensalidadeControl = new MensalidadeController(context);
        usuarioControl = new UsuarioController(context);
        funcoes = new Funcoes(context);
        this.context = context;
    }

    public int getCount() {
        return mensalidades.size();
    }

    public ParseObject getItem(int position) {
        return mensalidades.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        ItemSuporte itemHolder;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_mensalidades, null);

            itemHolder = new ItemSuporte();
            itemHolder.tvData = ((TextView) view.findViewById(R.id.item_mensalidades_data));
            itemHolder.tvUsuario = ((TextView) view.findViewById(R.id.item_mensalidades_usuario));
            itemHolder.tvValor = ((TextView) view.findViewById(R.id.item_mensalidades_valor));
            itemHolder.tvValorPago = ((TextView) view.findViewById(R.id.item_mensalidades_valor_pago));
            itemHolder.ivCheck = ((ImageView) view.findViewById(R.id.item_mensalidades_imgcheck));
            itemHolder.ivVencido = ((ImageView) view.findViewById(R.id.item_mensalidades_imgvencido));

            view.setTag(itemHolder);
        } else {
            itemHolder = (ItemSuporte) view.getTag();
        }

        if (!mensalidades.get(position).getObjectId().isEmpty()) {

            itemHolder.tvData.setText(funcoes.transformarDataEmString(mensalidades.get(position).getDate("data")).trim());
            itemHolder.tvUsuario.setText(mensalidades.get(position).getString("nomeUsuario").trim());
            itemHolder.tvValor.setText(funcoes.formatarNumeroComVirgula(mensalidades.get(position).getDouble("valor")).trim());
            itemHolder.tvValorPago.setText(funcoes.formatarNumeroComVirgula(mensalidades.get(position).getDouble("valorPago")).trim());
            itemHolder.ivVencido.setBackgroundColor(context.getResources().getColor(R.color.AzulPrincipal));

            if (mensalidades.get(position).getDouble("valor") == mensalidades.get(position).getDouble("valorPago")) {
                itemHolder.ivCheck.setImageResource(R.drawable.check_green_oval_48);
                itemHolder.tvValor.setTextColor(context.getResources().getColor(R.color.AzulPrincipal));
                itemHolder.tvValorPago.setTextColor(context.getResources().getColor(R.color.AzulPrincipal));
            } else {
                itemHolder.ivCheck.setImageResource(R.drawable.interrogacao_red_48);
                itemHolder.tvValor.setTextColor(context.getResources().getColor(R.color.AzulPrincipal));
                itemHolder.tvValorPago.setTextColor(context.getResources().getColor(R.color.vermelhoEscuro));
                try {
                    if (mensalidades.get(position).getDate("data").before(funcoes.getDate())) {
                        itemHolder.ivVencido.setBackgroundColor(context.getResources().getColor(R.color.vermelhoEscuro));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            itemHolder.tvData.setText("");
            itemHolder.tvUsuario.setText("Nenhuma mensalidade encontrada");
            itemHolder.tvValor.setText("");
            itemHolder.tvValorPago.setText("");
        }

        return view;
    }

    private class ItemSuporte {
        public TextView tvData;
        public TextView tvUsuario;
        public TextView tvValor;
        public TextView tvValorPago;
        public ImageView ivCheck;
        public ImageView ivVencido;

    }
}
