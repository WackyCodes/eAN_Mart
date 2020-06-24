package wackycodes.ecom.eanmart.apphome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import wackycodes.ecom.eanmart.MainActivity;
import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.category.ShopsViewFragment;
import wackycodes.ecom.eanmart.shophome.ShopHomeFragment;
import wackycodes.ecom.eanmart.wgridview.MyGridView;

import static wackycodes.ecom.eanmart.MainActivity.mainActivity;
import static wackycodes.ecom.eanmart.category.ShopsViewFragment.shopViewFragmentContext;
import static wackycodes.ecom.eanmart.category.ShopsViewFragment.shopsViewFragment;
import static wackycodes.ecom.eanmart.other.StaticMethods.showToast;
import static wackycodes.ecom.eanmart.other.StaticValues.FRAGMENT_MAIN_HOME;
import static wackycodes.ecom.eanmart.other.StaticValues.FRAGMENT_MAIN_SHOPS_VIEW;
import static wackycodes.ecom.eanmart.other.StaticValues.FRAGMENT_SHOP_HOME;
import static wackycodes.ecom.eanmart.other.StaticValues.TYPE_BANNER_MAIN_HOME;
import static wackycodes.ecom.eanmart.other.StaticValues.TYPE_BANNER_SHOPS_VIEW;
import static wackycodes.ecom.eanmart.other.StaticValues.TYPE_HOME_CATEGORY_LAYOUT;
import static wackycodes.ecom.eanmart.other.StaticValues.TYPE_HOME_SHOP_BANNER_AD;
import static wackycodes.ecom.eanmart.other.StaticValues.TYPE_HOME_SHOP_BANNER_SLIDER;
import static wackycodes.ecom.eanmart.other.StaticValues.TYPE_HOME_SHOP_STRIP_AD;
import static wackycodes.ecom.eanmart.other.StaticValues.TYPE_LIST_MAIN_HOME_CATEGORY;
import static wackycodes.ecom.eanmart.other.StaticValues.TYPE_LIST_SHOPS_VIEW_NAME;
import static wackycodes.ecom.eanmart.shophome.ShopHomeFragment.shopHomeFragment;

public class HomeFragmentAdaptor  extends RecyclerView.Adapter {

    private List <HomeFragmentModel> homeFragmentModelList;
    private RecyclerView.RecycledViewPool recycledViewPool;

    public HomeFragmentAdaptor(List <HomeFragmentModel> homeFragmentModelList) {
        this.homeFragmentModelList = homeFragmentModelList;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getItemViewType(int position) {
        switch (homeFragmentModelList.get( position ).getLayoutType()) {
            case TYPE_HOME_SHOP_BANNER_SLIDER:
                return TYPE_HOME_SHOP_BANNER_SLIDER;
            case TYPE_HOME_CATEGORY_LAYOUT:
                return TYPE_HOME_CATEGORY_LAYOUT;
            case TYPE_HOME_SHOP_STRIP_AD:
                return TYPE_HOME_SHOP_STRIP_AD;
            case TYPE_HOME_SHOP_BANNER_AD:
                return TYPE_HOME_SHOP_BANNER_AD;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType){
            case TYPE_HOME_SHOP_BANNER_SLIDER:
                // TODO : banner Slider
                return null;
            case TYPE_HOME_CATEGORY_LAYOUT:
                View categoryView = LayoutInflater.from( parent.getContext() ).inflate(
                        R.layout.grid_view_home_layout, parent, false );
                return new GridLayoutViewHolder( categoryView );
            case TYPE_HOME_SHOP_STRIP_AD:
            case TYPE_HOME_SHOP_BANNER_AD:
                View adBannerView = LayoutInflater.from( parent.getContext() ).inflate(
                        R.layout.ad_layout_strip_item, parent, false );
                return new ShopAdBannerViewHolder( adBannerView );
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (homeFragmentModelList.get( position ).getLayoutType()) {
            case TYPE_HOME_SHOP_BANNER_SLIDER:
                // TODO: Slider...
                break;
            case TYPE_HOME_CATEGORY_LAYOUT:
                ((GridLayoutViewHolder)holder).setDataGridLayout( homeFragmentModelList.get( position ).getCategoryTypeModelList() );
                break;
            case TYPE_HOME_SHOP_STRIP_AD:
            case TYPE_HOME_SHOP_BANNER_AD:
                String imgLink = homeFragmentModelList.get( position ).getShopImage();
                String nameOrColor = homeFragmentModelList.get( position ).getShopNameOrColor();
                String id = homeFragmentModelList.get( position ).getShopID();
                int bannerViewFragmentType = homeFragmentModelList.get( position ).getBannerViewFragmentType();
                ((ShopAdBannerViewHolder)holder).setBannerAd( id, imgLink, nameOrColor, bannerViewFragmentType );
                break;
            default:
                break;
        }

    }

    @Override
    public int getItemCount() {
        return homeFragmentModelList.size();
    }

    //============  Banner Slider View Holder ============

    //============  Banner Slider View Holder ============

    //============ Strip and Banner Banner ad  View Holder ============
    public class ShopAdBannerViewHolder extends RecyclerView.ViewHolder{
        private ImageView bannerAdImage;
        private ConstraintLayout bannerAdContainer;

        public ShopAdBannerViewHolder(@NonNull View itemView) {
            super( itemView );
            bannerAdContainer = itemView.findViewById(R.id.strip_ad_container);
            bannerAdImage = itemView.findViewById( R.id.strip_ad_image );
        }
        private void setBannerAd(String shopId, String imgLink, String colorCode, final int bannerViewFragmentType){
//            bannerAdContainer.setBackgroundColor( Color.parseColor( colorCode ) );
            // set Image Resource from database..
//            Glide.with( itemView.getContext() ).load( imgLink )
//                    .apply( new RequestOptions().placeholder( R.drawable.ic_strip_black_24dp ) ).into( bannerAdImage );

            bannerAdImage.setImageResource( R.drawable.strip_ad_a );
            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (bannerViewFragmentType){
                        case TYPE_BANNER_MAIN_HOME:
                            MainActivity.wPreviousFragment = FRAGMENT_MAIN_HOME;
                            if (shopHomeFragment == null){
                                shopHomeFragment = new ShopHomeFragment();
                            }
                            setFromHomeFragment( shopHomeFragment, FRAGMENT_SHOP_HOME );
                            break;
                        case TYPE_BANNER_SHOPS_VIEW:
                            if (shopHomeFragment == null){
                                shopHomeFragment = new ShopHomeFragment();
                            }
                            MainActivity.wPreviousFragment = FRAGMENT_MAIN_SHOPS_VIEW;
                            MainActivity.wCurrentFragment = FRAGMENT_SHOP_HOME;
                            setFromShopViewFragment( shopHomeFragment );
                            break;
                        default:
                            showToast(itemView.getContext(), "Code Not Found.!");
                            break;
                    }

                }
            } );
        }
    }
    //============  Banner ad  View Holder ============

    //==============  GridProduct Grid Layout View Holder =================
    public class GridLayoutViewHolder extends  RecyclerView.ViewHolder{
        private MyGridView gridLayout;

        public GridLayoutViewHolder(@NonNull View itemView) {
            super( itemView );
            gridLayout = itemView.findViewById( R.id.home_cat_grid_view );
        }
        private void setDataGridLayout( List<CategoryTypeModel> categoryTypeModelList ){
            SetCategoryItem setCategoryItem = new SetCategoryItem( categoryTypeModelList );
            gridLayout.setAdapter( setCategoryItem );
            setCategoryItem.notifyDataSetChanged();
        }

        private class SetCategoryItem extends BaseAdapter{
            List<CategoryTypeModel> categoryTypeModelList;
            public SetCategoryItem(List <CategoryTypeModel> categoryTypeModelList) {
                this.categoryTypeModelList = categoryTypeModelList;
            }

            @Override
            public int getCount() {
                return categoryTypeModelList.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @SuppressLint({"ViewHolder", "InflateParams"})
            @Override
            public View getView(final int position, View convertView, final ViewGroup parent) {

                View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.square_category_layout_item, null );

                ImageView itemImage = view.findViewById( R.id.sq_image_view );
                TextView itemName =  view.findViewById( R.id.sq_text_view );
                itemImage.setImageResource( Integer.parseInt( categoryTypeModelList.get( position ).getCatImage() ) );
//                Glide.with( itemView.getContext() ).load( categoryTypeModelList.get( position ).getCatImage() )
//                        .apply( new RequestOptions().placeholder( R.drawable.ic_photo_black_24dp ) ).into( itemImage );
                itemName.setText( categoryTypeModelList.get( position ).getCatName() );

                view.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCategoryClick( parent.getContext(), categoryTypeModelList.get( position ).getCatId(), categoryTypeModelList.get( position ).getType() );
                    }
                } );
                return view;
            }

            private void onCategoryClick(Context context, String docID, int type){

                switch (type){
                    case TYPE_LIST_MAIN_HOME_CATEGORY:
                        MainActivity.wPreviousFragment = FRAGMENT_MAIN_HOME;
                        if (shopsViewFragment == null){
                            shopsViewFragment = new ShopsViewFragment();
                        }
                        setFromHomeFragment(shopsViewFragment , FRAGMENT_MAIN_SHOPS_VIEW );
                        break;
                    case TYPE_LIST_SHOPS_VIEW_NAME:

                        MainActivity.wPreviousFragment = FRAGMENT_MAIN_SHOPS_VIEW;
                        MainActivity.wCurrentFragment = FRAGMENT_SHOP_HOME;
                        if (shopsViewFragment == null){
                            shopsViewFragment = new ShopsViewFragment();
                        }
                        setFromShopViewFragment( shopsViewFragment );
//                        Intent shopIntent = new Intent(context, ProductDetails.class);
//                        shopIntent.putExtra( "PRODUCT_ID", docID );
//                        context.startActivity( shopIntent );
//                        showToast( context, "Code not found.!" );
                        break;
                    default:
                        showToast( context, "Something went wrong.!" );
                        break;
                }
            }
        }

    }
    //==============  GridProduct Grid Layout View Holder =================


//    private void gotoShopActivity(Context context, String shopId){
//        Intent shopIntent = new Intent(context, ProductDetails.class);
//        shopIntent.putExtra( "SHOP_ID", shopId );
//        context.startActivity( shopIntent );
//
//    }


    // Fragment Transaction...
    private void setFromShopViewFragment(Fragment showFragment){
//        MainActivity.wPreviousFragment = crrFragment;
//        MainActivity.wCurrentFragment = nextFragment;
        FragmentTransaction fragmentTransaction = shopViewFragmentContext.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.slide_from_right, R.anim.slide_out_from_left );
//        onDestroyView();
        fragmentTransaction.replace( ShopsViewFragment.shopViewFragmentFrameLayout.getId(),showFragment );
        fragmentTransaction.commit();
    }

    private void setFromHomeFragment(Fragment showFragment, int nextFragment){
//        MainActivity.wPreviousFragment = currentFragment;
        MainActivity.wCurrentFragment = nextFragment;
        FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.slide_from_right, R.anim.slide_out_from_left );
//        onDestroyView();
        fragmentTransaction.replace( MainActivity.mainHomeContentFrame.getId(),showFragment );
        fragmentTransaction.commit();

    }


}
