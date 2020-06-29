package wackycodes.ecom.eanmart.userprofile.orders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import wackycodes.ecom.eanmart.R;
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
        OrderItemModel orderItemModel = orderItemModelList.get( position );
        String pImageLink = orderItemModel.getCartOrderSubItemModelList().get( 0 ).getProductImage();
        String orderId = orderItemModel.getOrderID();
        String pName = orderItemModel.getCartOrderSubItemModelList().get( 0 ).getProductName();
        String billingAmount = orderItemModel.getBillingAmounts();
        String shopId = orderItemModel.getShopID();
        String dStatus = orderItemModel.getOrderStatus();
        int qtyText = orderItemModel.getCartOrderSubItemModelList().size();
        holder.setData( pImageLink, orderId, pName, billingAmount, shopId, dStatus, qtyText );
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

        private void setData(String pImageLink, String orderId, String pName,
                             String billingAmount, String shopId, String dStatus, int qtyText){
            // TODO : Set Data
            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StaticMethods.showToast( itemView.getContext(), "Code Not Found!" );
                }
            } );
        }
    }

}
