package com.taintflow.Service;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;

import com.demo.taintsee.MainActivity;
import com.taintflow.infos.App_infos;

public class TaintInfoService  {
	public ArrayList<NewsInfo> get_News_Info_List(Context context,
			String Package_Name,String taint) {
		ArrayList<NewsInfo> localArrayList = new ArrayList<NewsInfo>();
		Cursor cursor =MainActivity.taintInfo.select_package_name_Taint
				(Package_Name, taint);
			
		try {
			cursor.moveToLast();

			do {
				NewsInfo newsInfo = new NewsInfo();
				newsInfo.news_package_name = Package_Name;
				newsInfo.news_data = cursor.getString(
						cursor.getColumnIndex(MyTaintDroidNotifyService.KEY_DATA));
				newsInfo.news_ip = cursor
						.getString(
								cursor.getColumnIndex(MyTaintDroidNotifyService.KEY_IPADDRESS));
				newsInfo.news_taint = cursor.getString(
						cursor.getColumnIndex(MyTaintDroidNotifyService.KEY_TAINT));
				newsInfo.news_time = cursor
						.getString(
								cursor.getColumnIndex(MyTaintDroidNotifyService.KEY_TIMESTAMP));

				localArrayList.add(newsInfo);
			}while (cursor.moveToPrevious());
		} catch (Exception e) {
			
			e.printStackTrace();
		}finally{
			if (cursor != null) {
				cursor.close();
			}
		}

		return localArrayList;

	}
	
	public static int getNumber_appName(Context context,String app_Name){
		String package_Name = App_infos.getPackageName_app(context, app_Name);
		Cursor cursor = MainActivity.taintInfo.select_package_name(package_Name);
		int number = 0;
		try {
		number = cursor.getCount();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (cursor != null) {
				cursor.close();
			}
		}
		return number;
	}
	public static int getNumber_Title(Context context,String title){
		String taint=PowerService.taint[PowerService.get_index_title(title)];
		Cursor cursor = MainActivity.taintInfo.select_taint(taint);
		int number = 0;
		try {
			number = cursor.getCount();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (cursor != null) {
				cursor.close();
			}
		}
		return number;
	}
	public static int getNumber_appName_title(Context context,String appName,String title){
		String taint=PowerService.taint[PowerService.get_index_title(title)];
		String package_Name = App_infos.getPackageName_app(context, appName);
		Cursor cursor = MainActivity.taintInfo.select_package_name_Taint(package_Name, taint);
		int number = 0;
		try {
			number = cursor.getCount();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (cursor != null) {
				cursor.close();
			}
		}
		return number;
	}
	
	
	
	public static class NewsInfo {
		private String news_package_name;
		private String news_time;
		private String news_ip;
		private String news_data;
		private String news_taint;

		public String getNews_data() {
			return news_data;
		}

		public String getNews_ip() {
			return news_ip;
		}

		public String getNews_taint() {
			return news_taint;
		}

		public String getNews_package_name() {
			return this.news_package_name;
		}

		public String getNews_time() {
			return this.news_time;
		}

	}
}
