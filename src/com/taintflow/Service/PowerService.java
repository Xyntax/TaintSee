package com.taintflow.Service;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.demo.taintsee.R;
import com.taintflow.infos.Infos;

public class PowerService {
	static ArrayList<Infos> localArrayList = null;
	public static String[] titles = { "地理位置信息", "通讯录", "麦克风输入信息", "手机号码",
			"GPS", "网络地址", "最近的地理位置信息", "照相机输入信息", "传感器信息", "短信", "浏览器历史记录",
			"用户账户信息", "ICCID", "手机设备序列号", "IMEI", "IMSI" };
	public static String[] taint = { "Location",
			"Address Book", "Microphone Input",
			"Phone Number", "GPS Location", "NET-based Location",
			"Last known Location", "camera", "accelerometer", "SMS",
			"browser history", "User account information",
			"ICCID", "Device serial number", "IMEI",
			"IMSI" };
	public static String[] decsrib = { 
		"容易被恶意应用利用，将地理位置信息发送给广告商，谋取利益。",
		"恶意应用、木马病毒可能会盗取通讯录中联系人信息，倒卖给信息贩子，为不法分子所利用。"
		, "恶意开发者或黑客会利用麦克风来监听用户通话或手机附近的声音，达到盗取用户通话记录，甚至监听用户的目的。",
		"恶意应用和木马病毒可能会盗取手机号码，进行倒卖手机号码，拨动骚扰电话等行为。", 
		"存在大量LBS(基于位置的服务)应用程序会使用用户的GPS地理位置信息，可能将用户的GPS地理位置信息泄漏给广告运行商。",
		"恶意广告插件盗取用户的网络地址信息，测量用户的地理位置，造成用户的网络地址信息和地理位置信息的隐私泄漏。",
		"黑客可以通过系统漏洞窃取用户最近的地理位置信息，达到跟踪用户的目的。", 
		"恶意应用与木马病毒可能会利用摄像头设备来实时监控用户日常生活，偷窥用户隐私，侵犯公民的隐私权。",
		"恶意应用可以通过判读传感器的资料就能辨别出它是来自哪一部手机，即使用户不想分享自己的位置、姓名或是其他信息。",
		"存在部分恶意应用和木马病毒读取用户的短信，将内容发送到远程服务器。甚至，自动发送短信，彩信等，导致用户损失大量话费。",
		"恶意浏览器插件盗取用户的浏览器的历史记录信息，转卖给广告商，通过分析用户的浏览记录，来推广用户感兴趣的相应广告。", 
		"这类信息常常被恶意应用与木马病毒所窃取，通过登录用户的账户，来盗取用户隐私数据，造成严重的隐私数据泄漏问题。",
		"集成电路卡识别码：木马病毒会利用ICCID来判断Android手机用户的运营商是哪个，根据不同运营商来采取不同的攻击方式。",
		"这一点也被黑客所利用序列号，来核实Android手机的生产信息，针对不同机型手机采取不同的攻击方式。", 
		"移动设备国际识别码：存在大量的应用程序窃取IMEI来唯一标识一个用户账户或手机。",
		"国际移动用户识别码：恶意应用盗取IMSI来识别移动用户的身份。" };

	public static int[] icon_power = { R.drawable.location, R.drawable.contact,
			R.drawable.audio, R.drawable.phone_number, R.drawable.gps,
			R.drawable.internet, R.drawable.last_location, R.drawable.camera,
			R.drawable.sensor, R.drawable.sms, R.drawable.history_bookmarks,
			R.drawable.account, R.drawable.iccid, R.drawable.device_sn,
			R.drawable.imei, R.drawable.imsi, };

	public static ArrayList<Infos> get_Chart_Info_List(Context context) {
		Resources resources = context.getResources();
		Drawable drawable;
		ArrayList<Infos> localArrayList = new ArrayList<Infos>();
		Infos infos;
		for (int i = 0; i < titles.length; i++) {
			drawable = resources.getDrawable(icon_power[i]);
			infos = new Infos(drawable, titles[i]);
			
				infos.setEnable(Main_activityService.isColEnable(i));
		
			infos.setNumber(TaintInfoService.getNumber_Title(context, titles[i]));
			localArrayList.add(infos);

		}

		return localArrayList;
	}
	public static int get_index_taint(String taint){
		for (int i = 0; i < PowerService.taint.length; i++) {
			if (taint .equals(PowerService.taint[i])) {
				return i;
			}
		}
		return -1;
	}
	public static ArrayList<Infos> get_Chart_Info_List_old(Context context) {
		Resources resources = context.getResources();
		Drawable drawable;
		if (localArrayList != null) {
			return localArrayList;
		}
		localArrayList = new ArrayList<Infos>();
		Infos infos;
		for (int i = 0; i < titles.length; i++) {
			drawable = resources.getDrawable(icon_power[i]);
			infos = new Infos(drawable, titles[i]);
			localArrayList.add(infos);
		}

		return localArrayList;
	}

	public static ArrayList<String> get_Taint_List(Context context) {
		ArrayList<String> localArrayList = new ArrayList<String>();
		for (int i = 0;; i++) {
			if (i >= taint.length)
				return localArrayList;
			
				localArrayList.add(taint[i]);

		}
	}

	public static ArrayList<String> get_title_List(Context context) {
		ArrayList<String> localArrayList = new ArrayList<String>();
		for (int i = 0;; i++) {
			if (i >= titles.length)
				return localArrayList;
				localArrayList.add(titles[i]);

		}
	}

	public static int get_index_title(String title) {
		int index;
		for (index = 0; index < titles.length; index++) {
			if (title.equals(PowerService.titles[index])) {
				return index;
			}
		}
		return -1;
	}
}
