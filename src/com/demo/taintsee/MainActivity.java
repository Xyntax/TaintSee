package com.demo.taintsee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.taintflow.Application_Activity;
import com.taintflow.Power_Activity;
import com.taintflow.chart_allActivity;
import com.taintflow.Service.Main_activityService;
import com.taintflow.Service.MyTaintDroidNotifyService;
import com.taintflow.TaintFlowDataBaseHelper.TaintInfo;
import com.taintflow.infos.App_infos;

public class MainActivity extends Activity {
	
	public static int MAX_NUMBER_DATA = 10;
	public static String  SERVICE_OPENED = "服务已开启";
	public static String  SERVICE_CLOSED = "服务已关闭";
	public static TaintInfo taintInfo = null;
	
	com.demo.xclcharts.view.CircleChart02View chart = null;

	int percentage ;
	TextView tv_isServiceOpened;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Main_activityService.init_OCP_Array(this);
		if (taintInfo == null) {
			taintInfo =  new TaintInfo(getApplicationContext());
		}
		
		int number_data  = Main_activityService.get_number_data(this);
		   percentage = number_data;
		   int number_app =App_infos.getApp_Infos_All(this).size();
		   TextView tv_number_app= (TextView) findViewById(R.id.tv_number_app);
		   tv_number_app.setText(String.valueOf(number_app));
		   chart = (com.demo.xclcharts.view.CircleChart02View) findViewById(R.id.circle_view);
			chart.setPercentage(percentage);
			chart.chartRender();
			chart.invalidate();
			
			
		   
		   Button begin = (Button) findViewById(R.id.button_main_begin);
		   
		   tv_isServiceOpened = (TextView) findViewById(R.id.tv_isServiceOpened);
		   if (Main_activityService.isServiceOpened(this)) {
			tv_isServiceOpened.setText(SERVICE_OPENED);
			begin.setBackgroundResource(R.drawable.begin_open);
			
		}else {
			tv_isServiceOpened.setText(SERVICE_CLOSED);
			begin.setBackgroundResource(R.drawable.begin_close);
		}
		

	}
	
	public void	 changeServiceNature(View view) {
		Intent intent = new Intent(this, MyTaintDroidNotifyService.class);
		if(tv_isServiceOpened.getText().toString().equals(SERVICE_OPENED)){
			stopService(intent);
			tv_isServiceOpened.setText(SERVICE_CLOSED);
			view.setBackgroundResource(R.drawable.begin_close);
			
		}else {
			startService(intent);
			tv_isServiceOpened.setText(SERVICE_OPENED);
			view.setBackgroundResource(R.drawable.begin_open);
		}
	}

	public void show_power(View v) {
		Intent intent = new Intent(getApplicationContext(),
				Power_Activity.class);
		startActivity(intent);
	}

	public void show_application(View v) {
		Intent intent = new Intent(getApplicationContext(),
				Application_Activity.class);
		startActivity(intent);
	}

	public void showchart_all(View v) {

		Intent intent = new Intent(getApplicationContext(),
				chart_allActivity.class);
		startActivity(intent);
	}

	public void showchart_week(View v) {
		/*Intent intent = new Intent(getApplicationContext(),
				connect_us.class);
		startActivity(intent);*/
		
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		if (taintInfo == null) {
			taintInfo =  new TaintInfo(getApplicationContext());
		}
		int number_data  = Main_activityService.get_number_data(this);
		percentage = number_data;
		chart.setPercentage(percentage);
		chart.chartRender();
		chart.invalidate();
		
	}

}