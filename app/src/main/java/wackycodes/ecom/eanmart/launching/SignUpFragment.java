package wackycodes.ecom.eanmart.launching;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import wackycodes.ecom.eanmart.MainActivity;
import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.other.CheckInternetConnection;
import wackycodes.ecom.eanmart.other.StaticValues;

import static wackycodes.ecom.eanmart.other.StaticValues.FRAGMENT_SIGN_IN;
import static wackycodes.ecom.eanmart.other.StaticValues.FRAGMENT_SIGN_UP;

public class SignUpFragment extends Fragment {

    private FrameLayout parentFrameLayout;
    //---------
    private TextView alreadyHaveAccount;
    private ProgressDialog progressDialog;

    private EditText signUpUserName;
    private EditText signUpUserEmail;
    private EditText signUpUserPhone;
    private EditText signUpUserPass1;
    private EditText signUpUserPass2;

    private Button signUpBtn;
    //---------
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate( R.layout.fragment_sign_up, container, false );

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog( getActivity() );
        parentFrameLayout = view.findViewById( R.id.sign_up_frameLayout);

        alreadyHaveAccount = view.findViewById( R.id.sign_up_UserAlready );

        signUpUserName = view.findViewById( R.id.sign_up_UserName_rt );
        signUpUserEmail = view.findViewById( R.id.sign_up_UserEmail_rt );
        signUpUserPhone = view.findViewById( R.id.sign_up_UserPhone_rt );
        signUpUserPass1 = view.findViewById( R.id.sign_up_UserPass1_rt );
        signUpUserPass2 = view.findViewById( R.id.sign_up_UserPass2_rt );
        signUpBtn = view.findViewById( R.id.sign_up_UserRegisterBtn );


        // Sign Up Button Click Listener... and Query for Sign Up...
        signUpBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isEmailValid( signUpUserEmail ) && isValidDetails( ) && w_isInternetConnect() ){
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable( false );
                    progressDialog.show();
                    userRegistration();
                }
            }
        } );

        // If Already have any account...
        alreadyHaveAccount.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment( new SignInFragment() );
            }
        } );

        return view;
    }



    @Override
    public void onDestroyView() {
        parentFrameLayout.removeAllViews();
        super.onDestroyView();
    }

    // Fragment Transaction...
    public void setFragment( Fragment showFragment ){
        FragmentTransaction fragmentTransaction =  getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.slide_from_left, R.anim.slide_out_from_right );
        onDestroyView();
        fragmentTransaction.replace( parentFrameLayout.getId(),showFragment );
        fragmentTransaction.commit();
    }


    private void userRegistration(){

        //  Call the signUp Process or Method.
        firebaseAuth.createUserWithEmailAndPassword( signUpUserEmail.getText().toString(),
                signUpUserPass1.getText().toString() )
                .addOnCompleteListener( new OnCompleteListener <AuthResult>() {
            @Override
            public void onComplete(@NonNull Task <AuthResult> task) {
                // TODO : Check if Task is success...
                if (task.isSuccessful()){
                    // Write in local memory...
//                                StaticValues.writeFileInLocal( getActivity(),"city", StaticValues.userCityName );
//                                StaticValues.writeFileInLocal( getActivity(),"documentId", StaticValues.userAreaCode );

                    // Create a Map ... to store our data on firebase...
                    Map <String, Object> userData = new HashMap <>();
                    userData.put( "user_email", signUpUserEmail.getText().toString().trim() );
                    userData.put( "user_name", signUpUserName.getText().toString().trim() );
                    userData.put( "user_phone", signUpUserPhone.getText().toString().trim() );
                    userData.put( "user_image", "" );
                    userData.put( "app_version", StaticValues.APP_VERSION );

                    // TODO : Q02 Crate a Collection USER and make a document for new user using by Uid on fireStore...
                    firebaseFirestore.collection( "USERS" ).document( firebaseAuth.getUid() )
                            .set( userData ).addOnCompleteListener( new OnCompleteListener <Void>() {
                        @Override
                        public void onComplete(@NonNull Task <Void> task) {
                            if(task.isSuccessful()){

//                                CollectionReference userDataReference =
//                                        firebaseFirestore.collection( "USER" )
//                                                .document( firebaseAuth.getUid() )
//                                                .collection( "USER_DATA" );
                                loadUserData();
                            }else{
                                String error = task.getException().getMessage();
                                showToast(error);
                                progressDialog.dismiss();
                            }
                        }
                    } );
                    // end if ... Task success of Register user...
                }else{
                    String error = task.getException().getMessage();
                    showToast(error);
                    progressDialog.dismiss();
                }

            }
        } );



    }

    private void loadUserData(){

        // Check first whether user Come from Request or come from launching activity...
        if (AuthActivity.setFragmentRequest == FRAGMENT_SIGN_IN ||
                AuthActivity.setFragmentRequest == FRAGMENT_SIGN_UP){
            // if Come from request...

            AuthActivity.setFragmentRequest = -1;
            AuthActivity.comeFromActivity = -1;
            getActivity().finish();
        }else{
            // if come from first activity (launching activity...)
            startActivity( new Intent( getActivity(), MainActivity.class ) );
            getActivity().finish();
        }

        getActivity().finish();
    }

    //--- checkValidation
    private boolean isValidDetails(){

        String userName = signUpUserName.getText().toString();
        String userPhone = signUpUserPhone.getText().toString().trim();
        String userPass1 = signUpUserPass1.getText().toString().trim();
        String userPass2 = signUpUserPass2.getText().toString().trim();

        if (TextUtils.isEmpty( userName )){
            signUpUserName.setError( "Please Enter Your Name..!" );
            return false;
        }else  if (TextUtils.isEmpty( userPass1 )){
            signUpUserPass1.setError( "Please Enter Password..!" );
            return false;
        }else  if (TextUtils.isEmpty( userPass2 )){
            signUpUserPass2.setError( "Please Enter Password Again..!" );
            return false;
        }else
        if (!userPass1.equals( userPass2 )){
            signUpUserPass1.setError( "Password Not Matched..!" );
            signUpUserPass2.setError( "Password Not Matched..!" );
            return false;
        }
        else  if (!TextUtils.isEmpty( userPhone )){
            if (userPhone.length() < 10){
                signUpUserPhone.setError( "Please Enter Correct Mobile..!" );
                return false;
            }
        }

        return true;
    }

    private boolean isEmailValid( EditText wReference){
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
        return true;
    }

    private boolean w_isInternetConnect() {
        return CheckInternetConnection.isInternetConnected( getContext() );
    }
    // Toast message show method...
    private void showToast(String s){
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
        // or
//        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();

    }



}

