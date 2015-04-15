package com.bairuitech.demo; //[SELF]:S

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;



public class SettingActivity extends Activity implements OnSeekBarChangeListener{

	ImageButton defaultSettings,changeControlMode;
	TextView initialHeight, initialHeightTextView, rotationSpeed, rotationSpeedTextView, horizontalSpeed, horizontalSpeedTextView, verticalSpeed, verticalSpeedTextView, movingDistance, movingDistanceTextView;
	SeekBar initialHeightSeekBar, rotationSpeedSeekBar, horizontalSpeedSeekBar, verticalSpeedSeekBar, movingDistanceSeekBar;
//	ToggleButton initialHeightToggleButton, rotationSpeedToggleButton, horizontalSpeedToggleButton, verticalSpeedToggleButton, movingDistanceToggleButton;
	int initialHeightMagnitude, rotationSpeedMagnitude, horizontalSpeedMagnitude, verticalSpeedMagnitude, movingDistanceMagnitude;
	int userID;
	int controlMode;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Toast.makeText(getApplicationContext(), "SettingActivity onCreat", Toast.LENGTH_SHORT).show();
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        
        Intent intent = getIntent();
		userID = intent.getIntExtra("UserID", -1);
		controlMode = intent.getIntExtra("ModeID", 0);
        
        
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        TableRow tableRow = new TableRow(this);
        //TextView temp = new TextView(this);
        //temp.setText("text的值");
        //tableRow.addView(temp);

        
        //>UROP1000.project
        initialHeight = (TextView) findViewById(R.id.initialHeight);
        initialHeightTextView = (TextView) findViewById(R.id.initialHeightTextView);
        initialHeightSeekBar = (SeekBar) findViewById(R.id.initialHeightSeekBar);
        initialHeightSeekBar.setOnSeekBarChangeListener(this);

        rotationSpeed = (TextView) findViewById(R.id.rotationSpeed);
        rotationSpeedTextView = (TextView) findViewById(R.id.rotationSpeedTextView);
        rotationSpeedSeekBar = (SeekBar) findViewById(R.id.rotationSpeedSeekBar);
        rotationSpeedSeekBar.setOnSeekBarChangeListener(this);
        
        horizontalSpeed = (TextView) findViewById(R.id.horizontalSpeed);
        horizontalSpeedTextView = (TextView) findViewById(R.id.horizontalSpeedTextView);
        horizontalSpeedSeekBar = (SeekBar) findViewById(R.id.horizontalSpeedSeekBar);
        horizontalSpeedSeekBar.setOnSeekBarChangeListener(this);
        
        verticalSpeed = (TextView) findViewById(R.id.verticalSpeed);
        verticalSpeedTextView = (TextView) findViewById(R.id.verticalSpeedTextView);
        verticalSpeedSeekBar = (SeekBar) findViewById(R.id.verticalSpeedSeekBar);
        verticalSpeedSeekBar.setOnSeekBarChangeListener(this);
        
        movingDistance = (TextView) findViewById(R.id.movingDistance);
        movingDistanceTextView = (TextView) findViewById(R.id.movingDistanceTextView);
        movingDistanceSeekBar = (SeekBar) findViewById(R.id.movingDistanceSeekBar);
        movingDistanceSeekBar.setOnSeekBarChangeListener(this);
        
        defaultSettings = (ImageButton) findViewById(R.id.defaultSettingsButton);
        defaultSettings.setOnClickListener(defaultButtonOnClick);
        defaultSettings.getBackground().setAlpha(0);
        
        changeControlMode = (ImageButton) findViewById(R.id.controller);
        changeControlMode.setOnClickListener(controllerButtonOnClick);
        changeControlMode.getBackground().setAlpha(0);
        
        verticalSpeedSeekBar.setProgress(verticalSpeedSeekBar.getMax()/2);
		initialHeightSeekBar.setProgress(initialHeightSeekBar.getMax()/2);
		rotationSpeedSeekBar.setProgress(rotationSpeedSeekBar.getMax()/2);
		movingDistanceSeekBar.setProgress(movingDistanceSeekBar.getMax()/2);
		horizontalSpeedSeekBar.setProgress(horizontalSpeedSeekBar.getMax()/2);
    }

    //>UROP1000.project1
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "SettingActivity onProgressChanged", Toast.LENGTH_SHORT).show();
		updateState(seekBar);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "SettingActivity TrackingTouchStart", Toast.LENGTH_SHORT).show();
		updateState(seekBar);
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "SettingActivity TrackingTouchStop", Toast.LENGTH_SHORT).show();
		updateState(seekBar);
	}
    
   private void updateState(SeekBar seekBar) {
		// TODO Auto-generated method stub
	   Toast.makeText(getApplicationContext(), "SettingActivity UpdateState", Toast.LENGTH_SHORT).show();
    	switch (seekBar.getId()) {
    	/*
    	 * case R.id.SeekBarRed: red = seekBar.getProgress(); updateRed();
    	 * break;
    	 */
    	case R.id.initialHeightSeekBar:
    		initialHeightMagnitude = seekBar.getProgress();

    		initialHeightTextView.setText(Integer.toString(initialHeightMagnitude)); // 將power_Val轉型塞到textbox裡面

    		break;
    		
    	case R.id.rotationSpeedSeekBar:
    		rotationSpeedMagnitude = seekBar.getProgress();

    		rotationSpeedTextView.setText(Integer.toString(rotationSpeedMagnitude)); // 將power_Val轉型塞到textbox裡面

    		break;

    	case R.id.horizontalSpeedSeekBar:
    		horizontalSpeedMagnitude = seekBar.getProgress();

    		horizontalSpeedTextView.setText(Integer.toString(horizontalSpeedMagnitude)); // 將power_Val轉型塞到textbox裡面

    		break;

    	case R.id.verticalSpeedSeekBar:
    		verticalSpeedMagnitude = seekBar.getProgress();

    		verticalSpeedTextView.setText(Integer.toString(verticalSpeedMagnitude)); // 將power_Val轉型塞到textbox裡面

    		break;

    	case R.id.movingDistanceSeekBar:
    		movingDistanceMagnitude = seekBar.getProgress();

    		movingDistanceTextView.setText(Integer.toString(movingDistanceMagnitude)); // 將power_Val轉型塞到textbox裡面

    		break;

    	/*
    	 * case R.id.SeekBarBlue: blue = seekBar.getProgress(); updateBlue();
    	 * break;
    	 */
	}
    }
  //<UROP1000.project1
   
   //>http://developer.android.com/guide/topics/ui/controls/togglebutton.html.project1
	public void onToggleClicked(View view) {
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();
        
        if (on) {
            // Enable vibrate
        	Toast.makeText(getApplicationContext(), "S8", Toast.LENGTH_SHORT).show();
        } else {
            // Disable vibrate
        	Toast.makeText(getApplicationContext(), "S9", Toast.LENGTH_SHORT).show();
        }
    }
	//<http://developer.android.com/guide/topics/ui/controls/togglebutton.html.project1
	
	private ImageButton.OnClickListener defaultButtonOnClick = new ImageButton.OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			verticalSpeedSeekBar.setProgress(verticalSpeedSeekBar.getMax()/2);
			initialHeightSeekBar.setProgress(initialHeightSeekBar.getMax()/2);
			rotationSpeedSeekBar.setProgress(rotationSpeedSeekBar.getMax()/2);
			movingDistanceSeekBar.setProgress(movingDistanceSeekBar.getMax()/2);
			horizontalSpeedSeekBar.setProgress(horizontalSpeedSeekBar.getMax()/2);
		}
	};
	
	private ImageButton.OnClickListener controllerButtonOnClick = new ImageButton.OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent_mode = new Intent();
			intent_mode.putExtra("UserID",userID);	
			if(controlMode == 0){
				intent_mode.setClass(SettingActivity.this, VideoMotionControlActivity.class);				
				startActivity(intent_mode);
			}
			else if(controlMode == 1){
				intent_mode.setClass(SettingActivity.this, VideoActivity.class);				
				startActivity(intent_mode);
			}
		}
	};
    
    protected void onResume() {
        super.onResume();
        Toast.makeText(getApplicationContext(), "S10", Toast.LENGTH_SHORT).show();
    }
    
    protected void onPause() {
        super.onPause();
        Toast.makeText(getApplicationContext(), "S11", Toast.LENGTH_SHORT).show();
    }
    
    protected void onRestart(){
    	super.onRestart();Toast.makeText(getApplicationContext(), "S12", Toast.LENGTH_SHORT).show();
    }
    
    protected void onDestroy() {
    	super.onDestroy();
    	//System.exit(0);
    	Toast.makeText(getApplicationContext(), "S13", Toast.LENGTH_SHORT).show();
    }

}
