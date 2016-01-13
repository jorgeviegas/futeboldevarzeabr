package br.com.sharkweb.fbv.adapter;

/**
 * Created by Tiago on 23/07/2015.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

import br.com.sharkweb.fbv.R;
import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.controller.PosicaoController;
import br.com.sharkweb.fbv.controller.TipoUsuarioController;

/**
 * @author Tiago Klein
 *         Monta os itens da lista de Escolha de loja.
 */
public class UsuarioListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<ParseObject> usuarios;
    private PosicaoController posicaoControl;
    private TipoUsuarioController tipousuarioControl;
    private int modelo;
    private Context context;
    private boolean filtrarInativos;
    private boolean configPorTime;

    public UsuarioListAdapter(Context context, List<ParseObject> listaUsuarios, int modelo, boolean filtrarInativos,
                              boolean configurarPorTime) {

        this.usuarios = listaUsuarios;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        posicaoControl = new PosicaoController(context);
        tipousuarioControl = new TipoUsuarioController(context);
        this.filtrarInativos = filtrarInativos;
        this.configPorTime = configurarPorTime;
        //MODELOS DE ADAPTER:
        //1 - Exibe informarções como jogador;
        //2 - Exibe informações como usuário;
        this.modelo = modelo;

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
            view = mInflater.inflate(R.layout.item_usuariolist, null);

            itemHolder = new ItemSuporte();
            itemHolder.tvUsuarioNome = ((TextView) view.findViewById(R.id.usuariolist_nomeTime));
            itemHolder.tvUserName = ((TextView) view.findViewById(R.id.usuariolist_nomeUsuario));
            itemHolder.tvUsuarioPosicao = ((TextView) view.findViewById(R.id.usuariolist_posicao));
            itemHolder.tvTipoUsuario = ((TextView) view.findViewById(R.id.usuariolist_tipoUsuario));
            itemHolder.ivImagemUsuario = ((ImageView) view.findViewById(R.id.usuariolist_imagemUsuario));

            view.setTag(itemHolder);
        } else {
            itemHolder = (ItemSuporte) view.getTag();
        }
        if (!this.usuarios.get(position).getObjectId().isEmpty()) {
            itemHolder.tvUsuarioNome.setText(this.usuarios.get(position).getString("nome").trim());

            ParseFile imagemPerfil = this.usuarios.get(position).getParseFile("ImageFile");
            if (imagemPerfil != null) {
                try {
                    byte[] bitmapdata = imagemPerfil.getData();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                    itemHolder.ivImagemUsuario.setImageBitmap(bitmap);
                } catch (ParseException e) {
                    e.printStackTrace();
                    itemHolder.ivImagemUsuario.setImageResource(R.drawable.parson);
                }
            } else {
                itemHolder.ivImagemUsuario.setImageResource(R.drawable.parson);
            }

            itemHolder.tvTipoUsuario.setText("");
            if (configPorTime) {
                ArrayList<String> configsTimes = (ArrayList<String>) this.usuarios.get(position).get("configTimes");
                if (configsTimes != null && configsTimes.size() > 0) {
                    for (int i = 0; i < configsTimes.size(); i++) {
                        Object object = (Object) configsTimes.get(i);
                        String time = ((ArrayList<String>) object).get(0);

                        if (Constantes.getTimeSelecionado().getObjectId().equals(time)) {
                            String inativo = ((ArrayList<String>) object).get(1);
                            String tipoUsuario = ((ArrayList<String>) object).get(2);

                            if (Integer.valueOf(inativo) == 1) {
                                itemHolder.tvUsuarioNome.setTextColor(Color.RED);
                            }
                            itemHolder.tvTipoUsuario.setText(tipousuarioControl.selectTiposUsuariosPorId(
                                    Integer.valueOf(tipoUsuario)).get(0).getTipo().trim());

                            if (tipousuarioControl.selectTiposUsuariosPorId(Integer.valueOf(tipoUsuario))
                                    .get(0).getTipo().equals("Administrador"))
                                itemHolder.tvTipoUsuario.setTextColor(context.getResources().getColor(R.color.greengreen));

                            if (Integer.valueOf(inativo) == 1 && !filtrarInativos) {
                                usuarios.remove(position);
                            }
                        }

                    }
                } else {
                    itemHolder.tvTipoUsuario.setText("Pendente");
                    itemHolder.tvTipoUsuario.setTextColor(Color.RED);
                }
                if (itemHolder.tvTipoUsuario.getText().toString().equals("")) {
                    itemHolder.tvTipoUsuario.setText("Pendente");
                    itemHolder.tvTipoUsuario.setTextColor(Color.RED);
                }
            } else {
                itemHolder.tvTipoUsuario.setText(tipousuarioControl.selectTiposUsuariosPorId(
                        usuarios.get(position).getInt("id_tipo")).get(0).getTipo().trim());

                if (tipousuarioControl.selectTiposUsuariosPorId(Integer.valueOf(usuarios.get(position).getInt("id_tipo")))
                        .get(0).getTipo().equals("Administrador")) {
                    itemHolder.tvTipoUsuario.setTextColor(context.getResources().getColor(R.color.greengreen));
                } else {
                    itemHolder.tvTipoUsuario.setTextColor(context.getResources().getColor(R.color.material_blue_grey_800));
                }
            }
        } else {
            itemHolder.tvTipoUsuario.setText("");
        }

        if (this.usuarios.get(position).getString("posicao") != null &&
                !this.usuarios.get(position).getString("posicao").isEmpty()) {
            itemHolder.tvUsuarioPosicao.setText(" " + this.usuarios.get(position).getString("posicao").trim());
        } else {
            itemHolder.tvUsuarioPosicao.setText("");
        }
        itemHolder.tvUsuarioPosicao.setTextColor(context.getResources().getColor(R.color.AzulPrincipal));
        itemHolder.tvUserName.setText(this.usuarios.get(position).getString("username").trim());
        return view;
    }

    public class ItemSuporte {
        public TextView tvUsuarioNome;
        public TextView tvUsuarioPosicao;
        public TextView tvUserName;
        public TextView tvTipoUsuario;
        public ImageView ivImagemUsuario;
    }
}


