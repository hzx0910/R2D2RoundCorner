package make.more.r2d2.round_corner.shadow;

import android.graphics.Shader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created by lyk on 2022/7/29
 * ShadowHelperShader缓存数据类
 *
 * @author liyikang
 */
public class ShadowHelperShaderModel {

    private ArrayList<Shader> shadersList;
    private float[] radii;
    private int color;
    private float height;
    private float width;
    private float radiusS;

    public ShadowHelperShaderModel() {
        this.shadersList = new ArrayList<>(8);
        this.radii = new float[8];
        this.color = -1;
        this.height = -1;
        this.width = -1;
        this.radiusS = -1;
    }

    public ShadowHelperShaderModel(ArrayList<Shader> shadersList, float[] radii, int color, float height, float width, float radiusS) {
        this.shadersList = shadersList;
        this.radii = radii;
        this.color = color;
        this.height = height;
        this.width = width;
        this.radiusS = radiusS;
    }

    public ArrayList<Shader> getShadersList() {
        return shadersList;
    }

    public void setShadersList(ArrayList<Shader> shadersList) {
        this.shadersList = shadersList;
    }

    public float[] getRadii() {
        return radii;
    }

    public void setRadii(float[] radii) {
        this.radii = radii;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getRadiusS() {
        return radiusS;
    }

    public void setRadiusS(float radiusS) {
        this.radiusS = radiusS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShadowHelperShaderModel that = (ShadowHelperShaderModel) o;
        return color == that.color
                && Float.compare(that.height, height) == 0
                && Float.compare(that.width, width) == 0
                && Float.compare(that.radiusS, radiusS) == 0
                && Arrays.equals(radii, that.radii);
    }

    @Override
    public int hashCode() {
        int result = 0;
        if (android.os.Build.VERSION.SDK_INT >= 19) {
            result = Objects.hash(color, height, width, radiusS);
        }
        result = 31 * result + Arrays.hashCode(radii);
        return result;
    }

}
