package br.com.sharkweb.fbv.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import br.com.sharkweb.fbv.model.Jogo;
import br.com.sharkweb.fbv.model.Time;

public class JogoDAO {

    /*Dados da tabela*/
    private static final String NOME_TABELA = "jogo";
   // private static final String NOME_TABELA_VINCULO = "time_usuario";

    private static final String ID = "_id";
    private static final String ID_TIME = "id_time";
    private static final String ID_TIME2 = "id_time2";
    private static final String ID_LOCAL = "id_local";
    private static final String DATA = "data";
    private static final String HORA = "hora";
    private static final String HORAFINAL = "horafinal";
    private static final String INATIVO = "inativo";

    private FBVDAO fbvdao;

    public JogoDAO(Context context) {
        fbvdao = FBVDAO.getInstance(context);
    }

    public long inserir(Jogo jogo) {
        ContentValues valores = new ContentValues();
        valores.put(ID_TIME, jogo.getId_time());
        valores.put(ID_TIME2, jogo.getId_time2());
        valores.put(ID_LOCAL, jogo.getId_local());
        valores.put(DATA, jogo.getData());
        valores.put(HORA, jogo.getHora());
        valores.put(HORAFINAL, jogo.getHoraFinal());
        valores.put(INATIVO, jogo.getInativo());

        long retorno = fbvdao.getWritableDatabase().insert(NOME_TABELA, null, valores);
        fbvdao.close();
        return retorno;
    }

    public long alterar(Jogo jogo) {
        ContentValues valores = new ContentValues();
        valores.put(ID_TIME, jogo.getId_time());
        valores.put(ID_TIME2, jogo.getId_time2());
        valores.put(ID_LOCAL, jogo.getId_local());
        valores.put(DATA, jogo.getData());
        valores.put(HORA, jogo.getHora());
        valores.put(HORAFINAL, jogo.getHoraFinal());
        valores.put(INATIVO, jogo.getInativo());

        String[] whereArgs = {Integer.toString(jogo.getId())};
        int retorno = fbvdao.getWritableDatabase().update(NOME_TABELA, valores, ID + " = ?", whereArgs);
        fbvdao.close();
        return retorno;
    }

    public long inativarJogo(Jogo jogo){
        ContentValues valores = new ContentValues();
        valores.put(INATIVO, jogo.getInativo());
        String[] whereArgs = {Integer.toString(jogo.getId())};
        int retorno = fbvdao.getWritableDatabase().update(NOME_TABELA, valores, ID + " = ?", whereArgs);
        fbvdao.close();
        return retorno;
    }

    public ArrayList<Jogo> selectJogos() {
        ArrayList<Jogo> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " ORDER BY " + ID_TIME, null));
        fbvdao.close();
        return c;
    }

    public ArrayList<Jogo> selectJogoPorId(int id) {
        ArrayList<Jogo> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + ID + " = " + id + " ORDER BY " + ID, null));
        fbvdao.close();
        return c;
    }

    public ArrayList<Jogo> selectJogosPorIdTime(int id_time) {
        ArrayList<Jogo> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + ID_TIME + " = " + id_time + " ORDER BY " + DATA, null));
        fbvdao.close();
        return c;
    }

    public ArrayList<Jogo> selectJogosPorIdTimeEData(int id_time, String data) {
        ArrayList<Jogo> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + ID_TIME + " = " + id_time + " AND "+ DATA +" = '"+data+"' ORDER BY " + DATA+","+HORA, null));
        fbvdao.close();
        return c;
    }

    public ArrayList<Jogo> selectJogosPorIdTime2(int id_time2) {
        ArrayList<Jogo> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + ID_TIME2 + " = " + id_time2 + " ORDER BY " + ID, null));
        fbvdao.close();
        return c;
    }

    public void excluirTodosJogos() {
        fbvdao.getReadableDatabase().delete(NOME_TABELA, null, null);
    }

    public long excluirJogoPorId(int id) {
        String[] whereArgs = {Integer.toString(id)};
        long retorno;
        retorno = fbvdao.getReadableDatabase().delete(NOME_TABELA, ID + " = ?", whereArgs);
        fbvdao.close();
        return retorno;
    }

    private ArrayList<Jogo> cursorToArray(Cursor c) {
        ArrayList<Jogo> jogos = new ArrayList<Jogo>();
        while (c.moveToNext()) {
            jogos.add(new Jogo(c.getInt(0),c.getInt(1),c.getInt(2),c.getInt(3),c.getString(4),c.getString(5),c.getString(6),c.getInt(7)));
        }
        return jogos;
    }
}
