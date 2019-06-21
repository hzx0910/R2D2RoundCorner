package make.more.r2d2.round_corner;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by HeZX on 2019-06-19.
 */
public class RoundHelper {

    private float[] radii = new float[8];  // top-left, top-right, bottom-right, bottom-left
    private Path clipPath;                 // 剪裁区域
    private Paint paint;                   // 画笔
    private ColorStateList strokeColor;    // 描边颜色
    private int strokeWidth;               // 描边半径
    private RectF layer;                   // 画布图层大小
    private int radius;                    // 圆角大小
    private Drawable bg;                   // 背景
    private ColorStateList bg_tint;        // 背景tint颜色
    private PorterDuff.Mode bg_tint_mode;  // 背景tint模式

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void init(Context context, View view, AttributeSet attrs) {
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundLayout);
        strokeColor = ta.getColorStateList(R.styleable.RoundLayout_round_stroke_color);
        strokeWidth = ta.getDimensionPixelSize(R.styleable.RoundLayout_round_stroke_width, 0);
        if (radius == 0) {
            radius = ta.getDimensionPixelSize(R.styleable.RoundLayout_round_radius, 0);
        }
        bg = ta.getDrawable(R.styleable.RoundLayout_round_bg);
        bg_tint = ta.getColorStateList(R.styleable.RoundLayout_round_bg_tint);
        bg_tint_mode = parseTintMode(ta.getInt(R.styleable.RoundLayout_round_bg_tint_mode, -1), PorterDuff.Mode.SRC_IN);

        int radiusLeft = ta.getDimensionPixelSize(R.styleable.RoundLayout_round_radius_left, radius);
        int radiusRight = ta.getDimensionPixelSize(R.styleable.RoundLayout_round_radius_right, radius);
        int radiusTop = ta.getDimensionPixelSize(R.styleable.RoundLayout_round_radius_top, radius);
        int radiusBottom = ta.getDimensionPixelSize(R.styleable.RoundLayout_round_radius_bottom, radius);

        int radiusTopLeft = ta.getDimensionPixelSize(R.styleable.RoundLayout_round_radius_top_left, radiusLeft > 0 ? radiusLeft : radiusTop);
        int radiusTopRight = ta.getDimensionPixelSize(R.styleable.RoundLayout_round_radius_top_right, radiusRight > 0 ? radiusRight : radiusTop);
        int radiusBottomLeft = ta.getDimensionPixelSize(R.styleable.RoundLayout_round_radius_bottom_left, radiusLeft > 0 ? radiusLeft : radiusBottom);
        int radiusBottomRight = ta.getDimensionPixelSize(R.styleable.RoundLayout_round_radius_bottom_right, radiusRight > 0 ? radiusRight : radiusBottom);
        ta.recycle();

        radii[0] = radiusTopLeft;
        radii[1] = radiusTopLeft;

        radii[2] = radiusTopRight;
        radii[3] = radiusTopRight;

        radii[4] = radiusBottomRight;
        radii[5] = radiusBottomRight;

        radii[6] = radiusBottomLeft;
        radii[7] = radiusBottomLeft;

        layer = new RectF();
        clipPath = new Path();
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
    }

    @SuppressWarnings("unused")
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        layer.set(0, 0, w, h);
        clipPath.reset();
        clipPath.addRoundRect(layer, radii, Path.Direction.CCW);
    }

    public void drawBG(Canvas canvas, int[] drawableState) {
        canvas.saveLayer(layer, null, Canvas.ALL_SAVE_FLAG);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        if (bg != null) {
            bg.setState(drawableState);
            bg.setBounds(0, 0, (int) layer.width(), (int) layer.height());
            bg.draw(canvas);
            if (bg_tint != null) {
                paint.setColor(bg_tint.getColorForState(drawableState, bg_tint.getDefaultColor()));
                paint.setStyle(Paint.Style.FILL);
                paint.setXfermode(new PorterDuffXfermode(bg_tint_mode));
                Path path = new Path();
                path.addRect(0, 0, (int) layer.width(), (int) layer.height(), Path.Direction.CW);
                canvas.drawPath(path, paint);
            }
        }
    }

    public void drawableStateChanged(View view) {
        boolean refresh = false;
        int[] drawableState = view.getDrawableState();
        if (bg != null && bg.isStateful()) {
            if (bg.setState(drawableState)) {
                refresh = true;
            }
        }
        if (strokeColor != null && strokeColor.isStateful()) {
            refresh = true;
        }
        if (bg_tint != null && bg_tint.isStateful()) {
            refresh = true;
        }
        if (refresh) view.invalidate();
    }

    public void drawClip(Canvas canvas, int[] drawableState) {
        if (strokeWidth > 0 && strokeColor != null) {
            paint.setStrokeWidth(strokeWidth * 2);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(clipPath, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
            paint.setColor(strokeColor.getColorForState(drawableState, strokeColor.getDefaultColor()));
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(clipPath, paint);
        }
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        if (Build.VERSION.SDK_INT > 18) {
            Path path = new Path();
            path.addRect(0, 0, (int) layer.width(), (int) layer.height(), Path.Direction.CW);
            path.op(clipPath, Path.Op.DIFFERENCE);
            canvas.drawPath(path, paint);
        } else {
            clipUnder18(canvas);
        }
    }

    private void clipUnder18(Canvas canvas) {
        float topLeft = radii[0];
        float topRight = radii[2];
        float bottomRight = radii[4];
        float bottomLeft = radii[6];

        Path path = new Path();
        if (topLeft > 0) {
            path.reset();
            path.moveTo(0, topLeft);
            path.lineTo(0, 0);
            path.lineTo(topLeft, 0);
            path.arcTo(new RectF(0, 0, topLeft * 2, topLeft * 2),
                    -90, -90);
            path.close();
            canvas.drawPath(path, paint);
        }
        if (topRight > 0) {
            float width = layer.width();
            path.reset();
            path.moveTo(width - topRight, 0);
            path.lineTo(width, 0);
            path.lineTo(width, topRight);
            path.arcTo(new RectF(width - 2 * topRight, 0, width,
                    topRight * 2), 0, -90);
            path.close();
            canvas.drawPath(path, paint);
        }
        if (bottomRight > 0) {
            float height = layer.height();
            float width = layer.width();
            path.reset();
            path.moveTo(width - bottomRight, height);
            path.lineTo(width, height);
            path.lineTo(width, height - bottomRight);
            path.arcTo(new RectF(width - 2 * bottomRight, height - 2
                    * bottomRight, width, height), 0, 90);
            path.close();
            canvas.drawPath(path, paint);
        }
        if (bottomLeft > 0) {
            float height = layer.height();
            path.reset();
            path.moveTo(0, height - bottomLeft);
            path.lineTo(0, height);
            path.lineTo(bottomLeft, height);
            path.arcTo(new RectF(0, height - 2 * bottomLeft,
                    bottomLeft * 2, height), 90, 90);
            path.close();
            canvas.drawPath(path, paint);
        }
    }

    public static PorterDuff.Mode parseTintMode(int value, PorterDuff.Mode defaultMode) {
        switch (value) {
            case 3:
                return PorterDuff.Mode.SRC_OVER;
            case 5:
                return PorterDuff.Mode.SRC_IN;
            case 9:
                return PorterDuff.Mode.SRC_ATOP;
            case 14:
                return PorterDuff.Mode.MULTIPLY;
            case 15:
                return PorterDuff.Mode.SCREEN;
            case 16:
                return PorterDuff.Mode.ADD;
            default:
                return defaultMode;
        }
    }
}
