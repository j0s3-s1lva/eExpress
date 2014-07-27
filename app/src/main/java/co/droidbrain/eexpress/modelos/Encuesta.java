package co.droidbrain.eexpress.modelos;

/**
 * Created by j0s3 on 10/06/14.
 */
public class Encuesta {
    private String nombre;
    private Preguntas pregunta;

    public String getNombre(){
        return nombre;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public  Preguntas getPregunta(){
        return  pregunta;
    }
}
