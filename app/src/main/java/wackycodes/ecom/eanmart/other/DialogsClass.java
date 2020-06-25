package wackycodes.ecom.eanmart.other;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import wackycodes.ecom.eanmart.MainActivity;
import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.launching.AuthActivity;
import wackycodes.ecom.eanmart.launching.SignInFragment;

import static wackycodes.ecom.eanmart.other.StaticValues.FRAGMENT_SIGN_IN;
import static wackycodes.ecom.eanmart.other.StaticValues.FRAGMENT_SIGN_UP;
import static wackycodes.ecom.eanmart.other.StaticValues.MAIN_ACTIVITY;

public class DialogsClass {



    public static Dialog getDialog(Context context){
        // ---- Progress Dialog...
        Dialog progressDialog = new Dialog( context );
        progressDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        progressDialog.setContentView( R.layout.dialog_circle_process );
        progressDialog.setCancelable( false );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getDialog.getWindow().setBackgroundDrawable( context.getDrawable( R.drawable.x_ractangle_layout ) );
        }
        progressDialog.getWindow().setLayout( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
//        getDialog.show();
        return progressDialog;
        // ---- Progress Dialog...
    }

    public static ProgressDialog getProgressDialog(Context context,@Nullable String message){
        ProgressDialog progressDialog = new ProgressDialog( context );
        if (message!=null){
            progressDialog.setMessage(message);
        }
        progressDialog.setCancelable( false );
        return progressDialog;
    }

    public static AlertDialog.Builder alertDialog(Context c, @Nullable String title, @NonNull String body) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(c);
        if (title!=null)
            builder.setTitle(title);
        builder.setMessage(body);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder;
    }

    public static Dialog getMessageDialog(Context context,@NonNull String title,@NonNull String message){
        /// Single Ok Button ...
        final Dialog dialog = new Dialog( context );
        dialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        dialog.setContentView( R.layout.dialog_message_ok_layout );
        dialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        dialog.setCancelable( false );
        Button okBtn = dialog.findViewById( R.id.dialog_ok_btn );
        TextView titleText = dialog.findViewById( R.id.dialog_title );
        TextView messageText = dialog.findViewById( R.id.dialog_message );
        titleText.setText( title );
        messageText.setText( message );
        okBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        } );
        return dialog;
    }


    public static void signInUpDialog(final Context context, final int comeFrom) {

        /// Sample Button click...
        final Dialog signInUpDialog = new Dialog( context );
        signInUpDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        signInUpDialog.setContentView( R.layout.dialog_signin_signup );
        signInUpDialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        signInUpDialog.setCancelable( false );
        Button signInBtn = signInUpDialog.findViewById( R.id.dialog_sign_in_btn );
        Button signUpBtn = signInUpDialog.findViewById( R.id.dialog_sign_up_btn );
        TextView cancelBtn = signInUpDialog.findViewById( R.id.cancelTextBtn );

        signInBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInUpDialog.dismiss();
                // TODO : Sign In...
                AuthActivity.setFragmentRequest = FRAGMENT_SIGN_IN;
                AuthActivity.comeFromActivity = comeFrom;
                context.startActivity( new Intent( context, AuthActivity.class ) );

            }
        } );
        signUpBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInUpDialog.dismiss();
                // TODO : Sign up...
                AuthActivity.setFragmentRequest = FRAGMENT_SIGN_UP;
                AuthActivity.comeFromActivity = comeFrom;
                context.startActivity( new Intent( context, AuthActivity.class ) );

            }
        } );
        cancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInUpDialog.dismiss();
                // TODO : cancel...
            }
        } );
        signInUpDialog.show();

    }


}
