package wackycodes.ecom.eanmart.bannerslider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.other.StaticMethods;
import wackycodes.ecom.eanmart.apphome.shophome.ShopHomeActivity;
import wackycodes.ecom.eanmart.productdetails.ProductDetails;

import static wackycodes.ecom.eanmart.other.StaticMethods.showToast;
import static wackycodes.ecom.eanmart.other.StaticValues.BANNER_CLICK_TYPE_CATEGORY;
import static wackycodes.ecom.eanmart.other.StaticValues.BANNER_CLICK_TYPE_NONE;
import static wackycodes.ecom.eanmart.other.StaticValues.BANNER_CLICK_TYPE_PRODUCT;
import static wackycodes.ecom.eanmart.other.StaticValues.BANNER_CLICK_TYPE_SHOP;
import static wackycodes.ecom.eanmart.other.StaticValues.BANNER_CLICK_TYPE_WEBSITE;
import static wackycodes.ecom.eanmart.other.StaticValues.LIST_MAIN_HOME_PAGE;
import static wackycodes.ecom.eanmart.other.StaticValues.LIST_SHOP_VIEW_PAGE;

public class BannerSliderAdaptor extends PagerAdapter {

    List <BannerSliderModel> bannerSliderModelList;
    private int pageType;

    public BannerSliderAdaptor(List <BannerSliderModel> bannerSliderModelList, int pageType) {
        this.bannerSliderModelList = bannerSliderModelList;
        this.pageType = pageType;
    }

    @SuppressLint("NewApi")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        View view = LayoutInflater.from( container.getContext() ).inflate( R.layout.banner_slider_item, container, false );
//        RoundedImageView bannerImage = view.findViewById( R.id.banner_slider_Images );
        ImageView bannerImage = view.findViewById( R.id.banner_slider_Image );
        ConstraintLayout bannerSliderContainer = view.findViewById( R.id.banner_slider_container );
        // Set Background color...
//        if (position%2 == 0){
//            bannerSliderContainer.setBackgroundTintList(
//                    ColorStateList.valueOf( Color.parseColor( "#f2ff22" ) ) );
//        }else{
//            bannerSliderContainer.setBackgroundTintList(
//                    ColorStateList.valueOf( Color.parseColor( "#aafa23" ) ) );
//        }

//         Set Image Resource...
        Glide.with( view.getContext() ).load( bannerSliderModelList.get( position ).getBannerImage() )
                .apply( new RequestOptions().placeholder( R.drawable.ic_photo_black_24dp ) ).into( bannerImage );

        bannerImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                 TODO:
                switch (bannerSliderModelList.get( position ).getBannerClickType()){
                    case BANNER_CLICK_TYPE_NONE:
                        // No Action..
                        break;
                    case BANNER_CLICK_TYPE_WEBSITE:
                        // Click And GO to WebSite...
                        StaticMethods.gotoURL( v.getContext(), bannerSliderModelList.get( position ).getBannerClickID() );
                        break;
                    case BANNER_CLICK_TYPE_SHOP:
                        // Open Shop...
                        Intent intent = new Intent( v.getContext(), ShopHomeActivity.class );
                        intent.putExtra( "SHOP_ID", bannerSliderModelList.get( position ).getBannerClickID()  );
                        v.getContext().startActivity( intent );
                        break;
                    case BANNER_CLICK_TYPE_CATEGORY:
                        // Open Category Item...
                        if (pageType == LIST_MAIN_HOME_PAGE){
                            // GO TO Any Category...in shop view....
                          //  MainActivity.wPreviousFragment = FRAGMENT_MAIN_HOME;
//                            if (shopsViewFragment == null || !ShopsViewFragment.catID.equals(  bannerSliderModelList.get( position ).getBannerClickID()  )){
//                                shopsViewFragment = new ShopsViewFragment( bannerSliderModelList.get( position ).getBannerClickID() );
//                            }
//                            setFromHomeFragment(shopsViewFragment , FRAGMENT_MAIN_SHOPS_VIEW );

                        }else if(pageType == LIST_SHOP_VIEW_PAGE){
                            // RESTART SHOP VIEW... Using New Object...

                        }
                        // 1
                        // GO TO SHOP VIEW...
//                            MainActivity.wPreviousFragment = FRAGMENT_MAIN_HOME;
//                            if (shopHomeFragment == null){
//                                shopHomeFragment = new ShopHomeFragment();
//                            }
//                            setFromHomeFragment( shopHomeFragment, FRAGMENT_SHOP_HOME );

//                            // 2.
//                            if (shopHomeFragment == null){
//                                shopHomeFragment = new ShopHomeFragment();
//                            }
//                            MainActivity.wPreviousFragment = FRAGMENT_MAIN_SHOPS_VIEW;
//                            MainActivity.wCurrentFragment = FRAGMENT_SHOP_HOME;
//                            setFromShopViewFragment( shopHomeFragment );
                        break;
                    case BANNER_CLICK_TYPE_PRODUCT:
                        Intent productDetailIntent = new Intent( v.getContext(), ProductDetails.class );
                        productDetailIntent.putExtra( "PRODUCT_ID", bannerSliderModelList.get( position ).getBannerClickID() );
//                            productDetailIntent.putExtra( "HOME_CAT_INDEX", crrShopCatIndex );
//                            productDetailIntent.putExtra( "LAYOUT_INDEX", layoutIndex );
//                            productDetailIntent.putExtra( "PRODUCT_INDEX", productIndex );
                        v.getContext().startActivity( productDetailIntent );
                        break;
                    default:
                        showToast(v.getContext(), "Code Not Found.!");
                        break;
                }
            }
        } );

        container.addView( view, 0 );
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView( (View)object );
    }

    @Override
    public int getCount() {
        return bannerSliderModelList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
