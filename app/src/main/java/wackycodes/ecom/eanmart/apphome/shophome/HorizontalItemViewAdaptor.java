package wackycodes.ecom.eanmart.apphome.shophome;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.productdetails.ProductDetails;
import wackycodes.ecom.eanmart.productdetails.ProductModel;

import static wackycodes.ecom.eanmart.other.StaticValues.VIEW_GRID_LAYOUT;
import static wackycodes.ecom.eanmart.other.StaticValues.VIEW_HORIZONTAL_LAYOUT;
import static wackycodes.ecom.eanmart.other.StaticValues.VIEW_RECTANGLE_LAYOUT;

public class HorizontalItemViewAdaptor extends RecyclerView.Adapter <RecyclerView.ViewHolder>  {

    private int crrShopCatIndex;
    private int layoutIndex;
    private int viewType;
    private List <ProductModel> horizontalItemViewModelList;

    public HorizontalItemViewAdaptor( int crrShopCatIndex, int layoutIndex, int viewType, List <ProductModel> horizontalItemViewModelList) {
        this.crrShopCatIndex = crrShopCatIndex;
        this.layoutIndex = layoutIndex;
        this.viewType = viewType;
        this.horizontalItemViewModelList = horizontalItemViewModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (viewType) {
            case VIEW_HORIZONTAL_LAYOUT:
                return VIEW_HORIZONTAL_LAYOUT;
            case VIEW_RECTANGLE_LAYOUT:
                return VIEW_RECTANGLE_LAYOUT;
             case VIEW_GRID_LAYOUT:
                return VIEW_GRID_LAYOUT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_HORIZONTAL_LAYOUT:
            case VIEW_GRID_LAYOUT:
                View hrView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.horizontal_itemview_item, parent, false );
                return new HomeHorizontalViewHolder( hrView );
            case VIEW_RECTANGLE_LAYOUT:
                View viewAllView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.horizontal_products_view_all_layout, parent, false );
                return new ViewAllHorizontalViewHolder( viewAllView );
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String productId = horizontalItemViewModelList.get( position ).getpProductID();
        ArrayList<String> imgLink = horizontalItemViewModelList.get( position ).getProductSubModelList().get( 0 ).getpImage();
        String name = horizontalItemViewModelList.get( position ).getProductSubModelList().get( 0 ).getpName();
        String price = horizontalItemViewModelList.get( position ).getProductSubModelList().get( 0 ).getpSellingPrice();
        String cutPrice = horizontalItemViewModelList.get( position ).getProductSubModelList().get( 0 ).getpMrpPrice();
        String stockInfo = horizontalItemViewModelList.get( position ).getProductSubModelList().get( 0 ).getpStocks();
        switch (viewType) {
            case VIEW_HORIZONTAL_LAYOUT:
            case VIEW_GRID_LAYOUT:
                ((HomeHorizontalViewHolder) holder).setHomeHrProduct( productId, imgLink, name, price, cutPrice, stockInfo, position );
                break;
            case VIEW_RECTANGLE_LAYOUT:
                Boolean codInfo = horizontalItemViewModelList.get( position ).getpIsCOD();
                ((ViewAllHorizontalViewHolder) holder).setHorizontalProducts( productId, imgLink, name, price, cutPrice, codInfo, stockInfo, position );
                break;
            default:
                return;
        }

//        holder.setHrProductImage( imgLink, name, price, cutPrice );
    }

    @Override
    public int getItemCount() {
        if (horizontalItemViewModelList.size() > 8 && viewType == VIEW_HORIZONTAL_LAYOUT ) {
            return 8;
        } else {
            return horizontalItemViewModelList.size();
        }
    }

    public class HomeHorizontalViewHolder extends RecyclerView.ViewHolder {

        ImageView hrProductImage;
        TextView hrProductName;
        TextView hrProductPrice;
        TextView hrProductCutPrice;
        TextView hrProductOffPercentage;
        TextView stockText;

        public HomeHorizontalViewHolder(@NonNull View itemView) {
            super( itemView );

            hrProductImage = itemView.findViewById( R.id.hr_product_image );
            hrProductName = itemView.findViewById( R.id.hr_product_name );
            hrProductPrice = itemView.findViewById( R.id.hr_product_price );
            hrProductCutPrice = itemView.findViewById( R.id.hr_product_cut_price );
            hrProductOffPercentage = itemView.findViewById( R.id.hr_off_percentage );
            stockText = itemView.findViewById( R.id.stock_text );

        }

        private void setHomeHrProduct(final String productId, ArrayList<String> imgLink, String name, String price, String cutPrice, String stockInfo, final int index) {

            hrProductName.setText( name );
            hrProductPrice.setText( "Rs." + price + "/-" );
            hrProductCutPrice.setText( "Rs." + cutPrice + "/-" );

            Glide.with( itemView.getContext() ).load( imgLink.get( 0 ) ).apply( new RequestOptions()
                    .placeholder( R.drawable.ic_photo_black_24dp ) ).into( hrProductImage );

            if (Integer.parseInt( stockInfo ) > 0) {
//                hrProductStockInfo.setText( "in Stock" );
                stockText.setVisibility( View.GONE );
            } else {
                stockText.setVisibility( View.VISIBLE );
                stockText.setText( "Out of Stock" );
                itemView.setEnabled( false );
                itemView.setClickable( false );
            }

//            int perOff = ((Integer.parseInt( cutPrice ) - Integer.parseInt( price )) * 100) / Integer.parseInt( cutPrice );
            hrProductOffPercentage.setText( "Rs." + ((Integer.parseInt( cutPrice ) - Integer.parseInt( price )) + " save" ));

            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailIntent = new Intent( itemView.getContext(), ProductDetails.class );
                    productDetailIntent.putExtra( "PRODUCT_ID", productId );
                    productDetailIntent.putExtra( "HOME_CAT_INDEX", crrShopCatIndex );
                    productDetailIntent.putExtra( "LAYOUT_INDEX", layoutIndex );
                    productDetailIntent.putExtra( "PRODUCT_INDEX", index );
                    itemView.getContext().startActivity( productDetailIntent );
                }
            } );

        }

    }

    public class ViewAllHorizontalViewHolder extends RecyclerView.ViewHolder {
        ImageView hrProductImage;
        TextView hrProductName;
        TextView hrProductPrice;
        TextView hrProductCutPrice;
        TextView hrProductOffPercentage;
        TextView hrProductCodText;
        TextView hrProductStockInfo;

        public ViewAllHorizontalViewHolder(@NonNull View itemView) {
            super( itemView );
            hrProductImage = itemView.findViewById( R.id.hr_viewAll_product_image );
            hrProductName = itemView.findViewById( R.id.hr_viewAll_product_name );
            hrProductPrice = itemView.findViewById( R.id.hr_viewAll_product_price );
            hrProductCutPrice = itemView.findViewById( R.id.hr_viewAll_product_cut_price );
            hrProductOffPercentage = itemView.findViewById( R.id.hr_viewAll_product_off_per );
            hrProductCodText = itemView.findViewById( R.id.hr_viewAll_product_cod_text );
            hrProductStockInfo = itemView.findViewById( R.id.hr_viewAll_product_stock_info );
        }

        private void setHorizontalProducts(final String productId, ArrayList<String> imgLink, String name, String price, String cutPrice, Boolean cod, String stockInfo, final int index) {
            hrProductName.setText( name );
            hrProductPrice.setText( "Rs." + price + "/-" );
            hrProductCutPrice.setText( "Rs." + cutPrice + "/-" );
            if (cod) {
                hrProductCodText.setText( "Cash on Delivery Available" );
                hrProductCodText.setVisibility( View.VISIBLE );
            } else {
                hrProductCodText.setVisibility( View.INVISIBLE );
            }
            if (Integer.parseInt( stockInfo ) > 0) {
//                hrProductStockInfo.setText( "in Stock" );
                hrProductStockInfo.setVisibility( View.GONE );
            } else {
                hrProductStockInfo.setVisibility( View.VISIBLE );
                hrProductStockInfo.setText( "Out of Stock" );
                itemView.setEnabled( false );
                itemView.setClickable( false );
            }

            Glide.with( itemView.getContext() ).load( imgLink.get( 0 ) ).apply( new RequestOptions()
                    .placeholder( R.drawable.ic_photo_black_24dp ) ).into( hrProductImage );

//            int perOff = ((Integer.parseInt( cutPrice ) - Integer.parseInt( price )) * 100) / Integer.parseInt( cutPrice );
            hrProductOffPercentage.setText( "Rs." + ((Integer.parseInt( cutPrice ) - Integer.parseInt( price )) + " save" ));

            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailIntent = new Intent( itemView.getContext(), ProductDetails.class );
                    productDetailIntent.putExtra( "PRODUCT_ID", productId );
                    productDetailIntent.putExtra( "HOME_CAT_INDEX", crrShopCatIndex );
                    productDetailIntent.putExtra( "LAYOUT_INDEX", layoutIndex );
                    productDetailIntent.putExtra( "PRODUCT_INDEX", index );
                    itemView.getContext().startActivity( productDetailIntent );
                }
            } );

        }
    }

}
