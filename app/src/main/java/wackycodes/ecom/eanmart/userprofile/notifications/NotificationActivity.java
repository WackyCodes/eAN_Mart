package wackycodes.ecom.eanmart.userprofile.notifications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import wackycodes.ecom.eanmart.R;

import static wackycodes.ecom.eanmart.databasequery.UserDataQuery.notificationModelList;
import static wackycodes.ecom.eanmart.databasequery.UserDataQuery.temCartItemModelList;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView notificationRecycler;
    public static  NotificationAdaptor notificationAdaptor;
    private TextView noNotifyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_notification );


        Toolbar toolbar = findViewById( R.id.x_ToolBar );
        setSupportActionBar( toolbar );
        // Set Title on Action Menu
        try{
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar().setTitle( "Notifications" );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException e){
        }

        notificationRecycler = findViewById( R.id.notification_recyclerView );
        noNotifyText = findViewById( R.id.no_notification_text );

        // Set value of cart variables...
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this );
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        notificationRecycler.setLayoutManager( linearLayoutManager );

        notificationAdaptor = new NotificationAdaptor( notificationModelList );
        notificationRecycler.setAdapter( notificationAdaptor );
        notificationAdaptor.notifyDataSetChanged();
        setVisibility();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisibility();
        if (notificationAdaptor!=null)
            notificationAdaptor.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            return true;
        }else

        return super.onOptionsItemSelected( item );
    }

    private void setVisibility(){
        if (notificationModelList.size()==0){
            noNotifyText.setVisibility( View.VISIBLE );
            notificationRecycler.setVisibility( View.GONE );
        }else{
            noNotifyText.setVisibility( View.GONE );
            notificationRecycler.setVisibility( View.VISIBLE );
        }
    }


}
