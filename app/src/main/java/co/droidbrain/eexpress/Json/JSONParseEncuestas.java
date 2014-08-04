package co.droidbrain.eexpress.Json;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.droidbrain.eexpress.BDManager.EncuestaBDManager;
import co.droidbrain.eexpress.modelos.Encuesta;
import co.droidbrain.eexpress.modelos.Preguntas;

/**
 * Ésta clase se encarga de leer el servicio ayudado del método definido en la clase JSONManager.
 * Created by j0s3 on 10/06/14.
 */

public class JSONParseEncuestas {

    private Activity activity;
    private JSONObject jsonObject;
    private ProgressDialog progressDialog = null;
    private Runnable runnable;
    //public static Encuesta e = new Encuesta();
   public  JSONParseEncuestas(Activity a){
       activity = a;
   }

    public void readAndParseJSONEncuestas() throws JSONException {
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    readJSONEncuestas();
                }catch (Exception e){
                }
            }
        };
        Thread thread = new Thread(null, runnable, "bgReadJSONEncuestas");
        thread.start();
        progressDialog = ProgressDialog.show(activity, "Descargando información", "Por favor espere", true);
    }

    private void readJSONEncuestas() throws JSONException{
        jsonObject = JSONManager.getJSONfromULR("http://192.168.1.12/eExpress/service.encuestas.php");

        if (jsonObject != null)
            parseJSONEncuestas(jsonObject.getJSONArray("Encuesta Express"));
        activity.runOnUiThread(returnRes);
    }

    private void parseJSONEncuestas(JSONArray encuestasArray) throws JSONException{
        EncuestaBDManager manager = new EncuestaBDManager(activity);
        ArrayList<String> opciones = new ArrayList<String>();

        try {
            manager.abrirBD();
            for (int i=0; i < encuestasArray.length();i++) {
                JSONObject json_dato =  encuestasArray.getJSONObject(i);
                Encuesta dato = new Encuesta();
                dato.setNombre(json_dato.getString("Nombre"));
                //manager.guardarTitulo(dato.getNombre(), json_dato.getInt("Preguntas"));
                //Log.i("Lectura", dato.getNombre());

                if (manager.guardarTitulo(dato.getNombre(), json_dato.getInt("Preguntas")) == true) {
                    for (int j =0; j < json_dato.getInt("Preguntas"); j++) {
                        JSONObject preguntas = json_dato.getJSONObject("Pregunta "+(j+1));
                        Preguntas pregunta = new Preguntas();
                        pregunta.setEnunciado(preguntas.getString("Enunciado"));
                        //Log.i("Lectura", pregunta.getEnunciado());

                        for (int k = 0; k < preguntas.getInt("Opciones"); k++) {
                            pregunta.setOpciones(preguntas.getString("Opcion "+(k+1)));
                            opciones.add(pregunta.getOpciones());
                            //Log.i("Lectura", "***"+opciones.get(k));
                        }
                        manager.guardarPreguntas(pregunta.getEnunciado(), opciones);
                        opciones.clear();
                        //Log.i("limpiar", "tamaño de el List: "+opciones.size());
                    }
                }

            }
            manager.cerrarBD();
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }


    }

    private Runnable returnRes = new Runnable() {
        @Override
        public void run() {
            progressDialog.dismiss();
        }
    };
}
