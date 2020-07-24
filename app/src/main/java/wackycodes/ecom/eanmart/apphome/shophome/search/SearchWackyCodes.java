package wackycodes.ecom.eanmart.apphome.shophome.search;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wackycodes.ecom.eanmart.MainActivity;
import wackycodes.ecom.eanmart.apphome.category.ShopItemModel;
import wackycodes.ecom.eanmart.other.DialogsClass;

import static wackycodes.ecom.eanmart.databasequery.DBQuery.firebaseFirestore;
import static wackycodes.ecom.eanmart.other.StaticValues.CURRENT_CITY_CODE;
import static wackycodes.ecom.eanmart.other.StaticValues.MAIN_ACTIVITY;

public class SearchWackyCodes {

    public SearchWackyCodes() {
    }

    private static List <ShopItemModel> searchShopItemList = new ArrayList <>();
    private static List<String> searchShopTags = new ArrayList <>();

    public static void getShopSearchItems(final Context context, final int activity , final Dialog dialog,
                                          SearchView searchView, final SearchAdapter searchAdaptor ){
        // Search Methods...
        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                dialog.show();
                searchShopItemList.clear();
                searchShopTags.clear();
                final String [] tags = query.toLowerCase().split( " " );
                for ( final String tag : tags ){
                    firebaseFirestore
                            .collection( "HOME_PAGE" )
                            .document( CURRENT_CITY_CODE.toUpperCase() )
                            .collection( "SHOPS" )
                            .whereArrayContainsAny( "tags", Arrays.asList( tags ) )
//                            .whereArrayContains( "tags", tag.trim() )
                            .get().addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task <QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                                    ShopItemModel model = new ShopItemModel(
                                            documentSnapshot.getId(),
                                            documentSnapshot.get( "shop_name" ).toString(),
                                            documentSnapshot.get( "shop_logo" ).toString(),
                                            documentSnapshot.get( "shop_rating" ).toString(),
                                            documentSnapshot.get( "shop_category_name" ).toString(),
                                            Integer.parseInt( documentSnapshot.get( "shop_veg_non_type" ).toString() )
                                    );
                                    if ( !searchShopTags.contains( model.getShopID() )){
                                        searchShopItemList.add( model );
                                        searchShopTags.add( model.getShopID() );
                                    }

                                }
                                if (searchShopItemList.size()>0){
                                    setFrameVisibility(activity, false);
                                }else{
                                    setFrameVisibility(activity,true);
                                }
                                if (tag.equals(tags[tags.length - 1])){
                                    if (searchShopTags.isEmpty()){
                                        DialogsClass.alertDialog( context, null, "No Shop found.!" ).show();
                                        setFrameVisibility(activity,true);
                                    }else{
                                        searchAdaptor.getFilter().filter( query );
                                    }
                                    dialog.dismiss();
                                }
                                dialog.dismiss();
//                                closeKeyboard();
                            }else{
                                // error...
                                dialog.dismiss();
                                showToast(  context,"Failed ! Product Not found.!" );
                                setFrameVisibility(activity, true);
                            }
                        }
                    } );
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        } );
        searchView.setOnCloseListener( new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                setFrameVisibility(activity, true);
                return false;
            }
        } );

    }

    public static void showToast(Context context, String msg){
        Toast.makeText( context, msg, Toast.LENGTH_SHORT ).show();
    }

    public static void setFrameVisibility(int activity, boolean val){
        switch (activity){
            case MAIN_ACTIVITY:
                MainActivity.setFrameVisibility( val );
                break;
            default:
                break;
        }
    }


    public static void shopSearchQuery(final Context context, final Dialog dialog, final int activity, final String query, final SearchAdapter searchAdaptor ){
        dialog.show();
        searchShopItemList.clear();
        searchShopTags.clear();
        final String [] tags = query.toLowerCase().split( " " );
        for ( final String tag : tags ){
            firebaseFirestore
                    .collection( "HOME_PAGE" )
                    .document( CURRENT_CITY_CODE.toUpperCase() )
                    .collection( "SHOPS" )
                    .whereArrayContainsAny( "tags", Arrays.asList( tags ) )
//                            .whereArrayContains( "tags", tag.trim() )
                    .get().addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task <QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                            ShopItemModel model = new ShopItemModel(
                                    documentSnapshot.getId(),
                                    documentSnapshot.get( "shop_name" ).toString(),
                                    documentSnapshot.get( "shop_logo" ).toString(),
                                    documentSnapshot.get( "shop_rating" ).toString(),
                                    documentSnapshot.get( "shop_category_name" ).toString(),
                                    Integer.parseInt( documentSnapshot.get( "shop_veg_non_type" ).toString() )
                            );
                            if ( !searchShopTags.contains( model.getShopID() )){
                                searchShopItemList.add( model );
                                searchShopTags.add( model.getShopID() );
                            }

                        }
                        if (searchShopItemList.size()>0){
                            setFrameVisibility(activity, false);
                        }else{
                            setFrameVisibility(activity,true);
                        }
                        if (tag.equals(tags[tags.length - 1])){
                            if (searchShopTags.isEmpty()){
                                DialogsClass.alertDialog( context, null, "No Shop found.!" ).show();
                                setFrameVisibility(activity,true);
                            }else{
                                searchAdaptor.getFilter().filter( query );
                            }
                            dialog.dismiss();
                        }
                        dialog.dismiss();
//                                closeKeyboard();
                    }else{
                        // error...
                        dialog.dismiss();
                        showToast( context,"Failed ! Product Not found.!" );
                        setFrameVisibility(activity, true);
                    }
                }
            } );
        }
    }


}
