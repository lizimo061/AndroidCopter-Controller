package com.bairuitech.demo; //[L,VideoActivity]

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bairuitech.anychat.AnyChatBaseEvent;
import com.bairuitech.anychat.AnyChatCoreSDK;
import com.bairuitech.anychat.AnyChatDefine;
import com.bairuitech.anychat.AnyChatRecordEvent;
import com.bairuitech.anychat.AnyChatTextMsgEvent;










//>JoystickDemo.Project0
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.Button;

import com.bairuitech.demo.JoyStickClass;
//<JoystickDemo.Project0


public class VideoActivity extends Activity implements AnyChatBaseEvent,AnyChatTextMsgEvent,AnyChatRecordEvent, OnClickListener {
	//Top bar
	//private MenuItem miPhoto,miVideo;//The button in the pull down menu
	
	//Layout and related variables
	private RelativeLayout rlLocal; //The layout of remote view
	private SurfaceView myView,otherView; //The view of local camera and remote camera
	//private ImageView mCameraSwitchImage; //The button that switch the front camera to back camera, or vice versa
	LayoutParams para1=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 0); //constant layout parameter
	LayoutParams para2=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f); //constant layout parameter
	
	//Variables required by the Anychat
	private ConfigEntity configEntity;
	public AnyChatCoreSDK anychat;
	int userID;
	boolean bOnPaused = false;
	
	//send commands
	//private MessageListView messageListView;
	//private ArrayList<String> messageList = new ArrayList<String> ();

	//video paras
	private boolean bSelfVideoOpened = false; //To check whether the local camera is turn on
	private boolean bOtherVideoOpened = false; //To check whether the remote camera is turn on
	private boolean bVideoAreaLoaded = false; //To check whether the view is loaded

	/*change the size of the view, backup
	private int dwLocalVideoWidth = 0;
	private int dwLocalVideoHeight = 0;
	private int dwRemoteVideoHeight = 0;
	private int dwRemoteVideoWidth = 0;*/

	//recording video
    private String VideoTodo;
    Chronometer cm1;
    long saveTime;
    
    
    private Camera camera;

    //>JoystickDemo.Project1
    JoyStickClass joyStick_L, joyStick_R;
	RelativeLayout layoutJoyStick_L, layoutJoyStick_R;
	ImageButton startSetting, startEngine, changeController, takePhoto;
	boolean takeOff = false;
	TextView directionText_L, directionText_R; //for test
    //<JoystickDemo.Project1
	@SuppressLint("HandlerLeak")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Toast.makeText(getApplicationContext(), "VideoActivity OnCreat", Toast.LENGTH_SHORT).show();
		super.onCreate(savedInstanceState);
		configEntity = ConfigService.LoadConfig(this);
		Intent intent = getIntent();
		userID = Integer.parseInt(intent.getStringExtra("UserID"));
		InitialSDK();
		InitialLayout();
		//>JoystickDemo.Project2
		layoutJoyStick_L = (RelativeLayout) findViewById(R.id.layout_joystick);
		layoutJoyStick_R = (RelativeLayout) findViewById(R.id.layout_joystick1);
		
		joyStick_L = new JoyStickClass(getApplicationContext(), layoutJoyStick_L, R.drawable.image_button);
		joyStick_L.setStickSize(150, 150); //Inner Circle, unit?
		joyStick_L.setLayoutSize(400, 400);//Outer Circle
		// joyStick_L.setLayoutAlpha(150);
		joyStick_L.setStickAlpha(100);
		joyStick_L.setOffset(90); //Unknown
		joyStick_L.setMinimumDistance(50);//Unknown
		
		joyStick_R = new JoyStickClass(getApplicationContext(), layoutJoyStick_R, R.drawable.image_button);
		joyStick_R.setStickSize(150, 150);
		joyStick_R.setLayoutSize(400, 400);
		// joyStick_R.setLayoutAlpha(150);
		joyStick_R.setStickAlpha(100);
		joyStick_R.setOffset(90);
		joyStick_R.setMinimumDistance(50);
		
		startSetting = (ImageButton) findViewById(R.id.settingButton);
		startSetting.setTag(0);
		startSetting.getBackground().setAlpha(0);
		startSetting.setOnClickListener(btnOnClick);
		
	    startEngine = (ImageButton) findViewById(R.id.flyButton);
		startEngine.setTag(1);
		startEngine.getBackground().setAlpha(0);
		startEngine.setOnClickListener(btnOnClick);

		changeController = (ImageButton) findViewById(R.id.changeControllerButton);
		changeController.setTag(2);
		changeController.getBackground().setAlpha(0);
		changeController.setOnClickListener(btnOnClick);
		
		takePhoto = (ImageButton) findViewById(R.id.takePhotoButton);
		takePhoto.setTag(3);
		takePhoto.getBackground().setAlpha(0);
		takePhoto.setOnClickListener(btnOnClick);
		
		directionText_L = (TextView) findViewById(R.id.textView0);
		directionText_R = (TextView) findViewById(R.id.textView1);
		
		layoutJoyStick_L.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				joyStick_L.drawStick(arg1);
			//	int direction = joyStick_L.get8Direction();
				directionText_L.setText(Integer.toString(joyStick_L.getPositionX()));
				directionText_R.setText(Integer.toString(joyStick_L.getPositionY()));//Display Joystick_L output
			/*	if (arg1.getAction() == MotionEvent.ACTION_DOWN || arg1.getAction() == MotionEvent.ACTION_MOVE) {
					// textView1.setText("X : " + String.valueOf(joyStick_L.getX()));
					// textView2.setText("Y : " + String.valueOf(joyStick_L.getY()));
					// textView3.setText("Angle : " + String.valueOf(joyStick_L.getAngle()));
					// textView4.setText("Distance : " + String.valueOf(joyStick_L.getDistance()));
					int direction = joyStick_L.get8Direction();
					directionText_L.setText(Integer.toString(direction));
					// if (direction == JoyStickClass_L.STICK_UP) {
					// textView5.setText("Direction : Up");
					// } else if (direction == JoyStickClass_L.STICK_UPRIGHT) {
					// textView5.setText("Direction : Up Right");
					// } else if (direction == JoyStickClass_L.STICK_RIGHT) {
					// textView5.setText("Direction : Right");
					// } else if (direction == JoyStickClass_L.STICK_DOWNRIGHT) {
					// textView5.setText("Direction : Down Right");
					// } else if (direction == JoyStickClass_L.STICK_DOWN) {
					// textView5.setText("Direction : Down");
					// } else if (direction == JoyStickClass_L.STICK_DOWNLEFT) {
					// textView5.setText("Direction : Down Left");
					// } else if (direction == JoyStickClass_L.STICK_LEFT) {
					// textView5.setText("Direction : Left");
					// } else if (direction == JoyStickClass_L.STICK_UPLEFT) {
					// textView5.setText("Direction : Up Left");
					// } else if (direction == JoyStickClass_L.STICK_NONE) {
					// textView5.setText("Direction : Center");
					// }
				} else if (arg1.getAction() == MotionEvent.ACTION_UP) {
					
					// textView1.setText("X :");
					// textView2.setText("Y :");
					// textView3.setText("Angle :");
					// textView4.setText("Distance :");
					// textView5.setText("Direction :");
				} */
				return true;
			}
		});
		layoutJoyStick_R.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				joyStick_R.drawStick(arg1);
			//	int direction1 = joyStick_R.get8Direction();
			//	directionText_R.setText(Integer.toString(direction1)); //Display Joystick_R output
				/*if (arg1.getAction() == MotionEvent.ACTION_DOWN || arg1.getAction() == MotionEvent.ACTION_MOVE) {
					// textView1.setText("X : " + String.valueOf(joyStick_L.getX()));
					// textView2.setText("Y : " + String.valueOf(joyStick_L.getY()));
					// textView3.setText("Angle : " + String.valueOf(joyStick_L.getAngle()));
					// textView4.setText("Distance : " + String.valueOf(joyStick_L.getDistance()));

					int direction1 = joyStick_R.get8Direction();
					directionText_R.setText(Integer.toString(direction1));
					// if (direction == JoyStickClass_L.STICK_UP) {
					// textView5.setText("Direction : Up");
					// } else if (direction == JoyStickClass_L.STICK_UPRIGHT) {
					// textView5.setText("Direction : Up Right");
					// } else if (direction == JoyStickClass_L.STICK_RIGHT) {
					// textView5.setText("Direction : Right");
					// } else if (direction == JoyStickClass_L.STICK_DOWNRIGHT) {
					// textView5.setText("Direction : Down Right");
					// } else if (direction == JoyStickClass_L.STICK_DOWN) {
					// textView5.setText("Direction : Down");
					// } else if (direction == JoyStickClass_L.STICK_DOWNLEFT) {
					// textView5.setText("Direction : Down Left");
					// } else if (direction == JoyStickClass_L.STICK_LEFT) {
					// textView5.setText("Direction : Left");
					// } else if (direction == JoyStickClass_L.STICK_UPLEFT) {
					// textView5.setText("Direction : Up Left");
					// } else if (direction == JoyStickClass_L.STICK_NONE) {
					// textView5.setText("Direction : Center");
					// }
				} else if (arg1.getAction() == MotionEvent.ACTION_UP) {
					// textView1.setText("X :");
					// textView2.setText("Y :");
					// textView3.setText("Angle :");
					// textView4.setText("Distance :");
					// textView5.setText("Direction :");
				}           */
				return true;
			}
		});
	    //<JoystickDemo.Project2
/*	startEngine.setOnClickListener(new OnClickListener(){
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(!takeOff){
				startEngine.setImageResource(R.drawable.landing);
				}
			else{
				startEngine.setImageResource(R.drawable.takeoff);
				}
			takeOff=!takeOff;
			}		
	    });		*/
	} 
	


	private void InitialSDK() {
		Toast.makeText(getApplicationContext(), "VideoActivity InitSDK", Toast.LENGTH_SHORT).show();
		anychat = new AnyChatCoreSDK();
		anychat.SetBaseEvent(this);
		anychat.SetTextMessageEvent(this);
		// 启动AnyChat传感器监听
		anychat.mSensorHelper.InitSensor(this);
		// 初始化Camera上下文句柄
		AnyChatCoreSDK.mCameraHelper.SetContext(this);
		
		anychat.SetRecordSnapShotEvent(this);
	}
/*
	private void adjuestVideoSize(int width, int height) {

		if (3 * width > 4 * height) {

			dwRemoteVideoHeight = height;
			dwRemoteVideoWidth = (int) (4.0 / 3.0 * dwRemoteVideoHeight);
		} else {
			dwRemoteVideoWidth = width;
			dwRemoteVideoHeight = (int) (3.0 / 4.0 * dwRemoteVideoWidth);
		}
		dwLocalVideoWidth = dwRemoteVideoWidth;
		dwLocalVideoHeight = dwRemoteVideoHeight;
		FrameLayout.LayoutParams layoutParamSelf=new FrameLayout.LayoutParams(dwLocalVideoWidth, dwLocalVideoHeight);
		myView.setLayoutParams(layoutParamSelf);
		LinearLayout.LayoutParams layoutPramOther=new LinearLayout.LayoutParams(dwLocalVideoWidth, dwLocalVideoHeight);
		otherView.setLayoutParams(layoutPramOther);
	}*/

	private void InitialLayout() {
		Toast.makeText(getApplicationContext(), "VideoActivity InitialLayout", Toast.LENGTH_SHORT).show();
		this.setContentView(R.layout.video_room);
		//startActivityForResult(new Intent("com.bairuitech.demo.VideoConfigActivity"), 1);
		this.setTitle("Remote Controlling " + anychat.GetUserName(userID) + " now");
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		VideoTodo="Record";
		myView = (SurfaceView) findViewById(R.id.svLocal);
		otherView = (SurfaceView) findViewById(R.id.svRemote);
		rlLocal=(RelativeLayout)findViewById(R.id.rlLocal);
		cm1=(Chronometer)findViewById(R.id.cmTimer);
		
		if(configEntity.usage==ConfigEntity.Master){
			otherView.setLayoutParams(para1);
			rlLocal.setLayoutParams(para2);
		}else if(configEntity.usage==ConfigEntity.Slave){
			rlLocal.setLayoutParams(para1);
			otherView.setLayoutParams(para2);
		}
		
		// 如果是采用Java视频采集，则需要设置Surface的CallBack
		if(AnyChatCoreSDK.GetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_CAPDRIVER) == AnyChatDefine.VIDEOCAP_DRIVER_JAVA) {
			myView.getHolder().addCallback(AnyChatCoreSDK.mCameraHelper);			
		}
		
		// 如果是采用Java视频显示，则需要设置Surface的CallBack
		if(AnyChatCoreSDK.GetSDKOptionInt(AnyChatDefine.BRAC_SO_VIDEOSHOW_DRIVERCTRL) == AnyChatDefine.VIDEOSHOW_DRIVER_JAVA) {
			int index = anychat.mVideoHelper.bindVideo(otherView.getHolder());
			anychat.mVideoHelper.SetVideoUser(index, userID);
		}
	
		otherView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				if(!bVideoAreaLoaded)
				{
					//adjuestVideoSize(otherView.getWidth(), otherView.getHeight());
					bVideoAreaLoaded=true;
				}
			}
		});
		//mCameraSwitchImage = (ImageView) findViewById(R.id.ivSwitchCamera);  Switch camera to the front one 
		//mCameraSwitchImage.setOnClickListener(this);
		SurfaceHolder holder = otherView.getHolder();
		holder.setKeepScreenOn(true);
		anychat.UserCameraControl(userID, 1);
		anychat.UserSpeakControl(userID, 1);
		myView.setOnClickListener(this);
		ConfigEntity configEntity = ConfigService.LoadConfig(this);		
		if (configEntity.videoOverlay != 0) {
			myView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		
		// 判断是否显示本地摄像头切换图标
		/*if(AnyChatCoreSDK.GetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_CAPDRIVER) == AnyChatDefine.VIDEOCAP_DRIVER_JAVA) {
			if(AnyChatCoreSDK.mCameraHelper.GetCameraNumber() > 1) {
				mCameraSwitchImage.setVisibility(View.VISIBLE);
				// 默认打开前置摄像头
				AnyChatCoreSDK.mCameraHelper.SelectVideoCapture(AnyChatCoreSDK.mCameraHelper.CAMERA_FACING_BACK);
			}
		}else {
			String[] strVideoCaptures = anychat.EnumVideoCapture();
			if (strVideoCaptures != null && strVideoCaptures.length > 1) {
				mCameraSwitchImage.setVisibility(View.VISIBLE);
				// 默认打开前置摄像头
				for(int i=0;i<strVideoCaptures.length;i++)
				{
					String strDevices=strVideoCaptures[i];
					if(strDevices.indexOf("Back")>=0) {
						anychat.SelectVideoCapture(strDevices);
						break;
					}
				}
			}
		}*/
		//mCameraSwitchImage.setVisibility(View.INVISIBLE);
		// 打开本地音频、视频设备	
		anychat.UserCameraControl(-1, 1);
		anychat.UserSpeakControl(-1, 1);		
		
		
	}


	private void refreshAV() {
		Toast.makeText(getApplicationContext(), "VideoActivity refreshAV", Toast.LENGTH_SHORT).show();
		anychat.UserCameraControl(userID, 1);
		anychat.UserSpeakControl(userID, 1);
		anychat.UserCameraControl(-1, 1);
		anychat.UserSpeakControl(-1, 1);
		bOtherVideoOpened = false;
		bSelfVideoOpened = false;
	}

	protected void onRestart() {
		super.onRestart();
		Toast.makeText(getApplicationContext(), "VideoActivity onRestart", Toast.LENGTH_SHORT).show();
		// 如果是采用Java视频显示，则需要设置Surface的CallBack
		if(AnyChatCoreSDK.GetSDKOptionInt(AnyChatDefine.BRAC_SO_VIDEOSHOW_DRIVERCTRL) == AnyChatDefine.VIDEOSHOW_DRIVER_JAVA) {
			int index = anychat.mVideoHelper.bindVideo(otherView.getHolder());
			anychat.mVideoHelper.SetVideoUser(index, userID);
		}
		
		refreshAV();
		bOnPaused = false;
	}

	protected void onResume() {
		Toast.makeText(getApplicationContext(), "VideoActivity onResume", Toast.LENGTH_SHORT).show();
		anychat.SetBaseEvent(this);
		super.onResume();
	}

	protected void onPause() {
		Toast.makeText(getApplicationContext(), "VideoActivity onPause", Toast.LENGTH_SHORT).show();
		super.onPause();
		bOnPaused = true;
		anychat.UserCameraControl(userID, 0);
		anychat.UserSpeakControl(userID, 0);
		anychat.UserCameraControl(-1, 0);
		anychat.UserSpeakControl(-1, 0);
	}

	protected void onDestroy() {
		Toast.makeText(getApplicationContext(), "VideoActivity onDestroy", Toast.LENGTH_SHORT).show();
		super.onDestroy();
		Log.e("******RoomActivity***********", "RoomActivity  onDestroy");	
	 	anychat.LeaveRoom(-1);
		anychat.mSensorHelper.DestroySensor();
		finish();
	}

	@Override
	public void OnAnyChatConnectMessage(boolean bSuccess) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "VideoActivity OnAnyChatConnectMessage", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void OnAnyChatEnterRoomMessage(int dwRoomId, int dwErrorCode) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "VideoActivity OnAnychatEnterRoomMessage", Toast.LENGTH_SHORT).show();
		Log.e("********VideoActivity*********", "OnAnyChatEnterRoomMessage");

	}

	@Override
	public void OnAnyChatLinkCloseMessage(int dwErrorCode) {
		// 网络连接断开之后，上层需要主动关闭已经打开的音视频设备
		Toast.makeText(getApplicationContext(), "VideoActivity OnAnychatLinkCloseMessage", Toast.LENGTH_SHORT).show();
		if(bOtherVideoOpened)
		{
			anychat.UserCameraControl(userID, 0);
			anychat.UserSpeakControl(userID, 0);
			bOtherVideoOpened = false;
		}
		if(bSelfVideoOpened)
		{
			anychat.UserCameraControl(-1, 0);
			anychat.UserSpeakControl(-1, 0);
			bSelfVideoOpened = false;
		}
	}

	@Override
	public void OnAnyChatLoginMessage(int dwUserId, int dwErrorCode) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "OnAnychatLoginMessage", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void OnAnyChatOnlineUserMessage(int dwUserNum, int dwRoomId) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "VideoActivity OnAnyChatOnlineUserMessage", Toast.LENGTH_SHORT).show();
		Log.e("********VideoActivity*********", "OnAnyChatOnlineUserMessage   "
				+ dwUserNum);
		refreshAV();
	}

	@Override
	public void OnAnyChatUserAtRoomMessage(int dwUserId, boolean bEnter) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "VideoActivity OnAnyChatUserAtRoomMessage", Toast.LENGTH_SHORT).show();
		Log.e("********VideoActivity*********", "OnAnyChatUserAtRoomMessage"
				+ dwUserId);
		if (dwUserId == userID) {
			if (!bEnter) {
				anychat.UserCameraControl(dwUserId, 0);
				anychat.UserSpeakControl(dwUserId, 0);
				bOtherVideoOpened = false;
			} else {
				anychat.UserCameraControl(dwUserId, 1);
				anychat.UserSpeakControl(dwUserId, 1);
			}
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		/*
		if (v == mCameraSwitchImage) {
			
			// 如果是采用Java视频采集，则在Java层进行摄像头切换
			if(AnyChatCoreSDK.GetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_CAPDRIVER) == AnyChatDefine.VIDEOCAP_DRIVER_JAVA) {
				AnyChatCoreSDK.mCameraHelper.SwitchCamera();
				return;
			}

			String strVideoCaptures[] = anychat.EnumVideoCapture();
			
			String temp = anychat.GetCurVideoCapture();
			for (int i = 0; i < strVideoCaptures.length; i++) {
				if (!temp.equals(strVideoCaptures[i])) {
					anychat.UserCameraControl(-1, 0);
					bSelfVideoOpened = false;
					anychat.SelectVideoCapture(strVideoCaptures[i]);
					anychat.UserCameraControl(-1, 1);
					break;
				}
			}

		}*/
	}
	
	
	@Override
	public void OnAnyChatTextMessage(int dwFromUserid, int dwToUserid,
			boolean bSecret, String message) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "VideoActivity OnAnyChatTextMessage", Toast.LENGTH_SHORT).show();
		//messageList.add(anychat.GetUserName(dwFromUserid)+"说: "+message);
		/*if(configEntity.usage==ConfigEntity.Slave){
			Toast.makeText(this, message,Toast.LENGTH_SHORT).show();	
			if(message.equals("TakePhoto")){
				anychat.SnapShot(dwToUserid, 1, 1);
			}else if(message.equals("RecordVideo")){
				Toast.makeText(this, message+"is developing",Toast.LENGTH_SHORT).show();
			}else if(message.equals("SaveVideo")){
				Toast.makeText(this, message+"is developing",Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(this, "here",Toast.LENGTH_SHORT).show();
				Log.e("here", message);
			}
		}*/
	}  

	
	

	
   
    //send commands
	private void SendMessage(String command){
		Toast.makeText(getApplicationContext(), "VideoActivity SendMessage", Toast.LENGTH_SHORT).show();
		if(configEntity.usage==ConfigEntity.Master)
			anychat.SendTextMessage(-1, 0, command);
    }
	
	
@Override
public void OnAnyChatRecordEvent(int dwUserId, String lpFileName, int dwElapse,
		int dwFlags, int dwParam, String lpUserStr) {
	// TODO Auto-generated method stub
	//Toast.makeText(getApplicationContext(), "VideoActivity RecordEvent", Toast.LENGTH_SHORT).show();
}


@Override
public void OnAnyChatSnapShotEvent(int dwUserId, String lpFileName,
		int dwFlags, int dwParam, String lpUserStr) {
	// TODO Auto-generated method stub
	//Toast.makeText(getApplicationContext(), "VideoActivity OnAnyChatSnapShotEvent", Toast.LENGTH_SHORT).show();
}

//>JoystickDemo.Project5
private Button.OnClickListener btnOnClick = new Button.OnClickListener() {

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch ((Integer) v.getTag()) {
		case 0:
			startSetting();
			break;
		case 1:
			startEngine();
			if(!takeOff){
				startEngine.setImageResource(R.drawable.landing);
				}
			else{
				startEngine.setImageResource(R.drawable.takeoff);
				}
			takeOff=!takeOff;
			break;
		case 2:
			changeControlMode();
			break;
		case 3:
			anychat.SnapShot(userID, 1, 1);
			break;
		}
	} 

	private void changeControlMode() {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "VideoActivity controlChange", Toast.LENGTH_SHORT).show();
		Intent intent_mode = new Intent();
		intent_mode.putExtra("UserID",userID);
		intent_mode.setClass(VideoActivity.this, VideoMotionControlActivity.class);
		startActivity(intent_mode);
		
		//intent_mode.setClass(, cls)
	}

	private void startSetting() {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "VideoActivity startSetting", Toast.LENGTH_SHORT).show();
		//>JoystickDemo.Project4
		Intent intent_setting=new Intent();
	    intent_setting.setClass(VideoActivity.this, SettingActivity.class);
	    startActivity(intent_setting);
		//<JoystickDemo.Project4
	}

	private void startEngine() {
		// TODO Auto-generated method stub
		//Toast.makeText(getApplicationContext(), "L21", Toast.LENGTH_LONG).show();
	}	
	
	
};
//<JoystickDemo.Project5

}






