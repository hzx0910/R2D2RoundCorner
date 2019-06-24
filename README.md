# R2D2RoundCorner

Android roundCorner layout and view

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
        implementation 'com.github.hzx0910:R2D2RoundCorner:1.0.1'
}
```

**Set each corner in your layout.xml!**
```xml

    <make.more.r2d2.round_corner.RoundText
        style="@style/base_button"
        android:layout_height="120dp"
        android:clickable="true"
        android:focusable="true"
        app:round_bg="@mipmap/android"
        app:round_bg_tint="@color/stroke_stateful"
        app:round_bg_tint_mode="add"
        app:round_radius="60dp"
        app:round_radius_top_right="30dp"
        app:round_stroke_color="@color/stroke_stateful"
        app:round_stroke_width="2dp" />
```