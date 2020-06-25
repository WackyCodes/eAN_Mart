package wackycodes.ecom.eanmart.shophome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.bannerslider.BannerSliderModel;
import wackycodes.ecom.eanmart.databasequery.DBQuery;
import wackycodes.ecom.eanmart.other.CheckInternetConnection;

import static wackycodes.ecom.eanmart.databasequery.DBQuery.shopHomeCategoryList;
import static wackycodes.ecom.eanmart.databasequery.DBQuery.shopHomeCategoryListName;
import static wackycodes.ecom.eanmart.shophome.HorizontalItemViewModel.hrViewType;

public class ShopHomeActivity extends AppCompatActivity {


    private String shopID;


    public static SwipeRefreshLayout shopHomeSwipeRefreshLayout;
    public static TextView locationText;


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



}

