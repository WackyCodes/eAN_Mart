package wackycodes.ecom.eanmart.apphome.mainhome;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import wackycodes.ecom.eanmart.R;

public class BannerSliderViewHolder extends RecyclerView.ViewHolder {
    //------ View Pager for Banner Slider...
    private ViewPager bannerSliderViewPager;
    private int BANNER_CURRENT_PAGE;
    private final long DELAY_TIME = 3000;
    private final long PERIOD_TIME = 3000;
    private Timer timer;
//    private List<BannerSliderModel> arrangedList;
    //------ View Paager for Banner Slidder...

    public BannerSliderViewHolder(@NonNull View itemView) {
        super( itemView );
//        bannerSliderViewPager = itemView.findViewById( R.id.banner_slider_viewPager );
    }

  /*  // BannerSlidder ---
    private void setBannerSliderViewPager(final List <BannerSliderModel> bannerSliderModelList) {
        BANNER_CURRENT_PAGE = 2;
        if (timer != null){
            timer.cancel();
        }

        arrangedList = new ArrayList <>();
        for(int i =0; i < bannerSliderModelList.size(); i++){
            arrangedList.add( i, bannerSliderModelList.get( i ) );
        }
        arrangedList.add( 2, bannerSliderModelList.get( bannerSliderModelList.size() - 2 ) );
        arrangedList.add( 1, bannerSliderModelList.get( bannerSliderModelList.size() - 1 ) );
        arrangedList.add( bannerSliderModelList.get( 0 ) );
        arrangedList.add( bannerSliderModelList.get( 1 ) );

        BannerSliderAdaptor bannerSliderAdaptor = new BannerSliderAdaptor( arrangedList );
        bannerSliderViewPager.setAdapter( bannerSliderAdaptor );
        bannerSliderViewPager.setClipToPadding( false );
        bannerSliderViewPager.setPageMargin( 20 );
        bannerSliderViewPager.setCurrentItem( BANNER_CURRENT_PAGE );

        ViewPager.OnPageChangeListener viewPagerOnPageChange = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BANNER_CURRENT_PAGE = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    bannerSlideLooper( arrangedList );
                }
            }
        };
        bannerSliderViewPager.addOnPageChangeListener( viewPagerOnPageChange );
        startBannerSliderAnim( arrangedList );

        bannerSliderViewPager.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                bannerSlideLooper( arrangedList );
                stopBannerSliderAnim();
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    startBannerSliderAnim( arrangedList );
                }
                return false;
            }
        } );

        bannerSliderViewPager.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : OnClick Banner...
            }
        } );

        //------ View Paager for Banner Slidder...
    }

    private void bannerSlideLooper(List <BannerSliderModel> bannerSliderModelList) {
        if (BANNER_CURRENT_PAGE == bannerSliderModelList.size() - 2) {
            BANNER_CURRENT_PAGE = 2;
            bannerSliderViewPager.setCurrentItem( BANNER_CURRENT_PAGE, false );
        }
        if (BANNER_CURRENT_PAGE == 1) {
            BANNER_CURRENT_PAGE = bannerSliderModelList.size() - 3;
            bannerSliderViewPager.setCurrentItem( BANNER_CURRENT_PAGE, false );
        }
    }

    private void startBannerSliderAnim(final List <BannerSliderModel> bannerSliderModelList) {
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if (BANNER_CURRENT_PAGE > bannerSliderModelList.size()) {
                    BANNER_CURRENT_PAGE = 1;
                }
                bannerSliderViewPager.setCurrentItem( BANNER_CURRENT_PAGE++, true );
            }
        };
        timer = new Timer();
        timer.schedule( new TimerTask() {
            @Override
            public void run() {
                handler.post( update );
            }
        }, DELAY_TIME, PERIOD_TIME );
    }

    private void stopBannerSliderAnim() {
        timer.cancel();
    }
    // BannerSlidder ---
*/
}
