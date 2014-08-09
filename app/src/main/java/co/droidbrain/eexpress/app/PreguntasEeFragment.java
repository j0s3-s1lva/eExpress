package co.droidbrain.eexpress.app;


import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

import co.droidbrain.eexpress.BDManager.EncuestaBDManager;

/**
 * Created by j0s3 on 6/08/14.
 */
public class PreguntasEeFragment extends Fragment {
    /**
     * Key to insert the background color into the mapping of a Bundle.
     */
    private static final String PREGUNTAS_ENCUESTA = "preguntas";

    /**
     * Key to insert the index page into the mapping of a Bundle.
     */
    private static final String INDEX = "index";
    private static final String NUMERO_PREGUNTAS = "n_preguntas";
    private static final String OPCIONES_PREGUNTAS = "opciones";

    private String pregunta; //int color;
    private int index;
    private int n_preguntas = 0;
    private ArrayList<String> opciones;

    /**
     * Instances a new fragment with a background color and an index page.
     *
     * @param preg
     *            Array de preguntas
     * @param index
     *            index page
     * @param op
     * @return a new page
     */
    public static PreguntasEeFragment newInstance(String preg, int index, int n_preg, ArrayList<String> op) {

        // Instantiate a new fragment
        PreguntasEeFragment fragment = new PreguntasEeFragment();

        // Save the parameters
        Bundle bundle = new Bundle();
        bundle.putString(PREGUNTAS_ENCUESTA, preg);
        bundle.putInt(INDEX, index);
        bundle.putInt(NUMERO_PREGUNTAS, n_preg);
        bundle.putStringArrayList(OPCIONES_PREGUNTAS, op);
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);

        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Load parameters when the initial creation of the fragment is done
        this.pregunta = (getArguments() != null) ? getArguments().getString(PREGUNTAS_ENCUESTA) : null;
        this.index = (getArguments() != null) ? getArguments().getInt(INDEX) : -1;
        this.n_preguntas = (getArguments() != null) ? getArguments().getInt(NUMERO_PREGUNTAS) : 0;
        this.opciones = (getArguments() != null) ? getArguments().getStringArrayList(OPCIONES_PREGUNTAS) : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_preguntas_ee, container, false);

        LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.llCuadroOpciones);

        TextView tvIndex = (TextView) rootView.findViewById(R.id.tvIndex);
        TextView tvPreguntas = (TextView) rootView.findViewById(R.id.tvContador);

        Iterator<String> iterator = opciones.iterator();

        tvIndex.setText(this.pregunta);
        tvPreguntas.setText(this.index+"/"+n_preguntas);

        while(iterator.hasNext()) {
            TextView opcion = new TextView(getActivity());
            opcion.setText(iterator.next());
            linearLayout.addView(opcion);
        }


        // Change the background color
        //rootView.setBackgroundColor(this.color);

        return rootView;

    }
}
