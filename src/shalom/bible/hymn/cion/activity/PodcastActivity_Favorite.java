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
import android.media.AudioManager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
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
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import shalom.bible.hymn.cion.R;
import shalom.bible.hymn.cion.common.Const;
import shalom.bible.hymn.cion.dao.Activity_Data_Podcast_Favorite;
import shalom.bible.hymn.cion.db.helper.DBOpenHelper;
import shalom.bible.hymn.cion.util.NetworkHelper;
import shalom.bible.hymn.cion.util.PreferenceUtil;
import shalom.bible.hymn.cion.util.RoundedTransform;

public class PodcastActivity_Favorite extends SherlockActivity implements OnClickListener,OnItemClickListener, OnScrollListener, AdViewListener {
	public static Context context;
	private final DBOpenHelper dbhelper = DBOpenHelper.getInstance();
	private ArrayList<Activity_Data_Podcast_Favorite> list;
	private LinearLayout layout_listview_main, layout_nodata;
	private GridView gridview;
	private ActivityAdapter adapter;
	private ImageButton btnLeft, btn_download;
	private TextView main_title;
	private final NetworkHelper mNetHelper = NetworkHelper.getInstance();
	private RelativeLayout ad_layout;
	private NativeExpressAdView admobNative;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_podcast_favorite);
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
		list = new ArrayList<Activity_Data_Podcast_Favorite>();
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
		gridview = (GridView)findViewById(R.id.gridview);
		gridview.setOnScrollListener(this);
		gridview.setOnItemClickListener(this);
		btn_download = (ImageButton)findViewById(R.id.btn_download);
		btn_download.setOnClickListener(this);
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
		dbhelper.bind_podcast_favorite_db(list);
		adapter = new ActivityAdapter();
		gridview.setAdapter(adapter);
		
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
		}else if(view == btn_download){
			
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		if(!mNetHelper.is3GConnected() && !mNetHelper.isWIFIConneced()){
			Toast.makeText(context, context.getString(R.string.download_data_connection_ment), Toast.LENGTH_LONG).show();
			return;
		}
		
		Activity_Data_Podcast_Favorite data_favorite = (Activity_Data_Podcast_Favorite)adapter.getItem(position);
		String id = data_favorite.getId();
		String num = data_favorite.getNum();
		String title = data_favorite.getTitle();
		String provider = data_favorite.getProvider();
		String imageurl = data_favorite.getImageurl();
		String rssurl = data_favorite.getRssurl();
		String udate = data_favorite.getUdate();
		
		Intent intent = new Intent(context, PodcastActivity_Sub.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("id", id);
		intent.putExtra("num", num);
		intent.putExtra("title", title);
		intent.putExtra("provider", provider);
		intent.putExtra("imageurl", imageurl);
		intent.putExtra("rssurl", rssurl);
		intent.putExtra("udate", udate);
		startActivity(intent);
	}
	
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == OnScrollListener.SCROLL_STATE_FLING){
			gridview.setFastScrollEnabled(true);
		}else{
			gridview.setFastScrollEnabled(false);
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
					view = layoutInflater.inflate(R.layout.activity_podcast_favorite_listrow, parent, false);
					ViewHolder holder = new ViewHolder();
					holder.img_imageurl = (ImageView)view.findViewById(R.id.img_imageurl);
					holder.txt_title = (TextView)view.findViewById(R.id.txt_title);
					holder.txt_provider = (TextView)view.findViewById(R.id.txt_provider);
					holder.bt_favorite_del = (Button)view.findViewById(R.id.bt_favorite_del);
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
			    .load(list.get(position).getImageurl())
			    .transform(new RoundedTransform())
			    .resize(width, height )
			    .placeholder(R.drawable.no_image)
			    .error(R.drawable.no_image)
			    .into(holder.img_imageurl);
				
				holder.txt_title.setText(list.get(position).getTitle());
				holder.txt_provider.setText(list.get(position).getProvider());
				holder.bt_favorite_del.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View view) {
						AlertShow_PodcastActivity_Favorite(context.getString(R.string.frg_podcast_14), list.get(position).get_id());
					}
				});
			}catch (Exception e) {
			}
			return view;
		}
	}
	
	public void AlertShow_PodcastActivity_Favorite(String msg, final int id) {
        AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(context);
        alert_internet_status.setCancelable(true);
        alert_internet_status.setMessage(msg);
        alert_internet_status.setPositiveButton(context.getString(R.string.frg_podcast_12),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    	Toast.makeText(context, context.getString(R.string.frg_podcast_15), Toast.LENGTH_SHORT).show();
						dbhelper.delete_podcast_favorite_db(id);
						list = new ArrayList<Activity_Data_Podcast_Favorite>();
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
		public ImageView img_imageurl;
		public TextView txt_title;
		public TextView txt_provider;
		public Button bt_favorite_del;
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
