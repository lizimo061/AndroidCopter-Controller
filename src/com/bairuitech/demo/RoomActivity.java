package com.bairuitech.demo; //[P,RoomActivity]

import java.util.ArrayList;

import com.bairuitech.anychat.AnyChatBaseEvent;
import com.bairuitech.anychat.AnyChatCoreSDK;
import com.bairuitech.anychat.AnyChatTextMsgEvent;

import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RoomActivity extends Activity implements AnyChatBaseEvent,AnyChatTextMsgEvent{

	private ListView userListView;
	//private MessageListView messageListView;
	private BaseAdapter userListAdapter;
	
	public AnyChatCoreSDK anychat;
	
	private ArrayList<String> idList = new ArrayList<String> ();
	private ArrayList<String> userList = new ArrayList<String> ();
	//private ArrayList<String> messageList = new ArrayList<String> ();
	
	//private EditText messageEditText;
	
	private ConfigEntity configEntity;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Toast.makeText(this, "RoomActivity OnCreat", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room);
        userListAdapter=new UserListListAdapter(this);
        InitialSDK();
        Intent intent = getIntent();
        intent.getIntExtra("RoomID",0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        InitialLayout();
        configEntity = ConfigService.LoadConfig(this);
    }
    
    private void InitialSDK()
    {
        anychat = new AnyChatCoreSDK();
        anychat.SetBaseEvent(this);
        anychat.SetTextMessageEvent(this);
    }
    private void InitialLayout(){

		userListView = (ListView)findViewById(R.id.lvOnlineDevice);
		userListView.setAdapter(userListAdapter);
		userListView.setOnItemClickListener(itemClickListener);

    }
    
    OnItemClickListener itemClickListener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			StartVideoChat(arg2); //Start VideoActivity[L]
		}
	};
    public class UserListListAdapter extends BaseAdapter 
	{
		private Context context;

		public UserListListAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return userList.size();
		}

		@Override
		public Object getItem(int position) {
			return userList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv =  new TextView(context);
			tv.setTextColor(Color.YELLOW);
			tv.setPadding(4, 4, 4, 4);
			tv.setTextSize(24);
			tv.setBackgroundColor(color.black);
			tv.setText(userList.get(position));
			return tv;
		}
	}
  /*  
    private void SendMessage()
    {
		anychat.SendTextMessage(-1, 0, messageEditText.getText().toString());
		messageList.add("我说: "+ messageEditText.getText().toString());
		//messageListView.setStackFromBottom(true);
		messageListView.SetFileList(messageList);
		messageEditText.setText("");
		messageListView.setSelection(messageListView.getAdapter().getCount()-1);


    }*/
    
    public void StartVideoChat(int position)
    {
		   Intent intent=new Intent();
		   intent.putExtra("UserID", idList.get(position));
		   intent.setClass(RoomActivity.this, VideoActivity.class);
		   startActivity(intent);
    }
    
/*    
    private 
    OnTouchListener touchListener =  new OnTouchListener()
    {
		@Override
		public boolean onTouch(View v, MotionEvent e) {
			// TODO Auto-generated method stub
	        switch (e.getAction()) 
	        {
	    		case MotionEvent.ACTION_DOWN:
	    			try
	    			{
	    				((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(RoomActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); 
	    			}
	    			catch(Exception excp)
	    			{
	    				
	    			}
	    			break;
	    		case MotionEvent.ACTION_UP:

	    			break;
	        }
	        return false;
		}
    };*/
    
    protected void onDestroy(){
		Log.e("******RoomActivity***********", "RoomActivity  onDestroy");	
 	   anychat.LeaveRoom(-1);
    	super.onDestroy();
    }
    
    protected void onResume() {
        anychat.SetBaseEvent(this);
        idList.clear();
        userList.clear();
        int[] userID = anychat.GetOnlineUser();
    	for(int i=0; i<userID.length ; i++)
    	{
    		idList.add(""+userID[i]);
    	}
    	for(int i=0; i<userID.length ; i++)
    	{
    		userList.add(anychat.GetUserName(userID[i]));
    	}
    	userListAdapter.notifyDataSetChanged();
        super.onResume();
    }
    
	@Override
	public void OnAnyChatConnectMessage(boolean bSuccess) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnAnyChatEnterRoomMessage(int dwRoomId, int dwErrorCode) {
		// TODO Auto-generated method stub
		Log.e("********RoomActivity*********", "OnAnyChatEnterRoomMessage");	

	}

	@Override
	public void OnAnyChatLinkCloseMessage(int dwErrorCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnAnyChatLoginMessage(int dwUserId, int dwErrorCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnAnyChatOnlineUserMessage(int dwUserNum, int dwRoomId) {
		// TODO Auto-generated method stub
		Log.e("********RoomActivity*********", "OnAnyChatOnlineUserMessage   "+dwUserNum);	

	}

	@Override
	public void OnAnyChatUserAtRoomMessage(int dwUserId, boolean bEnter) {
		// TODO Auto-generated method stub
		if(bEnter)
		{
			idList.add(""+dwUserId);
    		userList.add(anychat.GetUserName(dwUserId));
    		userListAdapter.notifyDataSetChanged();

		}
		else
		{
			for(int i=0;i<idList.size();i++)
			{
				if(idList.get(i).equals(""+dwUserId))
				{
					idList.remove(i);
					userList.remove(i);
					userListAdapter.notifyDataSetChanged();
				}
			}
			
		}
	}
	@Override
	public void OnAnyChatTextMessage(int dwFromUserid, int dwToUserid,
			boolean bSecret, String message) {
		//messageList.add(anychat.GetUserName(dwFromUserid)+"说: "+message);
		//messageListView.setStackFromBottom(true);
		//messageListView.SetFileList(messageList);
		//messageListView.setSelection(messageListView.getAdapter().getCount()-1);
		//messageListView.scrollTo(0, Integer.MAX_VALUE);		
	}

}
