package wackycodes.ecom.eanmart.apphome.shophome.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
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
import wackycodes.ecom.eanmart.other.DialogsClass;
import wackycodes.ecom.eanmart.other.StaticMethods;
import wackycodes.ecom.eanmart.productdetails.ProductModel;
import wackycodes.ecom.eanmart.productdetails.ProductSubModel;
import wackycodes.ecom.eanmart.apphome.shophome.HorizontalItemViewAdaptor;

import static wackycodes.ecom.eanmart.databasequery.DBQuery.firebaseFirestore;
import static wackycodes.ecom.eanmart.other.StaticValues.SHOP_ID_CURRENT;
import static wackycodes.ecom.eanmart.other.StaticValues.VIEW_RECTANGLE_LAYOUT;

public class ProductSearchActivity extends AppCompatActivity {

    // Search Variables...
    private SearchView homeMainSearchView;
    public static List <ProductModel> searchProductList = new ArrayList <>();
    private List <String> searchShopTags = new ArrayList <>();
    private SearchAdapter searchAdaptor;
    // Search Variables...

    private RecyclerView searchRecycler;
    private TextView noProductFound;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_product_search );

        Toolbar toolbar = findViewById( R.id.appToolbar );
        setSupportActionBar( toolbar );
        try{
            getSupportActionBar().setDisplayShowTitleEnabled( false );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException e){
        }

        dialog = DialogsClass.getDialog( this );
        searchRecycler = findViewById( R.id.home_search_recycler_view );
        noProductFound = findViewById( R.id.no_product_text );

        LinearLayoutManager homeLinearLayoutManager = new LinearLayoutManager( this );
        homeLinearLayoutManager.setOrientation( RecyclerView.VERTICAL );
        searchRecycler.setLayoutManager( homeLinearLayoutManager );
        searchAdaptor = new SearchAdapter( searchProductList );

        // SearchView...
        homeMainSearchView = findViewById( R.id.home_shop_search_view );
        getShopSearchItems( );

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (searchProductList.size()==0){
            noProductFound.setVisibility( View.VISIBLE );
            searchRecycler.setVisibility( View.GONE );
            noProductFound.setText( "Please search any product!" );
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if ((item.getItemId() == android.R.id.home)){
            finish();
            return true;
        }
        return super.onOptionsItemSelected( item );
    }

    private void setLayoutVisibility(boolean isFound){
        if (isFound){
            noProductFound.setVisibility( View.GONE );
            searchRecycler.setVisibility( View.VISIBLE );
            searchRecycler.setAdapter( searchAdaptor );
            searchAdaptor.notifyDataSetChanged();
        }else{
            noProductFound.setVisibility( View.VISIBLE );
            searchRecycler.setVisibility( View.GONE );
            noProductFound.setText( "Sorry, Product not found!" );
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
                                    setLayoutVisibility(true);
                                }
                                if (tag.equals(tags[tags.length - 1])){
                                    if (searchShopTags.isEmpty()){
//                                        DialogsClass.alertDialog( ProductSearchActivity.this, null, "No Shop found.!" ).show();
                                        setLayoutVisibility(false);
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
                                StaticMethods.showToast( ProductSearchActivity.this, "Failed ! Product Not found.!" );
                                setLayoutVisibility(false);
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


}
