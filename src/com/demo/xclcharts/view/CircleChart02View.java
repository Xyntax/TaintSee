/**
 * Copyright 2014  XCL-Charts
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 	
 * @Project XCL-Charts 
 * @Description Android图表基类库
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @Copyright Copyright (c) 2014 XCL-Charts (www.xclcharts.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */
package com.demo.xclcharts.view;

import java.util.LinkedList;

import org.xclcharts.chart.CircleChart;
import org.xclcharts.chart.PieData;
import org.xclcharts.view.GraphicalView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;

/**
 * @ClassName CircleChart02View
 * @Description 图形图例子
 * @author XiongChuanLiang<br/>
 *         (xcl_168@aliyun.com)
 */
public class CircleChart02View extends GraphicalView {

	private String TAG = "CircleChart02View";
	private CircleChart chart = new CircleChart();

	// 设置图表数据源
	private LinkedList<PieData> mlPieData = new LinkedList<PieData>();
	private String mDataInfo = "";

	public CircleChart02View(Context context) {
		super(context);
		setPercentage(0);
		chartRender();
	}
	
	public CircleChart02View(Context context, AttributeSet attrs){   
        super(context, attrs);   
        setPercentage(0);
		chartRender();
	 }
	 
	 public CircleChart02View(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			setPercentage(0);
			chartRender();
	 }

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// 图所占范围大小
		chart.setChartRange(w, h);
	}

	public void chartRender() {
		try {
			// 设置信息
			chart.setAttributeInfo(mDataInfo);
			// 数据源
			chart.setDataSource(mlPieData);

			// 背景色
			chart.getBgCirclePaint().setColor(Color.rgb(255,255,255));
			// 深色
			chart.getFillCirclePaint().setColor(Color.rgb(56, 162, 230));
			// 信息颜色
			chart.getDataInfoPaint().setColor(Color.rgb(255,255,255));
			// 显示边框
			// chart.showRoundBorder();

		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	// 百分比
	public void setPercentage(int per) {
		// PieData(标签，百分比，在饼图中对应的颜色)
		mlPieData.clear();
		chart.getLabelPaint().setColor(Color.RED);
		chart.getLabelPaint().setTextSize(140);
		int color = Color.rgb(255,255,255);
		int mid = per;
		per /= 5;
		if (per < 40) {
			mDataInfo = "轻度威胁";
		} else if (per < 60) {
			mDataInfo = "中度威胁";
		} else if(per<99) {
			mDataInfo = "严重威胁";
		}else {
			mDataInfo = "爆表了！";
			per = 100;
		}
		mlPieData.add(new PieData(Integer.toString(mid) , per, color));

	}

	@Override
	public void render(Canvas canvas) {
		try {
			chart.render(canvas);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}
}
