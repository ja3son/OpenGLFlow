package com.ja3son.libdemo.Sample7_8;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
public class GameActivity extends Activity {
    MySurfaceView mf;
	public SensorManager mySensorManager;	//SensorManager��������	
	public Sensor myAccelerometer; 	//����������
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //����Ϊȫ��
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//����Ϊ����ģʽ
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    	//���SensorManager����
        mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);	        
        myAccelerometer=mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		MySurfaceView mf=new MySurfaceView(this);
		mf.requestFocus();//��ȡ����
		mf.setFocusableInTouchMode(true);//����Ϊ�ɴ���
		setContentView(mf);//��ת��ӭ����
    }
	   //����ʵ����SensorEventListener�ӿڵĴ�����������
	  	public SensorEventListener mySensorListener = new SensorEventListener(){
	  		public void onAccuracyChanged(Sensor sensor, int accuracy) {

	  		}
	  		public void onSensorChanged(SensorEvent event) 
	  		{
	  			//�������������Ļ�ϵ�ͶӰ����
	  			float []values=event.values;//��ȡ�����᷽���ϵļ��ٶ�ֵ
	  			//System.out.println("v[0]="+values[0]+",,v[1]="+values[1]+",,v[2]="+values[2]);
	  			Constant.Angle=values[1];
	  		}
	  	};
	  	@Override
	  	protected void onResume() {
			super.onResume();
			
	    	mySensorManager.registerListener(
					mySensorListener, 		//��Ӽ���
					myAccelerometer, 		//����������
					SensorManager.SENSOR_DELAY_NORMAL	//�������¼����ݵ�Ƶ��
					);
		}
	    @Override
	    protected void onPause() {
	        super.onPause();
    	mySensorManager.unregisterListener(mySensorListener);//ȡ��ע�������
	}
}
	
