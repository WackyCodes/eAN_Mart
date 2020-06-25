package wackycodes.ecom.eanmart.shophome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.bannerslider.BannerSliderModel;
import wackycodes.ecom.eanmart.category.ShopsViewFragment;
import wackycodes.ecom.eanmart.databasequery.DBQuery;
import wackycodes.ecom.eanmart.databasequery.UserDataQuery;
import wackycodes.ecom.eanmart.other.CheckInternetConnection;
import wackycodes.ecom.eanmart.other.DialogsClass;
import wackycodes.ecom.eanmart.productdetails.ProductDetails;

import static wackycodes.ecom.eanmart.databasequery.DBQuery.currentUser;
import static wackycodes.ecom.eanmart.databasequery.DBQuery.shopHomeCategoryList;
import static wackycodes.ecom.eanmart.databasequery.DBQuery.shopHomeCategoryListName;
import static wackycodes.ecom.eanmart.other.StaticValues.SHOP_HOME_ACTIVITY;
import static wackycodes.ecom.eanmart.shophome.HorizontalItemViewModel.hrViewType;

public class ShopHomeActivity extends AppCompatActivity {


    private String shopID;


    public static SwipeRefreshLayout shopHomeSwipeRefreshLayout;
    public static TextView locationText;
    public static TextView badgeCartCount;

    private RecyclerView shopHomeContainerRecycler;

    private ShopHomeFragmentAdaptor shopHomeFragmentAdaptor;

    //------ View Pager for Banner Slider...
    public static List<BannerSliderModel> bannerSliderModelList;
    //------ View Pager for Banner Slider...
    // ------- Horizontal Item View ..----------------
    public static List <HorizontalItemViewModel> horizontalItemViewModelList;
    public static List<HorizontalItemViewModel> gridLayoutViewList;
    // ------- Horizontal Item View ..----------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_show_home );

        Toolbar toolbar = findViewById( R.id.appToolbar );
        setSupportActionBar( toolbar );
        try{
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar().setTitle( "AN Electronics" );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException e){
        }
        // get ShopId from Intent...
        shopID = getIntent().getStringExtra( "SHOP_ID" );

        locationText = findViewById( R.id.shop_location );

        shopHomeSwipeRefreshLayout = findViewById( R.id.shop_home_swipe_refresh_layout );
        shopHomeContainerRecycler = findViewById( R.id.shop_home_layout_container_recycler );


        // Assign value for horizontal product view in box shape...
        hrViewType = 0;
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
        // ========== Home Layout Container Recycler --------------------


        if (shopHomeCategoryList.size() == 0) {
            if (CheckInternetConnection.isInternetConnected( this )) {
                DBQuery.getQuerySetFragmentData( this, shopHomeContainerRecycler, 0, "HOME" );
            }
        } else {
            if (shopHomeCategoryList.get( 0 ).size() == 0){
                if (CheckInternetConnection.isInternetConnected( this )) {
                    DBQuery.getQuerySetFragmentData( this, shopHomeContainerRecycler, 0, "HOME" );
                }
            }else{
                shopHomeFragmentAdaptor = new ShopHomeFragmentAdaptor( shopHomeCategoryList.get( 0 ) );
                shopHomeContainerRecycler.setAdapter( shopHomeFragmentAdaptor );
                shopHomeFragmentAdaptor.notifyDataSetChanged();
            }

        }
        // ========== Home Layout Container Recycler --------------------
//        }

        // ----= Refresh Layout... check is Null.?
        if (shopHomeSwipeRefreshLayout != null)

            shopHomeSwipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener(){

                @Override
                public void onRefresh() {
                    shopHomeSwipeRefreshLayout.setRefreshing( true );

                    if (CheckInternetConnection.isInternetConnected( ShopHomeActivity.this )){
                        hrViewType = 0;

                        shopHomeCategoryList.get( 0 ).clear();
                        DBQuery.getQuerySetFragmentData( ShopHomeActivity.this,
                                shopHomeContainerRecycler, 0, "HOME" );

                        // Access data again from database...
//                        getQueryCategoryIcon( categoryIconRecycler, getContext() );
//
//                        homeCategoryListName.add("HOME");
//                        homeCategoryList.add( new ArrayList<HomeFragmentModel>() );
//                        getQuerySetFragmentData(getContext(), homeLayoutContainerRecycler, 0, "HOME");

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
//                    startActivity( new Intent(ProductDetails.this, MainActivity.class) );
//                    MainActivity.isFragmentIsMyCart = true;
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
//                startActivity( new Intent(this, MainActivity.class) );
//                MainActivity.isFragmentIsMyCart = true;
            }
            return true;
        }
        return super.onOptionsItemSelected( item );
    }



}

