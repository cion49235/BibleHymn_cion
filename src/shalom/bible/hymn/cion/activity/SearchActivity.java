package shalom.bible.hymn.cion.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import shalom.bible.hymn.cion.MainFragmentActivity;
import shalom.bible.hymn.cion.R;
import shalom.bible.hymn.cion.common.Const;
import shalom.bible.hymn.cion.dao.Fragment_Data_Bible;
import shalom.bible.hymn.cion.db.helper.DBOpenHelper;
import shalom.bible.hymn.cion.util.PreferenceUtil;
import shalom.bible.hymn.cion.util.StringUtil;

public class SearchActivity extends SherlockActivity implements OnClickListener,OnItemClickListener, OnScrollListener, OnEditorActionListener, AdViewListener {
	private Context context;
	private final DBOpenHelper dbhelper = DBOpenHelper.getInstance();
	private ArrayList<Fragment_Data_Bible> list;
	private ArrayList<Fragment_Data_Bible> list2;
	private Cursor cursor, cursor2;
	private LinearLayout layout_nodata, layout_bottom, layout_listview_search, action_layout;
	private ListView listview_search;
	private FragmentAdapter adapter;
	private Button Bottom_01, Bottom_02, Bottom_03, bt_action1, bt_action2, bt_action3;
	private EditText edit_seacher;
	private ImageButton bt_search_result;
	private Handler handler = new Handler();
	private ImageButton btnLeft;
	private Button bt_action_mode;
	private TextView main_title;
	private boolean action_mode = false;
	private RelativeLayout ad_layout;
	private NativeExpressAdView admobNative;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMIXER, "vuh3q0an");
    	AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB, "ca-app-pub-4637651494513698/7757784963");
    	AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB_FULL, "ca-app-pub-4637651494513698/1711251362");
    	addBannerView();
//    	init_admob_naive();
		context = this;
		init_ui();
		set_titlebar();
		TextChangedListener();
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
		DeActionmode();
	}
	
	private void init_ui(){
		layout_nodata = (LinearLayout)findViewById(R.id.layout_nodata);
		layout_bottom = (LinearLayout)findViewById(R.id.layout_bottom);
		action_layout = (LinearLayout)findViewById(R.id.action_layout);
		layout_listview_search = (LinearLayout)findViewById(R.id.layout_listview_search);
		edit_seacher = (EditText)findViewById(R.id.edit_seacher);
		edit_seacher.setOnEditorActionListener(this);
		listview_search = (ListView)findViewById(R.id.listview_search);
		listview_search.setOnScrollListener(this);
		listview_search.setOnItemClickListener(this);
		listview_search.setItemsCanFocus(false);
		listview_search.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		bt_search_result = (ImageButton)findViewById(R.id.bt_search_result);
		bt_search_result.setOnClickListener(this);
		Bottom_01 = (Button)findViewById(R.id.Bottom_01);
    	Bottom_02 = (Button)findViewById(R.id.Bottom_02);
    	Bottom_03 = (Button)findViewById(R.id.Bottom_03);
    	Bottom_01.setOnClickListener(this);
    	Bottom_02.setOnClickListener(this);
    	Bottom_03.setOnClickListener(this);
    	
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
		main_title.setText(context.getString(R.string.activity_search_03));
		bt_action_mode = (Button)findViewById(R.id.bt_action_mode);
		bt_action_mode.setText(context.getString(R.string.activity_search_05));
		bt_action_mode.setOnClickListener(this);
	}
	
	private void search_action(){
		if(StringUtil.isEmpty(edit_seacher.getText().toString())){
			Toast.makeText(this, R.string.activity_search_01, Toast.LENGTH_SHORT).show();
			edit_seacher.requestFocus();
			return;
		}
		displayList();
		InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
		inputMethodManager.hideSoftInputFromWindow(edit_seacher.getWindowToken(), 0);	
	}
	
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if(actionId == EditorInfo.IME_ACTION_SEARCH){
			search_action();
		}
		return false;
	}
	
	@Override
	public void onClick(View view) {
		if(view == bt_search_result){
			search_action(); 
		}else if(view == Bottom_03){
			if(Bottom_03.isSelected()){
    			Bottom_03.setSelected(false);
    			Bottom_03.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_down, 0, 0);
    			if(autoscroll_thread != null){
    	    		autoscroll_thread.interrupt();
    	    		autoscroll_thread = null;
    	    	}
    		}else{
    			Bottom_03.setSelected(true);
    			Bottom_03.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_down_on, 0, 0);
    			if(autoscroll_thread == null) autoScrollTask();
    		}
		}else if(view == btnLeft){
			onBackPressed();
		}else if(view == bt_action_mode){
			if(bt_action_mode.isSelected()){
				bt_action_mode.setSelected(false);
				bt_action_mode.setText(context.getString(R.string.activity_search_05));
				action_mode = false;
				DeSelected();
				Toast.makeText(context, context.getString(R.string.activity_search_08), Toast.LENGTH_LONG).show();
			}else{
				bt_action_mode.setSelected(true);//액션모드
				bt_action_mode.setText(context.getString(R.string.activity_search_06));
				action_mode = true;
				Toast.makeText(context, context.getString(R.string.activity_search_07), Toast.LENGTH_LONG).show();
			}
		}else if(view == bt_action1){
			if(bt_action1.isSelected()){
				bt_action1.setSelected(false);
				bt_action1.setText(getString(R.string.frg_action_menu_1_1));
				for(int i=0; i < listview_search.getCount(); i++){
					listview_search.setItemChecked(i, false);
				}
				action_layout.setVisibility(View.GONE);
			}else{
				bt_action1.setSelected(true);
				bt_action1.setText(getString(R.string.frg_action_menu_1_2));
				for(int i=0; i < listview_search.getCount(); i++){
					listview_search.setItemChecked(i, true);
				}
			}
		}else if(view == bt_action2){
			String kwon = "";
			String jang = "";
			String jul = "";
			SparseBooleanArray sba = listview_search.getCheckedItemPositions();
			StringBuffer strBuf = new StringBuffer();
			if(sba.size() != 0){
				for(int i=0; i < listview_search.getCount(); i++){
					if(sba.get(i)){
						Fragment_Data_Bible data = (Fragment_Data_Bible)adapter.getItem(i);
						kwon = data.getKwon();
						String parse_kwon = Const.kwon_kbb_all[Integer.parseInt(kwon)-1];
						jang = data.getJang();
						jul = data.getJul();
						String content = data.getContent();
						strBuf.append(parse_kwon + jang + context.getString(R.string.txt_jang) + jul + context.getString(R.string.txt_jul) + "  " + content + " \n");
						sba = listview_search.getCheckedItemPositions();	
					}
				}
				if(dbhelper.check_note_db(strBuf) == false){
					SimpleDateFormat dateFormat = new SimpleDateFormat("y.MM.dd a h:mm:ss");  
					Date date = new Date();
					ContentValues cv = new ContentValues();
					cv.put("kwon", context.getString(R.string.activity_search_04));
					cv.put("jang", "\n"+dateFormat.format(date));
					cv.put("content", strBuf.toString() + "");
					dbhelper.insert_note_db(cv);
					dbhelper.close_note_db();
					Toast.makeText(context, R.string.frg_toast_note_1, Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(context, R.string.frg_toast_note_2, Toast.LENGTH_SHORT).show();
				}
			}
		}else if(view == bt_action3){
			String kwon = "";
			String jang = "";
			String jul = "";
			SparseBooleanArray sba = listview_search.getCheckedItemPositions();
			StringBuffer strBuf = new StringBuffer();
			if(sba.size() != 0){
				for(int i=0; i < listview_search.getCount(); i++){
					if(sba.get(i)){
						Fragment_Data_Bible data = (Fragment_Data_Bible)adapter.getItem(i);
						kwon = data.getKwon();
						String parse_kwon = Const.kwon_kbb_all[Integer.parseInt(kwon)-1];
						jang = data.getJang();
						jul = data.getJul();
						String content = data.getContent();
						strBuf.append(parse_kwon + jang + context.getString(R.string.txt_jang) + jul + context.getString(R.string.txt_jul) + "  " + content + " \n");
						sba = listview_search.getCheckedItemPositions();	
					}
				}
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");    
				intent.addCategory(Intent.CATEGORY_DEFAULT);
//				intent.putExtra(Intent.EXTRA_SUBJECT, title);
				intent.putExtra(Intent.EXTRA_TEXT, strBuf.toString() + "");
//				intent.putExtra(Intent.EXTRA_TITLE, title);
				startActivity(Intent.createChooser(intent, context.getString(R.string.frg_intent_share)));
			}
		}
	}
	
	private void DeSelected(){
		bt_action1.setSelected(false);
		bt_action1.setText(getString(R.string.frg_action_menu_1_1));
		for(int i=0; i < listview_search.getCount(); i++){
			listview_search.setItemChecked(i, false);
		}
		action_layout.setVisibility(View.GONE);
	}
	
	private void DeActionmode(){
		bt_action_mode.setSelected(false);
		bt_action_mode.setText(context.getString(R.string.activity_search_05));
		action_mode = false;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		Fragment_Data_Bible data = (Fragment_Data_Bible)adapter.getItem(position);
		String kwon = data.getKwon();
		int int_kwon = Integer.parseInt(kwon);
		String parse_kwon = Const.kwon_kbb_all[Integer.parseInt(kwon)-1];
		String jang = data.getJang();
		String jul = data.getJul();
		String content = data.getContent();
		if(action_mode == false){
			if(Integer.parseInt(kwon) < 40){
				PreferenceUtil.setStringSharedData(context, PreferenceUtil.PREF_KWON_OLD, kwon);
				PreferenceUtil.setIntSharedData(context, PreferenceUtil.PREF_KWON_OLD_WHICH, Integer.parseInt(kwon)-1);
				PreferenceUtil.setStringSharedData(context, PreferenceUtil.PREF_JANG_OLD, jang.replace(context.getString(R.string.txt_jang), ""));
				PreferenceUtil.setIntSharedData(context, PreferenceUtil.PREF_JANG_OLD_WHICH, Integer.parseInt(jang.replace(context.getString(R.string.txt_jang), ""))-1);
				Intent intent = new Intent(this, MainFragmentActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("current_page", 0);
				startActivity(intent);
			}else{
				PreferenceUtil.setStringSharedData(context, PreferenceUtil.PREF_KWON_NEW, kwon);
				PreferenceUtil.setIntSharedData(context, PreferenceUtil.PREF_KWON_NEW_WHICH, Integer.parseInt(kwon)-40);
				PreferenceUtil.setStringSharedData(context, PreferenceUtil.PREF_JANG_NEW, jang.replace(context.getString(R.string.txt_jang), ""));
				PreferenceUtil.setIntSharedData(context, PreferenceUtil.PREF_JANG_NEW_WHICH, Integer.parseInt(jang.replace(context.getString(R.string.txt_jang), ""))-1);
				Intent intent = new Intent(this, MainFragmentActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("current_page", 1);
				startActivity(intent);
			}
		}else{
			int selectd_count = 0;
	    	SparseBooleanArray sba = listview_search.getCheckedItemPositions();
			if(sba.size() != 0){
				for(int i = listview_search.getCount() -1; i>=0; i--){
					if(sba.get(i)){
						sba = listview_search.getCheckedItemPositions();
						selectd_count++;
					}
				}
			}
			if(selectd_count == 0){
				action_layout.setVisibility(View.GONE);
			}else{
				action_layout.setVisibility(View.VISIBLE);
			}
			if(adapter != null){
				adapter.notifyDataSetChanged();
			}
		}
	}
	
	Thread autoscroll_thread = null;
	void autoScrollTask() {
		(autoscroll_thread = new Thread() {			
			public void run() {
				try {
					for (;;) {
						Thread.sleep(100);
						listview_search.smoothScrollBy(3, 100);
						handler.sendEmptyMessage(0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void TextChangedListener(){
		edit_seacher.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable arg0) {
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				try {
					if(s.length() == 0){
						bt_search_result.setVisibility(View.INVISIBLE);
					}else{
						bt_search_result.setVisibility(View.VISIBLE);
					}
				} catch (Exception e) {
				}
			}
		});
	}
	
	private void displayList(){
		dbhelper.bible_db_init();
		list = new ArrayList<Fragment_Data_Bible>();
		cursor = dbhelper.mdb.rawQuery("select * from bible  WHERE content like '%' || '"+edit_seacher.getText().toString() + ""+"' || '%' Order by kwon asc, jang asc, jul asc", null);
		while(cursor.moveToNext()){
			list.add(new Fragment_Data_Bible(cursor.getString(cursor.getColumnIndex("kwon")),cursor.getString(cursor.getColumnIndex("jang")), cursor.getString(cursor.getColumnIndex("jul")), cursor.getString(cursor.getColumnIndex("content"))));
		}
		cursor.close();
		int bible_type_2 = PreferenceUtil.getIntSharedData(context, PreferenceUtil.PREF_BIBLE_TYPE_2, 0);
		if(bible_type_2 != 0){
			list2 = new ArrayList<Fragment_Data_Bible>();
			cursor2 = dbhelper.mdb2.rawQuery("select * from bible  WHERE kwon = '"+Const.kwon_old+"' and jang = '"+Const.jang_old+"'", null);
			while(cursor2.moveToNext()){
				list2.add(new Fragment_Data_Bible(cursor2.getString(cursor2.getColumnIndex("kwon")),cursor2.getString(cursor2.getColumnIndex("jang")), cursor2.getString(cursor2.getColumnIndex("jul")), cursor2.getString(cursor2.getColumnIndex("content"))));
			}
			cursor2.close();
    	}
		adapter = new FragmentAdapter(Bottom_01, Bottom_02);
		listview_search.setAdapter(adapter);
		if(listview_search.getCount() > 0){
			layout_listview_search.setVisibility(View.VISIBLE);
			layout_bottom.setVisibility(View.VISIBLE);
			layout_nodata.setVisibility(View.GONE);
			bt_action_mode.setVisibility(View.VISIBLE);
		}else{
			layout_listview_search.setVisibility(View.VISIBLE);
			layout_bottom.setVisibility(View.GONE);
			layout_nodata.setVisibility(View.VISIBLE);
			bt_action_mode.setVisibility(View.GONE);
		}
		
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == OnScrollListener.SCROLL_STATE_FLING){
			InputMethodManager inputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);  
    		inputMethodManager.hideSoftInputFromWindow(edit_seacher.getWindowToken(), 0);
    		listview_search.setFastScrollEnabled(true);
		}else{
			listview_search.setFastScrollEnabled(false);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		int count = totalItemCount - visibleItemCount;
		if(firstVisibleItem >= count && totalItemCount != 0){
			if(autoscroll_thread != null){
	    		autoscroll_thread.interrupt();
	    		autoscroll_thread = null;
	    		Bottom_03.setSelected(false);
	    		Bottom_03.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_down, 0, 0);
	    	}
		}	
	}

	public class FragmentAdapter extends BaseAdapter implements OnClickListener{
		Button btn_zoom_out, btn_zoom_in;
		public FragmentAdapter(Button bt_zoom_out, Button bt_zoom_in) {
			this.btn_zoom_out = bt_zoom_out;
			this.btn_zoom_in = bt_zoom_in;
			btn_zoom_out.setOnClickListener(this);
			btn_zoom_in.setOnClickListener(this);
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
					view = layoutInflater.inflate(R.layout.activity_search_listrow, parent, false);
					ViewHolder holder = new ViewHolder();
					holder.txt_kwon = (TextView)view.findViewById(R.id.txt_kwon);
					holder.txt_jul = (TextView)view.findViewById(R.id.txt_jul);
					holder.txt_content = (TextView)view.findViewById(R.id.txt_content);
					view.setTag(holder);
				}
				Const.default_textsize = PreferenceUtil.getIntSharedData(context, PreferenceUtil.PREF_BIBLE_TEXT_SIZE, Const.default_textsize);
				String kwon = list.get(position).getKwon();
				String parse_kwon = Const.kwon_kbb_all[Integer.parseInt(kwon)-1];
				ViewHolder holder = (ViewHolder)view.getTag();
				holder.txt_kwon.setText(parse_kwon +" "+ list.get(position).getJang()+ context.getString(R.string.txt_jang));
				holder.txt_kwon.setTextSize(Const.default_textsize);
				holder.txt_kwon.setTextColor(PreferenceUtil.getIntSharedData(context, PreferenceUtil.PREF_BIBLE_TEXT_COLOR, Const.BIBLE_TEXT_COLOR));
				holder.txt_jul.setText(list.get(position).getJul());
				holder.txt_jul.setTextSize(Const.default_textsize);
				holder.txt_jul.setTextColor(PreferenceUtil.getIntSharedData(context, PreferenceUtil.PREF_BIBLE_TEXT_COLOR, Const.BIBLE_TEXT_COLOR));
				int bible_type_2 = PreferenceUtil.getIntSharedData(context, PreferenceUtil.PREF_BIBLE_TYPE_2, 0);
				if(listview_search.isItemChecked(position)){
					view.setBackgroundColor(Color.parseColor("#e6e6e6"));
					if(bible_type_2 != 0){
						setTextViewColorPartial(holder.txt_content, list.get(position).getContent()+ "\n" + list2.get(position).getContent(),
								edit_seacher.getText().toString(), Color.RED, Const.default_textsize);
					}else{
						/*holder.txt_content.setText(list.get(position).getContent());
						holder.txt_content.setTextSize(Const.default_textsize);*/
						setTextViewColorPartial(holder.txt_content, list.get(position).getContent(),
								edit_seacher.getText().toString(), Color.RED, Const.default_textsize);
					}
				}else{
					view.setBackgroundColor(Color.parseColor("#00000000"));
					if(bible_type_2 != 0){
						setTextViewColorPartial(holder.txt_content, list.get(position).getContent()+ "\n" + list2.get(position).getContent(),
								edit_seacher.getText().toString(), Color.RED, Const.default_textsize);
						holder.txt_content.setTextColor(PreferenceUtil.getIntSharedData(context, PreferenceUtil.PREF_BIBLE_TEXT_COLOR, Const.BIBLE_TEXT_COLOR));
					}else{
						/*holder.txt_content.setText(list.get(position).getContent());
						holder.txt_content.setTextSize(Const.default_textsize);*/
						setTextViewColorPartial(holder.txt_content, list.get(position).getContent(),
								edit_seacher.getText().toString(), Color.RED, Const.default_textsize);
						holder.txt_content.setTextColor(PreferenceUtil.getIntSharedData(context, PreferenceUtil.PREF_BIBLE_TEXT_COLOR, Const.BIBLE_TEXT_COLOR));
					}
				}
				layout_listview_search.setBackgroundColor(PreferenceUtil.getIntSharedData(context, PreferenceUtil.PREF_BIBLE_BG_COLOR, Const.BIBLE_BG_COLOR));
			}catch (Exception e) {
			}
			return view;
		}
		
		private void setTextViewColorPartial(TextView view, String fulltext, String subtext, int color, int default_textsize) {
			try{
				view.setText(fulltext, TextView.BufferType.SPANNABLE);
				view.setTextSize(default_textsize);
				Spannable str = (Spannable) view.getText();
				int i = fulltext.indexOf(subtext);
				str.setSpan(new ForegroundColorSpan(color), i, i + subtext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}catch (IndexOutOfBoundsException e) {
			}
		}
		
		@Override
		public void onClick(View view ) {
			if(view == btn_zoom_out){
				Const.default_textsize--;
			}else if(view == btn_zoom_in){
				Const.default_textsize++;
			}
			PreferenceUtil.setIntSharedData(context, PreferenceUtil.PREF_BIBLE_TEXT_SIZE, Const.default_textsize);
			this.notifyDataSetInvalidated();
		}
	}

	private class ViewHolder {
		public TextView txt_kwon;
		public TextView txt_jul;
		public TextView txt_content;
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
