package com.ja3son.demo.objects.torus;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.ja3son.temp.R;


public class TorusActivity extends Activity {
	MySurfaceView mySurfaceView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //����Ϊȫ��
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//����Ϊ����ģʽ
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//�л���������
		setContentView(R.layout.torus);
		//��ʼ��GLSurfaceView
		mySurfaceView = new MySurfaceView(this);
		mySurfaceView.requestFocus();//��ȡ����
		mySurfaceView.setFocusableInTouchMode(true);//����Ϊ�ɴ���  
        //���Զ����GLSurfaceView��ӵ����LinearLayout��
        LinearLayout ll=(LinearLayout)findViewById(R.id.main_liner); 
        ll.addView(mySurfaceView); 
        
        
        //ΪRadioButton��Ӽ�������SxTѡ�����
        RadioButton rab=(RadioButton)findViewById(R.id.RadioButton01);
        rab.setOnCheckedChangeListener(
            new OnCheckedChangeListener()
            {
     			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) 
     			{
     				//�����
     				if(isChecked)
     				{
     					mySurfaceView.drawWhatFlag=true;
     				}
     			}        	   
            }         		
        );       
        rab=(RadioButton)findViewById(R.id.RadioButton02);
        
        rab.setOnCheckedChangeListener(
            new OnCheckedChangeListener()
            {
     			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) 
     			{
     				//�����
     				if(isChecked)
     				{
     					mySurfaceView.drawWhatFlag=false;
     				}
     			}        	   
            }         		
        );         
    }
    @Override
    protected void onResume() {
        super.onResume();
        mySurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mySurfaceView.onPause();
    }  
    public boolean onKeyDown(int keyCode,KeyEvent e)
    {
    	switch(keyCode)
        	{
    	case 4:
    		System.exit(0);
    		break;
        	}
    	return true;
    };
}