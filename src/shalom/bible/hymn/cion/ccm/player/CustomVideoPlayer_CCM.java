package shalom.bible.hymn.cion.ccm.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import shalom.bible.hymn.cion.R;
import shalom.bible.hymn.cion.common.Const;
import shalom.bible.hymn.cion.util.PreferenceUtil;
import shalom.bible.hymn.cion.util.StringUtil;
import shalom.bible.hymn.cion.util.TimeUtil;

public class CustomVideoPlayer_CCM extends SherlockActivity implements OnCompletionListener, OnPreparedListener,android.widget.SeekBar.OnSeekBarChangeListener, OnErrorListener, OnClickListener, OnTouchListener, AdViewListener, InterstitialAdListener{
	private ArrayList<String> array_videoid, array_subject;
	private Mobile_YoutubeAsync mobile_youtubeAsync = null;
	private int video_num = 0;
	private CustomVideoView videoView;
	private LinearLayout layout_progress;
	private Context context;
	private SeekBar mediacontroller_progress;
	private Handler navigator_handler = new Handler();
	private TextView max_time, current_time, txt_video_title;
	private ImageButton bt_rew, bt_duration_rew, bt_pause, bt_duration_ffwd, bt_ffwd;
	private int seekBackwardTime = 5000; // 5000 milliseconds
	private int seekForwardtime = 5000; // 5000 milliseconds
	private static ImageView btn_lock, btn_screen_orientation;
	private boolean isLock = false;
	private static LinearLayout layout_control;
	private static RelativeLayout layout_video_View;
	private Handler mHandler = new Handler();
	private int duration_check = 0;
	Editor edit;
	public Handler handler = new Handler();
	public static RelativeLayout ad_layout;
	public static InterstitialAd interstialAd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_videoplayer);
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMIXER, "vuh3q0an");
    	AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB, "ca-app-pub-4637651494513698/7757784963");
    	AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB_FULL, "ca-app-pub-4637651494513698/1711251362");
		context = this;
		layout_video_View = (RelativeLayout)findViewById(R.id.layout_video_View);
		layout_video_View.setOnTouchListener(this);
		videoView = (CustomVideoView)findViewById(R.id.video_View);
		layout_progress = (LinearLayout)findViewById(R.id.layout_progress);
		layout_control = (LinearLayout)findViewById(R.id.layout_control);
		mediacontroller_progress = (SeekBar)findViewById(R.id.mediacontroller_progress);
		max_time = (TextView)findViewById(R.id.max_time);
		current_time = (TextView)findViewById(R.id.current_time);
		txt_video_title = (TextView)findViewById(R.id.txt_video_title);
		bt_rew = (ImageButton)findViewById(R.id.bt_rew);
		bt_duration_rew = (ImageButton)findViewById(R.id.bt_duration_rew);
		bt_pause = (ImageButton)findViewById(R.id.bt_pause);
		bt_duration_ffwd = (ImageButton)findViewById(R.id.bt_duration_ffwd);
		bt_ffwd = (ImageButton)findViewById(R.id.bt_ffwd);
		btn_lock = (ImageView)findViewById(R.id.btn_lock);
		btn_screen_orientation = (ImageView)findViewById(R.id.btn_screen_orientation);
		bt_rew.setOnClickListener(this);
		bt_duration_rew.setOnClickListener(this);
		bt_pause.setOnClickListener(this);
		bt_duration_ffwd.setOnClickListener(this);
		bt_ffwd.setOnClickListener(this);
		btn_lock.setOnClickListener(this);
		btn_screen_orientation.setOnClickListener(this);
		array_videoid = getIntent().getStringArrayListExtra("array_videoid");
		array_subject = getIntent().getStringArrayListExtra("array_subject");
		video_num = array_videoid.size()-1;
		video_sequence_start(video_num);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if ((videoView != null) && (videoView.isPlaying())){
			videoView.pause();
			duration_check = videoView.getCurrentPosition();
	    }
	}
	@Override
	protected void onStop() {
		super.onStop();
	    try{
	    	videoView = (CustomVideoView)findViewById(R.id.video_View);
			videoView.setKeepScreenOn(true);
	    	if ((videoView != null) && (videoView.isPlaying())){
				videoView.pause();
				duration_check = videoView.getCurrentPosition();
		    }
	    	mHandler.removeMessages(0);
	    	return;
	    }catch (IllegalStateException localIllegalStateException){
	    }
	    catch (IllegalArgumentException localIllegalArgumentException){
	    }
	    catch (NullPointerException localNullPointerException){
	    }
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		navigator_handler.removeCallbacks(UpdateTimetask);
		videoView = null;
		if(mobile_youtubeAsync != null){
			mobile_youtubeAsync.cancel(true);
		}
		finish();
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		try{
			videoView = (CustomVideoView)findViewById(R.id.video_View);
			videoView.setKeepScreenOn(true);
			updateProgressBar();
	    	if(!videoView.isPlaying()){
	    		if(duration_check > 0){
	    			videoView.seekTo(duration_check);
	    			videoView.start();
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
		super.onUserLeaveHint();
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
					PlayVideo(Response);
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
    
    public void updateProgressBar(){
		navigator_handler.postDelayed(UpdateTimetask, 100);
	}
    
    public Runnable UpdateTimetask = new Runnable() {
		@Override
		public void run() {
			if(videoView != null){
				if(videoView.isPlaying()){
					layout_progress.setVisibility(View.GONE);
					bt_pause.setImageResource(R.drawable.ic_action_pause);
				}else{
					bt_pause.setImageResource(R.drawable.ic_action_play);
				}
				long totalDuration = videoView.getDuration();
				long currentDuration = videoView.getCurrentPosition();
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
	
	public void PlayVideo(String target_url){
		try{
			if(mobile_youtubeAsync != null){
				mobile_youtubeAsync.cancel(true);
			}
			videoView.setOnCompletionListener(this);
			videoView.setOnErrorListener(this);
			videoView.setKeepScreenOn(true);
			videoView.setOnPreparedListener(this);
			mediacontroller_progress.setOnSeekBarChangeListener(this);
			CustomMediaController mediaController = new CustomMediaController(context);
			mediaController.setAnchorView(videoView);
			Uri uri = Uri.parse(target_url);           
			videoView.setMediaController(mediaController);
			videoView.setVideoURI(uri);
			videoView.requestLayout();
			videoView.requestFocus();    
			mediacontroller_progress.setProgress(0);
			mediacontroller_progress.setSecondaryProgress(0);
			mediacontroller_progress.setMax(100);
			updateProgressBar();
		}catch(Exception e){
		} 
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if(!videoView.isPlaying()){
					videoView.start();
				}
			}
		},1000);
	}
	
	@Override
	public boolean onError(MediaPlayer mp, int arg1, int arg2) {
		return false;
	}
	@Override
	public void onProgressChanged(SeekBar seekBar, int arg1, boolean arg2) {
	}
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		navigator_handler.removeCallbacks(UpdateTimetask);
	}
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		if(videoView != null){
			int totalDuration = videoView.getDuration();	
			int currentPosition = TimeUtil.progressToTimer(seekBar.getProgress(), totalDuration);
			// forward or backward to certain seconds
			videoView.seekTo(currentPosition);
			if (videoView.isPlaying()){
				videoView.start();
				bt_pause.setImageResource(R.drawable.ic_action_pause);
				updateProgressBar();
		    }else{
		    	videoView.start();
		    	bt_pause.setImageResource(R.drawable.ic_action_pause);
				updateProgressBar();
		    }
		}
	}
	@Override
	public void onPrepared(MediaPlayer mp) {
		txt_video_title.setText(array_subject.get(video_num));
		toggleControl();
		mp.start();
		mp.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
			@Override
			public void onBufferingUpdate(MediaPlayer mp, int percent) {
				mediacontroller_progress.setSecondaryProgress(percent);
			}
		});
		
	}
	@Override
	public void onCompletion(MediaPlayer mp) {
		navigator_handler.removeCallbacks(UpdateTimetask);
		if(videoView != null){
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
	public void onClick(View view) {
		if(view == bt_ffwd){
			if(video_num > 0){
				videoView.pause();
				navigator_handler.removeCallbacks(UpdateTimetask);
				video_num = video_num - 1;
				video_sequence_start(video_num);
			}else{
				videoView.pause();
				navigator_handler.removeCallbacks(UpdateTimetask);
				video_num = array_videoid.size()-1;
				video_sequence_start(video_num);
			}
		}else if(view == bt_rew){
			if(video_num < array_videoid.size() -1){
				videoView.pause();
				navigator_handler.removeCallbacks(UpdateTimetask);
				video_num = video_num + 1;
				video_sequence_start(video_num);
			}else{
				Toast.makeText(context, context.getString(R.string.frg_ccm_11), Toast.LENGTH_SHORT).show();
			}
			
		}else if(view == bt_pause){
			if(videoView.isPlaying()){
				videoView.pause();
				bt_pause.setImageResource(R.drawable.ic_action_pause);
			}else{
				videoView.start();
				bt_pause.setImageResource(R.drawable.ic_action_pause);
			}
		}else if(view == bt_duration_rew){
			int currentPosition = videoView.getCurrentPosition();
			// check if seekBackward time is greater than 0 sec
			if(currentPosition - seekBackwardTime >= 0){
				// forward song
				videoView.seekTo(currentPosition - seekBackwardTime); 
			}else{
				// backward to starting position
				videoView.seekTo(0);
			}
		}else if(view == bt_duration_ffwd){
			int currentPosition = videoView.getCurrentPosition();
			if(currentPosition + seekForwardtime <= videoView.getDuration()){
				// forward song
				videoView.seekTo(currentPosition + seekForwardtime);
			}else{
				// forward to end position
				videoView.seekTo(videoView.getDuration());
			}
		}else if(view == btn_lock){
			if(btn_lock.isSelected()){
				btn_lock.setSelected(false);
				isLock = false;
				btn_lock.setImageResource(R.drawable.ic_action_not_secure);
				btn_lock.setVisibility(View.VISIBLE);
				layout_control.setVisibility(View.VISIBLE);
				txt_video_title.setVisibility(View.VISIBLE);
			}else{
				btn_lock.setSelected(true);
				isLock = true;
				btn_lock.setImageResource(R.drawable.ic_action_secure);
				btn_lock.setVisibility(View.VISIBLE);
				layout_control.setVisibility(View.INVISIBLE);
				txt_video_title.setVisibility(View.INVISIBLE);
			}
		}else if(view == btn_screen_orientation){
			try{
				if(btn_screen_orientation.isSelected()){
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					btn_screen_orientation.setSelected(false);
					btn_screen_orientation.setImageResource(R.drawable.ic_action_full_screen);
					btn_screen_orientation.setVisibility(View.VISIBLE);
					if(isLock == true){
						layout_control.setVisibility(View.INVISIBLE);
						txt_video_title.setVisibility(View.INVISIBLE);
					}else{
						layout_control.setVisibility(View.VISIBLE);
						txt_video_title.setVisibility(View.VISIBLE);
					}
					
				}else{
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); 
					btn_screen_orientation.setSelected(true);
					btn_screen_orientation.setImageResource(R.drawable.ic_action_return_from_full_screen);
					btn_screen_orientation.setVisibility(View.VISIBLE);
					if(isLock == true){
						layout_control.setVisibility(View.INVISIBLE);
						txt_video_title.setVisibility(View.INVISIBLE);
					}else{
						layout_control.setVisibility(View.VISIBLE);
						txt_video_title.setVisibility(View.VISIBLE);
					}
				}
			}catch(Exception e){
			}
		}
	}
	
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		if(view == layout_video_View){
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				toggleControl();
			}
		}
		return false;
	}
	
	public void toggleControl(){
		if(isLock == true){
			if (btn_lock.isShown()){
		    	btn_lock.setVisibility(View.INVISIBLE);
		    	btn_screen_orientation.setVisibility(View.INVISIBLE);
		    	layout_control.setVisibility(View.INVISIBLE);
		    	txt_video_title.setVisibility(View.INVISIBLE);
		    }else{
		    	 btn_lock.setVisibility(View.VISIBLE);
		    	 btn_screen_orientation.setVisibility(View.VISIBLE);
		    	 layout_control.setVisibility(View.INVISIBLE);
		    	 txt_video_title.setVisibility(View.INVISIBLE);
		    	 mHandler.removeCallbacks(hideLock);
		    	 mHandler.postDelayed(hideLock, 8000L);
		    }
		}else{
			if (btn_lock.isShown()){
		    	btn_lock.setVisibility(View.INVISIBLE);
		    	btn_screen_orientation.setVisibility(View.INVISIBLE);
		    	layout_control.setVisibility(View.INVISIBLE);
		    	txt_video_title.setVisibility(View.INVISIBLE);
		    }else{
		    	 btn_lock.setVisibility(View.VISIBLE);
		    	 btn_screen_orientation.setVisibility(View.VISIBLE);
		    	 layout_control.setVisibility(View.VISIBLE);
		    	 txt_video_title.setVisibility(View.VISIBLE);
		    	 mHandler.removeCallbacks(hideControl);
		    	 mHandler.postDelayed(hideControl, 8000L);
		    }
		}
	}
	public Runnable hideLock = new Runnable(){
	    public void run(){
	    	btn_lock.setVisibility(View.INVISIBLE);
	    	btn_screen_orientation.setVisibility(View.INVISIBLE);
	    	layout_control.setVisibility(View.INVISIBLE);
	    	txt_video_title.setVisibility(View.INVISIBLE);
	    }
	};
	public Runnable hideControl = new Runnable(){
	    public void run(){
	    	btn_lock.setVisibility(View.INVISIBLE);
	    	btn_screen_orientation.setVisibility(View.INVISIBLE);
	    	layout_control.setVisibility(View.INVISIBLE);
	    	txt_video_title.setVisibility(View.INVISIBLE);
	    }
	};
	
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
	public void onBackPressed() {
		super.onBackPressed();
	}
    
	@Override
	public void onInterstitialAdClosed(InterstitialAd arg0) {
		interstialAd = null;
		finish();
	}

	@Override
	public void onInterstitialAdFailedToReceive(int arg0, String arg1,
			InterstitialAd arg2) {
		interstialAd = null;
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
			if(isLock == true){
				Toast.makeText(context, context.getString(R.string.frg_ccm_13), Toast.LENGTH_SHORT).show();
				return false;
			}else{
				if(!PreferenceUtil.getStringSharedData(context, PreferenceUtil.PREF_ISSUBSCRIBED, Const.isSubscribed).equals("true")){
					Toast.makeText(context, context.getString(R.string.txt_after_ad), Toast.LENGTH_SHORT).show();
					addInterstitialView();					
				}
				handler.postDelayed(new Runnable() {
					 @Override
					 public void run() {
						 onDestroy();
					 }
				 },3000);
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
