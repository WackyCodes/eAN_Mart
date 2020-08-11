package wackycodes.ecom.eanmart.buyprocess;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import wackycodes.ecom.eanmart.MainActivity;
import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.productdetails.ProductDetails;
import wackycodes.ecom.eanmart.userprofile.cart.CartActivity;

import static wackycodes.ecom.eanmart.other.StaticValues.CONTINUE_SHOPPING_FRAGMENT;

public class ContinueShopping extends Fragment {

    public ContinueShopping() {
        // Required empty public constructor
        ConformOrderActivity.currentFrameLayout = CONTINUE_SHOPPING_FRAGMENT;
    }

    private FrameLayout continueShoppingFrameLayout;

    public static LinearLayout waitingLayout;
    public static LinearLayout orderSuccessLinearLayout;
    public static TextView orderIdText;
    private Button continueShoppingBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_continue_shopping, container, false );

        waitingLayout = view.findViewById( R.id.waitLinearLayout );
        orderSuccessLinearLayout = view.findViewById( R.id.successOrderTextLinearLayout );
        continueShoppingFrameLayout = view.findViewById( R.id.continue_frameLayout );
        continueShoppingBtn = view.findViewById( R.id.continueShoppingBtn );
        orderIdText = view.findViewById( R.id.order_id_text );
        orderSuccessLinearLayout.setVisibility( View.GONE );

        continueShoppingBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Jump To MainActivity....
                finishPrevious();
            }
        } );

        return view;
    }

    private void finishPrevious(){
        // Finish Cart Activity....
        if (CartActivity.cartActivty != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!CartActivity.cartActivty.isDestroyed()){
                    CartActivity.cartActivty.finish();
                }
            }else{
                CartActivity.cartActivty.finish();
            }
        }

        // Finish ProductDetails Activity....
        if (ProductDetails.productDetails != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!ProductDetails.productDetails.isDestroyed()){
                    ProductDetails.productDetails.finish();
                }
            }else{
                ProductDetails.productDetails.finish();
            }
        }

        // Finish this Activity..
        getActivity().finish();

    }

}
