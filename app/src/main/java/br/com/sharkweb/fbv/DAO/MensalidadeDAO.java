package br.com.sharkweb.fbv.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import br.com.sharkweb.fbv.model.Mensalidade;
import br.com.sharkweb.fbv.model.Movimento;

public class MensalidadeDAO {

    /*Dados da tabela*/
    private static final String NOME_TABELA = "mensalidade";

    private static final String ID = "_id";
    private static final String ID_TIME = "id_time";
    private static final String ID_USUARIO = "id_usuario";
    private static final String DATA = "data";
    private static final String PAGAMENTO = "pagamento";
    private static final String VALOR = "valor";
    private static final String VALOR_PAGO = "valor_pago";

    private FBVDAO fbvdao;

    public MensalidadeDAO(Context context) {
        fbvdao = FBVDAO.getInstance(context);
    }

    public long inserir(Mensalidade mensalidade) {
        ContentValues valores = new ContentValues();
        valores.put(ID_TIME, mensalidade.getId_time());
        valores.put(ID_USUARIO, mensalidade.getId_usuario());
        valores.put(DATA, mensalidade.getData());
        valores.put(PAGAMENTO, mensalidade.getPagamento());
        valores.put(VALOR, mensalidade.getValor());
        valores.put(VALOR_PAGO, mensalidade.getValor_pago());

        long retorno = fbvdao.getWritableDatabase().insert(NOME_TABELA, null, valores);
        fbvdao.close();
        return retorno;
    }

    public long alterar(Mensalidade mensalidade) {
        ContentValues valores = new ContentValues();
        valores.put(ID_TIME, mensalidade.getId_time());
        valores.put(ID_USUARIO, mensalidade.getId_usuario());
        valores.put(DATA, mensalidade.getData());
        valores.put(PAGAMENTO, mensalidade.getPagamento());
        valores.put(VALOR, mensalidade.getValor());
        valores.put(VALOR_PAGO, mensalidade.getValor_pago());

        String[] whereArgs = {Integer.toString(mensalidade.getId())};
        int retorno = fbvdao.getWritableDatabase().update(NOME_TABELA, valores, ID + " = ?", whereArgs);
        fbvdao.close();
        return retorno;
    }

    public ArrayList<Mensalidade> selectMensalidades() {
        ArrayList<Mensalidade> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " ORDER BY " + ID, null));
        fbvdao.close();
        return c;
    }

    public ArrayList<Mensalidade> selectMensalidadePorId(int id) {
        ArrayList<Mensalidade> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + ID + " = " + id + " ORDER BY " + ID, null));
        fbvdao.close();
        return c;
    }

    public ArrayList<Mensalidade> selectMensalidadesPorIdTime(int id_time) {
        ArrayList<Mensalidade> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + ID_TIME + " = " + id_time + " ORDER BY " + DATA, null));
        fbvdao.close();
        return c;
    }
    public ArrayList<Mensalidade> selectMensalidadesPorIdTimeUsuario(int id_time, int id_usuario) {
        ArrayList<Mensalidade> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + ID_TIME + " = " + id_time + " AND " + ID_USUARIO +" = "+id_usuario+" ORDER BY " + DATA, null));
        fbvdao.close();
        return c;
    }

    public void excluirTodasMensalidades() {
        fbvdao.getReadableDatabase().delete(NOME_TABELA, null, null);
    }

    public long excluirMensalidadePorId(int id) {
        String[] whereArgs = {Integer.toString(id)};
        long retorno;
        retorno = fbvdao.getReadableDatabase().delete(NOME_TABELA, ID + " = ?", whereArgs);
        fbvdao.close();
        return retorno;
    }

    private ArrayList<Mensalidade> cursorToArray(Cursor c) {
        ArrayList<Mensalidade> mensalidades = new ArrayList<Mensalidade>();
        while (c.moveToNext()) {
            mensalidades.add(new Mensalidade(c.getInt(0),c.getInt(1),c.getInt(2),c.getString(3),c.getInt(4),c.getDouble(5),c.getDouble(6)));
        }
        return mensalidades;
    }
}
