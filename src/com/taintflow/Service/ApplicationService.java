package com.taintflow.Service;

import java.util.ArrayList;

import android.content.Context;

import com.taintflow.infos.App_infos;
import com.taintflow.infos.App_infos.AppInfo;
import com.taintflow.infos.Infos;

public class ApplicationService {

	public static ArrayList<Infos> getApp_Info_List(Context context) {

		ArrayList<AppInfo> appInfos = App_infos.getApp_Infos_All(context);
		ArrayList<Infos> localArrayList = new ArrayList<Infos>();
		Infos infos;
		AppInfo appInfo;
		for (int i = 0; i < appInfos.size(); i++) {
			appInfo = appInfos.get(i);
			infos = new Infos(appInfo.getAppIcon(), appInfo.getAppName());		
			infos.setEnable(Main_activityService.isLineEnable(i));
			infos.setNumber(TaintInfoService.getNumber_appName(context,
			     infos.getName()));

			localArrayList.add(infos);

		}
		return localArrayList;

	}

}
