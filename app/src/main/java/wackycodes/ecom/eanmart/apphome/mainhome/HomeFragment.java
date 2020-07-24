package wackycodes.ecom.eanmart.apphome.mainhome;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.databasequery.DBQuery;
import wackycodes.ecom.eanmart.other.CheckInternetConnection;

import static wackycodes.ecom.eanmart.databasequery.DBQuery.homePageCategoryList;
import static wackycodes.ecom.eanmart.other.StaticValues.CURRENT_CITY_CODE;
import static wackycodes.ecom.eanmart.other.StaticValues.LIST_MAIN_HOME_PAGE;

public class HomeFragment extends Fragment {

    public static HomeFragment homeFragment;

    private FrameLayout frameLayout;

    public static SwipeRefreshLayout homeSwipeRefreshLayout;
    // Home Recycler view...
    private RecyclerView homeRecyclerView;
    public static MainHomeFragmentAdaptor mainHomeFragmentAdaptor;

    public HomeFragment() {
        // Required empty public constructor
    }

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
        mainHomeFragmentAdaptor = new MainHomeFragmentAdaptor( homePageCategoryList, LIST_MAIN_HOME_PAGE );
        homeRecyclerView.setAdapter( mainHomeFragmentAdaptor );
        mainHomeFragmentAdaptor.notifyDataSetChanged();

        if (homePageCategoryList.size()==0&&CURRENT_CITY_CODE !=null){
            DBQuery.getHomePageCategoryListQuery(null, null, CURRENT_CITY_CODE, false );
        }

        // Swipe Refresh...
        if (homeSwipeRefreshLayout != null)
            homeSwipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener(){

                @Override
                public void onRefresh() {
                    homeSwipeRefreshLayout.setRefreshing( true );
                    if (CheckInternetConnection.isInternetConnected( getContext() )){
                        // Refreshing...
                        DBQuery.getHomePageCategoryListQuery(null, homeSwipeRefreshLayout, CURRENT_CITY_CODE, true );
                    }else{
                        homeSwipeRefreshLayout.setRefreshing( false );
                    }

                }
            });

        return view;
    }


}
