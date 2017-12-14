package shalom.bible.hymn.cion.dao;

import java.util.Comparator;


public class Fragment_Data_Podcast_Main implements Comparator<Fragment_Data_Podcast_Main>{
	 public String id;
	 public String num;
	 public String title;
	 public String provider;
	 public String imageurl;
	 public String rssurl;
	 public String udate;
	 
	 @Override
		public int compare(Fragment_Data_Podcast_Main arg0, Fragment_Data_Podcast_Main arg1) {
			// TODO Auto-generated method stub
			return arg0.title.compareTo(arg1.title);
		}
 }
