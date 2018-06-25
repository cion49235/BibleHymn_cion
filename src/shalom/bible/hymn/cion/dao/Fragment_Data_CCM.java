package shalom.bible.hymn.cion.dao;

import java.util.Comparator;


public class Fragment_Data_CCM implements Comparator<Fragment_Data_CCM> {
	public int _id;
	public String id; 
	public String title;
	public String category;
	public String thumbnail_hq;
	public String duration;
	
	@Override
	public int compare(Fragment_Data_CCM arg0, Fragment_Data_CCM arg1) {
		// TODO Auto-generated method stub
		return arg0.title.compareTo(arg1.title);
	}
 }
