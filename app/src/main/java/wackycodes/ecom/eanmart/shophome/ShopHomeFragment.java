package wackycodes.ecom.eanmart.shophome;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import wackycodes.ecom.eanmart.R;

public class ShopHomeFragment extends Fragment {
    public static ShopHomeFragment shopHomeFragment;

    private FrameLayout shopHomeFrameLayout;

    public ShopHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate( R.layout.fragment_shop_home, container, false );

        shopHomeFrameLayout = view.findViewById( R.id.fragment_shop_home_frame_layout );





        return view;
    }



}
