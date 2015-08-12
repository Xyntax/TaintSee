package com.taintflow.infos;

import java.util.ArrayList;
import java.util.List;

import com.taintflow.Service.LogEntry;
import com.taintflow.Service.MyTaintDroidNotifyService;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;



public class App_infos {
	static ArrayList< AppInfo> appInfos_all = null;
	public static ArrayList<AppInfo> getApp_Infos_All(Context paramContext)
	  {
		if (appInfos_all != null) {
			return appInfos_all;
		}
	    appInfos_all = new ArrayList<AppInfo>();
	    List<?> localList = paramContext.getPackageManager().getInstalledPackages(0);
	    for (int i = 0; ; i++)
	    {
	      if (i >= localList.size())
	        return appInfos_all;
	      PackageInfo localPackageInfo = (PackageInfo)localList.get(i);
	      AppInfo localAppInfo = new AppInfo();
	      localAppInfo.appName = localPackageInfo.applicationInfo.loadLabel(paramContext.getPackageManager()).toString();
	      localAppInfo.packageName = localPackageInfo.packageName;
	      localAppInfo.versionName = localPackageInfo.versionName;
	      localAppInfo.versionCode = localPackageInfo.versionCode;
	      localAppInfo.appIcon = localPackageInfo.applicationInfo.loadIcon(paramContext.getPackageManager());
	      if ((0x1 & localPackageInfo.applicationInfo.flags) == 0)
	        appInfos_all.add(localAppInfo);
	    }
	  }

	 

	  public static class AppInfo
	  {
	    private Drawable appIcon = null;
	    private String appName = "";
	    private String packageName = "";
	    private int versionCode = 0;
	    private String versionName = "";

	    public AppInfo()
	    { }

	    public Drawable getAppIcon()
	    {
	      return this.appIcon;
	    }

	    public String getAppName()
	    {
	      return this.appName;
	    }

	    public String getPackageName()
	    {
	      return this.packageName;
	    }

	    public int getVersionCode()
	    {
	      return this.versionCode;
	    }

	    public String getVersionName()
	    {
	      return this.versionName;
	    }
	  }
	  
	  public static int  getIndex_app(Context context,String Name_app) {
		  ArrayList<AppInfo> appInfos = getApp_Infos_All(context);
		  for (int i = 0; i < appInfos.size(); i++) {
			if (appInfos.get(i).getAppName().equals(Name_app)) {
				return i;
			}
		}
		  return -1;
	}
	  public static String  getPackageName_app(Context context,String Name_app) {
		  ArrayList<AppInfo> appInfos = getApp_Infos_All(context);
		  for (int i = 0; i < appInfos.size(); i++) {
			if (appInfos.get(i).getAppName().equals(Name_app)) {
				return appInfos.get(i).getPackageName();
			}
		}
		  return null;
	}
	  public static int  getIndex_packagename(Context context,String packageName) {
		  ArrayList<AppInfo> appInfos = getApp_Infos_All(context);
		  for (int i = 0; i < appInfos.size(); i++) {
			if (packageName .contains(appInfos.get(i).getPackageName())) {
				return i;
			}
		}
		  return -1;
	}
	

}
