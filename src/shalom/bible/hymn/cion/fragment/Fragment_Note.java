package shalom.bible.hymn.cion.fragment;



import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import shalom.bible.hymn.cion.R;
import shalom.bible.hymn.cion.dao.Fragment_Data_Note;
import shalom.bible.hymn.cion.db.helper.DBOpenHelper;
import shalom.bible.hymn.cion.db.helper.DBOpenHelper_note;

public class Fragment_Note extends Fragment implements OnClickListener, OnItemClickListener, OnScrollListener{
	private ArrayList<Fragment_Data_Note> list;
	private FragmentAdapter adapter;
	private ListView listview;
	private LinearLayout layout_nodata;
	private final DBOpenHelper dbhelper = DBOpenHelper.getInstance();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_note,container, false);
		init_ui(view);
		display_list();
		listview.setOnScrollListener(this);
		listview.setOnItemClickListener(this);
		listview.setItemsCanFocus(false);
		return view;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		display_list();
	}
	
	DBOpenHelper_note note_db;
	public void display_list(){
		try{
			list = new ArrayList<Fragment_Data_Note>();
			adapter = new FragmentAdapter();
			note_db = new DBOpenHelper_note(getActivity());
			Cursor cursor = note_db.getReadableDatabase().rawQuery("select * from my_list order by _id desc", null);
			while(cursor.moveToNext()){
				int idx = cursor.getColumnIndex("_id");
				int id = cursor.getInt(idx);
				list.add(new Fragment_Data_Note(id, cursor.getString(cursor.getColumnIndex("kwon")),cursor.getString(cursor.getColumnIndex("jang")), cursor.getString(cursor.getColumnIndex("jul")), cursor.getString(cursor.getColumnIndex("content"))));
//				Log.i("dsu", "노트추가내용 : " +"id : " + id + cursor.getString(cursor.getColumnIndex("content")));
			}	
		}catch (Exception e) {
		}finally{
			if(note_db != null){
				note_db.close();
			}
		}
		listview.setAdapter(adapter);
		if(listview.getCount() > 0){
			layout_nodata.setVisibility(View.GONE);
		}else{
			layout_nodata.setVisibility(View.VISIBLE);
		}
	}
	
	public void datasetchanged(){
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, final int position, long arg3) {
		Fragment_Data_Note data = (Fragment_Data_Note)adapter.getItem(position);
    	String[] mylist_alert ={getActivity().getString(R.string.frg_note_alert_01),
    			getActivity().getString(R.string.frg_note_alert_02),
    			getActivity().getString(R.string.frg_note_alert_03),
    			getActivity().getString(R.string.frg_note_alert_04)};
    	final int id = data.get_id();
    	final String kwon = data.getKwon();
    	final String jang = data.getJang();
    	final String jul = data.getJul();
    	final String content = data.getContent();
    	new AlertDialog.Builder(getActivity())
    	.setTitle(kwon +" "+ jang)
    	.setItems(mylist_alert, new DialogInterface.OnClickListener() {
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			if(which == 0){
    				Intent intent = new Intent(Intent.ACTION_SEND);
    				intent.setType("text/plain");    
    				intent.addCategory(Intent.CATEGORY_DEFAULT);
    				intent.putExtra(Intent.EXTRA_TEXT, kwon +" "+ jang + "\n"+content);
    				startActivity(Intent.createChooser(intent, getActivity().getString(R.string.frg_note_alert_07)));
    			}else if(which == 1){
    				/*new AlertDialog.Builder(getActivity())
    		    	.setTitle(kwon +" "+ jang)
    		  		.setMessage(content)
    		  		.setCancelable(true)
    		  		.setPositiveButton(R.string.frg_note_alert_06, new DialogInterface.OnClickListener() {
    		  			@Override
    		  			public void onClick(DialogInterface dialog, int which) {
    		  			  dialog.dismiss();
    		  			}
    		  		}).show();*/
    				Intent intent = new Intent(getActivity(), Fragment_Note_Writer.class);
    				intent.putExtra("is_view", true);
    				intent.putExtra("edit", true);
    				intent.putExtra("_id", id);
    				intent.putExtra("kwon", kwon);
    				intent.putExtra("jang", jang);
    				intent.putExtra("jul", jul);
    				intent.putExtra("content", content);
    				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    				startActivity(intent);
    			}else if(which == 2){
    				Intent intent = new Intent(getActivity(), Fragment_Note_Writer.class);
    				intent.putExtra("is_view", false);
    				intent.putExtra("edit", true);
    				intent.putExtra("_id", id);
    				intent.putExtra("kwon", kwon);
    				intent.putExtra("jang", jang);
    				intent.putExtra("jul", jul);
    				intent.putExtra("content", content);
    				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    				startActivity(intent);
    			}else if(which == 3){
    				dbhelper.delete_note_db(id);
    				Toast.makeText(getActivity(), R.string.frg_note_alert_08, Toast.LENGTH_SHORT).show();
    				display_list();
    			}
    		}
    	}).show();
    }
	
	private void init_ui(View view){
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		layout_nodata = (LinearLayout)view.findViewById(R.id.layout_nodata);
		listview = (ListView)view.findViewById(R.id.listview);
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
					view = layoutInflater.inflate(R.layout.fragment_note_listrow, parent, false);
					ViewHolder holder = new ViewHolder();
					holder.txt_kwon = (TextView)view.findViewById(R.id.txt_kwon);
					holder.txt_jul = (TextView)view.findViewById(R.id.txt_jul);
					holder.txt_content = (TextView)view.findViewById(R.id.txt_content);
					view.setTag(holder);
				}
				ViewHolder holder = (ViewHolder)view.getTag();
				holder.txt_kwon.setText(list.get(position).getKwon() + " " + list.get(position).getJang());
				holder.txt_jul.setText(list.get(position).getJul());
				holder.txt_content.setText(list.get(position).getContent());
			}catch (Exception e) {
			}
			return view;
		}
	}

	@Override
	public void onClick(View v) {
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
	
	private class ViewHolder {
		  public TextView txt_kwon;
	      public TextView txt_jul;
	      public TextView txt_content;
	  }
}
