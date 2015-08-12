package com.taintflow.Service;

import java.util.ArrayList;

import android.content.Context;

import com.taintflow.infos.App_infos;
import com.taintflow.infos.App_infos.AppInfo;
import com.taintflow.infos.Infos;

public class Power_detailService {

	public static ArrayList<Infos> getApp_Info_List_Power(Context context,
			String title) {

		ArrayList<AppInfo> arrayList = App_infos.getApp_Infos_All(context);
		ArrayList<Infos> resultList = new ArrayList<Infos>();
		int colNumber  = PowerService.get_index_title(title);

		for (int i = 0; i < arrayList.size(); i++) {
			Infos infos = new Infos(arrayList.get(i).getAppIcon(), arrayList
					.get(i).getAppName());
			
			infos.setEnable(Main_activityService.isPointEnable(i,colNumber));
			
			infos.setNumber(TaintInfoService.getNumber_appName_title(context,
					infos.getName(), title));
			resultList.add(infos);

		}
		return resultList;

	}
}