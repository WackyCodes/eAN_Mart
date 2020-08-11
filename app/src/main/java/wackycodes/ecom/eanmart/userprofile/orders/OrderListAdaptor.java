package wackycodes.ecom.eanmart.userprofile.orders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.databasequery.UserDataQuery;
import wackycodes.ecom.eanmart.other.StaticMethods;

public class OrderListAdaptor extends RecyclerView.Adapter<OrderListAdaptor.ViewHolder> {
    private List <OrderItemModel> orderItemModelList;

    public OrderListAdaptor(List <OrderItemModel> orderItemModelList) {
        this.orderItemModelList = orderItemModelList;
    }
    @NonNull
    @Override
    public OrderListAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View orderView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.order_list_layout_item, parent, false );
        return new ViewHolder( orderView );
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListAdaptor.ViewHolder holder, int position) {

        holder.setData( position );
    }

    @Override
    public int getItemCount() {
        return orderItemModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView orderID;
        private TextView productName;
        private TextView billAmount;
        private TextView shopID;
        private TextView deliveryStatus;
        private TextView qtyText;
        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            productImage = itemView.findViewById( R.id.product_image );
            orderID = itemView.findViewById( R.id.order_shopping_id );
            productName = itemView.findViewById( R.id.product_name );
            billAmount = itemView.findViewById( R.id.bill_amount );
            shopID = itemView.findViewById( R.id.shop_id );
            deliveryStatus = itemView.findViewById( R.id.delivery_status );
            qtyText = itemView.findViewById( R.id.product_qty );
        }

        private void setData(int position){
            // TODO : Set Data
            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StaticMethods.showToast( itemView.getContext(), "Code Not Found!" );
                }
            } );

            if (orderItemModelList.get( position ).getCartOrderSubItemModelList().size()!=0){
//                getListData(position);
            }else{
                setOrderItemData(position);
            }

        }

        private void setOrderItemData(int position){
//            OrderItemModel orderItemModel = orderItemModelList.get( position );
//            String pImageLink = orderItemModel.getCartOrderSubItemModelList().get( 0 ).getProductImage();
//            String orderId = orderItemModel.getOrderID();
//            String pName = orderItemModel.getCartOrderSubItemModelList().get( 0 ).getProductName();
//            String billingAmount = orderItemModel.getBillingAmounts();
//            String shopId = orderItemModel.getShopID();
//            String dStatus = orderItemModel.getOrderStatus();
//            int qtyText = orderItemModel.getCartOrderSubItemModelList().size();


        }

        private void getListData(final int index){
            UserDataQuery.getCollection( "ORDERS" )
                    .orderBy( "index", Query.Direction.DESCENDING ) //order_time. order_date
                    .get()
                    .addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task <QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                if (task.getResult().size()>0){
                                    OrderItemModel orderItemModel;
//                                    showToast( context, "Order Founded.!" );
                                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                                        orderItemModel = new OrderItemModel(  );
                                        orderItemModel.setOrderID( documentSnapshot.get( "order_id" ).toString() );
                                        orderItemModel.setShopID( documentSnapshot.get( "shop_id" ).toString() );




                                        orderItemModelList.add( orderItemModel );
                                    }

                                }else{
//                                    showToast( context, "No Order Founded.!" );
                                }
                            }else{

                            }
                        }
                    } );
        }

    }

}
