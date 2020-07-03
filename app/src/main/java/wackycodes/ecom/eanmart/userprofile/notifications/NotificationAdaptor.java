package wackycodes.ecom.eanmart.userprofile.notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import wackycodes.ecom.eanmart.R;

import static wackycodes.ecom.eanmart.other.StaticValues.NOTIFY_OFFER;
import static wackycodes.ecom.eanmart.other.StaticValues.NOTIFY_ORDER_ACCEPTED;
import static wackycodes.ecom.eanmart.other.StaticValues.NOTIFY_ORDER_CANCEL;
import static wackycodes.ecom.eanmart.other.StaticValues.NOTIFY_ORDER_DELIVERED;
import static wackycodes.ecom.eanmart.other.StaticValues.NOTIFY_ORDER_ON_DELIVERY;
import static wackycodes.ecom.eanmart.other.StaticValues.NOTIFY_SIMPLE;

public class NotificationAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NotificationModel> notificationModelList;

    public NotificationAdaptor(List <NotificationModel> notificationModelList) {
        this.notificationModelList = notificationModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (notificationModelList.get( position ).getType()){
            case NOTIFY_SIMPLE:
                return NOTIFY_SIMPLE;
            case NOTIFY_ORDER_ACCEPTED:
                return NOTIFY_ORDER_ACCEPTED;
            case NOTIFY_ORDER_ON_DELIVERY:
                return NOTIFY_ORDER_ON_DELIVERY;
            case NOTIFY_ORDER_DELIVERED:
                return NOTIFY_ORDER_DELIVERED;
            case NOTIFY_ORDER_CANCEL:
                return NOTIFY_ORDER_CANCEL;
            case NOTIFY_OFFER:
                return NOTIFY_OFFER;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case NOTIFY_SIMPLE:
            case NOTIFY_ORDER_ACCEPTED:
            case NOTIFY_ORDER_ON_DELIVERY:
                View simpleView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.notify_sample_lay_item, parent, false );
                return new NotificationSimpleViewHolder( simpleView );
            case NOTIFY_ORDER_DELIVERED:
                View oneBtnView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.notify_one_btn_lay_item, parent, false );
                return new NotifyOneButtonViewHolder( oneBtnView );
            case NOTIFY_ORDER_CANCEL:
            case NOTIFY_OFFER:
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        NotificationModel notificationModel = notificationModelList.get( position );
        int notifyType = notificationModel.getType();
        String notifyID = notificationModel.getNotifyID();
        String notifyImgLink = notificationModel.getNotifyImage();
        String notifyTitle = notificationModel.getNotifyTitle();
        String notifyBodyMsg = notificationModel.getNotifyBody();
        String notifyDate = notificationModel.getNotifyDate();
        String notifyTime = notificationModel.getNotifyTime();
        Boolean notifyIsRead = notificationModel.getNotifyIsRead();
        switch (notificationModel.getType()) {
            case NOTIFY_SIMPLE:
            case NOTIFY_ORDER_ACCEPTED:
            case NOTIFY_ORDER_ON_DELIVERY:
                ((NotificationSimpleViewHolder) holder).setData( notifyType, notifyID, notifyImgLink, notifyTitle, notifyBodyMsg, notifyDate, notifyTime,notifyIsRead );
                break;
            case NOTIFY_ORDER_DELIVERED:
                String clickId = notificationModel.getNotifyClickID();
                ((NotifyOneButtonViewHolder) holder).setData( notifyType, notifyID, clickId, notifyImgLink, notifyTitle, notifyBodyMsg, notifyDate, notifyTime,notifyIsRead );
                break;
            case NOTIFY_ORDER_CANCEL:
            case NOTIFY_OFFER:
            default:
                // TODO://?
                break;
        }
    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }

    public class NotificationSimpleViewHolder extends RecyclerView.ViewHolder{

        private ImageView notifyImage;
        private TextView notifyBody;
        private TextView notifyTime;

        public NotificationSimpleViewHolder(@NonNull View itemView) {
            super( itemView );
            notifyImage = itemView.findViewById( R.id.notify_image );
            notifyBody = itemView.findViewById( R.id.notify_body );
            notifyTime = itemView.findViewById( R.id.notify_time );
        }

        private void setData(int notifyType, String notifyID, String notifyImgLink, String notifyTitle, String notifyBodyMsg,
                             String notifyDate, String notifyTime, Boolean notifyIsRead){
            Glide.with( itemView.getContext() ).load( notifyImgLink )
                    .apply( new RequestOptions().placeholder( R.mipmap.logo_round) ).into( notifyImage );
            notifyBody.setText( notifyBodyMsg );


        }


    }

    public class NotifyOneButtonViewHolder extends RecyclerView.ViewHolder{
        private ImageView notifyImage;
        private TextView notifyBody;
        private TextView notifyTime;
        private TextView notifyTitle;
        private TextView notifyClickBtn;

        public NotifyOneButtonViewHolder(@NonNull View itemView) {
            super( itemView );
            notifyImage = itemView.findViewById( R.id.notify_image );
            notifyBody = itemView.findViewById( R.id.notify_body );
            notifyTime = itemView.findViewById( R.id.notify_time );
            notifyClickBtn = itemView.findViewById( R.id.notify_button );
        }

        private void setData(int notifyType, String notifyID, String notifyClickID, String notifyImgLink, String notifyTitle, String notifyBodyMsg,
                             String notifyDate, String notifyTime, Boolean notifyIsRead){
            Glide.with( itemView.getContext() ).load( notifyImgLink )
                    .apply( new RequestOptions().placeholder( R.mipmap.logo_round) ).into( notifyImage );
            notifyBody.setText( notifyBodyMsg );

        }

    }


}
