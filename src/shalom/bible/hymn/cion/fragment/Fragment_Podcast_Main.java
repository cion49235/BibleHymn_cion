package shalom.bible.hymn.cion.fragment;



import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.squareup.picasso.Picasso;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import shalom.bible.hymn.cion.MainFragmentActivity;
import shalom.bible.hymn.cion.R;
import shalom.bible.hymn.cion.activity.PodcastActivity_Download_Main;
import shalom.bible.hymn.cion.activity.PodcastActivity_Favorite;
import shalom.bible.hymn.cion.activity.PodcastActivity_Sub;
import shalom.bible.hymn.cion.common.Const;
import shalom.bible.hymn.cion.dao.Activity_Data_Podcast_Favorite;
import shalom.bible.hymn.cion.dao.Activity_Data_Podcast_Sub;
import shalom.bible.hymn.cion.dao.Fragment_Data_Podcast_Main;
import shalom.bible.hymn.cion.db.helper.DBOpenHelper;
import shalom.bible.hymn.cion.util.Crypto;
import shalom.bible.hymn.cion.util.KoreanTextMatch;
import shalom.bible.hymn.cion.util.KoreanTextMatcher;
import shalom.bible.hymn.cion.util.RoundedTransform;
import shalom.bible.hymn.cion.util.StringUtil;
import shalom.bible.hymn.cion.util.Utils;

public class Fragment_Podcast_Main extends Fragment implements OnClickListener, OnItemClickListener, OnScrollListener, OnEditorActionListener{
	private final DBOpenHelper dbhelper = DBOpenHelper.getInstance();
	private EditText edit_searcher;
	private ArrayList<Fragment_Data_Podcast_Main> list;
	private FragmentAdapter adapter;
	private GridView gridview;
	private LinearLayout layout_nodata, layout_progress;
	private String searchKeyword;
	private ImageButton btn_close;
	private Button btn_intent;
	private KoreanTextMatch match1, match2, match3, match4;
	private String view_num;
	private Main_ParseAsync main_parseAsync = null;
	private boolean retry_alert = false;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_podcast_main,container, false);
		init_ui(view);
		view_num = getActivity().getString(R.string.frg_podcast_08);
		list = new ArrayList<Fragment_Data_Podcast_Main>();
		list.clear();
		retry_alert = true;
		display_list();
		seacher_start();
		return view;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		try{
			retry_alert = false;
			view_num = getActivity().getString(R.string.frg_podcast_08);
			if(main_parseAsync != null){
				main_parseAsync.cancel(true);
			}
			if(edit_searcher != null){
			edit_searcher.setText("");
			}
		}catch (NullPointerException e) {
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		datasetchanged();
	}
	
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if(actionId == EditorInfo.IME_ACTION_SEARCH){
			search_action();
		}
		return false;
	}
	
	private void search_action(){
		if(StringUtil.isEmpty(edit_searcher.getText().toString())){
			Toast.makeText(getActivity(), R.string.activity_search_01, Toast.LENGTH_SHORT).show();
			edit_searcher.requestFocus();
			return;
		}
		list = new ArrayList<Fragment_Data_Podcast_Main>();
		list.clear();
		display_list();
		InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);  
		inputMethodManager.hideSoftInputFromWindow(edit_searcher.getWindowToken(), 0);	
	}
	
	private void seacher_start(){
		edit_searcher.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable arg0) {
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				try {
					searchKeyword = s.toString();
					if(s.length() == 0){
						btn_close.setVisibility(View.INVISIBLE);
						btn_intent.setVisibility(View.VISIBLE);
					}else{
						btn_close.setVisibility(View.VISIBLE);
						btn_intent.setVisibility(View.INVISIBLE);
					}
				} catch (Exception e) {
				}
			}
		});
	}
	
	private void display_list(){
		main_parseAsync = new Main_ParseAsync();
		main_parseAsync.execute();
	}
	
	public class Main_ParseAsync extends AsyncTask<String, Integer, String>{
		Fragment_Data_Podcast_Main data_main;
		ArrayList<Fragment_Data_Podcast_Main> menuItems = new ArrayList<Fragment_Data_Podcast_Main>();
		String i;
		String id;
		String num;
		String title;
		String provider;
		String imageurl;
		String rssurl;
		String udate;
		String sprit_title[];
		public Main_ParseAsync(){
		}
			@Override
			protected String doInBackground(String... params) {
				String sTag;
				try{
				   String data = Crypto.decrypt(Utils.data, getActivity().getString(R.string.frg_podcast_01));
		           String str = data+i+".php?view="+view_num;
//		           Log.i("dsu", "str : " + str);
		           HttpURLConnection localHttpURLConnection = (HttpURLConnection)new URL(str).openConnection();
		           HttpURLConnection.setFollowRedirects(false);
		           localHttpURLConnection.setConnectTimeout(15000);
		           localHttpURLConnection.setReadTimeout(15000); 
		           localHttpURLConnection.setRequestMethod("GET");
		           localHttpURLConnection.connect();
		           InputStream inputStream = new URL(str).openStream(); //open Stream을 사용하여 InputStream을 생성합니다.
		           XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); 
		           XmlPullParser xpp = factory.newPullParser();
		           xpp.setInput(inputStream, "EUC-KR"); //euc-kr로 언어를 설정합니다. utf-8로 하니깐 깨지더군요
		           int eventType = xpp.getEventType();
		           while (eventType != XmlPullParser.END_DOCUMENT) {
			        	if (eventType == XmlPullParser.START_DOCUMENT) {
			        	}else if (eventType == XmlPullParser.END_DOCUMENT) {
			        	}else if (eventType == XmlPullParser.START_TAG){
			        		sTag = xpp.getName();
			        		if(sTag.equals("Content")){
			        			data_main = new Fragment_Data_Podcast_Main();
			            	}else if(sTag.equals("id")){
			            		id = xpp.nextText()+"";
			            	}else if(sTag.equals("num")){
			            		num = xpp.nextText()+"";
			            	}else if(sTag.equals("title")){
			            		title = xpp.nextText()+"";
			            	}else if(sTag.equals("provider")){
			            		provider = xpp.nextText()+"";
			            	}else if(sTag.equals("imageurl")){
			            		imageurl = xpp.nextText()+"";
			            	}else if(sTag.equals("rssurl")){
			            		rssurl = xpp.nextText()+"";
			            	}else if(sTag.equals("udate")){
			            		udate = xpp.nextText()+"";
			            	}
			        	} else if (eventType == XmlPullParser.END_TAG){
			            	sTag = xpp.getName();
			            	if(sTag.equals("Content")){
			            		data_main.id = id;
			            		data_main.num = num;
			            		data_main.title = title;
			            		data_main.provider = provider;
			            		data_main.imageurl = imageurl;
			            		data_main.rssurl = rssurl;
			            		data_main.udate = udate;
			            		if(searchKeyword != null && "".equals(searchKeyword.trim()) == false){
			            			KoreanTextMatcher matcher1 = new KoreanTextMatcher(searchKeyword.toLowerCase());
			        				KoreanTextMatcher matcher2 = new KoreanTextMatcher(searchKeyword.toUpperCase());
			        				KoreanTextMatcher matcher3 = new KoreanTextMatcher(searchKeyword.toLowerCase());
			        				KoreanTextMatcher matcher4 = new KoreanTextMatcher(searchKeyword.toUpperCase());
			            			match1 = matcher1.match(data_main.title);
			            			match2 = matcher2.match(data_main.title);
			            			match3 = matcher3.match(data_main.provider);
			            			match4 = matcher4.match(data_main.provider);
			            			if(match1.success()) {
			            				list.add(data_main);
			            			}else if(match2.success()) {
				            			list.add(data_main);
			            			}else if(match3.success()) {
				            			list.add(data_main);
			            			}else if(match4.success()) {
				            			list.add(data_main);
			            			}
			            		}else{
			            			list.add(data_main);
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
		         catch (NullPointerException NullPointerException)
		         {
		         }
				 catch (JSONException e) 
				 {
				 } 
				 catch (Exception e) 
				 {
				 }
				 return rssurl;
			}
			
			@Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            i="2";
	            layout_progress.setVisibility(View.VISIBLE);
	        }
			@Override
			protected void onPostExecute(String Response) {
				super.onPostExecute(Response);
				layout_progress.setVisibility(View.GONE);
				try{
					if(Response != null){
						for(int i=0;; i++){
							if(i >= list.size()){
//							while (i > list.size()-1){
								adapter = new FragmentAdapter();
								gridview.setAdapter(adapter);
								if(gridview.getCount() == 0){
									layout_nodata.setVisibility(View.VISIBLE);
								}else{
									layout_nodata.setVisibility(View.GONE);
								}
								return;
							}
							menuItems.add(list.get(i));
						}
					}else{
						layout_nodata.setVisibility(View.VISIBLE);
						Retry_AlertShow(getActivity().getString(R.string.frg_podcast_02));
					}
				}catch(NullPointerException e){
				}
			}
			@Override
			protected void onProgressUpdate(Integer... values) {
				super.onProgressUpdate(values);
			}
		}
	
	public void Retry_AlertShow(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setCancelable(false);
		builder.setMessage(msg);
		builder.setNeutralButton(getActivity().getString(R.string.frg_podcast_03), new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){
				display_list();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton(getActivity().getString(R.string.frg_podcast_04), new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){
				dialog.dismiss();
			}
		});
		AlertDialog myAlertDialog = builder.create();
		if(retry_alert) myAlertDialog.show();
	}
	
	private void init_ui(View view){
		layout_progress = (LinearLayout)view.findViewById(R.id.layout_progress);
		layout_nodata = (LinearLayout)view.findViewById(R.id.layout_nodata);
		edit_searcher = (EditText)view.findViewById(R.id.edit_searcher);
		edit_searcher.setOnEditorActionListener(this);
		btn_close = (ImageButton)view.findViewById(R.id.btn_close);
		btn_close.setOnClickListener(this);
		btn_intent = (Button)view.findViewById(R.id.btn_intent);
		btn_intent.setOnClickListener(this);
		gridview = (GridView)view.findViewById(R.id.gridview);
		gridview.setOnScrollListener(this);
		gridview.setOnItemClickListener(this);
	}
	
	public class FragmentAdapter extends BaseAdapter{
		public FragmentAdapter() {
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
					LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
					view = layoutInflater.inflate(R.layout.fragment_podcast_main_listrow, parent, false);
					ViewHolder holder = new ViewHolder();
					holder.img_imageurl = (ImageView)view.findViewById(R.id.img_imageurl);
					holder.txt_title = (TextView)view.findViewById(R.id.txt_title);
					holder.txt_provider = (TextView)view.findViewById(R.id.txt_provider);
					holder.bt_favorite = (Button)view.findViewById(R.id.bt_favorite);
					holder.bt_favorite.setFocusable(false);
					holder.bt_favorite.setSelected(false);
					view.setTag(holder);
				}
				
				final ViewHolder holder = (ViewHolder)view.getTag();
				
				BitmapFactory.Options dimensions = new BitmapFactory.Options(); 
				dimensions.inJustDecodeBounds = true;
				Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ic_app, dimensions);
				        int height = dimensions.outHeight;
				        int width =  dimensions.outWidth;
				Picasso.with(getActivity())
			    .load(list.get(position).imageurl)
			    .transform(new RoundedTransform())
			    .resize(width, height )
			    .placeholder(R.drawable.no_image)
			    .error(R.drawable.no_image)
			    .into(holder.img_imageurl);
				if(edit_searcher.getText().toString().length() > 0){
					setTextViewColorPartial(holder.txt_title, list.get(position).title, searchKeyword, Color.RED);
					setTextViewColorPartial(holder.txt_provider, list.get(position).provider, searchKeyword, Color.RED);
				}else{
					holder.txt_title.setText(list.get(position).title);
					holder.txt_provider.setText(list.get(position).provider);					
				}
				
				dbhelper.check_podcast_main_favorite_db(list.get(position).num, holder.bt_favorite);
				holder.bt_favorite.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						dbhelper.insert_delete_podcast_main_favorite_db(list.get(position).id, list.get(position).num, list.get(position).title, list.get(position).provider, list.get(position).imageurl, list.get(position).rssurl, list.get(position).udate, holder.bt_favorite);
						datasetchanged();
					}
				});
			}catch (Exception e) {
			}
			return view;
		}
		
		private class ViewHolder {
			public ImageView img_imageurl;
			public TextView txt_title;
			public TextView txt_provider;
			public Button bt_favorite;
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
	}
	
	private void datasetchanged(){
		if(adapter != null){
			adapter.notifyDataSetChanged();
		}
	}
	
	private void cleardata(){
		edit_searcher.setText("");
		list = new ArrayList<Fragment_Data_Podcast_Main>();
		list.clear();
		display_list();
		InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);  
		inputMethodManager.hideSoftInputFromWindow(edit_searcher.getWindowToken(), 0);
	}

	@Override
	public void onClick(View view) {
		if(view  == btn_close){
			cleardata();
		}else if(view == btn_intent){
			alert_intent();
		}
	}
	
	private void alert_intent(){
		AlertDialog.Builder alert_dialog= new AlertDialog.Builder(getActivity());
        alert_dialog.setTitle(getActivity().getString(R.string.frg_podcast_11));
        alert_dialog.setItems(Const.intent_alert_podcast, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	if(which == 0){
            		intent_favorite();		
				}else if(which == 1){
					intent_download();
				}
            }
        });alert_dialog.show();
	}
	
	private void intent_download(){
		Intent intent = new Intent(getActivity(), PodcastActivity_Download_Main.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	private void intent_favorite(){
		Intent intent = new Intent(getActivity(), PodcastActivity_Favorite.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long _id) {
		Fragment_Data_Podcast_Main data_main = (Fragment_Data_Podcast_Main)adapter.getItem(position);
		String id = data_main.id;
		String num = data_main.num;
		String title = data_main.title;
		String provider = data_main.provider;
		String imageurl = data_main.imageurl;
		String rssurl = data_main.rssurl;
		String udate = data_main.udate;
		
		Intent intent = new Intent(getActivity(), PodcastActivity_Sub.class);
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
			InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);  
    		inputMethodManager.hideSoftInputFromWindow(edit_searcher.getWindowToken(), 0);
    		gridview.setFastScrollEnabled(true);
		}else{
			gridview.setFastScrollEnabled(false);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		
	}
}
