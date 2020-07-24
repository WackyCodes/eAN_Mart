package wackycodes.ecom.eanmart.apphome.shophome;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.databasequery.DBQuery;
import wackycodes.ecom.eanmart.other.DialogsClass;
import wackycodes.ecom.eanmart.productdetails.ProductModel;
import wackycodes.ecom.eanmart.productdetails.ProductSubModel;
import wackycodes.ecom.eanmart.userprofile.cart.CartActivity;

import static wackycodes.ecom.eanmart.databasequery.DBQuery.currentUser;
import static wackycodes.ecom.eanmart.databasequery.DBQuery.shopHomeCategoryList;
import static wackycodes.ecom.eanmart.other.StaticValues.GRID_PRODUCT_LAYOUT;
import static wackycodes.ecom.eanmart.other.StaticValues.RECYCLER_PRODUCT_LAYOUT;
import static wackycodes.ecom.eanmart.other.StaticValues.SHOP_ID_CURRENT;
import static wackycodes.ecom.eanmart.other.StaticValues.VIEW_ALL_ACTIVITY;
import static wackycodes.ecom.eanmart.other.StaticValues.VIEW_RECTANGLE_LAYOUT;

public class ViewAllActivity  extends AppCompatActivity {

    private RecyclerView recyclerProductLayout;
    private GridView gridProductLayout;

    private HorizontalItemViewAdaptor horizontalItemViewAdaptor;
    private ItemActivityGridViewAdapter itemActivityGridViewAdapter;

    public static List <String> totalProductsIdViewAll;
    public static List <ProductModel> horizontalItemViewModelListViewAll;
    public static List <ProductModel> gridViewModelListViewAll;

    public static TextView badgeCartCount;
    private int layoutCode;
    private int crrShopCatIndex;
    private int layoutIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_view_all );

        Toolbar toolbar = findViewById( R.id.x_ToolBar );
        setSupportActionBar( toolbar );
        try {
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            String layoutTitle = getIntent().getStringExtra( "TITLE" );
            getSupportActionBar().setTitle( layoutTitle );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException ignored){ }

        layoutCode = getIntent().getIntExtra( "LAYOUT_CODE", -1 );
        crrShopCatIndex = getIntent().getIntExtra( "HOME_CAT_INDEX", -1 );//crrShopCatIndex
        layoutIndex = getIntent().getIntExtra( "LAYOUT_INDEX", -1 );// layout_index
        // -- Recycler View...
        recyclerProductLayout = findViewById( R.id.view_all_layout_recycler );
        // ----------- Grid View....
        gridProductLayout = findViewById( R.id.view_all_layout_grid );
        //-- condition..
        if (layoutCode == RECYCLER_PRODUCT_LAYOUT){
            gridProductLayout.setVisibility( View.GONE );
            recyclerProductLayout.setVisibility( View.VISIBLE );
            setRecyclerProductLayout();

            if (totalProductsIdViewAll.size() > horizontalItemViewModelListViewAll.size()){
                horizontalItemViewModelListViewAll.clear();
                // Load Again Data...
                for (int i = 0; i < totalProductsIdViewAll.size(); i++){
                    loadProductData( totalProductsIdViewAll.get( i ));
                }
            }

        }else
        if (layoutCode == GRID_PRODUCT_LAYOUT){
            recyclerProductLayout.setVisibility( View.GONE );
            gridProductLayout.setVisibility( View.VISIBLE );
            setGridProductLayout();
            if (totalProductsIdViewAll.size() > gridViewModelListViewAll.size()){
                gridViewModelListViewAll.clear();
                // Load Again Data...
                for (int i = 0; i < totalProductsIdViewAll.size(); i++){
                    loadProductData( totalProductsIdViewAll.get( i ));
                }
            }
        }

    }
    private void setRecyclerProductLayout(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this );
        linearLayoutManager.setOrientation( RecyclerView.VERTICAL );
        recyclerProductLayout.setLayoutManager( linearLayoutManager );
        horizontalItemViewAdaptor = new HorizontalItemViewAdaptor( crrShopCatIndex, layoutIndex, VIEW_RECTANGLE_LAYOUT, horizontalItemViewModelListViewAll );
        recyclerProductLayout.setAdapter( horizontalItemViewAdaptor );
        horizontalItemViewAdaptor.notifyDataSetChanged();
    }

    private void setGridProductLayout(){
        itemActivityGridViewAdapter = new ItemActivityGridViewAdapter( gridViewModelListViewAll );
        gridProductLayout.setAdapter( itemActivityGridViewAdapter );
        itemActivityGridViewAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_cart_header,menu);
        MenuItem cartItem = menu.findItem( R.id.menu_cart );
        // Check First whether any item in cart or not...

        // if any item has in cart...
        cartItem.setActionView( R.layout.badge_cart_layout );
//            ImageView badgeCartIcon = cartItem.getActionView().findViewById( R.id.badge_cart_icon );
        badgeCartCount = cartItem.getActionView().findViewById( R.id.badge_count_text );

        cartItem.getActionView().setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // GOTO : Goto Cart
                if (currentUser == null){
                    DialogsClass.signInUpDialog( ViewAllActivity.this, VIEW_ALL_ACTIVITY );
                }else{
                    startActivity( new Intent(ViewAllActivity.this, CartActivity.class) );
                }
            }
        } );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            return true;
        }else
        if (id == R.id.menu_cart){
            // GOTO : Goto Cart
            if (currentUser == null){
                DialogsClass.signInUpDialog( ViewAllActivity.this, VIEW_ALL_ACTIVITY );
            }else{
                startActivity( new Intent(this, CartActivity.class) );
            }
            return true;
        }
        return super.onOptionsItemSelected( item );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // To Refresh Menu...
        invalidateOptionsMenu();
    }

    private void loadProductData( final String productId){
        DBQuery.firebaseFirestore.collection( "SHOPS" ).document( SHOP_ID_CURRENT )
                .collection( "PRODUCTS" ).document( productId )
                .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    // access the banners from database...
//                    String[] pImage;
                    long p_no_of_variants = (long) task.getResult().get( "p_no_of_variants" );
                    List<ProductSubModel> productSubModelList = new ArrayList <>();
                    for (long tempI = 1; tempI <= p_no_of_variants; tempI++){
//
                        // We can use Array...
//                        String[] pImage = (String[]) task.getResult().get( "p_image_" + tempI );
                        ArrayList<String> Images = (ArrayList <String>) task.getResult().get( "p_image_" + tempI );
//                        int sz = Images.size();
//                        String[] pImage = new String[sz];
//                        for (int i = 0; i < sz; i++){
//                            pImage[i] = Images.get( i );
//                        }

                        // add Data...
                        productSubModelList.add( new ProductSubModel(
                                task.getResult().get( "p_name_"+tempI).toString(),
                                Images,
                                task.getResult().get( "p_selling_price_"+tempI).toString(),
                                task.getResult().get( "p_mrp_price_"+tempI).toString(),
                                task.getResult().get( "p_weight_"+tempI).toString(),
                                task.getResult().get( "p_stocks_"+tempI).toString(),
                                task.getResult().get( "p_offer_"+tempI).toString()
                        ) );
                    }
                    String p_id = task.getResult().get( "p_id").toString();
                    String p_main_name = task.getResult().get( "p_main_name" ).toString();
//                        String p_main_image = task.getResult().get( "p_main_image" ).toString();
                    String p_weight_type = task.getResult().get( "p_weight_type" ).toString();
                    int p_veg_non_type = Integer.valueOf( task.getResult().get( "p_veg_non_type" ).toString() );
                    Boolean p_is_cod = (Boolean) task.getResult().get( "p_is_cod" );

                    if (layoutCode == RECYCLER_PRODUCT_LAYOUT){
                        horizontalItemViewModelListViewAll.add( new ProductModel(
                                p_id,
                                p_main_name,
                                p_is_cod,
                                String.valueOf(p_no_of_variants),
                                p_weight_type,
                                p_veg_non_type,
                                productSubModelList
                        ) );
                        shopHomeCategoryList.get( crrShopCatIndex ).get( layoutIndex ).setProductModelList( horizontalItemViewModelListViewAll );
                        horizontalItemViewAdaptor.notifyDataSetChanged();
                    }else{
                        gridViewModelListViewAll.add( new ProductModel(
                                p_id,
                                p_main_name,
                                p_is_cod,
                                String.valueOf(p_no_of_variants),
                                p_weight_type,
                                p_veg_non_type,
                                productSubModelList
                        ) );
                        shopHomeCategoryList.get( crrShopCatIndex ).get( layoutIndex ).setProductModelList( gridViewModelListViewAll );
                        itemActivityGridViewAdapter.notifyDataSetChanged();
                    }
                }
                else{
                    String error = task.getException().getMessage();
//                                    showToast(error);
//                                    dialog.dismiss();
                }
            }
        } );

    }


}
