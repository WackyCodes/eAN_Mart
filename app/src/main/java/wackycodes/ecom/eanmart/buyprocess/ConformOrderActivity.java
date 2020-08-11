package wackycodes.ecom.eanmart.buyprocess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import wackycodes.ecom.eanmart.MainActivity;
import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.databasequery.UserDataQuery;
import wackycodes.ecom.eanmart.other.DialogsClass;
import wackycodes.ecom.eanmart.other.StaticValues;
import wackycodes.ecom.eanmart.productdetails.ProductDetails;
import wackycodes.ecom.eanmart.userprofile.cart.CartActivity;

import static wackycodes.ecom.eanmart.other.StaticValues.CONFORM_ORDER_ACTIVITY;
import static wackycodes.ecom.eanmart.other.StaticValues.USER_DATA_MODEL;

public class ConformOrderActivity extends AppCompatActivity {

    public static AppCompatActivity conFormOrderActivity;
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
        // Set Title on Action Menu...
        try{
            getSupportActionBar().setTitle( "Conform Order" );
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException e){ }

        // get intent value...
        index = getIntent().getIntExtra( "INDEX", -1 );

        confirmOrderFrameLayout = findViewById( R.id.confirm_order_frame );

        if (!StaticValues.USER_DATA_MODEL.isLoadData()){
            UserDataQuery.loadUserDataQuery(this, null);
        }

        setFragment( new ConformOrderFragment() );

    }

    @Override
    public void onBackPressed() {
        if (currentFrameLayout == CONFORM_ORDER_ACTIVITY){
            super.onBackPressed();
        }else{
            finishPrevious();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ((item.getItemId() == android.R.id.home)){
            onBackPressed();
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

    // Finishing All Previous Activity...
    private void finishPrevious(){
        // Finish Cart Activity....
        if (CartActivity.cartActivty != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!CartActivity.cartActivty.isDestroyed()){
                    CartActivity.cartActivty.finish();
                }
            }else{
                CartActivity.cartActivty.finish();
            }
        }

        // Finish ProductDetails Activity....
        if (ProductDetails.productDetails != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!ProductDetails.productDetails.isDestroyed()){
                    ProductDetails.productDetails.finish();
                }
            }else{
                ProductDetails.productDetails.finish();
            }
        }

        // Finish this Activity..
        this.finish();

    }

    // -----------------------------------------------------------------------





}
