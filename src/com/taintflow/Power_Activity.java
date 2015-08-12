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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.demo.taintsee.R;
import com.taintflow.Service.Main_activityService;
import com.taintflow.Service.PowerService;
import com.taintflow.infos.Infos;
import com.tiantfolw.listview_adapter.ListViewAdapter;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class Power_Activity extends ActionBarActivity {
	LinearLayout linearButtons;
	protected ListView listView = null;
	BaseAdapter listViewAdapter;
	Integer rel_position;
	ArrayList<Infos> infos;

	public void NO_Clicked(View paramView) throws IOException {
		int position = Integer.parseInt(paramView.getTag().toString());
		int tag = Integer.parseInt(
				this.listView.getChildAt(0).findViewById(R.id.ib_app_no).getTag().toString());
		
		TextView textView = (TextView) this.listView.getChildAt(position - tag)
				.findViewById(R.id.tv_name);
		
		
		
		Main_activityService.Change_OCP_col(PowerService.get_index_title(textView.getText().toString()));
		infos.set(position,PowerService
				.get_Chart_Info_List(this).get(position));
		listViewAdapter.notifyDataSetChanged();
	}

	public void OK_Clicked(View paramView) throws IOException {
		int position = Integer.parseInt(paramView.getTag().toString());
		int tag = Integer.parseInt(
				this.listView.getChildAt(0).findViewById(R.id.ib_app_no).getTag().toString());
		
		TextView textView = (TextView) this.listView.getChildAt(position - tag)
				.findViewById(R.id.tv_name);
		
		Main_activityService.Change_OCP_col(PowerService.get_index_title(textView.getText().toString()));
		infos.set(position,PowerService
				.get_Chart_Info_List(this).get(position));
		listViewAdapter.notifyDataSetChanged();
	}

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		
		setContentView(R.layout.activity_list);

		this.listView = ((ListView) findViewById(R.id.list));
		
		infos = PowerService
				.get_Chart_Info_List(this);
		this.listViewAdapter = new ListViewAdapter(this, infos);
		this.listView.setAdapter(this.listViewAdapter);
		
		this.listView
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
								Power_DetailActivity.class);
						Bundle localBundle = new Bundle();
						localBundle.putParcelable("Icon_taint", localBitmap);
						localBundle.putString("Title_taint", str);
						localIntent.putExtras(localBundle);
						startActivity(localIntent);
					}
				});

		/* } */

	}
	@Override
	protected void onRestart() {
		super.onRestart();
		infos = PowerService
				.get_Chart_Info_List(this);
		this.listViewAdapter = new ListViewAdapter(this, infos);
		this.listView.setAdapter(this.listViewAdapter);
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
