package wackycodes.ecom.eanmart.productdetails;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.databasequery.DBQuery;
import wackycodes.ecom.eanmart.databasequery.UserDataQuery;
import wackycodes.ecom.eanmart.other.CheckInternetConnection;
import wackycodes.ecom.eanmart.other.DialogsClass;
import wackycodes.ecom.eanmart.other.StaticMethods;
import wackycodes.ecom.eanmart.userprofile.cart.CartActivity;
import wackycodes.ecom.eanmart.userprofile.cart.CartOrderSubItemModel;

import static wackycodes.ecom.eanmart.databasequery.DBQuery.currentUser;
import static wackycodes.ecom.eanmart.databasequery.DBQuery.shopHomeCategoryList;
import static wackycodes.ecom.eanmart.databasequery.UserDataQuery.temCartItemModelList;
import static wackycodes.ecom.eanmart.other.StaticValues.CART_TYPE_ITEMS;
import static wackycodes.ecom.eanmart.other.StaticValues.CART_TYPE_TOTAL_PRICE;
import static wackycodes.ecom.eanmart.other.StaticValues.PRODUCT_DETAILS_ACTIVITY;
import static wackycodes.ecom.eanmart.other.StaticValues.SHOP_ID_CURRENT;
import static wackycodes.ecom.eanmart.other.StaticValues.SHOP_TYPE_NON_VEG;
import static wackycodes.ecom.eanmart.other.StaticValues.SHOP_TYPE_NO_SHOW;
import static wackycodes.ecom.eanmart.other.StaticValues.SHOP_TYPE_VEG;
import static wackycodes.ecom.eanmart.apphome.shophome.ShopHomeActivity.searchProductList;

public class ProductDetails extends AppCompatActivity {
    public static AppCompatActivity productDetails;

    private ImageView pVegNonTypeImage; // product_veg_non_type_image
    private LinearLayout weightSpinnerLayout;// weight_spinner_layout
    private Spinner weightSpinner; //weight_spinner

    // Cart...
    private LinearLayout addToCartLayout;// add_to_cart_layout
    private LinearLayout addMoreCartLayout;// add_more_cart_layout
    private LinearLayout checkOutCartLayout;// check_out_layout
    private ImageButton removeOneCartBtn; // remove_item_from_cart_imgBtn
    private ImageButton addOneCartBtn; // add_item_to_cart_imgBtn
    private TextView cartQtyText; // cart_item_text

    // --- Product Details Image Layout...
    private ViewPager productImagesViewPager;
    private TabLayout productImagesIndicator;
    private ConstraintLayout productDescriptionLayout;
    private TextView productName;
    private TextView productPrice;
    private TextView productCutPrice;
    private TextView productCODText; // product_item_cod_text

    // create a list for testing...
    private List<String> productImageList = new ArrayList <>();
    private ProductDetailsImagesAdapter productDetailsImagesAdapter;

    private List<String> productVariantList = new ArrayList <>();

    // --- Product Details Image Layout...
    private TextView productDetailsText;

    private ViewPager productDescriptionViewPager;
    private TabLayout productDescriptionIndicator;

    public static String productID;
    public static TextView badgeCartCount;

    // Dialogs...
    private Dialog dialog;

    // Product Specification ...
    private List <ProductDetailsSpecificationModel> productDetailsSpecificationModelList = new ArrayList <>();
    private String productDescription;

    private int crrShopCatIndex;
    private int layoutIndex;
    private int productIndex;
    public static boolean ALREADY_ADDED_IN_CART = false;
    private int cartThisQty = 0;
    private int cartThisIndex = 0;
    private String cartShopID;

    private int currentVariant = 0;

    private ProductModel pProductModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_product_details );
        productDetails = this;

        Toolbar toolbar = findViewById( R.id.x_ToolBar );
        setSupportActionBar( toolbar );
        // TODO : get product ID through Intent ...
        productID = getIntent().getStringExtra( "PRODUCT_ID" );
        crrShopCatIndex = getIntent().getIntExtra( "HOME_CAT_INDEX", -1 );
        layoutIndex = getIntent().getIntExtra( "LAYOUT_INDEX", -1 );
        productIndex = getIntent().getIntExtra( "PRODUCT_INDEX", -1 );

        if (crrShopCatIndex != -1 && layoutIndex != -1){
            // This is for layout product click...
            pProductModel = shopHomeCategoryList.get( crrShopCatIndex ).get( layoutIndex ).getProductModelList().get( productIndex );
        }else{
            // This is for search activity...
            if (searchProductList.size() > 0)
                pProductModel = searchProductList.get( productIndex );
            else{
                showToast( "Product Not found!" );
                finish();
            }
        }

        dialog = DialogsClass.getDialog( ProductDetails.this );
        dialog.show();
        // ---- Progress Dialog...

        // Set Title on Action Menu
        try{
            // To test We assign a default PRODUCT_ID ...
            if (productID.isEmpty()){
//                productID = "k2SGQbneH477j6X18l6a";
                dialog.dismiss();
                Toast.makeText( this, "Product Not found.!", Toast.LENGTH_SHORT ).show();
                finish();
            }
            getSupportActionBar().setDisplayShowTitleEnabled( false );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException e){
        }

        // --- Product Details Image Layout...
        productName = findViewById( R.id.product_item_name );
        productPrice = findViewById( R.id.product_item_price );
        productCutPrice = findViewById( R.id.product_item_cut_price );
        productCODText = findViewById( R.id.product_item_cod_text );

        // --- Product Details Image Layout...
        productDescriptionLayout = findViewById( R.id.product_details_description_ConstLayout );
        productDetailsText = findViewById( R.id.product_details_text );

        pVegNonTypeImage = findViewById( R.id.product_veg_non_type_image );
        weightSpinnerLayout = findViewById( R.id.weight_spinner_layout );
        weightSpinner = findViewById( R.id.weight_spinner );
        // Cart...
        addToCartLayout = findViewById( R.id.add_to_cart_layout );
        addMoreCartLayout = findViewById( R.id.add_more_cart_layout );
        checkOutCartLayout = findViewById( R.id.check_out_layout );
        removeOneCartBtn = findViewById( R.id.remove_item_from_cart_imgBtn );
        addOneCartBtn = findViewById( R.id.add_item_to_cart_imgBtn );
        cartQtyText = findViewById( R.id.cart_item_text );
        //----------- Product Images ---
        productImagesViewPager = findViewById( R.id.product_images_viewpager );
        productImagesIndicator = findViewById( R.id.product_images_viewpager_indicator );

        // ---------- Product Description code----
        productDescriptionViewPager = findViewById( R.id.product_detail_viewpager );
        productDescriptionIndicator = findViewById( R.id.product_details_indicator );
        // Default Tab Layout Invisible
        productDescriptionLayout.setVisibility( View.GONE );

        // set adapter with viewpager...
        productDetailsImagesAdapter = new ProductDetailsImagesAdapter( productImageList );
        productImagesViewPager.setAdapter( productDetailsImagesAdapter );

        // connect TabLayout with viewPager...
        productImagesIndicator.setupWithViewPager( productImagesViewPager, true );
        //----------- Product Images ---
        productDescriptionViewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener( productDescriptionIndicator ) );
        productDescriptionIndicator.addOnTabSelectedListener( new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productDescriptionViewPager.setCurrentItem( tab.getPosition() );
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        } );

        // Retrieve details from database...----------------
        getProductDetails();
        // SetData...
        setProductData( 0 );
        // set Product VegNon Veg...
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setVegNonData();
        }
        if (pProductModel.getpIsCOD()){
            productCODText.setVisibility( View.VISIBLE );
            productCODText.setText( "Cash On Delivery Available" );
        }else{
            productCODText.setVisibility( View.GONE );
        }

        // Cart Visibility with qty...
        if (currentUser!=null && temCartItemModelList.size()>0){
            setCartUpdate();
        }
        else{
            ALREADY_ADDED_IN_CART = false;
            setCartLayoutVisibility(true, null);
        }

        // Cart Action...
        cartAction();
        // Add Weight Data in List...
        if (pProductModel.getProductSubModelList().get( currentVariant ).getpWeight()!=null){
            for (int pWIndex = 0; pWIndex < pProductModel.getProductSubModelList().size(); pWIndex++ ) {
                productVariantList.add( pProductModel.getProductSubModelList().get( pWIndex ).getpWeight() );
            }
            weightSpinnerLayout.setVisibility( View.VISIBLE );
        }else{
            weightSpinnerLayout.setVisibility( View.GONE );
        }
        //  Set Weight Spinner...
        if(productVariantList.size() > 0){
           setWeightSpinner();
       }

    }
    // onCreate method End.. ----------------------------------
    @Override
    protected void onResume() {
        super.onResume();
        // To Refresh Menu...
        invalidateOptionsMenu();
        // Cart Visibility with qty...
        if (currentUser!=null && temCartItemModelList.size()>0){
            setCartUpdate();
        }
        else{
            ALREADY_ADDED_IN_CART = false;
            setCartLayoutVisibility(true, null);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // check offline wishList Size.. and run DB query to update wishList if size is 0
        if (currentUser != null){

            // for cart list...
            if (UserDataQuery.cartItemModelList.size() == 0){
                UserDataQuery.loadUserDataQuery(ProductDetails.this,  dialog);
            }else {
                for (int i = 0; i < UserDataQuery.cartItemModelList.size(); i++) {
                    if( UserDataQuery.cartItemModelList.get( i ).getProductID().equals( productID )){
                        ALREADY_ADDED_IN_CART = true;
                        break;
                    }else{
                        ALREADY_ADDED_IN_CART = false;
                    }
                }
                dialog.dismiss();
            }

        }else {
            dialog.dismiss();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_cart_header,menu);
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
                    DialogsClass.signInUpDialog( ProductDetails.this, PRODUCT_DETAILS_ACTIVITY );
                }else{
                    startActivity( new Intent(ProductDetails.this, CartActivity.class) );
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
                DialogsClass.signInUpDialog( ProductDetails.this, PRODUCT_DETAILS_ACTIVITY );
            }else{
                startActivity( new Intent(ProductDetails.this, CartActivity.class) );
            }
            return true;
        }

        return super.onOptionsItemSelected( item );
    }

    private void showToast(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private boolean isInternetConnected(){
        return CheckInternetConnection.isInternetConnected( this );
    }

    private void getProductDetails(){
        // TODO: Retrieve details from database...----------------
        if (isInternetConnected()){
            DBQuery.firebaseFirestore.collection( "SHOPS" ).document( SHOP_ID_CURRENT )
                    .collection( "PRODUCTS" ).document( productID )
                    .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        /** // --- add Product Images...
                         for (long x = 1; x < (long)documentSnapshot.get( "product_image_num" )+1; x++){
                         productImageList.add( documentSnapshot.get( "product_image_"+x ).toString() );
                         }
                         // set adapter with viewpager...
                         ProductDetailsImagesAdapter productDetailsImagesAdapter = new ProductDetailsImagesAdapter( productImageList );
                         productImagesViewPager.setAdapter( productDetailsImagesAdapter );
                         // Set other Details of  Product Details Image Layout.
                         // Product Name...
                         productName.setText( documentSnapshot.get( "product_full_name" ).toString() );
                         // Product Price...
                         productPrice.setText( "Rs. " + documentSnapshot.get( "product_price" ).toString() +"/-" );
                         // Product Cut Price...
                         productCutPrice.setText( "Rs. " + documentSnapshot.get( "product_cut_price" ).toString() +"/-" );

                         if ((boolean)documentSnapshot.get( "product_cod" )){
                         productCODText.setVisibility( View.VISIBLE );
                         productCODSubText.setVisibility( View.VISIBLE );
                         }else{
                         productCODText.setVisibility( View.INVISIBLE );
                         productCODSubText.setVisibility( View.INVISIBLE );
                         }
                         if ((long)documentSnapshot.get( "product_stocks" ) > 0){
                         productStock.setText( "In Stock" );
                         }else{
                         productStock.setText( "Out of Stock" );
                         }
                         */
                        // Set other Details of  Product Details Image Layout.
                        if ((boolean)documentSnapshot.get( "use_tab_layout" )){
                            // use tab layout...
                            productDescriptionLayout.setVisibility( View.VISIBLE );
                            // TODO : set Description data..
                            productDescription = documentSnapshot.get( "product_description" ).toString() ;
                            // TODO : set Specification data...
                            for (long x = 1; x < (long) documentSnapshot.get( "pro_sp_head_num" )+1; x++){
                                productDetailsSpecificationModelList.add( new ProductDetailsSpecificationModel( 0,
                                        documentSnapshot.get( "pro_sp_head_" + x ).toString() ) );
                                for (long i = 1; i < (long)documentSnapshot.get( "pro_sp_sub_head_"+x+"_num" )+1; i++){
                                    productDetailsSpecificationModelList.add( new ProductDetailsSpecificationModel( 1,
                                            documentSnapshot.get( "pro_sp_sub_head_" + x + i ).toString()
                                            , documentSnapshot.get( "pro_sp_sub_head_d_" + x + i ).toString() ) );
                                }
                            }

                            ProductDetailsDescriptionAdaptor productDetailsDescriptionAdaptor
                                    = new ProductDetailsDescriptionAdaptor( getSupportFragmentManager()
                                    , productDescriptionIndicator.getTabCount()
                                    , productDescription
                                    , productDetailsSpecificationModelList  );
                            productDescriptionViewPager.setAdapter( productDetailsDescriptionAdaptor );
                            productDetailsDescriptionAdaptor.notifyDataSetChanged();

                        }
                        else{
                            // don't use tabLayout...
                            productDescriptionLayout.setVisibility( View.GONE );
                        }
                        productDetailsText.setText( documentSnapshot.get( "product_details" ).toString() );
                        dialog.dismiss();
                    }
                    else{
                        String error = task.getException().getMessage();
                        showToast(error);
                        dialog.dismiss();
                    }
                }
            } );
        }else{
            dialog.dismiss();
        }

    }

    private void setCartUpdate(){
        cartShopID = temCartItemModelList.get( currentVariant ).getProductShopID();
        for ( int p = 0; p <temCartItemModelList.size()-1; p++){
            CartOrderSubItemModel cIM = temCartItemModelList.get( p );
            if (cIM.getProductID().equals( productID )) {
                cartThisIndex = temCartItemModelList.indexOf( cIM );
                cartThisQty = Integer.parseInt( cIM.getProductQty() );
                ALREADY_ADDED_IN_CART = true;
                setCartLayoutVisibility(false, cIM.getProductQty());
                break;
            }
        }

    }

    private void setProductData(int variantIndex){
        // Set ImageLayout Data
        productImageList.clear();
        ProductSubModel productSubModel =  pProductModel.getProductSubModelList().get( variantIndex );
        productImageList.addAll( productSubModel.getpImage() );
        productName.setText( productSubModel.getpName() );
        productPrice.setText( "Rs." + productSubModel.getpSellingPrice() + "/-" );
        productCutPrice.setText( "Rs." + productSubModel.getpMrpPrice() + "/-" );

        if (productDetailsImagesAdapter!=null){
            productDetailsImagesAdapter.notifyDataSetChanged();
        }else{
            productDetailsImagesAdapter = new ProductDetailsImagesAdapter( productImageList );
            productImagesViewPager.setAdapter( productDetailsImagesAdapter );
            productDetailsImagesAdapter.notifyDataSetChanged();
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setVegNonData(){
        // Set veg Non Image...
        if ( pProductModel.getpVegNonType() == SHOP_TYPE_VEG){
            pVegNonTypeImage.setImageTintList( this.getColorStateList( R.color.colorGreen )  );
            pVegNonTypeImage.setBackgroundTintList( this.getColorStateList(  R.color.colorGreen ) );
        }else if( pProductModel.getpVegNonType() == SHOP_TYPE_NON_VEG){
            pVegNonTypeImage.setImageTintList( this.getColorStateList( R.color.colorRed )  );
            pVegNonTypeImage.setBackgroundTintList( this.getColorStateList(  R.color.colorRed ) );
        }else if( pProductModel.getpVegNonType() == SHOP_TYPE_NO_SHOW){
            pVegNonTypeImage.setVisibility( View.GONE );
        }
    }
    private void setWeightSpinner(){
        // Set city code Spinner
        ArrayAdapter <String> weightAdaptor = new ArrayAdapter <String>(this,
                android.R.layout.simple_spinner_item, productVariantList);
        weightAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weightSpinner.setAdapter(weightAdaptor);
        weightSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                currentVariant = position;
                setProductData( currentVariant );
                // update cart variant...
                if (currentUser!=null && ALREADY_ADDED_IN_CART){
                    updateCartVariant(currentVariant);
                }
            }

            @Override
            public void onNothingSelected(AdapterView <?> parent) {
//                if (cityList.size() == 1 || ! isUploadImages)
//                    showToast( "Upload Images first..!" );
            }
        } );
    }

    // Cart Action ----------------------------------------------------
    private void cartAction(){

        // Add to Cart Click...
        addToCartLayout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser!=null && isInternetConnected()){
                    if (cartShopID != null){
                        if (cartShopID.equals( SHOP_ID_CURRENT )){
                            // Continue...
                            dialog.show();
                            uploadOnDatabaseNewCart( );
                        }
                        else{
                            DialogsClass.alertDialog( ProductDetails.this, "Cart has another shop's products!",
                                    "Sorry! You Can not purchase from different shop at a time. Please remove your cart to purchase from this shop. " ).show();
                        }
                    }
                    else{
                        // Continue...
                        dialog.show();
                        uploadOnDatabaseNewCart( );
                    }
                }
                else{
                    DialogsClass.signInUpDialog( ProductDetails.this, PRODUCT_DETAILS_ACTIVITY );
                }
            }
        } );

        addOneCartBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartThisQty = cartThisQty + 1;
//                setCartLayoutVisibility(false, String.valueOf( cartThisQty + 1 ) );
                updateCartThisQty( String.valueOf( cartThisQty ));
            }
        } );

        removeOneCartBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartThisQty = cartThisQty - 1;
                if (cartThisQty > 0){
//                    setCartLayoutVisibility(false, String.valueOf( cartThisQty ) );
                    updateCartThisQty( String.valueOf( cartThisQty ));
                }else{
                    if (isInternetConnected()){
                        dialog.show();
                        setCartLayoutVisibility(true, null );
                        //  REMOVE FROM CART...
                        removeFromCart();
                    }
                }
            }
        } );

        checkOutCartLayout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // GOTO Cart Activity...
                startActivity( new Intent(ProductDetails.this, CartActivity.class) );
            }
        } );

    }

    private void setCartLayoutVisibility( boolean isCartBtnVisible,@Nullable String cartQty ){
        if (isCartBtnVisible){
            addToCartLayout.setVisibility( View.VISIBLE );
            addMoreCartLayout.setVisibility( View.GONE );
        }else{
            addToCartLayout.setVisibility( View.GONE );
            addMoreCartLayout.setVisibility( View.VISIBLE );
            updateCartThisQty(cartQty);
        }
    }

    private void updateCartThisQty( String pQty ){
        cartQtyText.setText( pQty );
        temCartItemModelList.get( cartThisIndex ).setProductQty( pQty );
    }
    private void updateCartVariant( int variantNo ){
        ProductSubModel productSubModel =  pProductModel.getProductSubModelList().get( variantNo );
        String pName = productSubModel.getpName();
        ArrayList<String> pImageLink = productSubModel.getpImage();
        String pPrice = productSubModel.getpSellingPrice();
        String pMrpPrice = productSubModel.getpMrpPrice();
        String pWeight = productSubModel.getpWeight();
        if (pWeight!=null){
            pName = pName + " ( " + pWeight + " )";
        }
        temCartItemModelList.get( cartThisIndex ).setProductName( pName );
        temCartItemModelList.get( cartThisIndex ).setProductImage( pImageLink.get( 0 ));
        temCartItemModelList.get( cartThisIndex ).setProductSellingPrice( pPrice );
        temCartItemModelList.get( cartThisIndex ).setProductMrpPrice( pMrpPrice );
    }

    private void uploadOnDatabaseNewCart( ){
        String cartID = StaticMethods.getRandomCartID();
        ProductSubModel productSubModel =  pProductModel.getProductSubModelList().get( currentVariant );
        String pName = productSubModel.getpName();
        ArrayList<String> pImageLink = productSubModel.getpImage();
        String pPrice = productSubModel.getpSellingPrice();
        String pMrpPrice = productSubModel.getpMrpPrice();
        String pWeight = productSubModel.getpWeight();
        if (pWeight!=null){
            pName = pName + " ( " + pWeight + " )";
        }
        String pQty = "1";
//        uploadOnDatabaseNewCart(cartID, pName, pImageLink[0], pPrice, pMrpPrice, "1" );

//        dialog.show();
        CartOrderSubItemModel cartOrderSubItemModel = new CartOrderSubItemModel(
                CART_TYPE_ITEMS,
                cartID,
                SHOP_ID_CURRENT,
                productID,
                pName,
                pImageLink.get( 0 ),
                pPrice,
                pMrpPrice,
                pQty
        );
        int sizeOfCartList = temCartItemModelList.size();
        if (sizeOfCartList>0){
            if (sizeOfCartList == 2){
                cartThisIndex = sizeOfCartList - 1;
                temCartItemModelList.add(cartThisIndex, cartOrderSubItemModel );
            }else{
                cartThisIndex = sizeOfCartList - 2;
                temCartItemModelList.add(cartThisIndex, cartOrderSubItemModel );
            }

        }else{
            cartThisIndex = 0;
            temCartItemModelList.add( cartOrderSubItemModel );
            temCartItemModelList.add( new CartOrderSubItemModel( CART_TYPE_TOTAL_PRICE ) );
        }
        // Add In Temp Cart list..
        cartThisQty = 1;
        ALREADY_ADDED_IN_CART = true;
        cartShopID = SHOP_ID_CURRENT;
        // query to update on database...
        UserDataQuery.uploadCartDataQuery( ProductDetails.this, dialog, cartOrderSubItemModel);
        setCartLayoutVisibility(false, pQty );
    }

    private void removeFromCart(){
        String cartID = temCartItemModelList.get( cartThisIndex ).getCartID();
        UserDataQuery.deleteFromCartQuery(  dialog, cartID );
        temCartItemModelList.remove( cartThisIndex );
        cartThisQty = 0;
        cartThisIndex = 0;
        ALREADY_ADDED_IN_CART = false;
    }
    // Cart Action ----------------------------------------------------


}

