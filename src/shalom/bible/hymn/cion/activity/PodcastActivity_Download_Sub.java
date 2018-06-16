package shalom.bible.hymn.cion.activity;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.admixer.AdAdapter;
import com.admixer.AdInfo;
import com.admixer.AdMixerManager;
import com.admixer.AdView;
import com.admixer.AdViewListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import shalom.bible.hymn.cion.R;
import shalom.bible.hymn.cion.common.Const;
import shalom.bible.hymn.cion.dao.Fragment_Data_Podcast_Download;
import shalom.bible.hymn.cion.db.helper.DBOpenHelper;
import shalom.bible.hymn.cion.podcast.mediaplayer.ContinueMediaPlayer_Podcast;
import shalom.bible.hymn.cion.util.PreferenceUtil;
import shalom.bible.hymn.cion.util.StringUtil;

public class PodcastActivity_Download_Sub extends SherlockActivity implements OnClickListener,OnItemClickListener, OnScrollListener, AdViewListener {
	public static Context context;
	private final DBOpenHelper dbhelper = DBOpenHelper.getInstance();
	private ArrayList<Fragment_Data_Podcast_Download> list;
	private LinearLayout layout_listview_main, layout_nodata, action_layout;
	private ListView listview;
	private ActivityAdapter adapter;
	private ImageButton btnLeft;
	private TextView main_title;
	private String description_title;
	private Button bt_action1, bt_action2;
	private boolean action_mode = false;
	private Button bt_action_mode;
	private RelativeLayout ad_layout;
	private NativeExpressAdView admobNative;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_podcast_download_sub);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		context = this;
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMIXER, "vuh3q0an");
    	AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB, "ca-app-pub-4637651494513698/7757784963");
    	AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB_FULL, "ca-app-pub-4637651494513698/1711251362");
    	if(!PreferenceUtil.getStringSharedData(context, PreferenceUtil.PREF_ISSUBSCRIBED, Const.isSubscribed).equals("true")){
        	addBannerView();    		
    	}
//    	init_admob_naive();
		init_ui();
		get_data();
		set_titlebar();
		list = new ArrayList<Fragment_Data_Podcast_Download>();
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
		action_mode = false;
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		adapter_datasetchanged();
	}
	
	private void init_ui(){
		layout_listview_main = (LinearLayout)findViewById(R.id.layout_listview_main);
		action_layout = (LinearLayout)findViewById(R.id.action_layout);
		layout_nodata = (LinearLayout)findViewById(R.id.layout_nodata);
		listview = (ListView)findViewById(R.id.listview);
		listview.setOnScrollListener(this);
		listview.setOnItemClickListener(this);
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listview.setItemsCanFocus(false);
		
		bt_action1 = (Button)findViewById(R.id.bt_action1);
    	bt_action2 = (Button)findViewById(R.id.bt_action2);
    	bt_action1.setOnClickListener(this);
    	bt_action2.setOnClickListener(this);
	}
	
	private void get_data(){
		description_title = getIntent().getStringExtra("description_title");
	}
	
	private void set_titlebar(){
		btnLeft = (ImageButton)findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(this);
		main_title = (TextView)findViewById(R.id.main_title);
		main_title.setTextSize(12);
		main_title.setSingleLine();
		main_title.setText(description_title);
		main_title.setSingleLine();
		
		bt_action_mode = (Button)findViewById(R.id.bt_action_mode);
		bt_action_mode.setText(context.getString(R.string.activity_podcast_25));
		bt_action_mode.setVisibility(View.VISIBLE);
		bt_action_mode.setOnClickListener(this);
	}
	
	private void displaylist(){
		dbhelper.bind_podcast_download_sub_db(list, description_title);
		adapter = new ActivityAdapter();
		listview.setAdapter(adapter);
		
		if(adapter.getCount() > 0){
			layout_nodata.setVisibility(View.GONE);
		}else{
			layout_nodata.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void onClick(View view) {
		if(view == btnLeft){
			onBackPressed();
		}else if(view == bt_action1){
			if(bt_action1.isSelected()){
				bt_action1.setSelected(false);
				bt_action1.setText(getString(R.string.activity_podcast_18_1));
				for(int i=0; i < listview.getCount(); i++){
					listview.setItemChecked(i, false);
				}
				action_layout.setVisibility(View.GONE);
			}else{
				bt_action1.setSelected(true);
				bt_action1.setText(getString(R.string.activity_podcast_18_2));
				for(int i=0; i < listview.getCount(); i++){
					listview.setItemChecked(i, true);
				}
			}
		}else if(view == bt_action2){
			SparseBooleanArray sba = listview.getCheckedItemPositions();
			ArrayList<String> array_title = new ArrayList<String>();
			ArrayList<String> array_enclosure = new ArrayList<String>();
			ArrayList<String> array_pubDate = new ArrayList<String>();
			ArrayList<String> array_image = new ArrayList<String>();
			ArrayList<String> array_description_title = new ArrayList<String>();
			if(sba.size() != 0){
				for(int i = listview.getCount() -1; i>=0; i--){
					if(sba.get(i)){
						Fragment_Data_Podcast_Download download_data = (Fragment_Data_Podcast_Download)adapter.getItem(i);
						String title = download_data.getTitle();
						String enclosure = download_data.getEnclosure();
						String pubDate = StringUtil.setDateTrim(download_data.getPubDate());
						String image = download_data.getImage();
						String description_title = download_data.getDescription_title();
						
						array_title.add(title);
						array_enclosure.add(enclosure);
						array_pubDate.add(pubDate);
						array_image.add(image);
						array_description_title.add(description_title);
						sba = listview.getCheckedItemPositions();
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
		}
	}
	
	private void DeSelected(){
		bt_action1.setSelected(false);
		bt_action1.setText(getString(R.string.activity_podcast_18_1));
		for(int i=0; i < listview.getCount(); i++){
			listview.setItemChecked(i, false);
		}
		action_layout.setVisibility(View.GONE);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		if(action_mode == true){
			int selectd_count = 0;
	    	SparseBooleanArray sba = listview.getCheckedItemPositions();
			if(sba.size() != 0){
				for(int i = listview.getCount() -1; i>=0; i--){
					if(sba.get(i)){
						sba = listview.getCheckedItemPositions();
						selectd_count++;
					}
				}
			}
			if(selectd_count == 0){
				action_layout.setVisibility(View.GONE);
			}else{
				action_layout.setVisibility(View.VISIBLE);
			}
			adapter_datasetchanged();
		}else{
			Fragment_Data_Podcast_Download data_download = (Fragment_Data_Podcast_Download)adapter.getItem(position);
			String sub_file_name;
			String old_title = data_download.getTitle();
			String title = data_download.getTitle().replaceAll("\\p{Punct}", "") + "";
			String enclosure = data_download.getEnclosure() + "";
			String pubDate = StringUtil.setDateTrim(data_download.getPubDate() + "");
			String description_title = data_download.getDescription_title() + "";
			String provider = data_download.getProvider() + "";
			String image = data_download.getImage() + "";
			String file_type = StringUtil.getExtension(data_download.getEnclosure());
			String file_name = Environment.getExternalStorageDirectory().getAbsolutePath()
					+ context.getString(R.string.activity_podcast_06) +title+"."+file_type;
			if(file_name.lastIndexOf("?") != -1){
	        	sub_file_name = file_name.replace(file_name.substring(file_name.lastIndexOf("?")), "");
	        }else{
	        	sub_file_name = file_name;
	        }
			dbhelper.check_podcast_download_task(context, sub_file_name, title, enclosure, position, description_title, provider, image, pubDate, old_title, 1, list.get(position).get_id());
		}
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == OnScrollListener.SCROLL_STATE_FLING){
			listview.setFastScrollEnabled(true);
		}else{
			listview.setFastScrollEnabled(false);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
	}

	
	public class ActivityAdapter extends BaseAdapter{
		String sub_file_name;
		String old_title;
		String title;
		String enclosure;
		String pubDate;
		String description_title;
		String provider;
		String image;
		String file_type;
		String file_name;
		public ActivityAdapter() {
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
					view = layoutInflater.inflate(R.layout.activity_podcast_download_sub_listrow, parent, false);
					ViewHolder holder = new ViewHolder();
					holder.txt_sub_downtitle = (TextView)view.findViewById(R.id.txt_sub_downtitle);
					holder.txt_sub_downpubdate = (TextView)view.findViewById(R.id.txt_sub_downpubdate);
					holder.bt_sub_download_play = (ImageButton)view.findViewById(R.id.bt_sub_download_play);
					holder.bt_sub_download_play.setFocusable(false);
					holder.bt_sub_download_play.setSelected(false);
					holder.bt_sub_download_delete = (ImageButton)view.findViewById(R.id.bt_sub_download_delete);
					holder.bt_sub_download_delete.setFocusable(false);
					holder.bt_sub_download_delete.setSelected(false);
					view.setTag(holder);
				}
				final ViewHolder holder = (ViewHolder)view.getTag();
				
				holder.txt_sub_downtitle.setText(list.get(position).getTitle());
				if(list.get(position).getTitle().equals(PreferenceUtil.getStringSharedData(context, PreferenceUtil.PREF_PODCAST_TITLE, "")) && StringUtil.setDateTrim(list.get(position).getPubDate()).equals(PreferenceUtil.getStringSharedData(context, PreferenceUtil.PREF_PODCAST_PUBDATE, ""))){
					holder.txt_sub_downtitle.setTextColor(Color.RED);	
				}else{
					holder.txt_sub_downtitle.setTextColor(Color.parseColor("#b3000000"));
				}
				holder.txt_sub_downpubdate.setText(list.get(position).getPubDate());
				holder.bt_sub_download_play.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						old_title = list.get(position).getTitle();
						title = list.get(position).getTitle().replaceAll("\\p{Punct}", "") + "";
						enclosure = list.get(position).getEnclosure() + "";
						pubDate = StringUtil.setDateTrim(list.get(position).getPubDate() + "");
						description_title = list.get(position).getDescription_title() + "";
						provider = list.get(position).getProvider() + "";
						image = list.get(position).getImage() + "";
						file_type = StringUtil.getExtension(list.get(position).getEnclosure());
						file_name = Environment.getExternalStorageDirectory().getAbsolutePath()
								+ context.getString(R.string.activity_podcast_06) +title+"."+file_type;
						if(file_name.lastIndexOf("?") != -1){
				        	sub_file_name = file_name.replace(file_name.substring(file_name.lastIndexOf("?")), "");
				        }else{
				        	sub_file_name = file_name;
				        }
						
						dbhelper.check_podcast_download_task(context, sub_file_name, title, enclosure, position, description_title, provider, image, pubDate, old_title, 1, list.get(position).get_id());
					}
				});
				
				holder.bt_sub_download_delete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						old_title = list.get(position).getTitle();
						title = list.get(position).getTitle().replaceAll("\\p{Punct}", "") + "";
						enclosure = list.get(position).getEnclosure() + "";
						pubDate = StringUtil.setDateTrim(list.get(position).getPubDate() + "");
						description_title = list.get(position).getDescription_title() + "";
						provider = list.get(position).getProvider() + "";
						image = list.get(position).getImage() + "";
						file_type = StringUtil.getExtension(list.get(position).getEnclosure());
						file_name = Environment.getExternalStorageDirectory().getAbsolutePath()
								+ context.getString(R.string.activity_podcast_06) +title+"."+file_type;
						if(file_name.lastIndexOf("?") != -1){
				        	sub_file_name = file_name.replace(file_name.substring(file_name.lastIndexOf("?")), "");
				        }else{
				        	sub_file_name = file_name;
				        }
						AlertShow_PodcastActivity_Download_Sub(context.getString(R.string.frg_podcast_14), list.get(position).get_id(), sub_file_name);
					}
				});
				
				if(listview.isItemChecked(position)){
					view.setBackgroundColor(Color.parseColor("#e6e6e6"));
				}else{
					view.setBackgroundColor(Color.parseColor("#00000000"));
				}
				        
			}catch (Exception e) {
			}
			return view;
		}
	}
	
	public void AlertShow_PodcastActivity_Download_Sub(String msg, final int id, final String sub_file_name) {
        AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(context);
        alert_internet_status.setCancelable(true);
        alert_internet_status.setMessage(msg);
        alert_internet_status.setPositiveButton(context.getString(R.string.frg_podcast_12),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    	Toast.makeText(context, context.getString(R.string.activity_podcast_24), Toast.LENGTH_SHORT).show();
						dbhelper.delete_podcast_download_db(id);
						if(shalom.bible.hymn.cion.util.Utils.file_check(sub_file_name) == true){
							shalom.bible.hymn.cion.util.Utils.file_delete(sub_file_name);	
						}
						list = new ArrayList<Fragment_Data_Podcast_Download>();
						list.clear();
						displaylist();
                    }
                });
        alert_internet_status.setNegativeButton(context.getString(R.string.frg_podcast_13), 
       		 new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
        alert_internet_status.show();
	}
	
	
	private class ViewHolder {
		public TextView txt_sub_downtitle;
		public TextView txt_sub_downpubdate;
		public ImageButton bt_sub_download_play;
		public ImageButton bt_sub_download_delete;
		
	}
	
	private void setTextViewColorPartial(TextView view, String fulltext, String subtext, int color) {
		try{
			view.setText(fulltext, TextView.BufferType.SPANNABLE);
			Spannable str = (Spannable) view.getText();
			int i = fulltext.indexOf(subtext);
			str.setSpan(new ForegroundColorSpan(color), i, i + subtext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}catch (IndexOutOfBoundsException e) {
		}
	}
	
	public void adapter_datasetchanged(){
		if(adapter != null){
			adapter.notifyDataSetChanged();
		}
	}
	
	public void datasetchanged(){
		list = new ArrayList<Fragment_Data_Podcast_Download>();
		list.clear();
		displaylist();
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
}
