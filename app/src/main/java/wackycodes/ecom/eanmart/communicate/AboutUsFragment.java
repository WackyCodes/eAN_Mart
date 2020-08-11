package wackycodes.ecom.eanmart.communicate;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.other.StaticMethods;

public class AboutUsFragment extends Fragment {


    public AboutUsFragment() {
        // Required empty public constructor
    }

    private TextView wackyCodesText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_about_us, container, false );

        wackyCodesText = view.findViewById( R.id.wacky_codes_name );

        wackyCodesText.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticMethods.gotoURL( getContext(), "https://linktr.ee/wackycodes" );
            }
        } );

        return view;
    }




}
