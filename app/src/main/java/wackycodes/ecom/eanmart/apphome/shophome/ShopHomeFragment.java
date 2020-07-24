package wackycodes.ecom.eanmart.apphome.shophome;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import wackycodes.ecom.eanmart.R;

public class ShopHomeFragment extends Fragment {
    public static ShopHomeFragment shopHomeFragment;
    public static FragmentActivity shopHomeFragmentContext;

    public static FrameLayout shopHomeFrameLayout;

    public ShopHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate( R.layout.fragment_shop_home, container, false );
        shopHomeFragmentContext = getActivity();
        shopHomeFrameLayout = view.findViewById( R.id.fragment_shop_home_frame_layout );


        return view;
    }



}
