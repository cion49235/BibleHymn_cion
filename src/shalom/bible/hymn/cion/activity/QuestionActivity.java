package shalom.bible.hymn.cion.activity;

import com.actionbarsherlock.app.SherlockActivity;
import com.admixer.AdAdapter;
import com.admixer.AdInfo;
import com.admixer.AdMixerManager;
import com.admixer.AdViewListener;
import com.admixer.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import shalom.bible.hymn.cion.R;
import shalom.bible.hymn.cion.common.Const;
import shalom.bible.hymn.cion.util.PreferenceUtil;
import shalom.bible.hymn.cion.util.SimpleCrypto;
import shalom.bible.hymn.cion.util.Utils;

public class QuestionActivity extends SherlockActivity implements InterstitialAdListener, AdViewListener, OnClickListener{
	public static Context context;
	public static com.admixer.InterstitialAd interstialAd;
	public Handler handler = new Handler();
	private WebView webview;
	private boolean retry_alert = false;
	private NativeExpressAdView admobNative;
	public static RelativeLayout ad_layout;
	private boolean flag;
	private ImageButton btnLeft;
	private TextView main_title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		retry_alert = true;
		context = this;
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMIXER, "vuh3q0an");
    	AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB, "ca-app-pub-4637651494513698/7757784963");
    	AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB_FULL, "ca-app-pub-4637651494513698/1711251362");
//    	init_admob_naive();
    	if(!PreferenceUtil.getStringSharedData(context, PreferenceUtil.PREF_ISSUBSCRIBED, Const.isSubscribed).equals("true")){
        	addBannerView();    		
    	}
		set_titlebar();
		display_question();
		exit_handler();
	}
	
	@Override
	public void onClick(View view) {
		if(view == btnLeft){
			exit_action();
		}
	}
	
	private void exit_action(){
		handler.postDelayed(new Runnable() {
			 @Override
			 public void run() {
				 if (webview.canGoBack()) {
					 webview.goBack();
				 } else {
					 if(!flag){
						 Toast.makeText(context, context.getString(R.string.txt_question_back) , Toast.LENGTH_SHORT).show();
						 flag = true;
						 handler.sendEmptyMessageDelayed(0, 2000);
					 }else{
						 onDestroy();
					 }
				 }
			 }
		 },0);
	}
	
	private void set_titlebar(){
		btnLeft = (ImageButton)findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(this);
		main_title = (TextView)findViewById(R.id.main_title);
		main_title.setText(context.getString(R.string.txt_question_title));
	}
	
	public void addBannerView() {
    	AdInfo adInfo = new AdInfo("vuh3q0an");
    	adInfo.setTestMode(false);
        com.admixer.AdView adView = new com.admixer.AdView(this);
        adView.setAdInfo(adInfo, this);
        adView.setAdViewListener(this);
        ad_layout = (RelativeLayout)findViewById(R.id.ad_layout);
        if(ad_layout != null){
        	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            ad_layout.addView(adView, params);	
        }
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
	
	private void display_question(){
		webview = new WebView(this);
		webview = (WebView) findViewById(R.id.webview);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setBuiltInZoomControls(true);
		webview.setVerticalScrollbarOverlay(true);
		webview.setVerticalScrollBarEnabled(true);
		webview.setWebViewClient(new WebViewClientClass());		
		webview.setWebChromeClient(new WebChromeClientClass());
		try {
			String get_data = SimpleCrypto.decrypt(Utils.get_data, context.getString(R.string.txt_url));
			webview.loadUrl(get_data);
		} catch (Exception e) {
		}
	}
	
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
	
	private class WebViewClientClass extends WebViewClient{
	    private WebViewClientClass() {
	    }
	    
	    public void onPageFinished(WebView paramWebView, String paramString){
	      super.onPageFinished(paramWebView, paramString);
	    }
	    
	    public void onPageStarted(WebView paramWebView, String paramString, Bitmap paramBitmap){
	      super.onPageStarted(paramWebView, paramString, paramBitmap);
	    }
	    
	    
	    public void onReceivedError(WebView paramWebView, int paramInt, String paramString1, String paramString2){
	    	NetworkErrorAlertShow(context.getString(R.string.txt_network_error));
	    }
	}
	
	public void NetworkErrorAlertShow(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(false);
		builder.setMessage(msg);
		builder.setInverseBackgroundForced(true);
		builder.setNeutralButton(context.getString(R.string.txt_confirm), new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){
				gotoSettingNetwork();
			}
		});
		builder.setNegativeButton(context.getString(R.string.txt_cancel), new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){
				finish();
			}
		});
		AlertDialog myAlertDialog = builder.create();
		if(retry_alert) myAlertDialog.show();
	}
	
	private class WebChromeClientClass extends WebChromeClient { 
		ProgressBar pb_item01 = (ProgressBar) findViewById(R.id.pb_item01);
		@Override
		public void onProgressChanged(WebView view, int progress) {
			super.onProgressChanged(view, progress);
			pb_item01.setProgress(progress); 
			if (progress == 100) { 
				pb_item01.setVisibility(View.GONE);
			} else {
				pb_item01.setVisibility(View.VISIBLE);
			}
		}
		public WebChromeClientClass() {
		}
	}
	
	public void gotoSettingNetwork() {
		Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
//		admobNative.pause();
		webview.onPause();

	}
	@Override
	protected void onResume() {
		super.onResume();
//		admobNative.resume();
		webview.onResume();
	}
	@Override
	protected void onStop() {
		super.onStop();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		retry_alert = false;
//		admobNative.destroy();
		finish();
	}
	@Override
	protected void onRestart() {
		super.onRestart();
	}
	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
	}
	
	public void addInterstitialView() {
    	if(interstialAd == null) {
        	AdInfo adInfo = new AdInfo("vuh3q0an");
//        	adInfo.setTestMode(false);
        	interstialAd = new com.admixer.InterstitialAd(this);
        	interstialAd.setAdInfo(adInfo, this);
        	interstialAd.setInterstitialAdListener(this);
        	interstialAd.startInterstitial();
    	}
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
			 handler.postDelayed(new Runnable() {
				 @Override
				 public void run() {
					 if (webview.canGoBack()) {
						 webview.goBack();
					 } else {
						 if(!flag){
							 Toast.makeText(context, context.getString(R.string.txt_question_back) , Toast.LENGTH_SHORT).show();
							 flag = true;
							 handler.sendEmptyMessageDelayed(0, 2000);
						 }else{
							 onDestroy();
						 }
					 }
				 }
			 },0);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onInterstitialAdClosed(com.admixer.InterstitialAd arg0) {
		interstialAd = null;
		onDestroy();
	}

	@Override
	public void onInterstitialAdFailedToReceive(int arg0, String arg1,
			com.admixer.InterstitialAd arg2) {
		interstialAd = null;
	}

	@Override
	public void onInterstitialAdReceived(String arg0,
			com.admixer.InterstitialAd arg1) {
		interstialAd = null;
	}

	@Override
	public void onInterstitialAdShown(String arg0,
			com.admixer.InterstitialAd arg1) {
	}

	@Override
	public void onLeftClicked(String arg0, com.admixer.InterstitialAd arg1) {
	}

	@Override
	public void onRightClicked(String arg0, com.admixer.InterstitialAd arg1) {
	}
	
	@Override
	public void onClickedAd(String arg0, com.admixer.AdView arg1) {
	}

	@Override
	public void onFailedToReceiveAd(int arg0, String arg1,
			com.admixer.AdView arg2) {
	}

	@Override
	public void onReceivedAd(String arg0, com.admixer.AdView arg1) {
	}
}
