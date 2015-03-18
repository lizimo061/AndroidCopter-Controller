package com.bairuitech.demo; //[A,LoginActivity]<-[B,ConfigService]&[D] && [A]:->[F]

import com.bairuitech.anychat.AnyChatBaseEvent;
import com.bairuitech.anychat.AnyChatCoreSDK;
import com.bairuitech.anychat.AnyChatDefine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LoginActivity extends Activity implements AnyChatBaseEvent {
	private Button loginBtn;
	private ConfigEntity configEntity;
	private EditText etIP,etPort,nameEditText,passwordEditText;
	private boolean bNeedRelease = false;
	private ProgressBar progressBar;
	public AnyChatCoreSDK anychat;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Toast.makeText(this, "LoginActivity creat", Toast.LENGTH_SHORT).show();
		super.onCreate(savedInstanceState);

		setDisPlayMetrics();
		setContentView(R.layout.login);
		configEntity = ConfigService.LoadConfig(this); //->[C,ConfigEntity]
		InitialSDK();
		InitialLayout();
	}

	private void InitialSDK() {
		Toast.makeText(this, "LoginActivity Init SDK", Toast.LENGTH_SHORT).show();
		if (anychat == null) {
			anychat = new AnyChatCoreSDK(); //->[D]
			anychat.SetBaseEvent(this); //->[D]
			if (configEntity.useARMv6Lib != 0)
				AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_CORESDK_USEARMV6LIB, 1);
			anychat.InitSDK(android.os.Build.VERSION.SDK_INT, 0);
			bNeedRelease = true;
		}
	}

	private void InitialLayout() {
		Toast.makeText(this, "LoginActivity Init Layout", Toast.LENGTH_SHORT).show();
		etIP = (EditText)findViewById(R.id.etIP);
		if (configEntity.IsSaveIPAndPort) 
			etIP.setText(configEntity.ip);
		
		etPort = (EditText)findViewById(R.id.etPort);
		if (configEntity.IsSaveIPAndPort) 
			etPort.setText(configEntity.port);
		
		nameEditText = (EditText)findViewById(R.id.etUsername);
		if (configEntity.IsSaveNameAndPw) 
			nameEditText.setText(configEntity.name);

		passwordEditText = (EditText)findViewById(R.id.etPassword);
		if (configEntity.IsSaveNameAndPw) 
			passwordEditText.setText(configEntity.password);

		progressBar = (ProgressBar)findViewById(R.id.pbLogin);		
		progressBar.setVisibility(View.GONE);

		loginBtn =(Button)findViewById(R.id.bLogin);
		loginBtn.setOnClickListener(listener);
	}

	OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			if (v == loginBtn) 
				Login();		
		}
	};

	private void Login() {
		Toast.makeText(this, "LoginActivity Login", Toast.LENGTH_SHORT).show();
			configEntity.IsSaveIPAndPort = true;
			configEntity.IsSaveNameAndPw = true;
			configEntity.ip = etIP.getEditableText().toString();
			configEntity.port = Integer.parseInt(etPort.getText().toString());
			configEntity.name = nameEditText.getEditableText().toString();
			configEntity.password = passwordEditText.getText().toString();
			ConfigService.SaveConfig(this, configEntity);
		
		// If user does not enter the user name, warning
		if (nameEditText.getText().length() == 0) {
			Toast.makeText(this, "Please enter the user name", Toast.LENGTH_SHORT).show();
			return;
		}

		this.anychat.Connect(configEntity.ip, configEntity.port);

			this.anychat.Login(nameEditText.getText().toString(),
					passwordEditText.getText().toString());

		loginBtn.setClickable(false);
		progressBar.setVisibility(View.VISIBLE);
		Toast.makeText(this, "LoginActivity Login Done", Toast.LENGTH_SHORT).show();
	}

	private void setDisPlayMetrics() {
		Toast.makeText(this, "LoginActivity setDiplayMetrics", Toast.LENGTH_SHORT).show();
		DisplayMetrics dMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dMetrics); // Access DiplayMetrics Members, get general information about a display
		ScreenInfo.WIDTH = dMetrics.widthPixels;
		ScreenInfo.HEIGHT = dMetrics.heightPixels;
	}

	protected void onDestroy() {
		Toast.makeText(this, "LoginActivity Destroy", Toast.LENGTH_SHORT).show();
		if (bNeedRelease) 
			anychat.Release(); // 关闭SDK
		super.onDestroy();
	}

	protected void onResume() {
		Toast.makeText(this, "LoginActivity Resume", Toast.LENGTH_SHORT).show();
		configEntity = ConfigService.LoadConfig(this);
		anychat.SetBaseEvent(this);
		super.onResume();
	}

	@Override
	public void OnAnyChatConnectMessage(boolean bSuccess) {
		Toast.makeText(this, "Login Connect", Toast.LENGTH_SHORT).show();
		if (!bSuccess) {
			bSuccess=true;
			loginBtn.setClickable(true);
			Toast.makeText(this, "连接服务器失败，自动重连，请稍后...", Toast.LENGTH_SHORT).show();
			progressBar.setVisibility(View.GONE);
		}
	}

	@Override
	public void OnAnyChatEnterRoomMessage(int dwRoomId, int dwErrorCode) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "LoginActivity Enter Room", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void OnAnyChatLinkCloseMessage(int dwErrorCode) {
		Toast.makeText(this, "LoginActivity Link Close", Toast.LENGTH_SHORT).show();
		Toast.makeText(this, "连接关闭，error：" + dwErrorCode, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void OnAnyChatLoginMessage(int dwUserId, int dwErrorCode) {
		Toast.makeText(this, "LoginActivity Message", Toast.LENGTH_SHORT).show();
		if (dwErrorCode == 0) {
			Toast.makeText(this, "登录成功！", Toast.LENGTH_SHORT).show();
			bNeedRelease = false;
			Intent itent = new Intent();
			itent.setClass(this, HallActivity.class); //->[F,HallActivity]
			startActivity(itent);
			finish();
		} else {
			Toast.makeText(this, "登录失败，错误代码：" + dwErrorCode, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void OnAnyChatOnlineUserMessage(int dwUserNum, int dwRoomId) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "LoginActivity Online User", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void OnAnyChatUserAtRoomMessage(int dwUserId, boolean bEnter) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "LoginActivity At Room", Toast.LENGTH_SHORT).show();
	}
}