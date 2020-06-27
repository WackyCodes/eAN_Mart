package wackycodes.ecom.eanmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import wackycodes.ecom.eanmart.apphome.HomeFragment;
import wackycodes.ecom.eanmart.category.ShopItemModel;
import wackycodes.ecom.eanmart.category.ShopListAdaptor;
import wackycodes.ecom.eanmart.cityareacode.AreaCodeCityModel;
import wackycodes.ecom.eanmart.cityareacode.SelectAreaCityAdaptor;
import wackycodes.ecom.eanmart.databasequery.DBQuery;
import wackycodes.ecom.eanmart.databasequery.UserDataQuery;
import wackycodes.ecom.eanmart.other.DialogsClass;
import wackycodes.ecom.eanmart.other.StaticValues;
import wackycodes.ecom.eanmart.userprofile.UserProfileActivity;

import static wackycodes.ecom.eanmart.apphome.HomeFragment.homeFragment;
import static wackycodes.ecom.eanmart.category.ShopsViewFragment.shopsViewFragment;
import static wackycodes.ecom.eanmart.databasequery.DBQuery.currentUser;
import static wackycodes.ecom.eanmart.databasequery.DBQuery.firebaseAuth;
import static wackycodes.ecom.eanmart.databasequery.DBQuery.firebaseFirestore;
import static wackycodes.ecom.eanmart.databasequery.DBQuery.homePageCategoryList;
import static wackycodes.ecom.eanmart.databasequery.DBQuery.shopsViewFragmentList;
import static wackycodes.ecom.eanmart.other.StaticValues.CURRENT_CITY_CODE;
import static wackycodes.ecom.eanmart.other.StaticValues.CURRENT_CITY_NAME;
import static wackycodes.ecom.eanmart.other.StaticValues.FRAGMENT_MAIN_HOME;
import static wackycodes.ecom.eanmart.other.StaticValues.FRAGMENT_MAIN_SHOPS_VIEW;
import static wackycodes.ecom.eanmart.other.StaticValues.FRAGMENT_NULL;
import static wackycodes.ecom.eanmart.other.StaticValues.MAIN_ACTIVITY;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static AppCompatActivity mainActivity;

    public static SelectAreaCityAdaptor selectAreaCityAdaptor;
    public static int wCurrentFragment = FRAGMENT_NULL;
    public static int wPreviousFragment = FRAGMENT_NULL;

    public static FrameLayout mainHomeContentFrame;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    public static TextView badgeCartCount;
    public static TextView badgeNotifyCount;
    // Drawer...User info
    public static CircleImageView drawerImage;
    public static TextView drawerName;
    public static TextView drawerEmail;
    public static LinearLayout drawerCityLayout;
    public static TextView drawerCityTitle;
    public static TextView drawerCityName;

    // Search Variables...
    private SearchView homeMainSearchView;
    private RecyclerView homeSearchItemRecycler;
    private TextView searchPlease;
    private static List <ShopItemModel> searchShopItemList = new ArrayList <>();
    private List<String> searchShopTags = new ArrayList <>();
    private SearchAdapter searchAdaptor;
    // Search Variables...

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        mainActivity = this;
        dialog = DialogsClass.getDialog( this );
        // assign..
        mainHomeContentFrame = findViewById( R.id.main_content_frame_layout );

        toolbar = findViewById( R.id.appToolbar );
        drawer = findViewById( R.id.drawer_layout );
        navigationView = findViewById( R.id.nav_view );
        setSupportActionBar( toolbar );
        try {
            getSupportActionBar().setDisplayShowTitleEnabled( true );
        }catch (NullPointerException ignored){ }

        // Nav Header...
        drawerImage = navigationView.getHeaderView( 0 ).findViewById( R.id.drawer_Image );
        drawerName = navigationView.getHeaderView( 0 ).findViewById( R.id.drawer_UserName );
        drawerEmail = navigationView.getHeaderView( 0 ).findViewById( R.id.drawer_userEmail );
        drawerCityLayout = navigationView.getHeaderView( 0 ).findViewById( R.id.drawer_user_city_layout );
        drawerCityTitle = navigationView.getHeaderView( 0 ).findViewById( R.id.drawer_title_text );
        drawerCityName = navigationView.getHeaderView( 0 ).findViewById( R.id.drawer_user_city );

        // Set Drawer City Click listener...
        drawerCityLayout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen( GravityCompat.START )){
                    drawer.closeDrawer( GravityCompat.START );
                }
                // select city Dialog...
                selectCityDialog();
            }
        } );

        // setNavigationItemSelectedListener()...
        navigationView.setNavigationItemSelectedListener( MainActivity.this );
        navigationView.getMenu().getItem( 0 ).setChecked( true );

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this,drawer,toolbar,
                R.string.navigation_Drawer_Open,R.string.navigation_Drawer_close);
        drawer.addDrawerListener( toggle );
        toggle.syncState();

        // CurrentUser Check...
        if(currentUser != null){
            drawerCityTitle.setText("Your City");
            drawerCityName.setText( CURRENT_CITY_NAME );
        }

        // Assigning and Set fragment...
        if (homeFragment!=null){
            setNextFragment( homeFragment, FRAGMENT_MAIN_HOME );
        }else{
            homeFragment = new HomeFragment();
            setNextFragment( homeFragment, FRAGMENT_MAIN_HOME );
        }

        // SearchView...
        homeMainSearchView = findViewById( R.id.home_shop_search_view );
        homeSearchItemRecycler = findViewById( R.id.home_search_recycler_view );

        LinearLayoutManager searchLinearLManager = new LinearLayoutManager( this );
        searchLinearLManager.setOrientation( RecyclerView.VERTICAL );
        homeSearchItemRecycler.setLayoutManager( searchLinearLManager );
        searchAdaptor = new SearchAdapter( searchShopItemList );
        homeSearchItemRecycler.setAdapter( searchAdaptor );
        getShopSearchItems();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (homePageCategoryList.size() == 0 && CURRENT_CITY_CODE !=null){
            DBQuery.getHomePageCategoryListQuery( CURRENT_CITY_CODE, false );
        }
        if ( currentUser != null && StaticValues.USER_DATA_MODEL.isLoadData()){
            Glide.with( this ).load( StaticValues.USER_DATA_MODEL.getUserProfilePhoto() ).
                    apply( new RequestOptions().placeholder( R.drawable.ic_account_circle_gray_24dp) ).into( drawerImage );
            drawerName.setText( StaticValues.USER_DATA_MODEL.getUserFullName() );
            drawerEmail.setText( StaticValues.USER_DATA_MODEL.getUserEmail() );
        }
        if (wCurrentFragment == FRAGMENT_MAIN_HOME ){ // && wPreviousFragment == FRAGMENT_MAIN_HOME
            wPreviousFragment = FRAGMENT_NULL;
            if (homeFragment!=null){
                setNextFragment( homeFragment, FRAGMENT_MAIN_HOME );
            }else{
                homeFragment = new HomeFragment();
                setNextFragment( homeFragment, FRAGMENT_MAIN_HOME );
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen( GravityCompat.START )){
            drawer.closeDrawer( GravityCompat.START );
        }
        else if (homeSearchItemRecycler.getVisibility() == View.VISIBLE){
            setFrameVisibility(true);
        }
        else {
            switch (wCurrentFragment){
                case FRAGMENT_MAIN_HOME:
                    super.onBackPressed();
                    finish();
                    break;
                case FRAGMENT_MAIN_SHOPS_VIEW:
                    wPreviousFragment = FRAGMENT_NULL;
                    if (homeFragment==null){
                        homeFragment = new HomeFragment();
                    }
                    setBckFragment( homeFragment );
//                    boolean handle = shopsViewFragment.onBackPressed();
//                    if (handle){
//                        break;
//                    }else{
//                        wPreviousFragment = FRAGMENT_NULL;
//                        if (homeFragment==null){
//                            homeFragment = new HomeFragment();
//                        }
//                        setBckFragment( homeFragment );
//                    }
                    break;
                default:
                    super.onBackPressed();
                    break;
            }
            /**
//            switch (wPreviousFragment){
//                case FRAGMENT_NULL:
//                    // No Previous fragment : Current Fragment is Main Home Fragment...
//                    wCurrentFragment = FRAGMENT_NULL;
//                    super.onBackPressed();
//                    finish();
//                    break;
//                case FRAGMENT_MAIN_HOME:
//                    wPreviousFragment = FRAGMENT_NULL;
//                    if (homeFragment==null){
//                        homeFragment = new HomeFragment();
//                    }
//                    setBckFragment( homeFragment );
//                    break;
//                case FRAGMENT_MAIN_SHOPS_VIEW:
//                    wPreviousFragment = FRAGMENT_MAIN_HOME;
//                    wCurrentFragment = FRAGMENT_MAIN_SHOPS_VIEW;
//                    if (shopsViewFragment == null){
//                        shopsViewFragment = new ShopsViewFragment();
//                    }
//                    FragmentTransaction fragmentTransaction = shopHomeFragmentContext.getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.setCustomAnimations( R.anim.slide_from_left, R.anim.slide_out_from_right );
//                    fragmentTransaction.replace( ShopHomeFragment.shopHomeFrameLayout.getId(),shopsViewFragment );
//                    fragmentTransaction.commit();
//                    break;
//                default:
//                    super.onBackPressed();
//                    break;
//            }
             */
        }

    }

    // --------  Menu And Navigation....

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate( R.menu.menu_main_home_header,menu);

            // Check First whether any item in cart or not...
            // if any item has in cart...
            MenuItem cartItem = menu.findItem( R.id.menu_cart_main );
            cartItem.setActionView( R.layout.badge_cart_layout );
            badgeCartCount = cartItem.getActionView().findViewById( R.id.badge_count_text );
            if (UserDataQuery.cartItemModelList.size() > 0){
                badgeCartCount.setVisibility( View.VISIBLE );
                badgeCartCount.setText( String.valueOf( UserDataQuery.cartItemModelList.size() ) );
            }
            cartItem.getActionView().setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // GOTO : My cart
                    if (currentUser == null){
                        DialogsClass.signInUpDialog( MainActivity.this, MAIN_ACTIVITY );
                    }else{
//                        myCart();// TODO : Open My Cart Activity....
                    }
                }
            } );

            // notification badge...
            MenuItem notificationItem = menu.findItem( R.id.menu_notification_main );
            notificationItem.setActionView( R.layout.badge_notification_layout );
            badgeNotifyCount = notificationItem.getActionView().findViewById( R.id.badge_count );
            if (currentUser != null){
                // Run user Notification Query to update...
                UserDataQuery.loadNotificationsQuery( MainActivity.this );
            }
            notificationItem.getActionView().setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent catIntent = new Intent( MainActivity.this, NotificationActivity.class);
//                    startActivity( catIntent );
                    showToast("Code Not found.!");
                }
            } );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
//            if (isFragmentIsMyCart){
////                setFragment( new HomeFragment(), M_HOME_FRAGMENT );
//                wCurrentFragment = M_HOME_FRAGMENT;
//                navigationView.getMenu().getItem( 0 ).setChecked( true );
//                mainActivityForCart.finish();
//            }
            return true;
        }
        if (id == R.id.menu_cart_main){
            // GOTO : My cart
            if (currentUser == null){
                DialogsClass.signInUpDialog( MainActivity.this, MAIN_ACTIVITY );
                return false;
            }else{
//                myCart();
                return true;
            }
        } else
        if (id == R.id.menu_notification_main){
            // GOTO : Notification Activity -- // CatTypeMobileRecycler
            // written code... in onCreateOptionsMenu() method.
            return true;
        } else

        return super.onOptionsItemSelected( item );
    }

    int mainNavItemId;
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawer.closeDrawer( GravityCompat.START );

        mainNavItemId = menuItem.getItemId();

        // ------- On Item Click...
        // Home Nav Option...
        if ( mainNavItemId == R.id.nav_home ){
            // index - 0
            invalidateOptionsMenu();
            getSupportActionBar().setTitle( R.string.app_name );
            setNextFragment( homeFragment, FRAGMENT_MAIN_HOME );
            return true;
        }else
            if (mainNavItemId == R.id.menu_my_account){
                startActivity( new Intent( MainActivity.this, UserProfileActivity.class ) );
            }else
            // Bottom Options...
            if ( mainNavItemId == R.id.menu_log_out ){
                // index - 5
                if (currentUser != null){
                    // TODO : Show Dialog to logOut..!
                    // Sign Out Dialog...
                    final Dialog signOut = new Dialog( MainActivity.this );
                    signOut.requestWindowFeature( Window.FEATURE_NO_TITLE );
                    signOut.setContentView( R.layout.dialig_sign_out );
                    signOut.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
                    signOut.setCancelable( false );
                    ImageView imageView = signOut.findViewById( R.id.sign_out_image );
                    Glide.with( this ).load( "sample" ).apply( new RequestOptions().placeholder( R.drawable.ic_account_circle_gray_24dp ) ).into( imageView );
                    final Button signOutOkBtn = signOut.findViewById( R.id.sign_out_ok_btn );
                    Button signOutCancelBtn = signOut.findViewById( R.id.sign_out_cancel_btn );
                    signOut.show();

                    signOutOkBtn.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            signOutOkBtn.setEnabled( false );
                            firebaseAuth.signOut();
                            currentUser = null;
                            wCurrentFragment = 0;
                            navigationView.getMenu().getItem( 0 ).setChecked( true );
                            navigationView.getMenu().getItem( 5 ).setEnabled( false );
//                            if(isFragmentIsMyCart){
//                                isFragmentIsMyCart = false;
//                                Intent finishIntent = new Intent(MainActivity.mainActivityForCart, RegisterActivity.class );
//                                startActivity( finishIntent );
//                                Toast.makeText( MainActivity.mainActivityForCart,"Sign Out successfully..!",Toast.LENGTH_SHORT).show();
//                                MainActivity.mainActivityForCart.finish();
//                            }else {
//                                Intent finishIntent = new Intent(MainActivity.mainActivity, RegisterActivity.class );
//                                startActivity( finishIntent );
//                                Toast.makeText( MainActivity.mainActivity,"Sign Out successfully..!",Toast.LENGTH_SHORT).show();
//                            }
//                            RegisterActivity.setFragmentRequest = -1;
//                            RegisterActivity.comeFromActivity = -1;
//                            MainActivity.mainActivity.finish();
                            signOut.dismiss();
                        }
                    } );
                    signOutCancelBtn.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            signOut.dismiss();
                            // TODO : Sign Out
                        }
                    } );

                    return false;
                }
            }else
            if ( mainNavItemId == R.id.menu_help ){
                // index - 6
//            Intent comIntent =  new Intent(MainActivity.this, CommunicateActivity.class);
//            comIntent.putExtra( "MENU_TYPE", "HELP" );
//            startActivity( comIntent );

                return false;
            }
            return false;
    }

    // --------  Menu And Navigation !....
    private void showToast(String msg){
        Toast.makeText( mainActivity, msg, Toast.LENGTH_SHORT ).show();
    }

    // Fragment Transaction...
    public void setNextFragment(Fragment fragment, int nextFragment){
        wPreviousFragment = wCurrentFragment;
        wCurrentFragment = nextFragment;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.fade_in, R.anim.fade_out );
        fragmentTransaction.replace( mainHomeContentFrame.getId(),fragment );
        fragmentTransaction.commit();
    }
    public void setBckFragment( Fragment showFragment ){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.slide_from_left, R.anim.slide_out_from_right );
        fragmentTransaction.replace( mainHomeContentFrame.getId(),showFragment );
        fragmentTransaction.commit();
//        FragmentTransaction fragmentTransaction =  shopViewFragmentContext.getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.setCustomAnimations( R.anim.slide_from_left, R.anim.slide_out_from_right );
//        fragmentTransaction.replace( ShopsViewFragment.shopViewFragmentFrameLayout.getId(),showFragment );
//        fragmentTransaction.commit();
        wCurrentFragment = FRAGMENT_MAIN_HOME;

    }

    // Select City Dialog...
    private AreaCodeCityModel tempAreaCodeCityModel = null;
    private void selectCityDialog(){
        // TODO :
        tempAreaCodeCityModel=null;
        /// Sample Button click...
        final Dialog cityDialog = new Dialog( this );
        cityDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        cityDialog.setContentView( R.layout.dialog_select_city_dialog );
        cityDialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        cityDialog.setCancelable( false );
        Button cancelBtn = cityDialog.findViewById( R.id.dialog_cancel_btn );
        Button selectBtn = cityDialog.findViewById( R.id.dialog_select_btn );
        final AutoCompleteTextView cityText = cityDialog.findViewById( R.id.dialogEditText );
        ArrayList<AreaCodeCityModel> areaCodeCityModelArrayList = new ArrayList <>();
        areaCodeCityModelArrayList.addAll( DBQuery.areaCodeCityModelList );
//        showToast( "Size : "+ areaCodeCityModelArrayList.size() );
        selectAreaCityAdaptor =
                new SelectAreaCityAdaptor(MainActivity.this, R.layout.area_select_list_item, areaCodeCityModelArrayList);

        cityText.setThreshold(1);
        cityText.setAdapter(selectAreaCityAdaptor);

        // handle click event and set desc on textView
        cityText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AreaCodeCityModel areaCodeCityModel = (AreaCodeCityModel) adapterView.getItemAtPosition(i);
                cityText.setText( areaCodeCityModel.getAreaCode() );
                tempAreaCodeCityModel = areaCodeCityModel;
            }
        });

        selectBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check
                if (tempAreaCodeCityModel!=null){
                    cityDialog.dismiss();
                    drawerCityTitle.setText( "Your City" );
                    drawerCityName.setText( tempAreaCodeCityModel.getAreaCode() + ", " + tempAreaCodeCityModel.getCityName() );
//                    showToast( "CityName : " +tempAreaCodeCityModel.getCityName() + " Code : " + tempAreaCodeCityModel.getAreaCode() );
                    // TODO : Reload Product...
//                     loadMainHomePageAgain(tempAreaCodeCityModel.getCityCode());
                }else{
                    cityText.setError( "pin code / City not found!" );
                }

            }
        } );
        cancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cityDialog.dismiss();
            }
        } );

        cityDialog.show();

    }

    // load Main Home again...
    public void loadMainHomePageAgain( String cityCode ){
        DBQuery.getHomePageCategoryListQuery( cityCode, true );
        finishAllActivity();
        if (wCurrentFragment == FRAGMENT_MAIN_SHOPS_VIEW){
            wPreviousFragment = FRAGMENT_NULL;
            homeFragment = new HomeFragment();
            setBckFragment( homeFragment );
        }
    }

    public void finishAllActivity(){
        // TODO : Finishing upper activities...
    }

    private void getShopSearchItems(){
        // Search Methods...
        homeMainSearchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                dialog.show();
                searchShopItemList.clear();
                searchShopTags.clear();
                final String [] tags = query.toLowerCase().split( " " );
                for ( final String tag : tags ){
                    firebaseFirestore
                            .collection( "HOME_PAGE" )
                            .document( CURRENT_CITY_CODE.toUpperCase() )
                            .collection( "SHOPS" )
                            .whereArrayContainsAny( "tags", Arrays.asList( tags ) )
//                            .whereArrayContains( "tags", tag.trim() )
                            .get().addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task <QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                                    ShopItemModel model = new ShopItemModel(
                                            documentSnapshot.getId(),
                                            documentSnapshot.get( "shop_name" ).toString(),
                                            documentSnapshot.get( "shop_image" ).toString(),
                                            documentSnapshot.get( "shop_rating" ).toString(),
                                            documentSnapshot.get( "shop_category" ).toString(),
                                            Integer.parseInt(
                                                    String.valueOf((long)documentSnapshot.get( "shop_veg_type" ) )
                                            )
                                    );
                                    searchShopItemList.add( model );
                                    if ( !searchShopTags.contains( model.getShopID() )){
                                        searchShopItemList.add( model );
                                        searchShopTags.add( model.getShopID() );
                                    }

                                }
                                if (searchShopItemList.size()>0){
                                    setFrameVisibility(false);
                                }else{
                                    setFrameVisibility(true);
                                }
                                if (tag.equals(tags[tags.length - 1])){
                                    if (searchShopTags.isEmpty()){
                                        DialogsClass.alertDialog( MainActivity.this, null, "No Shop found.!" ).show();
                                        setFrameVisibility(true);
                                    }else{
                                        searchAdaptor.getFilter().filter( query );
                                    }
                                    dialog.dismiss();
                                }
                                dialog.dismiss();
                            }else{
                                // error...
                                dialog.dismiss();
                                showToast(  "Failed ! Product Not found.!" );
                                setFrameVisibility(true);
                            }
                        }
                    } );
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        } );
        homeMainSearchView.setOnCloseListener( new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                setFrameVisibility(true);
                return false;
            }
        } );

    }
    private void setFrameVisibility(boolean isVisible){
        if (isVisible){
            mainHomeContentFrame.setVisibility( View.VISIBLE );
            homeSearchItemRecycler.setVisibility( View.GONE );
        }else{
            mainHomeContentFrame.setVisibility( View.GONE );
            homeSearchItemRecycler.setVisibility( View.VISIBLE );
        }
    }

    class SearchAdapter extends ShopListAdaptor implements Filterable {

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



}
