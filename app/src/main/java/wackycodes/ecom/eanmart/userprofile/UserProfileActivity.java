package wackycodes.ecom.eanmart.userprofile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import de.hdodenhof.circleimageview.CircleImageView;
import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.databasequery.DBQuery;
import wackycodes.ecom.eanmart.databasequery.UserDataQuery;
import wackycodes.ecom.eanmart.other.CheckInternetConnection;
import wackycodes.ecom.eanmart.other.DialogsClass;
import wackycodes.ecom.eanmart.other.StaticValues;
import wackycodes.ecom.eanmart.userprofile.addresses.MyAddressesActivity;
import wackycodes.ecom.eanmart.userprofile.addresses.UserAddressDataModel;

import static wackycodes.ecom.eanmart.databasequery.UserDataQuery.userAddressList;
import static wackycodes.ecom.eanmart.other.StaticValues.MANAGE_ADDRESS;

public class UserProfileActivity extends AppCompatActivity {


    // top
    private CircleImageView userImage;
    private TextView userNameTop;
    private TextView userEmailTop;
    // middle
    private TextView userNameMiddle;
    private TextView userEmailMiddle;
    private TextView userMobileMiddle;
    // middle - edit
    private ImageButton settingsButton;

    // bottom
    private TextView viewAllAddBtn;
    private static TextView addUserName;
    private static TextView addUserFullAddress;
    private static TextView addUserPincode;
    private Button addNewAddressBtn;

    // layout...
    private static LinearLayout setAddressLinearLayout;
    private static LinearLayout noAddressLinearLayout;

    // Private Dialog...
    private Dialog dialog;
//    public static List <String> userProfileInfoList = new ArrayList <>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_user_profile );


        // Top assign...
        userImage = findViewById( R.id.userImage );
        userNameTop = findViewById( R.id.userNameTop );
        userEmailTop = findViewById( R.id.userEmailTop );
        // middle
        userNameMiddle = findViewById( R.id.user_name );
        userEmailMiddle = findViewById( R.id.user_email );
        userMobileMiddle = findViewById( R.id.user_mobile );
        // middle - edit
        settingsButton = findViewById( R.id.settings_button );

        // bottom...
        viewAllAddBtn = findViewById( R.id.view_all_address_btn );
        addUserName = findViewById( R.id.user_add_name );
        addUserFullAddress = findViewById( R.id.user_add_full_address );
        addUserPincode = findViewById( R.id.user_add_pin_text );
        addNewAddressBtn = findViewById( R.id.add_new_address_btn );

        // layout...
        setAddressLinearLayout = findViewById( R.id.set_address_LinearLyout );
        noAddressLinearLayout = findViewById( R.id.no_address_LinearLayout );
        //-----------------------------------------------------------------------------

        settingsButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( UserProfileActivity.this, AccountSettingActivity.class);
                startActivity( intent );
            }
        } );

        // GOTO : My Address activity...
        viewAllAddBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // GOTO : Goto our my address...
                Intent myAddressIntent = new Intent(UserProfileActivity.this, MyAddressesActivity.class);
                myAddressIntent.putExtra( "MODE", MANAGE_ADDRESS );
                view.getContext().startActivity( myAddressIntent );
            }
        } );
        addNewAddressBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // GOTO : Goto our my address...
                Intent myAddressIntent = new Intent(UserProfileActivity.this, MyAddressesActivity.class);
                myAddressIntent.putExtra( "MODE", MANAGE_ADDRESS );
                view.getContext().startActivity( myAddressIntent );
            }
        } );

        // set First Address in profile...
        if (userAddressList.size() > 0){
//            UserDataQuery.getAllAddressesQuery();

            setAddress();
        }else{
            // Set Default Text : No Address Available...
            setAddressLinearLayout.setVisibility( View.GONE );
            noAddressLinearLayout.setVisibility( View.VISIBLE );
        }

        if (StaticValues.USER_DATA_MODEL.isLoadData()){
            setUserData( StaticValues.USER_DATA_MODEL.getUserFullName(), StaticValues.USER_DATA_MODEL.getUserEmail()
                    , StaticValues.USER_DATA_MODEL.getUserMobile());
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        dialog = DialogsClass.getDialog( this );
        if ( !StaticValues.USER_DATA_MODEL.isLoadData() || userAddressList.size() == 0 ){
            // Query to get data from database...
            if (w_isInternetConnect()){
                dialog.show();
                if (userAddressList.size() == 0){
                    UserDataQuery.getAllAddressesQuery();
                    dialog.dismiss();
                }
                if ( !StaticValues.USER_DATA_MODEL.isLoadData()){
                    UserDataQuery.loadUserDataQuery( this, dialog );
//                    setUserData( name, email, phone );
                }
            }

        }
    }

    // Methods...

    private void setUserData( String userName, String userEmail, String userPhone){
        // Set UserImage...
        Glide.with( this ).load( StaticValues.USER_DATA_MODEL.getUserProfilePhoto() ).
                apply( new RequestOptions().placeholder( R.drawable.ic_account_circle_gray_24dp) ).into( userImage );
        // Top Profile set
        userNameTop.setText( userName );
        userEmailTop.setText( userEmail );
        //middle profile set
        userNameMiddle.setText( userName );
        userEmailMiddle.setText( userEmail );
        if ( !TextUtils.isEmpty( userPhone )){
            userMobileMiddle.setText( userPhone );
        }
    }

    public static void setAddress(){
        setAddressLinearLayout.setVisibility( View.VISIBLE );
        noAddressLinearLayout.setVisibility( View.GONE );
        String landmark = "";
        UserAddressDataModel addressRecyclerModel = userAddressList.get( 0 );

        String fullName = addressRecyclerModel.getAddUserName();
        if ( !TextUtils.isEmpty( addressRecyclerModel.getAddLandmark()) ){
            landmark = ", ( Landmark : " + addressRecyclerModel.getAddLandmark() + " )";
        }
        String fullAddress = addressRecyclerModel.getAddHouseNoWard() + ", "
                + addressRecyclerModel.getAddColonyVillage() + ", "
                + addressRecyclerModel.getAddCityName() + ", \n"
                 + landmark;

        String pinCode = addressRecyclerModel.getAddAreaPinCode();

        addUserName.setText( fullName );
        addUserFullAddress.setText( fullAddress );
        addUserPincode.setText( pinCode );

    }

    private void showToast(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private boolean w_isInternetConnect() {
        return CheckInternetConnection.isInternetConnected( this );

    }
}
