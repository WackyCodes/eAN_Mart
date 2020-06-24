package wackycodes.ecom.eanmart.category;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import wackycodes.ecom.eanmart.apphome.CategoryTypeModel;
import wackycodes.ecom.eanmart.apphome.HomeFragmentAdaptor;
import wackycodes.ecom.eanmart.apphome.HomeFragmentModel;

import static wackycodes.ecom.eanmart.databasequery.DBQuery.homePageCategoryList;
import static wackycodes.ecom.eanmart.databasequery.DBQuery.shopsViewFragmentList;
import static wackycodes.ecom.eanmart.other.StaticValues.TYPE_BANNER_SHOPS_VIEW;
import static wackycodes.ecom.eanmart.other.StaticValues.TYPE_HOME_CATEGORY_LAYOUT;
import static wackycodes.ecom.eanmart.other.StaticValues.TYPE_HOME_SHOP_STRIP_AD;
import static wackycodes.ecom.eanmart.other.StaticValues.TYPE_LIST_SHOPS_VIEW_NAME;


public class ShopsViewFragment extends Fragment {
    public static ShopsViewFragment shopsViewFragment;
    public static FrameLayout shopViewFragmentFrameLayout;

    public static FragmentActivity shopViewFragmentContext;
    public static FragmentManager fragmentManager;

    public static SwipeRefreshLayout swipeRefreshLayout;
    // Home Recycler view...
    private RecyclerView shopsViewRecycler;
    private HomeFragmentAdaptor homeFragmentAdaptor;

    public ShopsViewFragment() {
        // Required empty public constructor
    }

    // Sample List of Category...
    private List <CategoryTypeModel> categoryTypeModelList = new ArrayList <>();

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
        // ... Recycler
        shopsViewRecycler = view.findViewById( R.id.home_layout_container_recycler );
        LinearLayoutManager homeLinearLayoutManager = new LinearLayoutManager( view.getContext() );
        homeLinearLayoutManager.setOrientation( RecyclerView.VERTICAL );
        shopsViewRecycler.setLayoutManager( homeLinearLayoutManager );


        // Set Adaptor...
        homeFragmentAdaptor = new HomeFragmentAdaptor( shopsViewFragmentList );
        shopsViewRecycler.setAdapter( homeFragmentAdaptor );
        homeFragmentAdaptor.notifyDataSetChanged();

        if(categoryTypeModelList.size() == 0){
            // Sample Data..
            categoryTypeModelList.add( new CategoryTypeModel( TYPE_LIST_SHOPS_VIEW_NAME," ", "AN Electronuics", String.valueOf( R.drawable.product1 ) ) );
            categoryTypeModelList.add( new CategoryTypeModel( TYPE_LIST_SHOPS_VIEW_NAME," ", "Deep Mobiles", String.valueOf( R.drawable.product1 ) ) );
            categoryTypeModelList.add( new CategoryTypeModel( TYPE_LIST_SHOPS_VIEW_NAME," ", "Jay T.V. & Electronics", String.valueOf( R.drawable.product2 ) ) );
            categoryTypeModelList.add( new CategoryTypeModel( TYPE_LIST_SHOPS_VIEW_NAME," ", "Sabir Computer Shop", String.valueOf( R.drawable.product1 ) ) );
            categoryTypeModelList.add( new CategoryTypeModel( TYPE_LIST_SHOPS_VIEW_NAME," ", "Bhopali Electronics", String.valueOf( R.drawable.product2 ) ) );
            categoryTypeModelList.add( new CategoryTypeModel( TYPE_LIST_SHOPS_VIEW_NAME," ", "Pankaj Mobile shop", String.valueOf( R.drawable.product2 ) ) );

        }
        if (shopsViewFragmentList.size()==0){
            shopsViewFragmentList.add( new HomeFragmentModel( TYPE_HOME_CATEGORY_LAYOUT, categoryTypeModelList  ) );
            shopsViewFragmentList.add( new HomeFragmentModel( TYPE_HOME_SHOP_STRIP_AD, "ShopId",  String.valueOf( R.drawable.banner_c ), "Name" , TYPE_BANNER_SHOPS_VIEW ) );
            shopsViewFragmentList.add( new HomeFragmentModel( TYPE_HOME_SHOP_STRIP_AD, "ShopId",  String.valueOf( R.drawable.stip_ad_b ), "Name", TYPE_BANNER_SHOPS_VIEW  ) );
            shopsViewFragmentList.add( new HomeFragmentModel( TYPE_HOME_SHOP_STRIP_AD, "ShopId",  String.valueOf( R.drawable.strip_ad_a ), "Name", TYPE_BANNER_SHOPS_VIEW  ) );
            shopsViewFragmentList.add( new HomeFragmentModel( TYPE_HOME_SHOP_STRIP_AD, "ShopId",  String.valueOf( R.drawable.banner_c ), "Name", TYPE_BANNER_SHOPS_VIEW  ) );
            shopsViewFragmentList.add( new HomeFragmentModel( TYPE_HOME_SHOP_STRIP_AD, "ShopId",  String.valueOf( R.drawable.stip_ad_b ), "Name", TYPE_BANNER_SHOPS_VIEW  ) );
            homeFragmentAdaptor.notifyDataSetChanged();
        }

        return view;
    }




}
