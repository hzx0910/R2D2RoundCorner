package make.more.r2d2.round_corner.shadow;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import make.more.r2d2.round_corner.help.RoundAble;

/**
 * Created by HeZX on 2019-07-15.
 * 采用 setShadowLayer 方法，需要关闭硬件加速
 */
class ShadowHelperLayer extends ShadowHelper {

    private Paint shadowPaint;

    ShadowHelperLayer() {
    }

    @Override
    void init() {
        shadowPaint = new Paint();
        shadowPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    void drawShadow(Canvas canvas, View roundView, RoundAble roundAble, int[] drawableState) {
        float[] radii = roundAble.getRoundHelper().getRadii();
        float top = 0;
        float bottom = roundView.getHeight();
        float left = 0;
        float right = roundView.getWidth();

        float radiusTopLeft = radii[0];
        float radiusTopRight = radii[2];
        float radiusBottomRight = radii[4];
        float radiusBottomLeft = radii[6];

        path.reset();
        path.moveTo(left + radiusTopLeft, top);
        path.lineTo(right - radiusTopRight, top);
        rect.set(right - 2 * radiusTopRight, top, right, top + 2 * radiusTopRight);
        path.arcTo(rect, -90, 90, false);
        path.lineTo(right, bottom - radiusBottomRight);
        rect.set(right - 2 * radiusBottomRight, bottom - 2 * radiusBottomRight, right, bottom);
        path.arcTo(rect, 0, 90, false);
        path.lineTo(left + radiusBottomLeft, bottom);
        rect.set(left, bottom - 2 * radiusBottomLeft, left + 2 * radiusBottomLeft, bottom);
        path.arcTo(rect, 90, 90, false);
        path.lineTo(left, top + radiusTopLeft);
        rect.set(left, top, left + 2 * radiusTopLeft, top + 2 * radiusTopLeft);
        path.arcTo(rect, 180, 90, false);
        path.close();

        shadowPaint.setAntiAlias(true);
        shadowPaint.setColor(Color.TRANSPARENT);
        shadowPaint.setStyle(Paint.Style.FILL);

        int color = shadowColor.getColorForState(drawableState, shadowColor.getDefaultColor());
        //必须有透明度才能画出阴影 故alpha最大设置0xFE
        if (color >>> 24 == 0xFF) color -= 0x01000000;
        shadowPaint.setShadowLayer(radiusS, 0, 0, color);

        canvas.drawPath(path, shadowPaint);
    }
}
