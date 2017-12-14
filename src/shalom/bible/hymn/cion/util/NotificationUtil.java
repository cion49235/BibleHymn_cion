package shalom.bible.hymn.cion.util;


import java.util.ArrayList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import shalom.bible.hymn.cion.MainFragmentActivity;
import shalom.bible.hymn.cion.R;
import shalom.bible.hymn.cion.activity.HymnViewActivity;
import shalom.bible.hymn.cion.ccm.player.ContinueMediaPlayer_CCM;
import shalom.bible.hymn.cion.podcast.mediaplayer.ContinueMediaPlayer_Podcast;
import shalom.bible.hymn.cion.podcast.mediaplayer.CustomMediaPlayer_Podcast;

public class NotificationUtil {
	public static NotificationManager notificationManager;
	public static Notification notification;
	public static int noti_state = 1;
	public static int SDK_INT = android.os.Build.VERSION.SDK_INT;
	
	public static void setNotification(Context context, String title, String enclosure, String pubDate, String image, String description_title) {
		if (SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){ 
			try{
				notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		        Intent intent = new Intent(context, CustomMediaPlayer_Podcast.class);
		    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
		        intent.putExtra("title", title);
				intent.putExtra("enclosure", enclosure);
				intent.putExtra("pubDate", pubDate);
				intent.putExtra("image", image);
				intent.putExtra("description_title", description_title);
				PendingIntent content = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		        Notification.Builder builder = new Notification.Builder(context)
		                .setContentIntent(content)
		                .setSmallIcon(R.drawable.ic_app)
		                .setContentTitle(title)
//		                .setContentText("")
		                .setDefaults(Notification.FLAG_AUTO_CANCEL)
		                .setTicker(context.getString(R.string.app_name));
		        notification = builder.build();
		        notificationManager.notify(noti_state, notification);
			}catch(NullPointerException e){
			}
		}
    }
	
	public static void setNotification_continue_podcast(Context context, ArrayList<String> array_title, ArrayList<String> array_enclosure, ArrayList<String> array_pubDate, ArrayList<String> array_image, ArrayList<String> array_description_title, int video_num) {
		if (SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){ 
			try{
				notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		        Intent intent = new Intent(context, ContinueMediaPlayer_Podcast.class);
		    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
		    	intent.putExtra("array_title", array_title);
				intent.putExtra("array_enclosure", array_enclosure);
				intent.putExtra("array_pubDate", array_pubDate);
				intent.putExtra("array_image", array_image);
				intent.putExtra("array_description_title", array_description_title);
				intent.putExtra("video_num", video_num);
				PendingIntent content = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		        Notification.Builder builder = new Notification.Builder(context)
		                .setContentIntent(content)
		                .setSmallIcon(R.drawable.ic_app)
		                .setContentTitle(array_title.get(video_num))
//		                .setContentText("")
		                .setDefaults(Notification.FLAG_AUTO_CANCEL)
		                .setTicker(context.getString(R.string.app_name));
		        notification = builder.build();
		        notificationManager.notify(noti_state, notification);
			}catch(NullPointerException e){
			}
		}
    }
	
	public static void setNotification_favorite(Context context, String title, String user_explain, String des, Intent intent){
		if (SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){ 
			try{
				notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
				PendingIntent content = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		        Notification.Builder builder = new Notification.Builder(context)
		                .setContentIntent(content)
		                .setSmallIcon(R.drawable.ic_app)
		                .setContentTitle(title)
		                .setContentText(user_explain + " " + des)
//		                .setDefaults(Notification.FLAG_AUTO_CANCEL)
//			      	    .setDefaults(Notification.DEFAULT_ALL)
		                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
		                .setTicker(user_explain + " " + des);
		        notification = builder.build();
		        notification.flags = Notification.FLAG_AUTO_CANCEL;
		        notificationManager.notify(noti_state, notification);
		        
			}catch(NullPointerException e){
			}
		}
	}
	
	public static void setNotification_main(Context context){
		if (SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){ 
			try{
				notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
				Intent intent = new Intent(context, MainFragmentActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
				PendingIntent content = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		        Notification.Builder builder = new Notification.Builder(context)
		                .setContentIntent(content)
		                .setSmallIcon(R.drawable.ic_app)
		                .setContentTitle(context.getString(R.string.app_name))
		                .setContentText(context.getString(R.string.txt_background_voice_play))
//		                .setDefaults(Notification.FLAG_AUTO_CANCEL)
//			      	    .setDefaults(Notification.DEFAULT_ALL)
//		                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
		                .setTicker(context.getString(R.string.app_name));
		        notification = builder.build();
		        notification.flags = Notification.FLAG_AUTO_CANCEL;
		        notificationManager.notify(noti_state, notification);
			}catch(NullPointerException e){
			}
		}
	}
	
	public static void setNotification_hymn(Context context){
		if (SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){ 
			try{
				notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
				Intent intent = new Intent(context, HymnViewActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
				PendingIntent content = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		        Notification.Builder builder = new Notification.Builder(context)
		                .setContentIntent(content)
		                .setSmallIcon(R.drawable.ic_app)
		                .setContentTitle(context.getString(R.string.app_name))
		                .setContentText(context.getString(R.string.txt_background_play))
		                .setTicker(context.getString(R.string.app_name));
		        notification = builder.build();
		        notification.flags = Notification.FLAG_AUTO_CANCEL;
		        notificationManager.notify(noti_state, notification);
			}catch(NullPointerException e){
			}
		}
	}
	
	public static void setNotification_continue_ccm(Context context, ArrayList<String> array_music, ArrayList<String> array_videoid, ArrayList<String> array_playtime, ArrayList<String> array_imageurl, ArrayList<String> array_artist, int video_num) {
		if (SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
			try{
				notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		        Intent intent = new Intent(context, ContinueMediaPlayer_CCM.class);
		    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
		    	intent.putExtra("array_music", array_music);
				intent.putExtra("array_videoid", array_videoid);
				intent.putExtra("array_playtime", array_playtime);
				intent.putExtra("array_imageurl", array_imageurl);
				intent.putExtra("array_artist", array_artist);
				intent.putExtra("video_num", video_num);
				PendingIntent content = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		        Notification.Builder builder = new Notification.Builder(context)
		                .setContentIntent(content)
		                .setSmallIcon(R.drawable.ic_app)
		                .setContentTitle(array_music.get(video_num))
//		                .setContentText("")
		                .setDefaults(Notification.FLAG_AUTO_CANCEL)
		                .setTicker(context.getString(R.string.app_name));
		        notification = builder.build();
		        notificationManager.notify(noti_state, notification);
			}catch(NullPointerException e){
			}
		}
    }
	
	public static void setNotification_Cancel(){
		if (SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
			if(notificationManager != null) notificationManager.cancel(noti_state);	
		}
    }
}
