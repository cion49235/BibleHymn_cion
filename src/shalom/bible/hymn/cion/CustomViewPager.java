package shalom.bible.hymn.cion;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import shalom.bible.hymn.cion.common.Const;
import shalom.bible.hymn.cion.util.PreferenceUtil;

public class CustomViewPager extends ViewPager {
	private boolean enabled;

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(PreferenceUtil.getIntSharedData(context, PreferenceUtil.PREF_SWIPE_MODE, Const.SWIPE_MODE) == 0){
        	this.enabled = true;
        }else{
        	this.enabled = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return enabled ? super.onTouchEvent(event) : false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return enabled ? super.onInterceptTouchEvent(event) : false;
    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isPagingEnabled() {
        return enabled;
    }

}
