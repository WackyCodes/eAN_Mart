package wackycodes.ecom.eanmart.apphome.shophome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.bannerslider.BannerSliderModel;
import wackycodes.ecom.eanmart.databasequery.DBQuery;
import wackycodes.ecom.eanmart.databasequery.UserDataQuery;
import wackycodes.ecom.eanmart.other.CheckInternetConnection;
import wackycodes.ecom.eanmart.other.DialogsClass;
import wackycodes.ecom.eanmart.other.StaticMethods;
import wackycodes.ecom.eanmart.productdetails.ProductModel;
import wackycodes.ecom.eanmart.productdetails.ProductSubModel;
import wackycodes.ecom.eanmart.apphome.shophome.aboutshop.AboutShopActivity;
import wackycodes.ecom.eanmart.apphome.shophome.aboutshop.AboutShopModel;
import wackycodes.ecom.eanmart.userprofile.cart.CartActivity;

import static wackycodes.ecom.eanmart.databasequery.DBQuery.currentUser;
import static wackycodes.ecom.eanmart.databasequery.DBQuery.firebaseFirestore;
import static wackycodes.ecom.eanmart.databasequery.DBQuery.shopHomeCategoryList;
import static wackycodes.ecom.eanmart.other.StaticValues.SHOP_HOME_ACTIVITY;
import static wackycodes.ecom.eanmart.other.StaticValues.SHOP_ID_CURRENT;
import static wackycodes.ecom.eanmart.other.StaticValues.SHOP_ID_PREVIOUS;
import static wackycodes.ecom.eanmart.other.StaticValues.VIEW_RECTANGLE_LAYOUT;

public class ShopHomeActivity extends AppCompatActivity {
    public static AppCompatActivity shopHomeActivity;
    private String shopID;
//    public static TextView shopIDText;
    private static AboutShopModel aboutShopModel;

    public static SwipeRefreshLayout shopHomeSwipeRefreshLayout;
    public static TextView badgeCartCount;

    private RecyclerView shopHomeContainerRecycler;

    private ShopHomeFragmentAdaptor shopHomeFragmentAdaptor;

    //------ View Pager for Banner Slider...
    public static List<BannerSliderModel> bannerSliderModelList;
    //------ View Pager for Banner Slider...
    // Search Variables...
    private SearchView homeMainSearchView;
    public static List <ProductModel> searchProductList = new ArrayList <>();
    private List<String> searchShopTags = new ArrayList <>();
    private SearchAdapter searchAdaptor;
    private Boolean isSearchView = false;
    // Search Variables...
    private TextView closeText;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_show_home );
        shopHomeActivity = this;
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

        if (shopID.equals( SHOP_ID_PREVIOUS )){
            // continue...
        }else{
            // reload...
            aboutShopModel = new AboutShopModel( shopID );
            dialog.show();
            shopHomeCategoryList.clear();
        }
        loadShopData(shopID);

//        shopIDText = findViewById( R.id.shop_id );
//        shopIDText.setText( shopID );
        closeText = findViewById(R.id.shop_close_text);
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
                setShopHomeAdaptor(false);
//                shopHomeFragmentAdaptor = new ShopHomeFragmentAdaptor(0, shopHomeCategoryList.get( 0 ) );
//                shopHomeContainerRecycler.setAdapter( shopHomeFragmentAdaptor );
//                shopHomeFragmentAdaptor.notifyDataSetChanged();
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

        // SearchView...
        homeMainSearchView = findViewById( R.id.home_shop_search_view );
        homeMainSearchView.setFocusable( false );
        getShopSearchItems( );

    }

    @Override
    public void onBackPressed() {
        if (isSearchView){
            setShopHomeAdaptor(false);
        }else
            super.onBackPressed();
    }

    // Tool Bar Menu...
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_shop_home,menu);
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
        }else
            if (id == R.id.menu_shop){
                // TODO : GOTO SHOP ACTIVITY..
                // Load All Data of shop...
                startActivity( new Intent( ShopHomeActivity.this, AboutShopActivity.class) );
                return true;
            }
        return super.onOptionsItemSelected( item );
    }

    private void setShopHomeAdaptor( Boolean isSearchAdaptor ){

        if (isSearchAdaptor){
            isSearchView = true;
            // Set SearchAdaptor...
            if (searchAdaptor!=null){
                shopHomeContainerRecycler.setAdapter( searchAdaptor );
                searchAdaptor.notifyDataSetChanged();
            }else{
                searchAdaptor = new SearchAdapter( searchProductList );
                shopHomeContainerRecycler.setAdapter( searchAdaptor );
                searchAdaptor.notifyDataSetChanged();
            }

        }else{
            // Set Shop Home Product...
            isSearchView = false;
            shopHomeFragmentAdaptor = new ShopHomeFragmentAdaptor(0, shopHomeCategoryList.get( 0 ) );
            shopHomeContainerRecycler.setAdapter( shopHomeFragmentAdaptor );
            shopHomeFragmentAdaptor.notifyDataSetChanged();
        }
    }


    private void getShopSearchItems(){
        // Search Methods...
        homeMainSearchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                dialog.show();
                searchProductList.clear();
                searchShopTags.clear();
                final String [] tags = query.toLowerCase().split( " " );
                for ( final String tag : tags ){
                    firebaseFirestore
                            .collection( "SHOPS" )
                            .document( SHOP_ID_CURRENT )
                            .collection( "PRODUCTS" )
                            .whereArrayContainsAny( "tags", Arrays.asList( tags ) )
//                            .whereArrayContains( "tags", tag.trim() )
                            .get().addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task <QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                                    // access the banners from database...
//                                    String[] pImage;
                                    long p_no_of_variants = (long) documentSnapshot.get( "p_no_of_variants" );
                                    List<ProductSubModel> productSubModelList = new ArrayList <>();
                                    for (long tempI = 1; tempI <= p_no_of_variants; tempI++){
//                                        int p_no_of_images = Integer.parseInt( String.valueOf(  (long) documentSnapshot.get( "p_no_of_images" ) ) );
//                                        pImage = new String[p_no_of_images];
//                                        for (int tempJ = 0; tempJ < p_no_of_images; tempJ++){
//                                            pImage[tempJ] = documentSnapshot.get( "p_image_"+ tempI +"_"+tempJ ).toString();
//                                        }
                                        // We can use Array...
//                                        String[] pImage = (String[]) documentSnapshot.get( "p_image_" + tempI );
                                        ArrayList<String> Images = (ArrayList <String>) documentSnapshot.get( "p_image_" + tempI );
                                        int sz = Images.size();
                                        String[] pImage = new String[sz];
                                        for (int i = 0; i < sz; i++){
                                            pImage[i] = Images.get( i );
                                        }
                                        // add Data...
                                        productSubModelList.add( new ProductSubModel(
                                                documentSnapshot.get( "p_name_"+tempI).toString(),
                                                Images,
                                                documentSnapshot.get( "p_selling_price_"+tempI).toString(),
                                                documentSnapshot.get( "p_mrp_price_"+tempI).toString(),
                                                documentSnapshot.get( "p_weight_"+tempI).toString(),
                                                documentSnapshot.get( "p_stocks_"+tempI).toString(),
                                                documentSnapshot.get( "p_offer_"+tempI).toString()
                                        ) );
                                    }
                                    String p_id = documentSnapshot.get( "p_id").toString();

                                    String p_main_name = documentSnapshot.get( "p_main_name" ).toString();
//                        String p_main_image = task.getResult().get( "p_main_image" ).toString();
                                    String p_weight_type = documentSnapshot.get( "p_weight_type" ).toString();
                                    int p_veg_non_type = Integer.valueOf( documentSnapshot.get( "p_veg_non_type" ).toString() );
                                    Boolean p_is_cod = (Boolean) documentSnapshot.get( "p_is_cod" );
                                    ProductModel model = new ProductModel(
                                            p_id,
                                            p_main_name,
                                            p_is_cod,
                                            String.valueOf(p_no_of_variants),
                                            p_weight_type,
                                            p_veg_non_type,
                                            productSubModelList
                                    );
                                    if ( !searchShopTags.contains( model.getpProductID() )){
                                        searchProductList.add( model );
                                        searchShopTags.add( model.getpProductID() );
                                    }

                                }
                                if (searchProductList.size()>0){
                                    setShopHomeAdaptor( true );
                                }
                                if (tag.equals(tags[tags.length - 1])){
                                    if (searchShopTags.isEmpty()){
                                        DialogsClass.alertDialog( ShopHomeActivity.this, null, "No Shop found.!" ).show();
                                        setShopHomeAdaptor( false );
                                    }else{
                                        searchAdaptor.getFilter().filter( query );
                                    }
                                    dialog.dismiss();
                                }
                                dialog.dismiss();
//                                closeKeyboard();
                            }else{
                                // error...
                                dialog.dismiss();
                                StaticMethods.showToast( ShopHomeActivity.this, "Failed ! Product Not found.!" );
                                setShopHomeAdaptor( false );
                            }
                        }
                    } );
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        } );
        homeMainSearchView.setOnCloseListener( new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
//                setFrameVisibility(true);
                return false;
            }
        } );

    }
    private class SearchAdapter extends HorizontalItemViewAdaptor implements Filterable {

        public SearchAdapter(List <ProductModel> shopItemModelList) {
            super( -1, -1, VIEW_RECTANGLE_LAYOUT,  shopItemModelList );
        }
        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    return null;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    notifyDataSetChanged();
                }
            };
        }
    }

    // Load Shop Data And Check whether isOpen Shop or Not...!
    private void loadShopData(@NonNull String shopID){

        DBQuery.firebaseFirestore.collection( "SHOPS" ).document( shopID )
                .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if (task.isSuccessful()){

                    DocumentSnapshot documentSnapshot = task.getResult();
                    Boolean isAvailableService = documentSnapshot.getBoolean( "available_service" );

                    Boolean isOpen = documentSnapshot.getBoolean( "is_open" );
                    String shopName = documentSnapshot.get( "shop_name" ).toString();
                    String shopAddress = documentSnapshot.get( "shop_address" ).toString();
                    getSupportActionBar().setTitle( shopName );
                    getSupportActionBar().setSubtitle( shopAddress );

                    if (!isAvailableService){
                        String closeMsg = "Service not Available! Shop is temporary closed! ";

                        Dialog closeDialog = DialogsClass.getMessageDialog( ShopHomeActivity.this, "Coming Soon!", closeMsg );
                        closeDialog.findViewById( R.id.dialog_ok_btn ).setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        } );
                        closeDialog.show();
                    }

                    if (isOpen){
                        aboutShopModel.setOpen( true );
                        aboutShopModel.setShopName( shopName );
                        aboutShopModel.setShopAddress( shopAddress );
                        closeText.setVisibility( View.GONE );
                    }else{
                        aboutShopModel.setOpen( false );
                        String closeMsg = documentSnapshot.get( "shop_close_msg" ).toString();
                        closeText.setVisibility( View.VISIBLE );
                    }

                    // Dialog if service is not Available...


                    // TODO : Shop Related Data...

                }else{

                }
            }
        } );

    }



}

