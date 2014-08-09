package co.droidbrain.eexpress.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import co.droidbrain.eexpress.BDManager.EncuestaBDManager;


public class EncuestaActivity extends FragmentActivity {
    ViewPager pager = null;
    EncuestaBDManager bdManager;
    String titulo_encuesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_viewpager);

        this.pager = (ViewPager) this.findViewById(R.id.pager);
        bdManager = new EncuestaBDManager(getApplicationContext());

        Bundle bolsa = getIntent().getExtras();

        titulo_encuesta = bolsa.getString("TITULO");
        int n_preguntas = bolsa.getInt("PREGUNTAS");

        this.setTitle(titulo_encuesta);
        ArrayList<ArrayList<String>> datos = obtenerPreguntas(titulo_encuesta);
        ArrayList<String> preguntas = datos.get(0);
        datos.remove(0);
        CargadorViewPager adapter = new CargadorViewPager(getSupportFragmentManager());

        for (int i = 0; i < n_preguntas; i++) {
            adapter.addFragment(PreguntasEeFragment.newInstance(preguntas.get(i), i+1, n_preguntas, datos.get(i)));
        }

        this.pager.setAdapter(adapter);
    }

    public ArrayList<ArrayList<String>> obtenerPreguntas(String nombre) {

        ArrayList<ArrayList<String>> preg = new ArrayList<ArrayList<String>>();

        try {
            bdManager.abrirBD();
            preg = bdManager.getPregEncuesta(nombre);
            bdManager.cerrarBD();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return preg;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.encuesta, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.finalizar_encuesta) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        // Return to previous page when we press back button
        if (this.pager.getCurrentItem() == 0)
            super.onBackPressed();
        else
            this.pager.setCurrentItem(this.pager.getCurrentItem() - 1);

    }
}
