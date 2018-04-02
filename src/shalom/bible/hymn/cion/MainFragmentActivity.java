package shalom.bible.hymn.cion;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.admixer.AdAdapter;
import com.admixer.AdInfo;
import com.admixer.AdMixerManager;
import com.admixer.AdView;
import com.admixer.AdViewListener;
import com.admixer.CustomPopup;
import com.admixer.CustomPopupListener;
import com.admixer.InterstitialAd;
import com.admixer.InterstitialAdListener;
import com.admixer.PopupInterstitialAdOption;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import kr.co.inno.autocash.service.AutoServiceActivity;
import shalom.bible.hymn.cion.activity.QuestionActivity;
import shalom.bible.hymn.cion.activity.SearchActivity;
import shalom.bible.hymn.cion.colorpicker.AmbilWarnaDialog;
import shalom.bible.hymn.cion.common.Const;
import shalom.bible.hymn.cion.db.helper.DBOpenHelper;
import shalom.bible.hymn.cion.fragment.Fragment_CCM;
import shalom.bible.hymn.cion.fragment.Fragment_Dictionary;
import shalom.bible.hymn.cion.fragment.Fragment_NewBible;
import shalom.bible.hymn.cion.fragment.Fragment_NewHymn;
import shalom.bible.hymn.cion.fragment.Fragment_Note;
import shalom.bible.hymn.cion.fragment.Fragment_Note_Writer;
import shalom.bible.hymn.cion.fragment.Fragment_OldBible;
import shalom.bible.hymn.cion.fragment.Fragment_OldHymn;
import shalom.bible.hymn.cion.fragment.Fragment_Podcast_Main;
import shalom.bible.hymn.cion.fragment.Fragment_Prayer;
import shalom.bible.hymn.cion.util.NetworkHelper;
import shalom.bible.hymn.cion.util.NotificationUtil;
import shalom.bible.hymn.cion.util.PreferenceUtil;
import shalom.bible.hymn.cion.util.SimpleCrypto;
import shalom.bible.hymn.cion.util.TimeUtil;
import shalom.bible.hymn.cion.util.Utils;

public class MainFragmentActivity extends SherlockFragmentActivity implements android.view.View.OnClickListener, InterstitialAdListener, AdViewListener, CustomPopupListener {
	private ActionBar actionbar;
	private ViewPager viewpager;
	private Tab tab;
	private Context context;
	private Handler handler = new Handler();
	private boolean flag;
	private NativeExpressAdView admobNative;
	private final NetworkHelper mNetHelper = NetworkHelper.getInstance();
	private int MY_PERMISSION_REQUEST_STORAGE = 0;
	private int SDK_INT = android.os.Build.VERSION.SDK_INT;
	private TabContentAdapter adapter;
	private int current_page = 0;
	
	private final DBOpenHelper dbhelper = DBOpenHelper.getInstance();
	private VoicePlayAsync voicePlayAsync = null;
	private Handler navigator_handler = new Handler();
	private TextView max_time, current_time, txt_voice_title ;
	private ImageButton bt_duration_rew, bt_pause, bt_duration_ffwd;
	private int seekBackwardTime = 5000; // 5000 milliseconds
	private int seekForwardtime = 5000; // 5000 milliseconds
	private SeekBar mediacontroller_progress;
	public  MediaPlayer mediaPlayer;
	private LinearLayout layout_progress;
	private RelativeLayout voice_control_panel_layout;
	private ImageView bt_voice_continue, bt_voice_background;
	private com.admixer.InterstitialAd interstialAd;
	private RelativeLayout ad_layout;
	private boolean action_background = false;
	public boolean retry_alert = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		setContentView(R.layout.fragment_main);
		context = this;
		retry_alert = true;
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMIXER, "vuh3q0an");
    	AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB, "ca-app-pub-4637651494513698/7757784963");
    	AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB_FULL, "ca-app-pub-4637651494513698/1711251362");
    	ad_layout = (RelativeLayout)findViewById(R.id.ad_layout);
    	/*if(!PreferenceUtil.getStringSharedData(context, PreferenceUtil.PREF_ISSUBSCRIBED, Const.isSubscribed).equals("true")){
        	addBannerView();    		
    	}*/
//    	init_admob_naive();
        CustomPopup.setCustomPopupListener(this);
        CustomPopup.startCustomPopup(this, "vuh3q0an");
		init_voice_control();
		actionbar = getSupportActionBar();
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4bc1d2")));
		actionbar.setStackedBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		actionbar.setTitle(Html.fromHtml("<font color=\"#ffffff\"><b>" + context.getString(R.string.app_name_short) + "</b></font>"));
		actionbar.setDisplayShowHomeEnabled(false); // remove the icon
		/*actionbar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
		actionbar.setHomeButtonEnabled(false); // disable the button
		actionbar.setDisplayHomeAsUpEnabled(false); // remove the left caret*/
		
		/*viewpager = (CustomViewPager)findViewById(R.id.pager);*/
		viewpager = (CustomViewPager)findViewById(R.id.pager);//swipe모드 선택
		FragmentManager fm = getSupportFragmentManager();
		ViewPager.SimpleOnPageChangeListener ViewPagerListener = new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				super.onPageSelected(position);
				actionbar.setSelectedNavigationItem(position);
			}
		};
		viewpager.setOnPageChangeListener(ViewPagerListener);
		adapter = new TabContentAdapter(fm);
		viewpager.setAdapter(adapter);
		current_page = getIntent().getIntExtra("current_page", current_page);
		viewpager.postDelayed(new Runnable() {
			@Override
			public void run() {
				viewpager.setCurrentItem(current_page, true);
			}
		}, 100);
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				try {
					viewpager.setCurrentItem(tab.getPosition());
					if(tab.getPosition() == 0){
						ad_layout.setVisibility(View.GONE);
//						nativeContainer.setVisibility(View.GONE);
						voice_control_panel_visible();	
						getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
					}else if(tab.getPosition() == 1){
						ad_layout.setVisibility(View.GONE);
//						nativeContainer.setVisibility(View.GONE);
						voice_control_panel_visible();
						getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
					}else{
						if(PreferenceUtil.getStringSharedData(context, PreferenceUtil.PREF_ISSUBSCRIBED, Const.isSubscribed).equals("true")) {
							ad_layout.setVisibility(View.GONE);	
						}else {
							ad_layout.setVisibility(View.VISIBLE);
						}
						
//						nativeContainer.setVisibility(View.VISIBLE);
						/*int do_random_addInterstitialView = random_addInterstitialView();
						if(do_random_addInterstitialView == 1){
							addInterstitialView_Basic();	
						}else if(do_random_addInterstitialView == 5){
							addInterstitialView_Basic();
						}else if(do_random_addInterstitialView == 7){
							addInterstitialView_Basic();
						}*/
						voice_control_panel_gone();
						getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
					}
				}catch (NullPointerException e) {
				}
			}

			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				if(tab.getPosition() == 0){
					voice_play_stop();
					voice_control_panel_gone();
				}else if(tab.getPosition() == 1){
					voice_play_stop();
					voice_control_panel_gone();
				}
				datasetchanged();
			}
			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
			}
		};
		
		tab = actionbar.newTab().setText(context.getString(R.string.tab_menu_0)).setIcon(R.drawable.tab_bg_menu01).setTabListener(tabListener);
		actionbar.addTab(tab);
		
		tab = actionbar.newTab().setText(context.getString(R.string.tab_menu_1)).setIcon(R.drawable.tab_bg_menu02).setTabListener(tabListener);
		actionbar.addTab(tab);
		if(Utils.language(context).equals("ko_KR")){
			tab = actionbar.newTab().setText(context.getString(R.string.tab_menu_2)).setIcon(R.drawable.tab_bg_menu03).setTabListener(tabListener);
			actionbar.addTab(tab);
			
			tab = actionbar.newTab().setText(context.getString(R.string.tab_menu_3)).setIcon(R.drawable.tab_bg_menu04).setTabListener(tabListener);
			actionbar.addTab(tab);
			
			tab = actionbar.newTab().setText(context.getString(R.string.tab_menu_4)).setIcon(R.drawable.tab_bg_menu05).setTabListener(tabListener);
			actionbar.addTab(tab);
			
			tab = actionbar.newTab().setText(context.getString(R.string.tab_menu_5)).setIcon(R.drawable.tab_bg_menu06).setTabListener(tabListener);
			actionbar.addTab(tab);
			
			tab = actionbar.newTab().setText(context.getString(R.string.tab_menu_6)).setIcon(R.drawable.tab_bg_menu07).setTabListener(tabListener);
			actionbar.addTab(tab);
			
			tab = actionbar.newTab().setText(context.getString(R.string.tab_menu_7)).setIcon(R.drawable.tab_bg_menu08).setTabListener(tabListener);
			actionbar.addTab(tab);
			
			tab = actionbar.newTab().setText(context.getString(R.string.tab_menu_8)).setIcon(R.drawable.tab_bg_menu09).setTabListener(tabListener);
			actionbar.addTab(tab);
    	}else{
    		tab = actionbar.newTab().setText(context.getString(R.string.tab_menu_5)).setIcon(R.drawable.tab_bg_menu06).setTabListener(tabListener);
			actionbar.addTab(tab);
			
			tab = actionbar.newTab().setText(context.getString(R.string.tab_menu_6)).setIcon(R.drawable.tab_bg_menu07).setTabListener(tabListener);
			actionbar.addTab(tab);
			
    	}
		init_textsize();
		language_bibletype();
		telephony_manager();
		exit_handler();
		if(!PreferenceUtil.getStringSharedData(context, PreferenceUtil.PREF_ISSUBSCRIBED, Const.isSubscribed).equals("true")) {
			auto_service();			
		}else{
			auto_service_stop();
		}
	}
	
	private void auto_service() {
        Intent intent = new Intent(context, AutoServiceActivity.class);
        context.stopService(intent);
        context.startService(intent);
    }
	
	private void auto_service_stop() {
        Intent intent = new Intent(context, AutoServiceActivity.class);
        context.stopService(intent);
    }
	
	int random;
	private int random_addInterstitialView(){
		for(int i=0; i < 8; i++){
			random = (int)(Math.random() * 8);
		}
		return random;
	}
	
	RelativeLayout nativeContainer;
	private void init_admob_naive(){
		nativeContainer = (RelativeLayout) findViewById(R.id.admob_native);
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
        if(ad_layout != null){
        	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            ad_layout.addView(adView, params);	
        }
    }
	
	public void addInterstitialView_Basic() {
    	if(interstialAd != null)
			return;
		AdInfo adInfo = new AdInfo("vuh3q0an");
		adInfo.setInterstitialTimeout(0); // 초단위로 전면 광고 타임아웃 설정 (기본값 : 0, 0 이면 서버 지정 시간(20)으로 처리됨)
		adInfo.setUseRTBGPSInfo(false);
		adInfo.setMaxRetryCountInSlot(-1); // 리로드 시간 내에 전체 AdNetwork 반복 최대 횟수(-1 : 무한, 0 : 반복 없음, n : n번 반복)
		adInfo.setBackgroundAlpha(true); // 고수익 전면광고 노출 시 광고 외 영역 반투명처리 여부 (true: 반투명, false: 처리안함)

//		 이 주석을 제거하시면 고수익 전면광고가 팝업형으로 노출됩니다.
		// 팝업형 전면광고 세부설정을 원하시면 아래 PopupInterstitialAdOption 설정하세요
		PopupInterstitialAdOption adConfig = new PopupInterstitialAdOption();
		// 팝업형 전면광고 노출 상태에서 뒤로가기 버튼 방지 (true : 비활성화, false : 활성화)
		adConfig.setDisableBackKey(true);
		// 왼쪽버튼. 디폴트로 제공되며, 광고를 닫는 기능이 적용되는 버튼 (버튼문구, 버튼색상)
		adConfig.setButtonLeft(context.getString(R.string.txt_finish_no), "#234234");
		// 오른쪽 버튼을 사용하고자 하면 반드시 설정하세요. 앱을 종료하는 기능을 적용하는 버튼. 미설정 시 위 광고종료 버튼만 노출
		adConfig.setButtonRight(context.getString(R.string.txt_finish_yes), "#234234");
		// 버튼영역 색상지정
		adConfig.setButtonFrameColor(null);
		// 팝업형 전면광고 추가옵션 (com.admixer.AdInfo$InterstitialAdType.Basic : 일반전면, com.admixer.AdInfo$InterstitialAdType.Popup : 버튼이 있는 팝업형 전면)
		adInfo.setInterstitialAdType(AdInfo.InterstitialAdType.Basic, adConfig);
		
		interstialAd = new InterstitialAd(this);
		interstialAd.setAdInfo(adInfo, this);
		interstialAd.setInterstitialAdListener(this);
		interstialAd.startInterstitial();
    }
	
	public void addInterstitialView() {
    	if(interstialAd != null)
			return;
    	action_background = false;
		AdInfo adInfo = new AdInfo("vuh3q0an");
		adInfo.setInterstitialTimeout(0); // 초단위로 전면 광고 타임아웃 설정 (기본값 : 0, 0 이면 서버 지정 시간(20)으로 처리됨)
		adInfo.setUseRTBGPSInfo(false);
		adInfo.setMaxRetryCountInSlot(-1); // 리로드 시간 내에 전체 AdNetwork 반복 최대 횟수(-1 : 무한, 0 : 반복 없음, n : n번 반복)
		adInfo.setBackgroundAlpha(true); // 고수익 전면광고 노출 시 광고 외 영역 반투명처리 여부 (true: 반투명, false: 처리안함)

//		 이 주석을 제거하시면 고수익 전면광고가 팝업형으로 노출됩니다.
		// 팝업형 전면광고 세부설정을 원하시면 아래 PopupInterstitialAdOption 설정하세요
		PopupInterstitialAdOption adConfig = new PopupInterstitialAdOption();
		// 팝업형 전면광고 노출 상태에서 뒤로가기 버튼 방지 (true : 비활성화, false : 활성화)
		adConfig.setDisableBackKey(true);
		// 왼쪽버튼. 디폴트로 제공되며, 광고를 닫는 기능이 적용되는 버튼 (버튼문구, 버튼색상)
		adConfig.setButtonLeft(context.getString(R.string.txt_finish_no), "#234234");
		// 오른쪽 버튼을 사용하고자 하면 반드시 설정하세요. 앱을 종료하는 기능을 적용하는 버튼. 미설정 시 위 광고종료 버튼만 노출
		adConfig.setButtonRight(context.getString(R.string.txt_finish_yes), "#234234");
		// 버튼영역 색상지정
		adConfig.setButtonFrameColor(null);
		// 팝업형 전면광고 추가옵션 (com.admixer.AdInfo$InterstitialAdType.Basic : 일반전면, com.admixer.AdInfo$InterstitialAdType.Popup : 버튼이 있는 팝업형 전면)
		adInfo.setInterstitialAdType(AdInfo.InterstitialAdType.Popup, adConfig);
		
		interstialAd = new InterstitialAd(this);
		interstialAd.setAdInfo(adInfo, this);
		interstialAd.setInterstitialAdListener(this);
		interstialAd.startInterstitial();
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
	protected void onDestroy() {
		super.onDestroy();
		retry_alert = false;
		try{
			CustomPopup.stopCustomPopup();
			voice_play_stop();
			action_background =false;
			NotificationUtil.setNotification_Cancel();
//			admobNative.destroy();			
		}catch (NullPointerException e) {
		}
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		PreferenceUtil.setBooleanSharedData(context, PreferenceUtil.PREF_AD_VIEW, false);
	}

	@Override  
	public boolean onCreateOptionsMenu(Menu menu) { 
		menu.add(0, 5, 0, context.getString(R.string.txt_setting_alert10))
		.setIcon(R.drawable.ic_action_note)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(0, 1, 0, context.getString(R.string.txt_setting_alert6))
		.setIcon(R.drawable.ic_action_contact_us)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(0, 2, 0, context.getString(R.string.txt_setting_alert7))
		.setIcon(R.drawable.ic_action_ad)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(0, 3, 0, context.getString(R.string.txt_setting_alert8))
		.setIcon(R.drawable.ic_action_share)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(0, 4, 0, context.getString(R.string.txt_setting_alert9))
		.setIcon(R.drawable.ic_action_prayer)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(0, 0, 0, context.getString(R.string.txt_setting_alert5))
		.setIcon(R.drawable.ic_action_search)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(0, 6, 0, context.getString(R.string.txt_setting_alert11))
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		return true;  
	}  


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			voice_play_stop();
			intent_search();
			return true;
		case 1:
			voice_play_stop();
			intent_question_webview();
			return true;
		case 2:
			if(!PreferenceUtil.getStringSharedData(context, PreferenceUtil.PREF_ISSUBSCRIBED, Const.isSubscribed).equals("true")){
				action_background = false;
				addInterstitialView_Basic();
				Toast.makeText(context, context.getString(R.string.toast_ad), Toast.LENGTH_LONG).show();
				return true;		
			}else {
				return true;
			}
		case 3:
			intent_app_share();
			return true;
		case 4: //기도하기
			voice_play_stop();
			Intent intent = new Intent(this, shalom.bible.hymn.cion.favorite.MainActivity.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		startActivity(intent);
			return true;
		case 5: //노트작성
			voice_play_stop();
			intent = new Intent(this, Fragment_Note_Writer.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		startActivity(intent);
    		return true;
		case 6: //Swipe Mode
			alert_swipe_mode();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void init_textsize(){
		if(PreferenceUtil.checkTabletDeviceWithUserAgent(context) == true){//Tablet
			Const.default_textsize = 25; 
		}else{//Mobile
			Const.default_textsize = 15;
		}
	}
	
	private void language_bibletype(){
    	if(Utils.language(context).equals("ko_KR")){
    		Const.BIBLE_TYPE_1 = 0;	
    	}else{
    		Const.BIBLE_TYPE_1 = 2;
    	}
	}
	
	private void telephony_manager(){
		TelephonyManager telephonymanager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		telephonymanager.listen(new PhoneStateListener() {
			public void onCallStateChanged(int state, String incomingNumber) {
				switch (state) {
				case TelephonyManager.CALL_STATE_IDLE: 
				
				case TelephonyManager.CALL_STATE_OFFHOOK:
					if ((mediaPlayer != null) && (mediaPlayer.isPlaying())){
						mediaPlayer.pause();
						bt_pause.setImageResource(R.drawable.ic_action_play);
					}
				case TelephonyManager.CALL_STATE_RINGING:
					if ((mediaPlayer != null) && (mediaPlayer.isPlaying())){
						if(viewpager.getCurrentItem() == 0){
							dbhelper.bible_voice_db_pause_task(mediaPlayer.getCurrentPosition(), Const.bt_kwon_old.getText().toString(), Const.bt_jang_old.getText().toString());	
						}else if(viewpager.getCurrentItem() == 1){
							dbhelper.bible_voice_db_pause_task(mediaPlayer.getCurrentPosition(), Const.bt_kwon_new.getText().toString(), Const.bt_jang_new.getText().toString());
						}
						mediaPlayer.pause();
						bt_pause.setImageResource(R.drawable.ic_action_play);
					}
				default: break;
				} 
			}
		}, PhoneStateListener.LISTEN_CALL_STATE); 
	}
	
	@Override
	public void onClick(View view) {
		if(view == bt_duration_rew){
			if(mediaPlayer != null){
				int currentPosition = mediaPlayer.getCurrentPosition();
				// check if seekBackward time is greater than 0 sec
				if(currentPosition - seekBackwardTime >= 0){
					// forward song
					mediaPlayer.seekTo(currentPosition - seekBackwardTime); 
				}else{
					// backward to starting position
					mediaPlayer.seekTo(0);
				}
			}else{
				return;
			}
		}else if(view == bt_duration_ffwd){
			if(mediaPlayer != null){
				int currentPosition = mediaPlayer.getCurrentPosition();
				if(currentPosition + seekForwardtime <= mediaPlayer.getDuration()){
					// forward song
					mediaPlayer.seekTo(currentPosition + seekForwardtime);
				}else{
					// forward to end position
					mediaPlayer.seekTo(mediaPlayer.getDuration());
				}
			}else{
				return;
			}
		}else if(view == bt_pause){
			try{
				if(mediaPlayer != null && mediaPlayer.isPlaying() ){
					if(viewpager.getCurrentItem() == 0){
						dbhelper.bible_voice_db_pause_task(mediaPlayer.getCurrentPosition(), Const.bt_kwon_old.getText().toString(), Const.bt_jang_old.getText().toString());	
					}else if(viewpager.getCurrentItem() == 1){
						dbhelper.bible_voice_db_pause_task(mediaPlayer.getCurrentPosition(), Const.bt_kwon_new.getText().toString(), Const.bt_jang_new.getText().toString());
					}
					mediaPlayer.pause();
					bt_pause.setImageResource(R.drawable.ic_action_play);
				}else{
					voice_play_start();
				}
				}catch(Exception e){
			}
		}else if(view == bt_voice_continue){
			if(PreferenceUtil.getBooleanSharedData(context, PreferenceUtil.PREF_VOICE_CONTINUE, Const.voice_continue) == true){
				bt_voice_continue.setSelected(false);
				bt_voice_continue.setImageResource(R.drawable.ic_action_repeat_cancel);
				Const.voice_continue = false;
				PreferenceUtil.setBooleanSharedData(context, PreferenceUtil.PREF_VOICE_CONTINUE, Const.voice_continue);
			}else{
				bt_voice_continue.setSelected(true);
				bt_voice_continue.setImageResource(R.drawable.ic_action_repeat);
				Const.voice_continue = true;
				PreferenceUtil.setBooleanSharedData(context, PreferenceUtil.PREF_VOICE_CONTINUE, Const.voice_continue);
			}
		}else if(view == bt_voice_background){
			if(mediaPlayer != null && mediaPlayer.isPlaying() ){
				if(!PreferenceUtil.getStringSharedData(context, PreferenceUtil.PREF_ISSUBSCRIBED, Const.isSubscribed).equals("true")){
					action_background = true;
					Toast.makeText(context, context.getString(R.string.txt_background_voice_play), Toast.LENGTH_LONG).show();
					addInterstitialView_Basic();					
				}else {
					home_action();
				}
			}else{
				return;
			}
		}
	}
	
	private void home_action(){
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
//		NotificationUtil.setNotification_main(context);
	}
	
	public void intent_app_share(){
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");    
		intent.addCategory(Intent.CATEGORY_DEFAULT);
//		intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
		intent.putExtra(Intent.EXTRA_TEXT,  context.getString(R.string.app_name) + "\n" + context.getString(R.string.app_share_link));
//		intent.putExtra(Intent.EXTRA_TITLE, context.getString(R.string.app_name));
		startActivity(Intent.createChooser(intent, context.getString(R.string.intent_app_share)));
	}
	
	public void intent_question_webview(){
		Intent intent = new Intent(this, QuestionActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	public void intent_search(){
		Intent intent = new Intent(this, SearchActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	public void alert_select_text(){
		AlertDialog.Builder alert_dialog= new AlertDialog.Builder(context);
        alert_dialog.setTitle(context.getString(R.string.txt_setting_alert3));
        alert_dialog.setItems(Const.text_colorpicker_alert, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	if(which == 0){//성경1선택
            		alert_text_color_picker(true);
				}else if(which == 1){//성경2선택
					alert_text_color_picker2(true);
				}
            }
        });alert_dialog.show();
	}
	
	public void alert_text_color_picker(boolean supportsAlpha) {
		AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, PreferenceUtil.getIntSharedData(context, PreferenceUtil.PREF_BIBLE_TEXT_COLOR, Const.BIBLE_TEXT_COLOR), supportsAlpha, new AmbilWarnaDialog.OnAmbilWarnaListener() {
			@Override
			public void onOk(AmbilWarnaDialog dialog, int color) {
				Toast.makeText(context, context.getString(R.string.frg_toast_bible_text_color_1), Toast.LENGTH_SHORT).show();
				Const.BIBLE_TEXT_COLOR = color;
				PreferenceUtil.setIntSharedData(context, PreferenceUtil.PREF_BIBLE_TEXT_COLOR, Const.BIBLE_TEXT_COLOR);
				datasetchanged();//refresh
			}

			@Override
			public void onCancel(AmbilWarnaDialog dialog) {
				Toast.makeText(context, context.getString(R.string.frg_toast_bible_text_color_2), Toast.LENGTH_SHORT).show();
			}
		});
		dialog.show();
	}
	
	public void alert_text_color_picker2(boolean supportsAlpha) {
		AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, PreferenceUtil.getIntSharedData(context, PreferenceUtil.PREF_BIBLE_TEXT_COLOR2, Const.BIBLE_TEXT_COLOR2), supportsAlpha, new AmbilWarnaDialog.OnAmbilWarnaListener() {
			@Override
			public void onOk(AmbilWarnaDialog dialog, int color) {
				Toast.makeText(context, context.getString(R.string.frg_toast_bible_text_color_1), Toast.LENGTH_SHORT).show();
				Const.BIBLE_TEXT_COLOR2 = color;
				PreferenceUtil.setIntSharedData(context, PreferenceUtil.PREF_BIBLE_TEXT_COLOR2, Const.BIBLE_TEXT_COLOR2);
				datasetchanged();//refresh
			}

			@Override
			public void onCancel(AmbilWarnaDialog dialog) {
				Toast.makeText(context, context.getString(R.string.frg_toast_bible_text_color_2), Toast.LENGTH_SHORT).show();
			}
		});
		dialog.show();
	}

	
	public void alert_bg_color_picker(boolean supportsAlpha) {
		AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, PreferenceUtil.getIntSharedData(context, PreferenceUtil.PREF_BIBLE_BG_COLOR, Const.BIBLE_BG_COLOR), supportsAlpha, new AmbilWarnaDialog.OnAmbilWarnaListener() {
			@Override
			public void onOk(AmbilWarnaDialog dialog, int color) {
				Toast.makeText(context, context.getString(R.string.frg_toast_bible_bg_color_1), Toast.LENGTH_SHORT).show();
				Const.BIBLE_BG_COLOR = color;
				PreferenceUtil.setIntSharedData(context, PreferenceUtil.PREF_BIBLE_BG_COLOR, Const.BIBLE_BG_COLOR);
				datasetchanged();//refresh
			}

			@Override
			public void onCancel(AmbilWarnaDialog dialog) {
				Toast.makeText(context, context.getString(R.string.frg_toast_bible_bg_color_2), Toast.LENGTH_SHORT).show();
			}
		});
		dialog.show();
	}
	
	public void alert_audio_speed(){
		if(mediaPlayer != null && mediaPlayer.isPlaying() ){
			Toast.makeText(context, context.getString(R.string.txt_audiospeed_after), Toast.LENGTH_LONG).show();
			return;
		}
		String[] audiospeed_list = {
				context.getString(R.string.txt_audiospeed_list1),
				context.getString(R.string.txt_audiospeed_list2)};
		new AlertDialog.Builder(context)
		.setTitle(R.string.txt_setting_alert2)
		.setSingleChoiceItems(audiospeed_list, PreferenceUtil.getIntSharedData(context, PreferenceUtil.PREF_AUDIO_SPEED, Const.AUDIO_SPEED), new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which){
				dialog.dismiss();
				if(which == 0){
					Const.AUDIO_SPEED = 0;
				}else if(which == 1){
					Const.AUDIO_SPEED = 1;
				}
				PreferenceUtil.setIntSharedData(context, PreferenceUtil.PREF_AUDIO_SPEED, Const.AUDIO_SPEED);
			}
		}).show();	
	}
	
	public void alert_swipe_mode(){
		String[] swipe_mode_list = {
				context.getString(R.string.txt_swipemode_list1),
				context.getString(R.string.txt_swipemode_list2)};
		new AlertDialog.Builder(context)
		.setTitle(R.string.txt_setting_alert11)
//		.setMessage(R.string.alert_msg_swipe)
		.setSingleChoiceItems(swipe_mode_list, PreferenceUtil.getIntSharedData(context, PreferenceUtil.PREF_SWIPE_MODE, Const.SWIPE_MODE), new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which){
				dialog.dismiss();
				if(which == 0){
					Const.SWIPE_MODE = 0;
				}else if(which == 1){
					Const.SWIPE_MODE = 1;
				}
				PreferenceUtil.setIntSharedData(context, PreferenceUtil.PREF_SWIPE_MODE, Const.SWIPE_MODE);
				Toast.makeText(context, context.getString(R.string.toast_msg_swipe), Toast.LENGTH_LONG).show();
			}
		}).show();	
	}
	
	public void alert_scroll_speed(){
		String[] scroll_speed_list = {
				context.getString(R.string.txt_scroll_speed_1),
				context.getString(R.string.txt_scroll_speed_2),
				context.getString(R.string.txt_scroll_speed_3)};
		new AlertDialog.Builder(context)
		.setTitle(R.string.txt_setting_alert12)
		.setSingleChoiceItems(scroll_speed_list, PreferenceUtil.getIntSharedData(context, PreferenceUtil.PREF_SCROLL_SPEED, Const.SCROLL_SPEED), new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which){
				dialog.dismiss();
				if(which == 0){
					Const.SCROLL_SPEED = 0;
				}else if(which == 1){
					Const.SCROLL_SPEED = 1;
				}else if(which == 2){
					Const.SCROLL_SPEED = 2;
				}
				PreferenceUtil.setIntSharedData(context, PreferenceUtil.PREF_SCROLL_SPEED, Const.SCROLL_SPEED);
			}
		}).show();	
	}
	
	int bible_type = 1;
	public void alert_bible_type(int which){
		if(which == 0){
			bible_type = 1;
			String[] bible_type_1 = {
					   context.getString(R.string.txt_bible_list1),
					   context.getString(R.string.txt_bible_list2),
					   context.getString(R.string.txt_bible_list3),
					   context.getString(R.string.txt_bible_list4),
					   context.getString(R.string.txt_bible_list5),
					   context.getString(R.string.txt_bible_list6),
					   context.getString(R.string.txt_bible_list7),
					   context.getString(R.string.txt_bible_list8),
					   context.getString(R.string.txt_bible_list9),
					   context.getString(R.string.txt_bible_list10),
					   context.getString(R.string.txt_bible_list11),
					   
					   context.getString(R.string.txt_bible_list12),
					   context.getString(R.string.txt_bible_list13),
					   context.getString(R.string.txt_bible_list14),
					   context.getString(R.string.txt_bible_list15),
					   context.getString(R.string.txt_bible_list16),
					   context.getString(R.string.txt_bible_list17),
					   context.getString(R.string.txt_bible_list18),
					   context.getString(R.string.txt_bible_list19),
					   context.getString(R.string.txt_bible_list20),
					   context.getString(R.string.txt_bible_list21),
					   context.getString(R.string.txt_bible_list22),
					   context.getString(R.string.txt_bible_list23),
					   context.getString(R.string.txt_bible_list24),
					   context.getString(R.string.txt_bible_list25),
					   context.getString(R.string.txt_bible_list26),
					   context.getString(R.string.txt_bible_list27),
					   context.getString(R.string.txt_bible_list28),
					   context.getString(R.string.txt_bible_list29),
					   context.getString(R.string.txt_bible_list30),
					   context.getString(R.string.txt_bible_list31)};
			new AlertDialog.Builder(context)
			.setTitle(R.string.txt_setting_alert0)
			.setSingleChoiceItems(bible_type_1, PreferenceUtil.getIntSharedData(context, PreferenceUtil.PREF_BIBLE_TYPE_1, Const.BIBLE_TYPE_1), new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(which == 0){
						Const.BIBLE_TYPE_1 = 0;
						PreferenceUtil.setIntSharedData(context, PreferenceUtil.PREF_BIBLE_TYPE_1, Const.BIBLE_TYPE_1);
						datasetchanged();
					}else if(which == 1){
						Const.BIBLE_TYPE_1 = 1;
						PreferenceUtil.setIntSharedData(context, PreferenceUtil.PREF_BIBLE_TYPE_1, Const.BIBLE_TYPE_1);
						datasetchanged();
					}else if(which == 2){
						Const.BIBLE_TYPE_1 = 2;
						PreferenceUtil.setIntSharedData(context, PreferenceUtil.PREF_BIBLE_TYPE_1, Const.BIBLE_TYPE_1);
						datasetchanged();
					}else if(which == 3){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(3);
						}else{
							bible_download(3);	
						}
					}else if(which == 4){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(4);
						}else{
							bible_download(4);	
						}
					}else if(which == 5){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(5);
						}else{
							bible_download(5);	
						};
					}else if(which == 6){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(6);
						}else{
							bible_download(6);	
						};
					}else if(which == 7){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(7);
						}else{
							bible_download(7);	
						};
						
					}else if(which == 8){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(8);
						}else{
							bible_download(8);	
						};
						
					}else if(which == 9){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(9);
						}else{
							bible_download(9);	
						};
						
					}else if(which == 10){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(10);
						}else{
							bible_download(10);	
						};
					}else if(which == 11){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(11);
						}else{
							bible_download(11);	
						};
					}else if(which == 12){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(12);
						}else{
							bible_download(12);	
						};
					}else if(which == 13){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(13);
						}else{
							bible_download(13);	
						};
					}else if(which == 14){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(14);
						}else{
							bible_download(14);	
						};
					}else if(which == 15){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(15);
						}else{
							bible_download(15);	
						};
					}else if(which == 16){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(16);
						}else{
							bible_download(16);	
						};
					}else if(which == 17){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(17);
						}else{
							bible_download(17);	
						};
					}else if(which == 18){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(18);
						}else{
							bible_download(18);	
						};
					}else if(which == 19){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(19);
						}else{
							bible_download(19);	
						};
					}else if(which == 20){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(20);
						}else{
							bible_download(20);	
						};
					}else if(which == 21){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(21);
						}else{
							bible_download(21);	
						};
					}else if(which == 22){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(22);
						}else{
							bible_download(22);	
						};
					}else if(which == 23){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(23);
						}else{
							bible_download(23);	
						};
					}else if(which == 24){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(24);
						}else{
							bible_download(24);	
						};
					}else if(which == 25){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(25);
						}else{
							bible_download(25);	
						};
					}else if(which == 26){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(26);
						}else{
							bible_download(26);	
						};
					}else if(which == 27){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(27);
						}else{
							bible_download(27);	
						};
					}else if(which == 28){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(28);
						}else{
							bible_download(28);	
						};
					}else if(which == 29){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(29);
						}else{
							bible_download(29);	
						};
					}else if(which == 30){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(30);
						}else{
							bible_download(30);	
						};
					}
					dialog.dismiss();
				}
			}).show();
		}else if(which == 1){
			bible_type = 2;
			String[] bible_type_2 = {
					   context.getString(R.string.txt_bible_list_none),
					   context.getString(R.string.txt_bible_list3),
					   context.getString(R.string.txt_bible_list1),
					   context.getString(R.string.txt_bible_list2),
					   context.getString(R.string.txt_bible_list4),
					   context.getString(R.string.txt_bible_list5),
					   context.getString(R.string.txt_bible_list6),
					   context.getString(R.string.txt_bible_list7),
					   context.getString(R.string.txt_bible_list8),
					   context.getString(R.string.txt_bible_list9),
					   context.getString(R.string.txt_bible_list10),
					   context.getString(R.string.txt_bible_list11),
					   context.getString(R.string.txt_bible_list12),
					   context.getString(R.string.txt_bible_list13),
					   context.getString(R.string.txt_bible_list14),
					   context.getString(R.string.txt_bible_list15),
					   context.getString(R.string.txt_bible_list16),
					   context.getString(R.string.txt_bible_list17),
					   context.getString(R.string.txt_bible_list18),
					   context.getString(R.string.txt_bible_list19),
					   context.getString(R.string.txt_bible_list20),
					   context.getString(R.string.txt_bible_list21),
					   context.getString(R.string.txt_bible_list22),
					   context.getString(R.string.txt_bible_list23),
					   context.getString(R.string.txt_bible_list24),
					   context.getString(R.string.txt_bible_list25),
					   context.getString(R.string.txt_bible_list26),
					   context.getString(R.string.txt_bible_list27),
					   context.getString(R.string.txt_bible_list28),
					   context.getString(R.string.txt_bible_list29),
					   context.getString(R.string.txt_bible_list30),
					   context.getString(R.string.txt_bible_list31)
			};
			new AlertDialog.Builder(context)
			.setTitle(R.string.txt_setting_alert1)
			.setSingleChoiceItems(bible_type_2, PreferenceUtil.getIntSharedData(context, PreferenceUtil.PREF_BIBLE_TYPE_2, Const.BIBLE_TYPE_2), new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(which == 0){
						Const.BIBLE_TYPE_2 = 0;
						PreferenceUtil.setIntSharedData(context, PreferenceUtil.PREF_BIBLE_TYPE_2, Const.BIBLE_TYPE_2);
						datasetchanged();
					}else if(which == 1){
						Const.BIBLE_TYPE_2 = 1;
						PreferenceUtil.setIntSharedData(context, PreferenceUtil.PREF_BIBLE_TYPE_2, Const.BIBLE_TYPE_2);
						datasetchanged();
					}else if(which == 2){
						Const.BIBLE_TYPE_2 = 2;
						PreferenceUtil.setIntSharedData(context, PreferenceUtil.PREF_BIBLE_TYPE_2, Const.BIBLE_TYPE_2);
						datasetchanged();
					}else if(which == 3){
						Const.BIBLE_TYPE_2 = 3;
						PreferenceUtil.setIntSharedData(context, PreferenceUtil.PREF_BIBLE_TYPE_2, Const.BIBLE_TYPE_2);
						datasetchanged();
					}else if(which == 4){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(3);
						}else{
							bible_download(3);	
						}
					}else if(which == 5){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(4);
						}else{
							bible_download(4);	
						};
					}else if(which == 6){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(5);
						}else{
							bible_download(5);	
						};
					}else if(which == 7){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(6);
						}else{
							bible_download(6);	
						};
						
					}else if(which == 8){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(7);
						}else{
							bible_download(7);	
						};
						
					}else if(which == 9){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(8);
						}else{
							bible_download(8);	
						};
						
					}else if(which == 10){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(9);
						}else{
							bible_download(9);	
						};
					}else if(which == 11){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(10);
						}else{
							bible_download(10);	
						};
					}else if(which == 12){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(11);
						}else{
							bible_download(11);	
						};
					}else if(which == 13){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(12);
						}else{
							bible_download(12);	
						};
					}else if(which == 14){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(13);
						}else{
							bible_download(13);	
						};
					}else if(which == 15){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(14);
						}else{
							bible_download(14);	
						};
					}else if(which == 16){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(15);
						}else{
							bible_download(15);	
						};
					}else if(which == 17){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(16);
						}else{
							bible_download(16);	
						};
					}else if(which == 18){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(17);
						}else{
							bible_download(17);	
						};
					}else if(which == 19){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(18);
						}else{
							bible_download(18);	
						};
					}else if(which == 20){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(19);
						}else{
							bible_download(19);	
						};
					}else if(which == 21){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(20);
						}else{
							bible_download(20);	
						};
					}else if(which == 22){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(21);
						}else{
							bible_download(21);	
						};
					}else if(which == 23){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(22);
						}else{
							bible_download(22);	
						};
					}else if(which == 24){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(23);
						}else{
							bible_download(23);	
						};
					}else if(which == 25){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(24);
						}else{
							bible_download(24);	
						};
					}else if(which == 26){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(25);
						}else{
							bible_download(25);	
						};
					}else if(which == 27){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(26);
						}else{
							bible_download(26);	
						};
					}else if(which == 28){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(27);
						}else{
							bible_download(27);	
						};
					}else if(which == 29){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(28);
						}else{
							bible_download(28);	
						};
					}else if(which == 30){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(29);
						}else{
							bible_download(29);	
						};
					}else if(which == 31){
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(30);
						}else{
							bible_download(30);	
						};
					}
					dialog.dismiss();
				}
			}).show();
			
		}
	}
	
	int which;
	private void checkPermission(int which) {
		this.which = which;
	    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
	            != PackageManager.PERMISSION_GRANTED
	            || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
	            != PackageManager.PERMISSION_GRANTED) {
	        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
	        	Toast.makeText(context, context.getString(R.string.permission_ment), Toast.LENGTH_LONG).show();
	        }
	        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
	                MY_PERMISSION_REQUEST_STORAGE);
	    } else {
	        // 다음 부분은 항상 허용일 경우에 해당이 됩니다.
	    	bible_download(which);
	    }
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
	    switch (requestCode) {
	        case 0:
	            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
	                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
	            	bible_download(this.which);
	            } else {
	            	Toast.makeText(context, context.getString(R.string.permission_cancel), Toast.LENGTH_LONG).show();
	            }
	            break;
	    }
	}
	
	private void bible_download(int which){
		String file_name = null;
		String dir_name = null;
		String get_data;
		String url_path = null;
		String input_db_path = null;
		try{
//			Log.i("dsu", "is3GConnected : " + mNetHelper.is3GConnected() + "\nisWIFIConneced : " + mNetHelper.isWIFIConneced());
			if(!mNetHelper.is3GConnected() && !mNetHelper.isWIFIConneced()){
				Toast.makeText(context, context.getString(R.string.download_data_connection_ment), Toast.LENGTH_LONG).show();
				return;
			}
			if(which == 3){//jpnnew.db
				file_name = context.getString(R.string.txt_input_jpnnew);
				dir_name = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ context.getString(R.string.path_folder);
				get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.download_db_url));
				url_path = get_data + context.getString(R.string.txt_input_jpnnew);
				input_db_path = context.getString(R.string.txt_jpnnew_path);
			}else if(which == 4){//ckb.db
				file_name = context.getString(R.string.txt_input_ckb);
				dir_name = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ context.getString(R.string.path_folder);
				get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.download_db_url));
				url_path = get_data + context.getString(R.string.txt_input_ckb);
				input_db_path = context.getString(R.string.txt_ckb_path);
			}else if(which == 5){//frenchdarby.db
				file_name = context.getString(R.string.txt_input_frenchdarby);
				dir_name = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ context.getString(R.string.path_folder);
				get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.download_db_url));
				url_path = get_data + context.getString(R.string.txt_input_frenchdarby);
				input_db_path = context.getString(R.string.txt_frenchdarby_path);
			}else if(which == 6){//germanluther.db
				file_name = context.getString(R.string.txt_input_germanluther);
				dir_name = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ context.getString(R.string.path_folder);
				get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.download_db_url));
				url_path = get_data + context.getString(R.string.txt_input_germanluther);
				input_db_path = context.getString(R.string.txt_germanluther_path);
			}else if(which == 7){//gst.db
				file_name = context.getString(R.string.txt_input_gst);
				dir_name = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ context.getString(R.string.path_folder);
				get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.download_db_url));
				url_path = get_data + context.getString(R.string.txt_input_gst);
				input_db_path = context.getString(R.string.txt_gst_path);
			}else if(which == 8){//indonesianbaru.db
				file_name = context.getString(R.string.txt_input_indonesianbaru);
				dir_name = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ context.getString(R.string.path_folder);
				get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.download_db_url));
				url_path = get_data + context.getString(R.string.txt_input_indonesianbaru);
				input_db_path = context.getString(R.string.txt_indonesianbaru_path);
			}else if(which == 9){//portugal.db
				file_name = context.getString(R.string.txt_input_portugal);
				dir_name = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ context.getString(R.string.path_folder);
				get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.download_db_url));
				url_path = get_data + context.getString(R.string.txt_input_portugal);
				input_db_path = context.getString(R.string.txt_portugal_path);
			}else if(which == 10){//russiansynodal.db
				file_name = context.getString(R.string.txt_input_russiansynodal);
				dir_name = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ context.getString(R.string.path_folder);
				get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.download_db_url));
				url_path = get_data + context.getString(R.string.txt_input_russiansynodal);
				input_db_path = context.getString(R.string.txt_russiansynodal_path);
			}else if(which == 11){
				file_name = context.getString(R.string.txt_input_alb);
				dir_name = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ context.getString(R.string.path_folder);
				get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.download_db_url));
				url_path = get_data + context.getString(R.string.txt_input_alb);
				input_db_path = context.getString(R.string.txt_alb_path);
			}else if(which == 12){
				file_name = context.getString(R.string.txt_input_asv);
				dir_name = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ context.getString(R.string.path_folder);
				get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.download_db_url));
				url_path = get_data + context.getString(R.string.txt_input_asv);
				input_db_path = context.getString(R.string.txt_asv_path);
			}else if(which == 13){
				file_name = context.getString(R.string.txt_input_avs);
				dir_name = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ context.getString(R.string.path_folder);
				get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.download_db_url));
				url_path = get_data + context.getString(R.string.txt_input_avs);
				input_db_path = context.getString(R.string.txt_avs_path);
			}else if(which == 14){
				file_name = context.getString(R.string.txt_input_barun);
				dir_name = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ context.getString(R.string.path_folder);
				get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.download_db_url));
				url_path = get_data + context.getString(R.string.txt_input_barun);
				input_db_path = context.getString(R.string.txt_barun_path);
			}else if(which == 15){
				file_name = context.getString(R.string.txt_input_chb);
				dir_name = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ context.getString(R.string.path_folder);
				get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.download_db_url));
				url_path = get_data + context.getString(R.string.txt_input_chb);
				input_db_path = context.getString(R.string.txt_chb_path);
			}else if(which == 15){
				file_name = context.getString(R.string.txt_input_chb);
				dir_name = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ context.getString(R.string.path_folder);
				get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.download_db_url));
				url_path = get_data + context.getString(R.string.txt_input_chb);
				input_db_path = context.getString(R.string.txt_chb_path);
			}else if(which == 16){
				file_name = context.getString(R.string.txt_input_chg);
				dir_name = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ context.getString(R.string.path_folder);
				get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.download_db_url));
				url_path = get_data + context.getString(R.string.txt_input_chg);
				input_db_path = context.getString(R.string.txt_chg_path);
			}else if(which == 17){
				file_name = context.getString(R.string.txt_input_cjb);
				dir_name = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ context.getString(R.string.path_folder);
				get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.download_db_url));
				url_path = get_data + context.getString(R.string.txt_input_cjb);
				input_db_path = context.getString(R.string.txt_cjb_path);
			}else if(which == 18){
				file_name = context.getString(R.string.txt_input_ckc);
				dir_name = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ context.getString(R.string.path_folder);
				get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.download_db_url));
				url_path = get_data + context.getString(R.string.txt_input_ckc);
				input_db_path = context.getString(R.string.txt_ckc_path);
			}else if(which == 19){
				file_name = context.getString(R.string.txt_input_ckg);
				dir_name = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ context.getString(R.string.path_folder);
				get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.download_db_url));
				url_path = get_data + context.getString(R.string.txt_input_ckg);
				input_db_path = context.getString(R.string.txt_ckg_path);
			}else if(which == 20){
				file_name = context.getString(R.string.txt_input_cks);
				dir_name = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ context.getString(R.string.path_folder);
				get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.download_db_url));
				url_path = get_data + context.getString(R.string.txt_input_cks);
				input_db_path = context.getString(R.string.txt_cks_path);
			}else if(which == 21){
				file_name = context.getString(R.string.txt_input_hebbhs);
				dir_name = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ context.getString(R.string.path_folder);
				get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.download_db_url));
				url_path = get_data + context.getString(R.string.txt_input_hebbhs);
				input_db_path = context.getString(R.string.txt_hebbhs_path);
			}else if(which == 22){
				file_name = context.getString(R.string.txt_input_hebmod);
				dir_name = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ context.getString(R.string.path_folder);
				get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.download_db_url));
				url_path = get_data + context.getString(R.string.txt_input_hebmod);
				input_db_path = context.getString(R.string.txt_hebmod_path);
			}else if(which == 23){
				file_name = context.getString(R.string.txt_input_hebwlc);
				dir_name = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ context.getString(R.string.path_folder);
				get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.download_db_url));
				url_path = get_data + context.getString(R.string.txt_input_hebwlc);
				input_db_path = context.getString(R.string.txt_hebwlc_path);
			}else if(which == 24){
				file_name = context.getString(R.string.txt_input_indianhindi);
				dir_name = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ context.getString(R.string.path_folder);
				get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.download_db_url));
				url_path = get_data + context.getString(R.string.txt_input_indianhindi);
				input_db_path = context.getString(R.string.txt_indianhindi_path);
			}else if(which == 25){
				file_name = context.getString(R.string.txt_input_indiantamil);
				dir_name = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ context.getString(R.string.path_folder);
				get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.download_db_url));
				url_path = get_data + context.getString(R.string.txt_input_indiantamil);
				input_db_path = context.getString(R.string.txt_indiantamil_path);
			}else if(which == 26){
				file_name = context.getString(R.string.txt_input_jpnold);
				dir_name = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ context.getString(R.string.path_folder);
				get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.download_db_url));
				url_path = get_data + context.getString(R.string.txt_input_jpnold);
				input_db_path = context.getString(R.string.txt_jpnold_path);
			}else if(which == 27){
				file_name = context.getString(R.string.txt_input_reina);
				dir_name = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ context.getString(R.string.path_folder);
				get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.download_db_url));
				url_path = get_data + context.getString(R.string.txt_input_reina);
				input_db_path = context.getString(R.string.txt_reina_path);
			}else if(which == 28){
				file_name = context.getString(R.string.txt_input_tagalog);
				dir_name = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ context.getString(R.string.path_folder);
				get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.download_db_url));
				url_path = get_data + context.getString(R.string.txt_input_tagalog);
				input_db_path = context.getString(R.string.txt_tagalog_path);
			}else if(which == 29){
				file_name = context.getString(R.string.txt_input_tkh);
				dir_name = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ context.getString(R.string.path_folder);
				get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.download_db_url));
				url_path = get_data + context.getString(R.string.txt_input_tkh);
				input_db_path = context.getString(R.string.txt_tkh_path);
			}else if(which == 30){
				file_name = context.getString(R.string.txt_input_web);
				dir_name = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ context.getString(R.string.path_folder);
				get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.download_db_url));
				url_path = get_data + context.getString(R.string.txt_input_web);
				input_db_path = context.getString(R.string.txt_web_path);
			}
			if(bible_type == 1){
				downloadDBAsync = new DownloadDBAsync(context, file_name, url_path, dir_name, PreferenceUtil.PREF_BIBLE_TYPE_1, which, input_db_path);
				downloadDBAsync.execute();	
			}else if(bible_type == 2){
				downloadDBAsync = new DownloadDBAsync(context, file_name, url_path, dir_name, PreferenceUtil.PREF_BIBLE_TYPE_2, which+1, input_db_path);
				downloadDBAsync.execute();	
			}
		}catch (Exception e) {
		}
	}
	
	public class DownloadDBAsync extends AsyncTask<String, Integer, String>{
		String file_name, url_path, dir_name, bible_type, input_db_path;
		int select_bible;
		Context context;
		HttpURLConnection urlConnection;
		String Response = "fail";
		public DownloadDBAsync(Context context,String file_name, String url_path, String dir_name, String bible_type, int select_bible, String input_db_path){
			this.context = context;
			this.dir_name = dir_name;
			this.file_name = file_name;
			this.url_path = url_path;
			this.bible_type = bible_type;
			this.select_bible = select_bible;
			this.input_db_path = input_db_path;
		}
		
		@Override
		protected String doInBackground(String...params) {
			File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
					+ context.getString(R.string.path_folder));
	        file.mkdirs();
	        try{
	        	long total = 0;
	        	int max = 100;
//	        	Log.i("dsu", "url_path : " + url_path + "\n" + "dir_filename : " + dir_name + file_name);
	        	URL url = new URL(url_path);
	        	HttpURLConnection localHttpURLConnection = (HttpURLConnection)url.openConnection();
	        	localHttpURLConnection.setConnectTimeout(10000);
	        	localHttpURLConnection.setReadTimeout(10000);
	        	localHttpURLConnection.setUseCaches(false);
	        	localHttpURLConnection.connect();
	        	int lenghtOfFile = localHttpURLConnection.getContentLength();
	        	BufferedInputStream localBufferedInputStream = new BufferedInputStream(url.openStream());
	        	FileOutputStream output = new FileOutputStream(dir_name + file_name);
	        	byte data[] = new byte[1024];
	        	while (true){
	        		int count = localBufferedInputStream.read(data);
	        		if (count == -1){
	        			output.flush();
	        			output.close();
	        			localBufferedInputStream.close();
	        			total += count;
		        		publishProgress(max, (int)((total*100)/lenghtOfFile));
	        			return Response = "success";
	        		}
	        		/*total += count;
	        		int percent = (int)(100 * total / lenghtOfFile);
	        		if (max < percent){
	        			Integer[] arrayOfInteger = new Integer[1];
	        			arrayOfInteger[0] = Integer.valueOf(percent);
	        			publishProgress(arrayOfInteger);
	        		}
	        		max = percent;*/
	        		output.write(data, 0, count);
	        	}
	        }catch (FileNotFoundException localFileNotFoundException){
//	        	Log.e("dsu", "FileNotFoundException : " + localFileNotFoundException.getMessage());
	        	Response = "fail";
	        	File delete_file = new File(dir_name + file_name); //경로를 SD카드로 잡은거고 그 안에 있는 A폴더 입니다. 입맛에 따라 바꾸세요.
//				Log.i("dsu", "file_check : " + file.exists());
				if(file.exists() == true){
				delete_file.delete();	
				}
	        	/*while (true)
	        		localFileNotFoundException.printStackTrace();*/
	        }catch (IOException localIOException){
//	        	Log.e("dsu", "localIOException : " + localIOException.getMessage());
	        	Response = "fail";
	        	File delete_file = new File(dir_name + file_name); //경로를 SD카드로 잡은거고 그 안에 있는 A폴더 입니다. 입맛에 따라 바꾸세요.
//				Log.i("dsu", "file_check : " + file.exists());
				if(file.exists() == true){
				delete_file.delete();	
				}
	        	/*while (true)
	                localIOException.printStackTrace();*/
	        } finally{
            	File fileCacheItem = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
      					+ dir_name + file_name);
    			sendBroadcast(new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(fileCacheItem)) );
            }
			return Response;
		}
		
		@SuppressWarnings("deprecation")
		@Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            mProgressDialog.setProgressDrawable(context.getResources().getDrawable(R.drawable.custom_progressbar));
            mProgressDialog.setButton(context.getString(R.string.download_cancel_ment), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					if(urlConnection != null){
						urlConnection.disconnect();
					}
					if(downloadDBAsync != null &&(downloadDBAsync.getStatus() == AsyncTask.Status.PENDING ||
							downloadDBAsync.getStatus() == AsyncTask.Status.RUNNING)){
						downloadDBAsync.cancel(true);
						downloadDBAsync = null;
					}
				}
			});
            mProgressDialog.setMessage(context.getString(R.string.downloading_ment));
            mProgressDialog.setCancelable(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.show();
        }
		@Override
		protected void onPostExecute(String Response) {
			super.onPostExecute(Response);
			 if(mProgressDialog != null){
				 mProgressDialog.dismiss();
			 }
			 if(Response.equals("success")){
				 String file_check = dir_name + file_name;
				 File file = new File(file_check);
				 if(file.exists() == true){
					 Toast.makeText(context, context.getString(R.string.downloading_complete), Toast.LENGTH_SHORT).show();
					 AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(context);
					 alert_internet_status.setCancelable(true);
					 alert_internet_status.setMessage(context.getString(R.string.downloading_db_apply_ment));
					 alert_internet_status.setPositiveButton(context.getString(R.string.downloading_db_apply), new DialogInterface.OnClickListener() {
						 @Override
						 public void onClick(DialogInterface dialog, int which) {
							 File file = new File(dir_name + file_name);
							 boolean result;
						        if(file!=null&&file.exists()){
						            try {
						                FileInputStream fis = new FileInputStream(file);
						                FileOutputStream newfos = new FileOutputStream(input_db_path);
						                int readcount=0;
						                byte[] buffer = new byte[1024];
						                while((readcount = fis.read(buffer,0,1024))!= -1){
						                    newfos.write(buffer,0,readcount);
						                }
						                newfos.close();
						                fis.close();
						            } catch (Exception e) {
						                e.printStackTrace();
						            } finally{
						            	File fileCacheItem = new File(input_db_path);
						    			sendBroadcast(new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(fileCacheItem)) );
						            }
						            result = true;
						        }else{
						            result = false;
						        }
						        if(result == true){
						        	Toast.makeText(context, context.getString(R.string.download_db_apply_complete), Toast.LENGTH_SHORT).show();
						        	if(MainFragmentActivity.this.bible_type == 1){
						        		PreferenceUtil.setIntSharedData(context, PreferenceUtil.PREF_BIBLE_TYPE_1, select_bible);	
						        	}else if(MainFragmentActivity.this.bible_type == 2){
						        		PreferenceUtil.setIntSharedData(context, PreferenceUtil.PREF_BIBLE_TYPE_2, select_bible);
						        	}
									datasetchanged();//refresh
						        }else{
						        	Toast.makeText(context, context.getString(R.string.download_db_write_fail), Toast.LENGTH_SHORT).show();
						        }
						 }
					 });
					 alert_internet_status.setNegativeButton(context.getString(R.string.downloading_db_cancle), new DialogInterface.OnClickListener() {
						 @Override
						 public void onClick(DialogInterface dialog, int which) {
							 dialog.dismiss();
						 }
					 });
				         alert_internet_status.show();				 
				 }else{
				 }
			 }
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			 mProgressDialog.setProgress(values[0]);
		}
	}
	
	public class TabContentAdapter extends FragmentPagerAdapter {
		private int PAGE_COUNT;
		public TabContentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if(Utils.language(context).equals("ko_KR")){
				if(position == 0){
					Fragment_OldBible fragment_old_bible = new Fragment_OldBible();
					return fragment_old_bible;
				}else if(position == 1){
					Fragment_NewBible fragment_new_bible = new Fragment_NewBible();
					return fragment_new_bible;
				}else if(position == 2){
					Fragment_OldHymn fragment_old_hymn = new Fragment_OldHymn();
					return fragment_old_hymn;
				}else if(position == 3){
					Fragment_NewHymn fragment_new_hymn = new Fragment_NewHymn();
					return fragment_new_hymn;
				}else if(position == 4){
					Fragment_Dictionary fragment_dictionary = new Fragment_Dictionary();
					return fragment_dictionary;
				}else if(position == 5){
					Fragment_Note fragment_note = new Fragment_Note();
					return fragment_note;
				}else if(position == 6){
					Fragment_Prayer fragment_prayer = new Fragment_Prayer();
					return fragment_prayer;
				}else if(position == 7){
					Fragment_Podcast_Main fragment_podcast_main = new Fragment_Podcast_Main();
					return fragment_podcast_main;
				}else if(position == 8){
					Fragment_CCM fragment_ccm = new Fragment_CCM();
					return fragment_ccm;
				}
	    	}else{
	    		if(position == 0){
					Fragment_OldBible fragment_old_bible = new Fragment_OldBible();
					return fragment_old_bible;
				}else if(position == 1){
					Fragment_NewBible fragment_new_bible = new Fragment_NewBible();
					return fragment_new_bible;
				}else if(position == 2){
					Fragment_Note fragment_note = new Fragment_Note();
					return fragment_note;
				}else if(position == 3){
					Fragment_Prayer fragment_prayer = new Fragment_Prayer();
					return fragment_prayer;
				}
	    	}
			return null;
		}
		
		@Override
		public int getCount() {
			if(Utils.language(context).equals("ko_KR")){
				PAGE_COUNT = 9;	
	    	}else{
	    		PAGE_COUNT = 4;
	    	}
			return PAGE_COUNT;
		}
		
		@Override
		public int getItemPosition(Object object) {
			if(object instanceof Fragment_OldBible) {
				((Fragment_OldBible) object).display_list();
		    }else if(object instanceof Fragment_NewBible) {
				((Fragment_NewBible) object).display_list();
		    }else if(object instanceof Fragment_Note) {
		    	((Fragment_Note) object).display_list();
		    }
			return super.getItemPosition(object);
		}
	}
	
	private void datasetchanged(){
		adapter.notifyDataSetChanged();
	}
	
	/*private void init_admob_native(){
		RelativeLayout nativeContainer = (RelativeLayout) findViewById(R.id.admob_native);
		AdRequest adRequest = new AdRequest.Builder().build();	    
		admobNative = new NativeExpressAdView(this);
		admobNative.setAdSize(new AdSize(360, 100));
		admobNative.setAdUnitId("ca-app-pub-4637651494513698/9851276161");
		nativeContainer.addView(admobNative);
		admobNative.loadAd(adRequest);
	}*/
	
	private void exit_handler(){
    	handler = new Handler(){
    		@Override
    		public void handleMessage(Message msg) {
    			if(msg.what == 0){
    				flag = false;
    			}
    		}
    	};
    }
	
	/*************
	VoicePlayAsync
	*************/
	
	public void init_voice_control(){
		voice_control_panel_layout = (RelativeLayout)findViewById(R.id.voice_control_panel_layout);
    	layout_progress = (LinearLayout)findViewById(R.id.layout_progress);
    	mediacontroller_progress = (SeekBar)findViewById(R.id.mediacontroller_progress);
    	max_time = (TextView)findViewById(R.id.max_time);
    	current_time = (TextView)findViewById(R.id.current_time);
    	txt_voice_title = (TextView)findViewById(R.id.txt_voice_title);
    	bt_duration_rew = (ImageButton)findViewById(R.id.bt_duration_rew); 
    	bt_pause = (ImageButton)findViewById(R.id.bt_pause); 
    	bt_duration_ffwd = (ImageButton)findViewById(R.id.bt_duration_ffwd);
    	bt_voice_continue = (ImageView)findViewById(R.id.bt_voice_continue);
    	if(PreferenceUtil.getBooleanSharedData(context, PreferenceUtil.PREF_VOICE_CONTINUE, Const.voice_continue) == true){
			bt_voice_continue.setSelected(true);
			bt_voice_continue.setImageResource(R.drawable.ic_action_repeat);
		}else{
			bt_voice_continue.setSelected(false);
			bt_voice_continue.setImageResource(R.drawable.ic_action_repeat_cancel);
		}
    	bt_voice_background = (ImageView)findViewById(R.id.bt_voice_background);
    	bt_duration_rew.setOnClickListener(this);
    	bt_pause.setOnClickListener(this);
    	bt_duration_ffwd.setOnClickListener(this);
    	bt_voice_continue.setOnClickListener(this);
    	bt_voice_background.setOnClickListener(this);
	}
	
	private void voice_control_panel_gone(){
		voice_control_panel_layout.setVisibility(View.GONE);
	}
	
	private void voice_control_panel_visible(){
		try{
        	String language = context.getResources().getConfiguration().locale.toString();
        	if(language.equals("ko_KR")){
        		voice_control_panel_layout.setVisibility(View.VISIBLE);
        	}else{
        		voice_control_panel_layout.setVisibility(View.GONE);
        	}
        }catch(Exception e){
        }
	}
	
	public class VoicePlayAsync extends AsyncTask<String, Long, Integer> implements OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener,android.widget.SeekBar.OnSeekBarChangeListener, OnErrorListener {
		public int result = -1;
		public String kwon;
		public int jang;
		public VoicePlayAsync(String kwon, int jang) {
			this.kwon = kwon;
			this.jang = jang;
		}
		
		@Override
        protected void onPreExecute() {
			try{
				mediaPlayer = new MediaPlayer();
				layout_progress.setVisibility(View.VISIBLE);
				txt_voice_title.setText("");
				if(viewpager.getCurrentItem() == 0){
					dbhelper.bible_voice_db_current_position(Const.bt_kwon_old.getText().toString(), Const.bt_jang_old.getText().toString());	
				} else if(viewpager.getCurrentItem() == 1){
					dbhelper.bible_voice_db_current_position(Const.bt_kwon_new.getText().toString(), Const.bt_jang_new.getText().toString());
				}
	            navigator_handler.removeCallbacks(UpdateTimetask);
	            if(mediaPlayer != null && mediaPlayer.isPlaying() ){
	            	mediaPlayer.stop();
	            }
			}catch(Exception e) {
			}
            super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(String... params) {
			try{
				String get_data;
				if(PreferenceUtil.getIntSharedData(context, PreferenceUtil.PREF_AUDIO_SPEED, Const.AUDIO_SPEED) == 0){
					get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.voicedata_url2));	
				}else{
					get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.voicedata_url));
				}
				mediaPlayer.setOnBufferingUpdateListener(this);
				mediaPlayer.setOnCompletionListener(this);
				mediaPlayer.setOnErrorListener(this);
				mediaPlayer.setOnPreparedListener(this);
				mediacontroller_progress.setOnSeekBarChangeListener(this);
				
				mediaPlayer.reset();
				String url = get_data+kwon+jang+""+context.getString(R.string.voicedata_type);
	            mediaPlayer.setDataSource(url);
	            mediaPlayer.prepare();
	            
	            mediacontroller_progress.setProgress(0);
				mediacontroller_progress.setMax(100);
				mediaPlayer.seekTo(Const.current_position_bible);
				updateProgressBar();
				return result = 1;
			}catch (Exception e) {
			}
			return result;
		}
		
		@Override
        protected void onProgressUpdate(Long... values) {
        	super.onProgressUpdate(values);
        }
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			try {
				if(result == 1){
					layout_progress.setVisibility(View.INVISIBLE);
					mediaPlayer.start();
				}else{
					voice_play_start();
				}	
			}catch (NullPointerException e) {
			}catch (Exception e) {
			}
		}
        
        @Override
		public void onPrepared(MediaPlayer mp) {
        	if(viewpager.getCurrentItem() == 0){
        		txt_voice_title.setText(Const.bt_kwon_old.getText().toString() + jang);	
        	}else if(viewpager.getCurrentItem() == 1){
        		txt_voice_title.setText(Const.bt_kwon_new.getText().toString() + jang);	
        	}
    		mediacontroller_progress.setSecondaryProgress(0);
		}
        
		@Override
		public void onBufferingUpdate(MediaPlayer mediaPlayer, int buffering) {
//			mediacontroller_progress.setSecondaryProgress(buffering);	
		}
		@Override
		public void onCompletion(MediaPlayer mp) {
			try{
				if(mediaPlayer != null && mediaPlayer.isPlaying() ){
					mediaPlayer.seekTo(0);
					mediaPlayer.stop();
				}else{
					if(viewpager.getCurrentItem() == 0){
						dbhelper.bible_voice_db_pause_task(0, Const.bt_kwon_old.getText().toString(), Const.bt_jang_old.getText().toString());	
					}else if(viewpager.getCurrentItem() == 1){
						dbhelper.bible_voice_db_pause_task(0, Const.bt_kwon_new.getText().toString(), Const.bt_jang_new.getText().toString());
					}
				}
				if(PreferenceUtil.getBooleanSharedData(context, PreferenceUtil.PREF_VOICE_CONTINUE, Const.voice_continue) == true){
					voice_play_next();	
				}
			}catch(Exception e){
			}
		}
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			navigator_handler.removeCallbacks(UpdateTimetask);
			if(mediaPlayer != null && mediaPlayer.isPlaying() ){
				if(viewpager.getCurrentItem() == 0){
					dbhelper.bible_voice_db_pause_task(mediaPlayer.getCurrentPosition(), Const.bt_kwon_old.getText().toString(), Const.bt_jang_old.getText().toString());	
				}else if(viewpager.getCurrentItem() == 1){
					dbhelper.bible_voice_db_pause_task(mediaPlayer.getCurrentPosition(), Const.bt_kwon_new.getText().toString(), Const.bt_jang_new.getText().toString());
				}
			}
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
		public boolean onError(MediaPlayer mp, int what, int extra) {
			try{
				voice_play_stop();
			}catch(Exception e){
			}
//			Log.i("dsu", "onError : " + extra);
			return false;
		}
	}
	
	public void updateProgressBar(){
		navigator_handler.postDelayed(UpdateTimetask, 100);
	}
	
	public Runnable UpdateTimetask = new Runnable() {
		@Override
		public void run() {
			if(mediaPlayer != null){
				if(mediaPlayer != null && mediaPlayer.isPlaying() ){
					bt_pause.setImageResource(R.drawable.ic_action_pause);
				}else{
					if(mediaPlayer != null && mediaPlayer.isPlaying() ){
						if(viewpager.getCurrentItem() == 0){
							dbhelper.bible_voice_db_pause_task(mediaPlayer.getCurrentPosition(), Const.bt_kwon_old.getText().toString(), Const.bt_jang_old.getText().toString());	
						}else if(viewpager.getCurrentItem() == 1){
							dbhelper.bible_voice_db_pause_task(mediaPlayer.getCurrentPosition(), Const.bt_kwon_new.getText().toString(), Const.bt_jang_new.getText().toString());
						}
					}
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
	
	private void voice_play_next(){
		if(viewpager.getCurrentItem() == 0){
			for(int i=1; i < Const.kwon_kbb_old.length+1; i++){
				if(Const.kwon_old.equals(Integer.toString(i))){
					if(Integer.parseInt(Const.jang_old) > Const.jang_page_max_old()-1 ){
						Toast.makeText(context, R.string.frg_old_bible_1, Toast.LENGTH_SHORT).show();
						return;
					}else{
						try{
							voice_play_stop();
						}catch(Exception e){
						}
						int next_num = Integer.parseInt(Const.jang_old)+1;
						Const.jang_old = Integer.toString(next_num);
						datasetchanged();
						Const.bt_jang_old.setText(Const.jang_old + context.getString(R.string.txt_jang));
						PreferenceUtil.setIntSharedData(context, PreferenceUtil.PREF_JANG_OLD_WHICH, next_num-1);
						PreferenceUtil.setStringSharedData(context, PreferenceUtil.PREF_JANG_OLD, Const.jang_old);
						Toast.makeText(context, context.getString(R.string.voicedata_continue_toast), Toast.LENGTH_LONG).show();
						action_background = false;
//						addInterstitialView();
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								voice_play_start();
							}
						},1000);
					}
				}
			}
		}else if(viewpager.getCurrentItem() == 1){
			for(int i=40; i < Const.kwon_kbb_old.length+40; i++){
				if(Const.kwon_new.equals(Integer.toString(i))){
					if(Integer.parseInt(Const.jang_new) > Const.jang_page_max_new()-1 ){
						Toast.makeText(context, R.string.frg_new_bible_1, Toast.LENGTH_SHORT).show();
						return;
					}else{
						try{
							voice_play_stop();
						}catch(Exception e){
						}
						int next_num = Integer.parseInt(Const.jang_new)+1;
						Const.jang_new = Integer.toString(next_num);
						datasetchanged();
						Const.bt_jang_new.setText(Const.jang_new + context.getString(R.string.txt_jang));
						PreferenceUtil.setIntSharedData(context, PreferenceUtil.PREF_JANG_NEW_WHICH, next_num-1);
						PreferenceUtil.setStringSharedData(context, PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
						Toast.makeText(context, context.getString(R.string.voicedata_continue_toast), Toast.LENGTH_LONG).show();
						action_background = false;
//						addInterstitialView();
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								voice_play_start();
							}
						},1000);
					}
				}
			}
		}
	}
	
	public void voice_play_start(){
		if(!mNetHelper.is3GConnected() && !mNetHelper.isWIFIConneced()){
			Toast.makeText(context, context.getString(R.string.download_data_connection_ment), Toast.LENGTH_LONG).show();
			return;
		}
		if(viewpager.getCurrentItem() == 0){
			voicePlayAsync = new VoicePlayAsync(Const.bt_kwon_old.getText().toString()
					.replace(Const.kwon_kbb_old[0], "Genesis")
					.replace(Const.kwon_kbb_old[1], "Exodus")
					.replace(Const.kwon_kbb_old[2], "Leviticus")
					.replace(Const.kwon_kbb_old[3], "Numbers")
					.replace(Const.kwon_kbb_old[4], "Deuteronomy")
					.replace(Const.kwon_kbb_old[5], "Joshua")
					.replace(Const.kwon_kbb_old[6], "Judges")
					.replace(Const.kwon_kbb_old[7], "Ruth")
					.replace(Const.kwon_kbb_old[8], "Samuel_up")
					.replace(Const.kwon_kbb_old[9], "Samuel_down")
					.replace(Const.kwon_kbb_old[10], "Kings_up")
					.replace(Const.kwon_kbb_old[11], "Kings_down")
					.replace(Const.kwon_kbb_old[12], "Chronicles_up")
					.replace(Const.kwon_kbb_old[13], "Chronicles_down")
					.replace(Const.kwon_kbb_old[14], "Ezra")
					.replace(Const.kwon_kbb_old[15], "Nehemiah")
					.replace(Const.kwon_kbb_old[16], "Esther")
					.replace(Const.kwon_kbb_old[17], "Job")
					.replace(Const.kwon_kbb_old[18], "Psalms")
					.replace(Const.kwon_kbb_old[19], "Proverbs")
					.replace(Const.kwon_kbb_old[20], "Ecclesiastes")
					.replace(Const.kwon_kbb_old[21], "Song")
					.replace(Const.kwon_kbb_old[22], "Isaiah")
					.replace(Const.kwon_kbb_old[23], "Jeremiah")
					.replace(Const.kwon_kbb_old[24], "Lamentations")
					.replace(Const.kwon_kbb_old[25], "Ezekiel")
					.replace(Const.kwon_kbb_old[26], "Daniel")
					.replace(Const.kwon_kbb_old[27], "Hosea")
					.replace(Const.kwon_kbb_old[28], "Joel")
					.replace(Const.kwon_kbb_old[29], "Amos")
					.replace(Const.kwon_kbb_old[30], "Obadiah")
					.replace(Const.kwon_kbb_old[31], "Jonah")
					.replace(Const.kwon_kbb_old[32], "Micah")
					.replace(Const.kwon_kbb_old[33], "Nahum")
					.replace(Const.kwon_kbb_old[34], "Habakkuk")
					.replace(Const.kwon_kbb_old[35], "Zephaniah")
					.replace(Const.kwon_kbb_old[36], "Haggai")
					.replace(Const.kwon_kbb_old[37], "Zechariah")
					.replace(Const.kwon_kbb_old[38], "Malachi")
					, Integer.parseInt(Const.bt_jang_old.getText().toString().replace(context.getString(R.string.txt_jang), "")));
			voicePlayAsync.execute();	
		}else if(viewpager.getCurrentItem() == 1){
			voicePlayAsync = new VoicePlayAsync(Const.bt_kwon_new.getText().toString()
					.replace(Const.kwon_kbb_new[0], "Matthew")
					.replace(Const.kwon_kbb_new[1], "Mark")
					.replace(Const.kwon_kbb_new[2], "Luke")
					.replace(Const.kwon_kbb_new[3], "John")
					.replace(Const.kwon_kbb_new[4], "Acts")
					.replace(Const.kwon_kbb_new[5], "Romans")
					.replace(Const.kwon_kbb_new[6], "Corinthians_up")
					.replace(Const.kwon_kbb_new[7], "Corinthians_down")
					.replace(Const.kwon_kbb_new[8], "Galatians")
					.replace(Const.kwon_kbb_new[9], "Ephesians")
					.replace(Const.kwon_kbb_new[10], "Philippians")
					.replace(Const.kwon_kbb_new[11], "Colossians")
					.replace(Const.kwon_kbb_new[12], "Thessalonians_up")
					.replace(Const.kwon_kbb_new[13], "Thessalonians_down")
					.replace(Const.kwon_kbb_new[14], "Timothy_up")
					.replace(Const.kwon_kbb_new[15], "Timothy_down")
					.replace(Const.kwon_kbb_new[16], "Titus")
					.replace(Const.kwon_kbb_new[17], "Philemon")
					.replace(Const.kwon_kbb_new[18], "Hebrews")
					.replace(Const.kwon_kbb_new[19], "James")
					.replace(Const.kwon_kbb_new[20], "Peter_up")
					.replace(Const.kwon_kbb_new[21], "Peter_down")
					.replace(Const.kwon_kbb_new[22], "John_one")
					.replace(Const.kwon_kbb_new[23], "John_two")
					.replace(Const.kwon_kbb_new[24], "John_three")
					.replace(Const.kwon_kbb_new[25], "Jude")
					.replace(Const.kwon_kbb_new[26], "Revelation")
					, Integer.parseInt(Const.bt_jang_new.getText().toString().replace(context.getString(R.string.txt_jang), "")));
			voicePlayAsync.execute();
		}
		
	}
	
	public void voice_play_stop(){
		try{
			if(mediaPlayer != null && mediaPlayer.isPlaying() ){
				if(viewpager.getCurrentItem() == 0){
					dbhelper.bible_voice_db_pause_task(mediaPlayer.getCurrentPosition(), Const.bt_kwon_old.getText().toString(), Const.bt_jang_old.getText().toString());	
				}else if(viewpager.getCurrentItem() == 1){
					dbhelper.bible_voice_db_pause_task(mediaPlayer.getCurrentPosition(), Const.bt_kwon_new.getText().toString(), Const.bt_jang_new.getText().toString());
				}
		    	txt_voice_title.setText(context.getString(R.string.voicedata_ready));
		    	current_time.setText(context.getString(R.string.voicedata_time));
		    	max_time.setText(context.getString(R.string.voicedata_time));
		    	navigator_handler.removeCallbacks(UpdateTimetask);
		    	mediaPlayer.seekTo(0);
		    	mediacontroller_progress.setProgress(0);
				if(mediaPlayer != null && mediaPlayer.isPlaying() ){
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
					bt_pause.setImageResource(R.drawable.ic_action_play);
				}else{
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
					bt_pause.setImageResource(R.drawable.ic_action_play);
				}
		    	if(voicePlayAsync != null){
		    		voicePlayAsync.cancel(true);
		    	}
			}
		}catch(NullPointerException e){
		}
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 if(keyCode == KeyEvent.KEYCODE_BACK){
			 if(!flag){
				 Toast.makeText(context, context.getString(R.string.txt_back) , Toast.LENGTH_LONG).show();
				 flag = true;
				 handler.sendEmptyMessageDelayed(0, 2000);
				 return false;
			 }else{
				 handler.postDelayed(new Runnable() {
					 @Override
					 public void run() {
						 PreferenceUtil.setBooleanSharedData(context, PreferenceUtil.PREF_AD_VIEW, true);
						 voice_play_stop();
						 NotificationUtil.setNotification_Cancel();
						 finish();
					 }
				 },0);
			 }
			 return false;	 
		 }
		return super.onKeyDown(keyCode, event);
	}
	
	//** CustomPopup 이벤트들 *************
	@Override
	public void onCloseCustomPopup(String arg0) {
	
	}

	@Override
	public void onHasNoCustomPopup() {
	
	}

	@Override
	public void onShowCustomPopup(String arg0) {
	
	}

	@Override
	public void onStartedCustomPopup() {
	
	}

	@Override
	public void onWillCloseCustomPopup(String arg0) {
	
	}

	@Override
	public void onWillShowCustomPopup(String arg0) {
	
	}
	
	@Override
	public void onInterstitialAdClosed(InterstitialAd arg0) {
		interstialAd = null;
		if(action_background == true){
			home_action();
		}
	}

	@Override
	public void onInterstitialAdFailedToReceive(int arg0, String arg1, InterstitialAd arg2) {
		interstialAd = null;
		if(action_background == true){
			home_action();
		}else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setCancelable(false);
			builder.setTitle(context.getString(R.string.app_name));
			builder.setMessage(context.getString(R.string.txt_finish_ment));
			builder.setInverseBackgroundForced(true);
			builder.setNeutralButton(context.getString(R.string.txt_finish_yes), new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int whichButton){
					PreferenceUtil.setBooleanSharedData(context, PreferenceUtil.PREF_AD_VIEW, true);
					finish();
				}
			});
			builder.setNegativeButton(context.getString(R.string.txt_finish_no), new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int whichButton){
	             	 dialog.dismiss();
				}
			});
			AlertDialog myAlertDialog = builder.create();
			if(retry_alert) myAlertDialog.show();
		}
	}

	@Override
	public void onInterstitialAdReceived(String arg0, InterstitialAd arg1) {
		interstialAd = null;
	}

	@Override
	public void onInterstitialAdShown(String arg0, InterstitialAd arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLeftClicked(String arg0, InterstitialAd arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRightClicked(String arg0, InterstitialAd arg1) {
		PreferenceUtil.setBooleanSharedData(context, PreferenceUtil.PREF_AD_VIEW, true);
		voice_play_stop();
		NotificationUtil.setNotification_Cancel();
		finish();
		
	}
	
	//** BannerAd 이벤트들 *************
	@Override
	public void onClickedAd(String arg0, AdView arg1) {
	}
	@Override
	public void onFailedToReceiveAd(int arg0, String arg1, AdView arg2) {
		
	}
	@Override
	public void onReceivedAd(String arg0, AdView arg1) {
	}		
	
	public static DownloadDBAsync downloadDBAsync = null;
	public static ProgressDialog mProgressDialog;
}
