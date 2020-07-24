package wackycodes.ecom.eanmart.apphome.shophome.search;

import android.widget.Filter;
import android.widget.Filterable;

import java.util.List;

import wackycodes.ecom.eanmart.apphome.category.ShopItemModel;
import wackycodes.ecom.eanmart.apphome.category.ShopListAdaptor;

public class SearchAdapter extends ShopListAdaptor implements Filterable {

    public SearchAdapter(List <ShopItemModel> shopItemModelList) {
        super( shopItemModelList );
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

