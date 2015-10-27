package br.com.sharkweb.fbv.DAOParse;


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import br.com.sharkweb.fbv.DAO.FBVDAO;
import br.com.sharkweb.fbv.model.Usuario;

public class UsuarioDAOParse {

    /*Dados da tabela*/
    private static final String NOME_TABELA = "usuario";
    private static final String ID = "_id";
    private static final String ID_PARSE = "objectId";
    private static final String CODIGO = "codigo";
    private static final String NOME = "nome";
    private static final String EMAIL = "email";
    private static final String SENHA = "senha";
    private static final String ID_POSICAO = "id_posicao";
    private static final String ID_TIME = "id_time";
    private static final String ID_TIPO = "id_tipo";
    private static final String CELULAR = "celular";
    private static final String APELIDO = "apelido";
    ParseObject usuarioParse;
    ParseQuery usuarioParsePesquisa;
    private Context context;

    private FBVDAO fbvdao;

    public UsuarioDAOParse(Context context) {
        fbvdao = FBVDAO.getInstance(context);
        usuarioParse = new ParseObject("usuario");
        usuarioParsePesquisa = new ParseQuery("usuario");
        this.context = context;
    }

    public long salvar(Usuario usuario) {
        if (!usuario.getIdParse().trim().isEmpty()) {
            usuarioParse.setObjectId(usuario.getIdParse().trim());
        }
        usuarioParse.put(NOME, usuario.getNome().trim());
        usuarioParse.put(CODIGO, usuario.getCodigo().trim());
        usuarioParse.put(EMAIL, usuario.getEmail().trim());
        usuarioParse.put(SENHA, usuario.getSenha().trim());
        usuarioParse.put(ID_TIPO, usuario.getId_tipo());
        usuarioParse.put(ID_POSICAO, usuario.getId_posicao());
        usuarioParse.put(ID_TIME, usuario.getId_time());
        usuarioParse.put(CELULAR, usuario.getCelular().trim());
        usuarioParse.put(APELIDO, usuario.getApelido().trim());

        try {
            usuarioParse.save();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    public long favoritarTime(int id, int id_time) {
        ContentValues valores = new ContentValues();
        valores.put(ID_TIME, id_time);
        String[] whereAgrs = {Integer.toString(id)};
        int retorno = fbvdao.getWritableDatabase().update(NOME_TABELA, valores, ID + " = ?", whereAgrs);
        fbvdao.close();
        return retorno;
    }

    public ArrayList<Usuario> buscarUsuarios(String coluna, String valor, int limite) {
        ArrayList<Usuario> retorno = new ArrayList<>();
        BuscarUsuarios busca = new BuscarUsuarios(context);
        AsyncTask<String, Void, ArrayList<Usuario>> ret = busca.execute(coluna, valor, String.valueOf(limite));
        try {
            retorno = ret.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return retorno;
    }

    public ArrayList<Usuario> selectUsuarioPorId(String id) {
        final ArrayList<Usuario> usuario = new ArrayList<Usuario>();
        try {
            ParseObject obj = usuarioParsePesquisa.get(id.trim());
            usuario.add(ParseObjectToUsuarioObject(obj));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    private ArrayList<Usuario> listToArray(List listUsuarios) {
        ArrayList<Usuario> usuario = new ArrayList<Usuario>();
        for (int i = 0; i < listUsuarios.size(); i++) {
            ParseObject p = (ParseObject) listUsuarios.get(i);
            usuario.add(ParseObjectToUsuarioObject(p));
        }
        return usuario;
    }

    private Usuario ParseObjectToUsuarioObject(ParseObject p) {
        String objectId = p.getObjectId();
        String nome = p.getString("nome");
        String codigo = p.getString("codigo");
        String email = p.getString("email");
        String senha = p.getString("senha");
        int id_tipo = p.getInt("id_tipo");
        int id_posicao = p.getInt("id_posicao");
        int id_time = p.getInt("id_time");
        String celular = p.getString("celular");
        String apelido = p.getString("apelido");

        Usuario user = new Usuario(nome, codigo, email, senha, id_tipo, id_posicao, id_time, celular, apelido, objectId);
        return user;
    }

    /**
     * @author Tiago Klein
     *         Classe privada de AsyncTask expecífica para busca de Usuarios.
     */
    private class BuscarUsuarios extends AsyncTask<String, Void, ArrayList<Usuario>> {

        private ProgressDialog progress;
        private Context context;

        public BuscarUsuarios(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(context);
            progress.setTitle("Buscando Usuario...");
            progress.setMessage("Aguarde...");
            progress.setCancelable(false);
            progress.setIndeterminate(true);
            progress.setCanceledOnTouchOutside(false);
            progress.show();
        }

        @Override
        protected ArrayList<Usuario> doInBackground(String... params) {
            final ArrayList<Usuario> usuario = new ArrayList<Usuario>();
            if (params[2] != null && Integer.valueOf(params[2]) > 0) {
                usuarioParsePesquisa.setLimit(Integer.valueOf(params[2]));
            }
            if (!params[0].isEmpty() || !params[1].isEmpty()) {
                usuarioParsePesquisa.whereEqualTo(params[0].trim(), params[1].trim());
            }
            List<ParseObject> results = null;
            try {
                results = usuarioParsePesquisa.find();
                if (results.size() > 0)
                    usuario.add(ParseObjectToUsuarioObject(results.get(0)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return usuario;
        }

        @Override
        protected void onPostExecute(ArrayList<Usuario> lista) {
            progress.dismiss();
        }
    }
}
