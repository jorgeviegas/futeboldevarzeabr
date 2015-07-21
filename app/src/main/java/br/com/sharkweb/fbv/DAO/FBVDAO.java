package br.com.sharkweb.fbv.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * @author Tiago Klein
 *
 * Classe singleton que cria a conexão com banco de dados. Todas as alterações devem ser feitas usando esta classe no respectivo DAO.
 *
 * Ao executar o aplicativo está classe é executada.
 * Caso o banco de dados ainda não exista, é criado no método onCreate.
 * Caso deseje que o aplicativo atualize o banco de dados já criado, use o método onUpgrade, assim não precisará desinstalar o App do dispositivo.
 * Para isso deve-se incrementar o número da VERSAO_SCHEMA.
 *
 */
public class FBVDAO extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "fbvapp_db";
    private static final int VERSAO_SCHEMA = 1;
    private static FBVDAO fbvdao;

    private FBVDAO(Context context) {
        super(context, NOME_BANCO, null, VERSAO_SCHEMA);
    }

    public static FBVDAO getInstance(Context context) {
        if (fbvdao == null)
            fbvdao = new FBVDAO(context);
        return fbvdao;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE usuario 		(_id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, codigo TEXT, email TEXT, senha TEXT, id_tipo INTEGER, id_posicao INTEGER, id_time INTEGER );");
        db.execSQL("CREATE TABLE tipo_usuario 		(_id INTEGER PRIMARY KEY AUTOINCREMENT, tipo TEXT);");
        db.execSQL("CREATE TABLE posicao 		(_id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT);");
        db.execSQL("CREATE TABLE login 		(_id INTEGER PRIMARY KEY AUTOINCREMENT, id_usuario INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
