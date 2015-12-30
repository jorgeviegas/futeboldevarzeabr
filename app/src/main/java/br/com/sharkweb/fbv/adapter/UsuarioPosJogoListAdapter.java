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
import java.util.List;

import br.com.sharkweb.fbv.R;
import br.com.sharkweb.fbv.controller.PosicaoController;
import br.com.sharkweb.fbv.controller.TimeUsuarioController;
import br.com.sharkweb.fbv.controller.TipoUsuarioController;
import br.com.sharkweb.fbv.controller.UsuarioController;
import br.com.sharkweb.fbv.model.PosJogoUsuarios;
import br.com.sharkweb.fbv.model.Time;
import br.com.sharkweb.fbv.model.TimeUsuario;
import br.com.sharkweb.fbv.model.Usuario;

/**
 * @author Tiago Klein
 *         Monta os itens da lista de Escolha de loja.
 */
public class UsuarioPosJogoListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<ParseObject> usuarios;
    private PosicaoController posicaoControl;
    private TipoUsuarioController tipousuarioControl;
    private TimeUsuarioController timeUserControl;
    private UsuarioController userControl;
    private boolean criar;

    public UsuarioPosJogoListAdapter(Context context, List<ParseObject> listaUsuarios, boolean paramCriar) {
        this.usuarios = listaUsuarios;
        mInflater = LayoutInflater.from(context);
        posicaoControl = new PosicaoController(context);
        tipousuarioControl = new TipoUsuarioController(context);
        timeUserControl = new TimeUsuarioController(context);
        userControl = new UsuarioController(context);
        criar = paramCriar;
    }

    public int getCount() {
        return usuarios.size();
    }

    public ParseObject getItem(int position) {
        return usuarios.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        ItemSuporte itemHolder;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_usuarioposjogolist, null);

            itemHolder = new ItemSuporte();
            itemHolder.tvNomeUsuario = ((TextView) view.findViewById(R.id.pos_jogo_nomejogador));
            itemHolder.tvNota = ((TextView) view.findViewById(R.id.pos_jogo_notajogador));
            itemHolder.tvGols = ((TextView) view.findViewById(R.id.pos_jogo_gols));
            itemHolder.tvCartoesAmarelos = ((TextView) view.findViewById(R.id.pos_jogo_cartoesamarelo));
            itemHolder.tvCartoesVermelhos = ((TextView) view.findViewById(R.id.pos_jogo_cartoesvermelho));
            itemHolder.imgGols = ((ImageView) view.findViewById(R.id.pos_jogo_imagemgol));
            itemHolder.imgCartoesVermelhos = ((ImageView) view.findViewById(R.id.pos_jogo_imagemvermelho));
            itemHolder.imgCartoesAmarelos = ((ImageView) view.findViewById(R.id.pos_jogo_imagemamarelo));

            view.setTag(itemHolder);
        } else {
            itemHolder = (ItemSuporte) view.getTag();
        }

        if (criar) {
            itemHolder.tvNomeUsuario.setText(usuarios.get(position).getString("nome").trim().toUpperCase());
            itemHolder.tvGols.setText("0");
            itemHolder.tvCartoesAmarelos.setText("0");
            itemHolder.tvCartoesVermelhos.setText("0");
            itemHolder.tvNota.setText("Nota: ");
        } else {
            itemHolder.tvNomeUsuario.setText(usuarios.get(position).getString("nomeUsuario").trim().toUpperCase());
            itemHolder.tvGols.setText(String.valueOf(usuarios.get(position).getInt("qtdGol")));
            itemHolder.tvCartoesAmarelos.setText(String.valueOf(usuarios.get(position).getInt("qtdCartaoAmarelo")));
            itemHolder.tvCartoesVermelhos.setText(String.valueOf(usuarios.get(position).getInt("qtdCartaoVermelho")));
            itemHolder.tvNota.setText("Nota: " + String.valueOf(usuarios.get(position).getInt("nota")));
        }

        return view;
    }

    private class ItemSuporte {
        public TextView tvNota;
        public TextView tvNomeUsuario;
        public TextView tvGols;
        public TextView tvCartoesAmarelos;
        public TextView tvCartoesVermelhos;
        public ImageView imgGols;
        public ImageView imgCartoesAmarelos;
        public ImageView imgCartoesVermelhos;

    }
}
