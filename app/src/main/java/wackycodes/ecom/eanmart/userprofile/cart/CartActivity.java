package wackycodes.ecom.eanmart.userprofile.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import wackycodes.ecom.eanmart.MainActivity;
import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.databasequery.UserDataQuery;
import wackycodes.ecom.eanmart.other.StaticValues;
import wackycodes.ecom.eanmart.userprofile.addresses.MyAddressesActivity;

import static wackycodes.ecom.eanmart.databasequery.UserDataQuery.cartItemModelList;
import static wackycodes.ecom.eanmart.databasequery.UserDataQuery.temCartItemModelList;
import static wackycodes.ecom.eanmart.other.StaticValues.SELECT_ADDRESS;

public class CartActivity extends AppCompatActivity {

    // my cart...
    public static ConstraintLayout myCartConstLayout;

    private LinearLayout myCartContinueBtn;
    public static TextView myCartTotalAmounts2;

    private RecyclerView myCartItemRecyclerView;
    public static  MyCartAdaptor myCartAdaptor;

    public static boolean isCartEmpty;

    // Don't have any cart item...
    public static ConstraintLayout dontHaveCartConstLayout;
    private Button dontHaveCartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_cart );

        dontHaveCartConstLayout = findViewById( R.id.my_cart_dont_have_cart_ConstLayout );
        myCartConstLayout = findViewById( R.id.my_cart_ConstLayout );

        // Don't have any cart item...
        dontHaveCartBtn = findViewById( R.id.my_cart_dont_have_any_cartBtn );
        dontHaveCartBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                startActivity( new Intent(CartActivity.this, MainActivity.class ) );
//                MainActivity.isFragmentIsMyCart = false;
            }
        } );
        // Don't have any cart item...

        // My Cart...
        myCartContinueBtn = findViewById( R.id.my_cart_continue_btn);
        myCartTotalAmounts2 = findViewById( R.id.my_cart_total_amounts2);
        myCartItemRecyclerView = findViewById( R.id.my_cart_recyclerView );

        // Set value of cart variables...
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this );
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        myCartItemRecyclerView.setLayoutManager( linearLayoutManager );
        myCartAdaptor = new MyCartAdaptor( temCartItemModelList );
        myCartItemRecyclerView.setAdapter( myCartAdaptor );

        myCartAdaptor.notifyDataSetChanged();

        myCartContinueBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showConditionDialog();
                Intent myAddressIntent = new Intent( CartActivity.this, MyAddressesActivity.class );
                myAddressIntent.putExtra( "MODE", SELECT_ADDRESS );
                startActivity( myAddressIntent );
            }
        } );

        // Check first if any item in cart..
        if ( isCartEmpty ){
            myCartConstLayout.setVisibility( View.GONE );
            dontHaveCartConstLayout.setVisibility( View.VISIBLE );
        }else {
            myCartConstLayout.setVisibility( View.VISIBLE );
            dontHaveCartConstLayout.setVisibility( View.GONE );
        }

    }

    private void showConditionDialog(){

        final Dialog deliveryDialog = new Dialog( this );
        deliveryDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
//        deliveryDialog.setContentView( R.layout.dialog_delivery_charge );
////        deliveryDialog.setCancelable( false );
////
////        ImageButton closeBtn = deliveryDialog.findViewById( R.id.close_btn );
////        Button dialogContinueBtn = deliveryDialog.findViewById( R.id.accept_and_continue_btn );
////
////        closeBtn.setOnClickListener( new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                deliveryDialog.dismiss();
////            }
////        } );
////
////        dialogContinueBtn.setOnClickListener( new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////                Intent myAddressIntent = new Intent( getContext(), MyAddressesActivity.class );
////                myAddressIntent.putExtra( "MODE", SELECT_ADDRESS );
////                startActivity( myAddressIntent );
////                StaticValues.BUY_FROM_VALUE = StaticValues.BUY_FROM_CART;
////            }
////        } );

        deliveryDialog.show();

    }


}
