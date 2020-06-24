package wackycodes.ecom.eanmart.databasequery;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import wackycodes.ecom.eanmart.MainActivity;
import wackycodes.ecom.eanmart.other.StaticValues;

import static wackycodes.ecom.eanmart.databasequery.DBQuery.currentUser;
import static wackycodes.ecom.eanmart.databasequery.DBQuery.firebaseFirestore;
import static wackycodes.ecom.eanmart.other.StaticMethods.showToast;

public class UserDataQuery {

    public static void loadUserData(final Context context, final Dialog dialog){
        firebaseFirestore.collection( "USER" ).document( FirebaseAuth.getInstance().getUid() )
                .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if (task.isSuccessful()){

                    String name = task.getResult().get( "user_name" ).toString();
                    String email = task.getResult().get( "user_email" ).toString();
                    String mobile = task.getResult().get( "user_phone" ).toString();
                    String image = task.getResult().get( "user_image" ).toString();
                    boolean isVerifyPhone = false;
                    if(task.getResult().get( "is_verify_phone" )!=null){
                        isVerifyPhone = task.getResult().getBoolean( "is_verify_phone" );
                    }

                    StaticValues.USER_DATA_MODEL.setUserAuthID( currentUser.getUid() );
                    StaticValues.USER_DATA_MODEL.setUserFullName( name );
                    StaticValues.USER_DATA_MODEL.setUserEmail( email );
                    StaticValues.USER_DATA_MODEL.setUserMobile( mobile );
                    StaticValues.USER_DATA_MODEL.setUserProfilePhoto( image );
                    StaticValues.USER_DATA_MODEL.setMobileVerify( isVerifyPhone );
                    StaticValues.USER_DATA_MODEL.setLoadData( true );

                    if (MainActivity.drawerImage != null && MainActivity.drawerName != null && MainActivity.drawerEmail != null){
                        Glide.with( context ).load( image ).into( MainActivity.drawerImage );
                        MainActivity.drawerName.setText( name );
                        MainActivity.drawerEmail.setText( email );
                    }
                    dialog.dismiss();
                }else{
                    // Show Error Message...
                    String error = task.getException().getMessage();
                    showToast(context ,error);
                    dialog.dismiss();
                }
            }
        } );
    }




}
