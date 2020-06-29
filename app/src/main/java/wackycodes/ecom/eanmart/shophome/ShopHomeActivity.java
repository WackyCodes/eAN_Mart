package wackycodes.ecom.eanmart.shophome;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.bannerslider.BannerSliderModel;
import wackycodes.ecom.eanmart.databasequery.DBQuery;
import wackycodes.ecom.eanmart.databasequery.UserDataQuery;
import wackycodes.ecom.eanmart.other.CheckInternetConnection;
import wackycodes.ecom.eanmart.other.DialogsClass;
import wackycodes.ecom.eanmart.userprofile.cart.CartActivity;

import static wackycodes.ecom.eanmart.databasequery.DBQuery.currentUser;
import static wackycodes.ecom.eanmart.databasequery.DBQuery.shopHomeCategoryList;
import static wackycodes.ecom.eanmart.other.StaticValues.SHOP_HOME_ACTIVITY;
import static wackycodes.ecom.eanmart.other.StaticValues.SHOP_ID_CURRENT;
import static wackycodes.ecom.eanmart.other.StaticValues.SHOP_ID_PREVIOUS;

public class ShopHomeActivity extends AppCompatActivity {

    private String shopID;

    public static SwipeRefreshLayout shopHomeSwipeRefreshLayout;
    public static TextView shopIDText;
    public static TextView badgeCartCount;

    private RecyclerView shopHomeContainerRecycler;

    private ShopHomeFragmentAdaptor shopHomeFragmentAdaptor;

    //------ View Pager for Banner Slider...
    public static List<BannerSliderModel> bannerSliderModelList;
    //------ View Pager for Banner Slider...

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_show_home );
        dialog = DialogsClass.getDialog( this );
        Toolbar toolbar = findViewById( R.id.appToolbar );
        setSupportActionBar( toolbar );
        try{
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar().setShowHideAnimationEnabled( true );
            getSupportActionBar().setSubtitle( R.string.sample_address );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException e){
        }
        // get ShopId from Intent...
        shopID = getIntent().getStringExtra( "SHOP_ID" );
        SHOP_ID_CURRENT = shopID;
        // Check SHOP_ID : Whether Equal or Not in Current List....
        loadShopData(shopID);
        if (shopID.equals( SHOP_ID_PREVIOUS )){
            // continue...
        }else{
            // reload...
            dialog.show();
            shopHomeCategoryList.clear();
        }

        shopIDText = findViewById( R.id.shop_id );
        shopIDText.setText( shopID );

        shopHomeSwipeRefreshLayout = findViewById( R.id.shop_home_swipe_refresh_layout );
        shopHomeContainerRecycler = findViewById( R.id.shop_home_layout_container_recycler );


        // Assign value for horizontal product view in box shape...
        // Refresh Progress...
        shopHomeSwipeRefreshLayout.setColorSchemeColors( this.getResources().getColor( R.color.colorPrimary ),
                this.getResources().getColor( R.color.colorPrimary ),
                this.getResources().getColor( R.color.colorPrimary ));
        // Refresh Progress...

        // -------- Home List....

        LinearLayoutManager homeLinearLayoutManager = new LinearLayoutManager( this );
        homeLinearLayoutManager.setOrientation( RecyclerView.VERTICAL );
        shopHomeContainerRecycler.setLayoutManager( homeLinearLayoutManager );
        // -------- Home List....

        // Category icon set...
        // ========== Load List Or set Adaptor --------------------
        if (shopHomeCategoryList.size() == 0) {
            shopHomeCategoryList.add( new ArrayList <ShopHomeFragmentModel>() );
            if (CheckInternetConnection.isInternetConnected( this )) {
                dialog.show();
                DBQuery.getShopHomeListQuery( this, dialog, shopID, "HOME", 0, shopHomeContainerRecycler, null );
            }
        }
        else {
            if (shopHomeCategoryList.get( 0 ).size() == 0){
                if (CheckInternetConnection.isInternetConnected( this )) {
                    dialog.show();
                    DBQuery.getShopHomeListQuery( this, dialog, shopID, "HOME", 0, shopHomeContainerRecycler, null );
                }
            }else{
                shopHomeFragmentAdaptor = new ShopHomeFragmentAdaptor(0, shopHomeCategoryList.get( 0 ) );
                shopHomeContainerRecycler.setAdapter( shopHomeFragmentAdaptor );
                shopHomeFragmentAdaptor.notifyDataSetChanged();
            }
        }

        // ----= Refresh Layout... check is Null.?
        if (shopHomeSwipeRefreshLayout != null)

            shopHomeSwipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener(){

                @Override
                public void onRefresh() {
                    shopHomeSwipeRefreshLayout.setRefreshing( true );
                    if (CheckInternetConnection.isInternetConnected( ShopHomeActivity.this )){
                        shopHomeCategoryList.get( 0 ).clear();
                        DBQuery.getShopHomeListQuery( ShopHomeActivity.this
                                , null, shopID, "HOME", 0, shopHomeContainerRecycler, shopHomeSwipeRefreshLayout );

                        // Access data again from database...
//                        getQueryCategoryIcon( categoryIconRecycler, getContext() );
                    }else{
                        shopHomeSwipeRefreshLayout.setRefreshing( false );
                    }

                }
            });
        // ----= Refresh Layout...

    }

    // Tool Bar Menu...
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_cart_header,menu);
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
                    DialogsClass.signInUpDialog( ShopHomeActivity.this, SHOP_HOME_ACTIVITY );
                }else{
                    startActivity( new Intent( ShopHomeActivity.this, CartActivity.class) );
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
                DialogsClass.signInUpDialog( ShopHomeActivity.this, SHOP_HOME_ACTIVITY );
            }else{
                startActivity( new Intent( ShopHomeActivity.this, CartActivity.class) );
            }
            return true;
        }
        return super.onOptionsItemSelected( item );
    }

    // Load Shop Data And Check whether isOpen Shop or Not...!
    private void loadShopData(@NonNull String shopID){

        DBQuery.firebaseFirestore.collection( "SHOPS" ).document( shopID )
                .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if (task.isSuccessful()){

                    DocumentSnapshot documentSnapshot = task.getResult();

//                    boolean isOpen = documentSnapshot.getBoolean( "is_open" );
                    String shopName = documentSnapshot.get( "shop_name" ).toString();
                    getSupportActionBar().setTitle( shopName );
                    // TODO : Shop Related Data...

                }else{

                }
            }
        } );

    }


}

