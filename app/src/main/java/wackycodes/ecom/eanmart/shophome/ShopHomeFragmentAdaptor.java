package wackycodes.ecom.eanmart.shophome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import wackycodes.ecom.eanmart.MainActivity;
import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.apphome.CategoryTypeModel;
import wackycodes.ecom.eanmart.bannerslider.BannerSliderAdaptor;
import wackycodes.ecom.eanmart.bannerslider.BannerSliderModel;
import wackycodes.ecom.eanmart.category.ShopsViewFragment;
import wackycodes.ecom.eanmart.databasequery.DBQuery;
import wackycodes.ecom.eanmart.other.StaticMethods;
import wackycodes.ecom.eanmart.productdetails.ProductDetails;
import wackycodes.ecom.eanmart.productdetails.ProductModel;
import wackycodes.ecom.eanmart.productdetails.ProductSubModel;
import wackycodes.ecom.eanmart.wcustomview.MyGridView;
import wackycodes.ecom.eanmart.wcustomview.MyImageView;

import static wackycodes.ecom.eanmart.other.StaticValues.SHOP_HOME_CAT_LIST_CONTAINER;
import static wackycodes.ecom.eanmart.other.StaticValues.BANNER_SLIDER_LAYOUT_CONTAINER;
import static wackycodes.ecom.eanmart.other.StaticValues.GRID_ITEM_LAYOUT_CONTAINER;
import static wackycodes.ecom.eanmart.other.StaticValues.GRID_PRODUCT_LAYOUT;
import static wackycodes.ecom.eanmart.other.StaticValues.HORIZONTAL_ITEM_LAYOUT_CONTAINER;
import static wackycodes.ecom.eanmart.other.StaticValues.RECYCLER_PRODUCT_LAYOUT;
import static wackycodes.ecom.eanmart.other.StaticValues.SHOP_ID_CURRENT;
import static wackycodes.ecom.eanmart.other.StaticValues.STRIP_AD_LAYOUT_CONTAINER;
import static wackycodes.ecom.eanmart.other.StaticValues.VIEW_HORIZONTAL_LAYOUT;
import static wackycodes.ecom.eanmart.shophome.ViewAllActivity.gridViewModelListViewAll;
import static wackycodes.ecom.eanmart.shophome.ViewAllActivity.horizontalItemViewModelListViewAll;
import static wackycodes.ecom.eanmart.shophome.ViewAllActivity.totalProductsIdViewAll;

public class ShopHomeFragmentAdaptor extends RecyclerView.Adapter  {

    private List <ShopHomeFragmentModel> homeFragmentModelList;
    private RecyclerView.RecycledViewPool recycledViewPool;

    // Category index Info....
    private int crrShopCatIndex;

    public ShopHomeFragmentAdaptor( int crrShopCatIndex,  List <ShopHomeFragmentModel> homeFragmentModelList) {
        this.crrShopCatIndex = crrShopCatIndex;
        this.homeFragmentModelList = homeFragmentModelList;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getItemViewType(int position) {
        switch (homeFragmentModelList.get( position ).getLayoutType()) {
            case BANNER_SLIDER_LAYOUT_CONTAINER:
                return BANNER_SLIDER_LAYOUT_CONTAINER;
            case STRIP_AD_LAYOUT_CONTAINER:
                return STRIP_AD_LAYOUT_CONTAINER;
            case SHOP_HOME_CAT_LIST_CONTAINER:
                return SHOP_HOME_CAT_LIST_CONTAINER;
            case HORIZONTAL_ITEM_LAYOUT_CONTAINER:
                return HORIZONTAL_ITEM_LAYOUT_CONTAINER;
            case GRID_ITEM_LAYOUT_CONTAINER:
                return GRID_ITEM_LAYOUT_CONTAINER;
            default:
                return -1;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case SHOP_HOME_CAT_LIST_CONTAINER:
                //  : Banner ad Slider
                View bannerAdView = LayoutInflater.from( parent.getContext() ).inflate(
                        R.layout.grid_view_home_layout, parent, false );
                return new ShopCatViewHolder( bannerAdView );
            case BANNER_SLIDER_LAYOUT_CONTAINER:
                // TODO : banner Slider
                View bannerView = LayoutInflater.from( parent.getContext() ).inflate(
                        R.layout.banner_slider_layout, parent, false );
                return new BannerSliderViewHolder( bannerView );
            case STRIP_AD_LAYOUT_CONTAINER:
                // TODO : Strip ad Slider
                View stripAdView = LayoutInflater.from( parent.getContext() ).inflate(
                        R.layout.strip_ad_layout, parent, false );
                return new StripAdViewHolder( stripAdView );
            case HORIZONTAL_ITEM_LAYOUT_CONTAINER:
                // TODO : Horizontal viewHolder
                View horizontalLayoutView = LayoutInflater.from( parent.getContext() ).inflate(
                        R.layout.horizontal_itemview_layout, parent, false );
                return new HorizontalLayoutViewHolder( horizontalLayoutView );
            case GRID_ITEM_LAYOUT_CONTAINER:
                // TODO : GridLayout viewHolder
                View gridLayoutView = LayoutInflater.from( parent.getContext() ).inflate(
                        R.layout.home_itemview_grid_layout, parent, false );
                return new GridLayoutViewHolder( gridLayoutView );
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (homeFragmentModelList.get( position ).getLayoutType()) {
            case SHOP_HOME_CAT_LIST_CONTAINER:
                List<CategoryTypeModel> categoryTypeModelList = homeFragmentModelList.get( position ).getCategoryTypeModelList();
                ((ShopCatViewHolder)holder).setDataGridLayout( categoryTypeModelList );
                break;
            case BANNER_SLIDER_LAYOUT_CONTAINER:
                List <BannerSliderModel> bannerSliderModelList =
                        homeFragmentModelList.get( position ).getBannerSliderModelList();
                ((BannerSliderViewHolder) holder).setBannerSliderViewPager( bannerSliderModelList );
                break;
            case STRIP_AD_LAYOUT_CONTAINER:
                String imgLink = homeFragmentModelList.get( position ).getStripAdImage();
                String colorCode = homeFragmentModelList.get( position ).getStripAdBackground();
                ((StripAdViewHolder)holder).setStripAd( imgLink, colorCode );
                break;
            case HORIZONTAL_ITEM_LAYOUT_CONTAINER:
                List<ProductModel> horizontalItemViewModelList =
                        homeFragmentModelList.get( position ).getProductModelList();
                List<String> hrProductIdList = homeFragmentModelList.get( position ).getHrAndGridProductIdList();
                String layoutTitle = homeFragmentModelList.get( position ).getHorizontalLayoutTitle();
                ((HorizontalLayoutViewHolder)holder).setHorizontalLayout( hrProductIdList ,horizontalItemViewModelList, layoutTitle, position );
                break;
            case GRID_ITEM_LAYOUT_CONTAINER:
                List<ProductModel> gridItemViewModelList =
                        homeFragmentModelList.get( position ).getProductModelList();
                List<String> gridProductIdList = homeFragmentModelList.get( position ).getHrAndGridProductIdList();
                String gridLayoutTitle = homeFragmentModelList.get( position ).getHorizontalLayoutTitle();
                ((GridLayoutViewHolder)holder).setDataGridLayout(gridProductIdList, gridItemViewModelList, gridLayoutTitle, position );
                break;
            default:
                return;
        }

    }

    @Override
    public int getItemCount() {
        return homeFragmentModelList.size();
    }

    //____________________________ View Holder Class _______________________________________________
    //============  Banner Slider View Holder ============
    public class BannerSliderViewHolder extends RecyclerView.ViewHolder {
        //------ View Pager for Banner Slider...
        private ViewPager bannerSliderViewPager;
        private int BANNER_CURRENT_PAGE;
        private final long DELAY_TIME = 3000;
        private final long PERIOD_TIME = 3000;
        private Timer timer;
        private List<BannerSliderModel> arrangedList;
        //------ View Paager for Banner Slidder...

        public BannerSliderViewHolder(@NonNull View itemView) {
            super( itemView );
            bannerSliderViewPager = itemView.findViewById( R.id.banner_slider_viewPager );
        }

        // BannerSlidder ---
        private void setBannerSliderViewPager(final List <BannerSliderModel> bannerSliderModelList) {
            BANNER_CURRENT_PAGE = 2;
            if (timer != null){
                timer.cancel();
            }

            arrangedList = new ArrayList <>();
            for(int i =0; i < bannerSliderModelList.size(); i++){
                arrangedList.add( i, bannerSliderModelList.get( i ) );
            }
            arrangedList.add( 2, bannerSliderModelList.get( bannerSliderModelList.size() - 2 ) );
            arrangedList.add( 1, bannerSliderModelList.get( bannerSliderModelList.size() - 1 ) );
            arrangedList.add( bannerSliderModelList.get( 0 ) );
            arrangedList.add( bannerSliderModelList.get( 1 ) );

            BannerSliderAdaptor bannerSliderAdaptor = new BannerSliderAdaptor( arrangedList );
            bannerSliderViewPager.setAdapter( bannerSliderAdaptor );
            bannerSliderViewPager.setClipToPadding( false );
            bannerSliderViewPager.setPageMargin( 20 );
            bannerSliderViewPager.setCurrentItem( BANNER_CURRENT_PAGE );

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

            //------ View Paager for Banner Slidder...
        }

        private void bannerSlideLooper(List <BannerSliderModel> bannerSliderModelList) {
            if (BANNER_CURRENT_PAGE == bannerSliderModelList.size() - 2) {
                BANNER_CURRENT_PAGE = 2;
                bannerSliderViewPager.setCurrentItem( BANNER_CURRENT_PAGE, false );
            }
            if (BANNER_CURRENT_PAGE == 1) {
                BANNER_CURRENT_PAGE = bannerSliderModelList.size() - 3;
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
        // BannerSlidder ---

    }
    //============  Banner Slider View Holder ============

    //============  Strip and Banner ad  View Holder ============
    public class StripAdViewHolder extends RecyclerView.ViewHolder{
        private MyImageView stripAdImage;
        private ConstraintLayout stripAdContainer;

        public StripAdViewHolder(@NonNull View itemView) {
            super( itemView );
            stripAdContainer = itemView.findViewById(R.id.strip_ad_container);
            stripAdImage = itemView.findViewById( R.id.strip_ad_image );

        }
        private void setStripAd(String imgLink, String colorCode){
//            stripAdContainer.setBackgroundColor( Color.parseColor( colorCode ) );
            // set Image Resouice from database..
            Glide.with( itemView.getContext() ).load( imgLink )
                    .apply( new RequestOptions().placeholder( R.drawable.ic_photo_black_24dp ) ).into( stripAdImage );
        }
    }
    //============  Strip ad  View Holder ============

    //============  ShopCatViewHolder   View Holder ============
    public class ShopCatViewHolder extends RecyclerView.ViewHolder{
        private MyGridView gridLayout;

        public ShopCatViewHolder(@NonNull View itemView) {
            super( itemView );
            gridLayout = itemView.findViewById( R.id.home_cat_grid_view );
        }
        private void setDataGridLayout( List<CategoryTypeModel> categoryTypeModelList ){
            SetCategoryItem setCategoryItem = new SetCategoryItem( categoryTypeModelList );
            gridLayout.setAdapter( setCategoryItem );
            setCategoryItem.notifyDataSetChanged();
        }

        private class SetCategoryItem extends BaseAdapter {
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

                View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.square_cat_item_medium, null );

                ImageView itemImage = view.findViewById( R.id.sq_image_view );
                TextView itemName =  view.findViewById( R.id.sq_text_view );
//                itemImage.setImageResource( Integer.parseInt( categoryTypeModelList.get( position ).getCatImage() ) );
                Glide.with( itemView.getContext() ).load( categoryTypeModelList.get( position ).getCatImage() )
                        .apply( new RequestOptions().placeholder( R.drawable.ic_photo_black_24dp ) ).into( itemImage );
                itemName.setText( categoryTypeModelList.get( position ).getCatName() );

                view.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCategoryClick( itemView.getContext(), categoryTypeModelList.get( position ).getCatId() );
                    }
                } );
                return view;
            }

            private void onCategoryClick(Context context, String docID){
                StaticMethods.showToast( context, "Code not Found!" );
//                Intent intent = new Intent( itemView.getContext(), ShopHomeActivity.class );
//                intent.putExtra( "CAT_ID", docID );
//                itemView.getContext().startActivity( intent );
            }
        }

    }
    //============  ShopCatViewHolder   View Holder ============

    //============  Horizontal Layout View Holder ============
    public class HorizontalLayoutViewHolder extends RecyclerView.ViewHolder{
        private RecyclerView horizontalItemViewRecycler;
        private Button horizontalItemViewAllBtn;
        private TextView horizontalLayoutTitle;
        private List<ProductModel> tempHorizontalList;
        private HorizontalItemViewAdaptor horizontalItemViewAdaptor;

        public HorizontalLayoutViewHolder(@NonNull View itemView) {
            super( itemView );
            horizontalItemViewRecycler = itemView.findViewById( R.id.horizontal_itemView_recycler );
            horizontalLayoutTitle = itemView.findViewById( R.id.horizontal_itemView_title );
            horizontalItemViewAllBtn = itemView.findViewById( R.id.horizontal_item_viewAll_btn );
            // TODO: $ Changes...
//            horizontalItemViewRecycler.setRecycledViewPool( recycledViewPool );
            tempHorizontalList = new ArrayList <>();
        }
        private void setHorizontalLayout(final List<String> hrLayoutProductIdList, final List<ProductModel> hrProductDetailsList,
                                         final String layoutTitle, final int index){
            horizontalLayoutTitle.setText( layoutTitle );

            if (hrLayoutProductIdList.size()>6){
                horizontalItemViewAllBtn.setVisibility( View.VISIBLE );
            }else {
                horizontalItemViewAllBtn.setVisibility( View.INVISIBLE );
            }

            horizontalItemViewAllBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    horizontalItemViewModelListViewAll = new ArrayList <>();
                    totalProductsIdViewAll = new ArrayList <>();
                    if (hrProductDetailsList.size() == 0){
                        horizontalItemViewModelListViewAll = tempHorizontalList;
                    }else{
                        horizontalItemViewModelListViewAll = hrProductDetailsList;
                    }
                    totalProductsIdViewAll = hrLayoutProductIdList;

                    Intent viewAllIntent = new Intent( itemView.getContext(), ViewAllActivity.class);
                    viewAllIntent.putExtra( "LAYOUT_CODE", RECYCLER_PRODUCT_LAYOUT );
                    viewAllIntent.putExtra( "HOME_CAT_INDEX", crrShopCatIndex);
                    viewAllIntent.putExtra( "LAYOUT_INDEX", index );
                    viewAllIntent.putExtra( "TITLE", layoutTitle );
                    itemView.getContext().startActivity( viewAllIntent );
                }
            } );

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager( itemView.getContext() );
            linearLayoutManager.setOrientation( RecyclerView.HORIZONTAL );
            horizontalItemViewRecycler.setLayoutManager( linearLayoutManager );
            // $ Changes...================
            if (hrProductDetailsList.size() == 0){
                if (hrLayoutProductIdList.size() > 6){
                    for (int id_no = 0; id_no < 6; id_no++){
                        loadProductData(index, horizontalItemViewRecycler, hrLayoutProductIdList.get( id_no ));
                    }
                }else{
                    for (int id_no = 0; id_no < hrLayoutProductIdList.size(); id_no++){
                        loadProductData(index, horizontalItemViewRecycler, hrLayoutProductIdList.get( id_no ));
                    }
                }
            }
            else{
                horizontalItemViewAdaptor = new HorizontalItemViewAdaptor( crrShopCatIndex, index, VIEW_HORIZONTAL_LAYOUT, hrProductDetailsList );
                horizontalItemViewRecycler.setAdapter( horizontalItemViewAdaptor );
                horizontalItemViewAdaptor.notifyDataSetChanged();
            }

        }

        private void loadProductData(final int index, final RecyclerView recyclerView, final String productId){

            DBQuery.firebaseFirestore.collection( "SHOPS" ).document( SHOP_ID_CURRENT )
                    .collection( "PRODUCTS" ).document( productId )
                    .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        // access the banners from database...
                        DocumentSnapshot documentSnapshot = task.getResult();

                        if ( documentSnapshot.get( "p_no_of_variants" ) !=null ){
                            String[] pImage;
//                            long p_no_of_variants = (long) documentSnapshot.get( "p_no_of_variants" );
                            int p_no_of_variants = Integer.valueOf( String.valueOf( (long) documentSnapshot.get( "p_no_of_variants" ) ) );
                            List<ProductSubModel> productSubModelList = new ArrayList <>();
                            for (int tempI = 1; tempI <= p_no_of_variants; tempI++){
                                int p_no_of_images = Integer.parseInt( String.valueOf(  (long) task.getResult().get( "p_no_of_images_"+ tempI) ) );
                                pImage = new String[p_no_of_images];
                                for (int tempJ = 0; tempJ < p_no_of_images; tempJ++){
                                    pImage[tempJ] = task.getResult().get( "p_image_"+ tempI +"_"+tempJ ).toString();
                                }

                                // We can use Array...
                               /** String image[]  = (String[]) documentSnapshot.get( "image_" + tempI ); */

                                // add Data...
                                productSubModelList.add( new ProductSubModel(
                                        task.getResult().get( "p_name_"+tempI).toString(),
                                        pImage,
                                        task.getResult().get( "p_selling_price_"+tempI).toString(),
                                        task.getResult().get( "p_mrp_price_"+tempI).toString(),
                                        task.getResult().get( "p_weight_"+tempI).toString(),
                                        task.getResult().get( "p_stocks_"+tempI).toString(),
                                        task.getResult().get( "p_offer_"+tempI).toString()
                                ) );
                            }
                            String p_id = task.getResult().get( "p_id").toString();
                            String p_main_name = task.getResult().get( "p_main_name" ).toString();
//                        String p_main_image = task.getResult().get( "p_main_image" ).toString();
                            String p_weight_type = task.getResult().get( "p_weight_type" ).toString();
                            int p_veg_non_type = Integer.valueOf( task.getResult().get( "p_veg_non_type" ).toString() );
                            Boolean p_is_cod = (Boolean) task.getResult().get( "p_is_cod" );

                            tempHorizontalList.add( new ProductModel(
                                    p_id,
                                    p_main_name,
                                    p_is_cod,
                                    String.valueOf(p_no_of_variants),
                                    p_weight_type,
                                    p_veg_non_type,
                                    productSubModelList
                            ) );

                            homeFragmentModelList.get( index ).setProductModelList( tempHorizontalList );
                            if (recyclerView != null){
                                horizontalItemViewAdaptor = new HorizontalItemViewAdaptor( crrShopCatIndex, index, VIEW_HORIZONTAL_LAYOUT, tempHorizontalList );
                                recyclerView.setAdapter( horizontalItemViewAdaptor );
                                horizontalItemViewAdaptor.notifyDataSetChanged();
                            }
                        }

                    }
                    else{
                        String error = task.getException().getMessage();
//                                    showToast(error);
//                                    dialog.dismiss();
                    }
                }
            } );

        }

    }
    //============  Horizontal Layout View Holder ============

    //==============  GridProduct Grid Layout View Holder =================
    public class GridLayoutViewHolder extends  RecyclerView.ViewHolder{
        private GridLayout gridLayout;
        private TextView gridLayoutTitle;
        private Button gridLayoutViewAllBtn;
        private int temp = 0;
        private List<ProductModel> tempHrGridList;
        public GridLayoutViewHolder(@NonNull View itemView) {
            super( itemView );
            gridLayout = itemView.findViewById( R.id.product_grid_layout );
            gridLayoutTitle = itemView.findViewById( R.id.product_grid__layout_title );
            gridLayoutViewAllBtn = itemView.findViewById( R.id.grid_view_all_btn );
        }
        private void setDataGridLayout(final List<String> gridProductIdList, final List<ProductModel> gridLayoutList
                , final String gridTitle, final int index){
            gridLayoutTitle.setText( gridTitle );
            if (gridLayoutList.size() == 0){
                tempHrGridList = new ArrayList <>();
                for (int i = 0; i < 4; i++ ){
                    loadProductData( i, index,gridProductIdList.get( i ));
                }
            }
            for (int i = 0; i < 4; i++ ){
                if (gridLayoutList.size() != 0){
                    setData( 0, i, index );
                }
                gridLayout.getChildAt( i ).setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent gotoProductDetailIntent = new Intent(itemView.getContext(), ProductDetails.class);
                        if (view == gridLayout.getChildAt( 0 )){
                            if (gridLayoutList.size() != 0){
                                gotoProductDetailIntent.putExtra( "PRODUCT_ID",
                                        homeFragmentModelList.get( index ).getProductModelList().get( 0 ).getpProductID() );
                            }else{
                                gotoProductDetailIntent.putExtra( "PRODUCT_ID",
                                        gridProductIdList.get( 0 ) );
                            }
                            gotoProductDetailIntent.putExtra( "PRODUCT_INDEX", 0 );
                        }else
                        if (view == gridLayout.getChildAt( 1 )){
                            if (gridLayoutList.size() != 0){
                                gotoProductDetailIntent.putExtra( "PRODUCT_ID", gridLayoutList.get( 1 ).getpProductID() );
                            }else{
                                gotoProductDetailIntent.putExtra( "PRODUCT_ID", gridProductIdList.get( 1 ) );
                            }
                            gotoProductDetailIntent.putExtra( "PRODUCT_INDEX", 1 );
                        }else
                        if (view == gridLayout.getChildAt( 2 )){
                            if (gridLayoutList.size() != 0){
                                gotoProductDetailIntent.putExtra( "PRODUCT_ID", gridLayoutList.get( 2 ).getpProductID() );
                            }else{
                                gotoProductDetailIntent.putExtra( "PRODUCT_ID", gridProductIdList.get( 2 ) );
                            }
                            gotoProductDetailIntent.putExtra( "PRODUCT_INDEX", 2 );
                        }else
                        if (view == gridLayout.getChildAt( 3 )){
                            if (gridLayoutList.size() != 0){
                                gotoProductDetailIntent.putExtra( "PRODUCT_ID", gridLayoutList.get( 3 ).getpProductID() );
                            }else{
                                gotoProductDetailIntent.putExtra( "PRODUCT_ID", gridProductIdList.get( 3 ) );
                            }
                            gotoProductDetailIntent.putExtra( "PRODUCT_INDEX", 3 );
                        }
                        gotoProductDetailIntent.putExtra( "HOME_CAT_INDEX", crrShopCatIndex );
                        gotoProductDetailIntent.putExtra( "LAYOUT_INDEX", index );
                        itemView.getContext().startActivity( gotoProductDetailIntent );
                    }
                } );
            }
            if (gridProductIdList.size()>4){
                gridLayoutViewAllBtn.setVisibility( View.VISIBLE );
            }else{
                gridLayoutViewAllBtn.setVisibility( View.INVISIBLE );
            }

            gridLayoutViewAllBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gridViewModelListViewAll = new ArrayList <>();
                    totalProductsIdViewAll = new ArrayList <>();
                    if (gridLayoutList.size() == 0){
                        gridViewModelListViewAll = tempHrGridList;
                    }else{
                        gridViewModelListViewAll = gridLayoutList;
                    }

                    totalProductsIdViewAll.addAll( gridProductIdList );
//                    totalProductsIdViewAll = gridProductIdList;

                    Intent viewAllIntent = new Intent( itemView.getContext(), ViewAllActivity.class);
                    viewAllIntent.putExtra( "LAYOUT_CODE",GRID_PRODUCT_LAYOUT );
                    viewAllIntent.putExtra( "HOME_CAT_INDEX",crrShopCatIndex );
                    viewAllIntent.putExtra( "LAYOUT_INDEX",index );
                    viewAllIntent.putExtra( "TITLE", gridTitle );
                    itemView.getContext().startActivity( viewAllIntent );
                }
            } );
        }
        private void loadProductData(final int i, final int index, final String productId){
            DBQuery.firebaseFirestore.collection( "SHOPS" ).document( SHOP_ID_CURRENT )
                    .collection( "PRODUCTS" ).document( productId )
                    .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        // access the banners from database...
                        String[] pImage;
                        long p_no_of_variants = (long) task.getResult().get( "p_no_of_variants" );
                        List<ProductSubModel> productSubModelList = new ArrayList <>();
                        for (long tempI = 1; tempI <= p_no_of_variants; tempI++){
                            int p_no_of_images = Integer.parseInt( String.valueOf(  (long) task.getResult().get( "p_no_of_images" ) ) );
                            pImage = new String[p_no_of_images];
                            for (int tempJ = 0; tempJ < p_no_of_images; tempJ++){
                                pImage[tempJ] = task.getResult().get( "p_image_"+ tempI +"_"+tempJ ).toString();
                            }
                            // add Data...
                            productSubModelList.add( new ProductSubModel(
                                    task.getResult().get( "p_name_"+tempI).toString(),
                                    pImage,
                                    task.getResult().get( "p_selling_price_"+tempI).toString(),
                                    task.getResult().get( "p_mrp_price_"+tempI).toString(),
                                    task.getResult().get( "p_weight_"+tempI).toString(),
                                    task.getResult().get( "p_stocks_"+tempI).toString(),
                                    task.getResult().get( "p_offer_"+tempI).toString()
                            ) );
                        }
                        String p_id = task.getResult().get( "p_id").toString();
                        String p_main_name = task.getResult().get( "p_main_name" ).toString();
//                        String p_main_image = task.getResult().get( "p_main_image" ).toString();
                        String p_weight_type = task.getResult().get( "p_weight_type" ).toString();
                        int p_veg_non_type = Integer.valueOf( task.getResult().get( "p_veg_non_type" ).toString() );
                        Boolean p_is_cod = (Boolean) task.getResult().get( "p_is_cod" );

                        tempHrGridList.add( new ProductModel(
                                p_id,
                                p_main_name,
                                p_is_cod,
                                String.valueOf(p_no_of_variants),
                                p_weight_type,
                                p_veg_non_type,
                                productSubModelList
                                ) );


                        homeFragmentModelList.get( index ).setProductModelList( tempHrGridList );
                        setData(0, i, index );

                    }
                    else{
                        String error = task.getException().getMessage();
//                                    showToast(error);
//                                    dialog.dismiss();
                    }
                }
            } );

        }

        private void setData(int variant, int childIndex, int index){
            ProductModel productModel = homeFragmentModelList.get( index ).getProductModelList().get( childIndex );

            ImageView img = gridLayout.getChildAt( childIndex ).findViewById( R.id.hr_product_image );
            TextView name = gridLayout.getChildAt( childIndex ).findViewById( R.id.hr_product_name );
            TextView price = gridLayout.getChildAt( childIndex ).findViewById( R.id.hr_product_price );
            TextView cutPrice = gridLayout.getChildAt( childIndex ).findViewById( R.id.hr_product_cut_price );
            TextView perOffText = gridLayout.getChildAt( childIndex ).findViewById( R.id.hr_off_percentage );
            TextView stockText = gridLayout.getChildAt( childIndex ).findViewById( R.id.stock_text );

            name.setText( productModel.getProductSubModelList().get( variant ).getpName() );
            price.setText("Rs." + productModel.getProductSubModelList().get( variant ).getpSellingPrice() +"/-" );
            cutPrice.setText( "Rs."+ productModel.getProductSubModelList().get( variant ).getpMrpPrice() +"/-" );
            // Set img resource
            String[] imageLink = productModel.getProductSubModelList().get( variant ).getpImage();
            Glide.with( itemView.getContext() ).load( imageLink[0] )
                    .apply( new RequestOptions().placeholder( R.drawable.ic_photo_black_24dp ) ).into( img );

            int mrp =  Integer.parseInt( productModel.getProductSubModelList().get( variant ).getpMrpPrice() );
            int showPrice = Integer.parseInt( productModel.getProductSubModelList().get( variant ).getpSellingPrice() );
            int perOff = (( mrp - showPrice )*100)/mrp;
            perOffText.setText( perOff +"% Off" );
            if (Integer.parseInt( productModel.getProductSubModelList().get( variant ).getpStocks() ) > 0){
                stockText.setText("In Stock");
            }else{
                stockText.setText( "Out Of Stock" );
            }
        }

    }
    //==============  GridProduct Grid Layout View Holder =================


}
