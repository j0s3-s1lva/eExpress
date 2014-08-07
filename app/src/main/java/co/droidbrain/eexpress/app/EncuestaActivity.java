package co.droidbrain.eexpress.app;

import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class EncuestaActivity extends FragmentActivity {
    ViewPager pager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_viewpager);


        this.pager = (ViewPager) this.findViewById(R.id.pager);

        Bundle bolsa = getIntent().getExtras();
        this.setTitle(bolsa.getString("TITULO"));

        int n = bolsa.getInt("PREGUNTAS");

        CargadorViewPager adapter = new CargadorViewPager(getSupportFragmentManager());

        for (int i = 0; i < n; i++) {
            adapter.addFragment(PreguntasEeFragment.newInstance(Color.CYAN, i));
        }

        this.pager.setAdapter(adapter);
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
