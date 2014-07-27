package co.droidbrain.eexpress.app;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import co.droidbrain.eexpress.BDManager.EncuestaBDManager;

/**
 * Created by j0s3 on 13/06/14.
 */
public class EncuestaProvider extends ContentProvider{

    public static final String PROVIDER_NAME = "co.droidbrain.eexpress.app.encuestaprovider";
    /** A uri to do operations on cust_master table. A content provider is identified by its uri */
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/encuestas" );

    /** Constants to identify the requested operation */
    private static final int ENCUESTAS = 1;

    private static final UriMatcher uriMatcher ;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "encuestas", ENCUESTAS);
    }
    /** This content provider does the database operations by this object */
    EncuestaBDManager mDB;

    @Override
    public boolean onCreate() {
        mDB = new EncuestaBDManager(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if(uriMatcher.match(uri)==ENCUESTAS){
            try {
                mDB.abrirBD();

            } catch (Exception e) {
                e.printStackTrace();
                mDB.cerrarBD();
            }
        }else{
            return null;
        }
        Cursor c = mDB.getNombreEncuestas();
        return c;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
