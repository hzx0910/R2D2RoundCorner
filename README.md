# R2D2RoundCorner

Android round corner layout and view with shadow

**Step 1. Add the JitPack repository to your root build.gradle:**
```groovy

allprojects {
    repositories {
        maven { url 'https://www.jitpack.io' }
    }
}
```

**Step 2. Add the dependency**
```groovy

dependencies {
        implementation 'com.github.hzx0910:R2D2RoundCorner:1.0.3'
}
```

**Step 3. Set each corner/shadow in your layout.xml**

|  PREVIEW   | CODE  |
|  ----  | ----  |
| ![avatar](preview/1.png)  | &lt;make.more.r2d2.round_corner.RoundText <br /> style="@style/base_button" <br /> android:clickable="true" <br /> android:text="Try Click!" <br /> app:round_bg="@color/stateful" <br /> app:round_radius="15dp" <br /> app:round_radius_top_left="6dp" <br /> app:round_stroke_color="@color/colorAccent" <br /> app:round_stroke_width="2dp" /&gt;|
| ![avatar](preview/2.png)  | &lt;make.more.r2d2.round_corner.ShadowFrame <br /> android:layout_width="wrap_content" <br /> android:layout_height="wrap_content" <br /> android:clickable="true" <br /> android:padding="6dp" <br /> app:shadow_color="@color/stateful" <br /> app:shadow_dx="2dp" <br /> app:shadow_dy="2dp" <br /> app:shadow_radius="2dp"&gt; <br />  <br /> &lt;make.more.r2d2.round_corner.RoundView <br /> android:layout_width="80dp" <br /> android:layout_height="40dp" <br /> app:round_bg="#8F8" <br /> app:round_radius="20dp" <br /> app:round_radius_bottom_left="4dp" /&gt; <br />  <br /> &lt;/make.more.r2d2.round_corner.ShadowFrame&gt; |