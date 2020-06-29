package wackycodes.ecom.eanmart.buyprocess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import wackycodes.ecom.eanmart.MainActivity;
import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.other.DialogsClass;
import wackycodes.ecom.eanmart.other.StaticValues;

import static wackycodes.ecom.eanmart.other.StaticValues.CONFORM_ORDER_ACTIVITY;
import static wackycodes.ecom.eanmart.other.StaticValues.CONTINUE_SHOPPING_FRAGMENT;

public class ConformOrderActivity extends AppCompatActivity {

    public static AppCompatActivity conFormOrderActivity;
    DialogsClass dialogsClass =  new DialogsClass(  );
    public static int index;

    private FrameLayout confirmOrderFrameLayout;

    public static int currentFrameLayout;

    public ConformOrderActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_conform_order );

        conFormOrderActivity  = this;
        currentFrameLayout = CONFORM_ORDER_ACTIVITY;
        Toolbar toolbar = findViewById( R.id.x_ToolBar );
        setSupportActionBar( toolbar );
        // Set Title on Action Menu
        try{
            getSupportActionBar().setTitle( "Conform Order" );
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException e){ }

        // get intent value...
        index = getIntent().getIntExtra( "INDEX", -1 );

        confirmOrderFrameLayout = findViewById( R.id.confirm_order_frame );

        if (!StaticValues.USER_DATA_MODEL.isLoadData()){
//            DBquery.userInformationQuery( this, dialogsClass.progressDialog( this )  );
            // TODO:?
        }

//        setFragment( new ConformOrderFragment() );

    }


    @Override
    protected void onStart() {
        super.onStart();
        //initOrderId();
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
    @Override
    public void onBackPressed() {
//        if (currentFrameLayout == CONTINUE_SHOPPING_FRAGMENT){
//            MainActivity.mainActivity.finish();
//            MainActivity.wCurrentFragment = 0;
//            finishAffinity();
//            startActivity( new Intent( ConformOrderActivity.this, MainActivity.class ) );
//            finish();
//        }else{
//            super.onBackPressed();
//        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ((item.getItemId() == android.R.id.home)){
            finish();
//            if (currentFrameLayout == CONTINUE_SHOPPING_FRAGMENT){
//                MainActivity.mainActivity.finish();
//                MainActivity.wCurrentFragment = 0;
//                finishAffinity();
//                startActivity( new Intent( ConformOrderActivity.this, MainActivity.class ) );
//                finish();
//            }else{
//                finish();
//            }
            return true;
        }
        return super.onOptionsItemSelected( item );
    }

    // Fragment Transaction...
    public void setFragment( Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.setCustomAnimations( R.anim.slide_from_right, R.anim.slide_out_from_left );
        fragmentTransaction.add( confirmOrderFrameLayout.getId(),fragment );
        fragmentTransaction.commit();
    }

    private void showToast(String s){
        Toast.makeText( ConformOrderActivity.this, s , Toast.LENGTH_SHORT ).show();
    }

    // -----------------------------------------------------------------------





}
