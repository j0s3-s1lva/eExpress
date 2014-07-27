package co.droidbrain.eexpress.Json;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by j0s3 on 10/06/14.
 */
public class JSONManager {
/**
 * El método se conecta a la url que le indiquemos, trata de leer y parsear la respuesta en un objeto JSONObject.
 */
    public static JSONObject getJSONfromULR(String url){
        InputStream is = null;
        String result = "";
        JSONObject json = null;

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        }catch (Exception e){}

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        }catch (Exception e){}

        try {
            json = new JSONObject(result);
        }catch (JSONException e){}

        return json;
    }
}
