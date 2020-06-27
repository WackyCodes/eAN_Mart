package wackycodes.ecom.eanmart.userprofile.addresses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.databasequery.UserDataQuery;
import wackycodes.ecom.eanmart.other.CheckInternetConnection;
import wackycodes.ecom.eanmart.other.DialogsClass;
import wackycodes.ecom.eanmart.other.StaticMethods;
import wackycodes.ecom.eanmart.other.StaticValues;

import static wackycodes.ecom.eanmart.databasequery.UserDataQuery.userAddressList;
import static wackycodes.ecom.eanmart.other.StaticValues.EDIT_ADDRESS_MODE;
import static wackycodes.ecom.eanmart.other.StaticValues.QUERY_TO_ADD_ADDRESS;
import static wackycodes.ecom.eanmart.other.StaticValues.QUERY_TO_UPDATE_ADDRESS;

public class AddAddressActivity extends AppCompatActivity {

    public static Button addSaveBtn;
    private EditText addUser;
    private EditText addHNo;
    private EditText addColonyVillage;
    private EditText addPinCode;
    private EditText addLandmark;
    private Spinner addCitySpinner;

    private int index;
    private ArrayAdapter<String> cityAdapter;
    private Dialog dialog;
    private String city = null;

    public static AppCompatActivity addAddressActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_address );
        addAddressActivity = this;

        Toolbar toolbar = findViewById( R.id.x_ToolBar );
        setSupportActionBar( toolbar );
        // Set Title on Action Menu
        try{
            getSupportActionBar().setDisplayShowTitleEnabled( false );
            getSupportActionBar().setTitle( "Add/Update Address" );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException e){
        }
        // Assign Variables..
        dialog = DialogsClass.getDialog( this );
        final int addMode = getIntent().getIntExtra( "MODE", -1 );
        addSaveBtn = findViewById( R.id.add_save_btn );
        addUser = findViewById( R.id.add_user );
        addHNo = findViewById( R.id.add_h_no );
        addColonyVillage = findViewById( R.id.add_colony_village );
        addPinCode = findViewById( R.id.add_pin_code );
        addLandmark = findViewById( R.id.add_landmark );
        addCitySpinner = findViewById( R.id.add_city_spinner );

        if (addMode == EDIT_ADDRESS_MODE){
            index  = getIntent().getIntExtra( "INDEX", -1 );
            addUser.setText( userAddressList.get( index ).getAddUserName() );
            addHNo.setText( userAddressList.get( index ).getAddHouseNoWard() );
            addColonyVillage.setText( userAddressList.get( index ).getAddColonyVillage() );
            addPinCode.setText( userAddressList.get( index ).getAddAreaPinCode() );
            addLandmark.setText( userAddressList.get( index ).getAddLandmark() );
            city = userAddressList.get( index ).getAddCityName();
        }

        // button click
        addSaveBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = addUser.getText().toString().trim();
                String HNo = addHNo.getText().toString().trim();
                String colony = addColonyVillage.getText().toString().trim();
                String areaCode = addPinCode.getText().toString().trim();
                String landmark = addLandmark.getText().toString().trim();

                //TODO: Check first validation..& and Net Connection
                if (isValidData(user, HNo, colony, areaCode) && isInternetConnect() && city != null){
                    addSaveBtn.setEnabled( false );
                    // Query to upload data on Database....
                    dialog.show();
                    Boolean isSelected = false;
                    String addressId =  String.valueOf( StaticMethods.getRandomOTP() );
                    if (userAddressList.size() == 0){
                        isSelected = true;
                    }
                    UserAddressDataModel userAddressDataModel = new UserAddressDataModel(
                            addressId,
                            user,
                            "Mobile",
                            HNo,
                            colony,
                            city,
                            "State",
                            areaCode,
                            landmark,
                            isSelected
                    );
                    if (addMode == EDIT_ADDRESS_MODE ){
                        UserDataQuery.addUpdateRemoveAddressQuery( AddAddressActivity.this, dialog, QUERY_TO_UPDATE_ADDRESS,
                                userAddressList.get( index ).getAddressID(), index,userAddressDataModel );
                    }
                    else{
                        UserDataQuery.addUpdateRemoveAddressQuery( AddAddressActivity.this, dialog, QUERY_TO_ADD_ADDRESS,
                                addressId, index,userAddressDataModel );
                    }

                }else{
                    addSaveBtn.setEnabled( true );
                    if (city != null){
//                        Toast.makeText( AddAddressActivity.this, "Select City.!", Toast.LENGTH_SHORT ).show();
                        DialogsClass.alertDialog( AddAddressActivity.this, null, "Select City!" ).show();
                    }
                }

            }
        } );

        // ---------------------------------------

        // Set city code Spinner
        cityAdapter = new ArrayAdapter <String>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray( R.array.city_list ));
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addCitySpinner.setAdapter(cityAdapter);
        addCitySpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                if ( position != 0){
                   city = cityAdapter.getItem( position );
                }
                else{
                    city = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView <?> parent) {
//                if (cityList.size() == 1 || ! isUploadImages)
//                    showToast( "Upload Images first..!" );
            }
        } );

    }

    // Check Validity ...
    private boolean isValidData(String user, String HNo, String colony, String areaCode){

        if ( TextUtils.isEmpty( HNo ) && TextUtils.isEmpty( colony ) && TextUtils.isEmpty( areaCode ) ){
            addHNo.setError( "Required!" );
            addColonyVillage.setError( "Required!" );
            addPinCode.setError( "Required!" );
            return false;
        }else
        if ( TextUtils.isEmpty( user ) ){
            addUser.setError( "Required!");
            return false;
        }else
        if ( TextUtils.isEmpty( HNo ) ){
            addHNo.setError( "Enter H No / Flat No. or Building Name " );
            return false;
        }else
//        if ( TextUtils.isEmpty( colony ) ){
//            addColony.setError( "Enter Street or Colony or Area" );
//            return false;
//        }else
//        if ( TextUtils.isEmpty( city ) ){
//            addCity.setError( "Enter Your City" );
//            return false;
//        }else
            if ( TextUtils.isEmpty( colony ) ){
                addColonyVillage.setError( "Required!" );
                return false;
            }else
            if ( TextUtils.isEmpty( areaCode ) ){
                addPinCode.setError( "Enter Your Area Code" );
                return false;
            }else
            if ( !TextUtils.isEmpty( areaCode ) ){
                if (areaCode.length() < 6){
                    addPinCode.setError( "Please enter Correct Area Code..!" );
                    return false;
                }
            }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ((item.getItemId() == android.R.id.home)){
            finish();
            return true;
        }
        return super.onOptionsItemSelected( item );
    }

    private boolean isInternetConnect() {
     return CheckInternetConnection.isInternetConnected( this );
    }


   /** public void getAreaListQuery(final Dialog dialog, @Nullable final ArrayAdapter<String> arrayAdapter, String cityName ){
//        // Area Request...
        areaNameList.clear();
        areaNameList.add( "Select Area" );
        areaCodeAndNameList.clear();
        areaCodeAndNameList.add( new AreaCodeAndName( " ", "Select Area" ) );
        FirebaseFirestore.getInstance().collection( "ADMIN_PER" ).document(cityName.toUpperCase())
                .collection( "SUB_LOCATION" ).orderBy( "location_id" ).get()
                .addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task <QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                areaNameList.add( documentSnapshot.get( "location_name" ).toString() );
                                areaCodeAndNameList.add( new AreaCodeAndName( documentSnapshot.get( "location_id" ).toString()
                                        , documentSnapshot.get( "location_name" ).toString()  ) );

                                if (arrayAdapter != null){
                                    arrayAdapter.notifyDataSetChanged();
                                }

                            }
                            dialog.dismiss();

                        }else{
                            dialog.dismiss();

                        }
                    }
                } );

    }

*/


}
