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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.xclcharts.chart.BarData;
import org.xclcharts.chart.StackBarChart;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.common.IFormatterDoubleCallBack;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.event.click.BarPosition;
import org.xclcharts.renderer.XChart;
import org.xclcharts.renderer.XEnum;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.demo.taintsee.MainActivity;
import com.taintflow.Service.ApplicationService;
import com.taintflow.Service.PowerService;
import com.taintflow.TaintFlowDataBaseHelper.TaintInfo;
import com.taintflow.infos.App_infos;
import com.taintflow.infos.Infos;

/**
 * @ClassName StackBarChart02View
 * @Description 堆叠图 的例子(横向)
 * @author XiongChuanLiang<br/>
 *         (xcl_168@aliyun.com)
 */
public class StackBarChart02View extends DemoView {
	
	public static int[]colors =  {
		Color.rgb(80,80,80),
		Color.rgb(31,186,182),		
		Color.rgb(255,201,8),
		Color.rgb(253,164,0),
		
		Color.rgb(251,217,6),		
		Color.rgb(44,13,132),
		Color.rgb(203,222,106),
		Color.rgb(124,12,124),
		
		Color.rgb(87,11,133),
		Color.rgb(2,155,235),
		Color.rgb(2,46,153),
		Color.rgb(166,16,80),
		
		Color.rgb(220,46,19),
		Color.rgb(234,92,16),
		Color.rgb(1,88,181),
		Color.rgb(0,0,0),
	};

	private String TAG = "StackBarChart02View";
	private StackBarChart chart = new StackBarChart();

	// 标签轴
	List<String> chartLabels = new LinkedList<String>();
	List<BarData> BarDataSet = new LinkedList<BarData>();

	Paint pToolTip = new Paint(Paint.ANTI_ALIAS_FLAG);

	Context context;

	ArrayList< Infos>app_Infos;
	ArrayList<String> taint_Infos;

	public StackBarChart02View(Context context) {
		super(context);
		this.context = context;
		initView();
	}

	public StackBarChart02View(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public StackBarChart02View(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	private void initView() {
		app_Infos = ApplicationService.getApp_Info_List(context);
		chartLabels();
		chartDataSet();
		chartRender();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// 图所占范围大小
		chart.setChartRange(w, h);
	}

	private void chartRender() {
		try {

			// 设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
			int[] ltrb = getBarLnDefaultSpadding();
			chart.setPadding(DensityUtil.dip2px(getContext(), 50), ltrb[1],
					ltrb[2], ltrb[3]);

			// 显示边框
			chart.showRoundBorder();

			// 指定显示为横向柱形
			chart.setChartDirection(XEnum.Direction.HORIZONTAL);
			// 数据源
			chart.setCategories(chartLabels);
			chart.setDataSource(BarDataSet);

			// 坐标系
			int max = 0;
			
			for (int i = 0; i < app_Infos.size(); i++) {
				if (max < app_Infos.get(i).getNumber()) {
					max = app_Infos.get(i).getNumber();
				}
			}
			max *=3;
			int step = max/10 +1;
			chart.getDataAxis().setAxisMax(step * 10);
			chart.getDataAxis().setAxisMin(0);
			chart.getDataAxis().setAxisSteps(step);
			// 指定数据轴标签旋转-45度显示
			chart.getCategoryAxis().setTickLabelRotateAngle(-45f);

			// 标题
			chart.setTitle("隐私泄漏统计图");
			chart.addSubtitle("");
			chart.setTitleAlign(XEnum.HorizontalAlign.CENTER);
			
			chart.setApplyBackgroundColor(true);
			chart.setBackgroundColor(Color.rgb(255,255,255));
			chart.getBorder().setBorderLineColor(Color.rgb(179, 147, 197));

			// 图例
			chart.getAxisTitle().setLowerTitle("");

			// 背景网格
			chart.getPlotGrid().showVerticalLines();
			chart.getPlotGrid().getVerticalLinePaint()
			.setColor(Color.rgb(179, 147, 197));
			chart.getPlotGrid().setVerticalLineStyle(XEnum.LineStyle.SOLID);

			// 定义数据轴标签显示格式
			chart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack() {

				@Override
				public String textFormatter(String value) {

					DecimalFormat df = new DecimalFormat("#0");
					Double tmp = Double.parseDouble(value);
					String label = df.format(tmp).toString();
					return label;
				}

			});

			// 定义标签轴标签显示颜色
			/*chart.getCategoryAxis().getTickLabelPaint()
					.setColor(Color.rgb(1, 188, 242));*/

			// 定义柱形上标签显示格式
			chart.getBar().setItemLabelVisible(true);
			chart.setItemLabelFormatter(new IFormatterDoubleCallBack() {
				@Override
				public String doubleFormatter(Double value) {
					DecimalFormat df = new DecimalFormat("#0");
					String label = df.format(value).toString();
					return label;
				}
			});
			// 定义柱形上标签显示颜色
			chart.getBar().getItemLabelPaint().setColor(Color.rgb(225, 43, 44));
			// 柱形形标签字体大小
			chart.getBar().getItemLabelPaint().setTextSize(18);

			// 激活点击监听
			chart.ActiveListenItemClick();
			chart.showClikedFocus();
			chart.setPlotPanMode(XEnum.PanMode.VERTICAL);

			// 设置柱子的最大高度范围，不然有些数据太少时，柱子太高不太好看。
			chart.getBar().setBarMaxPxHeight(100.f);

		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	private void chartDataSet() {
		// 标签对应的柱形数据集
		taint_Infos = PowerService.get_Taint_List(context);
		TaintInfo mid_taint = MainActivity.taintInfo;
		ArrayList<String> titleArrayList = PowerService.get_title_List(context);
		int number;
		Cursor cursor;
		for (int taint_index = 0; taint_index < taint_Infos.size(); taint_index++) {
			List<Double> dataSeries = new LinkedList<Double>();
			for (int app_index = 0; app_index < app_Infos.size(); app_index++) {
				number = 0;
				cursor = mid_taint.select_package_name_Taint(
						App_infos.getPackageName_app(context,
								app_Infos.get(app_index).getName()),
						taint_Infos.get(taint_index));
				try {
					while (cursor.moveToNext()) {
						number++;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					if (cursor != null) {
						cursor.close();
					}
				}	
				dataSeries.add((double) number);
			}
			BarDataSet.add(new BarData(titleArrayList.get(taint_index),
					dataSeries,colors[taint_index]));
		}
	}

	private void chartLabels() {
		for (int i = 0; i < app_Infos.size(); i++) {
			chartLabels.add(app_Infos.get(i).getName());
		}
	}

	@Override
	public void render(Canvas canvas) {
		try {
			chart.render(canvas);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	@Override
	public List<XChart> bindChart() {
		List<XChart> lst = new ArrayList<XChart>();
		lst.add(chart);
		return lst;
	}

	@SuppressLint("ClickableViewAccessibility") @Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		if (event.getAction() == MotionEvent.ACTION_UP) {
			triggerClick(event.getX(), event.getY());
		}
		return true;
	}

	// 触发监听
	private void triggerClick(float x, float y) {

		BarPosition record = chart.getPositionRecord(x, y);
		if (null == record)
			return;

		if (record.getDataID() >= BarDataSet.size())
			return;
		BarData bData = BarDataSet.get(record.getDataID());
		Double bValue = bData.getDataSet().get(record.getDataChildID());

		chart.showFocusRectF(record.getRectF());
		chart.getFocusPaint().setStyle(Style.FILL);
		chart.getFocusPaint().setStrokeWidth(3);
		chart.getFocusPaint().setColor(Color.GRAY);
		chart.getFocusPaint().setAlpha(100);

		// 在点击处显示tooltip
		pToolTip.setColor(Color.WHITE);
		chart.getToolTip().setAlign(Align.CENTER);
		chart.getToolTip().getBackgroundPaint().setColor(Color.BLUE);
		chart.getToolTip().setInfoStyle(XEnum.DyInfoStyle.CAPROUNDRECT);
		// chart.getToolTip().setCurrentXY(x,y);
		chart.getToolTip().setCurrentXY(record.getRectF().centerX(),
				record.getRectF().top);
		chart.getToolTip().addToolTip(
				" Current Value:" + Double.toString(bValue), pToolTip);

		this.invalidate();
	}

}
