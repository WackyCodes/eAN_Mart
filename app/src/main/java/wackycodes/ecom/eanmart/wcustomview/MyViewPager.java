package wackycodes.ecom.eanmart.wcustomview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class MyViewPager extends ViewPager {

    public MyViewPager(@NonNull Context context) {
        super( context );
    }

    public MyViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super( context, attrs );
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec = 0;

        for (int i = 0; i < getChildCount(); i++){
            View view = getChildAt( i );
            view.measure( widthMeasureSpec, MeasureSpec.makeMeasureSpec( 0, MeasureSpec.UNSPECIFIED ) );
            int h = view.getMeasuredHeight();
            if (h>heightSpec)
                heightSpec = h;
        }

        if (heightSpec != 0){
            heightMeasureSpec = MeasureSpec.makeMeasureSpec( heightSpec, MeasureSpec.EXACTLY );
        }

        super.onMeasure( widthMeasureSpec, heightMeasureSpec );

    }




}
