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
import java.util.Date;

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
    private ArrayList<Mensalidade> mensalidades;
    private MensalidadeController mensalidadeControl;
    private UsuarioController usuarioControl;
    private Funcoes funcoes;
    private Context context;

    public MensalidadeListAdapter(Context context, ArrayList<Mensalidade> listaMensalidades) {
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

    public Mensalidade getItem(int position) {
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

        Mensalidade mensalidade = mensalidades.get(position);
        if (mensalidade != null && mensalidade.getId() > 0) {
            Usuario user = usuarioControl.selectUsuarioPorId(mensalidade.getId_usuario()).get(0);

            itemHolder.tvData.setText(mensalidade.getData().trim());
            itemHolder.tvUsuario.setText(user.getNome().trim());
            itemHolder.tvValor.setText(funcoes.formatarNumeroComVirgula(mensalidade.getValor()).trim());
            itemHolder.tvValorPago.setText(funcoes.formatarNumeroComVirgula(mensalidade.getValor_pago()).trim());
            itemHolder.ivVencido.setBackgroundColor(context.getResources().getColor(R.color.AzulPrincipal));

            if (mensalidade.getValor() == mensalidade.getValor_pago()) {
                itemHolder.ivCheck.setImageResource(R.drawable.check_green_oval_48);
                itemHolder.tvValor.setTextColor(Color.BLUE);
                itemHolder.tvValorPago.setTextColor(Color.BLUE);
            } else {
                itemHolder.ivCheck.setImageResource(R.drawable.interrogacao_red_48);
                itemHolder.tvValor.setTextColor(Color.BLUE);
                itemHolder.tvValorPago.setTextColor(Color.RED);
                try {
                    if (funcoes.transformarStringEmData(mensalidade.getData()).before(funcoes.getDate())) {
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
