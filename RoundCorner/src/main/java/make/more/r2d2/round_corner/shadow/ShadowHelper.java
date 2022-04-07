package make.more.r2d2.round_corner.shadow;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import make.more.r2d2.round_corner.R;
import make.more.r2d2.round_corner.help.RoundAble;

/**
 * Created by HeZX on 2019-07-15.
 */
public abstract class ShadowHelper {

    ColorStateList shadowColor;
    float radiusS;
    float dX;
    float dY;

    Path path;
    RectF rect;

    public void init(Context context, View view, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ShadowLayout);
        if (array == null) return;
        try {
            radiusS = array.getDimension(R.styleable.ShadowLayout_shadow_radius, 0);
            dX = array.getDimension(R.styleable.ShadowLayout_shadow_dx, 0);
            dY = array.getDimension(R.styleable.ShadowLayout_shadow_dy, 0);
            if (array.hasValue(R.styleable.ShadowLayout_shadow_color))
                shadowColor = array.getColorStateList(R.styleable.ShadowLayout_shadow_color);
            else shadowColor = ColorStateList.valueOf(0x66000000);

        } finally {
            array.recycle();
        }
        path = new Path();
        rect = new RectF();
        init();
    }

    /*** 初始化 ***/
    abstract void init();

    public void drawAllShadow(ViewGroup group, Canvas canvas, int[] drawableState) {
        for (int i = 0; i < group.getChildCount(); i++) {
            View view = group.getChildAt(i);
            if (view instanceof RoundAble) {
                canvas.save();
                canvas.translate(view.getLeft() + dX, view.getTop() + dY);
                drawShadow(canvas, view, (RoundAble) view, drawableState);
                canvas.restore();
            }
        }
    }

    abstract void drawShadow(Canvas canvas, View view, RoundAble roundAble, int[] drawableState);

    public void drawableStateChanged(View view) {
        boolean refresh = false;
        if (shadowColor != null && shadowColor.isStateful()) {
            refresh = true;
        }
        if (refresh) view.postInvalidate();
    }

    public ColorStateList getShadowColor() {
        return shadowColor;
    }

    public void setShadowColor(int color) {
        this.shadowColor = ColorStateList.valueOf(color);
    }

    public void setShadowColor(ColorStateList shadowColor) {
        this.shadowColor = shadowColor;
    }

    public float getShadowRadius() {
        return radiusS;
    }

    public void setShadowRadius(float radiusS) {
        this.radiusS = radiusS;
    }

    public float getDx() {
        return dX;
    }

    public void setDx(float dX) {
        this.dX = dX;
    }

    public float getDy() {
        return dY;
    }

    public void setDy(float dY) {
        this.dY = dY;
    }
}
