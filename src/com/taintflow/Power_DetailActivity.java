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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.taintsee.R;
import com.taintflow.Service.Main_activityService;
import com.taintflow.Service.PowerService;
import com.taintflow.Service.Power_detailService;
import com.taintflow.infos.App_infos;
import com.taintflow.infos.Infos;
import com.tiantfolw.listview_adapter.ListViewAdapter;

public class Power_DetailActivity extends ActionBarActivity {
	ImageView image;
	TextView text;
	RelativeLayout chart;
	ListView news_list;
	BaseAdapter listViewAdapter;

	ArrayList<Infos> charts;
	String title;

	Bundle localBundle;

	public void Init() {
		Intent localIntent = getIntent();
		localBundle = localIntent.getExtras();
		this.image.setImageBitmap((Bitmap) localBundle
				.getParcelable("Icon_taint"));
		title = localIntent.getStringExtra("Title_taint");
		this.text.setText(title);
		charts = Power_detailService.getApp_Info_List_Power(
				getApplicationContext(), title);
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
						localBundle.putParcelable("Icon_app", localBitmap);
						localBundle.putString("Name_app", str);
						localIntent.putExtras(localBundle);
						startActivity(localIntent);
					}
				});
	}

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.activity_detail);
		this.image = ((ImageView) findViewById(R.id.iv_detail_icon));
		this.text = ((TextView) findViewById(R.id.tv_detail_name));
		this.news_list = ((ListView) findViewById(R.id.list_detail));
		ImageButton imageButton = (ImageButton) findViewById(R.id.bt_detail_chart);
		imageButton.setVisibility(RelativeLayout.GONE);
		
		TextView list_name = (TextView) findViewById(R.id.tv_list_name);
		list_name.setText("应用列表");
		Init();
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		charts = Power_detailService.getApp_Info_List_Power(
				getApplicationContext(), title);
		listViewAdapter = new ListViewAdapter(this, charts);
		this.news_list.setAdapter(listViewAdapter);
	}

	public void NO_Clicked(View paramView) throws IOException {
		int position = Integer.parseInt(paramView.getTag().toString());
		int tag = Integer.parseInt(
				this.news_list.getChildAt(0).findViewById(R.id.ib_app_no).getTag().toString());
		
		TextView textView = (TextView) this.news_list.getChildAt(position - tag)
				.findViewById(R.id.tv_name);
		
		int index = App_infos.getIndex_app(this, textView.getText().toString());
		int colNumber = PowerService.get_index_title(title);
		

		Main_activityService.Change_OCP_point(index,colNumber);
		
		charts.set(position,Power_detailService.getApp_Info_List_Power(
				getApplicationContext(), title).get(position));
		listViewAdapter.notifyDataSetChanged();

	}

	public void OK_Clicked(View paramView) throws IOException {
		int position = Integer.parseInt(paramView.getTag().toString());
		int tag = Integer.parseInt(
				this.news_list.getChildAt(0).findViewById(R.id.ib_app_no).getTag().toString());
		
		TextView textView = (TextView) this.news_list.getChildAt(position - tag)
				.findViewById(R.id.tv_name);
		int index = App_infos.getIndex_app(this, textView.getText().toString());
		int colNumber = PowerService.get_index_title(title);
		

		Main_activityService.Change_OCP_point(index,colNumber);

		charts.set(position,Power_detailService.getApp_Info_List_Power(
				getApplicationContext(), title).get(position));
		listViewAdapter.notifyDataSetChanged();
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