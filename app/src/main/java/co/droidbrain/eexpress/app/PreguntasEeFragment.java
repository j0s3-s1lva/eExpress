package co.droidbrain.eexpress.app;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by j0s3 on 6/08/14.
 */
public class PreguntasEeFragment extends Fragment {
    /**
     * Key para normbrar las bolsas (Bundle).
     */
    private static final String PREGUNTAS_ENCUESTA = "preguntas";
    private static final String INDEX = "index";
    private static final String NUMERO_PREGUNTAS = "n_preguntas";
    private static final String OPCIONES_PREGUNTAS = "opciones";

    private String pregunta;
    private int index;
    private int n_preguntas = 0;
    private ArrayList<String> opciones;

    /**
     * Constructor que isntancia un nuevo fragment
     *
     * @param preg
     *            Array de preguntas
     * @param index
     *            index page
     * @param op
     * @return a new page
     */
    public static PreguntasEeFragment newInstance(String preg, int index, int n_preg, ArrayList<String> op) {

        // Instaciamos un nuevo fragment
        PreguntasEeFragment fragment = new PreguntasEeFragment();

        // Guardamos los parámetros
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

        // Cargamos los parámetros iniciales
        this.pregunta = (getArguments() != null) ? getArguments().getString(PREGUNTAS_ENCUESTA) : null;
        this.index = (getArguments() != null) ? getArguments().getInt(INDEX) : -1;
        this.n_preguntas = (getArguments() != null) ? getArguments().getInt(NUMERO_PREGUNTAS) : 0;
        this.opciones = (getArguments() != null) ? getArguments().getStringArrayList(OPCIONES_PREGUNTAS) : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_preguntas_ee, container, false);

        ListView lista = (ListView) rootView.findViewById(R.id.lvOpciones);
        TextView tvIndex = (TextView) rootView.findViewById(R.id.tvIndex);
        TextView tvPreguntas = (TextView) rootView.findViewById(R.id.tvContador);

        AdaptadorListOpciones adapter;
        // Inicializamos el adapter personalizado.
        adapter = new AdaptadorListOpciones(getActivity(), opciones);

        tvIndex.setText(this.pregunta);
        tvPreguntas.setText(this.index+"/"+n_preguntas);

        lista.setAdapter(adapter);

        return rootView;

    }
}
