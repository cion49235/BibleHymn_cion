package shalom.bible.hymn.cion.fragment;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.admixer.AdInfo;
import com.admixer.InterstitialAd;
import com.admixer.InterstitialAdListener;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import shalom.bible.hymn.cion.MainFragmentActivity;
import shalom.bible.hymn.cion.R;
import shalom.bible.hymn.cion.common.Const;
import shalom.bible.hymn.cion.dao.Fragment_Data_Bible;
import shalom.bible.hymn.cion.db.helper.DBOpenHelper;
import shalom.bible.hymn.cion.util.NetworkHelper;
import shalom.bible.hymn.cion.util.PreferenceUtil;
import shalom.bible.hymn.cion.util.Utils;

public class Fragment_NewBible extends Fragment implements OnClickListener, OnItemClickListener, OnScrollListener, InterstitialAdListener{
	private Cursor cursor, cursor2;
	private ArrayList<Fragment_Data_Bible> list;
	private ArrayList<Fragment_Data_Bible> list2;
	private FragmentAdapter adapter;
	private ListView listview;
	private LinearLayout layout_nodata, action_layout;
	private final DBOpenHelper dbhelper = DBOpenHelper.getInstance();
	private Button Bottom_01, Bottom_02, Bottom_03, Bottom_04, Bottom_05, Bottom_06, Bottom_07;
	
	private ImageButton ibt_kwon_new, ibt_jang_new;
	private Button bt_action1, bt_action2, bt_action3;
	private int kwon_new_which = 0;
	private int jang_new_which = 0;
	private Handler handler = new Handler();
	public static LinearLayout layout_listview_main;
	private NetworkHelper mNetHelper = NetworkHelper.getInstance();
	private com.admixer.InterstitialAd interstialAd;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_new_bible,container, false);
		init_ui(view);
		display_list();
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
		bt_action1_deselect();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		dbhelper.close_bible_db();
	}
	
	public void bt_action1_deselect(){
		if(bt_action1.isSelected()){
			bt_action1.setSelected(false);
			bt_action1.setText(getString(R.string.frg_action_menu_1_1));
			for(int i=0; i < listview.getCount(); i++){
				listview.setItemChecked(i, false);
			}
			action_layout.setVisibility(View.GONE);
		}
	}

	public void display_list(){
		try {
			dbhelper.bible_db_init();
			list = new ArrayList<Fragment_Data_Bible>();
			cursor = dbhelper.mdb.rawQuery("select * from bible  WHERE kwon = '"+Const.kwon_new+"' and jang = '"+Const.jang_new+"'", null);
			while(cursor.moveToNext()){
				list.add(new Fragment_Data_Bible(cursor.getString(cursor.getColumnIndex("kwon")),cursor.getString(cursor.getColumnIndex("jang")), cursor.getString(cursor.getColumnIndex("jul")), cursor.getString(cursor.getColumnIndex("content"))));
			}
			cursor.close();
			int bible_type_2 = PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_BIBLE_TYPE_2, 0);
			if(bible_type_2 != 0){
				list2 = new ArrayList<Fragment_Data_Bible>();
				cursor2 = dbhelper.mdb2.rawQuery("select * from bible  WHERE kwon = '"+Const.kwon_new+"' and jang = '"+Const.jang_new+"'", null);
				while(cursor2.moveToNext()){
					list2.add(new Fragment_Data_Bible(cursor2.getString(cursor2.getColumnIndex("kwon")),cursor2.getString(cursor2.getColumnIndex("jang")), cursor2.getString(cursor2.getColumnIndex("jul")), cursor2.getString(cursor2.getColumnIndex("content"))));
				}
				cursor2.close();
	    	}
			
			adapter = new FragmentAdapter(Bottom_01, Bottom_02);
			listview.setAdapter(adapter);
			dbhelper.check_bible_bookmark_db(getActivity(), Const.kwon_new, Const.jang_new, Bottom_07);
			if(listview.getCount() > 0){
				layout_nodata.setVisibility(View.GONE);
			}else{
				layout_nodata.setVisibility(View.VISIBLE);
			}
		}catch (NullPointerException e) {
		}
	}
	
	public void init_ui(View view){
		layout_nodata = (LinearLayout)view.findViewById(R.id.layout_nodata);
		layout_listview_main = (LinearLayout)view.findViewById(R.id.layout_listview_main);
		listview = (ListView)view.findViewById(R.id.listview);
		listview.setOnScrollListener(this);
		listview.setOnItemClickListener(this);
		listview.setItemsCanFocus(false);
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		/*
		*하단메뉴	
		*/
		Bottom_01 = (Button)view.findViewById(R.id.Bottom_01);
    	Bottom_02 = (Button)view.findViewById(R.id.Bottom_02);
    	Bottom_03 = (Button)view.findViewById(R.id.Bottom_03);
    	Bottom_04 = (Button)view.findViewById(R.id.Bottom_04);
    	Bottom_05 = (Button)view.findViewById(R.id.Bottom_05);
    	Bottom_06 = (Button)view.findViewById(R.id.Bottom_06);
    	Bottom_07 = (Button)view.findViewById(R.id.Bottom_07);
    	Bottom_01.setOnClickListener(this);
    	Bottom_02.setOnClickListener(this);
    	Bottom_03.setOnClickListener(this);
    	Bottom_04.setOnClickListener(this);
    	Bottom_05.setOnClickListener(this);
    	Bottom_06.setOnClickListener(this);
    	Bottom_07.setOnClickListener(this);
    	
    	/*
		*아이템클릭메뉴	
		*/
    	action_layout = (LinearLayout)view.findViewById(R.id.action_layout);
    	bt_action1 = (Button)view.findViewById(R.id.bt_action1);
    	bt_action2 = (Button)view.findViewById(R.id.bt_action2);
    	bt_action3 = (Button)view.findViewById(R.id.bt_action3);
    	bt_action1.setOnClickListener(this);
    	bt_action2.setOnClickListener(this);
    	bt_action3.setOnClickListener(this);

    	Const.bt_kwon_new = (Button)view.findViewById(R.id.bt_kwon_new);
    	Const.bt_jang_new = (Button)view.findViewById(R.id.bt_jang_new);
    	Const.bt_kwon_new.setOnClickListener(this);
    	Const.bt_jang_new.setOnClickListener(this);
    	ibt_kwon_new = (ImageButton)view.findViewById(R.id.ibt_kwon_new);
    	ibt_jang_new = (ImageButton)view.findViewById(R.id.ibt_jang_new);
    	ibt_kwon_new.setOnClickListener(this);
    	ibt_jang_new.setOnClickListener(this);
    	/*
		*성경봉독	
		*/
    	Const.kwon_new = PreferenceUtil.getStringSharedData(getActivity(), PreferenceUtil.PREF_KWON_NEW, Const.kwon_new);
    	Const.jang_new = PreferenceUtil.getStringSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
    	kwon_new_which = PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_KWON_NEW_WHICH, 0);
    	jang_new_which = PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, 0);
    	Const.bt_kwon_new.setText(Const.kwon_kbb_new[kwon_new_which]);
    	Const.bt_jang_new.setText(Const.jang_kbb_new[jang_new_which] + getActivity().getString(R.string.txt_jang));
    	
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
					LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
					view = layoutInflater.inflate(R.layout.fragment_new_bible_listrow, parent, false);
					ViewHolder holder = new ViewHolder();
					holder.txt_jul = (TextView)view.findViewById(R.id.txt_jul);
					holder.txt_content = (TextView)view.findViewById(R.id.txt_content);
					view.setTag(holder);
				}
				Const.default_textsize = PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_BIBLE_TEXT_SIZE, Const.default_textsize);
				ViewHolder holder = (ViewHolder)view.getTag();
				holder.txt_jul.setText(list.get(position).getJul());
				holder.txt_jul.setTextSize(Const.default_textsize);
				holder.txt_jul.setTextColor(PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_BIBLE_TEXT_COLOR, Const.BIBLE_TEXT_COLOR));
				int bible_type_2 = PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_BIBLE_TYPE_2, 0);
				if(listview.isItemChecked(position)){
					view.setBackgroundColor(Color.parseColor("#e6e6e6"));
					if(bible_type_2 != 0){
						setTextViewColorPartial(holder.txt_content, list.get(position).getContent() + "\n" + list2.get(position).getContent(),
								list2.get(position).getContent(), PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_BIBLE_TEXT_COLOR, Const.BIBLE_TEXT_COLOR), Const.default_textsize);
					}else{
						holder.txt_content.setText(list.get(position).getContent());
						holder.txt_content.setTextSize(Const.default_textsize);
					}
				}else{
					view.setBackgroundColor(Color.parseColor("#00000000"));
					if(bible_type_2 != 0){
						setTextViewColorPartial(holder.txt_content, list.get(position).getContent() + "\n" + list2.get(position).getContent(),
								list2.get(position).getContent(), PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_BIBLE_TEXT_COLOR2, Const.BIBLE_TEXT_COLOR2), Const.default_textsize);
						holder.txt_content.setTextColor(PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_BIBLE_TEXT_COLOR, Const.BIBLE_TEXT_COLOR));
					}else{
						holder.txt_content.setText(list.get(position).getContent());
						holder.txt_content.setTextSize(Const.default_textsize);
						holder.txt_content.setTextColor(PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_BIBLE_TEXT_COLOR, Const.BIBLE_TEXT_COLOR));
					}
				}
				layout_listview_main.setBackgroundColor(PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_BIBLE_BG_COLOR, Const.BIBLE_BG_COLOR));
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
			PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_BIBLE_TEXT_SIZE, Const.default_textsize);
			this.notifyDataSetInvalidated();
		}
	}
	
	private class ViewHolder {
      public TextView txt_jul;
      public TextView txt_content;
  }
	

	@Override
	public void onClick(View view) {
		if(view == Bottom_03){
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
		}else if(view == Bottom_04){
			if(Const.jang_new.equals("1")){
				Toast.makeText(getActivity(), R.string.frg_new_bible_1, Toast.LENGTH_SHORT).show();
				return;
			}else{
				try{
					((MainFragmentActivity) getActivity()).voice_play_stop();
				}catch(Exception e){
				}
				int pre_num = Integer.parseInt(Const.jang_new)-1;
				Const.jang_new = Integer.toString(pre_num);
				display_list();
				Const.bt_jang_new.setText(Const.jang_new + getActivity().getString(R.string.txt_jang));
				PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, pre_num-1);
				PreferenceUtil.setStringSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
			}
		}else if(view == Bottom_05){
			for(int i=40; i < Const.kwon_kbb_new.length+40; i++){
				if(Const.kwon_new.equals(Integer.toString(i))){
					if(Integer.parseInt(Const.jang_new) > Const.jang_page_max_new()-1 ){
						Toast.makeText(getActivity(), R.string.frg_new_bible_1, Toast.LENGTH_SHORT).show();
						return;
					}else{
						try{
							((MainFragmentActivity) getActivity()).voice_play_stop();
						}catch(Exception e){
						}
						int next_num = Integer.parseInt(Const.jang_new)+1;
						Const.jang_new = Integer.toString(next_num);
						display_list();
						Const.bt_jang_new.setText(Const.jang_new + getActivity().getString(R.string.txt_jang));
						PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, next_num-1);
						PreferenceUtil.setStringSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
					}
				}
			}
		}else if(view == Bottom_06){
			if(Utils.language(getActivity()).equals("ko_KR")){
				AlertDialog.Builder alert_dialog= new AlertDialog.Builder(getActivity());
	            alert_dialog.setTitle(getActivity().getString(R.string.txt_setting_alert_title));
	            alert_dialog.setItems(Const.setting_alert, new DialogInterface.OnClickListener() {
	                @Override
	                public void onClick(DialogInterface dialog, int which) {
	                	if(which == 0){//성경1선택
							((MainFragmentActivity)getActivity()).alert_bible_type(0);
						}else if(which == 1){//성경2선택
							((MainFragmentActivity)getActivity()).alert_bible_type(1);
						}else if(which == 2){//읽기속도
							((MainFragmentActivity)getActivity()).alert_audio_speed();
						}else if(which == 3){//글자색상
							((MainFragmentActivity)getActivity()).alert_select_text();
						}else if(which == 4){//배경색상
							((MainFragmentActivity)getActivity()).alert_bg_color_picker(true);
						}else if(which == 5){//성경검색
							((MainFragmentActivity)getActivity()).intent_search();
						}else if(which == 6){//문의하기
							((MainFragmentActivity)getActivity()).intent_question_webview();
						}else if(which == 7){//후원하기
							if(!PreferenceUtil.getStringSharedData(getActivity(), PreferenceUtil.PREF_ISSUBSCRIBED, Const.isSubscribed).equals("true")){
								addInterstitialView();
								Toast.makeText(getActivity(), getActivity().getString(R.string.toast_ad), Toast.LENGTH_LONG).show();								
							}
						}else if(which == 8){//이앱전도
							((MainFragmentActivity)getActivity()).intent_app_share();
						}else if(which == 9){//Swipe Mode
							((MainFragmentActivity)getActivity()).alert_swipe_mode();
						}else if(which == 10){//스크롤 속도
							((MainFragmentActivity)getActivity()).alert_scroll_speed();
						}
	                }
	            });alert_dialog.show();
			}else{
				AlertDialog.Builder alert_dialog= new AlertDialog.Builder(getActivity());
	            alert_dialog.setTitle(getActivity().getString(R.string.txt_setting_alert_title));
	            alert_dialog.setItems(Const.setting_alert_en, new DialogInterface.OnClickListener() {
	                @Override
	                public void onClick(DialogInterface dialog, int which) {
	                	if(which == 0){
							((MainFragmentActivity)getActivity()).alert_bible_type(0);
						}else if(which == 1){
							((MainFragmentActivity)getActivity()).alert_bible_type(1);
						}else if(which == 2){
							((MainFragmentActivity)getActivity()).alert_select_text();
						}else if(which == 3){
							((MainFragmentActivity)getActivity()).alert_bg_color_picker(true);
						}else if(which == 4){
							((MainFragmentActivity)getActivity()).intent_search();
						}else if(which == 5){
							((MainFragmentActivity)getActivity()).intent_question_webview();
						}else if(which == 6){
							if(!PreferenceUtil.getStringSharedData(getActivity(), PreferenceUtil.PREF_ISSUBSCRIBED, Const.isSubscribed).equals("true")){
								addInterstitialView();	
							}
							Toast.makeText(getActivity(), getActivity().getString(R.string.toast_ad), Toast.LENGTH_LONG).show();
						}else if(which == 7){
							((MainFragmentActivity)getActivity()).intent_app_share();
						}else if(which == 8){
							((MainFragmentActivity)getActivity()).alert_swipe_mode();
						}else if(which == 9){//스크롤 속도
							((MainFragmentActivity)getActivity()).alert_scroll_speed();
						}
	                }
	            });alert_dialog.show();
			}
		}else if(view == Bottom_07){//책갈피
			dbhelper.insert_delete_bible_bookmark_db(getActivity(), Const.kwon_new, Const.jang_new, Bottom_07);
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
			StringBuffer strBuf = new StringBuffer();
			if(sba.size() != 0){
				for(int i=0; i < listview.getCount(); i++){
					if(sba.get(i)){
						Fragment_Data_Bible data = (Fragment_Data_Bible)adapter.getItem(i);
						if(PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_BIBLE_TYPE_2, 0) != 0){
							String bible_data2 = list2.get(i).getContent();
							String content = data.getContent() + "\n" + bible_data2;
							String jul = data.getJul();
							strBuf.append(jul + " " + content);
							sba = listview.getCheckedItemPositions();
						}else{
							String content = data.getContent();
							String jul = data.getJul();
							strBuf.append(jul + " " + content);
							sba = listview.getCheckedItemPositions();
						}
					}
				}
				if(dbhelper.check_note_db(strBuf) == false){
					SimpleDateFormat dateFormat = new SimpleDateFormat("y.MM.dd a h:mm:ss");  
					Date date = new Date();
					ContentValues cv = new ContentValues();
					cv.put("kwon", Const.bt_kwon_new.getText().toString() + "" + " " + Const.bt_jang_new.getText().toString());
					cv.put("jang", "\n"+dateFormat.format(date));
					cv.put("content", strBuf.toString() + "");
					dbhelper.insert_note_db(cv);
					dbhelper.close_note_db();
					Toast.makeText(getActivity(), R.string.frg_toast_note_1, Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(getActivity(), R.string.frg_toast_note_2, Toast.LENGTH_SHORT).show();
				}
			}
		}else if(view == bt_action3){
			SparseBooleanArray sba = listview.getCheckedItemPositions();
			StringBuffer strBuf = new StringBuffer();
			if(sba.size() != 0){
				for(int i=0; i < listview.getCount(); i++){
					if(sba.get(i)){
						Fragment_Data_Bible data = (Fragment_Data_Bible)adapter.getItem(i);
						if(PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_BIBLE_TYPE_2, 0) != 0){
							String bible_data2 = list2.get(i).getContent();
							String content = data.getContent() + "\n" + bible_data2;
							String jul = data.getJul();
							strBuf.append(jul + " " + content);
							sba = listview.getCheckedItemPositions();
						}else{
							String content = data.getContent();
							String jul = data.getJul();
							strBuf.append(jul + " " + content);
							sba = listview.getCheckedItemPositions();
						}
					}
				}
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");    
				intent.addCategory(Intent.CATEGORY_DEFAULT);
//				intent.putExtra(Intent.EXTRA_SUBJECT, title);
				intent.putExtra(Intent.EXTRA_TEXT, Const.bt_kwon_new.getText().toString() + "" + " " + Const.bt_jang_new.getText().toString() + "" + "\n" +strBuf.toString() + "");
//				intent.putExtra(Intent.EXTRA_TITLE, title);
				startActivity(Intent.createChooser(intent, getActivity().getString(R.string.frg_intent_share)));
			}
		}else if(view == Const.bt_kwon_new){
			alert_kwon();
		}else if(view == Const.bt_jang_new){
			alert_jang();
		}else if(view == ibt_kwon_new){
			alert_kwon();
		}else if(view == ibt_jang_new){
			alert_jang();
		}
	}
	
	public void DeSelected(){
		bt_action1.setSelected(false);
		bt_action1.setText(getString(R.string.frg_action_menu_1_1));
		for(int i=0; i < listview.getCount(); i++){
			listview.setItemChecked(i, false);
		}
		action_layout.setVisibility(View.GONE);
	}
	
	private void alert_kwon(){
		new AlertDialog.Builder(getActivity())
		.setTitle(getActivity().getString(R.string.txt_bt_kwon2))
		.setSingleChoiceItems(Const.kwon_kbb_new, PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_KWON_NEW_WHICH, 0), new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which){
				try{
					((MainFragmentActivity) getActivity()).voice_play_stop();
				}catch(Exception e){
				}
				for(int i=0; i < Const.kwon_kbb_new.length; i++){
					if(which == i){
						Const.kwon_new = Integer.toString(39+i+1);
						PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_KWON_NEW_WHICH, i);
						PreferenceUtil.setStringSharedData(getActivity(), PreferenceUtil.PREF_KWON_NEW, Const.kwon_new);
					}
				}
				
				Const.bt_kwon_new.setText(Const.kwon_kbb_new[which]);
				Const.bt_jang_new.setText(Const.jang_kbb_new[0] + getActivity().getString(R.string.txt_jang));
				Const.jang_new = "1";
				PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, 0);
				PreferenceUtil.setStringSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
				display_list();
				dialog.dismiss();
			}
		}).show();
	}
	
	private void alert_jang(){
		new AlertDialog.Builder(getActivity())
		.setSingleChoiceItems(Const.jang_page_num_new(), PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, 0), new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which){
				if(Const.kwon_new.equals("40")){
					for(int i=0; i < Const.jang_kbb40.length; i++){
						if(which == i){
							Const.jang_new = Integer.toString(i+1);
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, i);
							PreferenceUtil.setStringSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
						}
					}
				}else if(Const.kwon_new.equals("41")){
					for(int i=0; i < Const.jang_kbb41.length; i++){
						if(which == i){
							Const.jang_new = Integer.toString(i+1);
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, i);
							PreferenceUtil.setStringSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
						}
					}
				}else if(Const.kwon_new.equals("42")){
					for(int i=0; i < Const.jang_kbb42.length; i++){
						if(which == i){
							Const.jang_new = Integer.toString(i+1);
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, i);
							PreferenceUtil.setStringSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
						}
					}
				}else if(Const.kwon_new.equals("43")){
					for(int i=0; i < Const.jang_kbb43.length; i++){
						if(which == i){
							Const.jang_new = Integer.toString(i+1);
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, i);
							PreferenceUtil.setStringSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
						}
					}
				}else if(Const.kwon_new.equals("44")){
					for(int i=0; i < Const.jang_kbb44.length; i++){
						if(which == i){
							Const.jang_new = Integer.toString(i+1);
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, i);
							PreferenceUtil.setStringSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
						}
					}
				}else if(Const.kwon_new.equals("45")){
					for(int i=0; i < Const.jang_kbb45.length; i++){
						if(which == i){
							Const.jang_new = Integer.toString(i+1);
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, i);
							PreferenceUtil.setStringSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
						}
					}
				}else if(Const.kwon_new.equals("46")){
					for(int i=0; i < Const.jang_kbb46.length; i++){
						if(which == i){
							Const.jang_new = Integer.toString(i+1);
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, i);
							PreferenceUtil.setStringSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
						}
					}
				}else if(Const.kwon_new.equals("47")){
					for(int i=0; i < Const.jang_kbb47.length; i++){
						if(which == i){
							Const.jang_new = Integer.toString(i+1);
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, i);
							PreferenceUtil.setStringSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
						}
					}
				}else if(Const.kwon_new.equals("48")){
					for(int i=0; i < Const.jang_kbb48.length; i++){
						if(which == i){
							Const.jang_new = Integer.toString(i+1);
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, i);
							PreferenceUtil.setStringSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
						}
					}
				}else if(Const.kwon_new.equals("49")){
					for(int i=0; i < Const.jang_kbb49.length; i++){
						if(which == i){
							Const.jang_new = Integer.toString(i+1);
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, i);
							PreferenceUtil.setStringSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
						}
					}
				}else if(Const.kwon_new.equals("50")){
					for(int i=0; i < Const.jang_kbb50.length; i++){
						if(which == i){
							Const.jang_new = Integer.toString(i+1);
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, i);
							PreferenceUtil.setStringSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
						}
					}
				}else if(Const.kwon_new.equals("51")){
					for(int i=0; i < Const.jang_kbb51.length; i++){
						if(which == i){
							Const.jang_new = Integer.toString(i+1);
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, i);
							PreferenceUtil.setStringSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
						}
					}
				}else if(Const.kwon_new.equals("52")){
					for(int i=0; i < Const.jang_kbb52.length; i++){
						if(which == i){
							Const.jang_new = Integer.toString(i+1);
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, i);
							PreferenceUtil.setStringSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
						}
					}
				}else if(Const.kwon_new.equals("53")){
					for(int i=0; i < Const.jang_kbb53.length; i++){
						if(which == i){
							Const.jang_new = Integer.toString(i+1);
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, i);
							PreferenceUtil.setStringSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
						}
					}
				}else if(Const.kwon_new.equals("54")){
					for(int i=0; i < Const.jang_kbb54.length; i++){
						if(which == i){
							Const.jang_new = Integer.toString(i+1);
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, i);
							PreferenceUtil.setStringSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
						}
					}
				}else if(Const.kwon_new.equals("55")){
					for(int i=0; i < Const.jang_kbb55.length; i++){
						if(which == i){
							Const.jang_new = Integer.toString(i+1);
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, i);
							PreferenceUtil.setStringSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
						}
					}
				}else if(Const.kwon_new.equals("56")){
					for(int i=0; i < Const.jang_kbb56.length; i++){
						if(which == i){
							Const.jang_new = Integer.toString(i+1);
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, i);
							PreferenceUtil.setStringSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
						}
					}
				}else if(Const.kwon_new.equals("57")){
					for(int i=0; i < Const.jang_kbb57.length; i++){
						if(which == i){
							Const.jang_new = Integer.toString(i+1);
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, i);
							PreferenceUtil.setStringSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
						}
					}
				}else if(Const.kwon_new.equals("58")){
					for(int i=0; i < Const.jang_kbb58.length; i++){
						if(which == i){
							Const.jang_new = Integer.toString(i+1);
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, i);
							PreferenceUtil.setStringSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
						}
					}
				}else if(Const.kwon_new.equals("59")){
					for(int i=0; i < Const.jang_kbb59.length; i++){
						if(which == i){
							Const.jang_new = Integer.toString(i+1);
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, i);
							PreferenceUtil.setStringSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
						}
					}
				}else if(Const.kwon_new.equals("60")){
					for(int i=0; i < Const.jang_kbb60.length; i++){
						if(which == i){
							Const.jang_new = Integer.toString(i+1);
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, i);
							PreferenceUtil.setStringSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
						}
					}
				}else if(Const.kwon_new.equals("61")){
					for(int i=0; i < Const.jang_kbb61.length; i++){
						if(which == i){
							Const.jang_new = Integer.toString(i+1);
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, i);
							PreferenceUtil.setStringSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
						}
					}
				}else if(Const.kwon_new.equals("62")){
					for(int i=0; i < Const.jang_kbb62.length; i++){
						if(which == i){
							Const.jang_new = Integer.toString(i+1);
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, i);
							PreferenceUtil.setStringSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
						}
					}
				}else if(Const.kwon_new.equals("63")){
					for(int i=0; i < Const.jang_kbb63.length; i++){
						if(which == i){
							Const.jang_new = Integer.toString(i+1);
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, i);
							PreferenceUtil.setStringSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
						}
					}
				}else if(Const.kwon_new.equals("64")){
					for(int i=0; i < Const.jang_kbb64.length; i++){
						if(which == i){
							Const.jang_new = Integer.toString(i+1);
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, i);
							PreferenceUtil.setStringSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
						}
					}
				}else if(Const.kwon_new.equals("65")){
					for(int i=0; i < Const.jang_kbb65.length; i++){
						if(which == i){
							Const.jang_new = Integer.toString(i+1);
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, i);
							PreferenceUtil.setStringSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
						}
					}
				}else if(Const.kwon_new.equals("66")){
					for(int i=0; i < Const.jang_kbb66.length; i++){
						if(which == i){
							Const.jang_new = Integer.toString(i+1);
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW_WHICH, i);
							PreferenceUtil.setStringSharedData(getActivity(), PreferenceUtil.PREF_JANG_NEW, Const.jang_new);
						}
					}
				}
				try{
					((MainFragmentActivity) getActivity()).voice_play_stop();
				}catch(Exception e){
				}
				Const.bt_jang_new.setText(Const.jang_new + getActivity().getString(R.string.txt_jang));
				display_list();
				dialog.dismiss();
			}
		}).show();
	}
	
	Thread autoscroll_thread = null;
	void autoScrollTask() {
		(autoscroll_thread = new Thread() {			
			public void run() {
				try {
					for (;;) {
						Thread.sleep(100);
						if(PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_SCROLL_SPEED, Const.SCROLL_SPEED) == 0){
							listview.smoothScrollBy(3, 100);	
						}else if(PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_SCROLL_SPEED, Const.SCROLL_SPEED) == 1){
							listview.smoothScrollBy(10, 50);
						}else if(PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_SCROLL_SPEED, Const.SCROLL_SPEED) == 2){
							listview.smoothScrollBy(15, 50);
						}
						handler.sendEmptyMessage(0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//		Fragment_Data_newBible data = (Fragment_Data_newBible)adapter.getItem(position);
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
		if(adapter != null){
			adapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
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
	
	public void addInterstitialView() {
    	if(interstialAd == null) {
        	AdInfo adInfo = new AdInfo("vuh3q0an");
//        	adInfo.setTestMode(false);
        	interstialAd = new com.admixer.InterstitialAd(getActivity());
        	interstialAd.setAdInfo(adInfo, getActivity());
        	interstialAd.setInterstitialAdListener(this);
        	interstialAd.startInterstitial();
    	}
    }
	
	@Override
	public void onInterstitialAdClosed(InterstitialAd arg0) {
		interstialAd = null;
	}

	@Override
	public void onInterstitialAdFailedToReceive(int arg0, String arg1, InterstitialAd arg2) {
		interstialAd = null;
	}

	@Override
	public void onInterstitialAdReceived(String arg0, InterstitialAd arg1) {
		interstialAd = null;
	}

	@Override
	public void onInterstitialAdShown(String arg0, InterstitialAd arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLeftClicked(String arg0, InterstitialAd arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRightClicked(String arg0, InterstitialAd arg1) {
		// TODO Auto-generated method stub
		
	}

}
