package shalom.bible.hymn.cion.fragment;



import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import shalom.bible.hymn.cion.R;
import shalom.bible.hymn.cion.activity.CCM_Activity_Favorite;
import shalom.bible.hymn.cion.activity.PodcastActivity_Favorite;
import shalom.bible.hymn.cion.ccm.player.ContinueMediaPlayer_CCM;
import shalom.bible.hymn.cion.ccm.player.CustomVideoPlayer_CCM;
import shalom.bible.hymn.cion.dao.Fragment_Data_CCM;
import shalom.bible.hymn.cion.db.helper.DBOpenHelper;
import shalom.bible.hymn.cion.util.Crypto;
import shalom.bible.hymn.cion.util.KoreanTextMatch;
import shalom.bible.hymn.cion.util.KoreanTextMatcher;
import shalom.bible.hymn.cion.util.RoundedTransform;
import shalom.bible.hymn.cion.util.StringUtil;
import shalom.bible.hymn.cion.util.Utils;
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
import android.util.Log;
import android.util.SparseBooleanArray;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class Fragment_CCM extends Fragment implements OnClickListener, OnItemClickListener, OnScrollListener, OnEditorActionListener{
	private LinearLayout action_layout;
	private EditText edit_searcher;
	private ArrayList<Fragment_Data_CCM> list;
	private FragmentAdapter adapter;
	private ListView listview;
	private LinearLayout layout_nodata, layout_progress;
	private String searchKeyword;
	private ImageButton btn_close;
	private Button btn_favorite;
	private KoreanTextMatch match1, match2;
	private String view_num;
	private Main_ParseAsync main_parseAsync = null;
	private boolean retry_alert = false;
	private Button bt_action1, bt_action2, bt_action3;
	private final DBOpenHelper dbhelper = DBOpenHelper.getInstance();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_ccm,container, false);
		init_ui(view);
		view_num = getActivity().getString(R.string.frg_ccm_01);
		list = new ArrayList<Fragment_Data_CCM>();
		list.clear();
		retry_alert = true;
		display_list();
		seacher_start();
		return view;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		retry_alert = false;
		view_num = getActivity().getString(R.string.frg_ccm_01);
		if(main_parseAsync != null){
			main_parseAsync.cancel(true);
		}
		edit_searcher.setText("");
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
		list = new ArrayList<Fragment_Data_CCM>();
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
						btn_favorite.setVisibility(View.VISIBLE);
					}else{
						btn_close.setVisibility(View.VISIBLE);
						btn_favorite.setVisibility(View.INVISIBLE);
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
		Fragment_Data_CCM data_main;
		ArrayList<Fragment_Data_CCM> menuItems = new ArrayList<Fragment_Data_CCM>();
		String Response;
		String i;
		int _id;
		String id;
		String title;
		String thumbnail_hq;
		String sprit_title[];
		public Main_ParseAsync(){
		}
			@Override
			protected String doInBackground(String... params) {
				String sTag;
				try{
				   String data = Crypto.decrypt(Utils.data, getActivity().getString(R.string.frg_ccm_02));
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
			        			data_main = new Fragment_Data_CCM();
			        			_id = Integer.parseInt(xpp.getAttributeValue(null, "id") + "");
			            	}else if(sTag.equals("videoid")){
			        			Response = xpp.nextText()+"";
			            	}else if(sTag.equals("subject")){
			            		title = xpp.nextText()+"";
			            		sprit_title = title.split("-");
//			            		Log.i("dsu", "sprit_title : " + title);
			            	}else if(sTag.equals("thumb")){
			            		thumbnail_hq = xpp.nextText()+"";
			            	}
			        	} else if (eventType == XmlPullParser.END_TAG){
			            	sTag = xpp.getName();
			            	if(sTag.equals("Content")){
			            		data_main._id = _id;
			            		data_main.id = Response;
			            		data_main.title = title;
			            		data_main.category = getActivity().getString(R.string.app_name);
			            		data_main.thumbnail_hq = thumbnail_hq;
			            		if(searchKeyword != null && "".equals(searchKeyword.trim()) == false){
			            			KoreanTextMatcher matcher1 = new KoreanTextMatcher(searchKeyword.toLowerCase());
			        				KoreanTextMatcher matcher2 = new KoreanTextMatcher(searchKeyword.toUpperCase());
			            			match1 = matcher1.match(data_main.title);
			            			match2 = matcher2.match(data_main.title);
			            			if(match1.success()) {
			            				list.add(data_main);
			            			}else if(match2.success()) {
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
				 return Response;
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
								listview.setAdapter(adapter);
								if(listview.getCount() == 0){
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
		action_layout = (LinearLayout)view.findViewById(R.id.action_layout);
		edit_searcher = (EditText)view.findViewById(R.id.edit_searcher);
		edit_searcher.setOnEditorActionListener(this);
		btn_close = (ImageButton)view.findViewById(R.id.btn_close);
		btn_close.setOnClickListener(this);
		btn_favorite = (Button)view.findViewById(R.id.btn_favorite);
		btn_favorite.setOnClickListener(this);
		listview = (ListView)view.findViewById(R.id.listview);
		listview.setOnScrollListener(this);
		listview.setOnItemClickListener(this);
		listview.setItemsCanFocus(false);
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		bt_action1 = (Button)view.findViewById(R.id.bt_action1);
		bt_action2 = (Button)view.findViewById(R.id.bt_action2);
		bt_action3 = (Button)view.findViewById(R.id.bt_action3);
		bt_action1.setOnClickListener(this);
		bt_action2.setOnClickListener(this);
		bt_action3.setOnClickListener(this);
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
					view = layoutInflater.inflate(R.layout.fragment_ccm_listrow, parent, false);
					ViewHolder holder = new ViewHolder();					
					holder.img_imageurl = (ImageView)view.findViewById(R.id.img_imageurl);
					holder.txt_title = (TextView)view.findViewById(R.id.txt_title);
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
			    .load(list.get(position).thumbnail_hq)
			    .transform(new RoundedTransform())
			    .resize(width, height )
			    .placeholder(R.drawable.no_image)
			    .error(R.drawable.no_image)
			    .into(holder.img_imageurl);
				
				if(edit_searcher.getText().toString().length() > 0){
					setTextViewColorPartial(holder.txt_title, list.get(position).title, searchKeyword, Color.RED);
				}else{
					holder.txt_title.setText(list.get(position).title);
				}
				Collections.sort(list, new Fragment_Data_CCM());
				
				dbhelper.check_ccm_favorite_db(getActivity(), list.get(position).id, holder.bt_favorite);
				holder.bt_favorite.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						dbhelper.insert_delete_ccm_favorite_db(getActivity(), list.get(position).id, list.get(position).title, list.get(position).category, list.get(position).thumbnail_hq, list.get(position).thumbnail_hq, holder.bt_favorite);
						datasetchanged();
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
		
		private class ViewHolder {
			public ImageView img_imageurl;
			public TextView txt_title;
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
		list = new ArrayList<Fragment_Data_CCM>();
		list.clear();
		display_list();
		InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);  
		inputMethodManager.hideSoftInputFromWindow(edit_searcher.getWindowToken(), 0);
	}

	@Override
	public void onClick(View view) {
		if(view  == btn_close){
			cleardata();
		}else if(view == btn_favorite){
			intent_favorite();
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
						Fragment_Data_CCM main_data = (Fragment_Data_CCM)adapter.getItem(i);
						String id = main_data.id;
						String title = main_data.title;
						String category = main_data.category;
						String thumbnail_hq = main_data.thumbnail_hq;
						String duration = main_data.duration;
						array_id.add(id);
						array_title.add(title);
						array_category.add(category);
						array_thumbnail_hq.add(thumbnail_hq.replace("hqdefault", "default"));
						array_duration.add(duration);
						sba = listview.getCheckedItemPositions();
					}
				}
				Intent intent = new Intent(getActivity(), ContinueMediaPlayer_CCM.class);
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
						Fragment_Data_CCM main_data = (Fragment_Data_CCM)adapter.getItem(i);
						String videoid = main_data.id;
						String subject = main_data.title;
						array_videoid.add(videoid);
						array_subject.add(subject);
						sba = listview.getCheckedItemPositions();
					}
				}
				Intent intent = new Intent(getActivity(), CustomVideoPlayer_CCM.class);
				intent.putExtra("array_videoid", array_videoid);
				intent.putExtra("array_subject", array_subject);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
			}
		}
	}
	
	private void intent_favorite(){
		Intent intent = new Intent(getActivity(), CCM_Activity_Favorite.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long _id) {
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
			InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);  
    		inputMethodManager.hideSoftInputFromWindow(edit_searcher.getWindowToken(), 0);
    		listview.setFastScrollEnabled(true);
		}else{
			listview.setFastScrollEnabled(false);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		
	}
}
