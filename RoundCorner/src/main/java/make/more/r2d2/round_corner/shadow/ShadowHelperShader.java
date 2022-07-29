package make.more.r2d2.round_corner.shadow;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.view.View;

import make.more.r2d2.round_corner.R;
import make.more.r2d2.round_corner.help.RoundAble;

/**
 * Created by HeZX on 2022-04-07.
 * 采用 setShader 方法，不用关闭硬件加速
 */
class ShadowHelperShader extends ShadowHelper {

    private Paint centerPaint;
    private Paint borderPaint;
    private Paint cornerPaint;
    private ShadowHelperShaderModel shaderModel;

    ShadowHelperShader() {
    }

    @Override
    void init() {
        centerPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        centerPaint.setStyle(Paint.Style.FILL);
        centerPaint.setAntiAlias(false);
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        borderPaint.setAntiAlias(false);
        cornerPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        cornerPaint.setAntiAlias(false);
        shaderModel = new ShadowHelperShaderModel();
    }

    @Override
    void drawShadow(Canvas canvas, View view, RoundAble roundAble, int[] drawableState) {
        float[] radii = roundAble.getRoundHelper().getRadii();
        int color = shadowColor.getColorForState(drawableState, shadowColor.getDefaultColor());
        {   //画中间的实心
            float top = radiusS;
            float bottom = view.getHeight() - radiusS;
            float left = radiusS;
            float right = view.getWidth() - radiusS;

            float radiusTL = Math.max(0, radii[0] - radiusS);
            float radiusTR = Math.max(0, radii[2] - radiusS);
            float radiusBR = Math.max(0, radii[4] - radiusS);
            float radiusBL = Math.max(0, radii[6] - radiusS);

            path.reset();
            path.setFillType(Path.FillType.EVEN_ODD);
            path.moveTo(left + radiusTL, top);
            path.lineTo(right - radiusTR, top);
            rect.set(right - 2 * radiusTR, top, right, top + 2 * radiusTR);
            path.arcTo(rect, -90, 90, false);
            path.lineTo(right, bottom - radiusBR);
            rect.set(right - 2 * radiusBR, bottom - 2 * radiusBR, right, bottom);
            path.arcTo(rect, 0, 90, false);
            path.lineTo(left + radiusBL, bottom);
            rect.set(left, bottom - 2 * radiusBL, left + 2 * radiusBL, bottom);
            path.arcTo(rect, 90, 90, false);
            path.lineTo(left, top + radiusTL);
            rect.set(left, top, left + 2 * radiusTL, top + 2 * radiusTL);
            path.arcTo(rect, 180, 90, false);
            path.close();

            centerPaint.setColor(color);
            canvas.drawPath(path, centerPaint);
        }
        {
            //画边上的虚线
            float h = view.getHeight();
            float w = view.getWidth();
            float radiusTL = radii[0];
            float radiusTR = radii[2];
            float radiusBR = radii[4];
            float radiusBL = radii[6];
            //必须有透明度才能画出阴影 故alpha最小设置0x01
            int to = color & 0x00FFFFFF;
            int[] colors = new int[]{color, to};
            int start = (color & 0xFF000000) == 0xFF000000 ? color : to;

            if(isNeedUpdate(view, shaderModel)){
                //上方渐变
                shaderModel.getShadersList().add(new LinearGradient(0, +radiusS, 0, -radiusS,
                        colors, null, Shader.TileMode.CLAMP));
                //下方渐变
                shaderModel.getShadersList().add(new LinearGradient(0, h - radiusS, 0, h + radiusS,
                        colors, null, Shader.TileMode.CLAMP));
                //左边渐变
                shaderModel.getShadersList().add(new LinearGradient(+radiusS, 0, -radiusS, 0,
                        colors, null, Shader.TileMode.CLAMP));
                //右边渐变
                shaderModel.getShadersList().add(new LinearGradient(w - radiusS, 0, w + radiusS, 0,
                        colors, null, Shader.TileMode.CLAMP));

                //--------------------------------------------画弧形-------------------------------------
                colors = new int[]{start, start, color, to};
                //左上弧形
                float percent = (radiusTL - radiusS) / (radiusTL + radiusS);
                shaderModel.getShadersList().add(new RadialGradient(radiusTL, radiusTL, radiusTL + radiusS,
                        colors, new float[]{0f, percent, percent, 1f}, Shader.TileMode.CLAMP));
                //右上弧形
                percent = (radiusTR - radiusS) / (radiusTR + radiusS);
                shaderModel.getShadersList().add(new RadialGradient(w - radiusTR, radiusTR, radiusTR + radiusS,
                        colors, new float[]{0f, percent, percent, 1f}, Shader.TileMode.CLAMP));
                //左下弧形
                percent = (radiusBL - radiusS) / (radiusBL + radiusS);
                shaderModel.getShadersList().add(new RadialGradient(radiusBL, h - radiusBL, radiusBL + radiusS,
                        colors, new float[]{0f, percent, percent, 1f}, Shader.TileMode.CLAMP));
                //右下弧形
                percent = (radiusBR - radiusS) / (radiusBR + radiusS);
                shaderModel.getShadersList().add(new RadialGradient(w - radiusBR, h - radiusBR, radiusBR + radiusS,
                        colors, new float[]{0f, percent, percent, 1f}, Shader.TileMode.CLAMP));

                //更新后将数据放入缓存
                shaderModel.setRadii(radii);
                shaderModel.setColor(color);
                shaderModel.setHeight(h);
                shaderModel.setWidth(w);
                shaderModel.setRadiusS(radiusS);
                view.setTag(R.id.tag_shadowHelperShader, shaderModel);
            }
            if(view.getTag(R.id.tag_shadowHelperShader) == null
                    || !(view.getTag(R.id.tag_shadowHelperShader) instanceof ShadowHelperShaderModel)){
                return;
            }
            //首先从缓存中取出
            shaderModel = (ShadowHelperShaderModel) view.getTag(R.id.tag_shadowHelperShader);
            //上方渐变
            borderPaint.setShader(shaderModel.getShadersList().get(0));
            canvas.drawRect(radiusTL, -radiusS, w - radiusTR, +radiusS, borderPaint);
            //下方渐变
            borderPaint.setShader(shaderModel.getShadersList().get(1));
            canvas.drawRect(radiusBL, h - radiusS, w - radiusBR, h + radiusS, borderPaint);
            //左边渐变
            borderPaint.setShader(shaderModel.getShadersList().get(2));
            canvas.drawRect(-radiusS, radiusTL, +radiusS, h - radiusBL, borderPaint);
            //右边渐变
            borderPaint.setShader(shaderModel.getShadersList().get(3));
            canvas.drawRect(w - radiusS, radiusTR, w + radiusS, h - radiusBR, borderPaint);

            //左上弧形
            cornerPaint.setShader(shaderModel.getShadersList().get(4));
            canvas.drawRect(-radiusS, -radiusS, radiusTL, radiusTL, cornerPaint);
            //右上弧形
            cornerPaint.setShader(shaderModel.getShadersList().get(5));
            canvas.drawRect(w - radiusTR, -radiusS, w + radiusS, radiusTR, cornerPaint);
            //左下弧形
            cornerPaint.setShader(shaderModel.getShadersList().get(6));
            canvas.drawRect(-radiusS, h - radiusBL, radiusBL, h + radiusS, cornerPaint);
            //右下弧形
            cornerPaint.setShader(shaderModel.getShadersList().get(7));
            canvas.drawRect(w - radiusBR, h - radiusBR, w + radiusS, h + radiusS, cornerPaint);
        }
    }

    /**
     * 是否需要更新缓存
     * @param view
     * @param shaderModel
     * @return
     */
    private boolean isNeedUpdate(View view, ShadowHelperShaderModel shaderModel){
        if(view.getTag(R.id.tag_shadowHelperShader) != null
                && view.getTag(R.id.tag_shadowHelperShader) instanceof ShadowHelperShaderModel){
            return !((ShadowHelperShaderModel)view.getTag(R.id.tag_shadowHelperShader)).equals(shaderModel);
        }
        return true;
    }
}
