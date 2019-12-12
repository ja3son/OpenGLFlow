package com.ja3son.libdemo.Sample7_7;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class Sample7_7_Activity extends Activity {
	private MySurfaceView mGLSurfaceView;
	static float screenWidth;//��Ļ���
	static float screenHeight;//��Ļ�߶�
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ������
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);//ȥ����ͷ
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//ǿ�ƺ���
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth=dm.widthPixels;			//dm.widthPixels    ��ȡ��Ļ����ֱ���
        screenHeight=dm.heightPixels;		//dm.heightPixels	��ȡ��Ļ����ֱ���
        
        mGLSurfaceView = new MySurfaceView(this);
        mGLSurfaceView.requestFocus();//��ȡ����
        mGLSurfaceView.setFocusableInTouchMode(true);//����Ϊ�ɴ���
        
        setContentView(mGLSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    } 
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent e)
    {
    	if(keyCode==4)
    	{
    		System.exit(0);
    	}
		return false;
    }
}



