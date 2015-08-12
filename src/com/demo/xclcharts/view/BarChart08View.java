package com.demo.xclcharts.view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.xclcharts.chart.BarChart;
import org.xclcharts.chart.BarData;
import org.xclcharts.chart.CustomLineData;
import org.xclcharts.common.DrawHelper;
import org.xclcharts.common.IFormatterDoubleCallBack;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.renderer.XChart;
import org.xclcharts.renderer.XEnum;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.Log;

import com.demo.taintsee.MainActivity;
import com.taintflow.Service.App_detailService;
import com.taintflow.Service.PowerService;
import com.taintflow.TaintFlowDataBaseHelper.TaintInfo;
import com.taintflow.infos.App_infos;
import com.taintflow.infos.Infos;

public class BarChart08View extends DemoView {

	private static final int ACCESSIBILITY_LIVE_REGION_ASSERTIVE = 0;
	private String TAG = "BarChart08View";
	private BarChart chart = new BarChart();
	// 轴数据源
	private List<String> chartLabels;
	private List<BarData> chartData;
	private List<CustomLineData> mCustomLineDataset;

	Intent intent;
	private String subtitle = "";
	private Context context;
	static String package_name = null;
	static String app_name;
	static ArrayList<Infos> charts;

	public BarChart08View(Context context, Intent intent) {

		super(context);
		this.context = context;
		this.intent = intent;

		init();

	}

	private void init() {

		app_name = intent.getExtras().getString("app_Name");
		package_name = App_infos.getPackageName_app(context, app_name);
		/*charts = App_detailService.get_Chart_Info_List_App(context, app_name);*/
		charts = App_detailService.get_Chart_Info_List_App(context, app_name);

		chartLabels = BarChartService.getLabels(intent, context);
		chartData = BarChartService.getDate(intent, context);
		mCustomLineDataset = BarChartService.getCustomLineDatas(intent);
		subtitle = intent.getExtras().getString("app_Name");

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
			chart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);

			// 显示边框
			chart.showRoundBorder();

			// 标题
			chart.setTitle("隐私泄漏频率统计图");
			chart.addSubtitle(subtitle);
			// 数据源
			chart.setDataSource(chartData);
			chart.setCategories(chartLabels);
			chart.setCustomLines(mCustomLineDataset);
			int max = 0;
			for (int i = 0; i < charts.size(); i++) {
				if (max < charts.get(i).getNumber()) {
					max = charts.get(i).getNumber();
				}
			}
			max = (int)(max*1.2);
			int step = max/10 +1;
					

			// 数据轴
			chart.getDataAxis().setAxisMax(step * 10);
			chart.getDataAxis().setAxisMin(0);
			chart.getDataAxis().setAxisSteps(step);
			// 指隔多少个轴刻度(即细刻度)后为主刻度
			chart.getDataAxis().setDetailModeSteps(2);

			chart.getDataAxis().enabledAxisStd();
			chart.getDataAxis().setAxisStd(0);
			chart.getCategoryAxis().setAxisBuildStd(true);

			// 背景网格
			chart.getPlotGrid().showHorizontalLines();

			// 定义数据轴标签显示格式
			chart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack() {

				@Override
				public String textFormatter(String value) {
					Double tmp = Double.parseDouble(value);
					DecimalFormat df = new DecimalFormat("#0");
					String label = df.format(tmp).toString();
					return (label);
				}

			});

			// 标签旋转45度
			 chart.getCategoryAxis().setTickLabelRotateAngle(45f);
			chart.getCategoryAxis().getTickLabelPaint().setTextSize(15);
			
			chart.getCategoryAxis().setTickLabelMargin(ACCESSIBILITY_LIVE_REGION_ASSERTIVE);

			// 在柱形顶部显示值
			chart.getBar().setItemLabelVisible(true);
			// 设定格式
			chart.setItemLabelFormatter(new IFormatterDoubleCallBack() {
				@Override
				public String doubleFormatter(Double value) {
					DecimalFormat df = new DecimalFormat("#0");
					String label = df.format(value).toString();
					return label;
				}
			});

			// 隐藏Key
			chart.getPlotLegend().hide();

			// 让柱子间没空白
			chart.getBar().setBarInnerMargin(0.5f); // 可尝试0.1或0.5各有啥效果噢

			// 禁用平移模式
			// chart.disablePanMode();

			chart.disableHighPrecision();

			// 限制只能左右滑动
			chart.setPlotPanMode(XEnum.PanMode.HORIZONTAL);

			chart.setBarCenterStyle(XEnum.BarCenterStyle.TICKMARKS);

			// chart.showRoundBorder();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

	@Override
	public void render(Canvas canvas) {
		try {
			chart.render(canvas);

			paint.setTextSize(22);
			paint.setColor(Color.RED);

			float textHeight = DrawHelper.getInstance().getPaintFontHeight(
					paint);
			paint.setTextAlign(Align.LEFT);
			canvas.drawText("频数", chart.getPlotArea().getLeft(), chart
					.getPlotArea().getTop() - textHeight, paint);

			paint.setTextAlign(Align.RIGHT);
			canvas.drawText("隐私数据类型", chart.getPlotArea().getRight(), chart
					.getPlotArea().getBottom() + textHeight, paint);

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

	private static class BarChartService {

		public static List<String> getLabels(Intent intent, Context context) {

			LinkedList<String> chartLabels = new LinkedList<String>();
			for (int i = 0; i < charts.size(); i++) {
				chartLabels.add(charts.get(i).getName());
			}
			return (LinkedList<String>) chartLabels;
		}

		public static List<BarData> getDate(Intent intent, Context context) {

			LinkedList<BarData> chartData = new LinkedList<BarData>();
			// 标签对应的柱形数据集
			List<Double> dataSeriesA = new LinkedList<Double>();
			// 依数据值确定对应的柱形颜色.
			List<Integer> dataColorA = new LinkedList<Integer>();

			Cursor cursor;
			int v;

			for (int i = 0; i < charts.size(); i++) {
				v = 0;
				cursor = MainActivity.taintInfo.select_package_name_Taint(
						package_name, PowerService.taint[PowerService
								.get_index_title(charts.get(i).getName())]);
				try {
					while (cursor.moveToNext()) {
						v++;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					if (cursor != null) {
						cursor.close();
					}
				}
				dataSeriesA.add((double) v);
				if (v <= 25d) // 适中
				{
					dataColorA.add(Color.rgb(77, 184, 73));
				} else if (v <= 50d) { // 超重
					dataColorA.add(Color.rgb(252, 210, 9));
				} else if (v <= 100d) { // 偏胖
					dataColorA.add(Color.rgb(171, 42, 96));
				} else { // 肥胖
					dataColorA.add(Color.RED);
				}
			}
			// 此地的颜色为Key值颜色及柱形的默认颜色
			BarData BarDataA = new BarData("", dataSeriesA, dataColorA,
					Color.rgb(53, 169, 239));

			chartData.add(BarDataA);
			return chartData;

		}

		public static List<CustomLineData> getCustomLineDatas(Intent intent) {
			
			LinkedList<CustomLineData> mCustomLineDataset = new LinkedList<CustomLineData>();
			
			return mCustomLineDataset;

		}
	}

}
