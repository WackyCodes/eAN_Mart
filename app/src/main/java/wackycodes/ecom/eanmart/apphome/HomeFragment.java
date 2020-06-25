package wackycodes.ecom.eanmart.apphome;


import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.databasequery.DBQuery;
import wackycodes.ecom.eanmart.other.CheckInternetConnection;
import wackycodes.ecom.eanmart.shophome.ShopHomeActivity;

import static wackycodes.ecom.eanmart.databasequery.DBQuery.homePageCategoryList;
import static wackycodes.ecom.eanmart.other.StaticValues.CURRENT_CITY_CODE;
import static wackycodes.ecom.eanmart.other.StaticValues.CURRENT_CITY_NAME;

public class HomeFragment extends Fragment {

    public static HomeFragment homeFragment;

    private FrameLayout frameLayout;

    // Search Variables...
    private SearchView searchView;
    private RecyclerView searchItemRecycler;
    private TextView searchPlease;
    // Search Variables...

    public static SwipeRefreshLayout homeSwipeRefreshLayout;
    // Home Recycler view...
    private RecyclerView homeRecyclerView;
    public static MainHomeFragmentAdaptor mainHomeFragmentAdaptor;

    public HomeFragment() {
        // Required empty public constructor
    }
    // Sample List of Category...
    private List <CategoryTypeModel> categoryTypeModelList = new ArrayList <>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate( R.layout.fragment_home, container, false );
        frameLayout = view.findViewById( R.id.home_fragment_frame_layout );

        // Refresh Progress...
        homeSwipeRefreshLayout = view.findViewById( R.id.home_swipe_refresh_layout );
        homeSwipeRefreshLayout.setColorSchemeColors( getContext().getResources().getColor( R.color.colorPrimary ),
                getContext().getResources().getColor( R.color.colorPrimary ),
                getContext().getResources().getColor( R.color.colorPrimary ));
        // Refresh Progress...
        // ...Home Recycler
        homeRecyclerView = view.findViewById( R.id.home_layout_container_recycler );
        LinearLayoutManager homeLinearLayoutManager = new LinearLayoutManager( view.getContext() );
        homeLinearLayoutManager.setOrientation( RecyclerView.VERTICAL );
        homeRecyclerView.setLayoutManager( homeLinearLayoutManager );

        // Set Adaptor...
        mainHomeFragmentAdaptor = new MainHomeFragmentAdaptor( homePageCategoryList );
        homeRecyclerView.setAdapter( mainHomeFragmentAdaptor );
        mainHomeFragmentAdaptor.notifyDataSetChanged();

        if (homePageCategoryList.size()==0){
            DBQuery.getHomePageCategoryListQuery( CURRENT_CITY_CODE, false );
        }

        // Swipe Refresh...
        if (homeSwipeRefreshLayout != null)
            homeSwipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener(){

                @Override
                public void onRefresh() {
                    homeSwipeRefreshLayout.setRefreshing( true );
                    if (CheckInternetConnection.isInternetConnected( getContext() )){
                        // Refreshing...
                        DBQuery.getHomePageCategoryListQuery( CURRENT_CITY_CODE, true );
                    }else{
                        homeSwipeRefreshLayout.setRefreshing( false );
                    }

                }
            });

/*
        if(categoryTypeModelList.size() == 0){
            // Sample Data..
            categoryTypeModelList.add( new CategoryTypeModel( TYPE_LIST_MAIN_HOME_CATEGORY," ", "Man Fashion", String.valueOf( R.drawable.product2 ) ) );
            categoryTypeModelList.add( new CategoryTypeModel( TYPE_LIST_MAIN_HOME_CATEGORY," ", "Woman Fashion", String.valueOf( R.drawable.product1 ) ) );
            categoryTypeModelList.add( new CategoryTypeModel( TYPE_LIST_MAIN_HOME_CATEGORY," ", "Woman Fashion", String.valueOf( R.drawable.product1 ) ) );
            categoryTypeModelList.add( new CategoryTypeModel( TYPE_LIST_MAIN_HOME_CATEGORY," ", "Jwellary", String.valueOf( R.drawable.product1 ) ) );

        }
        if (homePageCategoryList.size()==0){
            homePageCategoryList.add( new MainHomeFragmentModel( TYPE_HOME_CATEGORY_LAYOUT, categoryTypeModelList  ) );
            homePageCategoryList.add( new MainHomeFragmentModel( TYPE_HOME_SHOP_STRIP_AD, "ShopId",  String.valueOf( R.drawable.banner_c ), "Name", TYPE_BANNER_MAIN_HOME  ) );
            homePageCategoryList.add( new MainHomeFragmentModel( TYPE_HOME_SHOP_STRIP_AD, "ShopId",  String.valueOf( R.drawable.stip_ad_b ), "Name", TYPE_BANNER_MAIN_HOME  ) );
            mainHomeFragmentAdaptor.notifyDataSetChanged();
        }
*/
        return view;
    }


}
