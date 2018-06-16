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
import com.squareup.picasso.Picasso;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import shalom.bible.hymn.cion.R;
import shalom.bible.hymn.cion.ccm.player.ContinueMediaPlayer_CCM;
import shalom.bible.hymn.cion.ccm.player.CustomVideoPlayer_CCM;
import shalom.bible.hymn.cion.common.Const;
import shalom.bible.hymn.cion.dao.Activity_Data_CCM_Favorite;
import shalom.bible.hymn.cion.db.helper.DBOpenHelper;
import shalom.bible.hymn.cion.util.AlertUtil;
import shalom.bible.hymn.cion.util.PreferenceUtil;
import shalom.bible.hymn.cion.util.RoundedTransform;

public class CCM_Activity_Favorite extends SherlockActivity implements OnClickListener,OnItemClickListener, OnScrollListener, AdViewListener {
	public static Context context;
	private final DBOpenHelper dbhelper = DBOpenHelper.getInstance();
	private ArrayList<Activity_Data_CCM_Favorite> list;
	private LinearLayout layout_listview_main, layout_nodata, action_layout;
	private ListView listview;
	private ActivityAdapter adapter;
	private ImageButton btnLeft;
	private TextView main_title;
	private Button bt_action1, bt_action2, bt_action3;
	private RelativeLayout ad_layout;
	private NativeExpressAdView admobNative;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ccm_favorite);
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
		set_titlebar();
		list = new ArrayList<Activity_Data_CCM_Favorite>();
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
	}
	
	private void init_ui(){
		layout_listview_main = (LinearLayout)findViewById(R.id.layout_listview_main);
		layout_nodata = (LinearLayout)findViewById(R.id.layout_nodata);
		action_layout = (LinearLayout)findViewById(R.id.action_layout);
		listview = (ListView)findViewById(R.id.listview);
		listview.setOnScrollListener(this);
		listview.setOnItemClickListener(this);
		listview.setItemsCanFocus(false);
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		bt_action1 = (Button)findViewById(R.id.bt_action1);
		bt_action2 = (Button)findViewById(R.id.bt_action2);
		bt_action3 = (Button)findViewById(R.id.bt_action3);
		bt_action1.setOnClickListener(this);
		bt_action2.setOnClickListener(this);
		bt_action3.setOnClickListener(this);
	}
	
	private void set_titlebar(){
		btnLeft = (ImageButton)findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(this);
		main_title = (TextView)findViewById(R.id.main_title);
		main_title.setSingleLine();
		main_title.setText(context.getString(R.string.activity_podcast_21));
		main_title.setSingleLine();
	}
	
	private void displaylist(){
		dbhelper.bind_ccm_favorite_db(list);
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
				bt_action1.setText(getString(R.string.frg_action_menu_1_1));
				for(int i=0; i < listview.getCount(); i++){
					listview.setItemChecked(i, false);
				}
				action_layout.setVisibility(View.GONE);
			}else{
				bt_action1.setSelected(true);
				bt_action1.setText(getString(R.string.frg_action_menu_1_2));
				for(int i=0; i < listview.getCount(); i++){
					listview.setItemChecked(i, true);
				}
			}
		}else if(view == bt_action2){
			SparseBooleanArray sba = listview.getCheckedItemPositions();
			ArrayList<String> array_id = new ArrayList<String>();
			ArrayList<String> array_title = new ArrayList<String>();
			ArrayList<String> array_category = new ArrayList<String>();
			ArrayList<String> array_thumbnail_hq = new ArrayList<String>();
			ArrayList<String> array_duration = new ArrayList<String>();
			if(sba.size() != 0){
				for(int i = listview.getCount() -1; i>=0; i--){
					if(sba.get(i)){
						Activity_Data_CCM_Favorite favorite_data = (Activity_Data_CCM_Favorite)adapter.getItem(i);
						String id = favorite_data.getId();
						String title = favorite_data.getTitle();
						String category = favorite_data.getCategory();
						String thumbnail_hq = favorite_data.getThumbnail_hq();
						String duration = favorite_data.getDuration();
						array_id.add(id);
						array_title.add(title);
						array_category.add(category);
						array_thumbnail_hq.add(thumbnail_hq.replace("hqdefault", "default"));
						array_duration.add(duration);
						sba = listview.getCheckedItemPositions();
					}
				}
				Intent intent = new Intent(context, ContinueMediaPlayer_CCM.class);
				intent.putExtra("array_videoid", array_id);
				intent.putExtra("array_music", array_title);
				intent.putExtra("array_artist", array_category);
				intent.putExtra("array_imageurl", array_thumbnail_hq);
				intent.putExtra("array_playtime", array_duration);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		}else if(view == bt_action3){
			SparseBooleanArray sba = listview.getCheckedItemPositions();
			ArrayList<String> array_videoid = new ArrayList<String>();
			ArrayList<String> array_subject = new ArrayList<String>();
			if(sba.size() != 0){
				for(int i = listview.getCount() -1; i>=0; i--){
					if(sba.get(i)){
						Activity_Data_CCM_Favorite favorite_data = (Activity_Data_CCM_Favorite)adapter.getItem(i);
						String videoid = favorite_data.getId();
						String subject = favorite_data.getTitle();
						array_videoid.add(videoid);
						array_subject.add(subject);
						sba = listview.getCheckedItemPositions();
					}
				}
				Intent intent = new Intent(context, CustomVideoPlayer_CCM.class);
				intent.putExtra("array_videoid", array_videoid);
				intent.putExtra("array_subject", array_subject);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
			}
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
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
		datasetchanged();
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
					view = layoutInflater.inflate(R.layout.activity_ccm_favorite_listrow, parent, false);
					ViewHolder holder = new ViewHolder();
					holder.img_imageurl = (ImageView)view.findViewById(R.id.img_imageurl);
					holder.txt_title = (TextView)view.findViewById(R.id.txt_title);
					holder.bt_favorite_del = (ImageButton)view.findViewById(R.id.bt_favorite_del);
					holder.bt_favorite_del.setFocusable(false);
					holder.bt_favorite_del.setSelected(false);
					view.setTag(holder);
				}
				
				final ViewHolder holder = (ViewHolder)view.getTag();
				
				BitmapFactory.Options dimensions = new BitmapFactory.Options(); 
				dimensions.inJustDecodeBounds = true;
				Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_app, dimensions);
				        int height = dimensions.outHeight;
				        int width =  dimensions.outWidth;
				Picasso.with(context)
			    .load(list.get(position).getThumbnail_hq())
			    .transform(new RoundedTransform())
			    .resize(width, height )
			    .placeholder(R.drawable.no_image)
			    .error(R.drawable.no_image)
			    .into(holder.img_imageurl);
				
				holder.txt_title.setText(list.get(position).getTitle());
				holder.bt_favorite_del.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						AlertShow_CCM_Activity_Favorite(context.getString(R.string.frg_ccm_21), list.get(position).get_id());
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
	
	public void AlertShow_CCM_Activity_Favorite(String msg, final int id) {
        AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(context);
        alert_internet_status.setCancelable(true);
        alert_internet_status.setMessage(msg);
        alert_internet_status.setPositiveButton(context.getString(R.string.frg_ccm_19),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    	Toast.makeText(context, context.getString(R.string.frg_ccm_22), Toast.LENGTH_SHORT).show();
						dbhelper.delete_ccm_favorite_db(id);
						list = new ArrayList<Activity_Data_CCM_Favorite>();
						list.clear();
						displaylist();
                    }
                });
        alert_internet_status.setNegativeButton(context.getString(R.string.frg_ccm_20), 
       		 new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
        alert_internet_status.show();
	}
	
	private class ViewHolder {
		public ImageView img_imageurl;
		public TextView txt_title;
		public ImageButton bt_favorite_del;
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
	
	private void datasetchanged(){
		if(adapter != null){
			adapter.notifyDataSetChanged();
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
}
