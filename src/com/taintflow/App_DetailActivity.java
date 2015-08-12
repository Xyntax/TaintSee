package com.taintflow;

import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.taintsee.R;
import com.taintflow.Service.App_detailService;
import com.taintflow.Service.Main_activityService;
import com.taintflow.Service.PowerService;
import com.taintflow.infos.App_infos;
import com.taintflow.infos.Infos;
import com.tiantfolw.listview_adapter.ListViewAdapter;

public class App_DetailActivity extends ActionBarActivity {
	ImageView app_image;
	TextView app_text;
	RelativeLayout chart;
	ListView news_list;
	
	BaseAdapter listViewAdapter ;

	ArrayList<Infos> charts;
	String app_Name;

	Bundle localBundle;

	public void Init() {
		Intent localIntent = getIntent();
		localBundle = localIntent.getExtras();
		this.app_image.setImageBitmap((Bitmap) localBundle
				.getParcelable("Icon_app"));
		app_Name = localIntent.getStringExtra("Name_app");
		this.app_text.setText(app_Name);
		charts = App_detailService.get_Chart_Info_List_App(
				getApplicationContext(), app_Name);
		listViewAdapter = new ListViewAdapter(this, charts);
		this.news_list.setAdapter(listViewAdapter);
		
		this.news_list
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(
							AdapterView<?> paramAnonymousAdapterView,
							View localView, int paramAnonymousInt,
							long paramAnonymousLong) {

						ImageView localImageView = (ImageView) localView
								.findViewById(R.id.iv_icon);
						localImageView.setDrawingCacheEnabled(true);
						Bitmap localBitmap = Bitmap.createBitmap(localImageView
								.getDrawingCache());
						String str = ((TextView) localView
								.findViewById(R.id.tv_name)).getText()
								.toString();
						Intent localIntent = new Intent(
								paramAnonymousAdapterView.getContext(),
								TaintInfoActivity.class);
						localBundle.putParcelable("Icon_taint", localBitmap);
						localBundle.putString("Title_taint", str);
						localIntent.putExtras(localBundle);
						startActivity(localIntent);
					}
				});
	}

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.activity_detail);
		this.app_image = ((ImageView) findViewById(R.id.iv_detail_icon));
		this.app_text = ((TextView) findViewById(R.id.tv_detail_name));
		this.news_list = ((ListView) findViewById(R.id.list_detail));
		
		TextView list_name = (TextView) findViewById(R.id.tv_list_name);
		list_name.setText("ÒþË½ÁÐ±í");
		Init();
	}

	public void NO_Clicked(View paramView) throws IOException {
		int position = Integer.parseInt(paramView.getTag().toString());
		int tag = Integer.parseInt(
				this.news_list.getChildAt(0).findViewById(R.id.ib_app_no).getTag().toString());
		
		TextView textView = (TextView) this.news_list.getChildAt(position - tag)
				.findViewById(R.id.tv_name);
		
		int index = App_infos.getIndex_app(this, app_Name);
		int colnumber = PowerService.get_index_title(textView.getText().toString());
		
		Main_activityService.Change_OCP_point(index,colnumber);
		
		charts.set(position,App_detailService.get_Chart_Info_List_App(
				getApplicationContext(), app_Name).get(position));
		listViewAdapter.notifyDataSetChanged();
	}

	public void OK_Clicked(View paramView) throws IOException {
		int position = Integer.parseInt(paramView.getTag().toString());
		int tag = Integer.parseInt(
				this.news_list.getChildAt(0).findViewById(R.id.ib_app_no).getTag().toString());
		
		TextView textView = (TextView) this.news_list.getChildAt(position - tag)
				.findViewById(R.id.tv_name);
		
		int index = App_infos.getIndex_app(this, app_Name);
		int colnumber = PowerService.get_index_title(textView.getText().toString());
		
		Main_activityService.Change_OCP_point(index,colnumber);

		charts.set(position,App_detailService.get_Chart_Info_List_App(
				getApplicationContext(), app_Name).get(position));
		listViewAdapter.notifyDataSetChanged();
	}

	public void showcharts_app_info(View v) {
		Intent intent = new Intent(getApplicationContext(),
				app_detail_chartActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("app_Name", app_Name);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		charts = App_detailService.get_Chart_Info_List_App(
				getApplicationContext(), app_Name);
		listViewAdapter = new ListViewAdapter(this, charts);
		this.news_list.setAdapter(listViewAdapter);
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