package wackycodes.ecom.eanmart.apphome.shophome.aboutshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import wackycodes.ecom.eanmart.R;

public class AboutShopActivity extends AppCompatActivity {

    private LinearLayout continueShopping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_about_shop );

        String shopID = getIntent().getStringExtra( "SHOP_ID" );

        continueShopping = findViewById( R.id.continue_shopping_layout );
        continueShopping.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );

    }



}
