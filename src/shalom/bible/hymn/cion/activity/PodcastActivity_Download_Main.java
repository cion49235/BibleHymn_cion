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

import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import shalom.bible.hymn.cion.R;
import shalom.bible.hymn.cion.common.Const;
import shalom.bible.hymn.cion.dao.Fragment_Data_Podcast_Download;
import shalom.bible.hymn.cion.db.helper.DBOpenHelper;
import shalom.bible.hymn.cion.util.PreferenceUtil;
import shalom.bible.hymn.cion.util.RoundedTransform;

public class PodcastActivity_Download_Main extends SherlockActivity implements OnClickListener,OnItemClickListener, OnScrollListener, AdViewListener {
	public static Context context;
	private final DBOpenHelper dbhelper = DBOpenHelper.getInstance();
	private ArrayList<Fragment_Data_Podcast_Download> list;
	private LinearLayout layout_listview_main, layout_nodata;
	private ListView listview;
	private ActivityAdapter adapter;
	private ImageButton btnLeft;
	private TextView main_title;
	private RelativeLayout ad_layout;
	private NativeExpressAdView admobNative;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_podcast_download_main);
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
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		datasetchanged();
	}
	
	private void init_ui(){
		layout_listview_main = (LinearLayout)findViewById(R.id.layout_listview_main);
		layout_nodata = (LinearLayout)findViewById(R.id.layout_nodata);
		listview = (ListView)findViewById(R.id.listview);
		listview.setOnScrollListener(this);
		listview.setOnItemClickListener(this);
		listview.setItemsCanFocus(false);
	}
	
	private void set_titlebar(){
		btnLeft = (ImageButton)findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(this);
		main_title = (TextView)findViewById(R.id.main_title);
		main_title.setTextSize(12);
		main_title.setSingleLine();
		main_title.setText(context.getString(R.string.frg_podcast_10));
		main_title.setSingleLine();
	}
	
	private void displaylist(){
		dbhelper.bind_podcast_download_main_db(list);
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
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		Fragment_Data_Podcast_Download data_download = (Fragment_Data_Podcast_Download)adapter.getItem(position);
		String title = data_download.getTitle();
		String enclosure = data_download.getEnclosure();
		String pubDate = data_download.getPubDate();
		String image = data_download.getImage();
		String description_title = data_download.getDescription_title();
		String provider = data_download.getProvider();

		Intent intent = new Intent(context, PodcastActivity_Download_Sub.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("title", title);
		intent.putExtra("enclosure", enclosure);
		intent.putExtra("pubDate", pubDate);
		intent.putExtra("image", image);
		intent.putExtra("description_title", description_title);
		intent.putExtra("provider", provider);
		startActivity(intent);
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
					view = layoutInflater.inflate(R.layout.activity_podcast_download_main_listrow, parent, false);
					ViewHolder holder = new ViewHolder();
					holder.img_imageurl = (ImageView)view.findViewById(R.id.img_imageurl);
					holder.txt_main_downtitle = (TextView)view.findViewById(R.id.txt_main_downtitle);
					holder.txt_main_provider = (TextView)view.findViewById(R.id.txt_main_provider);
					view.setTag(holder);
				}
				
				final ViewHolder holder = (ViewHolder)view.getTag();
				
				BitmapFactory.Options dimensions = new BitmapFactory.Options(); 
				dimensions.inJustDecodeBounds = true;
				Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_app, dimensions);
				        int height = dimensions.outHeight;
				        int width =  dimensions.outWidth;
				        
				Picasso.with(context)
			    .load(list.get(position).getImage())
			    .transform(new RoundedTransform())
			    .resize(width, height )
			    .placeholder(R.drawable.no_image)
			    .error(R.drawable.no_image)
			    .into(holder.img_imageurl);
				
				holder.txt_main_downtitle.setText(list.get(position).getTitle());
				holder.txt_main_provider.setText(list.get(position).getProvider());
			}catch (Exception e) {
			}
			return view;
		}
	}
	
	private class ViewHolder {
		public ImageView img_imageurl;
		public TextView txt_main_downtitle;
		public TextView txt_main_provider;
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
