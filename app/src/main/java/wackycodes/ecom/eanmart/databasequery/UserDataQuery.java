package wackycodes.ecom.eanmart.databasequery;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wackycodes.ecom.eanmart.MainActivity;
import wackycodes.ecom.eanmart.other.CheckInternetConnection;
import wackycodes.ecom.eanmart.other.DialogsClass;
import wackycodes.ecom.eanmart.other.StaticMethods;
import wackycodes.ecom.eanmart.other.StaticValues;
import wackycodes.ecom.eanmart.productdetails.ProductDetails;
import wackycodes.ecom.eanmart.shophome.ShopHomeActivity;
import wackycodes.ecom.eanmart.shophome.ShopProductCatActivity;
import wackycodes.ecom.eanmart.userprofile.addresses.AddAddressActivity;
import wackycodes.ecom.eanmart.userprofile.addresses.MyAddressesActivity;
import wackycodes.ecom.eanmart.userprofile.addresses.UserAddressDataModel;
import wackycodes.ecom.eanmart.userprofile.cart.CartActivity;
import wackycodes.ecom.eanmart.userprofile.cart.CartOrderSubItemModel;
import wackycodes.ecom.eanmart.userprofile.notifications.NotificationActivity;
import wackycodes.ecom.eanmart.userprofile.notifications.NotificationModel;
import wackycodes.ecom.eanmart.userprofile.orders.OrderItemModel;

import static wackycodes.ecom.eanmart.MainActivity.badgeNotifyCount;
import static wackycodes.ecom.eanmart.databasequery.DBQuery.currentUser;
import static wackycodes.ecom.eanmart.databasequery.DBQuery.firebaseFirestore;
import static wackycodes.ecom.eanmart.other.StaticMethods.showToast;
import static wackycodes.ecom.eanmart.other.StaticValues.CART_TYPE_ITEMS;
import static wackycodes.ecom.eanmart.other.StaticValues.CART_TYPE_TOTAL_PRICE;
import static wackycodes.ecom.eanmart.other.StaticValues.NOTIFY_SIMPLE;
import static wackycodes.ecom.eanmart.other.StaticValues.QUERY_TO_ADD_ADDRESS;
import static wackycodes.ecom.eanmart.other.StaticValues.QUERY_TO_REMOVE_ADDRESS;
import static wackycodes.ecom.eanmart.other.StaticValues.QUERY_TO_UPDATE_ADDRESS;
import static wackycodes.ecom.eanmart.other.StaticValues.USER_DATA_MODEL;

public class UserDataQuery {

    public static ListenerRegistration userCartLR;
    public static ListenerRegistration userNotificationLR;

    // List Variables.....
    public static List <CartOrderSubItemModel> cartItemModelList = new ArrayList <>();
    public static List <CartOrderSubItemModel> temCartItemModelList = new ArrayList <>();

    // Order List...
    public static List<OrderItemModel> orderItemModelList = new ArrayList <>();

    public static List<UserAddressDataModel> userAddressList = new ArrayList <>();

    public static List<NotificationModel> notificationModelList = new ArrayList <>();

    // Reference...
    public static CollectionReference getCollection( String collectionName){

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
    public static void loadCartDataQuery(@Nullable Context context){
//        if ( CheckInternetConnection.isInternetConnected( context )){
//        }
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

                                CartOrderSubItemModel cartOrderSubItemModel = new CartOrderSubItemModel(
                                        CART_TYPE_ITEMS,
                                        documentSnapshot.get( "cart_id" ).toString(),
                                        documentSnapshot.get( "p_shop_id" ).toString(),
                                        documentSnapshot.get( "p_id" ).toString(),
                                        documentSnapshot.get( "p_name" ).toString(),
                                        documentSnapshot.get( "p_image" ).toString(),
                                        documentSnapshot.get( "p_selling_price" ).toString(),
                                        documentSnapshot.get( "p_mrp_price" ).toString(),
                                        documentSnapshot.get( "p_qty" ).toString()
                                );
                                cartItemModelList.add( cartOrderSubItemModel );
                                cartCount = cartCount+1;
                            }
                            setCartBadge( cartCount );
                            if(cartCount>0){
                                // Set Cart
                                CartActivity.isCartEmpty = false;
                                if (temCartItemModelList.size()==0){
                                    temCartItemModelList.addAll( cartItemModelList );
                                    temCartItemModelList.add( cartItemModelList.size(), new CartOrderSubItemModel( CART_TYPE_TOTAL_PRICE ) );
                                }
                            }else{
                                // set Invisible cart
                                CartActivity.isCartEmpty = true;
                            }

                        }
                    }
                } );
    }
    // Set Cart badge...
    public static void setCartBadge(int cartCount){
        // Main Activity Badge
        if (cartCount > 0){
            String cartText = String.valueOf( cartCount );
            if (MainActivity.badgeCartCount!=null){
                MainActivity.badgeCartCount.setVisibility( View.VISIBLE );
                MainActivity.badgeCartCount.setText( cartText );
            }
            // Shop Home Activity badge
            if (ShopHomeActivity.badgeCartCount!=null){
                ShopHomeActivity.badgeCartCount.setVisibility( View.VISIBLE );
                ShopHomeActivity.badgeCartCount.setText( cartText );
            }
            // Shop Product Cat Activity badge
            if (ShopProductCatActivity.badgeCartCount!=null){
                ShopProductCatActivity.badgeCartCount.setVisibility( View.VISIBLE );
                ShopProductCatActivity.badgeCartCount.setText( cartText );
            }
            // Product Details Activity...
            if (ProductDetails.badgeCartCount!=null){
                ProductDetails.badgeCartCount.setVisibility( View.VISIBLE );
                ProductDetails.badgeCartCount.setText( cartText );
            }
        }else{
            if (MainActivity.badgeCartCount!=null){
                MainActivity.badgeCartCount.setVisibility( View.GONE );
            }
            // Shop Home Activity badge
            if (ShopHomeActivity.badgeCartCount!=null){
                ShopHomeActivity.badgeCartCount.setVisibility( View.GONE );
            }
            // Shop Product Cat Activity badge
            if (ShopProductCatActivity.badgeCartCount!=null){
                ShopProductCatActivity.badgeCartCount.setVisibility( View.GONE );
            }
            // Product Details Activity...
            if (ProductDetails.badgeCartCount!=null){
                ProductDetails.badgeCartCount.setVisibility( View.GONE );
            }
        }

    }
    // Query to Upload Cart data....
    public static void uploadCartDataQuery(final Context context, final Dialog dialog, CartOrderSubItemModel cartOrderSubItemModel){
        Map <String, Object> cartMap = new HashMap <>();
//        cart_index
        cartMap.put( "cart_index", temCartItemModelList.size() );
        cartMap.put( "cart_id", cartOrderSubItemModel.getCartID() );
        cartMap.put( "p_shop_id", cartOrderSubItemModel.getProductShopID() );
        cartMap.put( "p_id", cartOrderSubItemModel.getProductID() );
        cartMap.put( "p_name", cartOrderSubItemModel.getProductName() );
        cartMap.put( "p_image", cartOrderSubItemModel.getProductImage() );
        cartMap.put( "p_selling_price", cartOrderSubItemModel.getProductSellingPrice() );
        cartMap.put( "p_mrp_price", cartOrderSubItemModel.getProductMrpPrice() );
        cartMap.put( "p_qty", cartOrderSubItemModel.getProductQty() );
        if (CheckInternetConnection.isInternetConnected( context )){
            getCollection("CART").document( cartOrderSubItemModel.getCartID() )
                    .set( cartMap )
                    .addOnCompleteListener( new OnCompleteListener <Void>() {
                        @Override
                        public void onComplete(@NonNull Task <Void> task) {
                            if (task.isSuccessful()){
                                // Automatically updated...
                                if (CartActivity.myCartAdaptor!=null){
                                    CartActivity.myCartAdaptor.notifyDataSetChanged();
                                }
                            }else{

                            }
                            dialog.dismiss();
                        }
                    } );

        }

    }
    // Query to delete Cart...
    public static void deleteFromCartQuery(@Nullable final Dialog dialog, String cartID){
        getCollection("CART").document( cartID ).delete()
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        if (task.isSuccessful()){
                            if (CartActivity.myCartAdaptor!=null){
                                CartActivity.myCartAdaptor.notifyDataSetChanged();
                            }
                        }else{

                        }
                        if (dialog!=null){
                            dialog.dismiss();
                        }
                    }
                } );
    }

    // Order data query...
    public static void loadOrderDataQuery(final Context context){

        getCollection( "ORDERS" )
                .orderBy( "index", Query.Direction.DESCENDING ) //order_time. order_date
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
    public static void loadNotificationsQuery(@Nullable final Context context){
        userNotificationLR = getCollection("NOTIFICATIONS")
                .orderBy( "index", Query.Direction.DESCENDING ).limit( 10 ) //order_time. order_date
                .addSnapshotListener( new EventListener <QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null ) {
                            int cartCount = 0;
                            notificationModelList.clear();
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                NotificationModel notificationModel = new NotificationModel(
                                        Integer.parseInt( documentSnapshot.get( "notify_type" ).toString()),
                                        documentSnapshot.get( "notify_id" ).toString(),
                                        documentSnapshot.get( "notify_click_id" ).toString(),
                                        documentSnapshot.get( "notify_image" ).toString(),
                                        documentSnapshot.get( "notify_title" ).toString(),
                                        documentSnapshot.get( "notify_body" ).toString(),
                                        documentSnapshot.get( "notify_date" ).toString(),
                                        documentSnapshot.get( "notify_time" ).toString(),
                                        documentSnapshot.getBoolean( "notify_is_read" )
                                );

                                notificationModelList.add( notificationModel );
                                if (!notificationModel.getNotifyIsRead()){
                                    cartCount = cartCount+1;
                                }
                            }

                            if (NotificationActivity.notificationAdaptor!=null){
                                NotificationActivity.notificationAdaptor.notifyDataSetChanged();
                            }

                            if (badgeNotifyCount != null)
                                if (cartCount>0){
                                    badgeNotifyCount.setVisibility( View.VISIBLE );
                                    badgeNotifyCount.setText( cartCount + "" );
                                    if ( context!=null ){
                                        DialogsClass.setAlarmOnNotification( context, "Notification", "You have "+cartCount +" new notification!" );
                                    }
                                }else{
                                    badgeNotifyCount.setVisibility( View.GONE );
                                }

                        }
                    }
                } );
    }

    // Query To Get All Address...
    public static void getAllAddressesQuery(){
        userAddressList.clear();

        getCollection("ADDRESSES").orderBy( "add_id" ).get()
                .addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task <QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            UserAddressDataModel userAddressDataModel;
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String add_id = documentSnapshot.get( "add_id" ).toString();
                                String add_user_name = documentSnapshot.get( "add_user_name" ).toString();
                                String add_h_no = documentSnapshot.get( "add_h_no" ).toString();
                                String add_colony = documentSnapshot.get( "add_colony" ).toString();
                                String add_city = documentSnapshot.get( "add_city" ).toString();
                                String add_pin_code = documentSnapshot.get( "add_pin_code" ).toString();
                                String add_landmark = documentSnapshot.get( "add_landmark" ).toString();
                                Boolean is_selected = documentSnapshot.getBoolean( "is_selected" );
                                userAddressDataModel = new UserAddressDataModel(
                                        add_id,
                                        add_user_name,
                                        "Mobile",
                                        add_h_no,
                                        add_colony,
                                        add_city,
                                        " ",
                                        add_pin_code,
                                        add_landmark,
                                        is_selected
                                );
                                userAddressList.add( userAddressDataModel );
                            }

                            if (MyAddressesActivity.myAddressRecyclerAdaptor!=null){
                                MyAddressesActivity.myAddressRecyclerAdaptor.notifyDataSetChanged();
                            }

                        }else{
                            // Failed...
                        }
                    }
                } );
    }

    // Query To Remove and update address from database...
    public static void addUpdateRemoveAddressQuery(final Context context
            , final Dialog dialog, final int queryType, @NonNull String addressID,
                                                   final int index,@Nullable final UserAddressDataModel userAddressDataModel ){
        if (queryType == QUERY_TO_ADD_ADDRESS || queryType == QUERY_TO_UPDATE_ADDRESS ){
            Map <String, Object> addNewAddress = new HashMap <>();
            addNewAddress.put( "add_id", addressID );
            addNewAddress.put( "add_user_name", userAddressDataModel.getAddUserName() );
            addNewAddress.put( "add_h_no", userAddressDataModel.getAddHouseNoWard() );
            addNewAddress.put( "add_colony", userAddressDataModel.getAddColonyVillage() );
            addNewAddress.put( "add_city", userAddressDataModel.getAddCityName() );
            addNewAddress.put( "add_pin_code", userAddressDataModel.getAddAreaPinCode() );
            addNewAddress.put( "add_landmark", userAddressDataModel.getAddLandmark() );
            addNewAddress.put( "is_selected", userAddressDataModel.getSelectedAddress() );

            getCollection("ADDRESSES").document( addressID )
                    .set( addNewAddress )
                    .addOnCompleteListener( new OnCompleteListener <Void>() {
                        @Override
                        public void onComplete(@NonNull Task <Void> task) {
                            if (task.isSuccessful()){
                                // Start activity and finishing..
                                userAddressList.add(index, userAddressDataModel );
                                MyAddressesActivity.myAddressRecyclerAdaptor.notifyDataSetChanged();
                                dialog.dismiss();
                                switch (queryType){
                                    case QUERY_TO_ADD_ADDRESS:
                                        showToast( context, "Address Added Successfully..!" );
                                        AddAddressActivity.addAddressActivity.finish();
                                        break;
                                    case QUERY_TO_UPDATE_ADDRESS:
                                        showToast( context, "Update address Successfully..!" );
                                        AddAddressActivity.addAddressActivity.finish();
                                        break;
                                    default:
                                        break;
                                }
                            }
                            else{
                                dialog.dismiss();
                                MyAddressesActivity.myAddressRecyclerAdaptor.notifyDataSetChanged();
                                String error = "Failed..! Error : " + task.getException().getMessage();
                                showToast( context, error );
                            }
                        }
                    } );

        }else{
            getCollection("ADDRESSES").document( addressID )
                    .delete()
                    .addOnCompleteListener( new OnCompleteListener <Void>() {
                        @Override
                        public void onComplete(@NonNull Task <Void> task) {
                            if (task.isSuccessful()){
                                dialog.dismiss();
                                userAddressList.remove( index );
                                MyAddressesActivity.myAddressRecyclerAdaptor.notifyDataSetChanged();
                                showToast( context, "Remove Address Successfully..!" );
                                AddAddressActivity.addAddressActivity.finish();
                            }else{
                                dialog.dismiss();
                                String error = "Failed..! Error : " + task.getException().getMessage();
                            }
                        }
                    } );

        }

    }







}
