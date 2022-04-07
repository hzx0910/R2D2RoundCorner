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
    private final static int DEFAULT_COLOR = 0x66000000;
    private final static int MODE_LAYER = 1;
    private final static int MODE_SHADER = 2;
    ColorStateList shadowColor;
    float radiusS;
    float dX;
    float dY;

    Path path;
    RectF rect;

    public static ShadowHelper init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ShadowLayout);
        if (array == null) return null;
        ShadowHelper helper;
        try {
            int mode = array.getInt(R.styleable.ShadowLayout_shadow_mode, MODE_LAYER);
            helper = mode == MODE_SHADER ? new ShadowHelperShader() : new ShadowHelperLayer();
            helper.radiusS = array.getDimension(R.styleable.ShadowLayout_shadow_radius, 0);
            helper.dX = array.getDimension(R.styleable.ShadowLayout_shadow_dx, 0);
            helper.dY = array.getDimension(R.styleable.ShadowLayout_shadow_dy, 0);
            if (array.hasValue(R.styleable.ShadowLayout_shadow_color))
                helper.shadowColor = array.getColorStateList(R.styleable.ShadowLayout_shadow_color);
            else helper.shadowColor = ColorStateList.valueOf(DEFAULT_COLOR);
        } finally {
            array.recycle();
        }
        helper.path = new Path();
        helper.rect = new RectF();
        helper.init();
        return helper;
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
