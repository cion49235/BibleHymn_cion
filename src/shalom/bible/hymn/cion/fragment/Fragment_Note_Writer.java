package shalom.bible.hymn.cion.fragment;


import java.text.SimpleDateFormat;
import java.util.Date;

import com.actionbarsherlock.app.SherlockActivity;
import com.admixer.AdAdapter;
import com.admixer.AdInfo;
import com.admixer.AdMixerManager;
import com.admixer.AdView;
import com.admixer.AdViewListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import shalom.bible.hymn.cion.R;
import shalom.bible.hymn.cion.common.Const;
import shalom.bible.hymn.cion.db.helper.DBOpenHelper;
import shalom.bible.hymn.cion.util.PreferenceUtil;


public class Fragment_Note_Writer extends SherlockActivity implements OnClickListener, AdViewListener {
	private EditText edit_title_txt, edit_content_txt;
	private int _id;
	private String kwon, jang, jul, content;
	private boolean edit = false;
	private final DBOpenHelper dbhelper = DBOpenHelper.getInstance();
	private Context context;
	private RelativeLayout ad_layout;
	private boolean is_view = false;
	private ImageButton btnLeft, titlebar_add;
	private TextView main_title;
	private NativeExpressAdView admobNative;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_writer);
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMIXER, "vuh3q0an");
    	AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB, "ca-app-pub-4637651494513698/7757784963");
    	AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB_FULL, "ca-app-pub-4637651494513698/1711251362");
		context = this;
		if(!PreferenceUtil.getStringSharedData(context, PreferenceUtil.PREF_ISSUBSCRIBED, Const.isSubscribed).equals("true")){
        	addBannerView();    		
    	}
//		init_admob_naive();
		get_intent_data();
		init_ui();
		set_titlebar();
		if(is_view == true){
			edittext_focus(false);
		}
		
		edit_title_txt.setText(kwon);
		content = getIntent().getStringExtra("content");
		if(content != null){
			edit_content_txt.setText(content);
		}
	}
	
	private void set_titlebar(){
		btnLeft = (ImageButton)findViewById(R.id.btnLeft);
		titlebar_add = (ImageButton)findViewById(R.id.titlebar_add);
		titlebar_add.setVisibility(View.VISIBLE);
		btnLeft.setOnClickListener(this);
		titlebar_add.setOnClickListener(this);
		main_title = (TextView)findViewById(R.id.main_title);
		main_title.setText(context.getString(R.string.tab_menu_5));
	}
	
	private void get_intent_data(){
		is_view = getIntent().getBooleanExtra("is_view", is_view);
		_id = getIntent().getIntExtra("_id", 0);
		kwon = getIntent().getStringExtra("kwon");
		jang = getIntent().getStringExtra("jang");
		jul = getIntent().getStringExtra("jul");
		edit = getIntent().getBooleanExtra("edit", false);
	}
	
	private void init_ui(){
		edit_title_txt = (EditText)findViewById(R.id.edit_title_txt);
		edit_content_txt = (EditText)findViewById(R.id.edit_content_txt);
	}
	
	private void edittext_focus(boolean is_view){
		edit_title_txt.setFocusableInTouchMode(is_view);
		edit_content_txt.setFocusableInTouchMode(is_view);
		edit_title_txt.setFocusable(is_view);
		edit_content_txt.setFocusable(is_view);
	}
	
	private void edittext_clear(){
		edit_title_txt.setText("");
		edit_content_txt.setText("");
		edit = false;
		is_view = false;
	}
	
	private void hide_keypad(){
		InputMethodManager inputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);  
		inputMethodManager.hideSoftInputFromWindow(edit_title_txt.getWindowToken(), 0);
		inputMethodManager.hideSoftInputFromWindow(edit_content_txt.getWindowToken(), 0);
	}
	
	private void exit_action(){
		try{
			if(edit_title_txt.getText().length() == 0 && edit_content_txt.getText().length() == 0){
				onBackPressed();
			}else if(edit_content_txt.getText().length() == 0){
				onBackPressed();
			}
			else{
				if(is_view == true){
					onBackPressed();
					return;
				}
				SimpleDateFormat dateFormat = new SimpleDateFormat("y.MM.dd a h:mm:ss");  
				Date date = new Date();
				ContentValues cv = new ContentValues();
				if(edit_title_txt.getText().length() == 0){
					cv.put("kwon", context.getString(R.string.frg_note_writer_01));	
				}else{
					cv.put("kwon", edit_title_txt.getText().toString());
				}
				/*if(jang != null){
					if(jang.length() < 2){
						cv.put("jang", jang);
					}	
				}else{
					cv.put("jang", "\n"+dateFormat.format(date));
				}*/
				cv.put("jang", "\n"+dateFormat.format(date));
				if(jul != null){
					if(jul.length() == 0){
						cv.put("jul", "");
					}else{
						cv.put("jul", jul);
					}
				}
				cv.put("content", edit_content_txt.getText().toString());
				if(edit == true){
					dbhelper.update_note_db(cv, _id);
					Toast.makeText(context, R.string.frg_note_writer_04, Toast.LENGTH_SHORT).show();
				}else{
					dbhelper.insert_note_db(cv);
					Toast.makeText(context, R.string.frg_note_writer_03, Toast.LENGTH_SHORT).show();
				}
				onBackPressed();
			}
		}catch(Exception e){
		}finally{
			dbhelper.close_note_db();
			hide_keypad();
		}
	}
	
	@Override
	public void onClick(View view) {
		if(view == btnLeft){
			exit_action();
		}else if(view == titlebar_add){
			Toast.makeText(context, context.getString(R.string.frg_note_writer_05), Toast.LENGTH_SHORT).show();
			edittext_clear();
			edittext_focus(true);
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
	protected void onDestroy() {
		super.onDestroy();
//		admobNative.destroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			try{
				if(edit_title_txt.getText().length() == 0 && edit_content_txt.getText().length() == 0){
					onBackPressed();
				}else if(edit_content_txt.getText().length() == 0){
					onBackPressed();
				}
				else{
					if(is_view == true){
						onBackPressed();
						return true;
					}
					SimpleDateFormat dateFormat = new SimpleDateFormat("y.MM.dd a h:mm:ss");  
					Date date = new Date();
					ContentValues cv = new ContentValues();
					if(edit_title_txt.getText().length() == 0){
						cv.put("kwon", context.getString(R.string.frg_note_writer_01));	
					}else{
						cv.put("kwon", edit_title_txt.getText().toString());
					}
					/*if(jang != null){
						if(jang.length() < 2){
							cv.put("jang", jang);
						}	
					}else{
						cv.put("jang", "\n"+dateFormat.format(date));
					}*/
					cv.put("jang", "\n"+dateFormat.format(date));
					if(jul != null){
						if(jul.length() == 0){
							cv.put("jul", "");
						}else{
							cv.put("jul", jul);
						}
					}
					cv.put("content", edit_content_txt.getText().toString());
					if(edit == true){
						dbhelper.update_note_db(cv, _id);
						Toast.makeText(context, R.string.frg_note_writer_04, Toast.LENGTH_SHORT).show();
					}else{
						dbhelper.insert_note_db(cv);
						Toast.makeText(context, R.string.frg_note_writer_03, Toast.LENGTH_SHORT).show();
					}
					onBackPressed();
				}
			}catch(Exception e){
			}finally{
				dbhelper.close_note_db();
				hide_keypad();
			}
		}
		return super.onKeyDown(keyCode, event);
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
	
	//** BannerAd 이벤트들 *************
	@Override
	public void onClickedAd(String arg0, AdView arg1) {
	}

	@Override
	public void onFailedToReceiveAd(int arg0, String arg1, AdView arg2) {
		
	}
	@Override
	public void onReceivedAd(String arg0, AdView arg1) {
//		Log.i("dsu", "배너광고 : arg0 : " + arg0+"\n" + arg1) ;
	}
}
