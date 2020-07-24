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
import java.util.List;

import wackycodes.ecom.eanmart.MainActivity;
import wackycodes.ecom.eanmart.apphome.mainhome.CategoryTypeModel;
import wackycodes.ecom.eanmart.apphome.mainhome.HomeFragment;
import wackycodes.ecom.eanmart.apphome.mainhome.MainHomeFragmentModel;
import wackycodes.ecom.eanmart.bannerslider.BannerSliderModel;
import wackycodes.ecom.eanmart.apphome.category.ShopsViewFragment;
import wackycodes.ecom.eanmart.cityareacode.AreaCodeCityModel;
import wackycodes.ecom.eanmart.other.StaticValues;
import wackycodes.ecom.eanmart.productdetails.ProductModel;
import wackycodes.ecom.eanmart.apphome.shophome.ShopHomeFragmentAdaptor;
import wackycodes.ecom.eanmart.apphome.shophome.ShopHomeFragmentModel;

import static wackycodes.ecom.eanmart.other.StaticMethods.showToast;
import static wackycodes.ecom.eanmart.other.StaticValues.SHOP_HOME_BANNER_SLIDER_CONTAINER;
import static wackycodes.ecom.eanmart.other.StaticValues.CURRENT_CITY_CODE;
import static wackycodes.ecom.eanmart.other.StaticValues.GRID_PRODUCTS_LAYOUT_CONTAINER;
import static wackycodes.ecom.eanmart.other.StaticValues.HORIZONTAL_PRODUCTS_LAYOUT_CONTAINER;
import static wackycodes.ecom.eanmart.other.StaticValues.SHOP_HOME_CAT_LIST_CONTAINER;
import static wackycodes.ecom.eanmart.other.StaticValues.SHOP_HOME_STRIP_AD_CONTAINER;
import static wackycodes.ecom.eanmart.other.StaticValues.TYPE_HOME_CATEGORY_LAYOUT;
import static wackycodes.ecom.eanmart.other.StaticValues.TYPE_HOME_SHOP_BANNER_SLIDER;
import static wackycodes.ecom.eanmart.other.StaticValues.TYPE_HOME_SHOP_ITEMS_CONTAINER;
import static wackycodes.ecom.eanmart.other.StaticValues.TYPE_HOME_SHOP_STRIP_AD;
import static wackycodes.ecom.eanmart.other.StaticValues.TYPE_LIST_MAIN_HOME_CATEGORY;
import static wackycodes.ecom.eanmart.other.StaticValues.TYPE_LIST_SHOPS_VIEW_NAME;
import static wackycodes.ecom.eanmart.apphome.shophome.ShopHomeActivity.bannerSliderModelList;

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

    public static CollectionReference getCollectionRef(String collectionName){
        return firebaseFirestore.collection( "HOME_PAGE" ).document( CURRENT_CITY_CODE ).collection( collectionName );
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
                        int viewType = Integer.parseInt( String.valueOf( (long)documentSnapshot.get( "type" ) ) );
                        if (viewType == TYPE_HOME_SHOP_BANNER_SLIDER) {
                            // TODO: Banner Slider...
                            Boolean is_visible = documentSnapshot.getBoolean( "is_visible" );
                            if (is_visible){
                                // Load
                                bannerSliderModelList = new ArrayList <>();
//                                String layout_id = documentSnapshot.get( "layout_id" ).toString();
                                long no_of_banners = (long)documentSnapshot.get( "no_of_banners" );

                                for (long ln = 1; ln <= no_of_banners; ln++){
                                    String banner_ = documentSnapshot.get( "banner_"+ln ).toString();
                                    String banner_click_id_ = documentSnapshot.get( "banner_click_id_"+ln ).toString();
                                    long banner_click_type_ = (long)documentSnapshot.get( "banner_click_type_"+ln );

                                    bannerSliderModelList.add( new BannerSliderModel( Integer.parseInt( String.valueOf( banner_click_type_ ) ) ,
                                            banner_click_id_, banner_, "new" ) );
                                }

                                homePageCategoryList.add( new MainHomeFragmentModel( TYPE_HOME_SHOP_BANNER_SLIDER, bannerSliderModelList, -1 ) );

                            }else{
                                // None
                            }
                        }else
                        if (viewType == TYPE_HOME_CATEGORY_LAYOUT) {
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
                        if (viewType == TYPE_HOME_SHOP_STRIP_AD) {
                            // Strip Ad...
                            homePageCategoryList.add( new MainHomeFragmentModel(
                                    TYPE_HOME_SHOP_STRIP_AD,
                                    documentSnapshot.get( "banner_click_id" ).toString(),
                                    documentSnapshot.get( "banner_image" ).toString(),
                                    documentSnapshot.get( "extra_text" ).toString(),
                                    Integer.parseInt( String.valueOf( (long)documentSnapshot.get( "banner_click_type" )  ) )
                            ) );
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
                        int viewType = Integer.parseInt( String.valueOf( (long)documentSnapshot.get( "type" ) ) );
                        if (viewType == TYPE_HOME_SHOP_BANNER_SLIDER) {
                            // TODO: Banner Slider...
                            Boolean is_visible = documentSnapshot.getBoolean( "is_visible" );
                            if (is_visible){
                                // Load
                                String layout_id = documentSnapshot.get( "layout_id" ).toString();
                                long no_of_banners = (long)documentSnapshot.get( "no_of_banners" );

                                for (long ln = 1; ln <= no_of_banners; ln++){
                                    String banner_ = documentSnapshot.get( "banner_"+ln ).toString();
                                    String banner_click_id_ = documentSnapshot.get( "banner_click_id_"+ln ).toString();
                                    long banner_click_type_ = (long)documentSnapshot.get( "banner_click_type_"+ln );

                                    bannerSliderModelList.add( new BannerSliderModel( Integer.parseInt( String.valueOf( banner_click_type_ ) ) ,
                                            banner_click_id_, banner_, "new" ) );
                                }

                                homePageCategoryList.add( new MainHomeFragmentModel( TYPE_HOME_SHOP_BANNER_SLIDER, bannerSliderModelList, -1 ) );

                            }else{
                                // None
                            }

                        }else
                        if (viewType == TYPE_HOME_SHOP_ITEMS_CONTAINER) {
                            // Grid List...
                            // One More Query Needed here...
                            int shopsListIndex = Integer.parseInt( String.valueOf( (long)documentSnapshot.get( "index" ) ) );
                            getShopsItemQuery(cityName, catId, shopsListIndex );

                        }else
                        if (viewType == TYPE_HOME_SHOP_STRIP_AD) {
                            // Strip Ad...
                            shopsViewFragmentList.add(  new MainHomeFragmentModel(
                                    TYPE_HOME_SHOP_STRIP_AD,
                                    documentSnapshot.get( "banner_click_id" ).toString(),
                                    documentSnapshot.get( "banner_image" ).toString(),
                                    documentSnapshot.get( "extra_text" ).toString(),
                                    Integer.parseInt( String.valueOf( (long)documentSnapshot.get( "banner_click_type" )  ) )
                            ) );
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

    public static void getShopsItemQuery( String cityName, final String categoryID, final int index){
        // One More Query Needed here...
        firebaseFirestore.collection( "HOME_PAGE" ).document( cityName.toUpperCase() )
                .collection( "SHOPS" )
                .whereArrayContains( "shop_categories", categoryID )
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
                                documentSnapshot1.get( "shop_logo" ).toString()
                        ) );
                    }

                    shopsViewFragmentList.add( index, new MainHomeFragmentModel( TYPE_HOME_SHOP_ITEMS_CONTAINER, categoryTypeModelList ) );
                    if(ShopsViewFragment.shopViewFragmentAdaptor != null){
                        ShopsViewFragment.shopViewFragmentAdaptor.notifyDataSetChanged();
                    }

                }else{

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
                        int viewType = Integer.parseInt( String.valueOf( (long)documentSnapshot.get( "type" ) ) );
//                            showToast( context, "City : " + userCityName );
                        if ( viewType == SHOP_HOME_BANNER_SLIDER_CONTAINER) {
                            /** add banners slider */
//                            String layout_id = documentSnapshot.get( "layout_id" ).toString();
                            bannerSliderModelList = new ArrayList <>();
                            long no_of_banners = (long) documentSnapshot.get( "no_of_banners" );
                            for (long i = 1; i < no_of_banners + 1; i++) {
                                // access the banners from database...
                                bannerSliderModelList.add( new BannerSliderModel(
                                        Integer.parseInt( String.valueOf( (long)documentSnapshot.get( "banner_click_type_"+i ) )),
                                        documentSnapshot.get( "banner_click_id_" + i ).toString(),
                                        documentSnapshot.get( "banner_" + i ).toString(),
                                        "Extra_Text_bgColor" ));
                            }
                            // add the banners list in the home recycler list...
                            if (bannerSliderModelList.size() >= 2){
                                shopHomeCategoryList.get( index ).add( new ShopHomeFragmentModel( SHOP_HOME_BANNER_SLIDER_CONTAINER, bannerSliderModelList ) );
                            }

                        } else
                        if ( viewType == SHOP_HOME_STRIP_AD_CONTAINER) {
                            /**  for strip and banner ad */
//                            String layout_id = documentSnapshot.get( "layout_id" ).toString();

                            shopHomeCategoryList.get( index ).add( new ShopHomeFragmentModel( SHOP_HOME_STRIP_AD_CONTAINER,
                                    Integer.parseInt( String.valueOf( (long) documentSnapshot.get( "banner_click_type" ) ) ),
                                    documentSnapshot.get( "banner_click_id" ).toString(),
                                    documentSnapshot.get( "banner_image" ).toString(),
                                    documentSnapshot.get( "extra_text" ).toString() )
                            );
                        } else
                        if ( viewType == HORIZONTAL_PRODUCTS_LAYOUT_CONTAINER) {
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
                                shopHomeCategoryList.get( index ).add( new ShopHomeFragmentModel( HORIZONTAL_PRODUCTS_LAYOUT_CONTAINER, hrAndGridProductIdList,
                                        productModelList, documentSnapshot.get( "layout_title" ).toString() ) );
                            }

                        } else
                        if ( viewType == GRID_PRODUCTS_LAYOUT_CONTAINER) {
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
                                shopHomeCategoryList.get( index ).add( new ShopHomeFragmentModel( GRID_PRODUCTS_LAYOUT_CONTAINER, hrAndGridProductIdList,
                                        productModelList, documentSnapshot.get( "layout_title" ).toString() ) );
                            }
//                            // TODO : To Admin
//                            commonCatList.get( index ).add( new CommonCatModel( GRID_PRODUCTS_LAYOUT_CONTAINER, layout_id
//                                    , documentSnapshot.get( "layout_title" ).toString()
//                                    , hrAndGridProductIdList, hrAndGridProductList ) );


                        } else
                        if ( viewType == SHOP_HOME_CAT_LIST_CONTAINER) {
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
        areaCodeCityModelList.clear();
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



