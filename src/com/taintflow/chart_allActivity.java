package com.taintflow;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.taintsee.R;
import com.demo.xclcharts.view.SplineChart03View;
import com.demo.xclcharts.view.StackBarChart02View;

public class chart_allActivity extends Activity {

	private FrameLayout frameLayout;
	private StackBarChart02View show_all;
	private SplineChart03View show_week;
	private TextView mark_all,mark_week;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.chart_all);

		frameLayout = (FrameLayout) findViewById(R.id.fl_chart);
		DisplayMetrics dm = getResources().getDisplayMetrics();
		int scrWidth = (int) (dm.widthPixels * 0.9);
		int scrHeight = (int) (dm.heightPixels);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				scrWidth, scrHeight);
		show_all = new StackBarChart02View(this);
		show_week = new SplineChart03View(this);
		mark_all = (TextView) findViewById(R.id.tv_mark_all);
		mark_week = (TextView) findViewById(R.id.tv_mark_week);

		final RelativeLayout chartLayout = new RelativeLayout(this);

		chartLayout.addView(show_all, layoutParams);
		chartLayout.addView(show_week, layoutParams);

		frameLayout.addView(chartLayout);

		show_week.setVisibility(View.GONE);

	}

	public void show_week(View view) {
		show_all.setVisibility(View.GONE);
		mark_all.setVisibility(View.GONE);
		show_week.setVisibility(View.VISIBLE);
		mark_week.setVisibility(View.VISIBLE);

	}

	public void show_all(View view) {
		show_all.setVisibility(View.VISIBLE);
		show_week.setVisibility(View.GONE);
		
		mark_all.setVisibility(View.VISIBLE);
		mark_week.setVisibility(View.GONE);

	}

}