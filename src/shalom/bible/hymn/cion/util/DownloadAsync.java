package shalom.bible.hymn.cion.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.RemoteViews;
import shalom.bible.hymn.cion.R;
import shalom.bible.hymn.cion.activity.PodcastActivity_Sub;
import shalom.bible.hymn.cion.db.helper.DBopenHelper_podcast_download;

public class DownloadAsync extends AsyncTask<String, Integer, String>{
	Context context;
	String title;
	String enclosure;
	String Response = "fail";
    Notification notification;
    NotificationManager notificationManager;
    int progress = 0;
    int NOTIFICATION_ID;
    NotificationHelper mNotificationHelper;
    int position;
    String description_title;
    String sub_file_name;
    DBopenHelper_podcast_download down_mydb;
    String provider;
    String image;
    String pubDate;
    String old_title;
	public DownloadAsync(Context context, String title, String enclosure, int position, String description_title, DBopenHelper_podcast_download down_mydb, String provider, String image, String pubDate, String old_title){
		this.context = context;
		this.title = title;
		this.enclosure = enclosure;
		this.NOTIFICATION_ID = StringUtil.getRandomNumber();
		this.mNotificationHelper = new NotificationHelper(context);
		this.position = position;
		this.description_title = description_title;
		this.down_mydb = down_mydb;
		this.provider = provider;
		this.image = image;
		this.pubDate = pubDate;
		this.old_title = old_title;
	}
	@Override
	protected String doInBackground(String... params) {
		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
				+ context.getString(R.string.activity_podcast_06));
        file.mkdirs();
        String file_type = StringUtil.getExtension(enclosure);
        String file_name = Environment.getExternalStorageDirectory().getAbsolutePath()
					+ context.getString(R.string.activity_podcast_06) +title+"."+file_type;
        if(file_name.lastIndexOf("?") != -1){
        	sub_file_name = file_name.replace(file_name.substring(file_name.lastIndexOf("?")), "");
        }else{
        	sub_file_name = file_name;
        }
        try{
        	long total = 0;
        	int max = 0;

        	URL url = new URL(enclosure);
//        	Log.i("dsu", "file_name : " + sub_file_name + "\nenclosure : " + enclosure);
        	HttpURLConnection localHttpURLConnection = (HttpURLConnection)url.openConnection();
        	localHttpURLConnection.setConnectTimeout(150000);
        	localHttpURLConnection.setReadTimeout(150000);
        	localHttpURLConnection.setUseCaches(false);
        	localHttpURLConnection.connect();
        	int lenghtOfFile = localHttpURLConnection.getContentLength();
        	BufferedInputStream localBufferedInputStream = new BufferedInputStream(url.openStream());
        	FileOutputStream output = new FileOutputStream(sub_file_name);
        	byte data[] = new byte[1024];
        	while (true){
        		int count = localBufferedInputStream.read(data);
        		if (count == -1){
        			output.flush();
        			output.close();
        			localBufferedInputStream.close();
        			return Response = "success";
        		}
        		total += count;
        		int percent = (int)(100 * total / lenghtOfFile);
        		if (max < percent){
        			String str3 = percent + context.getString(R.string.activity_podcast_07);
        			notification.contentView.setProgressBar(R.id.status_progress, 100, percent, false);
        	        notification.contentView.setTextViewText(R.id.status_text, str3);
        	        notificationManager.notify(this.NOTIFICATION_ID, notification);
        			Integer[] arrayOfInteger = new Integer[1];
        			arrayOfInteger[0] = Integer.valueOf(percent);
        			publishProgress(arrayOfInteger);
        		}
        		max = percent;
        		output.write(data, 0, count);
        	}
        }catch (FileNotFoundException localFileNotFoundException){
        	Log.e("dsu", "FileNotFoundException : " + localFileNotFoundException.getMessage());
        	Response = "fail";
        	File delete_file = new File(sub_file_name); 
//			Log.i("dsu", "file_check : " + file.exists());
			if(file.exists() == true){
			delete_file.delete();	
			}
//        	while (true)
//        		localFileNotFoundException.printStackTrace();
        }catch (IOException localIOException){
        	Log.e("dsu", "localIOException : " + localIOException.getMessage());
        	Response = "fail";
        	File delete_file = new File(sub_file_name); 
//			Log.i("dsu", "file_check : " + file.exists());
			if(file.exists() == true){
			delete_file.delete();	
			}
//        	while (true)
//                localIOException.printStackTrace();
        }
		return Response;
	}
	
	
	
	
	@Override
    protected void onPreExecute() {
		mNotificationHelper.createNotification();
		PodcastActivity_Sub.updateStatus(position, 0, enclosure, description_title);
		super.onPreExecute();
    }
	@Override
	protected void onPostExecute(String Response) {
		notificationManager.cancel(this.NOTIFICATION_ID);
		PodcastActivity_Sub.updateStatus(position, 100, enclosure, description_title);
		if(Response.equals("success")){
			ContentValues cv = new ContentValues();
			cv.put("title", old_title);
			cv.put("enclosure", sub_file_name);
			cv.put("pubDate", pubDate);
			cv.put("image", image);
			cv.put("description_title", description_title);
			cv.put("provider", provider);
			down_mydb.getWritableDatabase().insert("download_list", null, cv);
			((PodcastActivity_Sub)context).datasetchanged();
		}
		super.onPostExecute(Response);
	}
	@Override
	protected void onProgressUpdate(Integer... values) {
		PodcastActivity_Sub.updateStatus(position, values[0].intValue(), enclosure, description_title);
		super.onProgressUpdate(values);
	}
	
	public class NotificationHelper{
	    public PendingIntent mContentIntent;
	    public CharSequence mContentTitle;
	    public Context mContext;
	    public Notification mNotification;
	    public NotificationManager mNotificationManager;

	    public NotificationHelper(Context context){
	      this.mContext = context;
	    }

	    public void completed(){
	      this.mNotificationManager.cancel(DownloadAsync.this.NOTIFICATION_ID);
	    }

	    public void createNotification(){
	      Intent localIntent = new Intent(mContext, mContext.getApplicationContext().getClass());
	      PendingIntent localPendingIntent = PendingIntent.getActivity(mContext, 0, localIntent, 0);
	      DownloadAsync.this.notification = new Notification(R.drawable.ic_app, DownloadAsync.this.old_title, System.currentTimeMillis());
	      DownloadAsync.this.notification.flags = (0x10 | DownloadAsync.this.notification.flags);
	      DownloadAsync.this.notification.contentView = new RemoteViews(this.mContext.getPackageName(), R.layout.notibar);
	      DownloadAsync.this.notification.contentIntent = localPendingIntent;
	      DownloadAsync.this.notification.contentView.setImageViewResource(R.id.status_icon, R.drawable.ic_app);
	      DownloadAsync.this.notification.contentView.setTextViewText(R.id.status_title, DownloadAsync.this.old_title);
	      DownloadAsync.this.notification.contentView.setTextViewText(R.id.status_text, context.getString(R.string.activity_podcast_08));
	      DownloadAsync.this.notification.contentView.setTextColor(R.id.status_title, Color.parseColor("#4bc1d2"));
	      DownloadAsync.this.notification.contentView.setTextColor(R.id.status_text, Color.parseColor("#4bc1d2"));
	      DownloadAsync.this.notification.contentView.setProgressBar(R.id.status_progress, 100, DownloadAsync.this.progress, false);
	      DownloadAsync.this.notificationManager = ((NotificationManager)this.mContext.getSystemService("notification"));
	      DownloadAsync.this.notificationManager.notify(DownloadAsync.this.NOTIFICATION_ID, DownloadAsync.this.notification);
	    }
	}
}
