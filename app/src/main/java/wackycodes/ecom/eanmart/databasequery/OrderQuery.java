package wackycodes.ecom.eanmart.databasequery;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.HashMap;
import java.util.Map;

import wackycodes.ecom.eanmart.buyprocess.ContinueShopping;
import wackycodes.ecom.eanmart.other.StaticMethods;

import static wackycodes.ecom.eanmart.databasequery.DBQuery.firebaseFirestore;
import static wackycodes.ecom.eanmart.databasequery.UserDataQuery.orderItemModelList;
import static wackycodes.ecom.eanmart.databasequery.UserDataQuery.temCartItemModelList;
import static wackycodes.ecom.eanmart.other.StaticValues.SHOP_ID_CURRENT;
import static wackycodes.ecom.eanmart.other.StaticValues.USER_DATA_MODEL;

public class OrderQuery {

    /**
     * Step 1 : create a random order ID
     * step 2 : check that Oder ID that is exist on database or not..
     * step 3 : if Oder ID already exist then gmyscl Back (step 1) to New Order ID...
     * step 4 : if Order ID is new then create a new Document with this order ID
     * step 5 : if Task is failed then gmyscl back (step 1)...
     * step 6 : if Task is Success Go Process to payment...
     * step 7 : if PayMode COD gmyscl to (step 12) :
     * step 8 : if PayMode Online then check status - ( a - Success, b - Pending, c - Failed )
     * step 9 : if PayStatus - Failed : stored Info in order Document and exit.
     * step 10 : if PayStatus - Pending : stored Info in order Document and wait for get response...
     * step 11 : if PayStatus - Success : gmyscl to ( Step 12)
     * step 12 : Update order document and create collection to track order...
     * step 13 : check delivery_status and tracking details until order is not placed...
     * step 14 :
     *          delivery Status :
     *          (1) WAITING : when order is not accepted by admin...
     *          (2) ACCEPTED : when order has been accepted...
     *          (3) PROCESS : When order is in process to delivery... OUT_FOR_DELIVERY
     *          (4) CANCELLED : When Order has been cancelled by user...
     *          (5) SUCCESS : when order has been delivered successfully...
     *          (6) FAILED : when PayMode Online and payment has been failed...
     *          (7) PENDING : when Payment is Pending
     *          (8) PACKED - ( Waiting for Delivery ) READY_TO_DELIVERY
     *
     *          1. WAITING - ( For Accept )
     *          2. ACCEPTED - ( Preparing )
     *          3. PACKED - ( Waiting for Delivery ) READY_TO_DELIVERY
     *          4. PROCESS  - ( On Delivery ) OUT_FOR_DELIVERY
     *          5. SUCCESS - Success Full Delivered..!
     *          6. CANCELLED -  When Order has been cancelled by user...
     *          7. FAILED -  when PayMode Online and payment has been failed...
     *          8. PENDING - when Payment is Pending...
     *
     * step 15 : if delivery status : CANCELLED then Query to get payment Return...
     * step 16 : Update Order history..!!
     *
     */

    public static void placeOrderRequestQuery(Dialog dialog, String orderID, String orderDeliveredName, String orderDeliveryAddress, String orderDeliveryPin
            , String totalAmounts, String billingAmounts, String savingAmounts, String deliveryCharge, String deliverySchedule ){
        // User Information...
        String orderByUserId = DBQuery.currentUser.getUid();
        String orderByUserName = USER_DATA_MODEL.getUserFullName();
        String orderByUserMobile = USER_DATA_MODEL.getUserMobile();
//        String orderDeliveredName = getDeliveredFullName(); // 1
//        String orderDeliveryAddress = getDeliveryAddress(); // 2
//        String orderDeliveryPin = getDeliveryPin(); // 3
        String orderDate = StaticMethods.getCurrentDate();
        String orderDay = StaticMethods.getCurrentDay();
        String orderTime = StaticMethods.getCurrentTime();


//        String orderID = getRandomOrderID(); // 4. Check whether is Exist() already Or not...
        String shopID = SHOP_ID_CURRENT;
        String deliveryID = " ";
//        int totalAmounts = getTotalAmounts(); // 5
//        int billingAmounts; // 6
//        int savingAmounts = getTotalMrps() - totalAmounts; // 7
        String orderStatus = "PENDING";
//        String deliveryCharge = "50"; // 8

        Map <String, Object> orderDetailMap = new HashMap <>();

        orderDetailMap.put( "order_id",  orderID );
        orderDetailMap.put( "index", StaticMethods.getRandomIndex() );

        orderDetailMap.put( "delivery_status", "WAITING" );
        orderDetailMap.put( "pay_mode", "COD" );

        orderDetailMap.put( "delivery_charge", deliveryCharge );
        orderDetailMap.put( "total_amounts", billingAmounts );
        orderDetailMap.put( "saving_amounts", savingAmounts );
        orderDetailMap.put( "billing_amounts", billingAmounts );

        // Put User Info and Address..
        orderDetailMap.put( "order_by_auth_id", orderByUserId );
        orderDetailMap.put( "order_by_name", orderByUserName );
        orderDetailMap.put( "order_by_mobile", orderByUserMobile );

        orderDetailMap.put( "order_accepted_by", orderDeliveredName );
        orderDetailMap.put( "order_delivery_address", orderDeliveryAddress );
        orderDetailMap.put( "order_delivery_pin", orderDeliveryPin );
        orderDetailMap.put( "order_date", orderDate );
        orderDetailMap.put( "order_day", orderDay );
        orderDetailMap.put( "order_time", orderTime );

        orderDetailMap.put( "delivery_schedule_time", deliverySchedule );

        // Get No_of_Product =
        orderDetailMap.put( "no_of_products", temCartItemModelList.size()-1 );

        for (int x = 0; x < temCartItemModelList.size() - 1; x++){
            orderDetailMap.put( "product_id_" + x, temCartItemModelList.get( x ).getProductID() );
            orderDetailMap.put( "product_image_" + x, temCartItemModelList.get( x ).getProductImage() );
            orderDetailMap.put( "product_name_" + x, temCartItemModelList.get( x ).getProductName() );
            orderDetailMap.put( "product_price_" + x, temCartItemModelList.get( x ).getProductSellingPrice() );
            orderDetailMap.put( "product_qty_" + x, temCartItemModelList.get( x ).getProductQty() );
        }

        // Put Delivery Info and Address..
//        orderDetailMap.put( "delivery_date", "" );
//        orderDetailMap.put( "delivery_day", "" );
//        orderDetailMap.put( "delivery_time", "" );
//        orderDetailMap.put( "delivery_by_uid", "" );
//        orderDetailMap.put( "delivery_by_name", "" );
//        orderDetailMap.put( "delivery_by_mobile", "" );

        // completeOrderQuery..
        completeOrderQuery(dialog, orderID, orderDetailMap);

    }

    // Next Step...
    private static void completeOrderQuery(final Dialog dialog, @NonNull final String orderID, @NonNull Map< String, Object > orderMap ){
        firebaseFirestore.collection( "SHOPS" ).document( SHOP_ID_CURRENT )
                .collection( "ORDERS" )
                .document( orderID )
                .set( orderMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        if (task.isSuccessful()){
                            // Now Add in User Order Item...
                            Map<String, Object> userOrderMap = new HashMap <>();
                            userOrderMap.put( "order_id", orderID );
                            userOrderMap.put( "shop_id", SHOP_ID_CURRENT );
                            userOrderMap.put( "index", StaticMethods.getRandomIndex() );

                            UserDataQuery.getCollection( "ORDERS" ).document( orderID )
                                    .set( userOrderMap )
                                    .addOnCompleteListener( new OnCompleteListener <Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task <Void> task) {
                                            if (task.isSuccessful()){
                                                // Successfully Order...
                                                dialog.dismiss();
                                                setSuccessFragment( orderID );
                                                // Update Products Stocks and Clear tempCart List...
                                                updateCartAndNotify();
                                            }else{

                                            }
                                        }
                                    } );
                        }else{

                        }

                    }
                } );

    }

    // Toast message show method...
    private static void showToast(Context context, String s) {
        Toast.makeText( context, s, Toast.LENGTH_SHORT ).show();
    }

    private static void setSuccessFragment( String orderId){
        ContinueShopping.waitingLayout.setVisibility( View.GONE );
        ContinueShopping.orderSuccessLinearLayout.setVisibility( View.VISIBLE );
        ContinueShopping.orderIdText.setText( "Your Order ID :\n" + orderId );

    }

    private static void updateCartAndNotify(){
        // TODO : Variant Change...
        for (int i = 0; i<temCartItemModelList.size() - 1; i++){
            UserDataQuery.deleteFromCartQuery(null, temCartItemModelList.get( i ).getCartID() );
            updateProductStockQuery(temCartItemModelList.get( i ).getProductID(), 1, temCartItemModelList.get( i ).getProductName()
                    ,Integer.parseInt( temCartItemModelList.get( i ).getProductQty() ) );
        }

        temCartItemModelList.clear();

    }

    private static String productStocks;
    public static void updateProductStockQuery(final String productID, final int variant, final String productName, final int qty){
        final CollectionReference collectionReference =
                firebaseFirestore.collection( "SHOPS" ).document( SHOP_ID_CURRENT ).collection( "PRODUCTS" );

        collectionReference
                .document( productID )
                .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    int stocks = Integer.parseInt( task.getResult().get("p_stocks_"+variant).toString() );
//                    String pName = task.getResult().get( "p_name_"+variant ).toString();
                    String pImage = task.getResult().get( "p_main_image" ).toString();
                    stocks = stocks - qty;

                    if (stocks >= 0){
                        int newV = variant;
                        int newStock = stocks;
                        // Update Stocks And Buy process...
                        collectionReference.document( productID )
                                .update( "p_stocks_"+newV, newStock )
                                .addOnSuccessListener( new OnSuccessListener <Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Call Function to Order process...

                            }
                        } );

                        if (newStock == 0){
                            // Send Notification of Out of Stocks..
                            sendOutOfStockNotification( productID, newV, productName, pImage );
                        }

                    }else{
                        // Product out of Stocks already...

                    }
//                    dialog.dismiss();

                }else {
                    // Task Failed...
//                    dialog.dismiss();
                }
            }
        } );

    }

    // Out Of Stock Notification...
    public static void sendOutOfStockNotification(String productID, int varant, String pName, String pImage){

        String notify_id = productID;

        Map <String, Object> notifyMap = new HashMap <>();
        notifyMap.put( "index", StaticMethods.getRandomIndex() );
        notifyMap.put( "notify_id", notify_id );
        notifyMap.put( "notify_type", "OOS" );
        notifyMap.put( "notify_product_id", productID );
        notifyMap.put( "notify_image", pImage );
        notifyMap.put( "notify_read_state", false );
        notifyMap.put( "notify_title", "Product Out of Stock" );
        notifyMap.put( "notify_body", "Product Name : " + pName +"\n Product ID : " + productID );

        firebaseFirestore.collection( "SHOPS" ).document( SHOP_ID_CURRENT )
                .collection( "NOTIFICATIONS" )
                .document( notify_id )
                .set( notifyMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        // Notify...

                    }
                } );


    }




}
