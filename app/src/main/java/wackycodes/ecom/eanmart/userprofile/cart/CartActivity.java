package wackycodes.ecom.eanmart.userprofile.cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import wackycodes.ecom.eanmart.MainActivity;
import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.databasequery.UserDataQuery;
import wackycodes.ecom.eanmart.other.CheckInternetConnection;
import wackycodes.ecom.eanmart.other.DialogsClass;
import wackycodes.ecom.eanmart.other.StaticValues;
import wackycodes.ecom.eanmart.productdetails.ProductDetails;
import wackycodes.ecom.eanmart.shophome.ShopHomeActivity;
import wackycodes.ecom.eanmart.userprofile.addresses.MyAddressesActivity;
import wackycodes.ecom.eanmart.userprofile.orders.DeliveryItemModel;
import wackycodes.ecom.eanmart.userprofile.orders.OrderItemModel;

import static wackycodes.ecom.eanmart.databasequery.UserDataQuery.cartItemModelList;
import static wackycodes.ecom.eanmart.databasequery.UserDataQuery.temCartItemModelList;
import static wackycodes.ecom.eanmart.other.StaticValues.SELECT_ADDRESS;
import static wackycodes.ecom.eanmart.other.StaticValues.SHOP_ID_CURRENT;
import static wackycodes.ecom.eanmart.productdetails.ProductDetails.productDetails;
import static wackycodes.ecom.eanmart.shophome.ShopHomeActivity.shopHomeActivity;

public class CartActivity extends AppCompatActivity {

    // my cart...
    public static ConstraintLayout myCartConstLayout;

    private LinearLayout myCartContinueBtn;
    public static TextView myCartTotalAmounts2;

    private RecyclerView myCartItemRecyclerView;
    public static  MyCartAdaptor myCartAdaptor;
    private TextView addMoreCartBtn;

    public static boolean isCartEmpty;

    // Don't have any cart item...
    public static ConstraintLayout dontHaveCartConstLayout;
    private Button dontHaveCartBtn;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_cart );
        dialog = DialogsClass.getDialog( this );

        Toolbar toolbar = findViewById( R.id.x_ToolBar );
        setSupportActionBar( toolbar );
        // Set Title on Action Menu
        try{
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar().setTitle( "My Cart" );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException e){
        }
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

        addMoreCartBtn = findViewById( R.id.add_more_cart_btn);
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

        addMoreCartBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productDetails!=null){
                    productDetails.finish();
                }
//                shopHomeActivity.finish();
//                Intent intent = new Intent( CartActivity.this, ShopHomeActivity.class );
//                startActivity( intent );
                finish();
            }
        } );

        setMyCartConstLayout();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_cart_delete,menu);
        MenuItem cartItem = menu.findItem( R.id.menu_remove_cart );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            return true;
        }else
            if (id == R.id.menu_remove_cart){
                if (CheckInternetConnection.isInternetConnected( CartActivity.this ) && temCartItemModelList.size()>0){
                    dialog.show();
                    clearCartList();
                }
                return true;
            }
        return super.onOptionsItemSelected( item );
    }

    private void setMyCartConstLayout(){
        // Check first if any item in cart..
        if ( isCartEmpty ){
            myCartConstLayout.setVisibility( View.GONE );
            dontHaveCartConstLayout.setVisibility( View.VISIBLE );
        }else {
            myCartConstLayout.setVisibility( View.VISIBLE );
            dontHaveCartConstLayout.setVisibility( View.GONE );
        }
    }

    private void clearCartList(){
        for (int i = 0; i<temCartItemModelList.size()-1; i++){
            dialog.show();
            UserDataQuery.deleteFromCartQuery(dialog, temCartItemModelList.get( 0 ).getCartID() );
        }
        temCartItemModelList.clear();
        isCartEmpty = true;
        myCartAdaptor.notifyDataSetChanged();
        setMyCartConstLayout();
        dialog.dismiss();
    }

}
