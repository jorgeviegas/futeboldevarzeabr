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
    private ArrayList<PosJogoUsuarios> usuarios;
    private PosicaoController posicaoControl;
    private TipoUsuarioController tipousuarioControl;
    private TimeUsuarioController timeUserControl;
    private UsuarioController userControl;

    public UsuarioPosJogoListAdapter(Context context, ArrayList<PosJogoUsuarios> listaUsuarios) {
        this.usuarios = listaUsuarios;
        mInflater = LayoutInflater.from(context);
        posicaoControl = new PosicaoController(context);
        tipousuarioControl = new TipoUsuarioController(context);
        timeUserControl = new TimeUsuarioController(context);
        userControl = new UsuarioController(context);
    }

    public int getCount() {
        return usuarios.size();
    }

    public PosJogoUsuarios getItem(int position) {
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

        PosJogoUsuarios posJogoUsers = usuarios.get(position);
        Usuario user = userControl.selectUsuarioPorId(posJogoUsers.getId_usuario(),"").get(0);
        itemHolder.tvNomeUsuario.setText(user.getNome().trim().toUpperCase());
        itemHolder.tvGols.setText(String.valueOf(posJogoUsers.getQtd_gol()));
        itemHolder.tvCartoesAmarelos.setText(String.valueOf(posJogoUsers.getQtd_cartao_amarelo()));
        itemHolder.tvCartoesVermelhos.setText(String.valueOf(posJogoUsers.getQtd_cartao_vermelho()));
        itemHolder.tvNota.setText("Nota: "+String.valueOf(posJogoUsers.getNota()).trim());

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
