package shalom.bible.hymn.cion.fragment;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import shalom.bible.hymn.cion.R;
import shalom.bible.hymn.cion.activity.DictionaryViewActivity;
import shalom.bible.hymn.cion.dao.Fragment_Data_Dictionary;
import shalom.bible.hymn.cion.dao.Fragment_Data_Words_DictionaryList;
import shalom.bible.hymn.cion.util.KoreanTextMatch;
import shalom.bible.hymn.cion.util.KoreanTextMatcher;

public class Fragment_Dictionary extends Fragment implements OnItemClickListener, OnClickListener, OnScrollListener{
	private ListView listview_main;
	private Fragment_Data_Words_DictionaryList dl;
	private EditText edit_searcher;
	private String searchKeyword;
	private MainAdapter main_adapter;
	private ArrayList<Fragment_Data_Dictionary> list;
	private Fragment_Data_Dictionary main_data;
	private ImageButton btn_close;
	private LinearLayout layout_nodata;
	private KoreanTextMatch match1, match2;
	public static AlertDialog alertDialog;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_dictionary,container, false);
		init_ui(view);
		display_list();
		seacher_start();
		return view;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		edit_searcher.setText("");
	}
	
	private void init_ui(View view){
		layout_nodata = (LinearLayout)view.findViewById(R.id.layout_nodata);
		edit_searcher = (EditText)view.findViewById(R.id.edit_searcher);
		btn_close = (ImageButton)view.findViewById(R.id.btn_close);
		btn_close.setOnClickListener(this);
		listview_main = (ListView)view.findViewById(R.id.listview_main);
		listview_main.setOnScrollListener(this);
		listview_main.setOnItemClickListener(this);
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
					display_list();
					if(main_adapter != null){
						main_adapter.notifyDataSetChanged();
					}
					if(s.length() == 0){
						btn_close.setVisibility(View.INVISIBLE);
					}else{
						btn_close.setVisibility(View.VISIBLE);
					}
				} catch (Exception e) {
				}
			}
		});
	}
	
	private void display_list(){
		list = new ArrayList<Fragment_Data_Dictionary>();
		dl = new Fragment_Data_Words_DictionaryList();
		for(int i=1; i < dl.wordsmax; i++){
			main_data = new Fragment_Data_Dictionary(dl.words[i]);
			if(searchKeyword != null && "".equals(searchKeyword.trim()) == false){
				KoreanTextMatcher matcher1 = new KoreanTextMatcher(searchKeyword.toLowerCase());
				KoreanTextMatcher matcher2 = new KoreanTextMatcher(searchKeyword.toUpperCase());
				match1 = matcher1.match(dl.words[i].toLowerCase());
				match2 = matcher2.match(dl.words[i].toUpperCase());
				if(match1.success()){
					list.add(main_data);
				}else if (match2.success()){
					list.add(main_data);
				}
			}else{
				list.add(main_data);	
			}
		}
		main_adapter = new MainAdapter();
		listview_main.setAdapter(main_adapter);
		if(listview_main.getCount() > 0){
			layout_nodata.setVisibility(View.GONE);
		}else{
			layout_nodata.setVisibility(View.VISIBLE);			
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Fragment_Data_Dictionary main_data = (Fragment_Data_Dictionary)main_adapter.getItem(position);
		String name = main_data.getName();
		Intent intent = new Intent(getActivity(), DictionaryViewActivity.class);
		intent.putExtra("name", name);
		startActivity(intent);
	}
	
	@Override
	public void onClick(View view) {
		if(view == btn_close){
			edit_searcher.setText("");
			display_list();
			if(main_adapter != null){
				main_adapter.notifyDataSetChanged();
			}
			InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);  
    		inputMethodManager.hideSoftInputFromWindow(edit_searcher.getWindowToken(), 0);
		}
	}
	
	public class MainAdapter extends BaseAdapter{
		public MainAdapter() {
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
					view = layoutInflater.inflate(R.layout.fragment_dictionary_listrow, parent, false);
					ViewHolder holder = new ViewHolder();
					holder.txt_name = (TextView)view.findViewById(R.id.txt_name);
					holder.img_more = (ImageView)view.findViewById(R.id.img_more);
					view.setTag(holder);
				}
				ViewHolder holder = (ViewHolder)view.getTag();
				holder.img_more.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String name = list.get(position).getName();
						Intent intent = new Intent(getActivity(), DictionaryViewActivity.class);
						intent.putExtra("name", name);
						startActivity(intent);
					}
				});
				holder.img_more.setFocusable(false);
				setTextViewColorPartial(holder.txt_name, list.get(position).getName(), searchKeyword, Color.RED);
			}catch (Exception e) {
			}finally{
			}
			return view;
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
	
	private class ViewHolder {
		public TextView txt_name;
		public ImageView img_more;
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == OnScrollListener.SCROLL_STATE_FLING){
			InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);  
    		inputMethodManager.hideSoftInputFromWindow(edit_searcher.getWindowToken(), 0);
    		listview_main.setFastScrollEnabled(true);
		}else{
			listview_main.setFastScrollEnabled(false);
		}
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
	
	}
}
