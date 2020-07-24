package wackycodes.ecom.eanmart.apphome.shophome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.databasequery.DBQuery;
import wackycodes.ecom.eanmart.databasequery.UserDataQuery;
import wackycodes.ecom.eanmart.other.CheckInternetConnection;
import wackycodes.ecom.eanmart.other.DialogsClass;
import wackycodes.ecom.eanmart.apphome.shophome.search.ProductSearchActivity;
import wackycodes.ecom.eanmart.userprofile.cart.CartActivity;

import static wackycodes.ecom.eanmart.databasequery.DBQuery.currentUser;
import static wackycodes.ecom.eanmart.databasequery.DBQuery.shopHomeCategoryList;
import static wackycodes.ecom.eanmart.databasequery.DBQuery.shopHomeCategoryListName;
import static wackycodes.ecom.eanmart.other.StaticValues.SHOP_ID_CURRENT;
import static wackycodes.ecom.eanmart.other.StaticValues.SHOP_PRODUCT_CAT_ACTIVITY;

public class ShopProductCatActivity extends AppCompatActivity {
    public static AppCompatActivity shopProductCatActivity;


    private static SwipeRefreshLayout shopProductCatSwipeLayout;
    public static TextView badgeCartCount;

    private RecyclerView shopProductCatRecycler;


    private ShopHomeFragmentAdaptor shopHomeFragmentAdaptor;

    private Dialog dialog;
    private String productsCatID;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_shop_product_cat );
        shopProductCatActivity = this;

        dialog = DialogsClass.getDialog( this );
        // get ShopId from Intent...
        productsCatID = getIntent().getStringExtra( "PRODUCT_CAT_ID" );
        String categoryName = getIntent().getStringExtra( "PRODUCT_CAT_NAME" );

        Toolbar toolbar = findViewById( R.id.appToolbar );
        setSupportActionBar( toolbar );
        try{
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar().setShowHideAnimationEnabled( true );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
            getSupportActionBar().setTitle( categoryName );
        }catch (NullPointerException e){
        }

        shopProductCatSwipeLayout = findViewById( R.id.shop_home_swipe_refresh_layout );
        shopProductCatRecycler = findViewById( R.id.shop_home_layout_container_recycler );

        // Assign value for horizontal product view in box shape...
        // Refresh Progress...
        shopProductCatSwipeLayout.setColorSchemeColors( this.getResources().getColor( R.color.colorPrimary ),
                this.getResources().getColor( R.color.colorPrimary ),
                this.getResources().getColor( R.color.colorPrimary ));
        // Refresh Progress...
        // -------- Home List....
        // Check Whether this category is exists...
        if (shopHomeCategoryListName.contains( productsCatID.toUpperCase() )){
            index = shopHomeCategoryListName.indexOf( productsCatID.toUpperCase() );
        }else{
            index = shopHomeCategoryListName.size();
            shopHomeCategoryListName.add( productsCatID.toUpperCase() );
            shopHomeCategoryList.add( new ArrayList <ShopHomeFragmentModel>() ); // If List Blank...
        }

        LinearLayoutManager homeLinearLayoutManager = new LinearLayoutManager( this );
        homeLinearLayoutManager.setOrientation( RecyclerView.VERTICAL );
        shopProductCatRecycler.setLayoutManager( homeLinearLayoutManager );
        // -------- Home List....

        // ========== Load List Or set Adaptor --------------------
        if (shopHomeCategoryList.get( index ).size() == 0){
            if (CheckInternetConnection.isInternetConnected( this )) {
                dialog.show();
                DBQuery.getShopHomeListQuery( this, dialog, SHOP_ID_CURRENT, productsCatID, index, shopProductCatRecycler, null );
            }
        }else{
            shopHomeFragmentAdaptor = new ShopHomeFragmentAdaptor(index, shopHomeCategoryList.get( index ) );
            shopProductCatRecycler.setAdapter( shopHomeFragmentAdaptor );
            shopHomeFragmentAdaptor.notifyDataSetChanged();
        }

        // ----= Refresh Layout... check is Null.?
        if (shopProductCatSwipeLayout != null)

            shopProductCatSwipeLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener(){

                @Override
                public void onRefresh() {
                    shopProductCatSwipeLayout.setRefreshing( true );
                    if (CheckInternetConnection.isInternetConnected( ShopProductCatActivity.this )){
                        shopHomeCategoryList.get( 0 ).clear();
                        DBQuery.getShopHomeListQuery( ShopProductCatActivity.this
                                , null, SHOP_ID_CURRENT, productsCatID, index, shopProductCatRecycler, shopProductCatSwipeLayout );
                    }else{
                        shopProductCatSwipeLayout.setRefreshing( false );
                    }

                }
            });
        // ----= Refresh Layout...


    }

    // Tool Bar Menu...
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_cart_and_search_layout,menu);
        MenuItem cartItem = menu.findItem( R.id.menu_cart );
        // Check First whether any item in cart or not...
        // if any item has in cart...
        cartItem.setActionView( R.layout.badge_cart_layout );
        badgeCartCount = cartItem.getActionView().findViewById( R.id.badge_count_text );
        if (UserDataQuery.cartItemModelList.size() > 0){
            badgeCartCount.setVisibility( View.VISIBLE );
            badgeCartCount.setText( String.valueOf( UserDataQuery.cartItemModelList.size() ) );
        }
        cartItem.getActionView().setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // GOTO : Goto Cart
                if (currentUser == null){
                    DialogsClass.signInUpDialog( ShopProductCatActivity.this, SHOP_PRODUCT_CAT_ACTIVITY );
                }else{
                    startActivity( new Intent( ShopProductCatActivity.this, CartActivity.class) );
                }
            }
        } );
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if ((item.getItemId() == android.R.id.home)){
            finish();
            return true;
        }else
        if (id == R.id.menu_cart){
            // GOTO : Goto Cart
            if (currentUser == null){
                DialogsClass.signInUpDialog( ShopProductCatActivity.this, SHOP_PRODUCT_CAT_ACTIVITY );
            }else{
                startActivity( new Intent( ShopProductCatActivity.this, CartActivity.class) );
            }
            return true;
        }else
        if (id == R.id.menu_search){
            // TODO : GOTO SHOP ACTIVITY..
            // Load All Data of shop...
            startActivity( new Intent( ShopProductCatActivity.this, ProductSearchActivity.class) );
            return true;
        }
        return super.onOptionsItemSelected( item );
    }


}
