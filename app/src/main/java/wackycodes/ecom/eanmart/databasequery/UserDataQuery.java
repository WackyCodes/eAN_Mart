package wackycodes.ecom.eanmart.databasequery;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import wackycodes.ecom.eanmart.MainActivity;
import wackycodes.ecom.eanmart.other.CheckInternetConnection;
import wackycodes.ecom.eanmart.other.StaticValues;
import wackycodes.ecom.eanmart.productdetails.ProductDetails;
import wackycodes.ecom.eanmart.shophome.ShopHomeActivity;
import wackycodes.ecom.eanmart.userprofile.cart.CartItemModel;

import static wackycodes.ecom.eanmart.databasequery.DBQuery.currentUser;
import static wackycodes.ecom.eanmart.databasequery.DBQuery.firebaseFirestore;
import static wackycodes.ecom.eanmart.other.StaticMethods.showToast;
import static wackycodes.ecom.eanmart.other.StaticValues.USER_DATA_MODEL;

public class UserDataQuery {


    public static ListenerRegistration userCartLR;

    // List Variables.....
    public static List <CartItemModel> cartItemModelList = new ArrayList <>();


    // Reference...
    private static CollectionReference getCollection( String collectionName){

        CollectionReference collectionReference =
                firebaseFirestore.collection( "USERS" )
                        .document( currentUser.getUid() )
                        .collection( collectionName.toUpperCase() );
        return collectionReference;

    }

    // Query....

    // User Profile Data Query...
    public static void loadUserDataQuery(@Nullable final Context context,@Nullable final Dialog dialog){
        firebaseFirestore.collection( "USERS" ).document( currentUser.getUid() )
                .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if (task.isSuccessful()){

                    String user_first_name = task.getResult().get( "user_first_name" ).toString();
                    String user_last_name = task.getResult().get( "user_last_name" ).toString();
                    String user_full_name = task.getResult().get( "user_full_name" ).toString();
                    String user_email = task.getResult().get( "user_email" ).toString();
                    String user_mobile = task.getResult().get( "user_mobile" ).toString();
                    String user_profile_image = task.getResult().get( "user_profile_image" ).toString();
//                    String user_auth_id = task.getResult().get( "user_auth_id" ).toString();
                    String user_city_name = task.getResult().get( "user_city_name" ).toString();
                    String user_city_code = task.getResult().get( "user_city_code" ).toString();
                    String user_area_pincode = task.getResult().get( "user_area_pincode" ).toString();

//                    String app_version = task.getResult().get( "app_version" ).toString();
                    StaticValues.CURRENT_CITY_NAME = user_city_name;
                    StaticValues.CURRENT_CITY_CODE = user_city_code;

                    boolean is_mobile_verify = false;
                    boolean is_email_verify = false;
                    if(task.getResult().get( "is_mobile_verify" )!=null){
                        is_mobile_verify = task.getResult().getBoolean( "is_mobile_verify" );
                    }
                    if(task.getResult().get( "is_email_verify" )!=null){
                        is_email_verify = task.getResult().getBoolean( "is_email_verify" );
                    }

                    USER_DATA_MODEL.setUserCityName( user_city_name );
                    USER_DATA_MODEL.setUserCityCode( user_city_code );
                    USER_DATA_MODEL.setUserAreaPinCode( user_area_pincode );

                    USER_DATA_MODEL.setUserAuthID( currentUser.getUid() );
                    USER_DATA_MODEL.setUserFullName( user_full_name );
                    USER_DATA_MODEL.setUserEmail( user_email );
                    USER_DATA_MODEL.setUserMobile( user_mobile );
                    USER_DATA_MODEL.setUserProfilePhoto( user_profile_image );
                    USER_DATA_MODEL.setMobileVerify( is_mobile_verify );
                    USER_DATA_MODEL.setEmailVerify( is_email_verify );
                    USER_DATA_MODEL.setLoadData( true );

                    if (MainActivity.drawerImage != null && MainActivity.drawerName != null && MainActivity.drawerEmail != null){
                        if (context!=null){
                            Glide.with( context ).load( USER_DATA_MODEL.getUserProfilePhoto() ).into( MainActivity.drawerImage );
                        }
                        MainActivity.drawerName.setText( USER_DATA_MODEL.getUserFullName() );
                        MainActivity.drawerEmail.setText( USER_DATA_MODEL.getUserEmail() );
                    }
                    if (dialog!=null){
                        dialog.dismiss();
                    }
                }else{
                    // Show Error Message...
                    String error = task.getException().getMessage();
                    if (context!=null){
                        showToast(context ,error);
                    }
                    if (dialog!=null){
                        dialog.dismiss();
                    }
                }
            }
        } );
    }

    // Cart Data query....
    public static void loadCartDataQuery(Context context){

        if (CheckInternetConnection.isInternetConnected( context )){

            userCartLR = getCollection("CART").orderBy( "cart_index" )
                    .addSnapshotListener( new EventListener <QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                            if (queryDocumentSnapshots != null ) {
                                int cartCount = 0;
                                cartItemModelList.clear();

                                for ( int x = 0; x < queryDocumentSnapshots.getDocuments().size(); x++ ) {
                                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get( x );
                                    // Load RealTime Data...

                                    CartItemModel cartItemModel = new CartItemModel(
                                            documentSnapshot.get( "p_shop_id" ).toString(),
                                            documentSnapshot.get( "p_id" ).toString(),
                                            documentSnapshot.get( "p_name" ).toString(),
                                            documentSnapshot.get( "p_image" ).toString(),
                                            documentSnapshot.get( "p_selling_price" ).toString(),
                                            documentSnapshot.get( "p_mrp_price" ).toString(),
                                            documentSnapshot.get( "p_qty" ).toString()
                                    );

                                    cartItemModelList.add( cartItemModel );
                                    cartCount = cartCount+1;

                                }

                                if(cartCount>0){
                                    setCartBadge(String.valueOf( cartCount ));
                                }else{
                                    // set Invisible cart
                                }

                            }

                        }
                    } );
        }


    }
    // Set Cart badge...
    public static void setCartBadge(String cartCount){
        // Main Activity Badge
        if (MainActivity.badgeCartCount!=null){
            MainActivity.badgeCartCount.setVisibility( View.VISIBLE );
            MainActivity.badgeCartCount.setText( cartCount );
        }
        // Shop Home Activity badge
        if (ShopHomeActivity.badgeCartCount!=null){
            ShopHomeActivity.badgeCartCount.setVisibility( View.VISIBLE );
            ShopHomeActivity.badgeCartCount.setText( cartCount );
        }
        // Product Details Activity...
        if (ProductDetails.badgeCartCount!=null){
            ProductDetails.badgeCartCount.setVisibility( View.VISIBLE );
            ProductDetails.badgeCartCount.setText( cartCount );
        }

    }

    // Order data query...
    public static void loadOrderDataQuery(final Context context){

        getCollection( "ORDERS" )
                .orderBy( "order_date" ).orderBy( "order_time" )
                .get()
                .addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task <QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult().size()>0){

                                showToast( context, "Order Founded.!" );

                            }else{
                                showToast( context, "No Order Founded.!" );
                            }
                        }else{

                        }

                    }
                } );


    }

    // Notification ListenerRegistration
    public static void loadNotificationsQuery(Context context){



    }


    // Query to load Cart List data....
    public static void cartListQuery(final Context context, final boolean loadProductData, final Dialog dialog, final int requestActivity){
        // clear all List..
        cartItemModelList.clear();
        if (loadProductData){
            cartItemModelList.clear();
        }
        firebaseFirestore.collection( "USER" ).document( FirebaseAuth.getInstance().getUid() )
                .collection( "USER_DATA" ).document( "MY_CART" ).get()
                .addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            /**
                            tempIndex = 0;
                            final long my_cart_size = (long) task.getResult().get( "my_cart_size" );
                            for (long x = 0; x < my_cart_size; x++) {
                                dbTempListItem = task.getResult().get( "product_ID_" + x ).toString(); // Product ID
                                dbTempListItem2 = task.getResult().get( "product_qty_" + x ).toString(); // Product Qty
                                myCartCheckList.add( new MyCartCheckModel( task.getResult().get( "product_ID_" + x ).toString(),
                                        task.getResult().get( "product_qty_" + x ).toString() ) );
                                if (myCartCheckList.size() != 0){
                                    ProductDetails.ALREADY_ADDED_IN_CART = isContainInMyCartCheckList( ProductDetails.productID );
                                    // Set Badge Cart data...
                                    if (!loadProductData){
                                        setCartCountText( requestActivity, String.valueOf( myCartCheckList.size()) );
                                    }
                                }
                                if (loadProductData) {
                                    //set visibility of cart view...
                                    if ( tempIndex == 0 ){
                                        MyCartFragment.isCartEmpty = false;
                                        MyCartFragment.dontHaveCartConstLayout.setVisibility( View.GONE );
                                        MyCartFragment.myCartConstLayout.setVisibility( View.VISIBLE );
                                    }
//                            final String product_ID = task.getResult().get( "product_ID_" + x ).toString();
                                    firebaseFirestore.collection( "PRODUCTS" )
                                            .document( task.getResult().get( "product_ID_" + x ).toString() )
                                            .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
//                                        int cartIndex = 0;
//                                        if (myCartList.size() >= 2){
//                                            cartIndex = myCartList.size() - 2;
//                                        }

                                                if(task.getResult().get( "product_qty_type" ) != null){
                                                    myCartItemModelList.add( new MyCartItemModel( 0
                                                            , myCartCheckList.get( tempIndex ).getProductId()
                                                            , Integer.parseInt( myCartCheckList.get( tempIndex ).getProductQuantity() )
                                                            , task.getResult().get( "product_image_1" ).toString()
                                                            , task.getResult().get( "product_full_name" ).toString()
                                                            , task.getResult().get( "product_price" ).toString()
                                                            , task.getResult().get( "product_cut_price" ).toString()
                                                            , task.getResult().get( "product_qty_type" ).toString() ) );
                                                }else{
                                                    myCartItemModelList.add( new MyCartItemModel( 0
                                                            , myCartCheckList.get( tempIndex ).getProductId()
                                                            , Integer.parseInt( myCartCheckList.get( tempIndex ).getProductQuantity() )
                                                            , task.getResult().get( "product_image_1" ).toString()
                                                            , task.getResult().get( "product_full_name" ).toString()
                                                            , task.getResult().get( "product_price" ).toString()
                                                            , task.getResult().get( "product_cut_price" ).toString()
                                                            , "" ) );
                                                }

                                                if ( tempIndex == ( my_cart_size - 1 ) ){
                                                    myCartItemModelList.add( new MyCartItemModel( 1 ) );
                                                }
                                                if (myCartCheckList.size() == 0){
                                                    myCartItemModelList.clear();
                                                    MyCartFragment.isCartEmpty = true;
                                                    MyCartFragment.myCartConstLayout.setVisibility( View.GONE );
                                                    MyCartFragment.dontHaveCartConstLayout.setVisibility( View.VISIBLE );
                                                }
                                                MyCartFragment.myCartAdaptor.notifyDataSetChanged();
                                                tempIndex = tempIndex + 1;
                                                dialog.dismiss();
                                            } else {
                                                dialog.dismiss();
                                                tempIndex = tempIndex + 1;
                                                String error = task.getException().getMessage();
                                                showToast( context, error );
                                            }
                                        }
                                    } );

                                    dialog.dismiss();
                                }else{
                                    dialog.dismiss();
                                }
                            }

                            dialog.dismiss();
                      */
                        }
                        else {
                            dialog.dismiss();
                            String error = task.getException().getMessage();
                            showToast( context, error );
                        }

                    }
                } );

    }






}
