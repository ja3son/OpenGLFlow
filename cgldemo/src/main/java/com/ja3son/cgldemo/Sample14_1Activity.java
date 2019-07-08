package com.ja3son.cgldemo;

import android.app.Activity;
import android.os.Bundle;

public class Sample14_1Activity extends Activity {

    GL2JNIView mView;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        GL2JNILib.nativeSetAssetManager(this.getAssets());
        mView = new GL2JNIView(getApplication());
        mView.requestFocus();
        mView.setFocusableInTouchMode(true);
        setContentView(mView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mView.onResume();
    }
}
