package co.droidbrain.eexpress.app;

import android.app.Activity;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

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
        lista_encuesta = (ListView)findViewById(android.R.id.list);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            mAdapter = new SimpleCursorAdapter(getBaseContext(),
                    R.layout.listview_item_layout,
                    null,
                    new String[] {"_id", EncuestaBDManager.PREGUNTAS},
                    new int[] {R.id.titulo, R.id.cantidad}, 0);
            lista_encuesta.setAdapter(mAdapter);
            getLoaderManager().initLoader(0,null,this);
            return true;
        }
        if (id == R.id.consumir_rest) {
            lista_encuesta.setAdapter(null);
            JSONParseEncuestas json = new JSONParseEncuestas(this);
            try {
                json.readAndParseJSONEncuestas();
            }catch (JSONException e) {
                Log.e("ERROR", e.toString());
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        //super.onListItemClick(l, v, position, id);

        String itemValue = Long.toString(l.getSelectedItemId());
        Intent formato = new Intent(MainActivity.this, EncuestaActivity.class);

        Bundle bolsa = new Bundle();
        bolsa.putString("TITULO", itemValue);

        formato.putExtras(bolsa);
        startActivity(formato);

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
