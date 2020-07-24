package wackycodes.ecom.eanmart.apphome.category;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.apphome.mainhome.CategoryTypeModel;
import wackycodes.ecom.eanmart.apphome.mainhome.MainHomeFragmentAdaptor;
import wackycodes.ecom.eanmart.databasequery.DBQuery;
import wackycodes.ecom.eanmart.other.CheckInternetConnection;
import wackycodes.ecom.eanmart.other.DialogsClass;

import static wackycodes.ecom.eanmart.databasequery.DBQuery.shopsViewFragmentList;
import static wackycodes.ecom.eanmart.other.StaticValues.CURRENT_CITY_NAME;
import static wackycodes.ecom.eanmart.other.StaticValues.LIST_SHOP_VIEW_PAGE;


public class ShopsViewFragment extends Fragment {
    public static ShopsViewFragment shopsViewFragment;
    public static FrameLayout shopViewFragmentFrameLayout;

    public static FragmentActivity shopViewFragmentContext;
    public static FragmentManager fragmentManager;

    public static SwipeRefreshLayout swipeRefreshLayout;
    // Home Recycler view...
    private RecyclerView shopsViewRecycler;
    public static MainHomeFragmentAdaptor shopViewFragmentAdaptor;

    public static String catID;
    public ShopsViewFragment() {
        // Required empty public constructor
    }
    public ShopsViewFragment(String categoryId) {
        catID = categoryId;
    }

    // Sample List of Category...
    private List <CategoryTypeModel> categoryTypeModelList = new ArrayList <>();

    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate( R.layout.fragment_shops_view, container, false );

        shopViewFragmentContext = getActivity();
        shopViewFragmentFrameLayout = view.findViewById( R.id.shops_view_frame_layout );

        fragmentManager = getActivity().getSupportFragmentManager();

        // Refresh Progress...
        swipeRefreshLayout = view.findViewById( R.id.home_swipe_refresh_layout );
        swipeRefreshLayout.setColorSchemeColors( getContext().getResources().getColor( R.color.colorPrimary ),
                getContext().getResources().getColor( R.color.colorPrimary ),
                getContext().getResources().getColor( R.color.colorPrimary ));
        // Refresh Progress...
        dialog = DialogsClass.getDialog( view.getContext() );
        if (catID != null){
            dialog.show();
            DBQuery.getShopsViewFragmentListQuery( dialog, null, CURRENT_CITY_NAME, catID );
        }

        // ... Recycler
        shopsViewRecycler = view.findViewById( R.id.home_layout_container_recycler );
        LinearLayoutManager homeLinearLayoutManager = new LinearLayoutManager( view.getContext() );
        homeLinearLayoutManager.setOrientation( RecyclerView.VERTICAL );
        shopsViewRecycler.setLayoutManager( homeLinearLayoutManager );


        // Set Adaptor...
        shopViewFragmentAdaptor = new MainHomeFragmentAdaptor( shopsViewFragmentList, LIST_SHOP_VIEW_PAGE );
        shopsViewRecycler.setAdapter( shopViewFragmentAdaptor );
        shopViewFragmentAdaptor.notifyDataSetChanged();

//        if (shopsViewFragmentList.size()==0){
//            DBQuery.getShopsViewFragmentListQuery( CURRENT_CITY_NAME, "ELECTRONICS" );
//        }

        // Swipe Refresh...
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener(){

                @Override
                public void onRefresh() {
                    swipeRefreshLayout.setRefreshing( true );
                    if (CheckInternetConnection.isInternetConnected( getContext() )){
                        // Refreshing...
                        DBQuery.getShopsViewFragmentListQuery( null, swipeRefreshLayout, CURRENT_CITY_NAME, catID );
                    }else{
                        swipeRefreshLayout.setRefreshing( false );
                    }

                }
            });

        return view;
    }

    public boolean onBackPressed(){
        return false;
    }


}
