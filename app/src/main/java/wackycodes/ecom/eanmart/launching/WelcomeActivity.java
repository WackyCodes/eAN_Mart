package wackycodes.ecom.eanmart.launching;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import wackycodes.ecom.eanmart.MainActivity;
import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.databasequery.DBQuery;
import wackycodes.ecom.eanmart.databasequery.UserDataQuery;
import wackycodes.ecom.eanmart.other.CheckInternetConnection;
import wackycodes.ecom.eanmart.other.DialogsClass;
import wackycodes.ecom.eanmart.other.StaticValues;

import static wackycodes.ecom.eanmart.databasequery.DBQuery.currentUser;
import static wackycodes.ecom.eanmart.databasequery.DBQuery.firebaseFirestore;
import static wackycodes.ecom.eanmart.other.StaticValues.DEFAULT_CITY_NAME;
import static wackycodes.ecom.eanmart.other.StaticValues.STORAGE_PERMISSION;

public class WelcomeActivity extends AppCompatActivity {
    public static AppCompatActivity welcomeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_welcome );
        welcomeActivity = this;
        if( true ){ // CheckInternetConnection.isInternetConnected( this )
            firebaseFirestore.collection( "PERMISSION" ).document( "APP_USE_PERMISSION" )
                    .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        boolean isAllowed = task.getResult().getBoolean( StaticValues.APP_VERSION );
                        if ( isAllowed ){
//                            askStoragePermission();
                            checkCurrentUser();
                        }else{
                            DialogsClass.getMessageDialog( WelcomeActivity.this
                                    , "Sorry, Permission denied..!"
                                    , "You Don't have permission to use this App. If you have any query, Please contact App founder..!\\n Thank You!" );
                            finish();
                        }
                    }else {
                        showToast( "Failed..!" );
                        finish();
                    }
                }
            } );
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void checkCurrentUser(){

        // Load Area List...
        DBQuery.getCityListQuery();

        if (currentUser != null){
            // Load User Data...
            StaticValues.CURRENT_CITY_CODE = DEFAULT_CITY_NAME;
            StaticValues.CURRENT_CITY_NAME = DEFAULT_CITY_NAME;
            loadUserData();

        }else{
            // TODO: use Default Data to load Product and goto MainActivity...
            StaticValues.CURRENT_CITY_CODE = DEFAULT_CITY_NAME;
            StaticValues.CURRENT_CITY_NAME = DEFAULT_CITY_NAME;
            startActivity( new Intent( WelcomeActivity.this, MainActivity.class ) );
        }

    }

    // Methods...
    private void loadUserData(){
      //  Load User Data and set
        UserDataQuery.loadUserDataQuery( null, null );
      // TODO : forward to MainActivity
        startActivity( new Intent( WelcomeActivity.this, MainActivity.class ) );

    }

    private boolean isInternetConnect() {
        return CheckInternetConnection.isInternetConnected( this );
    }
    private void showToast(String msg){
        Toast.makeText( this, msg, Toast.LENGTH_SHORT ).show();
    }
    /// Storage Permission...
    public void askStoragePermission(){
        if(ContextCompat.checkSelfPermission( WelcomeActivity.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE )== PackageManager.PERMISSION_GRANTED){
            checkCurrentUser();
        }else {
            requestStoragePermission();
        }
    }
    private void requestStoragePermission(){

        if(ActivityCompat.shouldShowRequestPermissionRationale( this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE )){

            new AlertDialog.Builder( this )
                    .setTitle( "Storage Permission" )
                    .setMessage( "Storage permission is needed, because of File Storage will be required!" )
                    .setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions( WelcomeActivity.this,
                                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
//                                            , android.Manifest.permission.READ_SMS
//                                            , android.Manifest.permission.CALL_PHONE
//                                            , android.Manifest.permission.RECEIVE_SMS
//                                            , android.Manifest.permission.READ_CONTACTS
//                                            , android.Manifest.permission.ACCESS_FINE_LOCATION
                                    }, STORAGE_PERMISSION );
                        }
                    } ).setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    requestStoragePermission();
                }
            } ).create().show();
        }else{
            ActivityCompat.requestPermissions( WelcomeActivity.this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
//                            , android.Manifest.permission.READ_SMS
//                            , android.Manifest.permission.CALL_PHONE
//                            , android.Manifest.permission.RECEIVE_SMS
//                            , android.Manifest.permission.READ_CONTACTS
//                            , android.Manifest.permission.ACCESS_FINE_LOCATION
                    }, STORAGE_PERMISSION );
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode== STORAGE_PERMISSION){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                showToast( "Permission is GRANTED..." );
//
                checkCurrentUser();
            }
            else{
                showToast( "Permission DENIED!" );
                requestStoragePermission();
            }
        }
    }



}


