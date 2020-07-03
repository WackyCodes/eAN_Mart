package wackycodes.ecom.eanmart.buyprocess;


import android.content.Intent;
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

public class ContinueShopping extends Fragment {


    public ContinueShopping() {
        // Required empty public constructor
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
                getActivity().finish();
            }
        } );

        return view;
    }

}
