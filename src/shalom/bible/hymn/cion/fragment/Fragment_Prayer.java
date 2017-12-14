package shalom.bible.hymn.cion.fragment;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
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
import shalom.bible.hymn.cion.R;
import shalom.bible.hymn.cion.common.Const;
import shalom.bible.hymn.cion.dao.Fragment_Data_Prayer;
import shalom.bible.hymn.cion.db.helper.DBOpenHelper;
import shalom.bible.hymn.cion.util.PreferenceUtil;
import shalom.bible.hymn.cion.util.Utils;

public class Fragment_Prayer extends Fragment implements OnClickListener, OnItemClickListener, OnScrollListener {
	private LinearLayout layout_sub_description;
	private Button bt_main_description, bt_sub_description;
	private Button Bottom_01, Bottom_02, Bottom_03;
	private ImageButton ibt_main_description, ibt_sub_description;
	private ListView listview;
	private ArrayList<Fragment_Data_Prayer> list;
	private FragmentAdapter adapter;
	private final DBOpenHelper dbhelper = DBOpenHelper.getInstance();
	private Handler handler = new Handler();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_prayer,container, false);
		init_ui(view);
		set_bottom_menu(view);
		set_preference();
		display_list();
		return view;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	private void set_preference(){
		if(Utils.language(getActivity()).equals("ko_KR")){
			bt_main_description.setText(Const.main_description_prayer[PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_MAIN_PRAYER_WHICH, Const.main_prayer_which)]);	
		}else{
			bt_main_description.setText(Const.main_description_prayer_en[PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_MAIN_PRAYER_WHICH, Const.main_prayer_which)]);
		}
		if(PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_MAIN_PRAYER_WHICH, Const.main_prayer_which) == 7){
			bt_sub_description.setText(Const.new_kyodok_description_prayer[PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_NEW_KYODOK_PRAYER_WHICH, Const.new_kyodok_prayer_which)]);
			layout_sub_description.setVisibility(View.VISIBLE);
		}else if(PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_MAIN_PRAYER_WHICH, Const.main_prayer_which) == 8){
			bt_sub_description.setText(Const.simbang_description_prayer[PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_SIMBANG_PRAYER_WHICH, Const.simbang_prayer_which)]);
			layout_sub_description.setVisibility(View.VISIBLE);
		}else if(PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_MAIN_PRAYER_WHICH, Const.main_prayer_which) == 9){
			bt_sub_description.setText(Const.old_kyodok_description_prayer[PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_OLD_KYODOK_PRAYER_WHICH, Const.old_kyodok_prayer_which)]);
			layout_sub_description.setVisibility(View.VISIBLE);
		}else{
			layout_sub_description.setVisibility(View.INVISIBLE);
		}
	}
	
	private void set_bottom_menu(View view){
		Bottom_01 = (Button)view.findViewById(R.id.Bottom_01);
		Bottom_02 = (Button)view.findViewById(R.id.Bottom_02);
		Bottom_03 = (Button)view.findViewById(R.id.Bottom_03);
		Bottom_01.setOnClickListener(this);
		Bottom_02.setOnClickListener(this);
		Bottom_03.setOnClickListener(this);
	}
	
	private void init_ui(View view){
		layout_sub_description = (LinearLayout)view.findViewById(R.id.layout_sub_description);
		listview = (ListView)view.findViewById(R.id.listview);
		listview.setOnScrollListener(this);
		listview.setOnItemClickListener(this);
		listview.setItemsCanFocus(false);
		bt_main_description = (Button)view.findViewById(R.id.bt_main_description);
		bt_sub_description = (Button)view.findViewById(R.id.bt_sub_description);
		bt_main_description.setOnClickListener(this);
		bt_sub_description.setOnClickListener(this);
		ibt_main_description = (ImageButton)view.findViewById(R.id.ibt_main_description);
		ibt_sub_description = (ImageButton)view.findViewById(R.id.ibt_sub_description);
		ibt_main_description.setOnClickListener(this);
		ibt_sub_description.setOnClickListener(this);
	}
	
	public void display_list(){
		try{
			list = new ArrayList<Fragment_Data_Prayer>();
			adapter = new FragmentAdapter(list, Bottom_01, Bottom_02);
			dbhelper.bind_prayer_db(list);;
			listview.setAdapter(adapter);
		}catch (Exception e) {
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	
	}

	@Override
	public void onClick(View view) {
		if(view == bt_main_description){
			alert_main_description();
		}else if(view == bt_sub_description){
			alert_sub_description();
		}else if(view == ibt_main_description){
			alert_main_description();
		}else if(view == ibt_sub_description){
			alert_sub_description();
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
		}
	}
	
	Thread autoscroll_thread = null;
	void autoScrollTask() {
		(autoscroll_thread = new Thread() {			
			public void run() {
				try {
					for (;;) {
						Thread.sleep(100);
						listview.smoothScrollBy(3, 100);
						handler.sendEmptyMessage(0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void alert_main_description(){
		if(Utils.language(getActivity()).equals("ko_KR")){
			new AlertDialog.Builder(getActivity())
			.setTitle(R.string.frg_praye_01)
			.setSingleChoiceItems(Const.main_description_prayer, PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_MAIN_PRAYER_WHICH, Const.main_prayer_which), new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which){
					for (int i = 0; i < Const.main_description_prayer.length; i++) {
						if(which == i){
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_MAIN_PRAYER_WHICH, which);
						}else if(which == 7){
							bt_sub_description.setText(Const.new_kyodok_description_prayer[PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_NEW_KYODOK_PRAYER_WHICH, Const.new_kyodok_prayer_which)]);
							layout_sub_description.setVisibility(View.VISIBLE);
						}else if(which == 8){
							bt_sub_description.setText(Const.simbang_description_prayer[PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_SIMBANG_PRAYER_WHICH, Const.simbang_prayer_which)]);
							layout_sub_description.setVisibility(View.VISIBLE);
						}else if(which == 9){
							bt_sub_description.setText(Const.old_kyodok_description_prayer[PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_OLD_KYODOK_PRAYER_WHICH, Const.old_kyodok_prayer_which)]);
							layout_sub_description.setVisibility(View.VISIBLE);
						}else{
							layout_sub_description.setVisibility(View.INVISIBLE);
						}
					}
					bt_main_description.setText(Const.main_description_prayer[PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_MAIN_PRAYER_WHICH, Const.main_prayer_which)]);
					display_list();
					dialog.dismiss();
				}
			}).show();	
		}else{
			new AlertDialog.Builder(getActivity())
			.setTitle(R.string.frg_praye_01)
			.setSingleChoiceItems(Const.main_description_prayer_en, PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_MAIN_PRAYER_WHICH, Const.main_prayer_which), new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which){
					for (int i = 0; i < Const.main_description_prayer_en.length; i++) {
						if(which == i){
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_MAIN_PRAYER_WHICH, which);
						}
					}
					bt_main_description.setText(Const.main_description_prayer_en[PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_MAIN_PRAYER_WHICH, Const.main_prayer_which)]);
					display_list();
					dialog.dismiss();
				}
			}).show();	
		}
	}
	
	private void alert_sub_description(){
		if(PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_MAIN_PRAYER_WHICH, Const.main_prayer_which) == 7){
			new AlertDialog.Builder(getActivity())
			.setSingleChoiceItems(Const.new_kyodok_description_prayer, PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_NEW_KYODOK_PRAYER_WHICH, Const.new_kyodok_prayer_which), new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which){
					for (int i = 0; i < Const.new_kyodok_description_prayer.length; i++) {
						if(which == i){
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_NEW_KYODOK_PRAYER_WHICH, which);
						}
					}
					bt_sub_description.setText(Const.new_kyodok_description_prayer[PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_NEW_KYODOK_PRAYER_WHICH, Const.new_kyodok_prayer_which)]);
					display_list();
					dialog.dismiss();
				}
			}).show();	
		}else if(PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_MAIN_PRAYER_WHICH, Const.main_prayer_which) == 8){
			new AlertDialog.Builder(getActivity())
			.setSingleChoiceItems(Const.simbang_description_prayer, PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_SIMBANG_PRAYER_WHICH, Const.simbang_prayer_which), new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which){
					for (int i = 0; i < Const.simbang_description_prayer.length; i++) {
						if(which == i){
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_SIMBANG_PRAYER_WHICH, which);
						}
					}
					bt_sub_description.setText(Const.simbang_description_prayer[PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_SIMBANG_PRAYER_WHICH, Const.simbang_prayer_which)]);
					display_list();
					dialog.dismiss();
				}
			}).show();	
		}else if(PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_MAIN_PRAYER_WHICH, Const.main_prayer_which) == 9){
			new AlertDialog.Builder(getActivity())
			.setSingleChoiceItems(Const.old_kyodok_description_prayer, PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_OLD_KYODOK_PRAYER_WHICH, Const.old_kyodok_prayer_which), new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which){
					for (int i = 0; i < Const.old_kyodok_description_prayer.length; i++) {
						if(which == i){
							PreferenceUtil.setIntSharedData(getActivity(), PreferenceUtil.PREF_OLD_KYODOK_PRAYER_WHICH, which);
						}
					}
					bt_sub_description.setText(Const.old_kyodok_description_prayer[PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_OLD_KYODOK_PRAYER_WHICH, Const.old_kyodok_prayer_which)]);
					display_list();
					dialog.dismiss();
				}
			}).show();	
		}
	}
	
	public class FragmentAdapter extends BaseAdapter implements OnClickListener{
		private ArrayList<Fragment_Data_Prayer> list;
		Button btn_zoom_out, btn_zoom_in;
		public FragmentAdapter(ArrayList<Fragment_Data_Prayer> list,Button bt_zoom_out, Button bt_zoom_in) {
			this.btn_zoom_out = bt_zoom_out;
			this.btn_zoom_in = bt_zoom_in;
			btn_zoom_out.setOnClickListener(this);
			btn_zoom_in.setOnClickListener(this);
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
					LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
					view = layoutInflater.inflate(R.layout.fragment_prayer_listrow, parent, false);
					ViewHolder holder = new ViewHolder();
					holder.txt_gidomun_content = (TextView)view.findViewById(R.id.txt_gidomun_content);
					view.setTag(holder);
				}
				Const.default_textsize = PreferenceUtil.getIntSharedData(getActivity(), PreferenceUtil.PREF_BIBLE_TEXT_SIZE, Const.default_textsize);
				ViewHolder holder = (ViewHolder)view.getTag();
				holder.txt_gidomun_content.setText(list.get(position).getGidomun_content());
				holder.txt_gidomun_content.setTextSize(Const.default_textsize);
			}catch (Exception e) {
			}
			return view;
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
		  public TextView txt_gidomun_content;
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

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == OnScrollListener.SCROLL_STATE_FLING){
    		listview.setFastScrollEnabled(true);
		}else{
			listview.setFastScrollEnabled(false);
		}
	}
}
