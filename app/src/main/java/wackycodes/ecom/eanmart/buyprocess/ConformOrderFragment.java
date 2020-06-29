package wackycodes.ecom.eanmart.buyprocess;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Arrays;
import java.util.List;

import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.databasequery.DBQuery;
import wackycodes.ecom.eanmart.databasequery.UserDataQuery;
import wackycodes.ecom.eanmart.other.DialogsClass;
import wackycodes.ecom.eanmart.other.StaticValues;
import wackycodes.ecom.eanmart.userprofile.addresses.MyAddressesActivity;
import wackycodes.ecom.eanmart.userprofile.addresses.UserAddressDataModel;

import static wackycodes.ecom.eanmart.other.StaticValues.BUY_FROM_CART;
import static wackycodes.ecom.eanmart.other.StaticValues.BUY_FROM_VALUE;
import static wackycodes.ecom.eanmart.other.StaticValues.SELECT_ADDRESS;

public class ConformOrderFragment extends Fragment {

    public ConformOrderFragment() {
        // Required empty public constructor
    }

    private TextView addUserName;
    private TextView addUserFullAddress;
    private TextView addUserPincode;
    private Button changeOrAddNewAddressBtn;
    private TextView totalBillAmount;
    private Button  conformOrderBtn;

    private ConstraintLayout orderConfirmConstLayout;

    private FrameLayout confirmOrderFragmentFrameLayout;
    // Pay mode...
    private RadioGroup payModeRadioGroup;
    private RadioButton payCODRadioBtn;
    private RadioButton payPaytmRadioBtn;
    // create Variable of addressRecyclerModel
    private UserAddressDataModel addressRecyclerModel;

    private String userPhone;


    // Time Schedule...
    private GridView scheduleGridView;
    private RadioGroup scheduleRadioGroup;
    private RadioButton scheduleMorning;
    private RadioButton scheduleEvening;
    private RadioButton scheduleAnyTime;

    private String scheduleDay = null;
    private String scheduleTime = null;
    private String deliverySchedule = null;

    Dialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_conform_order, container, false );

        Dialog dialog = DialogsClass.getDialog( view.getContext() );

        // Assign Variable...
        orderConfirmConstLayout = view.findViewById( R.id.orderConfirmConstLayout );
        changeOrAddNewAddressBtn = view.findViewById( R.id.add_nw_address_btn );
        addUserName = view.findViewById( R.id.my_add_full_name );
        addUserFullAddress = view.findViewById( R.id.my_add_full_address );
        addUserPincode = view.findViewById( R.id.my_add_pin_text );

        totalBillAmount = view.findViewById( R.id.total_bill_amount );
        conformOrderBtn = view.findViewById( R.id.conformOrderBtn );
        confirmOrderFragmentFrameLayout = view.findViewById( R.id.confirm_order_fragmentFrame );

        // Pay Mode...
        payModeRadioGroup = view.findViewById( R.id.payModeRadioGroup );
        payCODRadioBtn = view.findViewById( R.id.payCODRadioBtn );
        payPaytmRadioBtn = view.findViewById( R.id.payPaytmRadioBtn );

        // Scheduling...
        scheduleGridView = view.findViewById( R.id.schedule_grid_view );
        scheduleRadioGroup = view.findViewById( R.id.time_schedule_group );
        scheduleMorning = view.findViewById( R.id.schedule_time_morning );
        scheduleEvening = view.findViewById( R.id.schedule_time_evening );
        scheduleAnyTime = view.findViewById( R.id.schedule_time_anytime );


        List <String> scheduleList = Arrays.asList( getResources().getStringArray( R.array.schedule_array ) );
        ScheduleAdapter scheduleAdapter = new ScheduleAdapter( scheduleList );
        scheduleGridView.setAdapter( scheduleAdapter );
        scheduleAdapter.notifyDataSetChanged();
        ///------

        changeOrAddNewAddressBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myAddressIntent = new Intent( getActivity(), MyAddressesActivity.class );
                myAddressIntent.putExtra( "MODE", SELECT_ADDRESS );
                startActivity( myAddressIntent );
                MyAddressesActivity.isRequestForChangeAddress = true;
            }
        } );

        if (UserDataQuery.userAddressList.size() > 0){
            addressRecyclerModel = UserDataQuery.userAddressList.get( ConformOrderActivity.index );
            setDeliveryAddress();
        }

        if (StaticValues.USER_DATA_MODEL.isLoadData()){
//            orderDetailMap.put( "user_phone",  );/
            userPhone = StaticValues.USER_DATA_MODEL.getUserMobile();
        }else{
            dialog.show();
            UserDataQuery.loadUserDataQuery( getContext(), dialog );
        }

        // Set Bill Text...
        totalBillAmount.setText( "Rs." + StaticValues.TOTAL_BILL_AMOUNT + "/-" );

        conformOrderBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( scheduleDay != null && getScheduleTime() != null) {
                    int selectedMode = payModeRadioGroup.getCheckedRadioButtonId();
                    deliverySchedule = scheduleDay + " - " + scheduleTime;
                    if (selectedMode == payCODRadioBtn.getId()){

                        DialogsClass.alertDialog( getContext(), "Sorry ! We can't proceed ahead.",
                                "Our Service is temporary closed so we can not verify your mobile number.! Please contact app founder." );
                        /**
                         if (!StaticValues.USER_ACCOUNT_INFO_MODEL.isMobileVerify()){
                         if (StaticValues.USER_ACCOUNT_INFO_MODEL.getUserMobile()!=null){
                         StaticValues.isVerifiedMobile = false;
                         Intent otpIntent = new Intent( getActivity(), OTPVerifyActivity.class );
                         otpIntent.putExtra( "USER_PHONE",
                         StaticValues.getCorrectMobile( StaticValues.USER_ACCOUNT_INFO_MODEL.getUserMobile() ) );
                         startActivity( otpIntent );
                         }else{
                         DialogsClass.alertDialog( getContext(), "Mobile Number not found.!",
                         "Your mobile no. is not exist in our record. Please update your mobile from profile section.!" );
                         }
                         }else{
                         checkForAreaCode();
                         }
                         */
                    }
                    else if (selectedMode == payPaytmRadioBtn.getId()){
//                        showPaytmDialog();
//                    callOrderQuery( StaticValues.ONLINE_MODE );

                    }else{
                        Toast.makeText( getContext(),"Please select Pay Mode..!!", Toast.LENGTH_SHORT ).show();
                    }
                }else{
                    if (scheduleDay == null){
                        Toast.makeText( getContext(), "Please Select Scheduling...", Toast.LENGTH_SHORT ).show();
                    }else{
                        Toast.makeText( getContext(), "Please Schedule time...", Toast.LENGTH_SHORT ).show();
                    }
                }

            }
        } );


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

//        if (StaticValues.isVerifiedMobile){
//            StaticValues.isVerifiedMobile = false;
//            // GOTO :
//            callOrderQuery( StaticValues.COD_MODE );
//        }

    }

    public void setDeliveryAddress(){
        addUserName.setText( getDeliveredFullName() );
        addUserFullAddress.setText( getDeliveryAddress() );
        addUserPincode.setText( getDeliveryPin() );
    }

    private String getDeliveredFullName(){
        String fullName = addressRecyclerModel.getAddUserName();
        return fullName;
    }
    private String getDeliveryAddress(){
        String landmark = "";
        if ( !TextUtils.isEmpty( addressRecyclerModel.getAddLandmark()) ){
            landmark = ", ( Landmark : " + addressRecyclerModel.getAddLandmark() + " )";
        }
        String fullAddress = addressRecyclerModel.getAddHouseNoWard() + ", "
                + addressRecyclerModel.getAddColonyVillage() + ", "
                + addressRecyclerModel.getAddCityName() + ", \n"
                + landmark;
        return fullAddress;
    }
    private String getDeliveryPin(){
        String pinCode = addressRecyclerModel.getAddAreaPinCode();
        return pinCode;
    }

    // Order Sorting Key...
    private String getCityKey(){
        String cityKey = addressRecyclerModel.getAddCityName(); // TODO: Not needed...!
        return cityKey;
    }
    private String getAreaKey(){
        String areaKey = addressRecyclerModel.getAddAreaPinCode(); // TODO: Not needed...!
        return areaKey;
    }

    private String getScheduleTime(){
        int scheduleMode = scheduleRadioGroup.getCheckedRadioButtonId();
        if (scheduleMode==scheduleMorning.getId()){
            scheduleTime = scheduleMorning.getText().toString();
        }else if(scheduleMode==scheduleEvening.getId()){
            scheduleTime = scheduleEvening.getText().toString();
        }else if(scheduleMode==scheduleAnyTime.getId()){
            scheduleTime = scheduleAnyTime.getText().toString();
        }else{
            scheduleTime = null;
        }
        return scheduleTime;
    }

    // Check For Product is exist in particular Area ..
    private void checkForAreaCode(){
        dialog.show();
        if (true ) {
            showToast( "Code Not found!" );
//            DBQuery.firebaseFirestore.collection( "AREA_CODE" ).orderBy( "area_code" )
//                    .get()
//                    .addSnapshotListener( new EventListener <DocumentSnapshot>() {
//                        @Override
//                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                            if (documentSnapshot.exists()) {
//                                callOrderQuery();
//                            } else {
//                                dialog.dismiss();
//                                showToast( "Product is not available for this Area Code!" );
//                            }
//                        }
//                    } );
        }else{
            dialog.dismiss();
            showToast( "Your city or Area name is not found." );
        }
    }

    // Order Query call...
    private void callOrderQuery( ){

        if (!dialog.isShowing()){
            dialog.show();
        }
        int payMode = StaticValues.COD_MODE;




//        setFragment( new ContinueShopping() );
    }

   /** private void queryToBuy(int payMode, int BUY_FROM_VALUE  ){
        // User Information...
        String orderByUserId = FirebaseAuth.getInstance().getUid();
        String orderDeliveredName = getDeliveredFullName();
        String orderDeliveryAddress = getDeliveryAddress();
        String orderDeliveryPin = getDeliveryPin();
        String orderCityKey = getCityKey();
        String orderAreaKey = getAreaKey();
        String billAmount = String.valueOf( StaticValues.TOTAL_BILL_AMOUNT );
        String orderDateDay = StaticValues.getCurrentDateDay();
        String orderTime = StaticValues.getCurrentTime();
        String deliveryCharge = String.valueOf( StaticValues.DELIVERY_AMOUNT );

        // Order Query...
        OrderQuery.createOrderIdAndDocument( getActivity(), dialog,
                BUY_FROM_VALUE, deliveryCharge, payMode, "", billAmount, orderByUserId,
                orderDeliveredName, orderDeliveryAddress, orderDeliveryPin, orderDateDay, orderTime, deliverySchedule, orderCityKey, orderAreaKey);

        setFragment( new ContinueShopping() );

    }

    String productStocks;
    private void updateProductStockQuery(final int payMode, final int BUY_FROM_VALUE, final String productID, final int qty){
        dialog.show();
        DBquery.firebaseFirestore.collection( "PRODUCTS" ).document( productID )
                .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    long stocks = (long)task.getResult().get( "product_stocks" );
                    productStocks = String.valueOf( stocks );
                    int stockVal = Integer.parseInt( productStocks ) - qty;

                    if (stockVal >= 0 ){
                        // Update Stocks And Buy process...
                        DBquery.firebaseFirestore.collection( "PRODUCTS" ).document( productID )
                                .update( "product_stocks", stockVal ).addOnSuccessListener( new OnSuccessListener <Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Call Function to Order process...
                                dialog.show();
                                queryToBuy(payMode, BUY_FROM_VALUE);
                            }
                        } );

                        if (stockVal == 0){
                            // Send Notification of Out of Stocks..
                            DBquery.sendOutOfStockNotification(productID);
                        }

                    }else{
                        dialog.dismiss();
                        if (Integer.parseInt( productStocks )>0)
                            showToast( "Sorry.! We have only "+ productStocks + " products remains.!" );
                        else
                            showToast( "Sorry.! Products has been out of Stocks.!" );
                    }

                }else {
                    // Task Failed...
                    dialog.dismiss();

                }
            }
        } );

    }



    // Paytm accessing dialog...
    private void showPaytmDialog(){
        /// Sample Button click...
        final Dialog permissionDialog = new Dialog( getActivity() );
        permissionDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        permissionDialog.setContentView( R.layout.dialog_permission );
        permissionDialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        permissionDialog.setCancelable( false );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            permissionDialog.getWindow().setBackgroundDrawable( getActivity().getDrawable( R.drawable.x_ractangle_layout ) );
        }
        Button okBtn = permissionDialog.findViewById( R.id.per_ok_button );
        TextView msgTxt = permissionDialog.findViewById( R.id.per_text_des );

        msgTxt.setText( "Please select COD for Testing Purpose..! Paytm is Locked for Testing. For further Details Contact App Founder..!" );
        okBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permissionDialog.dismiss();
            }
        } );
        permissionDialog.show();
    }

    @Override
    public void onDestroyView() {
//        ViewGroup mContainer = (ViewGroup) getActivity().findViewById(R.id.sign_in_frameLayout);
//        mContainer.removeAllViews();
        confirmOrderFragmentFrameLayout.removeAllViews();
        super.onDestroyView();
    }

    // Fragment Transaction...
    public void setFragment( Fragment fragment){
        try{
            getActivity().getParent().getActionBar().setTitle( "Continue Shopping..." );
            getActivity().getParent().getActionBar().setDisplayShowTitleEnabled( true );
            getActivity().getParent().getActionBar().setDisplayHomeAsUpEnabled( false );
        }catch (NullPointerException e){ }
        ConformOrderActivity.currentFrameLayout = CONTINUE_SHOPPING_FRAGMENT;
        orderConfirmConstLayout.setVisibility( View.GONE );
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.slide_from_right, R.anim.slide_out_from_left );
//        onDestroyView();
        fragmentTransaction.replace( confirmOrderFragmentFrameLayout.getId(),fragment );
        fragmentTransaction.commit();
    }


*/
   private void showToast(String s){
       Toast.makeText( getActivity(), s , Toast.LENGTH_SHORT ).show();
   }
    ///////////// Scheduling Adopter....
    private class ScheduleAdapter extends BaseAdapter {

        private List <String> scheduleList;

        public ScheduleAdapter(List <String> scheduleList) {
            this.scheduleList = scheduleList;
        }

        @Override
        public int getCount() {
            return scheduleList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @SuppressLint({"ViewHolder", "InflateParams"})
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            scheduleDay = null;
            final View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.pick_schedule, null );
            view.setBackgroundTintList( ColorStateList.valueOf( getResources().getColor( R.color.colorGray ) ) );
            TextView pickScheduleText = view.findViewById( R.id.pick_schedule_text );
            pickScheduleText.setText( scheduleList.get( position ) );

            view.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < scheduleList.size(); i++){
                        parent.getChildAt( i ).setBackgroundTintList( ColorStateList.valueOf( getResources().getColor( R.color.colorGray ) ) );
                    }
                    TextView scheduleDayText = v.findViewById(  R.id.pick_schedule_text  );
                    scheduleDay = scheduleDayText.getText().toString();
                    // delivery_schedule_time
                    v.setBackgroundTintList( ColorStateList.valueOf( getResources().getColor( R.color.colorSecondary ) ) );

                }
            } );

            return  view;
        }

    }




}