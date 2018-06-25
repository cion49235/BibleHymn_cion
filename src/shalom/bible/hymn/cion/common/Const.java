package shalom.bible.hymn.cion.common;

import android.widget.Button;
import shalom.bible.hymn.cion.BibleHymnApp;
import shalom.bible.hymn.cion.R;
import shalom.bible.hymn.cion.util.Utils;

public class Const {
	/**
	 * Paramerter
	 */
	public static Button bt_kwon_old, bt_jang_old, bt_kwon_new, bt_jang_new;
	
	public static int main_prayer_which = 0;
	public static int new_kyodok_prayer_which = 0;
	public static int old_kyodok_prayer_which = 0;
	public static int simbang_prayer_which = 0;
	public static boolean voice_continue = true;
	public static boolean hymn_continue = false;
	public static int default_textsize;
	public static int current_position_bible;
	public static int current_position_podcast;
	public static String isSubscribed = "false";

	public static String[] intent_alert_podcast = {
			BibleHymnApp.getApplication().getString(R.string.frg_podcast_09),
			BibleHymnApp.getApplication().getString(R.string.frg_podcast_10),
	};
	public static String[] text_colorpicker_alert = {
			BibleHymnApp.getApplication().getString(R.string.txt_setting_alert0),
			BibleHymnApp.getApplication().getString(R.string.txt_setting_alert1),
	};
	public static String[] setting_alert ={
			BibleHymnApp.getApplication().getString(R.string.txt_setting_alert0),
			BibleHymnApp.getApplication().getString(R.string.txt_setting_alert1),
			BibleHymnApp.getApplication().getString(R.string.txt_setting_alert2),
			BibleHymnApp.getApplication().getString(R.string.txt_setting_alert3),
			BibleHymnApp.getApplication().getString(R.string.txt_setting_alert4),
			BibleHymnApp.getApplication().getString(R.string.txt_setting_alert5),
			BibleHymnApp.getApplication().getString(R.string.txt_setting_alert6),
			BibleHymnApp.getApplication().getString(R.string.txt_setting_alert7),
			BibleHymnApp.getApplication().getString(R.string.txt_setting_alert8),
			BibleHymnApp.getApplication().getString(R.string.txt_setting_alert11),
			BibleHymnApp.getApplication().getString(R.string.txt_setting_alert12)};
	public static String[] setting_alert_en ={
			BibleHymnApp.getApplication().getString(R.string.txt_setting_alert0),
			BibleHymnApp.getApplication().getString(R.string.txt_setting_alert1),
			BibleHymnApp.getApplication().getString(R.string.txt_setting_alert3),
			BibleHymnApp.getApplication().getString(R.string.txt_setting_alert4),
			BibleHymnApp.getApplication().getString(R.string.txt_setting_alert5),
			BibleHymnApp.getApplication().getString(R.string.txt_setting_alert6),
			BibleHymnApp.getApplication().getString(R.string.txt_setting_alert7),
			BibleHymnApp.getApplication().getString(R.string.txt_setting_alert8),
			BibleHymnApp.getApplication().getString(R.string.txt_setting_alert11),
			BibleHymnApp.getApplication().getString(R.string.txt_setting_alert12)};
	public static int AUDIO_SPEED = 1;
	public static int SWIPE_MODE = 0;
	public static int SCROLL_SPEED = 0;
	public static int BIBLE_TYPE_1;
	public static int BIBLE_TYPE_2 = 0;
	public static int BIBLE_TEXT_COLOR = 0xff000000;
	public static int BIBLE_TEXT_COLOR2 = 0xff00a8ff;
	public static int BIBLE_BG_COLOR = 0xffffffff;
	
	public static String kwon_kbb_old[] = {
			/*구약*/
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb1),
    		BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb2),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb3),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb4),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb5),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb6),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb7),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb8),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb9),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb10),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb11),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb12),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb13),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb14),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb15),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb16),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb17),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb18),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb19),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb20),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb21),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb22),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb23),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb24),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb25),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb26),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb27),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb28),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb29),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb30),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb31),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb32),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb33),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb34),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb35),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb36),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb37),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb38),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb39)};
	
	public static String kwon_kbb_new[] = {
			/*신약*/
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb40),
    		BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb41),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb42),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb43),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb44),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb45),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb46),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb47),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb48),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb49),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb50),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb51),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb52),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb53),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb54),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb55),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb56),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb57),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb58),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb59),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb60),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb61),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb62),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb63),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb64),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb65),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb66)};
	
	public static String kwon_kbb_all[] = {
			/*구약*/
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb1),
    		BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb2),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb3),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb4),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb5),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb6),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb7),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb8),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb9),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb10),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb11),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb12),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb13),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb14),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb15),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb16),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb17),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb18),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb19),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb20),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb21),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb22),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb23),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb24),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb25),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb26),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb27),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb28),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb29),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb30),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb31),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb32),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb33),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb34),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb35),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb36),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb37),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb38),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb39),
			/*신약*/
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb40),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb41),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb42),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb43),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb44),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb45),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb46),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb47),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb48),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb49),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb50),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb51),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb52),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb53),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb54),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb55),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb56),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb57),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb58),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb59),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb60),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb61),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb62),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb63),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb64),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb65),
			BibleHymnApp.getApplication().getString(R.string.txt_kwon_kbb66)};
	
	//jang_number
	public static String[] jang_kbb_old = {"1","2","3","4","5","6","7","8","9","10","11","12","13",
			"14","15","16","17","18","19","20","21","22","23","24","25","26","27",
			"28","29","30","31","32","33","34","35","36","37","38","39","40",
			"41","42","43","44","45","46","47","48","49","50",
			"51","52","53","54","55","56","57","58","59","60"
			,"61","62","63","64","65","66","67","68","69","70"
			,"71","72","73","74","75","76","77","78","79","80",
			"81","82","83","84","85","86","87","88","89","90",
			"91","92","93","94","95","96","97","98","99","100",
			"101","102","103","104","105","106","107","108","109","110",
			"111","112","113","114","115","116","117","118","119","120",
			"121","122","123","124","125","126","127","128","129","130",
			"131","132","133","134","135","136","137","138","139","140",
			"141","142","143","144","145","146","147","148","149","150"};
	
	public static String[] jang_kbb_new = {"1","2","3","4","5","6","7","8","9","10","11","12","13",
			"14","15","16","17","18","19","20","21","22","23","24","25","26","27",
			"28","29","30","31","32","33","34","35","36","37","38","39","40",
			"41","42","43","44","45","46","47","48","49","50",
			"51","52","53","54","55","56","57","58","59","60"
			,"61","62","63","64","65","66","67","68","69","70"
			,"71","72","73","74","75","76","77","78","79","80",
			"81","82","83","84","85","86","87","88","89","90",
			"91","92","93","94","95","96","97","98","99","100",
			"101","102","103","104","105","106","107","108","109","110",
			"111","112","113","114","115","116","117","118","119","120",
			"121","122","123","124","125","126","127","128","129","130",
			"131","132","133","134","135","136","137","138","139","140",
			"141","142","143","144","145","146","147","148","149","150"};
	
	//1.창세기
	public static String[] jang_kbb1 = {"1","2","3","4","5","6","7","8","9","10","11","12","13",
			"14","15","16","17","18","19","20","21","22","23","24","25","26","27",
			"28","29","30","31","32","33","34","35","36","37","38","39","40","41","42",
			"43","44","45","46","47","48","49","50"};
	//2.출애굽기
	public static String[] jang_kbb2 = {"1","2","3","4","5","6","7","8","9","10","11","12","13",
			"14","15","16","17","18","19","20","21","22","23","24","25","26","27",
			"28","29","30","31","32","33","34","35","36","37","38","39","40"};
	//3.레위기
	public static String[] jang_kbb3 = {"1","2","3","4","5","6","7","8","9","10","11","12","13",
			"14","15","16","17","18","19","20","21","22","23","24","25","26","27"};
	//4.민수기
	public static String[] jang_kbb4 = {"1","2","3","4","5","6","7","8","9","10","11","12","13",
			"14","15","16","17","18","19","20","21","22","23","24","25","26","27",
			"28","29","30","31","32","33","34","35","36"};
	//5.신명기
	public static String[] jang_kbb5 = {"1","2","3","4","5","6","7","8","9","10","11","12","13",
			"14","15","16","17","18","19","20","21","22","23","24","25","26","27",
			"28","29","30","31","32","33","34"};
	
	//6.여호수아
	public static String[] jang_kbb6 = {"1","2","3","4","5","6","7","8","9","10","11","12","13",
			"14","15","16","17","18","19","20","21","22","23","24"};
	//7.사사기
	public static String[] jang_kbb7 = {"1","2","3","4","5","6","7","8","9","10","11","12","13",
			"14","15","16","17","18","19","20","21"};
	//8.룻기
	public static String[] jang_kbb8 = {"1","2","3","4"};
	//9.사무엘상
	public static String[] jang_kbb9 = {"1","2","3","4","5","6","7","8","9","10","11","12","13",
			"14","15","16","17","18","19","20","21","22","23","24","25","26","27",
			"28","29","30","31"};
	//10.사무엘하
	public static String[] jang_kbb10 = {"1","2","3","4","5","6","7","8","9","10","11","12","13",
			"14","15","16","17","18","19","20","21","22","23","24"};
	//11.열왕기상
	public static String[] jang_kbb11 = {"1","2","3","4","5","6","7","8","9","10","11","12","13",
			"14","15","16","17","18","19","20","21","22"};
	//12.열왕기하
	public static String[] jang_kbb12 = {"1","2","3","4","5","6","7","8","9","10","11","12","13",
			"14","15","16","17","18","19","20","21","22","23","24","25"};
	//13.역대상
	public static String[] jang_kbb13 = {"1","2","3","4","5","6","7","8","9","10","11","12","13",
			"14","15","16","17","18","19","20","21","22","23","24","25","26","27",
			"28","29"};
	//14.역대하
	public static String[] jang_kbb14 = {"1","2","3","4","5","6","7","8","9","10","11","12","13",
			"14","15","16","17","18","19","20","21","22","23","24","25","26","27",
			"28","29","30","31","32","33","34","35","36"};
	//15.에스라
	public static String[] jang_kbb15 = {"1","2","3","4","5","6","7","8","9","10"};
	//16.느헤미야
	public static String[] jang_kbb16 = {"1","2","3","4","5","6","7","8","9","10","11","12","13"};
	
	//17.에스더
	public static String[] jang_kbb17 = {"1","2","3","4","5","6","7","8","9","10"};
	//18.욥기
	public static String[] jang_kbb18 = {"1","2","3","4","5","6","7","8","9","10","11","12","13",
			"14","15","16","17","18","19","20","21","22","23","24","25","26","27",
			"28","29","30","31","32","33","34","35","36","37","38","39","40","41","42"};
	//19.시편
	public static String[] jang_kbb19 = {"1","2","3","4","5","6","7","8","9","10","11","12","13",
				"14","15","16","17","18","19","20","21","22","23","24","25","26","27",
				"28","29","30","31","32","33","34","35","36","37","38","39","40",
				"41","42","43","44","45","46","47","48","49","50",
				"51","52","53","54","55","56","57","58","59","60"
				,"61","62","63","64","65","66","67","68","69","70"
				,"71","72","73","74","75","76","77","78","79","80",
				"81","82","83","84","85","86","87","88","89","90",
				"91","92","93","94","95","96","97","98","99","100",
				"101","102","103","104","105","106","107","108","109","110",
				"111","112","113","114","115","116","117","118","119","120",
				"121","122","123","124","125","126","127","128","129","130",
				"131","132","133","134","135","136","137","138","139","140",
				"141","142","143","144","145","146","147","148","149","150"};
	//20.잠언
	public static String[] jang_kbb20 = {"1","2","3","4","5","6","7","8","9","10","11","12","13",
			"14","15","16","17","18","19","20","21","22","23","24","25","26","27",
			"28","29","30","31"};
	//21.전도서
	public static String[] jang_kbb21 = {"1","2","3","4","5","6","7","8","9","10","11","12"};
	//22.아가
	public static String[] jang_kbb22 = {"1","2","3","4","5","6","7","8"};
	//23.이사야
	public static String[] jang_kbb23 = {"1","2","3","4","5","6","7","8","9","10","11","12","13",
			"14","15","16","17","18","19","20","21","22","23","24","25","26","27",
			"28","29","30","31","32","33","34","35","36","37","38","39","40",
			"41","42","43","44","45","46","47","48","49","50","51","52","53","54","55",
			"56","57","58","59","60","61","62","63","64","65","66"};
	//24.예레미야
	public static String[] jang_kbb24 = {"1","2","3","4","5","6","7","8","9","10","11","12","13",
			"14","15","16","17","18","19","20","21","22","23","24","25","26","27",
			"28","29","30","31","32","33","34","35","36","37","38","39","40","41",
			"42","43","44","45","46","47","48","49","50","51","52"};
	//25.예레미야애가
	public static String[] jang_kbb25 = {"1","2","3","4","5"};
	//26.에스겔
	public static String[] jang_kbb26 = {"1","2","3","4","5","6","7","8","9","10","11","12","13",
			"14","15","16","17","18","19","20","21","22","23","24","25","26","27",
			"28","29","30","31","32","33","34","35","36","37","38","39","40","41",
			"42","43","44","45","46","47","48"};
	//27.다니엘
	public static String[] jang_kbb27 = {"1","2","3","4","5","6","7","8","9","10","11","12"};
	//28.호세아
	public static String[] jang_kbb28 = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14"};
	//29.요엘
	public static String[] jang_kbb29 = {"1","2","3"};
	//30.아모스
	public static String[] jang_kbb30 = {"1","2","3","4","5","6","7","8","9"};
	//31.오바댜
	public static String[] jang_kbb31 = {"1"};
	//32.요나
	public static String[] jang_kbb32 = {"1","2","3","4"};
	//33.미가
	public static String[] jang_kbb33 = {"1","2","3","4","5","6","7"};
	//34.나훔
	public static String[] jang_kbb34 = {"1","2","3"};
	//35.하박구
	public static String[] jang_kbb35 = {"1","2","3"};
	//36.스바냐
	public static String[] jang_kbb36 = {"1","2","3"};
	//37.학개
	public static String[] jang_kbb37 = {"1","2"};
	//38.스가랴
	public static String[] jang_kbb38 = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14"};
	//39.말라기
	public static String[] jang_kbb39 = {"1","2","3","4"};
	
	public static String kwon_old = "1";
	public static String jang_old = "1";
	
	public static int jang_page_max_old;
	public static int jang_page_max_old(){
		if(kwon_old.equals("1")){
			jang_page_max_old = Const.jang_kbb1.length;
		}else if(kwon_old.equals("2")){
			jang_page_max_old = Const.jang_kbb2.length;
		}else if(kwon_old.equals("3")){
			jang_page_max_old = Const.jang_kbb3.length;
		}else if(kwon_old.equals("4")){
			jang_page_max_old = Const.jang_kbb4.length;
		}else if(kwon_old.equals("5")){
			jang_page_max_old = Const.jang_kbb5.length;
		}else if(kwon_old.equals("6")){
			jang_page_max_old = Const.jang_kbb6.length;
		}else if(kwon_old.equals("7")){
			jang_page_max_old = Const.jang_kbb7.length;
		}else if(kwon_old.equals("8")){
			jang_page_max_old = Const.jang_kbb8.length;
		}else if(kwon_old.equals("9")){
			jang_page_max_old = Const.jang_kbb9.length;
		}else if(kwon_old.equals("10")){
			jang_page_max_old = Const.jang_kbb10.length;
		}else if(kwon_old.equals("11")){
			jang_page_max_old = Const.jang_kbb11.length;
		}else if(kwon_old.equals("12")){
			jang_page_max_old = Const.jang_kbb12.length;
		}else if(kwon_old.equals("13")){
			jang_page_max_old = Const.jang_kbb13.length;
		}else if(kwon_old.equals("14")){
			jang_page_max_old = Const.jang_kbb14.length;
		}else if(kwon_old.equals("15")){
			jang_page_max_old = Const.jang_kbb15.length;
		}else if(kwon_old.equals("16")){
			jang_page_max_old = Const.jang_kbb16.length;
		}else if(kwon_old.equals("17")){
			jang_page_max_old = Const.jang_kbb17.length;
		}else if(kwon_old.equals("18")){
			jang_page_max_old = Const.jang_kbb18.length;
		}else if(kwon_old.equals("19")){
			jang_page_max_old = Const.jang_kbb19.length;
		}else if(kwon_old.equals("20")){
			jang_page_max_old = Const.jang_kbb20.length;
		}else if(kwon_old.equals("21")){
			jang_page_max_old = Const.jang_kbb21.length;
		}else if(kwon_old.equals("22")){
			jang_page_max_old = Const.jang_kbb22.length;
		}else if(kwon_old.equals("23")){
			jang_page_max_old = Const.jang_kbb23.length;
		}else if(kwon_old.equals("24")){
			jang_page_max_old = Const.jang_kbb24.length;
		}else if(kwon_old.equals("25")){
			jang_page_max_old = Const.jang_kbb25.length;
		}else if(kwon_old.equals("26")){
			jang_page_max_old = Const.jang_kbb26.length;
		}else if(kwon_old.equals("27")){
			jang_page_max_old = Const.jang_kbb27.length;
		}else if(kwon_old.equals("28")){
			jang_page_max_old = Const.jang_kbb28.length;
		}else if(kwon_old.equals("29")){
			jang_page_max_old = Const.jang_kbb29.length;
		}else if(kwon_old.equals("30")){
			jang_page_max_old = Const.jang_kbb30.length;
		}else if(kwon_old.equals("31")){
			jang_page_max_old = Const.jang_kbb31.length;
		}else if(kwon_old.equals("32")){
			jang_page_max_old = Const.jang_kbb32.length;
		}else if(kwon_old.equals("33")){
			jang_page_max_old = Const.jang_kbb33.length;
		}else if(kwon_old.equals("34")){
			jang_page_max_old = Const.jang_kbb34.length;
		}else if(kwon_old.equals("35")){
			jang_page_max_old = Const.jang_kbb35.length;
		}else if(kwon_old.equals("36")){
			jang_page_max_old = Const.jang_kbb36.length;
		}else if(kwon_old.equals("37")){
			jang_page_max_old = Const.jang_kbb37.length;
		}else if(kwon_old.equals("38")){
			jang_page_max_old = Const.jang_kbb38.length;
		}else if(kwon_old.equals("39")){
			jang_page_max_old = Const.jang_kbb39.length;
		}
		return jang_page_max_old;
	}
	
	public static String[] jang_page_num_old;
	public static String[] jang_page_num_old(){
		if(Const.kwon_old.equals("1")){
			jang_page_num_old = Const.jang_kbb1;
		}else if(Const.kwon_old.equals("2")){
			jang_page_num_old = Const.jang_kbb2;
		}else if(Const.kwon_old.equals("3")){
			jang_page_num_old = Const.jang_kbb3;
		}else if(Const.kwon_old.equals("4")){
			jang_page_num_old = Const.jang_kbb4;
		}else if(Const.kwon_old.equals("5")){
			jang_page_num_old = Const.jang_kbb5;
		}else if(Const.kwon_old.equals("6")){
			jang_page_num_old = Const.jang_kbb6;
		}else if(Const.kwon_old.equals("7")){
			jang_page_num_old = Const.jang_kbb7;
		}else if(Const.kwon_old.equals("8")){
			jang_page_num_old = Const.jang_kbb8;
		}else if(Const.kwon_old.equals("9")){
			jang_page_num_old = Const.jang_kbb9;
		}else if(Const.kwon_old.equals("10")){
			jang_page_num_old = Const.jang_kbb10;
		}else if(Const.kwon_old.equals("11")){
			jang_page_num_old = Const.jang_kbb11;
		}else if(Const.kwon_old.equals("12")){
			jang_page_num_old = Const.jang_kbb12;
		}else if(Const.kwon_old.equals("13")){
			jang_page_num_old = Const.jang_kbb13;
		}else if(Const.kwon_old.equals("14")){
			jang_page_num_old = Const.jang_kbb14;
		}else if(Const.kwon_old.equals("15")){
			jang_page_num_old = Const.jang_kbb15;
		}else if(Const.kwon_old.equals("16")){
			jang_page_num_old = Const.jang_kbb16;
		}else if(Const.kwon_old.equals("17")){
			jang_page_num_old = Const.jang_kbb17;
		}else if(Const.kwon_old.equals("18")){
			jang_page_num_old = Const.jang_kbb18;
		}else if(Const.kwon_old.equals("19")){
			jang_page_num_old = Const.jang_kbb19;
		}else if(Const.kwon_old.equals("20")){
			jang_page_num_old = Const.jang_kbb20;
		}else if(Const.kwon_old.equals("21")){
			jang_page_num_old = Const.jang_kbb21;
		}else if(Const.kwon_old.equals("22")){
			jang_page_num_old = Const.jang_kbb22;
		}else if(Const.kwon_old.equals("23")){
			jang_page_num_old = Const.jang_kbb23;
		}else if(Const.kwon_old.equals("24")){
			jang_page_num_old = Const.jang_kbb24;
		}else if(Const.kwon_old.equals("25")){
			jang_page_num_old = Const.jang_kbb25;
		}else if(Const.kwon_old.equals("26")){
			jang_page_num_old = Const.jang_kbb26;
		}else if(Const.kwon_old.equals("27")){
			jang_page_num_old = Const.jang_kbb27;
		}else if(Const.kwon_old.equals("28")){
			jang_page_num_old = Const.jang_kbb28;
		}else if(Const.kwon_old.equals("29")){
			jang_page_num_old = Const.jang_kbb29;
		}else if(Const.kwon_old.equals("30")){
			jang_page_num_old = Const.jang_kbb30;
		}else if(Const.kwon_old.equals("31")){
			jang_page_num_old = Const.jang_kbb31;
		}else if(Const.kwon_old.equals("32")){
			jang_page_num_old = Const.jang_kbb32;
		}else if(Const.kwon_old.equals("33")){
			jang_page_num_old = Const.jang_kbb33;
		}else if(Const.kwon_old.equals("34")){
			jang_page_num_old = Const.jang_kbb34;
		}else if(Const.kwon_old.equals("35")){
			jang_page_num_old = Const.jang_kbb35;
		}else if(Const.kwon_old.equals("36")){
			jang_page_num_old = Const.jang_kbb36;
		}else if(Const.kwon_old.equals("37")){
			jang_page_num_old = Const.jang_kbb37;
		}else if(Const.kwon_old.equals("38")){
			jang_page_num_old = Const.jang_kbb38;
		}else if(Const.kwon_old.equals("39")){
			jang_page_num_old = Const.jang_kbb39;
		}
		return jang_page_num_old;
	}
	
	
		//40.마태복음
		public static String[] jang_kbb40 = {"1","2","3","4","5","6","7","8","9","10","11","12","13",
				"14","15","16","17","18","19","20","21","22","23","24","25","26","27",
				"28"};
		//41.마가복음
		public static String[] jang_kbb41 = {"1","2","3","4","5","6","7","8","9","10","11","12","13",
				"14","15","16"};
		
		//42.누가복음
		public static String[] jang_kbb42 = {"1","2","3","4","5","6","7","8","9","10","11","12","13",
				"14","15","16","17","18","19","20","21","22","23","24"};
		//43.요한복음
		public static String[] jang_kbb43 = {"1","2","3","4","5","6","7","8","9","10","11","12","13",
				"14","15","16","17","18","19","20","21"};
		//44.사도행전
		public static String[] jang_kbb44 = {"1","2","3","4","5","6","7","8","9","10","11","12","13",
				"14","15","16","17","18","19","20","21","22","23","24","25","26","27",
				"28"};
		//45.로마서
		public static String[] jang_kbb45 = {"1","2","3","4","5","6","7","8","9","10","11","12","13",
				"14","15","16"};
		
		//46.고린도전서
		public static String[] jang_kbb46 = {"1","2","3","4","5","6","7","8","9","10","11","12","13",
				"14","15","16"};
		//47.고린도후서
		public static String[] jang_kbb47 = {"1","2","3","4","5","6","7","8","9","10","11","12","13"};
		
		//48.갈라디아서
		public static String[] jang_kbb48 = {"1","2","3","4","5","6"};
		
		//49.에베소서
		public static String[] jang_kbb49 = {"1","2","3","4","5","6"};
		
		//50.빌립보서
		public static String[] jang_kbb50 = {"1","2","3","4"};
		
		//51.골로새서
		public static String[] jang_kbb51 = {"1","2","3","4"};
		
		//52.데살로니가전서
		public static String[] jang_kbb52 = {"1","2","3","4","5"};
		
		//53.데살로니가후서
		public static String[] jang_kbb53 = {"1","2","3"};
		
		//54.디모데전서
		public static String[] jang_kbb54 = {"1","2","3","4","5","6"};
		
		//55.디모데후서
		public static String[] jang_kbb55 = {"1","2","3","4"};
		
		//56.디도서
		public static String[] jang_kbb56 = {"1","2","3"};
		
		//57.빌레몬서
		public static String[] jang_kbb57 = {"1"};
		
		//58.히브리서
		public static String[] jang_kbb58 = {"1","2","3","4","5","6","7","8","9","10","11","12","13"};
		
		//59.야고보서
		public static String[] jang_kbb59 = {"1","2","3","4","5"};
		
		//60.베드로전서
		public static String[] jang_kbb60 = {"1","2","3","4","5"};
		
		//61.베드로후서
		public static String[] jang_kbb61 = {"1","2","3"};
		
		//62.요한일서
		public static String[] jang_kbb62 = {"1","2","3","4","5"};
		
		//63.요한이서
		public static String[] jang_kbb63 = {"1"};
		
		//64.요한삼서
		public static String[] jang_kbb64 = {"1"};
		
		//65.유다서
		public static String[] jang_kbb65 = {"1"};
		
		//66.요한계시록
		public static String[] jang_kbb66 = {"1","2","3","4","5","6","7","8","9","10","11","12","13",
				"14","15","16","17","18","19","20","21","22"};
		
		public static String kwon_new = "40";
		public static String jang_new = "1";
		
		public static int jang_page_max_new;
		public static int jang_page_max_new(){
			if(kwon_new.equals("40")){
				jang_page_max_new = Const.jang_kbb40.length;
			}else if(kwon_new.equals("41")){
				jang_page_max_new = Const.jang_kbb41.length;
			}else if(kwon_new.equals("42")){
				jang_page_max_new = Const.jang_kbb42.length;
			}else if(kwon_new.equals("43")){
				jang_page_max_new = Const.jang_kbb43.length;
			}else if(kwon_new.equals("44")){
				jang_page_max_new = Const.jang_kbb44.length;
			}else if(kwon_new.equals("45")){
				jang_page_max_new = Const.jang_kbb45.length;
			}else if(kwon_new.equals("46")){
				jang_page_max_new = Const.jang_kbb46.length;
			}else if(kwon_new.equals("47")){
				jang_page_max_new = Const.jang_kbb47.length;
			}else if(kwon_new.equals("48")){
				jang_page_max_new = Const.jang_kbb48.length;
			}else if(kwon_new.equals("49")){
				jang_page_max_new = Const.jang_kbb49.length;
			}else if(kwon_new.equals("50")){
				jang_page_max_new = Const.jang_kbb50.length;
			}else if(kwon_new.equals("51")){
				jang_page_max_new = Const.jang_kbb51.length;
			}else if(kwon_new.equals("52")){
				jang_page_max_new = Const.jang_kbb52.length;
			}else if(kwon_new.equals("53")){
				jang_page_max_new = Const.jang_kbb53.length;
			}else if(kwon_new.equals("54")){
				jang_page_max_new = Const.jang_kbb54.length;
			}else if(kwon_new.equals("55")){
				jang_page_max_new = Const.jang_kbb55.length;
			}else if(kwon_new.equals("56")){
				jang_page_max_new = Const.jang_kbb56.length;
			}else if(kwon_new.equals("57")){
				jang_page_max_new = Const.jang_kbb57.length;
			}else if(kwon_new.equals("58")){
				jang_page_max_new = Const.jang_kbb58.length;
			}else if(kwon_new.equals("59")){
				jang_page_max_new = Const.jang_kbb59.length;
			}else if(kwon_new.equals("60")){
				jang_page_max_new = Const.jang_kbb60.length;
			}else if(kwon_new.equals("61")){
				jang_page_max_new = Const.jang_kbb61.length;
			}else if(kwon_new.equals("62")){
				jang_page_max_new = Const.jang_kbb62.length;
			}else if(kwon_new.equals("63")){
				jang_page_max_new = Const.jang_kbb63.length;
			}else if(kwon_new.equals("64")){
				jang_page_max_new = Const.jang_kbb64.length;
			}else if(kwon_new.equals("65")){
				jang_page_max_new = Const.jang_kbb65.length;
			}else if(kwon_new.equals("66")){
				jang_page_max_new = Const.jang_kbb66.length;
			}
			return jang_page_max_new;
		}
		
		public static String[] jang_page_num_new;
		public static String[] jang_page_num_new(){
			if(Const.kwon_new.equals("40")){
				jang_page_num_new = Const.jang_kbb40;
			}else if(Const.kwon_new.equals("41")){
				jang_page_num_new = Const.jang_kbb41;
			}else if(Const.kwon_new.equals("42")){
				jang_page_num_new = Const.jang_kbb42;
			}else if(Const.kwon_new.equals("43")){
				jang_page_num_new = Const.jang_kbb43;
			}else if(Const.kwon_new.equals("44")){
				jang_page_num_new = Const.jang_kbb44;
			}else if(Const.kwon_new.equals("45")){
				jang_page_num_new = Const.jang_kbb45;
			}else if(Const.kwon_new.equals("46")){
				jang_page_num_new = Const.jang_kbb46;
			}else if(Const.kwon_new.equals("47")){
				jang_page_num_new = Const.jang_kbb47;
			}else if(Const.kwon_new.equals("48")){
				jang_page_num_new = Const.jang_kbb48;
			}else if(Const.kwon_new.equals("49")){
				jang_page_num_new = Const.jang_kbb49;
			}else if(Const.kwon_new.equals("50")){
				jang_page_num_new = Const.jang_kbb50;
			}else if(Const.kwon_new.equals("51")){
				jang_page_num_new = Const.jang_kbb51;
			}else if(Const.kwon_new.equals("52")){
				jang_page_num_new = Const.jang_kbb52;
			}else if(Const.kwon_new.equals("53")){
				jang_page_num_new = Const.jang_kbb53;
			}else if(Const.kwon_new.equals("54")){
				jang_page_num_new = Const.jang_kbb54;
			}else if(Const.kwon_new.equals("55")){
				jang_page_num_new = Const.jang_kbb55;
			}else if(Const.kwon_new.equals("56")){
				jang_page_num_new = Const.jang_kbb56;
			}else if(Const.kwon_new.equals("57")){
				jang_page_num_new = Const.jang_kbb57;
			}else if(Const.kwon_new.equals("58")){
				jang_page_num_new = Const.jang_kbb58;
			}else if(Const.kwon_new.equals("59")){
				jang_page_num_new = Const.jang_kbb59;
			}else if(Const.kwon_new.equals("60")){
				jang_page_num_new = Const.jang_kbb60;
			}else if(Const.kwon_new.equals("61")){
				jang_page_num_new = Const.jang_kbb61;
			}else if(Const.kwon_new.equals("62")){
				jang_page_num_new = Const.jang_kbb62;
			}else if(Const.kwon_new.equals("63")){
				jang_page_num_new = Const.jang_kbb63;
			}else if(Const.kwon_new.equals("64")){
				jang_page_num_new = Const.jang_kbb64;
			}else if(Const.kwon_new.equals("65")){
				jang_page_num_new = Const.jang_kbb65;
			}else if(Const.kwon_new.equals("66")){
				jang_page_num_new = Const.jang_kbb66;
			}
			return jang_page_num_new;
		}
	

	/*기도문 ko*/
	public static String[] main_description_prayer = {
			BibleHymnApp.getApplication().getString(R.string.txt_prayer_description00),
			BibleHymnApp.getApplication().getString(R.string.txt_prayer_description01),
			BibleHymnApp.getApplication().getString(R.string.txt_prayer_description02),
			BibleHymnApp.getApplication().getString(R.string.txt_prayer_description03),
			BibleHymnApp.getApplication().getString(R.string.txt_prayer_description04),
			BibleHymnApp.getApplication().getString(R.string.txt_prayer_description05),
			BibleHymnApp.getApplication().getString(R.string.txt_prayer_description06),
			BibleHymnApp.getApplication().getString(R.string.txt_prayer_description07),
			BibleHymnApp.getApplication().getString(R.string.txt_prayer_description08),
			BibleHymnApp.getApplication().getString(R.string.txt_prayer_description09)
	};
	/*기도문 en*/
	public static String[] main_description_prayer_en = {
			BibleHymnApp.getApplication().getString(R.string.txt_prayer_description02),
			BibleHymnApp.getApplication().getString(R.string.txt_prayer_description05),
	};
	
	/*신교독문*/
	public static String[] new_kyodok_description_prayer = {
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description00),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description01),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description02),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description03),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description04),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description05),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description06),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description07),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description08),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description09),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description10),
			
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description11),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description12),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description13),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description14),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description15),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description16),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description17),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description18),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description19),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description20),
			
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description21),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description22),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description23),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description24),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description25),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description26),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description27),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description28),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description29),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description30),
			
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description31),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description32),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description33),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description34),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description35),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description36),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description37),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description38),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description39),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description40),
			
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description41),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description42),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description43),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description44),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description45),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description46),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description47),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description48),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description49),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description50),
			
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description51),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description52),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description53),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description54),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description55),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description56),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description57),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description58),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description59),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description60),
			
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description61),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description62),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description63),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description64),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description65),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description66),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description67),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description68),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description69),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description70),
			
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description71),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description72),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description73),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description74),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description75),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description76),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description77),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description78),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description79),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description80),
			
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description81),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description82),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description83),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description84),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description85),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description86),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description87),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description88),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description89),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description90),
			
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description91),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description92),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description93),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description94),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description95),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description96),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description97),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description98),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description99),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description100),
			
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description101),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description102),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description103),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description104),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description105),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description106),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description107),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description108),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description109),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description110),
			
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description111),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description112),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description113),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description114),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description115),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description116),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description117),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description118),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description119),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description120),
			
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description121),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description122),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description123),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description124),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description125),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description126),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description127),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description128),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description129),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description130),
			
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description131),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description132),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description133),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description134),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description135),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description136),
			BibleHymnApp.getApplication().getString(R.string.txt_kyodok_description137)
		};
	/*구교독문*/
	public static String[] old_kyodok_description_prayer = {
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description00),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description01),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description02),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description03),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description04),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description05),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description06),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description07),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description08),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description09),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description10),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description11),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description12),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description13),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description14),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description15),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description16),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description17),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description18),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description19),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description20),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description21),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description22),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description23),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description24),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description25),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description26),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description27),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description28),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description29),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description30),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description31),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description32),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description33),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description34),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description35),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description36),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description37),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description38),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description39),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description40),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description41),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description42),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description43),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description44),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description45),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description46),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description47),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description48),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description49),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description50),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description51),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description52),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description53),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description54),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description55),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description56),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description57),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description58),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description59),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description60),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description61),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description62),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description63),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description64),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description65),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description66),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description67),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description68),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description69),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description70),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description71),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description72),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description73),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description74),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description75),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description76),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description77),
			BibleHymnApp.getApplication().getString(R.string.txt_old_kyodok_description78)
		};
	/*심방*/
	public static String[] simbang_description_prayer = {
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description00),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description01),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description02),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description03),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description04),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description05),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description07),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description08),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description09),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description10),
			
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description11),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description12),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description13),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description14),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description15),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description16),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description17),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description18),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description19),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description19_5),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description20),
			
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description21),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description22),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description23),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description24),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description25),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description26),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description27),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description28),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description29),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description30),
			
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description31),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description32),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description33),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description34),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description35),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description36),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description37),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description38),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description39),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description40),
			
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description41),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description42),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description43),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description44),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description45),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description46),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description47),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description48),
			BibleHymnApp.getApplication().getString(R.string.txt_simbang_description49),
		};
	
}
