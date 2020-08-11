package wackycodes.ecom.eanmart.apphome.mainhome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import wackycodes.ecom.eanmart.MainActivity;
import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.bannerslider.BannerSliderAdaptor;
import wackycodes.ecom.eanmart.bannerslider.BannerSliderModel;
import wackycodes.ecom.eanmart.apphome.category.ShopsViewFragment;
import wackycodes.ecom.eanmart.other.StaticMethods;
import wackycodes.ecom.eanmart.apphome.shophome.ShopHomeActivity;
import wackycodes.ecom.eanmart.wcustomview.MyGridView;
import wackycodes.ecom.eanmart.wcustomview.MyImageView;
import wackycodes.ecom.eanmart.wcustomview.MyViewPagerImageSet;

import static wackycodes.ecom.eanmart.MainActivity.mainActivity;
import static wackycodes.ecom.eanmart.apphome.category.ShopsViewFragment.shopViewFragmentContext;
import static wackycodes.ecom.eanmart.apphome.category.ShopsViewFragment.shopsViewFragment;
import static wackycodes.ecom.eanmart.other.StaticMethods.showToast;
import static wackycodes.ecom.eanmart.other.StaticValues.BANNER_CLICK_TYPE_CATEGORY;
import static wackycodes.ecom.eanmart.other.StaticValues.BANNER_CLICK_TYPE_NONE;
import static wackycodes.ecom.eanmart.other.StaticValues.BANNER_CLICK_TYPE_SHOP;
import static wackycodes.ecom.eanmart.other.StaticValues.BANNER_CLICK_TYPE_WEBSITE;
import static wackycodes.ecom.eanmart.other.StaticValues.FRAGMENT_MAIN_HOME;
import static wackycodes.ecom.eanmart.other.StaticValues.FRAGMENT_MAIN_SHOPS_VIEW;
import static wackycodes.ecom.eanmart.other.StaticValues.LIST_MAIN_HOME_PAGE;
import static wackycodes.ecom.eanmart.other.StaticValues.LIST_SHOP_VIEW_PAGE;
import static wackycodes.ecom.eanmart.other.StaticValues.TYPE_HOME_CATEGORY_LAYOUT;
import static wackycodes.ecom.eanmart.other.StaticValues.TYPE_HOME_SHOP_BANNER_SLIDER;
import static wackycodes.ecom.eanmart.other.StaticValues.TYPE_HOME_SHOP_ITEMS_CONTAINER;
import static wackycodes.ecom.eanmart.other.StaticValues.TYPE_HOME_SHOP_STRIP_AD;

public class MainHomeFragmentAdaptor extends RecyclerView.Adapter {

    private List <MainHomeFragmentModel> mainHomeFragmentModelList;
    private RecyclerView.RecycledViewPool recycledViewPool;

    private int listType;

    public MainHomeFragmentAdaptor(List <MainHomeFragmentModel> mainHomeFragmentModelList, int listType) {
        this.mainHomeFragmentModelList = mainHomeFragmentModelList;
        this.listType = listType;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getItemViewType(int position) {
        switch (mainHomeFragmentModelList.get( position ).getLayoutType()) {
            case TYPE_HOME_SHOP_BANNER_SLIDER:
                return TYPE_HOME_SHOP_BANNER_SLIDER;
            case TYPE_HOME_CATEGORY_LAYOUT:
                return TYPE_HOME_CATEGORY_LAYOUT;
            case TYPE_HOME_SHOP_STRIP_AD:
                return TYPE_HOME_SHOP_STRIP_AD;
            case TYPE_HOME_SHOP_ITEMS_CONTAINER:
                return TYPE_HOME_SHOP_ITEMS_CONTAINER;
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
                // TODO : banner Slider
                View bannerView = LayoutInflater.from( parent.getContext() ).inflate(
                        R.layout.banner_slider_layout, parent, false );
                return new BannerSliderViewHolder( bannerView );
            case TYPE_HOME_CATEGORY_LAYOUT:
            case TYPE_HOME_SHOP_ITEMS_CONTAINER:
                View categoryView = LayoutInflater.from( parent.getContext() ).inflate(
                        R.layout.grid_view_home_layout, parent, false );
                return new GridLayoutViewHolder( categoryView );
            case TYPE_HOME_SHOP_STRIP_AD:
                View adBannerView = LayoutInflater.from( parent.getContext() ).inflate(
                        R.layout.ad_layout_strip_item, parent, false );
                return new ShopAdBannerViewHolder( adBannerView );
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (mainHomeFragmentModelList.get( position ).getLayoutType()) {
            case TYPE_HOME_SHOP_BANNER_SLIDER:
                // TODO: Slider...
                List <BannerSliderModel> bannerSliderModelList =
                        mainHomeFragmentModelList.get( position ).getBannerSliderModelList();
                ((BannerSliderViewHolder) holder).setBannerSliderViewPager( bannerSliderModelList );
                break;
            case TYPE_HOME_CATEGORY_LAYOUT:
            case TYPE_HOME_SHOP_ITEMS_CONTAINER:
                ((GridLayoutViewHolder)holder).setDataGridLayout( mainHomeFragmentModelList.get( position ).getCategoryTypeModelList() );
                break;
            case TYPE_HOME_SHOP_STRIP_AD:
                String imgLink = mainHomeFragmentModelList.get( position ).getBannerImage();
                String nameOrColor = mainHomeFragmentModelList.get( position ).getBannerExtraText();
                String id = mainHomeFragmentModelList.get( position ).getBannerClickID();
                int bannerViewFragmentType = mainHomeFragmentModelList.get( position ).getBannerClickType();
                ((ShopAdBannerViewHolder)holder).setBannerAd( id, imgLink, nameOrColor, bannerViewFragmentType );
                break;
            default:
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mainHomeFragmentModelList.size();
    }

    //============  Banner Slider View Holder ============
    public class BannerSliderViewHolder extends RecyclerView.ViewHolder {
        //------ View Pager for Banner Slider...
//        private ViewPager bannerSliderViewPager;
        private MyViewPagerImageSet bannerSliderViewPager;
        private TabLayout sliderIndicator;
        private int BANNER_CURRENT_PAGE;
        private final long DELAY_TIME = 1500;
        private final long PERIOD_TIME = 2000;
        private Timer timer;
        private List<BannerSliderModel> arrangedList;
        //------ View Pager for Banner Slider...

        public BannerSliderViewHolder(@NonNull View itemView) {
            super( itemView );
//            bannerSliderViewPager = itemView.findViewById( R.id.banner_slider_viewPager );
            bannerSliderViewPager = itemView.findViewById( R.id.banner_slider_viewPagers );
            sliderIndicator = itemView.findViewById( R.id.slider_indicator );
        }

        //  ---BannerSliderModel
        private void setBannerSliderViewPager(final List <BannerSliderModel> bannerSliderModelList) {
//            BANNER_CURRENT_PAGE = 2;
            BANNER_CURRENT_PAGE = 1;
            if (timer != null){
                timer.cancel();
            }

            arrangedList = new ArrayList <>();
            for(int i =0; i < bannerSliderModelList.size(); i++){
                arrangedList.add( i, bannerSliderModelList.get( i ) );
            }
//            arrangedList.add( 2, bannerSliderModelList.get( bannerSliderModelList.size() - 2 ) );
//            arrangedList.add( 1, bannerSliderModelList.get( bannerSliderModelList.size() - 1 ) );
//            arrangedList.add( bannerSliderModelList.get( 0 ) );
//            arrangedList.add( bannerSliderModelList.get( 1 ) );
            // Wacky Codes...
            if (bannerSliderModelList.size()>1){
                arrangedList.add( 0, bannerSliderModelList.get( bannerSliderModelList.size() - 1 ) ); // Add Last Index in 0
                arrangedList.add( bannerSliderModelList.get( 0 ) ); // Add 0 index in last...
            }else{
                arrangedList.add( bannerSliderModelList.get( 0 ) );
            }
            // Wacky Codes...

            BannerSliderAdaptor bannerSliderAdaptor = new BannerSliderAdaptor( arrangedList, listType );
            bannerSliderViewPager.setAdapter( bannerSliderAdaptor );
            bannerSliderViewPager.setClipToPadding( false );
//            bannerSliderViewPager.setPageMargin( 10 );
            bannerSliderViewPager.setCurrentItem( BANNER_CURRENT_PAGE );
            sliderIndicator.setupWithViewPager( bannerSliderViewPager, true );
            // if Size greater than 2...
            if (bannerSliderModelList.size()>1){
                ViewPager.OnPageChangeListener viewPagerOnPageChange = new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        BANNER_CURRENT_PAGE = position;
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        if (state == ViewPager.SCROLL_STATE_IDLE) {
                            bannerSlideLooper( arrangedList );
                        }
                    }
                };
                bannerSliderViewPager.addOnPageChangeListener( viewPagerOnPageChange );
                startBannerSliderAnim( arrangedList );
            }

            bannerSliderViewPager.setOnTouchListener( new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    bannerSlideLooper( arrangedList );
                    stopBannerSliderAnim();
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        startBannerSliderAnim( arrangedList );
                    }
                    return false;
                }
            } );

            bannerSliderViewPager.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO : OnClick Banner...
                }
            } );

            //------ View Pager for Banner Slider...
        }

        private void bannerSlideLooper(List <BannerSliderModel> bannerSliderModelList) {
//            if (BANNER_CURRENT_PAGE == bannerSliderModelList.size() - 2) {
//                BANNER_CURRENT_PAGE = 2;
//                bannerSliderViewPager.setCurrentItem( BANNER_CURRENT_PAGE, false );
//            }
//            if (BANNER_CURRENT_PAGE == 1) {
//                BANNER_CURRENT_PAGE = bannerSliderModelList.size() - 3;
//                bannerSliderViewPager.setCurrentItem( BANNER_CURRENT_PAGE, false );
//            }
            // Wacky Codes...
            if (BANNER_CURRENT_PAGE == bannerSliderModelList.size() - 1) {
                BANNER_CURRENT_PAGE = 1;
                bannerSliderViewPager.setCurrentItem( BANNER_CURRENT_PAGE, false );
            }
            if (BANNER_CURRENT_PAGE == 0) {
                BANNER_CURRENT_PAGE = bannerSliderModelList.size() - 2;
                bannerSliderViewPager.setCurrentItem( BANNER_CURRENT_PAGE, false );
            }
        }

        private void startBannerSliderAnim(final List <BannerSliderModel> bannerSliderModelList) {
            final Handler handler = new Handler();
            final Runnable update = new Runnable() {
                @Override
                public void run() {
                    if (BANNER_CURRENT_PAGE > bannerSliderModelList.size()) {
                        BANNER_CURRENT_PAGE = 1;
                    }
                    bannerSliderViewPager.setCurrentItem( BANNER_CURRENT_PAGE++, true );
                }
            };
            timer = new Timer();
            timer.schedule( new TimerTask() {
                @Override
                public void run() {
                    handler.post( update );
                }
            }, DELAY_TIME, PERIOD_TIME );
        }

        private void stopBannerSliderAnim() {
            timer.cancel();
        }
        // BannerSlider ---

    }

    //============  Banner Slider View Holder ============

    //============ Strip and Banner Banner ad  View Holder ============
    public class ShopAdBannerViewHolder extends RecyclerView.ViewHolder{
        private MyImageView bannerAdImage;
        private ConstraintLayout bannerAdContainer;

        public ShopAdBannerViewHolder(@NonNull View itemView) {
            super( itemView );
            bannerAdContainer = itemView.findViewById(R.id.strip_ad_container);
            bannerAdImage = itemView.findViewById( R.id.strip_ad_image );
        }
        private void setBannerAd(final String clickId, String imgLink, String extraText, final int clickType){
//            bannerAdContainer.setBannerOtherText( Color.parseColor( colorCode ) );
            // set Image Resource from database..
            Glide.with( itemView.getContext() ).load( imgLink )
                    .apply( new RequestOptions().placeholder( R.drawable.ic_strip_black_24dp ) ).into( bannerAdImage );

//            bannerAdImage.setImageResource( R.drawable.strip_ad_a );
            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (clickType){
                        case BANNER_CLICK_TYPE_NONE:
                            // No Action..
                            break;
                        case BANNER_CLICK_TYPE_WEBSITE:
                            // Click And GO to WebSite...
                            StaticMethods.gotoURL( itemView.getContext(), clickId );
                            break;
                        case BANNER_CLICK_TYPE_SHOP:
                            // Open Shop...
                            Intent intent = new Intent( itemView.getContext(), ShopHomeActivity.class );
                            intent.putExtra( "SHOP_ID", clickId );
                            itemView.getContext().startActivity( intent );
                            break;
                        case BANNER_CLICK_TYPE_CATEGORY:
                            // Open Category Item...
                            if (listType == LIST_MAIN_HOME_PAGE){
                                // GO TO Any Category...in shop view....
                                MainActivity.wPreviousFragment = FRAGMENT_MAIN_HOME;
                                if (shopsViewFragment == null || !ShopsViewFragment.catID.equals( clickId )){
                                    shopsViewFragment = new ShopsViewFragment(clickId);
                                }
                                setFromHomeFragment(shopsViewFragment , FRAGMENT_MAIN_SHOPS_VIEW );

                            }else if(listType == LIST_SHOP_VIEW_PAGE){
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
//                itemImage.setImageResource( Integer.parseInt( categoryTypeModelList.get( position ).getCatImage() ) );
                Glide.with( itemView.getContext() ).load( categoryTypeModelList.get( position ).getCatImage() )
                        .apply( new RequestOptions().placeholder( R.drawable.ic_photo_black_24dp ) ).into( itemImage );
                itemName.setText( categoryTypeModelList.get( position ).getCatName() );

                view.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCategoryClick( parent.getContext(), categoryTypeModelList.get( position ).getCatId() );
                    }
                } );
                return view;
            }

            private void onCategoryClick(Context context, String docID){

                switch (listType){
                    case LIST_MAIN_HOME_PAGE:
                        MainActivity.wPreviousFragment = FRAGMENT_MAIN_HOME;
                        if (shopsViewFragment == null || !ShopsViewFragment.catID.equals( docID )){
                            shopsViewFragment = new ShopsViewFragment(docID);
                        }
                        setFromHomeFragment(shopsViewFragment , FRAGMENT_MAIN_SHOPS_VIEW );
                        break;
                    case LIST_SHOP_VIEW_PAGE:
                        if (docID != null){
                            Intent intent = new Intent( itemView.getContext(), ShopHomeActivity.class );
                            intent.putExtra( "SHOP_ID", docID );
                            itemView.getContext().startActivity( intent );
                        }else{
                            showToast( itemView.getContext(), "Shop ID not found!" );
                        }
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
        fragmentTransaction.replace( MainActivity.mainHomeContentFrame.getId(),showFragment );
        fragmentTransaction.commit();
    }



}
