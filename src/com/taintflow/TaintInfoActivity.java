package com.taintflow;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.taintsee.R;
import com.taintflow.Service.PowerService;
import com.taintflow.Service.TaintInfoService;
import com.taintflow.Service.TaintInfoService.NewsInfo;
import com.taintflow.infos.App_infos;

public class TaintInfoActivity extends ActionBarActivity {
	ImageView taint_icon,app_icon;
	TextView taint_title,app_name;
	
	RelativeLayout chart;
	ListView news_list;

	String package_name;
	String taint;
	TextView describ;
	ArrayList<NewsInfo> newsInfos;

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		
		Intent localIntent = getIntent();		
		package_name = App_infos.getPackageName_app(getApplicationContext()
				, localIntent.getStringExtra("Name_app"));
		taint = localIntent.getStringExtra("Title_taint");
		taint = PowerService.taint[PowerService.get_index_title(taint)];
		newsInfos = new TaintInfoService().get_News_Info_List(
				getApplicationContext(), package_name, taint);
		if (newsInfos.isEmpty()) {
			setContentView(R.layout.empty);
			return;
		}
		setContentView(R.layout.activity_taint_info);
		this.taint_icon = ((ImageView) findViewById(R.id.iv_taint_icon));
		this.taint_title = ((TextView) findViewById(R.id.tv_taint_name));
		this.app_icon = (ImageView) findViewById(R.id.iv_app_icon);
		this.app_name = (TextView) findViewById(R.id.tv_app_name);
		this.news_list = ((ListView) findViewById(R.id.list_taint));
		describ = (TextView) findViewById(R.id.tv_describ);
		

		Bundle localBundle =  localIntent.getExtras();
		this.taint_icon.setImageBitmap((Bitmap) localBundle
				.getParcelable("Icon_taint"));
		this.taint_title.setText(localIntent.getStringExtra("Title_taint"));
		app_icon.setImageBitmap((Bitmap) localBundle
				.getParcelable("Icon_app"));
		this.app_name.setText(localIntent.getStringExtra("Name_app"));
		
		
		
		
		BaseAdapter listViewAdapter = new TaintInfoListViewAdapter(this,
				newsInfos);
		this.news_list.setAdapter(listViewAdapter);

	}
	@Override
	protected void onRestart() {
		super.onRestart();
		newsInfos = new TaintInfoService().get_News_Info_List(
				getApplicationContext(), package_name, taint);
		BaseAdapter listViewAdapter = new TaintInfoListViewAdapter(this,
				newsInfos);
		this.news_list.setAdapter(listViewAdapter);
	}
	
	public void show_describ(View view){
		String text = taint_title.getText().toString();
		text = PowerService.decsrib[PowerService.get_index_title(text)];
		Builder builder = new  AlertDialog.Builder(this) ;
		builder.setMessage(text);
		builder.setPositiveButton("È·¶¨" ,  null );
		builder.show();
	
		/*describ.setText(text);
		describ.setVisibility(View.VISIBLE);
		
		TextView blur = (TextView) findViewById(R.id.blur);
		blur.setVisibility(View.VISIBLE);
		blur.setOnClickListener(new listener());*/
	}
	
	class listener implements OnClickListener{
		@Override
		public void onClick(View v) {
			describ.setVisibility(View.GONE);
			v.setVisibility(View.GONE);
		}
	}

	class TaintInfoListViewAdapter extends BaseAdapter {
		private Context context;
		private ArrayList<TaintInfoService.NewsInfo> data;
		Boolean showed = Boolean.valueOf(false);

		public TaintInfoListViewAdapter(Context paramContext,
				ArrayList<NewsInfo> arrayList) {
			this.context = paramContext;
			this.data = arrayList;
		}

		public int getCount() {
			return this.data.size();
		}

		public Object getItem(int paramInt) {
			return this.data.get(paramInt);
		}

		public long getItemId(int paramInt) {
			return paramInt;
		}

		@SuppressLint({ "InflateParams" })
		public View getView(int paramInt, View paramView,
				ViewGroup paramViewGroup) {
			if (paramView == null) {
				paramView = LayoutInflater.from(this.context).inflate(
						R.layout.everyline_taint_infol, null);
				paramView.setTag(Integer.valueOf(paramInt));
			}
			TextView localTextView1 = (TextView) paramView
					.findViewById(R.id.DetailTimestampTextView);
			TextView localTextView2 = (TextView) paramView
					.findViewById(R.id.DetailIPTextView);
			TextView localTextView3 = (TextView) paramView
					.findViewById(R.id.DetailDataTextView);
			localTextView1.setText(((TaintInfoService.NewsInfo) this.data
					.get(paramInt)).getNews_time());
			localTextView2.setText(((TaintInfoService.NewsInfo) this.data
					.get(paramInt)).getNews_ip());
			localTextView3.setText(((TaintInfoService.NewsInfo) this.data
					.get(paramInt)).getNews_data());
			return paramView;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressLint("NewApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.home) {
			Intent upIntent = NavUtils.getParentActivityIntent(this);
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				TaskStackBuilder.create(this)
						.addNextIntentWithParentStack(upIntent)
						.startActivities();
				return true;
			}

			return true;
		}
		return false;
	}
}