package shalom.bible.hymn.cion.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.actionbarsherlock.app.SherlockActivity;
import com.admixer.AdAdapter;
import com.admixer.AdInfo;
import com.admixer.AdMixerManager;
import com.admixer.AdView;
import com.admixer.AdViewListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import shalom.bible.hymn.cion.R;
import shalom.bible.hymn.cion.dao.Activity_Data_Podcast_Sub;
import shalom.bible.hymn.cion.db.helper.DBOpenHelper;
import shalom.bible.hymn.cion.podcast.mediaplayer.ContinueMediaPlayer_Podcast;
import shalom.bible.hymn.cion.podcast.mediaplayer.CustomMediaPlayer_Podcast;
import shalom.bible.hymn.cion.util.NetworkHelper;
import shalom.bible.hymn.cion.util.PreferenceUtil;
import shalom.bible.hymn.cion.util.StringUtil;

public class PodcastActivity_Sub extends SherlockActivity implements OnClickListener,OnItemClickListener, OnScrollListener, AdViewListener {
	public static Context context;
	private final DBOpenHelper dbhelper = DBOpenHelper.getInstance();
	private ArrayList<Activity_Data_Podcast_Sub> list;
	private LinearLayout layout_listview_main, layout_nodata, action_layout;
	private static ListView listview_main;
	private ActivityAdapter adapter;
	private ImageButton btnLeft, btn_favorite, btn_download;
	private Button bt_action_mode;
	private TextView main_title;
	private boolean retry_alert = false;
	private String Response;;
	private Activity_Data_Podcast_Sub sub_data;
	private String enclosure;
	private String pubDate;
	private String image;
	private int dataPercent;
	private String rssurl;
	private String provider;
	private static String title;
	private PodcastAsync Async = null;
	private boolean action_mode = false;
	private Button bt_action1, bt_action2;
	private final NetworkHelper mNetHelper = NetworkHelper.getInstance();
	private RelativeLayout ad_layout;
	private NativeExpressAdView admobNative;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_podcast_sub);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		context = this;
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMIXER, "vuh3q0an");
    	AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB, "ca-app-pub-4637651494513698/7757784963");
    	AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB_FULL, "ca-app-pub-4637651494513698/1711251362");
    	addBannerView();
//    	init_admob_naive();
		retry_alert = true;
		title = getIntent().getStringExtra("title");
		provider = getIntent().getStringExtra("provider");
		rssurl = getIntent().getStringExtra("rssurl");
		init_ui();
		set_titlebar();
		list = new ArrayList<Activity_Data_Podcast_Sub>();
		list.clear();
		displaylist();
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
		retry_alert = false;
		action_mode = false;
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		datasetchanged();
	}
	
	private void init_ui(){
		layout_listview_main = (LinearLayout)findViewById(R.id.layout_listview_main);
		action_layout = (LinearLayout)findViewById(R.id.action_layout);
		layout_nodata = (LinearLayout)findViewById(R.id.layout_nodata);
		listview_main = (ListView)findViewById(R.id.listview_main);
		listview_main.setOnScrollListener(this);
		listview_main.setOnItemClickListener(this);
		listview_main.setItemsCanFocus(false);
		listview_main.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		btn_favorite = (ImageButton)findViewById(R.id.btn_favorite);
		btn_download = (ImageButton)findViewById(R.id.btn_download);
		btn_favorite.setOnClickListener(this);
		btn_download.setOnClickListener(this);
		
		bt_action1 = (Button)findViewById(R.id.bt_action1);
    	bt_action2 = (Button)findViewById(R.id.bt_action2);
    	bt_action1.setOnClickListener(this);
    	bt_action2.setOnClickListener(this);
	}
	
	private void set_titlebar(){
		btnLeft = (ImageButton)findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(this);
		main_title = (TextView)findViewById(R.id.main_title);
		main_title.setSingleLine();
		main_title.setText(title);
		main_title.setSingleLine();
		main_title.setTextSize(12);
		bt_action_mode = (Button)findViewById(R.id.bt_action_mode);
		bt_action_mode.setText(context.getString(R.string.activity_podcast_25));
		bt_action_mode.setVisibility(View.VISIBLE);
		bt_action_mode.setOnClickListener(this);
	}
	
	private void displaylist(){
		Async = new PodcastAsync();
		Async.execute();
	}
	
	@Override
	public void onClick(View view) {
		if(view == btnLeft){
			onBackPressed();
		}else if(view == btn_favorite){
			
		}else if(view == btn_download){
			
		}else if(view == bt_action_mode){
			if(bt_action_mode.isSelected()){
				bt_action_mode.setSelected(false);
				bt_action_mode.setText(context.getString(R.string.activity_podcast_25));
				action_mode = false;
				DeSelected();
			}else{
				bt_action_mode.setSelected(true);//액션모드
				bt_action_mode.setText(context.getString(R.string.activity_podcast_26));
				action_mode = true;
				Toast.makeText(context, context.getString(R.string.activity_podcast_27), Toast.LENGTH_LONG).show();
			}
		}else if(view == bt_action1){
			if(bt_action1.isSelected()){
				bt_action1.setSelected(false);
				bt_action1.setText(getString(R.string.activity_podcast_18_1));
				for(int i=0; i < listview_main.getCount(); i++){
					listview_main.setItemChecked(i, false);
				}
				action_layout.setVisibility(View.GONE);
			}else{
				bt_action1.setSelected(true);
				bt_action1.setText(getString(R.string.activity_podcast_18_2));
				for(int i=0; i < listview_main.getCount(); i++){
					listview_main.setItemChecked(i, true);
				}
			}
		}else if(view == bt_action2){
			if(!mNetHelper.is3GConnected() && !mNetHelper.isWIFIConneced()){
				Toast.makeText(context, context.getString(R.string.download_data_connection_ment), Toast.LENGTH_LONG).show();
				return;
			}
			SparseBooleanArray sba = listview_main.getCheckedItemPositions();
			ArrayList<String> array_title = new ArrayList<String>();
			ArrayList<String> array_enclosure = new ArrayList<String>();
			ArrayList<String> array_pubDate = new ArrayList<String>();
			ArrayList<String> array_image = new ArrayList<String>();
			ArrayList<String> array_description_title = new ArrayList<String>();
			if(sba.size() != 0){
				for(int i = listview_main.getCount() -1; i>=0; i--){
					if(sba.get(i)){
						Activity_Data_Podcast_Sub continue_data = (Activity_Data_Podcast_Sub)adapter.getItem(i);
						String title = continue_data.title;
						String enclosure = continue_data.enclosure;
						String pubDate = StringUtil.setDateTrim(continue_data.pubDate);
						String image = continue_data.image;
						String description_title = continue_data.description_title;
						
						array_title.add(title);
						array_enclosure.add(enclosure);
						array_pubDate.add(pubDate);
						array_image.add(image);
						array_description_title.add(description_title);
						sba = listview_main.getCheckedItemPositions();
					}
				}
				Intent intent = new Intent(context, ContinueMediaPlayer_Podcast.class);
				intent.putExtra("array_title", array_title);
				intent.putExtra("array_enclosure", array_enclosure);
				intent.putExtra("array_pubDate", array_pubDate);
				intent.putExtra("array_image", array_image);
				intent.putExtra("array_description_title", array_description_title);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
			}			
		}
	}
	
	private void DeSelected(){
		bt_action1.setSelected(false);
		bt_action1.setText(getString(R.string.activity_podcast_18_1));
		for(int i=0; i < listview_main.getCount(); i++){
			listview_main.setItemChecked(i, false);
		}
		action_layout.setVisibility(View.GONE);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		if(!mNetHelper.is3GConnected() && !mNetHelper.isWIFIConneced()){
			Toast.makeText(context, context.getString(R.string.download_data_connection_ment), Toast.LENGTH_LONG).show();
			return;
		}
		
		if(action_mode == true){
			int selectd_count = 0;
	    	SparseBooleanArray sba = listview_main.getCheckedItemPositions();
			if(sba.size() != 0){
				for(int i = listview_main.getCount() -1; i>=0; i--){
					if(sba.get(i)){
						sba = listview_main.getCheckedItemPositions();
						selectd_count++;
					}
				}
			}
			if(selectd_count == 0){
				action_layout.setVisibility(View.GONE);
			}else{
				action_layout.setVisibility(View.VISIBLE);
			}
			datasetchanged();
		}else if(action_mode == false){
			Activity_Data_Podcast_Sub sub_data = (Activity_Data_Podcast_Sub)adapter.getItem(position);
			String title = sub_data.title;
			String enclosure = sub_data.enclosure;
			String pubDate = sub_data.pubDate;
			String image = sub_data.image;
			String description_title = sub_data.description_title;
			Intent intent = new Intent(context, CustomMediaPlayer_Podcast.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent.putExtra("title", title);
			intent.putExtra("enclosure", enclosure);
			intent.putExtra("pubDate", StringUtil.setDateTrim(pubDate));
			intent.putExtra("image", image);
			intent.putExtra("description_title", description_title);
			startActivity(intent);
		}
	}
	
	public class PodcastAsync extends AsyncTask<String, Integer, String>{
		private ProgressDialog mProgressDialog;
		private ArrayList<Activity_Data_Podcast_Sub> menuItems = new ArrayList<Activity_Data_Podcast_Sub>();
		public PodcastAsync(){
		}
			@Override
			protected String doInBackground(String... params) {
				String sTag;
		         try{
		           String str = rssurl;
//		           Log.i("dsu", "str : " + str);
		           HttpURLConnection localHttpURLConnection = (HttpURLConnection)new URL(str).openConnection();
		           HttpURLConnection.setFollowRedirects(true);
		           localHttpURLConnection.setConnectTimeout(3000);
		           localHttpURLConnection.setReadTimeout(3000);
		           localHttpURLConnection.setRequestMethod("GET");
		           localHttpURLConnection.connect();
		           InputStream inputStream = new URL(str).openStream(); //open Stream을 사용하여 InputStream을 생성합니다.
		           XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); 
		           XmlPullParser xpp = factory.newPullParser();
		           xpp.setInput(inputStream, "UTF-8"); //euc-kr로 언어를 설정합니다. utf-8로 하니깐 깨지더군요
		           int eventType = xpp.getEventType();
		           while (eventType != XmlPullParser.END_DOCUMENT) {
			        	if (eventType == XmlPullParser.START_DOCUMENT) {
			        	}else if (eventType == XmlPullParser.END_DOCUMENT) {
			        	}else if (eventType == XmlPullParser.START_TAG){
			        		sTag = xpp.getName();
			        		if(sTag.equals("title")){
			        			sub_data = new Activity_Data_Podcast_Sub();
			        			Response = xpp.nextText();
			        			dataPercent++;
			        			publishProgress(dataPercent);
			        		}else if(sTag.equals("enclosure")){
			        			enclosure = xpp.getAttributeValue(null, "url");
			            	}else if(sTag.equals("pubDate")){
			            		pubDate = xpp.nextText();
			            	}else if(sTag.equals("itunes:image")) {
			            		image = xpp.getAttributeValue(null, "href");
        	        		}
			        	} else if (eventType == XmlPullParser.END_TAG){
			            	sTag = xpp.getName();
			            	if(sTag.equals("item")){
			            		sub_data.title = Response;
			            		sub_data.provider = provider;
			            		sub_data.enclosure = enclosure;
			            		String format_pubdate = StringUtil.setDate(pubDate);
			            		sub_data.pubDate = format_pubdate;
			            		sub_data.image = image;
			            		sub_data.description_title = title;
			            		
			            		String file_type = StringUtil.getExtension(sub_data.enclosure);
			            		Log.i("dsu", "file_type :" + file_type);
			            		if(file_type.indexOf("mp3") != -1){
			            			list.add(sub_data);	
			            		}else if(file_type.indexOf("MP3") != -1){
			            			list.add(sub_data);
			            		}else if(file_type.indexOf("m4a") != -1){
			            			list.add(sub_data);
			            		}
			            	}
			            } else if (eventType == XmlPullParser.TEXT) {
			            }
			            eventType = xpp.next();
			        }
		         }
		         catch (SocketTimeoutException localSocketTimeoutException)
		         {
		         }
		         catch (ClientProtocolException localClientProtocolException)
		         {
		         }
		         catch (IOException localIOException)
		         {
		         }
		         catch (Resources.NotFoundException localNotFoundException)
		         {
		         }
		         catch (XmlPullParserException localXmlPullParserException)
		         {
		         }
		         catch (NullPointerException NullPointerException)
		         {
		         }
		         return Response;
			}
			
			@Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            dataPercent = 0;
	            mProgressDialog = new ProgressDialog(context);
	            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	            mProgressDialog.setMessage(context.getString(R.string.activity_podcast_02));
	            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	            mProgressDialog.setCancelable(true);
	            mProgressDialog.setProgress(dataPercent);
	            mProgressDialog.setMax(100);
	            mProgressDialog.show();
	            
	        }
			@Override
			protected void onPostExecute(String Response) {
				super.onPostExecute(Response);
//				Log.i("dsu", "Response : " + Response);
				if(mProgressDialog != null){
					mProgressDialog.dismiss();
				}
				try{
					if(Response != null){
						for(int i=0;; i++){
							if(i >= list.size());
							while(i > list.size()-1){
								adapter = new ActivityAdapter(menuItems);
								listview_main.setAdapter(adapter);
								listview_main.setFocusable(true);
								listview_main.setSelected(true);
								layout_listview_main.setVisibility(View.VISIBLE);
								layout_nodata.setVisibility(View.GONE);
								return;
							}
							menuItems.add(list.get(i));
						}
					}else{
						layout_nodata.setVisibility(View.VISIBLE);
						Retry_AlertShow(context.getString(R.string.activity_podcast_03));
					}
				}catch(NullPointerException e){
				}
			}
			@Override
			protected void onProgressUpdate(Integer... values) {
				super.onProgressUpdate(values);
				mProgressDialog.setProgress(values[0]);
			}
	}
	
	public void Retry_AlertShow(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(false);
		builder.setMessage(msg);
		builder.setNeutralButton(context.getString(R.string.activity_podcast_04), new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){
				Async = new PodcastAsync();
				Async.execute();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton(context.getString(R.string.activity_podcast_05), new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){
				dialog.dismiss();
			}
		});
		AlertDialog myAlertDialog = builder.create();
		if(retry_alert) myAlertDialog.show();
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == OnScrollListener.SCROLL_STATE_FLING){
    		listview_main.setFastScrollEnabled(true);
		}else{
			listview_main.setFastScrollEnabled(false);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
	}

	public class ActivityAdapter extends BaseAdapter{
		private ArrayList<Activity_Data_Podcast_Sub> list;
		private String sub_file_name;
		public ActivityAdapter(ArrayList<Activity_Data_Podcast_Sub> list) {
			this.list = list;
		}
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View view, ViewGroup parent) {
			try{
				if(view == null){	
					LayoutInflater layoutInflater = LayoutInflater.from(context);
					view = layoutInflater.inflate(R.layout.activity_podcast_sub_listrow, parent, false);
					ViewHolder holder = new ViewHolder();
					holder.txt_sub_title = (TextView)view.findViewById(R.id.txt_sub_title);
					holder.txt_sub_pubdate = (TextView)view.findViewById(R.id.txt_sub_pubdate);
					holder.bt_sub_down = (ImageButton)view.findViewById(R.id.bt_sub_down);
					holder.bt_sub_continue = (ImageButton)view.findViewById(R.id.bt_sub_continue);
					holder.progress_down = (ProgressBar)view.findViewById(R.id.progress_down);
					holder.bt_sub_down.setFocusable(false);
					holder.bt_sub_down.setSelected(false);
					holder.bt_sub_continue.setFocusable(false);
					holder.bt_sub_continue.setSelected(false);
					view.setTag(holder);
				}
				final ViewHolder holder = (ViewHolder)view.getTag();
				holder.txt_sub_title.setText(list.get(position).title);

				if(list.get(position).title.equals(PreferenceUtil.getStringSharedData(context, PreferenceUtil.PREF_PODCAST_TITLE, "")) && StringUtil.setDateTrim(list.get(position).pubDate).equals(PreferenceUtil.getStringSharedData(context, PreferenceUtil.PREF_PODCAST_PUBDATE, ""))){
					holder.txt_sub_title.setTextColor(Color.RED);	
				}else{
					holder.txt_sub_title.setTextColor(Color.BLACK);
				}
				
				holder.txt_sub_pubdate.setText(StringUtil.setDateTrim(list.get(position).pubDate));
				holder.progress_down.setVisibility(View.INVISIBLE);
				
				String title = list.get(position).title.replaceAll("\\p{Punct}", "") + "";
				String file_type = StringUtil.getExtension(list.get(position).enclosure);
				String file_name = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ context.getString(R.string.activity_podcast_06) +title+"."+file_type;
				if(file_name.lastIndexOf("?") != -1){
		        	sub_file_name = file_name.replace(file_name.substring(file_name.lastIndexOf("?")), "");
		        }else{
		        	sub_file_name = file_name;
		        }
				
				dbhelper.check_podcast_download_db(sub_file_name, holder.bt_sub_down);
				holder.bt_sub_down.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						String old_title = list.get(position).title;
						String title = list.get(position).title.replaceAll("\\p{Punct}", "") + "";
						String enclosure = list.get(position).enclosure + "";
						String pubDate = StringUtil.setDateTrim(list.get(position).pubDate + "");
						String description_title = list.get(position).description_title + "";
						String provider = list.get(position).provider + "";
						String image = list.get(position).image + "";
						
						String file_type = StringUtil.getExtension(list.get(position).enclosure);
						String file_name = Environment.getExternalStorageDirectory().getAbsolutePath()
								+ context.getString(R.string.activity_podcast_06) +title+"."+file_type;
						if(file_name.lastIndexOf("?") != -1){
				        	sub_file_name = file_name.replace(file_name.substring(file_name.lastIndexOf("?")), "");
				        }else{
				        	sub_file_name = file_name;
				        }
						if (SDK_INT >= Build.VERSION_CODES.M){ 
							checkPermission(context, file_name, title, enclosure, position, description_title, provider, image, pubDate, old_title);	
						}else{
							dbhelper.check_podcast_download_task(context, sub_file_name , title, enclosure, position, description_title, provider, image, pubDate, old_title, 0, -1);
						}
					}
				});
				
				dbhelper.check_podcast_continue_db(list.get(position).enclosure, holder.bt_sub_continue);
				holder.bt_sub_continue.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						dbhelper.insert_delete_podcast_continue_db(context, sub_file_name, holder.bt_sub_continue, list.get(position).title, list.get(position).enclosure, list.get(position).pubDate, list.get(position).image, list.get(position).description_title);
						datasetchanged();
					}
				});
				
				if(action_mode == true){
					if(listview_main.isItemChecked(position)){
						view.setBackgroundColor(Color.parseColor("#e6e6e6"));
					}else{
						view.setBackgroundColor(Color.parseColor("#00000000"));
					}
				}else{
					view.setBackgroundColor(Color.parseColor("#00000000"));
				}
				
			}catch (Exception e) {
			}
			return view;
		}
	}
	
	/*private void setTextViewColorPartial(TextView view, String fulltext, String subtext, int color, int default_textsize) {
		try{
			view.setText(fulltext, TextView.BufferType.SPANNABLE);
			view.setTextSize(default_textsize);
			Spannable str = (Spannable) view.getText();
			int i = fulltext.indexOf(subtext);
			str.setSpan(new ForegroundColorSpan(color), i, i + subtext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}catch (IndexOutOfBoundsException e) {
		}
	}*/
	
	public void datasetchanged(){
		if(adapter != null){
			adapter.notifyDataSetChanged();
		}
	}
	
	private void checkPermission(Context context, String sub_file_name, String title, String enclosure, int position, String description_title, String provider, String image, String pubDate, String old_title) {
	    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
	            != PackageManager.PERMISSION_GRANTED
	            || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
	            != PackageManager.PERMISSION_GRANTED) {
	        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
	        	Toast.makeText(context, context.getString(R.string.activity_podcast_14), Toast.LENGTH_LONG).show();
	        }
	        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
	                MY_PERMISSION_REQUEST_STORAGE);
	    } else {
	    	dbhelper.check_podcast_download_task(context, sub_file_name , title, enclosure, position, description_title, provider, image, pubDate, old_title, 0, -1);
	    }
	}

	private class ViewHolder {
		public TextView txt_sub_title;
		public TextView txt_sub_pubdate;
		public ImageButton bt_sub_down;
		public ImageButton bt_sub_continue;
		public ProgressBar progress_down;
	}
	
	public static void updateStatus(int position, int max, String enclosure, String description_title){
		try{
			View view = listview_main.getChildAt(position - listview_main.getFirstVisiblePosition());
		    if (view != null){
		    	progressBar = (ProgressBar)view.findViewById(R.id.progress_down);
		    	progressBar.setFocusable(false);
		    	progressBar.setProgress(max);
		    	if(description_title.equals(title)){
			    	progressBar.setVisibility(View.VISIBLE);
		    	}else{
			    	progressBar.setVisibility(View.INVISIBLE);
		    	}
		    	if (max == 100){
		    		progressBar.setVisibility(View.INVISIBLE);
		    	}
		    }
		}catch(Exception e){
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
	}	
	
	private static ProgressBar progressBar;
	private int MY_PERMISSION_REQUEST_STORAGE = 0;
	private int SDK_INT = android.os.Build.VERSION.SDK_INT;
}
