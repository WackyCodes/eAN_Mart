package wackycodes.ecom.eanmart.userprofile.addresses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.buyprocess.ConformOrderActivity;
import wackycodes.ecom.eanmart.databasequery.DBQuery;
import wackycodes.ecom.eanmart.databasequery.UserDataQuery;
import wackycodes.ecom.eanmart.other.CheckInternetConnection;
import wackycodes.ecom.eanmart.other.DialogsClass;
import wackycodes.ecom.eanmart.other.StaticValues;
import wackycodes.ecom.eanmart.userprofile.cart.CartActivity;

import static wackycodes.ecom.eanmart.databasequery.DBQuery.firebaseFirestore;
import static wackycodes.ecom.eanmart.databasequery.UserDataQuery.temCartItemModelList;
import static wackycodes.ecom.eanmart.databasequery.UserDataQuery.userAddressList;
import static wackycodes.ecom.eanmart.other.StaticMethods.showToast;
import static wackycodes.ecom.eanmart.other.StaticValues.CURRENT_CITY_CODE;
import static wackycodes.ecom.eanmart.other.StaticValues.SELECT_ADDRESS;

public class MyAddressesActivity extends AppCompatActivity {

    public static boolean isRequestForChangeAddress = false;
    private RecyclerView myAddRecylerView;
    public static  MyAddressRecyclerAdaptor myAddressRecyclerAdaptor;
    private Button myAddDeliverHereBtn;
    private TextView addNewAddressBtn;
    public static TextView availableAddress;
    public static AppCompatActivity myAddressActivity;

    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_my_addresses );

        myAddressActivity = this;
        dialog = DialogsClass.getDialog( this );

        Toolbar toolbar = findViewById( R.id.x_ToolBar );
        setSupportActionBar( toolbar );
        // Set Title on Action Menu
        try{
            getSupportActionBar().setDisplayShowTitleEnabled( false );
            getSupportActionBar().setTitle( "My Addresses" );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException e){
        }

        final Dialog dialog = DialogsClass.getDialog( this );
        if (userAddressList.size()==0){
            UserDataQuery.getAllAddressesQuery();
        }

        // To check that user come from..?
        final int addMode = getIntent().getIntExtra( "MODE", -1 );
        // Assign variables...
        myAddDeliverHereBtn = findViewById( R.id.my_add_deliver_here_btn );
        addNewAddressBtn = findViewById( R.id.add_new_address_text );
        availableAddress = findViewById( R.id.available_address );

        // hiding deliver btn...
        if (addMode == SELECT_ADDRESS){
            myAddDeliverHereBtn.setVisibility( View.VISIBLE );
            if (!StaticValues.USER_DATA_MODEL.isLoadData()){
                UserDataQuery.loadUserDataQuery( MyAddressesActivity.this, dialog );
            }
        }else {
            myAddDeliverHereBtn.setVisibility( View.GONE );
        }

        // Click listener ...
        myAddDeliverHereBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // First We have to get Address which is selected by user...
                if (userAddressList.size() > 0){
                    checkIsShopOpen();
                }else{
                    // Means List is Empty So we have to suggest to add any address...
                    Toast.makeText( MyAddressesActivity.this, "Please Add Address first..!", Toast.LENGTH_SHORT ).show();
                }
            }
        } );
        // Click listener ...
        addNewAddressBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addNewAddressIntent = new Intent(MyAddressesActivity.this, AddAddressActivity.class );
                addNewAddressIntent.putExtra( "MODE", addMode );
                startActivity( addNewAddressIntent );
            }
        } );

        // --- My Address Recycler Items...
        myAddRecylerView = findViewById( R.id.my_address_reclycerView );

        LinearLayoutManager myAddLayoutManager = new LinearLayoutManager( this );
        myAddLayoutManager.setOrientation( RecyclerView.VERTICAL );
        myAddRecylerView.setLayoutManager( myAddLayoutManager );
        myAddressRecyclerAdaptor = new MyAddressRecyclerAdaptor( userAddressList, addMode);
        myAddRecylerView.setAdapter( myAddressRecyclerAdaptor );
        ((SimpleItemAnimator)myAddRecylerView.getItemAnimator()).setSupportsChangeAnimations( false );
        myAddressRecyclerAdaptor.notifyDataSetChanged();

        // --- My Address Recycler Items...

    }

    private void checkIsShopOpen(){
        if(CheckInternetConnection.isInternetConnected( this )){
            dialog.show();
            DBQuery.firebaseFirestore.collection( "SHOPS" ).document( temCartItemModelList.get( 0 ).getProductShopID() )
                    .get()
                    .addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot documentSnapshot = task.getResult();
                                Boolean isAvailableService = documentSnapshot.getBoolean( "available_service" );
                                Boolean isOpen = documentSnapshot.getBoolean( "is_open" );

                                if (!isAvailableService || !isOpen){
                                    dialog.dismiss();
                                    if (!isAvailableService){
                                        String closeMsg = "Service not Available! Shop is temporary closed! ";
                                        Dialog closeDialog = DialogsClass.getMessageDialog( MyAddressesActivity.this, "Coming Soon!", closeMsg );
                                        closeDialog.findViewById( R.id.dialog_ok_btn ).setOnClickListener( new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                finishIfShopClose();
                                            }
                                        } );
                                        closeDialog.show();
                                    }else{
                                        String closeMsg = documentSnapshot.get( "shop_close_msg" ).toString();
                                        Dialog closeDialog = DialogsClass.getMessageDialog( MyAddressesActivity.this, "Shop is Close!", closeMsg );
                                        closeDialog.findViewById( R.id.dialog_ok_btn ).setOnClickListener( new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                finishIfShopClose();
                                            }
                                        } );
                                        closeDialog.show();
                                    }
                                }
                                else{
                                    // Continue...
                                    // Means List is not Empty,...
                                    int size = userAddressList.size();
                                    int index = 0;
                                    for (int x = 0; x < size; x++){
                                        if ( userAddressList.get( x ).getSelectedAddress() ){
                                            ConformOrderActivity.index = x;
                                            index = x;
                                            break;
                                        }
                                    }
                                    // Check first if user come to Change address...
                                    checkForAreaCode( userAddressList.get( index ).getAddAreaPinCode(), index );
                                }

                            }else{
                                dialog.dismiss();
                                showToast(MyAddressesActivity.this, "Something Went Wrong!! Try Again..");
                            }
                        }
                    } );
        }

    }

    // Check For Product is exist in particular Area ..
    private void checkForAreaCode(final String areaPin, final int index){
        if (!dialog.isShowing()){
            dialog.show();
        }
        firebaseFirestore.collection( "AREA_CODE" )
                .document( areaPin )
                .addSnapshotListener( new EventListener <DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshot.exists()){
                            if (documentSnapshot.get( "area_city_code" ).toString().toUpperCase().equals( CURRENT_CITY_CODE )){
                                // GO to Next Step...
                                dialog.dismiss();
                                if (isRequestForChangeAddress){
                                    // finish the previous activity before reStart...
                                    isRequestForChangeAddress = false;
                                    finish();
                                }else{
                                    Intent goOrderConfirmation = new Intent(MyAddressesActivity.this, ConformOrderActivity.class );
                                    goOrderConfirmation.putExtra( "INDEX", index );
                                    startActivity( goOrderConfirmation );
                                    finish();
                                }
                            }else{
                                dialog.dismiss();
                                DialogsClass.alertDialog( MyAddressesActivity.this, "Not Available!", "Sorry! Delivery not available here, Please Check your city and PinCode!" ).show();
                            }
                        }else{
                            // Not Exist...
                            dialog.dismiss();
                            DialogsClass.alertDialog( MyAddressesActivity.this, "Alert!", "Sorry! We are not available at your area!" ).show();
                        }
                    }
                } );

//        dialog.show();
//            showToast( "Code Not found!" );
       /* firebaseFirestore.collection( "AREA_CODE" )
                .whereEqualTo( "area_city_code", CURRENT_CITY_CODE )
                .get()
                .addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task <QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            int sizeOfSnap = task.getResult().size();
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                if (documentSnapshot.getId().equals( areaPin )){
                                    // Process next step...
//                                    if (!dialog.isShowing()){
//                                        dialog.show();
//                                    }
                                    break;
                                }
                                sizeOfSnap--;
                            }

                            if (sizeOfSnap > 0){
//                                dialog.dismiss();
//                                showToast( "This Product can not delivered out of "+ CURRENT_CITY_NAME );
                            }

                        }
                    }
                } );

        */
    }

    private void finishIfShopClose(){
        // Finish Conform Order Activity...
        if (ConformOrderActivity.conFormOrderActivity != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!ConformOrderActivity.conFormOrderActivity.isDestroyed()){
                    ConformOrderActivity.conFormOrderActivity.finish();
                }
            }else{
                ConformOrderActivity.conFormOrderActivity.finish();
            }
        }

        // Finish this activity....
        finish();
    }

    public static void refreshMyAddressItem(int deSelected, int Selected){
        myAddressRecyclerAdaptor.notifyItemChanged( deSelected );
        myAddressRecyclerAdaptor.notifyItemChanged( Selected );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ((item.getItemId() == android.R.id.home)){
            finish();
            return true;
        }
        return super.onOptionsItemSelected( item );
    }

}
