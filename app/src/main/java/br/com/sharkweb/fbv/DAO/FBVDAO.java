package br.com.sharkweb.fbv.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * @author Tiago Klein
 *
 * Classe singleton que cria a conex�o com banco de dados. Todas as altera��es devem ser feitas usando esta classe no respectivo DAO.
 *
 * Ao executar o aplicativo est� classe � executada.
 * Caso o banco de dados ainda n�o exista, � criado no m�todo onCreate.
 * Caso deseje que o aplicativo atualize o banco de dados j� criado, use o m�todo onUpgrade, assim n�o precisar� desinstalar o App do dispositivo.
 * Para isso deve-se incrementar o n�mero da VERSAO_SCHEMA.
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
        db.execSQL("CREATE TABLE usuario 		(_id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, codigo TEXT, email TEXT, senha TEXT, id_tipo INTEGER, id_posicao INTEGER, id_time INTEGER, celular TEXT, apelido TEXT);");
        db.execSQL("CREATE TABLE tipo_usuario 		(_id INTEGER PRIMARY KEY AUTOINCREMENT, tipo TEXT);");
        db.execSQL("CREATE TABLE posicao 		(_id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT);");
        db.execSQL("CREATE TABLE time 		(_id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, cidade TEXT, UF TEXT);");
        db.execSQL("CREATE TABLE login 		(_id INTEGER PRIMARY KEY AUTOINCREMENT, id_usuario INTEGER);");
        db.execSQL("CREATE TABLE time_usuario (_id INTEGER PRIMARY KEY AUTOINCREMENT, id_time INTEGER, id_usuario INTEGER, inativo INTEGER, posicao TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
