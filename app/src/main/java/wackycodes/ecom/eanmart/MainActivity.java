package wackycodes.ecom.eanmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;
import wackycodes.ecom.eanmart.apphome.HomeFragment;
import wackycodes.ecom.eanmart.category.ShopsViewFragment;

import static wackycodes.ecom.eanmart.apphome.HomeFragment.homeFragment;
import static wackycodes.ecom.eanmart.category.ShopsViewFragment.shopsViewFragment;
import static wackycodes.ecom.eanmart.databasequery.DBQuery.currentUser;
import static wackycodes.ecom.eanmart.databasequery.DBQuery.firebaseAuth;
import static wackycodes.ecom.eanmart.other.StaticValues.FRAGMENT_MAIN_HOME;
import static wackycodes.ecom.eanmart.other.StaticValues.FRAGMENT_MAIN_SHOPS_VIEW;
import static wackycodes.ecom.eanmart.other.StaticValues.FRAGMENT_NULL;
import static wackycodes.ecom.eanmart.other.StaticValues.MAIN_ACTIVITY;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static AppCompatActivity mainActivity;

    public static int wCurrentFragment = FRAGMENT_NULL;
    public static int wPreviousFragment = FRAGMENT_NULL;
    public static boolean isFragmentIsMyCart = false;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        mainActivity = this;

        // assign..
        mainHomeContentFrame = findViewById( R.id.main_content_frame_layout );

        toolbar = findViewById( R.id.appToolbar );
        drawer = findViewById( R.id.drawer_layout );
        navigationView = findViewById( R.id.nav_view );
        setSupportActionBar( toolbar );

        // Nav Header...
        drawerImage = navigationView.getHeaderView( 0 ).findViewById( R.id.drawer_Image );
        drawerName = navigationView.getHeaderView( 0 ).findViewById( R.id.drawer_UserName );
        drawerEmail = navigationView.getHeaderView( 0 ).findViewById( R.id.drawer_userEmail );

        try {
            getSupportActionBar().setDisplayShowTitleEnabled( true );
        }catch (NullPointerException ignored){ }

        // setNavigationItemSelectedListener()...
        navigationView.setNavigationItemSelectedListener( MainActivity.this );
        navigationView.getMenu().getItem( 0 ).setChecked( true );

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this,drawer,toolbar,
                R.string.navigation_Drawer_Open,R.string.navigation_Drawer_close);
        drawer.addDrawerListener( toggle );
        toggle.syncState();



        // Assigning and Set fragment...
        if (homeFragment!=null){
            setNextFragment( homeFragment, FRAGMENT_MAIN_HOME );
        }else{
            homeFragment = new HomeFragment();
            setNextFragment( homeFragment, FRAGMENT_MAIN_HOME );
        }

    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen( GravityCompat.START )){
            drawer.closeDrawer( GravityCompat.START );
        }else{
            switch (wPreviousFragment){
                case FRAGMENT_NULL:
                    // No Previous fragment : Current Fragment is Main Home Fragment...
                    wCurrentFragment = FRAGMENT_NULL;
                    super.onBackPressed();
                    finish();
                    break;
                case FRAGMENT_MAIN_HOME:
                    wPreviousFragment = FRAGMENT_NULL;
                    wCurrentFragment = FRAGMENT_MAIN_HOME;
                    if (homeFragment!=null){
                        setFragment( homeFragment );
                    }else{
                        homeFragment = new HomeFragment();
                        setFragment( homeFragment );
                    }
                    break;
                case FRAGMENT_MAIN_SHOPS_VIEW:
                    wPreviousFragment = FRAGMENT_MAIN_HOME;
                    wCurrentFragment = FRAGMENT_MAIN_SHOPS_VIEW;
                    if (shopsViewFragment!=null){
                        setFragment( shopsViewFragment );
                    }else{
                        shopsViewFragment = new ShopsViewFragment();
                        setFragment( shopsViewFragment );
                    }
                    break;
                default:
                    super.onBackPressed();
                    break;
            }

        }

    }

    // --------  Menu And Navigation....

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        if (wCurrentFragment == M_HOME_FRAGMENT){
//            getMenuInflater().inflate( R.menu.menu_cart_item,menu);
//
//            // Check First whether any item in cart or not...
//            // if any item has in cart...
//            MenuItem cartItem = menu.findItem( R.id.menu_cart_main );
//            cartItem.setActionView( R.layout.badge_cart_layout );
//            badgeCartCount = cartItem.getActionView().findViewById( R.id.badge_count_text );
//            if (DBquery.myCartCheckList.size() > 0){
//                badgeCartCount.setVisibility( View.VISIBLE );
//                badgeCartCount.setText( String.valueOf( DBquery.myCartCheckList.size() ) );
//            }
//            cartItem.getActionView().setOnClickListener( new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // GOTO : My cart
//                    if (currentUser == null){
//                        dialogsClass.signInUpDialog( MAIN_ACTIVITY );
//                    }else{
//                        myCart();
//                    }
//                }
//            } );
//
//            // notification badge...
//            MenuItem notificationItem = menu.findItem( R.id.menu_notification_main );
//            notificationItem.setActionView( R.layout.badge_notification_layout );
//            badgeNotifyCount = notificationItem.getActionView().findViewById( R.id.badge_count );
//            if (currentUser != null){
//                // Run user Notification Query to update...
//                DBquery.updateNotificationQuery( MainActivity.this,false );
//            }
//            notificationItem.getActionView().setOnClickListener( new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent catIntent = new Intent( MainActivity.this, NotificationActivity.class);
//                    startActivity( catIntent );
//                }
//            } );
//
//        }

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
//                dialogsClass.signInUpDialog( MAIN_ACTIVITY );
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


    // Fragment Transaction...
    public void setNextFragment(Fragment fragment, int nextFragment){
        wPreviousFragment = wCurrentFragment;
        wCurrentFragment = nextFragment;
        setFragment(fragment);
    }
    public void setFragment( Fragment fragment ){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.fade_in, R.anim.fade_out );
        fragmentTransaction.replace( mainHomeContentFrame.getId(),fragment );
        fragmentTransaction.commit();
    }





}
