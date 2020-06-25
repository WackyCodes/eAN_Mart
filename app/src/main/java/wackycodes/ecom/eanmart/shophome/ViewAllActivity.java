package wackycodes.ecom.eanmart.shophome;

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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import wackycodes.ecom.eanmart.MainActivity;
import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.other.DialogsClass;

import static wackycodes.ecom.eanmart.databasequery.DBQuery.currentUser;
import static wackycodes.ecom.eanmart.other.StaticValues.GRID_PRODUCT_LAYOUT;
import static wackycodes.ecom.eanmart.other.StaticValues.RECYCLER_PRODUCT_LAYOUT;
import static wackycodes.ecom.eanmart.other.StaticValues.VIEW_ALL_ACTIVITY;
import static wackycodes.ecom.eanmart.shophome.HorizontalItemViewModel.hrViewType;

public class ViewAllActivity  extends AppCompatActivity {

    private RecyclerView recyclerProductLayout;
    private GridView gridProductLayout;

    private HorizontalItemViewAdaptor horizontalItemViewAdaptor;
    private ItemActivityGridViewAdapter itemActivityGridViewAdapter;

    public static List <String> totalProductsIdViewAll;
    public static List <HorizontalItemViewModel> horizontalItemViewModelListViewAll;
    public static List <HorizontalItemViewModel> gridViewModelListViewAll;

    public static TextView badgeCartCount;
    private int layoutCode;

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
                    loadProductDataAgain( totalProductsIdViewAll.get( i ));
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
                    loadProductDataAgain( totalProductsIdViewAll.get( i ));
                }
            }
        }

    }
    private void setRecyclerProductLayout(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this );
        linearLayoutManager.setOrientation( RecyclerView.VERTICAL );
        recyclerProductLayout.setLayoutManager( linearLayoutManager );
        horizontalItemViewAdaptor = new HorizontalItemViewAdaptor( horizontalItemViewModelListViewAll );
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
      /**  if (DBquery.myCartCheckList.size() > 0){
//            badgeCartCount.setVisibility( View.VISIBLE );
//            badgeCartCount.setText( String.valueOf( DBquery.myCartCheckList.size() ) );
//        } */
        cartItem.getActionView().setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // GOTO : Goto Cart
                if (currentUser == null){
                    DialogsClass.signInUpDialog( ViewAllActivity.this, VIEW_ALL_ACTIVITY );
                }else{
                    startActivity( new Intent(ViewAllActivity.this, MainActivity.class) );
//                    MainActivity.isFragmentIsMyCart = true;
                }
            }
        } );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            hrViewType = 0;
            finish();
            return true;
        }else
        if (id == R.id.menu_cart){
            // GOTO : Goto Cart
            if (currentUser == null){
                DialogsClass.signInUpDialog( ViewAllActivity.this, VIEW_ALL_ACTIVITY );
            }else{
                startActivity( new Intent(this, MainActivity.class) );
//                MainActivity.isFragmentIsMyCart = true;
            }
            return true;
        }
        return super.onOptionsItemSelected( item );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hrViewType = 0;
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // To Refresh Menu...
        invalidateOptionsMenu();
    }

    private void loadProductDataAgain( final String productId ){
        FirebaseFirestore.getInstance().collection( "PRODUCTS" ).document( productId )
                .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    // access the banners from database...
                    if (layoutCode == RECYCLER_PRODUCT_LAYOUT){
                        horizontalItemViewModelListViewAll.add(new HorizontalItemViewModel( 1, productId
                                , task.getResult().get( "product_image_1").toString()
                                , task.getResult().get( "product_full_name" ).toString()
                                , task.getResult().get( "product_price" ).toString()
                                , task.getResult().get( "product_cut_price" ).toString()
                                , (Boolean) task.getResult().get( "product_cod" )
                                , (long) task.getResult().get( "product_stocks" ) ) );

                        horizontalItemViewAdaptor.notifyDataSetChanged();
//                        commonCatList.get( catIndex ).get( listIndex ).setHrAndGridProductsDetailsList( horizontalItemViewModelListViewAll );
//                        horizontalItemViewAdaptor.notifyDataSetChanged();
                    }else
                    if(layoutCode == GRID_PRODUCT_LAYOUT){
                        gridViewModelListViewAll.add( new HorizontalItemViewModel( 0, productId
                                , task.getResult().get( "product_image_1").toString()
                                , task.getResult().get( "product_full_name" ).toString()
                                , task.getResult().get( "product_price" ).toString()
                                , task.getResult().get( "product_cut_price" ).toString()
                                , (Boolean) task.getResult().get( "product_cod" )
                                , (long) task.getResult().get( "product_stocks" ) ) );

                        itemActivityGridViewAdapter.notifyDataSetChanged();
//                        commonCatList.get( catIndex ).get( listIndex ).setHrAndGridProductsDetailsList( gridViewModelListViewAll );
//                        gridViewAllAdaptor.notifyDataSetChanged();
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
