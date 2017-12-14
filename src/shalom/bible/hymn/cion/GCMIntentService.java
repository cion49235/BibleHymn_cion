package shalom.bible.hymn.cion;
import java.util.Iterator;
import java.util.Set;

import com.google.android.gcm.GCMBaseIntentService;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import shalom.bible.hymn.cion.favorite.global.Global;
import shalom.bible.hymn.cion.favorite.page.ProfileActivity;
import shalom.bible.hymn.cion.favorite.page.document_read;
import shalom.bible.hymn.cion.util.NotificationUtil;

public class GCMIntentService extends GCMBaseIntentService {
	private static final String tag = "GCM";
	public static final String SEND_ID = "743824910564";
	public int noti_id = 0;
	String value;
	int SDK_INT = android.os.Build.VERSION.SDK_INT;
	
	public GCMIntentService() {
		// this(SEND_ID);
	}

	public GCMIntentService(String senderId) {
		super(senderId);
	}

	@Override
	protected String[] getSenderIds(Context context) {
		String[] ids = new String[1];
		ids[0] = SEND_ID;
		return ids;
	}

	// get Message
	@Override
	protected void onMessage(Context context, Intent intent) {
//		PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
//		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | 
//	            PowerManager.ON_AFTER_RELEASE, "lock");
//		wl.acquire();
//		wl.release();
		try{
			Bundle bundle = intent.getExtras();
		    Set<String> setKey = bundle.keySet();
	        Iterator<String> iterKey = setKey.iterator();
	        String send_user_srl = null;
	        String kind = null;
            String number = null;
            String title = null;
            String des = null;
            String content = null;
	        while (iterKey.hasNext()){
	            String key = iterKey.next();
	            String value = bundle.getString(key);
	            Log.d(tag, "onMessage. key = " + key + ", value = " + value);
	            if(key.matches("collapse_key")){
		            String[] keyarray = value.split("//");
	                kind = keyarray[0];
	                number = keyarray[1];
	            }
	                //Documents
	            if(key.matches("data")){
	            	String[] array = value.split("/LINE/.");
	            	send_user_srl = array[0];
	            	title = array[1];
	            	content = array[2];
	            	des = array[3];
	            	if(des.matches("new_document")) des = getString(R.string.notice_new_document);
	            	if(des.matches("new_comment")) des = getString(R.string.notice_new_comment);
	            	if(des.matches("added_to_favorite")) {des = getString(R.string.notice_added_to_favorite); content = getString(R.string.notice_added_to_favorite);}
	            }
	        }
	        if(kind.matches("1")){
	        	intent = new Intent(context, document_read.class);
	        	intent.putExtra("doc_srl", number);
	        }

	        if(kind.matches("2")){
	        	intent = new Intent(context, document_read.class);
	        	intent.putExtra("doc_srl", number);
	        }

	        if(kind.matches("3")){
	        	intent = new Intent(context, ProfileActivity.class);
	        	intent.putExtra("member_srl", send_user_srl);
	        }
	        
	        String user_explain = String.format(context.getString(R.string.user_explain), title);
	        NotificationUtil.setNotification_favorite(context, title, user_explain, des, intent);
	        
		}catch(Exception e){
			
		}
	}
	
	@Override
	protected void onError(Context context, String errorId) {
		Log.d(tag, "onError. errorId : " + errorId);
	}

	@Override
	protected void onRegistered(Context context, String regId) {
		Log.d(tag, "onRegistered. regId : " + regId);
		// Setting
		// Setting Editor
		SharedPreferences edit = getSharedPreferences("setting", MODE_PRIVATE);
		SharedPreferences.Editor editor = edit.edit();
		editor.putString("regId", regId);
		editor.commit();
	}

	@Override
	protected void onUnregistered(Context context, String regId) {
		Log.d(tag, "onUnregistered. regId : " + regId);
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		Log.d(tag, "onRecoverableError. errorId : " + errorId);
		return super.onRecoverableError(context, errorId);
	}
}