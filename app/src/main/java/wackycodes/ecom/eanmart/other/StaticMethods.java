package wackycodes.ecom.eanmart.other;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static wackycodes.ecom.eanmart.databasequery.DBQuery.firebaseFirestore;

public class StaticMethods {



    //Database References...
    public static DocumentReference getFirebaseDocumentRef(@NonNull String cityName){
        return firebaseFirestore.collection( "HOME" ).document( cityName.toUpperCase() );
    }
    //Database References...
    public static DocumentReference getFirebaseDocumentRef(@NonNull String cityName, @NonNull String areaCode){
        DocumentReference documentReference = firebaseFirestore.collection( "HOME" )
                .document( cityName.toUpperCase() ).collection( "SUB_LOCATION" ).document( areaCode );
        return documentReference;
    }


    // Static Toast...
    public static void showToast(Context context, String message){
        Toast.makeText( context, message, Toast.LENGTH_SHORT ).show();
    }



}
