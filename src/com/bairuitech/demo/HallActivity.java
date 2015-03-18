package com.bairuitech.demo; //[F,HallActivity]:->[G,VideoConfigActivity]&[C]

import com.bairuitech.anychat.AnyChatBaseEvent; //[E]
import com.bairuitech.anychat.AnyChatCoreSDK; //[D]
import com.bairuitech.anychat.AnyChatDefine; //[I]

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class HallActivity extends Activity implements AnyChatBaseEvent{
	
	public static final int ACTIVITY_ID_VIDEOCONFIG = 1;		// 视频配置界面标识

	private Button videoChatBtn,videoConfigBtn;

	public AnyChatCoreSDK anychat;
	
	private boolean IsDisConnect = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Toast.makeText(this, "HallActivity onCreat", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        InitialSDK();
        InitialLayout(); //->[C,ConfigEntity]
    }
    
    private void InitialSDK(){
    	Toast.makeText(this, "HallActivity InitialSDK", Toast.LENGTH_SHORT).show();
        anychat = new AnyChatCoreSDK();
        anychat.SetBaseEvent(this);
        ApplyVideoConfig(); //->[C,ConfigEntity]
    }
    
    private void InitialLayout()
    {   Toast.makeText(this, "HallActivity InitialLayout", Toast.LENGTH_SHORT).show();
    	setContentView(R.layout.mainmenu);

		videoChatBtn  =(Button)findViewById(R.id.bWork);
		videoChatBtn.setTag(1);
		videoChatBtn.setOnClickListener(listener);
		
    	videoConfigBtn  = (Button)findViewById(R.id.bSetting);
		videoConfigBtn.setOnClickListener(listener);
    }
    
    OnClickListener listener = new OnClickListener(){     //->[G]
		public void onClick(View v) 
		{
			if(v == videoConfigBtn) //->[G,VideoConfigActivity]
				startActivityForResult(new Intent("com.bairuitech.demo.VideoConfigActivity"), ACTIVITY_ID_VIDEOCONFIG);
			else
		        anychat.EnterRoom(Integer.parseInt(v.getTag().toString()), "");				
		}
    };
   
    protected void onDestroy() {
    	Toast.makeText(this, "HallActivity onDestroy", Toast.LENGTH_SHORT).show();
    	anychat.LeaveRoom(-1);
    	anychat.Logout();
    	anychat.Release();	// 关闭SDK，不再返回登录界面

    	if(!IsDisConnect)
    		android.os.Process.killProcess(android.os.Process.myPid());
    	else{
 		   Intent itent=new Intent();
	       itent.setClass(HallActivity.this, LoginActivity.class);
	       startActivity(itent);
    	}
    	super.onDestroy();
    	//System.exit(0);
    }
    
    protected void onResume() {
    	Toast.makeText(this, "HallActivity onResume()", Toast.LENGTH_SHORT).show();
        anychat.SetBaseEvent(this);
        super.onResume();
    }
    
	@Override
	public void OnAnyChatConnectMessage(boolean bSuccess) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "HallActivity ConnectMessage", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void OnAnyChatEnterRoomMessage(int dwRoomId, int dwErrorCode) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "HallActivity EnterRoomMessage", Toast.LENGTH_SHORT).show();
	    if((dwErrorCode == 0)&&(dwRoomId == 1)){    	
			Intent intent = new Intent();  
			intent.putExtra("RoomID", dwRoomId);
			intent.setClass(HallActivity.this, RoomActivity.class);//Enter RoomActivity[P]
			startActivity(intent);   
	    }
	}

	@Override
	public void OnAnyChatLinkCloseMessage(int dwErrorCode) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "HallActivity CloseMessage", Toast.LENGTH_SHORT).show();
		IsDisConnect = true;
		this.finish();
		
	}

	@Override
	public void OnAnyChatLoginMessage(int dwUserId, int dwErrorCode) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "HallActivity LoginMessage", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void OnAnyChatOnlineUserMessage(int dwUserNum, int dwRoomId) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "HallActivity OnlineUserMessage", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void OnAnyChatUserAtRoomMessage(int dwUserId, boolean bEnter) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "HallActivity AtRoomMessage", Toast.LENGTH_SHORT).show();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "HallActivity onActivityResult", Toast.LENGTH_SHORT).show();
		if(resultCode == RESULT_OK)
		{
			ApplyVideoConfig();
		}
	}

	// 根据配置文件配置视频参数
	private void ApplyVideoConfig()
	{	Toast.makeText(this, "HallActivity ApplyVideoConfig", Toast.LENGTH_SHORT).show();
		ConfigEntity configEntity = ConfigService.LoadConfig(this);
		if(configEntity.configMode == 1)		// 自定义视频参数配置
		{
			// 设置本地视频编码的码率（如果码率为0，则表示使用质量优先模式）
			AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_BITRATECTRL, configEntity.videoBitrate);
			if(configEntity.videoBitrate==0)
			{
				// 设置本地视频编码的质量
				AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_QUALITYCTRL, configEntity.videoQuality);
			}
			// 设置本地视频编码的帧率
			AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_FPSCTRL, configEntity.videoFps);
			// 设置本地视频编码的关键帧间隔
			AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_GOPCTRL, configEntity.videoFps*4);
			// 设置本地视频采集分辨率
			AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_WIDTHCTRL, configEntity.resolution_width);
			AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_HEIGHTCTRL, configEntity.resolution_height);
			// 设置视频编码预设参数（值越大，编码质量越高，占用CPU资源也会越高）
			AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_PRESETCTRL, configEntity.videoPreset);
		}
		// 让视频参数生效
		AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_APPLYPARAM, configEntity.configMode);
		// P2P设置
		AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_NETWORK_P2PPOLITIC, configEntity.enableP2P);
		// 本地视频Overlay模式设置
		AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_OVERLAY, configEntity.videoOverlay);
		// 回音消除设置
		AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_AUDIO_ECHOCTRL, configEntity.enableAEC);
		// 平台硬件编码设置
		AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_CORESDK_USEHWCODEC, configEntity.useHWCodec);
		// 视频旋转模式设置
		AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_ROTATECTRL, configEntity.videorotatemode);
		// 视频采集驱动设置
		AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_CAPDRIVER, configEntity.videoCapDriver);
		// 本地视频采集偏色修正设置
		AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_FIXCOLORDEVIA, configEntity.fixcolordeviation);
		// 视频显示驱动设置
		AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_VIDEOSHOW_DRIVERCTRL, configEntity.videoShowDriver);
		// 音频播放驱动设置
		AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_AUDIO_PLAYDRVCTRL, configEntity.audioPlayDriver);
		// 音频采集驱动设置
		AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_AUDIO_RECORDDRVCTRL, configEntity.audioRecordDriver);
		// 视频GPU渲染设置
		AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_VIDEOSHOW_GPUDIRECTRENDER, configEntity.videoShowGPURender);
		// 本地视频自动旋转设置
		AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_AUTOROTATION, configEntity.videoAutoRotation);
	}
}
