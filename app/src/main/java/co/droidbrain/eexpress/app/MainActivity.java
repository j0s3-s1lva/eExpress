package co.droidbrain.eexpress.app;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.json.JSONException;

import co.droidbrain.eexpress.BDManager.EncuestaBDManager;
import co.droidbrain.eexpress.Json.JSONParseEncuestas;


public class MainActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    ListView lista_encuesta;
    SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setTitle("Encuesta Express");

        lista_encuesta = (ListView)findViewById(android.R.id.list);

        JSONParseEncuestas json = new JSONParseEncuestas(this);
        try {
            json.readAndParseJSONEncuestas();
        }catch (JSONException e) {
            Log.e("ERROR", e.toString());
        }finally {
            //Se inicializa el loader para empezar la carga de los datos
            getLoaderManager().initLoader(0,null,this);
            mAdapter = new SimpleCursorAdapter(this,
                    R.layout.listview_encuestas_layout,
                    null,
                    new String[] {"_id", EncuestaBDManager.PREGUNTAS},
                    new int[] {R.id.titulo, R.id.cantidad}, 0);
            lista_encuesta.setAdapter(mAdapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.ver_encuestas:
                //Se reinicia el loader para volver a cargar los datos
                getLoaderManager().restartLoader(0,null,this);
                mAdapter = new SimpleCursorAdapter(this,
                        R.layout.listview_encuestas_layout,
                        null,
                        new String[] {"_id", EncuestaBDManager.PREGUNTAS},
                        new int[] {R.id.titulo, R.id.cantidad}, 0);
                lista_encuesta.setAdapter(mAdapter);
            break;
            case R.id.consumir_rest:
                JSONParseEncuestas json = new JSONParseEncuestas(this);
                try {
                    json.readAndParseJSONEncuestas();
                }catch (JSONException e) {
                    Log.e("ERROR", e.toString());
                }
             break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        //super.onListItemClick(l, v, position, id);
        TextView titulo = (TextView) v.findViewById(R.id.titulo);
        TextView preguntas = (TextView)v.findViewById(R.id.cantidad);

        int num_preguntas = Integer.parseInt(preguntas.getText().toString());
        String titulo_encuesta = titulo.getText().toString().trim();

        Intent intent = new Intent(MainActivity.this, EncuestaActivity.class);
        //Log.i("CADENA", "ESTO es: "+);
        Bundle bolsa = new Bundle();
        bolsa.putString("TITULO", titulo_encuesta);
        bolsa.putInt("PREGUNTAS", num_preguntas);

        intent.putExtras(bolsa);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = EncuestaProvider.CONTENT_URI;
        return new CursorLoader(this, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
