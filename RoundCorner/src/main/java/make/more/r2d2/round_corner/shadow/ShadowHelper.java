package make.more.r2d2.round_corner.shadow;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
public class ShadowHelper {

    private ColorStateList shadowColor;
    private float shadowRadius;
    private float dX;
    private float dY;

    private Paint shadowPaint;
    private Path shadowPath;
    private RectF rectCorner;

    public void init(Context context, View view, AttributeSet attrs) {
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ShadowLayout);
        if (array == null) return;
        try {
            shadowRadius = array.getDimension(R.styleable.ShadowLayout_shadow_radius, 0);
            dX = array.getDimension(R.styleable.ShadowLayout_shadow_dx, 0);
            dY = array.getDimension(R.styleable.ShadowLayout_shadow_dy, 0);
            if (array.hasValue(R.styleable.ShadowLayout_shadow_color))
                shadowColor = array.getColorStateList(R.styleable.ShadowLayout_shadow_color);
            else shadowColor = ColorStateList.valueOf(0x66000000);

        } finally {
            array.recycle();
        }

        shadowPaint = new Paint();
        shadowPaint.setStyle(Paint.Style.FILL);
        shadowPath = new Path();
        rectCorner = new RectF();
    }

    public void drawableStateChanged(View view) {
        boolean refresh = false;
        if (shadowColor != null && shadowColor.isStateful()) {
            refresh = true;
        }
        if (refresh) view.postInvalidate();
    }

    public void drawAllShadow(ViewGroup group, Canvas canvas, int[] drawableState) {
        for (int i = 0; i < group.getChildCount(); i++) {
            View view = group.getChildAt(i);
            if (view instanceof RoundAble) {
                canvas.save();
                canvas.translate(view.getLeft() - shadowRadius + dX, view.getTop() - shadowRadius + dY);
                drawShadow(canvas, view, (RoundAble) view, drawableState);
                canvas.restore();
            }
        }
    }

    private void drawShadow(Canvas canvas, View roundView, RoundAble roundAble, int[] drawableState) {
        float[] radii = roundAble.getRoundHelper().getRadii();
        float top = shadowRadius;
        float bottom = shadowRadius + roundView.getHeight();
        float left = shadowRadius;
        float right = shadowRadius + roundView.getWidth();

        float radiusTopLeft = radii[0];
        float radiusTopRight = radii[2];
        float radiusBottomRight = radii[4];
        float radiusBottomLeft = radii[6];

        shadowPath.reset();
        shadowPath.moveTo(left + radiusTopLeft, top);
        shadowPath.lineTo(right - radiusTopRight, top);
        rectCorner.set(right - 2 * radiusTopRight, top, right, top + 2 * radiusTopRight);
        shadowPath.arcTo(rectCorner, -90, 90, false);
        shadowPath.lineTo(right, bottom - radiusBottomRight);
        rectCorner.set(right - 2 * radiusBottomRight, bottom - 2 * radiusBottomRight, right, bottom);
        shadowPath.arcTo(rectCorner, 0, 90, false);
        shadowPath.lineTo(left + radiusBottomLeft, bottom);
        rectCorner.set(left, bottom - 2 * radiusBottomLeft, left + 2 * radiusBottomLeft, bottom);
        shadowPath.arcTo(rectCorner, 90, 90, false);
        shadowPath.lineTo(left, top + radiusTopLeft);
        rectCorner.set(left, top, left + 2 * radiusTopLeft, top + 2 * radiusTopLeft);
        shadowPath.arcTo(rectCorner, 180, 90, false);
        shadowPath.close();

        shadowPaint.setAntiAlias(true);
        shadowPaint.setColor(Color.TRANSPARENT);
        shadowPaint.setStyle(Paint.Style.FILL);

        int color = shadowColor.getColorForState(drawableState, shadowColor.getDefaultColor());
        //必须有透明度才能画出阴影 故alpha最大设置0xFE
        if (color >>> 24 == 0xFF) color -= 0x01000000;
        shadowPaint.setShadowLayer(shadowRadius, 0, 0, color);

        canvas.drawPath(shadowPath, shadowPaint);
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
        return shadowRadius;
    }

    public void setShadowRadius(float shadowRadius) {
        this.shadowRadius = shadowRadius;
    }

    public float getdX() {
        return dX;
    }

    public void setdX(float dX) {
        this.dX = dX;
    }

    public float getdY() {
        return dY;
    }

    public void setdY(float dY) {
        this.dY = dY;
    }
}
