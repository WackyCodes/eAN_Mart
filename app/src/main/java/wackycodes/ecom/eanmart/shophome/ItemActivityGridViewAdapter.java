package wackycodes.ecom.eanmart.shophome;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.databasequery.DBQuery;
import wackycodes.ecom.eanmart.productdetails.ProductDetails;
import wackycodes.ecom.eanmart.productdetails.ProductModel;

public class ItemActivityGridViewAdapter extends BaseAdapter {

    private List <ProductModel> gridProductList;

    public ItemActivityGridViewAdapter(List <ProductModel> gridProductList) {
        this.gridProductList = gridProductList;
    }

    @Override
    public int getCount() {
        return gridProductList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final ProductModel horizontalItemViewModel = gridProductList.get( position );
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.horizontal_itemview_item, null );

        ImageView itemImage = view.findViewById( R.id.hr_product_image );
        TextView itemName =  view.findViewById( R.id.hr_product_name );
        TextView itemPrice =  view.findViewById( R.id.hr_product_price );
        TextView itemCutPrice =  view.findViewById( R.id.hr_product_cut_price );
        TextView itemOffPer =  view.findViewById( R.id.hr_off_percentage );

//            TextView itemStock =  view.findViewById( R.id.hr_product );

        String sellingPrice = horizontalItemViewModel.getProductSubModelList().get( 0 ).getpSellingPrice();
        String mrpPrice = horizontalItemViewModel.getProductSubModelList().get( 0 ).getpMrpPrice();
        itemName.setText( horizontalItemViewModel.getProductSubModelList().get( 0 ).getpName() );
        itemPrice.setText( "Rs."+ sellingPrice + "/-" );
        itemCutPrice.setText( "Rs."+ mrpPrice + "/-" );

        String[] imageLinks = horizontalItemViewModel.getProductSubModelList().get( 0 ).getpImage();
        Glide.with( view.getContext() ).load( imageLinks[0] )
                .apply( new RequestOptions().placeholder( R.drawable.ic_photo_black_24dp ) ).into( itemImage );

        int perOff = ((Integer.parseInt( mrpPrice )
                - Integer.parseInt( sellingPrice )) * 100) /
                Integer.parseInt( mrpPrice );
        itemOffPer.setText( perOff + "% Off" );


        view.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoProductDetailIntent = new Intent(parent.getContext(), ProductDetails.class);
                gotoProductDetailIntent.putExtra( "PRODUCT_ID", gridProductList.get( position ).getpProductID() );

                int shopHomeCategoryIndex;
                int layoutIndex;
                int productIndex;

                DBQuery.shopHomeCategoryList.get( 0 ).get( 0 ).getProductModelList().get( 0 );
                parent.getContext().startActivity( gotoProductDetailIntent );
            }
        } );

//        if(convertView == null){
        // add Products....
//        }
//        else {
//            view = convertView;
//        }
        return  view;
    }

}
