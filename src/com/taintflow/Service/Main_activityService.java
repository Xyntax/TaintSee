package com.taintflow.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xclcharts.chart.PointD;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.database.Cursor;

import com.demo.xclcharts.view.SplineChart03View;
import com.taintflow.TaintFlowDataBaseHelper.TaintInfo;
import com.taintflow.infos.App_infos;
import com.taintflow.infos.App_infos.AppInfo;


public class Main_activityService {
	public static String true_String = " 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1";
	public static String false_String = " 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0";
	public static Boolean OCP[][];
	static Context context = null;
	public static String dayString;
	
	public static void init_OCP_Array(Context context){
		if (Main_activityService.context  == null) {
			Main_activityService.context = context;
		}
		ArrayList<AppInfo> applnfos = App_infos.getApp_Infos_All(context);
		String lines[];
		OCP = new Boolean[applnfos.size()][16];
		for (int i = 0; i < applnfos.size(); i++) {
			lines = Get_OCP_file_line( applnfos.get(i).getPackageName()).split(" ");
			for (int j = 1; j < lines.length; j++) {
				if (lines[j].equals("1")) {
					OCP[i][j-1] = true;
				}else {
					OCP[i][j-1] = false;
				}
				
			}
		}
		return;
	}

	
	public static String Get_OCP_file_line( String  package_name) {
		String line = null;
		String result = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(new File(
					context.getFilesDir(), "OCP.txt"));
			InputStreamReader inputStreamReader = new InputStreamReader(
					fileInputStream);
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			do {
				line = bufferedReader.readLine();
				if(line.contains(package_name)){
					result = line;break;
				}	
			}while (line!=null);
			
			bufferedReader.close();
			inputStreamReader.close();
			fileInputStream.close();
			if (result== null) {
				result= package_name + false_String;
			}
				return result;
		} catch (Exception localException) {
			localException.printStackTrace();
			Init_OCP_file(context);
			return  package_name + false_String;
		}

	}
	
	public static boolean isLineEnable(int lineNumber) {
		try {
			boolean result = false;
			for (int i = 0; i <16; i++) {
				result = result || OCP[lineNumber][i];
			}
			return result;
		} catch (Exception e) {
			return false;
		}
	}
	public static boolean isColEnable(int colNumber) {
		try {
			boolean result = false;
			for (int i = 0; i < OCP.length; i++) {
				result = result || OCP[i][colNumber];
			}
			return result;
		} catch (Exception e) {
			return false;
		}
	}
	public static boolean isPointEnable(int lineNumber,int colNumber) {
		try {
			return OCP[lineNumber][colNumber];
		} catch (Exception e) {
			return false;
		}
	}
	
	public static void save_OCP(){
		ArrayList<AppInfo> applnfos = App_infos.getApp_Infos_All(context);
		String[]ocpStrings = new String[applnfos.size()];
		String line;
		for (int i = 0; i < OCP.length; i++) {
			line = applnfos.get(i).getPackageName();
			for (int j = 0; j < 16; j++) {
				line += " ";
				if (OCP[i][j]) {
					line +="1";
				}else {
					line+="0";
				}
			}
			ocpStrings[i] = line;
		}
		Write_OCP(context, ocpStrings);
	}

	public static void Write_OCP(Context context, String input) {
		try {

			@SuppressWarnings("deprecation")
			FileOutputStream fileOutputStream =context.openFileOutput("OCP.txt", Context.MODE_WORLD_READABLE);
			/*new FileOutputStream(new File(context.getFilesDir(), "OCP.txt"));*/
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
					fileOutputStream);
			BufferedWriter bufferedWriter = new BufferedWriter(
					outputStreamWriter);

			bufferedWriter.write(input);

			bufferedWriter.close();
			outputStreamWriter.close();
			fileOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void Write_OCP(Context context, String[] inputs) {
		String input=new String();
		for (int i = 0; i < inputs.length; i++) {
			input+= inputs[i]+"\n";
		}
		Write_OCP(context, input);
	}

	public static void Init_OCP_file(Context context) {
		ArrayList<AppInfo> applnfos = App_infos.getApp_Infos_All(context);
		String input = new String();
		for (int i = 0; i < applnfos.size(); i++) {
			input += applnfos.get(i).getPackageName()
					+ false_String + '\n';
		}
		Write_OCP(context, input);
	}

	

	public static void Change_OCP_point(int lineNumber,int colNumber){	
			OCP[lineNumber][colNumber] = !isPointEnable(lineNumber, colNumber);
			save_OCP();
	}

	public static void Change_OCP_line(int lineNumber) {
		boolean mid = !isLineEnable(lineNumber);
		for (int i = 0; i < 16; i++) {
			OCP[lineNumber][i]=mid;
		}
		save_OCP();
	}

	public static void Change_OCP_col(int colNumber) {
		boolean mid = !isColEnable(colNumber);
		for (int i = 0; i < OCP.length; i++) {
			OCP[i][colNumber]=mid;
		}
		save_OCP();
	}

	

	public static boolean isServiceOpened(Context context) {
		ActivityManager mActivityManager = (ActivityManager) context
				.getSystemService(Activity.ACTIVITY_SERVICE);
		final String ClassName = "com.taintflow.Service.MyTaintDroidNotifyService";
		List<ActivityManager.RunningServiceInfo> mServiceList = mActivityManager
				.getRunningServices(100);
		return MusicServiceIsStart(mServiceList, ClassName);

	}

	// 通过Service的类名来判断是否启动某个服务
	private static boolean MusicServiceIsStart(
			List<ActivityManager.RunningServiceInfo> mServiceList,
			String className) {

		for (int i = 0; i < mServiceList.size(); i++) {
			if (className.equals(mServiceList.get(i).service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	@SuppressLint("SimpleDateFormat")
	public static int get_number_data(Context context) {
		Date day;
		int number;

			day = SplineChart03View.getDateBeforeOrAfter(0);
			SimpleDateFormat ss = new SimpleDateFormat("MM-dd");
			dayString = ss.format(day);

			
			number  = SplineChart03View.getNumberday(context, dayString);
			return number;
			
	}
}


