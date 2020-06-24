package wackycodes.ecom.eanmart.launching;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

import wackycodes.ecom.eanmart.R;

public class AuthActivity extends AppCompatActivity {

    public static int setFragmentRequest = -1;
    public static int comeFromActivity = -1;
    private FrameLayout parentFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_auth );
    }




}
