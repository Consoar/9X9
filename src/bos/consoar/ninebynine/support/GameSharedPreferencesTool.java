package bos.consoar.ninebynine.support;

import android.content.Context;
import android.content.SharedPreferences;

public class GameSharedPreferencesTool {
	public static Integer getHighSocre(Context c) {
		SharedPreferences Mysettings = c.getSharedPreferences("User_Data", 0);
		return Mysettings.getInt("High_Socre", 0);
	}

	public static void setHighSocre(Context c, Integer x) {
		SharedPreferences.Editor localEditor = c.getSharedPreferences(
				"User_Data", 0).edit();
		localEditor.putInt("High_Socre", x).commit();
	}
}
