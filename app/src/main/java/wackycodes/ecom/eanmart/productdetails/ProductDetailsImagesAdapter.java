package wackycodes.ecom.eanmart.productdetails;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import wackycodes.ecom.eanmart.R;

public class ProductDetailsImagesAdapter extends PagerAdapter{

    private List <String> productsImages;

    public ProductDetailsImagesAdapter(List <String> productsImages) {
        this.productsImages = productsImages;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        ImageView productImage = new ImageView( container.getContext() );
//        productImage.setImageResource( productsImages.get( position ) );
        Glide.with( container.getContext() ).load( productsImages.get( position ) )
                .apply( new RequestOptions().placeholder( R.drawable.ic_photo_black_24dp) ).into( productImage );
        container.addView( productImage, 0 );
        return productImage;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView( (ImageView)object );
    }

    @Override
    public int getCount() {
        return productsImages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


}
