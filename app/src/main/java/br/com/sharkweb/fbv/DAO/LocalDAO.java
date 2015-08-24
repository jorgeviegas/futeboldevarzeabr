package br.com.sharkweb.fbv.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import br.com.sharkweb.fbv.model.Local;
import br.com.sharkweb.fbv.model.Time;

public class LocalDAO {

    /*Dados da tabela*/
    private static final String NOME_TABELA = "local";
    private static final String ID = "_id";
    private static final String NOME = "nome";
    private static final String ENDERECO = "endereco";
    private static final String NUMERO = "numero";
    private static final String CIDADE = "cidade";
    private static final String ID_UF = "id_uf";
    private FBVDAO fbvdao;

    public LocalDAO(Context context) {
        fbvdao = FBVDAO.getInstance(context);
    }

    public long inserir(Local local) {
        ContentValues valores = new ContentValues();
        valores.put(NOME, local.getNome());
        valores.put(ENDERECO, local.getEndereco());
        valores.put(NUMERO, local.getNumero());
        valores.put(CIDADE, local.getCidade());
        valores.put(ID_UF, local.getId_uf());

        long retorno = fbvdao.getWritableDatabase().insert(NOME_TABELA, null, valores);
        fbvdao.close();
        return retorno;
    }

    public long alterar(Local local) {
        ContentValues valores = new ContentValues();
        valores.put(NOME, local.getNome());
        valores.put(ENDERECO, local.getEndereco());
        valores.put(NUMERO, local.getNumero());
        valores.put(CIDADE, local.getCidade());
        valores.put(ID_UF, local.getId_uf());

        String[] whereArgs = {Integer.toString(local.getId())};
        int retorno = fbvdao.getWritableDatabase().update(NOME_TABELA, valores, ID + " = ?", whereArgs);
        fbvdao.close();
        return retorno;
    }

    public ArrayList<Local> selectLocais() {
        ArrayList<Local> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " ORDER BY " + NOME, null));
        fbvdao.close();
        return c;
    }

    public ArrayList<Local> selectLocalPorId(int id) {
        ArrayList<Local> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + ID + " = " + id + " ORDER BY " + NOME, null));
        fbvdao.close();
        return c;
    }

    public void excluirTodosLocais() {
        fbvdao.getReadableDatabase().delete(NOME_TABELA, null, null);
    }

    public long excluirLocalPorId(int id) {
        String[] whereArgs = {Integer.toString(id)};
        long retorno;
        retorno = fbvdao.getReadableDatabase().delete(NOME_TABELA, ID + " = ?", whereArgs);
        fbvdao.close();
        return retorno;
    }

    private ArrayList<Local> cursorToArray(Cursor c) {
        ArrayList<Local> locais = new ArrayList<Local>();
        while (c.moveToNext()) {
            locais.add(new Local(c.getInt(0),c.getString(1),c.getString(2),c.getInt(3),c.getString(4),c.getInt(5)));
        }
        return locais;
    }
}
