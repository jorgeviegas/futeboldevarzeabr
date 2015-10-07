package br.com.sharkweb.fbv.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import br.com.sharkweb.fbv.model.Jogo;
import br.com.sharkweb.fbv.model.Movimento;

public class MovimentoDAO {

    /*Dados da tabela*/
    private static final String NOME_TABELA = "movimento";

    private static final String ID = "_id";
    private static final String ID_CAIXA = "id_caixa";
    private static final String HISTORICO = "historico";
    private static final String DATA = "data";
    private static final String VALOR = "valor";
    private static final String TIPO = "tipo";
    private static final String ID_USUARIO = "id_usuario";

    private FBVDAO fbvdao;

    public MovimentoDAO(Context context) {
        fbvdao = FBVDAO.getInstance(context);
    }

    public long inserir(Movimento movimento) {
        ContentValues valores = new ContentValues();
        valores.put(ID_CAIXA, movimento.getId_caixa());
        valores.put(HISTORICO, movimento.getHistorico());
        valores.put(DATA, movimento.getData());
        valores.put(VALOR, movimento.getValor());
        valores.put(TIPO, String.valueOf(movimento.getTipo()).trim());
        valores.put(ID_USUARIO, movimento.getId_usuario());

        long retorno = fbvdao.getWritableDatabase().insert(NOME_TABELA, null, valores);
        fbvdao.close();
        return retorno;
    }

    public long alterar(Movimento movimento) {
        ContentValues valores = new ContentValues();
        valores.put(ID_CAIXA, movimento.getId_caixa());
        valores.put(HISTORICO, movimento.getHistorico());
        valores.put(DATA, movimento.getData());
        valores.put(VALOR, movimento.getValor());
        valores.put(TIPO, movimento.getTipo());
        valores.put(ID_USUARIO, movimento.getId_usuario());

        String[] whereArgs = {Integer.toString(movimento.getId())};
        int retorno = fbvdao.getWritableDatabase().update(NOME_TABELA, valores, ID + " = ?", whereArgs);
        fbvdao.close();
        return retorno;
    }

    public ArrayList<Movimento> selectMovimentos() {
        ArrayList<Movimento> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " ORDER BY " + ID, null));
        fbvdao.close();
        return c;
    }

    public ArrayList<Movimento> selectMovimentosPorId(int id) {
        ArrayList<Movimento> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + ID + " = " + id + " ORDER BY " + ID, null));
        fbvdao.close();
        return c;
    }

    public ArrayList<Movimento> selectMovimentosPorIdCaixa(int id_caixa) {
        ArrayList<Movimento> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + ID_CAIXA + " = " + id_caixa + " ORDER BY " + DATA, null));
        fbvdao.close();
        return c;
    }

    public void excluirTodosMovimentos() {
        fbvdao.getReadableDatabase().delete(NOME_TABELA, null, null);
    }

    public long excluirMovimentoPorId(int id) {
        String[] whereArgs = {Integer.toString(id)};
        long retorno;
        retorno = fbvdao.getReadableDatabase().delete(NOME_TABELA, ID + " = ?", whereArgs);
        fbvdao.close();
        return retorno;
    }

    private ArrayList<Movimento> cursorToArray(Cursor c) {
        ArrayList<Movimento> movimentos = new ArrayList<Movimento>();
        while (c.moveToNext()) {
            movimentos.add(new Movimento(c.getInt(0),c.getInt(1),c.getString(2),c.getString(3),c.getDouble(4),c.getString(5),c.getInt(6)));
        }
        return movimentos;
    }
}
