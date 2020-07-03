package wackycodes.ecom.eanmart.databasequery;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wackycodes.ecom.eanmart.MainActivity;
import wackycodes.ecom.eanmart.apphome.CategoryTypeModel;
import wackycodes.ecom.eanmart.apphome.HomeFragment;
import wackycodes.ecom.eanmart.apphome.MainHomeFragmentModel;
import wackycodes.ecom.eanmart.bannerslider.BannerSliderModel;
import wackycodes.ecom.eanmart.category.ShopsViewFragment;
import wackycodes.ecom.eanmart.cityareacode.AreaCodeCityModel;
import wackycodes.ecom.eanmart.other.StaticValues;
import wackycodes.ecom.eanmart.productdetails.ProductModel;
import wackycodes.ecom.eanmart.shophome.ShopHomeFragmentAdaptor;
import wackycodes.ecom.eanmart.shophome.ShopHomeFragmentModel;

import static wackycodes.ecom.eanmart.other.StaticMethods.getRandomOrderID;
import static wackycodes.ecom.eanmart.other.StaticMethods.showToast;
import static wackycodes.ecom.eanmart.other.StaticValues.BANNER_SLIDER_LAYOUT_CONTAINER;
import static wackycodes.ecom.eanmart.other.StaticValues.GRID_ITEM_LAYOUT_CONTAINER;
import static wackycodes.ecom.eanmart.other.StaticValues.HORIZONTAL_ITEM_LAYOUT_CONTAINER;
import static wackycodes.ecom.eanmart.other.StaticValues.SHOP_HOME_CAT_LIST_CONTAINER;
import static wackycodes.ecom.eanmart.other.StaticValues.SHOP_ID_CURRENT;
import static wackycodes.ecom.eanmart.other.StaticValues.STRIP_AD_LAYOUT_CONTAINER;
import static wackycodes.ecom.eanmart.other.StaticValues.TYPE_BANNER_MAIN_HOME;
import static wackycodes.ecom.eanmart.other.StaticValues.TYPE_BANNER_SHOPS_VIEW;
import static wackycodes.ecom.eanmart.other.StaticValues.TYPE_HOME_CATEGORY_LAYOUT;
import static wackycodes.ecom.eanmart.other.StaticValues.TYPE_HOME_SHOP_BANNER_AD;
import static wackycodes.ecom.eanmart.other.StaticValues.TYPE_HOME_SHOP_BANNER_SLIDER;
import static wackycodes.ecom.eanmart.other.StaticValues.TYPE_HOME_SHOP_STRIP_AD;
import static wackycodes.ecom.eanmart.other.StaticValues.TYPE_LIST_MAIN_HOME_CATEGORY;
import static wackycodes.ecom.eanmart.other.StaticValues.TYPE_LIST_SHOPS_VIEW_NAME;
import static wackycodes.ecom.eanmart.shophome.ShopHomeActivity.bannerSliderModelList;

public class DBQuery {

    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    // get Current User Reference ...
    public static FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public static final ArrayList<AreaCodeCityModel> areaCodeCityModelList = new ArrayList <>();
    // Home : Main Recycler view List...
    public static List <MainHomeFragmentModel> homePageCategoryList = new ArrayList <>();
    // Shops View List : For Same category Multiple shops...
    public static List <MainHomeFragmentModel> shopsViewFragmentList = new ArrayList <>();

    public static List <String> shopHomeCategoryListName = new ArrayList <>();
    public static List <List <ShopHomeFragmentModel>> shopHomeCategoryList = new ArrayList <>();
    public static List <ProductModel> productModelList;

    // Reference...
    private static CollectionReference getCollection(String collectionName){

        CollectionReference collectionReference =
                firebaseFirestore.collection( "USERS" )
                        .document( currentUser.getUid() )
                        .collection( collectionName.toUpperCase() );
        return collectionReference;

    }

    public static void getHomePageCategoryListQuery(@Nullable final Dialog dialog, @Nullable final SwipeRefreshLayout swipeRefreshLayout,
            String cityCode, boolean reloadRequest ){

        firebaseFirestore.collection( "HOME_PAGE" ).document( cityCode.toUpperCase() )
                .collection( "HOME" ).orderBy( "index" )
                .get().addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task <QuerySnapshot> task) {
                if (task.isSuccessful()){
                    // data is loaded...
                    homePageCategoryList.clear();
                    List <CategoryTypeModel> categoryTypeModelList;

                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        if ((long) documentSnapshot.get( "type" ) == TYPE_HOME_SHOP_BANNER_SLIDER) {
                            // TODO: Banner Slider...

                        }else
                        if ((long) documentSnapshot.get( "type" ) == TYPE_HOME_CATEGORY_LAYOUT) {
                            // Grid List...
                            categoryTypeModelList = new ArrayList <>();
                            long no_of_cat = (long)documentSnapshot.get( "no_of_cat" );
                            for(long i = 1; i<=no_of_cat; i++){
                                categoryTypeModelList.add( new CategoryTypeModel(
                                        TYPE_LIST_MAIN_HOME_CATEGORY,
                                        documentSnapshot.get( "cat_id_"+i ).toString(),
                                        documentSnapshot.get( "cat_name_"+i ).toString(),
                                        documentSnapshot.get( "cat_image_"+i ).toString()
                                ) );
                            }

                            homePageCategoryList.add( new MainHomeFragmentModel( TYPE_HOME_CATEGORY_LAYOUT, categoryTypeModelList ) );

                        }else
                        if ((long) documentSnapshot.get( "type" ) == TYPE_HOME_SHOP_STRIP_AD) {
                            // Strip Ad...
                            homePageCategoryList.add( new MainHomeFragmentModel(
                                    TYPE_HOME_SHOP_STRIP_AD,
                                    documentSnapshot.get( "shop_id" ).toString(),
                                    documentSnapshot.get( "shop_image" ).toString(),
                                    documentSnapshot.get( "shop_name" ).toString(),
                                    TYPE_BANNER_MAIN_HOME  ) );

                        }else
                        if ((long) documentSnapshot.get( "type" ) == TYPE_HOME_SHOP_BANNER_AD) {
                            // Banner Ad...
                            homePageCategoryList.add( new MainHomeFragmentModel(
                                    TYPE_HOME_SHOP_BANNER_AD,
                                    documentSnapshot.get( "shop_id" ).toString(),
                                    documentSnapshot.get( "shop_image" ).toString(),
                                    documentSnapshot.get( "shop_name" ).toString(),
                                    TYPE_BANNER_MAIN_HOME  ) );
                        }

                        if(HomeFragment.mainHomeFragmentAdaptor != null){
                            HomeFragment.mainHomeFragmentAdaptor.notifyDataSetChanged();
                        }
                        if (HomeFragment.homeSwipeRefreshLayout!=null){
                            HomeFragment.homeSwipeRefreshLayout.setRefreshing(false);
                        }

                    }
                    // $ Changes...
                    if (dialog!=null){
                        dialog.dismiss();
                    }
                    if (swipeRefreshLayout != null){
                        swipeRefreshLayout.setRefreshing( false );
                    }
                }else{
                    // $ Changes...
                    if (dialog!=null){
                        dialog.dismiss();
                    }
                    if (swipeRefreshLayout != null){
                        swipeRefreshLayout.setRefreshing( false );
                    }
                }
            }
        } );

    }

    public static void getShopsViewFragmentListQuery(@Nullable final Dialog dialog, @Nullable final SwipeRefreshLayout swipeRefreshLayout
            , final String cityName, final String catId ){

        firebaseFirestore.collection( "HOME_PAGE" )
                .document( cityName.toUpperCase() )
                .collection( catId ).orderBy( "index" )
                .get().addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task <QuerySnapshot> task) {
                if (task.isSuccessful()){
                    // data is loaded...
                    shopsViewFragmentList.clear();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        if ((long) documentSnapshot.get( "type" ) == TYPE_HOME_SHOP_BANNER_SLIDER) {
                            // TODO: Banner Slider...

                        }else
                        if ((long) documentSnapshot.get( "type" ) == TYPE_HOME_CATEGORY_LAYOUT) {
                            // Grid List...
                            // One More Query Needed here...
                            firebaseFirestore.collection( "HOME_PAGE" ).document( cityName.toUpperCase() )
                                    .collection( "SHOPS" ).whereEqualTo( "category_id", catId )
                                    .get().addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task <QuerySnapshot> task1) {
                                    if (task1.isSuccessful()) {
                                        // data is loaded...
                                        List <CategoryTypeModel> categoryTypeModelList = new ArrayList <>();
                                        for (QueryDocumentSnapshot documentSnapshot1 : task1.getResult()) {
                                            categoryTypeModelList.add( new CategoryTypeModel(
                                                    TYPE_LIST_SHOPS_VIEW_NAME,
                                                    documentSnapshot1.get( "shop_id" ).toString(),
                                                    documentSnapshot1.get( "shop_name" ).toString(),
                                                    documentSnapshot1.get( "shop_image" ).toString()
                                            ) );
                                        }

                                        shopsViewFragmentList.add( 0, new MainHomeFragmentModel( TYPE_HOME_CATEGORY_LAYOUT, categoryTypeModelList ) );
                                        if(ShopsViewFragment.shopViewFragmentAdaptor != null){
                                            ShopsViewFragment.shopViewFragmentAdaptor.notifyDataSetChanged();
                                        }
                                    }else{

                                    }
                                }
                            } );

                        }else
                        if ((long) documentSnapshot.get( "type" ) == TYPE_HOME_SHOP_STRIP_AD) {
                            // Strip Ad...
                            shopsViewFragmentList.add( new MainHomeFragmentModel(
                                    TYPE_HOME_SHOP_STRIP_AD,
                                    documentSnapshot.get( "shop_id" ).toString(),
                                    documentSnapshot.get( "shop_image" ).toString(),
                                    documentSnapshot.get( "shop_name" ).toString(),
                                    TYPE_BANNER_SHOPS_VIEW  ) );

                        }else
                        if ((long) documentSnapshot.get( "type" ) == TYPE_HOME_SHOP_BANNER_AD) {
                            // Banner Ad...
                            shopsViewFragmentList.add( new MainHomeFragmentModel(
                                    TYPE_HOME_SHOP_BANNER_AD,
                                    documentSnapshot.get( "shop_id" ).toString(),
                                    documentSnapshot.get( "shop_image" ).toString(),
                                    documentSnapshot.get( "shop_name" ).toString(),
                                    TYPE_BANNER_SHOPS_VIEW  ) );
                        }

                        if(ShopsViewFragment.shopViewFragmentAdaptor != null){
                            ShopsViewFragment.shopViewFragmentAdaptor.notifyDataSetChanged();
                        }

                    }
                    // $ Changes...
                    if (dialog!=null){
                        dialog.dismiss();
                    }
                    if (swipeRefreshLayout != null){
                        swipeRefreshLayout.setRefreshing( false );
                    }
                }
                else{
                    // $ Changes...
                    if (dialog!=null){
                        dialog.dismiss();
                    }
                    if (swipeRefreshLayout != null){
                        swipeRefreshLayout.setRefreshing( false );
                    }
                }
            }
        } );

    }

    // Query to Load Fragment Data like homepage items etc...
    public static void getShopHomeListQuery(final Context context, @Nullable final Dialog dialog
            , final String shopID, String categoryID, final int index, final RecyclerView homeLayoutContainerRecycler
            , @Nullable final SwipeRefreshLayout swipeRefreshLayout ) {

        //------------------------------------------------------
        if (shopHomeCategoryList.size() != 0){
            shopHomeCategoryList.get( index ).clear(); // Reload Products Or Load First Time...
        }else{
            shopHomeCategoryListName.add( categoryID.toUpperCase() );
            shopHomeCategoryList.add( new ArrayList <ShopHomeFragmentModel>() ); // If List Blank...
        }
//        shopID = "46200001";
//        categoryID = "HOME";
        firebaseFirestore
                .collection( "SHOPS" ).document( shopID )
                .collection( categoryID ).orderBy( "index" ).get().addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task <QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ShopHomeFragmentAdaptor homeFragmentAdaptor = new ShopHomeFragmentAdaptor( index, shopHomeCategoryList.get( index ) );
                    if (homeLayoutContainerRecycler != null){
                        homeLayoutContainerRecycler.setAdapter( homeFragmentAdaptor );
                        homeFragmentAdaptor.notifyDataSetChanged();
                    }
                    // add Data from snapshot...
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                            showToast( context, "City : " + userCityName );
                        if ((long) documentSnapshot.get( "view_type" ) == BANNER_SLIDER_LAYOUT_CONTAINER) {
                            /** add banners slider */
//                            String layout_id = documentSnapshot.get( "layout_id" ).toString();
                            bannerSliderModelList = new ArrayList <>();
                            long no_of_banners = (long) documentSnapshot.get( "no_of_banners" );
                            for (long i = 1; i < no_of_banners + 1; i++) {
                                // access the banners from database...
                                bannerSliderModelList.add( new BannerSliderModel( documentSnapshot.get( "banner_" + i ).toString(),
                                        documentSnapshot.get( "banner_" + i + "_bg" ).toString() ) );
                            }
                            // add the banners list in the home recycler list...
                            if (bannerSliderModelList.size() >= 2){
                                shopHomeCategoryList.get( index ).add( new ShopHomeFragmentModel( BANNER_SLIDER_LAYOUT_CONTAINER, bannerSliderModelList ) );
                            }

                        } else
                        if ((long) documentSnapshot.get( "view_type" ) == STRIP_AD_LAYOUT_CONTAINER) {
                            /**  for strip and banner ad */
//                            String layout_id = documentSnapshot.get( "layout_id" ).toString();
                            shopHomeCategoryList.get( index ).add( new ShopHomeFragmentModel( STRIP_AD_LAYOUT_CONTAINER,
                                    documentSnapshot.get( "strip_ad" ).toString(), documentSnapshot.get( "strip_bg" ).toString() ) );
                        } else
                        if ((long) documentSnapshot.get( "view_type" ) == HORIZONTAL_ITEM_LAYOUT_CONTAINER) {
                            /** : for horizontal products */
//                            String layout_id = documentSnapshot.get( "layout_id" ).toString();
                            productModelList = new ArrayList <>();
                            List<String> hrAndGridProductIdList = new ArrayList <>();
                            long no_of_products = (long) documentSnapshot.get( "no_of_products" );
                            for (long i = 1; i < no_of_products + 1; i++) {
                                // Load Product Data List After set Adaptor... on View Time...
                                // Below we load only product Id...
                                hrAndGridProductIdList.add( documentSnapshot.get( "product_id_" + i ).toString() );
                            }
                            // add list in home fragment model
                            //  TODO : To User
                            if (hrAndGridProductIdList.size()>=2){
                                shopHomeCategoryList.get( index ).add( new ShopHomeFragmentModel( HORIZONTAL_ITEM_LAYOUT_CONTAINER, hrAndGridProductIdList,
                                        productModelList, documentSnapshot.get( "layout_title" ).toString() ) );
                            }

                        } else
                        if ((long) documentSnapshot.get( "view_type" ) == GRID_ITEM_LAYOUT_CONTAINER) {
                            /** for grid products */
//                            String layout_id = documentSnapshot.get( "layout_id" ).toString();

                            productModelList = new ArrayList <>();
                            List<String> hrAndGridProductIdList = new ArrayList <>();
                            long no_of_products = (long) documentSnapshot.get( "no_of_products" );
                            for (long i = 1; i < no_of_products + 1; i++) {
                                // access the banners from database...
                                hrAndGridProductIdList.add( documentSnapshot.get( "product_id_" + i ).toString() );
                            }
                            // add list in home fragment model
                            // TODO : To User
                            if (hrAndGridProductIdList.size()>=4){
                                shopHomeCategoryList.get( index ).add( new ShopHomeFragmentModel( GRID_ITEM_LAYOUT_CONTAINER, hrAndGridProductIdList,
                                        productModelList, documentSnapshot.get( "layout_title" ).toString() ) );
                            }
//                            // TODO : To Admin
//                            commonCatList.get( index ).add( new CommonCatModel( GRID_ITEM_LAYOUT_CONTAINER, layout_id
//                                    , documentSnapshot.get( "layout_title" ).toString()
//                                    , hrAndGridProductIdList, hrAndGridProductList ) );


                        } else
                        if ((long) documentSnapshot.get( "view_type" ) == SHOP_HOME_CAT_LIST_CONTAINER) {
                            // TODO : for Category
                         long no_of_cat = (long) documentSnapshot.get( "no_of_cat" );
                         List <CategoryTypeModel> categoryTypeModelList = new ArrayList <>();
                         for (long i=1; i<=no_of_cat; i++){
                             categoryTypeModelList.add( new CategoryTypeModel(
                                     documentSnapshot.get( "cat_id_"+i ).toString(),
                                     documentSnapshot.get( "cat_name_"+i ).toString(),
                                     documentSnapshot.get( "cat_image_"+i ).toString()
                             ) );
                         }
                            shopHomeCategoryList.get( index ).add( new ShopHomeFragmentModel( SHOP_HOME_CAT_LIST_CONTAINER, categoryTypeModelList, true ) );

                        }
                        // Load data without waste of time...
                        homeFragmentAdaptor.notifyDataSetChanged();
                    }
                    // $ Changes...
                    if (dialog!=null){
                        dialog.dismiss();
                    }
                    if (swipeRefreshLayout != null){
                        swipeRefreshLayout.setRefreshing( false );
                    }
                    // Set SHOP ID as Previous....
                    StaticValues.SHOP_ID_PREVIOUS = shopID;
                }
                else {
                    StaticValues.SHOP_ID_PREVIOUS = shopID;
                     showToast(context, "Failed to load Data.! Error : " + task.getException().getMessage() );
                    if (dialog!=null){
                        dialog.dismiss();
                    }
                    if (swipeRefreshLayout != null){
                        swipeRefreshLayout.setRefreshing( false );
                    }
                }
            }
        } );

        //------------------------------------------------------

    }

    public static void getCityListQuery(){
        firebaseFirestore.collection( "AREA_CODE" ).orderBy( "area_code" )
                .get().addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task <QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        String areaCode = String.valueOf( (long)documentSnapshot.get( "area_code" ) );
                        String areaName = documentSnapshot.get( "area_name" ).toString();
                        String cityName = documentSnapshot.get( "area_city" ).toString();
                        String cityCode = documentSnapshot.get( "area_city_code" ).toString();

                        areaCodeCityModelList.add( new AreaCodeCityModel( areaCode, areaName, cityName, cityCode ) );
                    }
                    if (MainActivity.selectAreaCityAdaptor != null){
                        MainActivity.selectAreaCityAdaptor.notifyDataSetChanged();
                    }

                }else{

                }
            }
        } );

    }



}



