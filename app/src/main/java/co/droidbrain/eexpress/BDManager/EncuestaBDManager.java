package co.droidbrain.eexpress.BDManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 * Clase para realizar acciones en la base de datos
 * Created by j0s3 on 11/06/14.
 */
public class EncuestaBDManager {
    public static final String ID_ENCUESTA = "id_encuesta";
    public static final String TITULO = "titulo";
    public static final String PREGUNTAS = "preguntas";
    public static final String FECHA = "fecha";

    public static final String ID_PREGUNTA = "id_pregunta";
    public static final String TEXTO_PREGUNTA = "texto_pregunta";

    public static final String ID_OPCION = "id_opcion";
    public static final String TEXTO_OPCION = "texto_opcion";

    private static final String N_BD = "encuesta_express";
    private static final String N_TABLA[] = {"encuestas","preguntas","opciones"};
    private static final int VERSION_BD = 1;

    private BDHelper nHelper;
    private final Context nContexto;
    private SQLiteDatabase nBD;


    public static class BDHelper extends SQLiteOpenHelper {

        public BDHelper(Context context) {
            super(context, N_BD, null, VERSION_BD);
        }

        @Override
        public void onCreate(SQLiteDatabase bd) {
            bd.execSQL("CREATE TABLE " + N_TABLA[0] + " (" +
                    ID_ENCUESTA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TITULO + " VARCHAR NOT NULL, " +
                    PREGUNTAS + " INTEGER NOT NULL, " +
                    FECHA + " INTEGER NOT NULL);"
            );

            bd.execSQL("CREATE TABLE " + N_TABLA[1] + " (" +
                    ID_PREGUNTA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TEXTO_PREGUNTA + " VARCHAR NOT NULL, " +
                    ID_ENCUESTA + " INTEGER NOT NULL);"
            );

            bd.execSQL("CREATE TABLE " + N_TABLA[2] + " (" +
                    ID_OPCION + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TEXTO_OPCION + " VARCHAR NOT NULL, " +
                    ID_PREGUNTA + " INTEGER NOT NULL, " +
                    ID_ENCUESTA + " INTEGER NOT NULL);"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i2) {
           for (int j=0; j<N_TABLA.length; j++) {
                db.execSQL("DROP TABLE IF EXISTS" + N_TABLA[j]); //SI EXISTE LA TABLA HACE ESTO
                onCreate(db); // SI NO EXISTE LA CREA
            }
        }
    }

    public EncuestaBDManager(Context c) {
        nContexto = c;
    }

    public EncuestaBDManager abrirBD() throws Exception {
        nHelper = new BDHelper(nContexto);
        nBD = nHelper.getWritableDatabase();
        return this;
    }

    public void cerrarBD() {
        nHelper.close();
    }

    //Se obtiene el id del ultimo registro almacenado. id_encuesta/id_pregunta
    public Cursor obtenerIdEncuesta(){
        return nBD.rawQuery("SELECT "+ID_ENCUESTA+" FROM "+N_TABLA[0]+" ORDER BY "+ID_ENCUESTA+" DESC LIMIT 1", null);
    }
    public Cursor obtenerIdPregunta(){
        return nBD.rawQuery("SELECT "+ID_PREGUNTA+" FROM "+N_TABLA[1]+" ORDER BY "+ID_PREGUNTA+" DESC LIMIT 1", null);
    }
/*
* Función para guardar el título de las encuestas en la base de datos local, también se toma la fecha de es momento
*/
    public boolean guardarTitulo(String titulo, int preguntas) throws Exception {
        // prueba de validación, para evitar registros duplicados,
        // si el cursor está vacío es porque el dato que se va guardar no se encuentra aún en la base de datos
        String[] arg = new String[] {titulo};
        //iniciamos con una consulta general para comprobar si la tabla tiene algún dato. Si no lo tiene aún... insertamos
        Cursor cursor = nBD.rawQuery("SELECT * FROM "+N_TABLA[0], null);
        if (cursor.getCount() == 0) {
            ContentValues cv = new ContentValues();
            Calendar c = Calendar.getInstance();
            SimpleDateFormat fecha = new SimpleDateFormat("dd-MM-yyyy");

            cv.put(TITULO, titulo);
            cv.put(PREGUNTAS, preguntas);
            cv.put(FECHA, fecha.format(c.getTime()));
            nBD.insert(N_TABLA[0], null, cv);

            return true;
        }else {
        // si el paso anterior indica que sí tiene datos,
        // hacemos una nueva consulta para verificar que el dato a ingresar no se encuentre dentro de la tabla
            Cursor crs = nBD.rawQuery("SELECT titulo FROM "+N_TABLA[0]+" WHERE titulo =? ", arg);
            if (crs.getCount() == 0) {
                ContentValues cv = new ContentValues();
                Calendar c = Calendar.getInstance();
                SimpleDateFormat fecha = new SimpleDateFormat("dd-MM-yyyy");

                cv.put(TITULO, titulo);
                cv.put(PREGUNTAS, preguntas);
                cv.put(FECHA, fecha.format(c.getTime()));
                nBD.insert(N_TABLA[0], null, cv);

                return true;
            }

            return false;
        }
    }
/*
* Función para guardar cada pregunta con sus respectivas opciones en la bd local.
* Relaciona la pregunta con su respectiva encuesta y la opción con su respectiva pregunta.
*/
    public void guardarPreguntas(String pregunta, ArrayList<String> opciones) throws Exception {
        ContentValues cv = new ContentValues();
        Cursor id_encuesta = obtenerIdEncuesta();
        id_encuesta.moveToFirst();

        cv.put(TEXTO_PREGUNTA, pregunta);
        cv.put(ID_ENCUESTA, id_encuesta.getInt(0));
        nBD.insert(N_TABLA[1], null, cv);

        Cursor id_pregunta = obtenerIdPregunta();
        id_pregunta.moveToFirst();

        //Log.i("hola", "estoy por aquí 1 - "+opciones.size());

        for (int i = 0; i < opciones.size(); i++) {
            //Log.i("hola", "estoy por aquí "+opciones.get(i));
            ContentValues c = new ContentValues();
            String op = opciones.get(i);
            c.put(TEXTO_OPCION, op);
            c.put(ID_PREGUNTA, id_pregunta.getInt(0));
            c.put(ID_ENCUESTA, id_encuesta.getInt(0));
            nBD.insert(N_TABLA[2], null, c);
        }
    }

    public Cursor getNombreEncuestas() {
        return nBD.rawQuery("SELECT "+TITULO+" AS _id, "+PREGUNTAS+" FROM "+N_TABLA[0], null);
    }

/*
* Método que obtiene las preguntas que hacen parte de una encuesta.
*/

    public ArrayList<ArrayList<String>> getPregEncuesta(String t) {
        ArrayList<ArrayList<String>> total = new ArrayList<ArrayList<String>>();

        ArrayList<String> pregunta = new ArrayList<String>();
        ArrayList<String> opciones = new ArrayList<String>();

        String[] titulo = new String[] {t};

        Cursor c = nBD.rawQuery("SELECT "+ID_ENCUESTA+" FROM "+N_TABLA[0]+" WHERE "+TITULO+"= ?", titulo);
        c.moveToFirst();
        int id = c.getInt(0);
        int i = 0;

        c = nBD.rawQuery("SELECT "+ID_PREGUNTA+","+TEXTO_PREGUNTA+" FROM "+N_TABLA[1]+" WHERE "+ID_ENCUESTA+"="+id, null);


        int texto_pregunta = c.getColumnIndex(TEXTO_PREGUNTA);
        int id_pregunta = c.getColumnIndex(ID_PREGUNTA);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            pregunta.add(c.getString(texto_pregunta));
            // Con el id de la pregunta anterior realiza una nueva consulta para obtenre sus opciones
            Cursor op = nBD.rawQuery("SELECT "+TEXTO_OPCION+" FROM "+N_TABLA[2]+" WHERE "+ID_ENCUESTA+"="+id+" AND "+ID_PREGUNTA+"="+c.getString(id_pregunta), null);
            int texto_opciones = op.getColumnIndex(TEXTO_OPCION);
            for (op.moveToFirst(); !op.isAfterLast(); op.moveToNext()){
                opciones.add(op.getString(texto_opciones));
                //Log.i("AÑADIDO", "Añadido: "+op.getString(op.getColumnIndex(TEXTO_OPCION)));
            }
            //mete una copia del ArrayList opciones en "total" y luego borra los datos de "opciones" para luego reutilizarlo en el siguiente ciclo
            total.add((ArrayList<String>) opciones.clone());
            opciones.clear();
        }
        total.add(0,pregunta);

        return total;
    }
}
