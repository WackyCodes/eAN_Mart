package wackycodes.ecom.eanmart.userprofile.orders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import wackycodes.ecom.eanmart.MainActivity;
import wackycodes.ecom.eanmart.R;
import wackycodes.ecom.eanmart.databasequery.UserDataQuery;
import wackycodes.ecom.eanmart.other.DialogsClass;
import wackycodes.ecom.eanmart.userprofile.cart.MyCartAdaptor;

import static wackycodes.ecom.eanmart.databasequery.UserDataQuery.orderItemModelList;

public class OrderListActivity extends AppCompatActivity {

    public static OrderListAdaptor orderListAdaptor;

    private LinearLayout noOrderLayout;
    private RecyclerView orderRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_order_list );

        if(orderItemModelList.size() == 0){
            // TODO : Load Order List...
            UserDataQuery.loadOrderDataQuery(this);
        }

        Toolbar toolbar = findViewById( R.id.x_ToolBar );
        setSupportActionBar( toolbar );
        // Set Title on Action Menu
        try{
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar().setTitle( "My Orders" );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException e){
        }

        noOrderLayout = findViewById( R.id.no_order_linear_layout );
        orderRecycler = findViewById( R.id.my_order_recyclerView );
        // SetVisibility


        // Set value of cart variables...
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this );
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        orderRecycler.setLayoutManager( linearLayoutManager );

        orderListAdaptor = new OrderListAdaptor( orderItemModelList );
        orderRecycler.setAdapter( orderListAdaptor );

        setVisibility();

    }

    private void setVisibility(){
        if(orderItemModelList.size() == 0){
            noOrderLayout.setVisibility( View.VISIBLE );
            orderRecycler.setVisibility( View.GONE );
        }else{
            orderRecycler.setVisibility(  View.VISIBLE );
            noOrderLayout.setVisibility(  View.GONE );
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected( item );
    }



}
