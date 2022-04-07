package make.more.r2d2.round_corner;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import make.more.r2d2.round_corner.shadow.ShadowAble;
import make.more.r2d2.round_corner.shadow.ShadowHelper;

/**
 * Created by HeZX on 2019-07-15.
 */
public class ShadowLinear extends LinearLayout implements ShadowAble {

    ShadowHelper helper;


    public ShadowLinear(Context context) {
        this(context, null);
    }

    public ShadowLinear(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowLinear(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        helper = ShadowHelper.init(context, attrs);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (helper != null) helper.drawAllShadow(this, canvas, getDrawableState());
        super.dispatchDraw(canvas);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (helper != null) helper.drawableStateChanged(this);
    }

    public ShadowHelper getShadowHelper() {
        return helper;
    }

}
