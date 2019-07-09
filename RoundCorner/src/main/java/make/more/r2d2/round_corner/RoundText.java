package make.more.r2d2.round_corner;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

import make.more.r2d2.round_corner.help.RoundAble;
import make.more.r2d2.round_corner.help.RoundHelper;

/**
 * Created by HeZX on 2019/6/19.
 */
public class RoundText extends TextView implements RoundAble {

    RoundHelper helper = new RoundHelper();

    public RoundText(Context context) {
        this(context, null);
    }

    public RoundText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        helper.init(context, this, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        helper.onSizeChanged(w, h, oldW, oldH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        helper.drawBG(canvas, getDrawableState());
        super.onDraw(canvas);
        helper.drawClip(canvas, getDrawableState());
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (helper != null) helper.drawableStateChanged(this);
    }

    @Override
    public RoundHelper getRoundHelper() {
        return helper;
    }
}
