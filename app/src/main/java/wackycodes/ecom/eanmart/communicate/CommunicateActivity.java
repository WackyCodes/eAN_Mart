package wackycodes.ecom.eanmart.communicate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import wackycodes.ecom.eanmart.R;

import static wackycodes.ecom.eanmart.other.StaticValues.FRAGMENT_ABOUT_US;

public class CommunicateActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    private int fragmentCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_communicate );

        fragmentCode = getIntent().getIntExtra( "FRAGMENT_CODE", -1 );

        Toolbar toolbar = findViewById( R.id.x_ToolBar );
        setSupportActionBar( toolbar );
        // Set Title on Action Menu
        try{
            getSupportActionBar().setDisplayShowTitleEnabled( true );
//            getSupportActionBar().setTitle( "" );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException e){
        }

        //
        frameLayout = findViewById( R.id.communicate_frame );

        if (fragmentCode == FRAGMENT_ABOUT_US){
            getSupportActionBar().setTitle( "About Us" );
            setFragment(new AboutUsFragment());
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }else

            return super.onOptionsItemSelected( item );
    }

    // Fragment Transaction...
    private void setFragment( Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add( frameLayout.getId(),fragment );
        fragmentTransaction.commit();
    }

}
