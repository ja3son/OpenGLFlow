package com.ja3son.cgldemo;

import android.content.res.AssetManager;

public class GL2JNILib {
    static {
        System.loadLibrary("native-lib");
    }

    public static native void init(int width, int height);

    public static native void step();

    public static native void nativeSetAssetManager(AssetManager am);
}
