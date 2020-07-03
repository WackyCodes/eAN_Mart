package wackycodes.ecom.eanmart.launching;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.regex.Pattern;

import wackycodes.ecom.eanmart.MainActivity;
import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.databasequery.UserDataQuery;
import wackycodes.ecom.eanmart.other.CheckInternetConnection;
import wackycodes.ecom.eanmart.other.DialogsClass;
import wackycodes.ecom.eanmart.other.StaticMethods;

import static wackycodes.ecom.eanmart.databasequery.DBQuery.currentUser;
import static wackycodes.ecom.eanmart.databasequery.DBQuery.firebaseAuth;
import static wackycodes.ecom.eanmart.other.StaticValues.FRAGMENT_SIGN_IN;
import static wackycodes.ecom.eanmart.other.StaticValues.FRAGMENT_SIGN_UP;


public class SignInFragment extends Fragment {

    private FrameLayout parentFrameLayout;
    private Dialog dialog;

    //---------
    private TextView dontHaveAccount;
    private TextView signInForgetPassword;
    private EditText signInEmail;
    private EditText signInPassword;
    private Button signInBtn;
    //---------

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate( R.layout.fragment_sign_in, container, false );

        parentFrameLayout = view.findViewById( R.id.sign_in_frameLayout);
        dialog = DialogsClass.getDialog( getContext() );

        dontHaveAccount = view.findViewById( R.id.sign_in_dont_have_account );
        signInForgetPassword = view.findViewById( R.id.sign_in_forget_password );
        signInEmail = view.findViewById( R.id.sign_in_email );
        signInPassword = view.findViewById( R.id.sign_in_password );
        signInBtn = view.findViewById( R.id.sign_in_btn );


        dontHaveAccount.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment( new SignUpFragment() );
            }
        } );

        signInEmail.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty( signInEmail.getText() )){
                    signInBtn.setEnabled( true );
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );

        signInBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email_Mobile = signInEmail.getText().toString().trim();
                final String password = signInPassword.getText().toString().trim();
                userLogIn(email_Mobile, password);
            }
        } );

        signInForgetPassword.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment( new ForgetPasswordFragment() );
            }
        } );

        return view;
    }

    @Override
    public void onDestroyView() {
//        ViewGroup mContainer = (ViewGroup) getActivity().findViewById(R.id.sign_in_frameLayout);
//        mContainer.removeAllViews();
        parentFrameLayout.removeAllViews();
        super.onDestroyView();
    }

    // Fragment Transaction...
    public void setFragment( Fragment showFragment ){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.slide_from_right, R.anim.slide_out_from_left );
        onDestroyView();
        fragmentTransaction.replace( parentFrameLayout.getId(),showFragment );
        fragmentTransaction.commit();
    }

    private void userLogIn(String email_Mobile, String password){
        if ( isEmailValid( signInEmail, password ) && w_isInternetConnect()){
            dialog.show();

            firebaseAuth.signInWithEmailAndPassword( email_Mobile, password )
                    .addOnCompleteListener( new OnCompleteListener <AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task <AuthResult> task) {
                            if (task.isSuccessful()){
                                // write file in local memory..!
                                currentUser = firebaseAuth.getCurrentUser();
                                loadUserData();
                                dialog.dismiss();
                            }else{
                                StaticMethods.showToast( getContext(), "Something going wrong..!!" );
                                dialog.dismiss();
                            }
                        }
                    } );
            //-----------
        }
    }

    private void loadUserData(){
        UserDataQuery.loadUserDataQuery( null, null );
        UserDataQuery.loadCartDataQuery( null );
        if (AuthActivity.setFragmentRequest == FRAGMENT_SIGN_IN ||
                AuthActivity.setFragmentRequest == FRAGMENT_SIGN_UP){
            AuthActivity.setFragmentRequest = -1;
            AuthActivity.comeFromActivity = -1;
            // if Come from request...
            getActivity().finish();
        }else{
            // if come from first activity (launching activity...)
            startActivity( new Intent( getActivity(), MainActivity.class ) );
            getActivity().finish();
        }
    }

    private boolean isEmailValid( EditText wReference, String password){
        String wEmail = wReference.getText().toString().trim();
        String emailRegex =
                "^[a-zA-Z0-9_+&*-]+(?:\\."+
                        "[a-zA-Z0-9_+&*-]+)*@" +
                        "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                        "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        boolean bool = pat.matcher(wEmail).matches();

        if (TextUtils.isEmpty( wEmail )) {
            wReference.setError( "Please Enter Email! " );
            return false;
        } else if (!bool){
            wReference.setError( "Please Enter Valid Email! " );
            return false;
        }
        if(TextUtils.isEmpty( password )){
            Toast.makeText(  getActivity(),"Please Enter Password",Toast.LENGTH_SHORT ).show();
            return false;
        }

        return true;
    }

    private boolean w_isInternetConnect() {
        return CheckInternetConnection.isInternetConnected( getContext() );
    }




}
