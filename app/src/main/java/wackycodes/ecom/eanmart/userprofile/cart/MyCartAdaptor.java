package wackycodes.ecom.eanmart.userprofile.cart;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.databasequery.UserDataQuery;
import wackycodes.ecom.eanmart.other.CheckInternetConnection;
import wackycodes.ecom.eanmart.other.DialogsClass;
import wackycodes.ecom.eanmart.other.StaticValues;

import static wackycodes.ecom.eanmart.databasequery.UserDataQuery.temCartItemModelList;
import static wackycodes.ecom.eanmart.other.StaticValues.CART_TYPE_ITEMS;
import static wackycodes.ecom.eanmart.other.StaticValues.CART_TYPE_TOTAL_PRICE;

public class MyCartAdaptor extends RecyclerView.Adapter {

    private List <CartOrderSubItemModel> myCartItemModelList;

    public MyCartAdaptor(List <CartOrderSubItemModel> myCartItemModelList) {
        this.myCartItemModelList = myCartItemModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (myCartItemModelList.get( position ).getCartType()){
            case CART_TYPE_ITEMS:
                return CART_TYPE_ITEMS;
            case CART_TYPE_TOTAL_PRICE:
                return CART_TYPE_TOTAL_PRICE;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case CART_TYPE_ITEMS:
                View cartItemView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.cart_layout_item, parent, false );
                return new MyCartViewHolder( cartItemView );

            case CART_TYPE_TOTAL_PRICE:
                View cartTotalAmountView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.cart_total_price_layout, parent, false );
                return new MyCartTotalAmountHolder( cartTotalAmountView );
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (myCartItemModelList.get( position ).getCartType()){
            case CART_TYPE_ITEMS:
                CartOrderSubItemModel myCartItemModel1 = myCartItemModelList.get( position );

                String productID = myCartItemModel1.getProductID();
                String imgLink = myCartItemModel1.getProductImage();
                String name = myCartItemModel1.getProductName();
                String price = myCartItemModel1.getProductSellingPrice();
                String cutPrice = myCartItemModel1.getProductMrpPrice();
                int productQuantity = Integer.parseInt( myCartItemModel1.getProductQty() );
//                String qtyType = myCartItemModel1.getProductQtyType();
                String qtyType = "Sample";
                ((MyCartViewHolder) holder).setCartData(position, productID, imgLink, name, price, cutPrice, productQuantity, qtyType );
                break;
            case CART_TYPE_TOTAL_PRICE:
                int totalItems = 0;
                int totalItemsPrice = 0;
                int deliveryCharge = 0;
                int totalCutPrice = 0;
                int productQty = 0;
                for (int x = 0; x < myCartItemModelList.size(); x++){
                    if (myCartItemModelList.get( x ).getCartType() == CART_TYPE_ITEMS){
                        totalItems++;
                        productQty = Integer.parseInt( myCartItemModelList.get( x ).getProductQty() );
                        totalItemsPrice = totalItemsPrice + productQty * Integer.parseInt(myCartItemModelList.get( x ).getProductSellingPrice());
                        totalCutPrice = totalCutPrice + productQty * Integer.parseInt( myCartItemModelList.get( x ).getProductMrpPrice() );
                    }
                }
                if (totalItemsPrice < 500 ){
                    deliveryCharge = 40;
                }
                int savedAmount = totalCutPrice - totalItemsPrice;

                ((MyCartTotalAmountHolder) holder).setCartTotalAmountData( totalItems, totalItemsPrice, deliveryCharge, savedAmount);
                break;
            default:
                return;
        }
    }

    @Override
    public int getItemCount() {
        return myCartItemModelList.size();
    }

    public class MyCartViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productName;
        private TextView productPrice;
        private TextView productCutPrice;
        private TextView removeItemBtn;
        private TextView productQuantity;
        private ImageButton removeOneCartBtn; // remove_item_from_cart_imgBtn
        private ImageButton addOneCartBtn; // add_item_to_cart_imgBtn
        Dialog dialog;

        public MyCartViewHolder(@NonNull final View itemView) {
            super( itemView );

            productImage = itemView.findViewById( R.id.product_image );
            productName = itemView.findViewById( R.id.product_name );
            productPrice = itemView.findViewById( R.id.product_price );
            productCutPrice = itemView.findViewById( R.id.product_cut_price );
            removeItemBtn = itemView.findViewById( R.id.remove_item_from_cart );
            productQuantity = itemView.findViewById( R.id.cart_item_text );
            removeOneCartBtn = itemView.findViewById( R.id.remove_item_from_cart_imgBtn );
            addOneCartBtn = itemView.findViewById( R.id.add_item_to_cart_imgBtn );

            dialog = DialogsClass.getDialog( itemView.getContext() );

        }

        private void setCartData(final int index, final String productId, String imgLink,
                                 String name, final String price, final String cutPrice, final int quantity, String qtyType ){
            Glide.with( itemView.getContext() ).load( imgLink )
                    .apply( new RequestOptions().placeholder( R.drawable.ic_photo_black_24dp) ).into( productImage );

            productQuantity.setText( String.valueOf( quantity ) );
            productName.setText( name );
            // Set Price according to quantity...
            productPrice.setText( "Rs."+ price +"/-" );
            productCutPrice.setText( "Rs." + cutPrice  + "/-" );
            removeItemBtn.setEnabled( true );
            removeItemBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeItemBtn.setEnabled( false );
                    if (CheckInternetConnection.isInternetConnected( itemView.getContext() )){
                        dialog.show();
                        //  REMOVE FROM CART...
                        removeFromCart(dialog, index );
                    }else{
                        removeItemBtn.setEnabled( true );
                    }
                }
            } );

            addOneCartBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   int qty = quantity + 1;
                    updateCartThisQty(String.valueOf( qty ), index );
                }
            } );
            removeOneCartBtn.setEnabled( true );
            removeOneCartBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qty = quantity - 1;
                    if (qty > 0){
                        updateCartThisQty(String.valueOf( qty ), index );
                    }else{
                        removeOneCartBtn.setEnabled( false );
                        if (CheckInternetConnection.isInternetConnected( itemView.getContext() )){
                            dialog.show();
                            //  REMOVE FROM CART...
                            removeFromCart(dialog, index );
                        }else{
                            removeOneCartBtn.setEnabled( true );
                        }
                    }
                }
            } );

        }

        private String  getPriceValue(int qty, String price){
            if (qty == 1){
                return price;
            }else if (qty > 1){
                int tempP = Integer.parseInt( price ) * qty;
                return Integer.toString( tempP );
            }
            return null;
        }

        private void removeFromCart(Dialog dialog, int cartIndex){
            String cartID = temCartItemModelList.get( cartIndex ).getCartID();
            UserDataQuery.deleteFromCartQuery(  dialog, cartID );
            temCartItemModelList.remove( cartIndex );

            if (temCartItemModelList.size()==1){
                temCartItemModelList.clear();
                CartActivity.myCartConstLayout.setVisibility( View.GONE );
                CartActivity.dontHaveCartConstLayout.setVisibility( View.VISIBLE );
            }
            CartActivity.myCartAdaptor.notifyDataSetChanged();
        }

        private void updateCartThisQty( String pQty, int cartIndex ){
            productQuantity.setText( pQty );
            temCartItemModelList.get( cartIndex ).setProductQty( pQty );
            CartActivity.myCartAdaptor.notifyDataSetChanged();
        }

    }

    public class MyCartTotalAmountHolder extends RecyclerView.ViewHolder{

        private TextView cartTotalItems;
        private TextView cartTotalItemsPrice;
        private TextView cartDeliveryCharge;
        private TextView cartSavedAmount;
        private TextView cartTotalAmount;

        public MyCartTotalAmountHolder(@NonNull View itemView) {
            super( itemView );
            cartTotalItems = itemView.findViewById( R.id.total_items );
            cartTotalItemsPrice = itemView.findViewById( R.id.total_items_amount );
            cartDeliveryCharge = itemView.findViewById( R.id.delivery_amount );
            cartSavedAmount = itemView.findViewById( R.id.my_cart_sving_amounts );
            cartTotalAmount = itemView.findViewById( R.id.my_cart_total_amounts1 );
        }

        private void setCartTotalAmountData( int totalItems, int totalItemsPrice, int deliveryCharge, int savedAmount){
            cartTotalItems.setText( "(" + totalItems + ")" );
            cartTotalItemsPrice.setText( "Rs." + totalItemsPrice + "/-");
            if (deliveryCharge > 0){
                cartDeliveryCharge.setText( "Rs." + deliveryCharge + "/-" );
            }else {
                cartDeliveryCharge.setText( "Free" );
            }
//            cartDeliveryCharge.setText( "As per Company" );
            if(savedAmount > 0){
                cartSavedAmount.setVisibility( View.VISIBLE );
                cartSavedAmount.setText( "You have saved Rs."+ savedAmount + "/- on this shopping..!" );
            }else {
                cartSavedAmount.setVisibility( View.INVISIBLE );
            }
            cartTotalAmount.setText( "Rs." + (totalItemsPrice + deliveryCharge) +"/-" );

            CartActivity.myCartTotalAmounts2.setText( "Rs." + (totalItemsPrice + deliveryCharge) +"/-" );

            // Update Bill amount and Delivery Charge...
            StaticValues.TOTAL_BILL_AMOUNT = totalItemsPrice + deliveryCharge;
            StaticValues.DELIVERY_AMOUNT = deliveryCharge;

        }
    }


}
