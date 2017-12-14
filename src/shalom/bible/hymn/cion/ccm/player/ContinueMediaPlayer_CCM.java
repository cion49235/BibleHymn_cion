package shalom.bible.hymn.cion.ccm.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.admixer.AdAdapter;
import com.admixer.AdInfo;
import com.admixer.AdMixerManager;
import com.admixer.AdView;
import com.admixer.AdViewListener;
import com.admixer.InterstitialAd;
import com.admixer.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import shalom.bible.hymn.cion.R;
import shalom.bible.hymn.cion.util.NotificationUtil;
import shalom.bible.hymn.cion.util.StringUtil;
import shalom.bible.hymn.cion.util.TimeUtil;

public class ContinueMediaPlayer_CCM extends SherlockActivity implements OnClickListener, AdViewListener, InterstitialAdListener, OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener,android.widget.SeekBar.OnSeekBarChangeListener, OnErrorListener{
	private LinearLayout layout_progress;
	private Context context;
	private SeekBar mediacontroller_progress;
	private Handler navigator_handler = new Handler();
	private TextView max_time, current_time,txt_title, txt_pubDate;
	private ImageButton bt_duration_rew, bt_pause, bt_duration_ffwd, bt_rew, bt_ffwd;
	private int seekBackwardTime = 5000; // 5000 milliseconds
	private int seekForwardtime = 5000; // 5000 milliseconds
	private int duration_check = 0;
	private MediaPlayer mediaPlayer = new MediaPlayer();
//	private String title,enclosure,pubDate,image,description_title;
	private ArrayList<String> array_videoid,array_music,array_artist,array_imageurl, array_catecode, array_playtime;
	private ImageView img_image, btn_media_continue;
	private LinearLayout control_panel_layout;
	private RelativeLayout ad_layout;
	private InterstitialAd interstialAd;
	private Handler handler = new Handler();
	private ImageButton btnLeft;
	private TextView main_title;
	private int video_num = 0;
	private Mobile_YoutubeAsync mobile_youtubeAsync = null;
	private NativeExpressAdView admobNative;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.continue_mediaplayer);
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMIXER, "vuh3q0an");
    	AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB, "ca-app-pub-4637651494513698/7757784963");
    	AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB_FULL, "ca-app-pub-4637651494513698/1711251362");
		context = this;
		addBannerView();
//		init_admob_naive();
		init_ui();
		telephony_manager();
		get_data();
		set_titlebar();
		
		video_sequence_start(video_num);
	}
	
	private void init_ui(){
		layout_progress = (LinearLayout)findViewById(R.id.layout_progress);
		control_panel_layout = (LinearLayout)findViewById(R.id.control_panel_layout);
		mediacontroller_progress = (SeekBar)findViewById(R.id.mediacontroller_progress);
		max_time = (TextView)findViewById(R.id.max_time);
		current_time = (TextView)findViewById(R.id.current_time);
		txt_title = (TextView)findViewById(R.id.txt_title);
		txt_title.setSelected(true);
		txt_pubDate = (TextView)findViewById(R.id.txt_pubDate);
		img_image = (ImageView)findViewById(R.id.img_image);
		btn_media_continue = (ImageView)findViewById(R.id.btn_media_continue);
		btn_media_continue.setOnClickListener(this);
		bt_duration_rew = (ImageButton)findViewById(R.id.bt_duration_rew);
		bt_pause = (ImageButton)findViewById(R.id.bt_pause);
		bt_duration_ffwd = (ImageButton)findViewById(R.id.bt_duration_ffwd);
		bt_rew = (ImageButton)findViewById(R.id.bt_rew);
		bt_ffwd = (ImageButton)findViewById(R.id.bt_ffwd);
		bt_duration_rew.setOnClickListener(this);
		bt_pause.setOnClickListener(this);
		bt_duration_ffwd.setOnClickListener(this);
		bt_rew.setOnClickListener(this);
		bt_ffwd.setOnClickListener(this);
	}
	
	private void set_titlebar(){
		btnLeft = (ImageButton)findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(this);
		main_title = (TextView)findViewById(R.id.main_title);
		main_title.setTextSize(12);
		main_title.setText(context.getString(R.string.frg_ccm_12));
		main_title.setSingleLine();
	}
	
	private void get_data(){
		array_videoid = getIntent().getStringArrayListExtra("array_videoid");
		array_music = getIntent().getStringArrayListExtra("array_music");
		array_artist = getIntent().getStringArrayListExtra("array_artist");
		array_imageurl = getIntent().getStringArrayListExtra("array_imageurl");
		array_catecode = getIntent().getStringArrayListExtra("array_catecode");
		array_playtime = getIntent().getStringArrayListExtra("array_playtime");
		
		video_num = getIntent().getIntExtra("video_num",array_videoid.size()-1);
		
		/*down_buffer = getIntent().getBooleanExtra("down_buffer", false);
		if(down_buffer == true){
			mediacontroller_progress.setSecondaryProgress(100);
		}*/
	}
	
	private void telephony_manager(){
		TelephonyManager telephonymanager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		telephonymanager.listen(new PhoneStateListener() {
			public void onCallStateChanged(int state, String incomingNumber) {
				switch (state) {
				case TelephonyManager.CALL_STATE_IDLE: 
					
				case TelephonyManager.CALL_STATE_OFFHOOK:
					if ((mediaPlayer != null) && (mediaPlayer.isPlaying())){
						mediaPlayer.pause();
						duration_check = mediaPlayer.getCurrentPosition();
					}
				case TelephonyManager.CALL_STATE_RINGING:
					if ((mediaPlayer != null) && (mediaPlayer.isPlaying())){
						mediaPlayer.pause();
						duration_check = mediaPlayer.getCurrentPosition();
					}
				default: break;
				} 
			}
		}, PhoneStateListener.LISTEN_CALL_STATE);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		invokeFragmentManagerNoteStateNotSaved();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void invokeFragmentManagerNoteStateNotSaved() {
	    /**
	     * For post-Honeycomb devices
	     */
	    if (Build.VERSION.SDK_INT < 11) {
	        return;
	    }
	    try {
	        Class cls = getClass();
	        do {
	            cls = cls.getSuperclass();
	        } while (!"Activity".equals(cls.getSimpleName()));
	        Field fragmentMgrField = cls.getDeclaredField("mFragments");
	        fragmentMgrField.setAccessible(true);

	        Object fragmentMgr = fragmentMgrField.get(this);
	        cls = fragmentMgr.getClass();

	        Method noteStateNotSavedMethod = cls.getDeclaredMethod("noteStateNotSaved", new Class[] {});
	        noteStateNotSavedMethod.invoke(fragmentMgr, new Object[] {});
	        Log.d("dsu", "Successful call for noteStateNotSaved!!!");
	    } catch (Exception ex) {
	        Log.e("dsu", "Exception on worka FM.noteStateNotSaved", ex);
	    }
	}
	
	@Override
	protected void onPause() {
		super.onPause();
//		admobNative.pause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
//		admobNative.resume();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
//		admobNative.destroy();
		navigator_handler.removeCallbacks(UpdateTimetask);
		finish();
		super.onDestroy();
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		try{
			updateProgressBar();
	    	if(!mediaPlayer.isPlaying()){
	    		if(duration_check > 0){
	    			mediaPlayer.seekTo(duration_check);
	    			mediaPlayer.start();
	    		}
	    		return;
	    	}
	    }catch (IllegalStateException localIllegalStateException){
	    }
	    catch (IllegalArgumentException localIllegalArgumentException){
	    }
	    catch (NullPointerException localNullPointerException){
	    }
	}
	@Override
	protected void onUserLeaveHint() {
		NotificationUtil.setNotification_continue_ccm(context, array_music, array_videoid, array_playtime, array_imageurl, array_artist, video_num );
		super.onUserLeaveHint();
	}
	
	private void init_admob_naive(){
		RelativeLayout nativeContainer = (RelativeLayout) findViewById(R.id.admob_native);
		AdRequest adRequest = new AdRequest.Builder().build();	    
		admobNative = new NativeExpressAdView(this);
		admobNative.setAdSize(new AdSize(360, 100));
		admobNative.setAdUnitId("ca-app-pub-4637651494513698/6141450967");
		nativeContainer.addView(admobNative);
		admobNative.loadAd(adRequest);
	}
	
	public void addBannerView() {
    	AdInfo adInfo = new AdInfo("vuh3q0an");
    	adInfo.setTestMode(false);
        AdView adView = new AdView(this);
        adView.setAdInfo(adInfo, this);
        adView.setAdViewListener(this);
        ad_layout = (RelativeLayout)findViewById(R.id.ad_layout);
        if(ad_layout != null){
        	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            ad_layout.addView(adView, params);	
        }
    }
	
	public void addInterstitialView() {
    	if(interstialAd == null) {
        	AdInfo adInfo = new AdInfo("vuh3q0an");
//        	adInfo.setTestMode(false);
        	interstialAd = new InterstitialAd(this);
        	interstialAd.setAdInfo(adInfo, this);
        	interstialAd.setInterstitialAdListener(this);
        	interstialAd.startInterstitial();
    	}
    }
    
    public void updateProgressBar(){
		navigator_handler.postDelayed(UpdateTimetask, 100);
	}
    
    public Runnable UpdateTimetask = new Runnable() {
		@Override
		public void run() {
			if(mediaPlayer != null){
				if(mediaPlayer.isPlaying()){
					layout_progress.setVisibility(View.GONE);
					bt_pause.setImageResource(R.drawable.ic_action_pause);
				}else{
					bt_pause.setImageResource(R.drawable.ic_action_play);
				}
				long totalDuration = mediaPlayer.getDuration();
				long currentDuration = mediaPlayer.getCurrentPosition();
				// Displaying Total Duration time
				max_time.setText(""+TimeUtil.milliSecondsToTimer(totalDuration));
				// Displaying time completed playing
				current_time.setText(""+TimeUtil.milliSecondsToTimer(currentDuration));
				// Updating progress bar
				int progress = (int)(TimeUtil.getProgressPercentage(currentDuration, totalDuration));
				mediacontroller_progress.setProgress(progress);
				navigator_handler.postDelayed(this, 100);	
			}
		}
	};
	
	public void video_sequence_start(int video_num){
		mobile_youtubeAsync = new Mobile_YoutubeAsync(array_videoid.get(video_num));
		mobile_youtubeAsync.execute();
	}
	
	public class Mobile_YoutubeAsync extends AsyncTask<String, Integer, String>{
	 	   String videoId;
	 	   String url;
	 	   public Mobile_YoutubeAsync(String videoId){
	 		   this.videoId = videoId;
	 	   }
				@Override
				protected String doInBackground(String... params) {
			  	   	try{
						String url_youtube_video_info = "http://www.youtube.com/get_video_info?video_id=" + videoId;
				        URL localURL = new URL(url_youtube_video_info);
				        HttpURLConnection localHttpURLConnection1 = (HttpURLConnection)localURL.openConnection();
				        HttpURLConnection.setFollowRedirects(false);
				        localHttpURLConnection1.setConnectTimeout(15000);
				        localHttpURLConnection1.setReadTimeout(15000);
				        localHttpURLConnection1.setRequestMethod("GET");
				        localHttpURLConnection1.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; de; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3 (.NET CLR 3.5.30729) (Prevx 3.0.5)");
				        localHttpURLConnection1.connect();
				        InputStream localInputStream1 = localHttpURLConnection1.getInputStream();
				        InputStreamReader localInputStreamReader1 = new InputStreamReader(localInputStream1);
				        BufferedReader localBufferedReader1 = new BufferedReader(localInputStreamReader1, 8192);
				        StringBuilder localStringBuilder = new StringBuilder();
				        HttpURLConnection localHttpURLConnection2;
				        while (true)
				        {
				        	String str2 = localBufferedReader1.readLine();
				        	if (str2 == null)
					        {
				        		localBufferedReader1.close();
				        		localHttpURLConnection1.disconnect();
				        		String str3 = localStringBuilder.toString();
				        		String str4 = str3.substring(str3.indexOf("url_encoded_fmt_stream_map"));
				        		String str5 = URLDecoder.decode(str4.substring(0, str4.indexOf("&")).trim(), "UTF-8");
				        		if (str5 == null)
				                    break;
				                  String str6 = URLDecoder.decode(str5, "UTF-8").replace("url_encoded_fmt_stream_map=", "").replace("sig=", "signature=");
				                  if (!str6.startsWith("url="))
				                    break;
				                  url = StringUtil.getUrlType(str6, "mp4");
				                  if (url == null)
				                    break;
				                  url = StringUtil.removeComma(url).replace("&&", "&");
				                  URL localURL2 = new URL(url);
				                  localHttpURLConnection2 = (HttpURLConnection)localURL2.openConnection();
				                  int i = localHttpURLConnection2.getResponseCode();
				                  if ((i == 200) || (i == 302))
				                  break;
				                  url = null;
				                  break;
					        }
				        	localStringBuilder.append(str2);
				        } 
			  	   	}catch (MalformedURLException e) {
						
					} catch (IOException e) {
					
					} catch (IllegalArgumentException e){
					
					} catch (Resources.NotFoundException e){
					
					} catch (StringIndexOutOfBoundsException e){
					
					} catch (RuntimeException e){
						
					} 
			  	   	return url;
				}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			layout_progress.setVisibility(View.VISIBLE);
		}
		@Override
		protected void onPostExecute(String Response) {
			super.onPostExecute(Response);
			try{
//				Log.i("dsu", "Response2 : " + Response);
				if(Response != null){
					PlayMedia(Response);
				}else{
					if(mobile_youtubeAsync != null){
						mobile_youtubeAsync.cancel(true);
					}
					mobile_youtubeAsync = new Mobile_YoutubeAsync(videoId);
					mobile_youtubeAsync.execute();
				}		
			}catch(NullPointerException e){
			}
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}									
	}
	
	public void PlayMedia(String target_url){
		try{
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnCompletionListener(this);
			mediaPlayer.setOnErrorListener(this);
			mediaPlayer.setOnPreparedListener(this);
			mediacontroller_progress.setOnSeekBarChangeListener(this);
			
			mediaPlayer.reset();
            mediaPlayer.setDataSource(target_url);
            mediaPlayer.prepare();
            
            mediacontroller_progress.setProgress(0);
			mediacontroller_progress.setMax(100);
			mediaPlayer.seekTo(0);
			updateProgressBar();
		}catch (Exception e) {
		}
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if(!mediaPlayer.isPlaying()){
					mediaPlayer.start();
				}
			}
		},1000);
	}
	
	@Override
	public boolean onError(MediaPlayer mp, int arg1, int arg2) {
		return true;
	}

	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekbar) {
		navigator_handler.removeCallbacks(UpdateTimetask);
	}
	
	
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		if(mediaPlayer != null){
			int totalDuration = mediaPlayer.getDuration();
			int currentPosition = TimeUtil.progressToTimer(seekBar.getProgress(), totalDuration);
			// forward or backward to certain seconds
			mediaPlayer.seekTo(currentPosition);
			if (mediaPlayer.isPlaying()){
				mediaPlayer.start();
				bt_pause.setImageResource(R.drawable.ic_action_pause);
				updateProgressBar();
		    }else{
		    	mediaPlayer.start();
				bt_pause.setImageResource(R.drawable.ic_action_pause);
				updateProgressBar();
		    }
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		txt_title.setText(array_music.get(video_num));
		txt_pubDate.setText(array_playtime.get(video_num));
		Picasso.with(context)
	    .load(array_imageurl.get(video_num))
	    .placeholder(R.drawable.empty_image)
	    .error(R.drawable.empty_image)
	    .into(img_image);
		if(mp != null){
			if(mp.isPlaying()){
				bt_pause.setImageResource(R.drawable.ic_action_pause);
			}else{
				bt_pause.setImageResource(R.drawable.ic_action_play);
			}
			mp.start();
			NotificationUtil.setNotification_continue_ccm(context, array_music, array_videoid, array_playtime, array_imageurl, array_artist, video_num );
			layout_progress.setVisibility(View.GONE);
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		if(mp != null){
			if(video_num > 0){
				video_num = video_num - 1;
				video_sequence_start(video_num);
			}else{
				video_num = array_videoid.size()-1;
				video_sequence_start(video_num);
			}	
		}
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int buffering) {
		mediacontroller_progress.setSecondaryProgress(buffering);
	}
	
	@Override
	public void onClick(View view) {
		if(view == btnLeft){
			exit_action();
		}else if(view == bt_pause){
			if(mediaPlayer.isPlaying()){
				mediaPlayer.pause();
				bt_pause.setImageResource(R.drawable.ic_action_play);
			}else{
				mediaPlayer.start();
				bt_pause.setImageResource(R.drawable.ic_action_pause);
			}
		}else if(view == bt_duration_rew){
			int currentPosition = mediaPlayer.getCurrentPosition();
			// check if seekBackward time is greater than 0 sec
			if(currentPosition - seekBackwardTime >= 0){
				// forward song
				mediaPlayer.seekTo(currentPosition - seekBackwardTime); 
			}else{
				// backward to starting position
				mediaPlayer.seekTo(0);
			}
		}else if(view == bt_duration_ffwd){
			int currentPosition = mediaPlayer.getCurrentPosition();
			if(currentPosition + seekForwardtime <= mediaPlayer.getDuration()){
				// forward song
				mediaPlayer.seekTo(currentPosition + seekForwardtime);
			}else{
				// forward to end position
				mediaPlayer.seekTo(mediaPlayer.getDuration());
			}
		}else if(view == bt_rew){
			if(video_num < array_videoid.size() -1){
				mediaPlayer.pause();
				navigator_handler.removeCallbacks(UpdateTimetask);
				video_num = video_num + 1;
				video_sequence_start(video_num);
			}else{
				Toast.makeText(context, context.getString(R.string.frg_ccm_11), Toast.LENGTH_SHORT).show();
			}
		}else if(view == bt_ffwd){
			if(video_num > 0){
				mediaPlayer.pause();
				navigator_handler.removeCallbacks(UpdateTimetask);
				video_num = video_num - 1;
				video_sequence_start(video_num);
			}else{
				mediaPlayer.pause();
				navigator_handler.removeCallbacks(UpdateTimetask);
				video_num = array_videoid.size()-1;
				video_sequence_start(video_num);
			}
		}else if(view == btn_media_continue){
			Toast.makeText(context, context.getString(R.string.activity_podcast_12), Toast.LENGTH_LONG).show();
			addInterstitialView();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
			if(mediaPlayer.isPlaying() == true){
				mediaPlayer.pause();
			}
			Toast.makeText(context, context.getString(R.string.txt_after_ad), Toast.LENGTH_SHORT).show();
			addInterstitialView();
			 handler.postDelayed(new Runnable() {
				 @Override
				 public void run() {
					 if(mediaPlayer.isPlaying() == true){
						 mediaPlayer.pause();
					 }
					 onDestroy();
					 NotificationUtil.setNotification_Cancel();
				 }
			 },1500);
			 return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void exit_action(){
		if(mediaPlayer.isPlaying() == true){
			mediaPlayer.pause();
		}
		Toast.makeText(context, context.getString(R.string.txt_after_ad), Toast.LENGTH_SHORT).show();
		addInterstitialView();
		 handler.postDelayed(new Runnable() {
			 @Override
			 public void run() {
				 if(mediaPlayer.isPlaying() == true){
					 mediaPlayer.pause();
				 }
				 onDestroy();
				 NotificationUtil.setNotification_Cancel();
				 finish();
			 }
		 },1500);
	}
	
	private void home_action(){
		if(mediaPlayer.isPlaying()){
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
		}
	}
	
	@Override
	public void onClickedAd(String arg0, AdView arg1) {
	}

	@Override
	public void onFailedToReceiveAd(int arg0, String arg1, AdView arg2) {
	}

	@Override
	public void onReceivedAd(String arg0, AdView arg1) {
	}
	
	@Override
	public void onInterstitialAdClosed(InterstitialAd arg0) {
		interstialAd = null;
		home_action();
		
	}

	@Override
	public void onInterstitialAdFailedToReceive(int arg0, String arg1,
			InterstitialAd arg2) {
		interstialAd = null;
		home_action();
	}

	@Override
	public void onInterstitialAdReceived(String arg0, InterstitialAd arg1) {
		interstialAd = null;
	}

	@Override
	public void onInterstitialAdShown(String arg0, InterstitialAd arg1) {
	}

	@Override
	public void onLeftClicked(String arg0, InterstitialAd arg1) {
	}

	@Override
	public void onRightClicked(String arg0, InterstitialAd arg1) {
	}
}
