package com.taintflow.Service;

import java.util.ArrayList;

import android.content.Context;

import com.taintflow.infos.App_infos;
import com.taintflow.infos.Infos;

public class App_detailService {

	public static ArrayList<Infos> get_Chart_Info_List_App(Context context,
			String name_app) {
		int index = App_infos.getIndex_app(context, name_app);
		
		Infos infos;
		ArrayList<Infos> arrayList =  PowerService.get_Chart_Info_List_old(context);
		ArrayList<Infos> res_ArrayList =  new ArrayList<Infos>();
		for (int i = 0; i < arrayList.size(); i++) {
			infos =arrayList.get(i);
			
			infos.setEnable(Main_activityService.isPointEnable(index, i));
			
			infos.setNumber(TaintInfoService.getNumber_appName_title(context, name_app, infos.getName()));
			res_ArrayList.add(infos);
		}
		
		return res_ArrayList;
	}


}
