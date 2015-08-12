package com.taintflow.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.demo.taintsee.MainActivity;
import com.taintflow.TaintFlowDataBaseHelper.TaintInfo;
import com.taintflow.infos.App_infos;

public class MyTaintDroidNotifyService extends Service {
	private static final String TAG = MyTaintDroidNotifyService.class
			.getSimpleName();

	private static Hashtable<Integer, String> ttable = new Hashtable<Integer, String>();
	static {
		// ttable.put(new Integer(0x00000000), "No taint");
		ttable.put(new Integer(0x00000001), "Location");
		ttable.put(new Integer(0x00000002), "Address Book (ContactsProvider)");
		ttable.put(new Integer(0x00000004), "Microphone Input");
		ttable.put(new Integer(0x00000008), "Phone Number");
		ttable.put(new Integer(0x00000010), "GPS Location");
		ttable.put(new Integer(0x00000020), "NET-based Location");
		ttable.put(new Integer(0x00000040), "Last known Location");
		ttable.put(new Integer(0x00000080), "camera");
		ttable.put(new Integer(0x00000100), "accelerometer");
		ttable.put(new Integer(0x00000200), "SMS");
		ttable.put(new Integer(0x00000400), "IMEI");
		ttable.put(new Integer(0x00000800), "IMSI");
		ttable.put(new Integer(0x00001000), "ICCID (SIM card identifier)");
		ttable.put(new Integer(0x00002000), "Device serial number");
		ttable.put(new Integer(0x00004000), "User account information");
		ttable.put(new Integer(0x00008000), "browser history");
	}

	private volatile static boolean isRunning = false;

	public static final String KEY_APPNAME = "KEY_APPNAME";
	public static final String KEY_IPADDRESS = "KEY_IPADDRESS";
	public static final String KEY_TAINT = "KEY_TAINT";
	public static final String KEY_DATA = "KEY_DATA";
	public static final String KEY_ID = "KEY_ID";
	public static final String KEY_TIMESTAMP = "KEY_TIMESTAMP";

	public static class Starter extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (!isRunning && intent.getAction() != null) {
				if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
					context.startService(new Intent(context,
							MyTaintDroidNotifyService.class));
				}
			}
		}
	};

	private BlockingQueue<LogEntry> logQueue;
	private static final int LOGQUEUE_MAXSIZE = 4096;

	private volatile boolean doCapture = false;
	private Thread captureThread = null;

	private class Producer implements Runnable {
		private final BlockingQueue<LogEntry> queue;

		Producer(BlockingQueue<LogEntry> q) {
			queue = q;
		}

		public void run() {
			LogcatDevice lc = LogcatDevice.getInstance();		
			while (doCapture && lc.isOpen()) {
				try {
					// read an entry and insert it to our content provider
					LogEntry le = lc.readLogEntry();
					if (le != null) {
						queue.put(le);
					}
				} catch (Exception e) {
					Log.e(TAG, "Could not read a log entry: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
	};

	private volatile boolean doRead = false;
	private Thread readThread = null;

	private class Consumer implements Runnable {
		private final BlockingQueue<LogEntry> queue;

		Consumer(BlockingQueue<LogEntry> q) {
			queue = q;
		}

		public void run() {
			while (doRead) {
				try {
					LogEntry le = (LogEntry) queue.take();
					processLogEntry(le);
				} catch (InterruptedException e) {
					Log.e(TAG, "Could not read log entry: " + e.getMessage());
				}
			}
		}
	}

	private String get_processname(int pid) {
		ActivityManager mgr = (ActivityManager) getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);

		String pname = "";
		List<RunningAppProcessInfo> apps = mgr.getRunningAppProcesses();
		for (RunningAppProcessInfo pinfo : apps) {
			if (pinfo.pid == pid) {
				pname = pinfo.processName;
				break;
			}
		}

		return pname;
	}
	private int get_pid_msg(String msg){
		int pid ;
		pid = Integer.valueOf(msg.substring(msg.indexOf("pid")+4).trim());
		return pid;
	}

	private String get_ipaddress(String msg) {
		Pattern p = Pattern.compile("\\((.+)\\) ");
		Matcher m = p.matcher(msg);

		if (m.find() && m.groupCount() > 0) {
			String result = m.group(1);
			// remove trailing junk
			if (result.contains(")"))
				result = result.substring(0, result.indexOf(")") - 1);
			return result;
		} else {
			return null;
		}
	}

	@SuppressLint("UseValueOf")
	private String get_taint(String msg) {
		// match hex digits
		Pattern p = Pattern.compile("with tag 0x(\\p{XDigit}+) ");
		Matcher m = p.matcher(msg);

		if (m.find() && m.groupCount() > 0) {

			String match = m.group(1);

			// get back int
			int taint;
			try {
				taint = Integer.parseInt(match, 16);
			} catch (NumberFormatException e) {
				return "Unknown Taint: " + match;
			}

			if (taint == 0x0) {
				return "No taint";
			}

			// for each taint
			ArrayList<String> list = new ArrayList<String>();
			int t;
			String tag;

			// check each bit
			for (int i = 0; i < 32; i++) {
				t = (taint >> i) & 0x1;
				tag = ttable.get(new Integer(t << i));
				if (tag != null) {
					list.add(tag);
				}
			}

			// build output
			StringBuilder sb = new StringBuilder("");
			if (list.size() > 1) {
				for (int i = 0; i < list.size() - 1; i++) {
					sb.append(list.get(i) + ", ");
				}
				sb.append(list.get(list.size() - 1));
			} else {
				if (!list.isEmpty()) {
					sb.append(list.get(0));
				}
			}

			return sb.toString();
		} else {
			return "No Taint Found";
		}
	}

	private String get_data(String msg) {
		int start = msg.indexOf("data=[") + 6;
		return msg.substring(start);
		
	}

	private boolean isTaintedSend(String msg) {
		// covers "libcore.os.send" and "libcore.os.sendto"
		return msg.contains("libcore.os.send");
	}

	private boolean isTaintedSSLSend(String msg) {
		return msg.contains("SSLOutputStream.write");
	}
	private boolean isCA_write(String msg) {
		return msg.contains("CA_write");
	}
	

	@SuppressLint("UseValueOf")
	private void processLogEntry(LogEntry le) {
		
		String msg = le.getMessage();
		boolean taintedSend = isTaintedSend(msg);
		boolean taintedSSLSend = isTaintedSSLSend(msg);
	

		
		if (taintedSend || taintedSSLSend) {
				String app_2 = get_processname(le.getPid()).split(":")[0];
			
				//String app_1 = get_processname(get_pid_msg(msg)).split(":")[0];
				
				
				//Log.e("test 1",app_1);
				//Log.e("test 2",app_2);
				
				/*if (!app_1.equals(app_2) ) {
					doneLog(le,app_1);
				}*/
			
				doneLog(le, app_2);
		}
	}
	
	@SuppressLint("UseValueOf")
	public  void doneLog(LogEntry le,String app_name){
		///done_before(le, app_name);
		String timestamp = le.getTimestamp();

		String msg = le.getMessage();

		boolean taintedSSLSend = isTaintedSSLSend(msg);

			String ip = get_ipaddress(msg);
			String taint = get_taint(msg);
			String app =app_name;
			String data = get_data(msg);
			String id = new Integer(le.hashCode()).toString();
			
			if (app != null) {	
				if (taintedSSLSend)
					ip = ip + " (SSL)";
				int index = App_infos.getIndex_packagename(this, app);
				TaintInfo taintInfo = MainActivity.taintInfo;
				data = data.replace("]", "");
				
				//Log.e("test",data);
				
					for (int i = 0; i < PowerService.taint.length; i++) {
						
						if (taint.contains(PowerService.taint[i])) {
							String dataString = data;
							//½âÃÜ£º
							if (Main_activityService.isPointEnable(index,i)) {		
								SMS4 sm4 = new SMS4();
								dataString = data;
								dataString = sm4.DECRYPT(dataString); 
							}
							
							Bundle extras = new Bundle();
							extras.putString(KEY_APPNAME, app);
							extras.putString(KEY_IPADDRESS, ip);
							extras.putString(KEY_TAINT, PowerService.taint[i]);
							extras.putString(KEY_DATA, dataString);
							extras.putString(KEY_TIMESTAMP, timestamp);
							extras.putString(KEY_ID, id);
							
							taintInfo.insert(extras);
						}
					}
		
			}

		}
		
	

	@Override
	public IBinder onBind(Intent intent) {
		// we don't bind to this service
		return null;
	}

	/*@Override
	public void onCreate() {
		return;
	}*/

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (isRunning) {
			return START_NOT_STICKY;
		}
		TaintInfo taintInfo = MainActivity.taintInfo;
		taintInfo.onCreate(taintInfo.getWritableDatabase());
		logQueue = new ArrayBlockingQueue<LogEntry>(LOGQUEUE_MAXSIZE);
		this.captureThread = new Thread(new Producer(logQueue));
		captureThread.setDaemon(true);

		this.readThread = new Thread(new Consumer(logQueue));
		readThread.setDaemon(true);

		try {
			LogcatDevice.getInstance().open();
		} catch (IOException e) {
			Log.e(TAG, "Could not open the log device: " + e.getMessage());
			return START_NOT_STICKY;
		}

		this.doCapture = true;
		captureThread.start();

		this.doRead = true;
		readThread.start();

		isRunning = true;

		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// stop the thread
		this.doCapture = false;
		this.doRead = false;

		// close the log
		try {
			LogcatDevice.getInstance().close();
		} catch (IOException e) {
			Log.e(TAG,
					"Could not close the log device properly: "
							+ e.getMessage());
		}

		// destroy the thread
		this.captureThread = null;
		this.readThread = null;
	}
}
