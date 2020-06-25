package wackycodes.ecom.eanmart.launching;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import wackycodes.ecom.eanmart.R;

import static wackycodes.ecom.eanmart.databasequery.DBQuery.currentUser;
import static wackycodes.ecom.eanmart.other.StaticMethods.showToast;
import static wackycodes.ecom.eanmart.other.StaticValues.FRAGMENT_SIGN_IN;
import static wackycodes.ecom.eanmart.other.StaticValues.FRAGMENT_SIGN_UP;

public class AuthActivity extends AppCompatActivity {

    public static int setFragmentRequest = -1;
    public static int comeFromActivity = -1;
    private FrameLayout parentFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_auth );

        parentFrameLayout = findViewById( R.id.auth_frameLayout );
        checkCurrentUser();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
//            SignInFragment.disableCloseSignFormButton = false;
            // back key...
        }
        return super.onKeyDown( keyCode, event );
    }


    private void checkCurrentUser(){
        if (currentUser != null){

            // User Already Login...
            finish();
        }else{
            // set Fragment...
            if ( setFragmentRequest == FRAGMENT_SIGN_UP ){
                setFragment( new SignUpFragment() );
            }else if (setFragmentRequest == FRAGMENT_SIGN_IN){
                setFragment( new SignInFragment() );
            }else {
                // Assign default from database..
//                currentUser = null;
                showToast(this, "Something went wrong.!");
                finish();
            }
        }

    }

    // Fragment Transaction...
    public void setFragment( Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add( parentFrameLayout.getId(),fragment );
        fragmentTransaction.commit();
    }



}
