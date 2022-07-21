package make.more.r2d2.round_corner.help;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.util.LruCache;

/**
 * Created by lyk on 2022/7/4
 *
 * @author liyikang
 */
public class BitmapLruCacheUtil {
    private static final String TAG = BitmapLruCacheUtil.class.getSimpleName();

    private LruCache<Integer, Bitmap> mLruMap;

    private volatile static BitmapLruCacheUtil mInstance;

    public static BitmapLruCacheUtil getInstance() {
        if (mInstance == null) {
            synchronized (BitmapLruCacheUtil.class) {
                if (mInstance == null) {
                    mInstance = new BitmapLruCacheUtil();
                }
            }
        }
        return mInstance;
    }

    public void init() {
        initLruCache();
    }

    private void initLruCache() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int maxSize = maxMemory / 8;
        mLruMap = new LruCache<Integer, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(Integer key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }

            @Override
            protected void entryRemoved(boolean evicted, Integer key, Bitmap oldValue, Bitmap newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
                oldValue.recycle();
            }

        };
    }

    /**
     * 从lruCache中拿到bitmap
     * @param width
     * @param height
     * @param config
     * @return
     */
    public Bitmap getBitmapFromLruCache(int width, int height, Bitmap.Config config) {
        int size = getBitmapByteSize(width, height, config);
        Bitmap result = mLruMap.get(size);
        return result;
    }


    /**
     * 将bitmap放入lruCache缓存
     * @param width
     * @param height
     * @param config
     * @param value
     */
    public void putBitmapToLruCache(int width, int height, Bitmap.Config config, Bitmap value) {
        int size = getBitmapByteSize(width, height, config);
        mLruMap.put(size, value);
    }

    /**
     * 清除缓存
     */
    public void clearLruCache() {
        mLruMap.evictAll();
    }

    /**
     * 不使用时释放
     */
    public void release() {
        mLruMap = null;
    }


    public static int getBitmapByteSize(int width, int height, Bitmap.Config config) {
        return width * height * getBytesPerPixel(config);
    }

    private static int getBytesPerPixel(Bitmap.Config config) {
        if (config == null) {
            config = Bitmap.Config.ARGB_8888;
        }

        byte bytesPerPixel;
        switch(config) {
            case ALPHA_8:
                bytesPerPixel = 1;
                break;
            case RGB_565:
            case ARGB_4444:
                bytesPerPixel = 2;
                break;
            case RGBA_F16:
                bytesPerPixel = 8;
                break;
            case ARGB_8888:
            default:
                bytesPerPixel = 4;
        }

        return bytesPerPixel;
    }


}
