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
 * @Description Android鍥捐〃鍩虹被搴�
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */

package org.xclcharts.renderer;

import java.util.ArrayList;
import java.util.Iterator;

import org.xclcharts.common.DrawHelper;
import org.xclcharts.common.MathHelper;
import org.xclcharts.event.click.ArcPosition;
import org.xclcharts.event.click.BarPosition;
import org.xclcharts.event.click.PlotArcPosition;
import org.xclcharts.event.click.PlotBarPosition;
import org.xclcharts.event.click.PlotPointPosition;
import org.xclcharts.event.click.PointPosition;
import org.xclcharts.event.click.PositionRecord;
import org.xclcharts.renderer.info.ToolTip;
import org.xclcharts.renderer.info.ToolTipRender;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

/**
 * @ClassName EventChart
 * @Description 澶勭悊绫讳技Clicked鐨勪簨浠跺浘鍩虹被
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */

public class EventChart extends XChart {
	
	private static final String TAG="EventChart";
	
	private boolean mListenClick = false;
	private int mClickRangeExtValue = 0;	
	private ArrayList mRecordset = null;	
	private int mSelectID = -1;
	private int mSelectDataID = -1;
	private int mSelectDataChildID = -1;
	
	private boolean mShowClikedFocus = false;
	
	private Paint mFocusPaint = null;
	private PointF mFocusPoint = null;
	private float mFocusRadius = 0.0f;	
	private RectF mFocusRect = null;	
	//private Path mFocusPath = null;
	private ArcPosition mFocusArcPosition = null;
	private boolean mFocusArcSelect = false;
	
	private ToolTipRender mToolTip = null;
	
	//private ArrayList<? extends PositionRecord> mRecordset = null;	
		
	public EventChart() {
		// TODO Auto-generated constructor stub		
			
		initPositionRecord();
	}
		
	/**
	 * 婵�娲荤偣鍑讳簨浠�
	 */
	public void ActiveListenItemClick()
	{
		mListenClick = true;	
	}

	/**
	 * 绂佺敤鐐瑰嚮浜嬩欢
	 */
	public void DeactiveListenItemClick()
	{
		mListenClick = false;
	}
	
	/**
	 * 杩斿洖浜嬩欢澶勭悊鐘舵��
	 * @return 鏄惁婵�娲�
	 */
	public boolean getListenItemClickStatus()
	{
		return mListenClick;
	}
	
	/**
	 * 杩斿洖璁板綍闆�
	 * @return 璁板綍闆�
	 */
	public ArrayList<PositionRecord> getPositionRecordset()
	{
		return  mRecordset;
	}
	
	/**
	 * 鏄偣瀵圭偣鍑婚�変腑瀵硅薄鏄剧ず鐩稿叧鐒︾偣鏍囪瘑
	 */
	public void showClikedFocus()
	{
		mShowClikedFocus = true;
	}
	
	
	private void clearSelected()
	{
		mSelectID = -1;
		mSelectDataID = -1;
		mSelectDataChildID = -1;
	}
	
	private void saveSelected(int recordID,int dataID,int dataChildID)
	{
		mSelectID = recordID;
		mSelectDataID = dataID;
		mSelectDataChildID = dataChildID;
	}
	
	protected int getSelected()
	{
		return mSelectID;
	}
	
	protected void savePointRecord(final int dataID,final int childID,
									final float x,final float y,final RectF r)
	{
		if(!getListenItemClickStatus())return;
		savePointRecord(dataID,childID,x,y,r.left,r.top,r.right,r.bottom);		
	}
	
	protected void savePointRecord(final int dataID,final int childID,
								  float x,float y,
								  float left,float top, float right, float bottom)
	{
		if(!getListenItemClickStatus())return;
		if(null == mRecordset)mRecordset =  new ArrayList<PlotPointPosition>();
	
		PlotPointPosition pRecord = new PlotPointPosition();			
		pRecord.savePlotDataID(dataID);
		pRecord.savePlotDataChildID(childID);			
		pRecord.savePlotPosition(x, y);							
		pRecord.savePlotRectF(left,top,right,bottom);			
		pRecord.extPointClickRange(mClickRangeExtValue);			
		mRecordset.add(pRecord);
	}
	
	
	protected void saveBarRectFRecord(int dataID,int childID,
			   float left,float top,float right,float bottom)
	{
		if(!getListenItemClickStatus())return;
		if(null == mRecordset)mRecordset =  new ArrayList<PlotBarPosition>();	
				
		PlotBarPosition pRecord = new PlotBarPosition();			
		pRecord.savePlotDataID(dataID);
		pRecord.savePlotDataChildID(childID);
		pRecord.savePlotRectF(left, top, right, bottom);			
		pRecord.extPointClickRange(mClickRangeExtValue);
		mRecordset.add(pRecord);		
	}
			
	protected void saveBarRecord(int dataID,int childID,float x,float y,RectF r)
	{
		if(!getListenItemClickStatus())return;
		if(null == mRecordset)mRecordset =  new ArrayList<PlotBarPosition>();	
	
		PlotBarPosition pRecord = new PlotBarPosition();			
		pRecord.savePlotDataID(dataID);
		pRecord.savePlotDataChildID(childID);						
		pRecord.savePlotRectF(r);			
		pRecord.extPointClickRange(mClickRangeExtValue);
		mRecordset.add(pRecord);		
	}
	
	//淇濆瓨瑙掑害 (鍗婂緞)
	protected void saveArcRecord(int dataID,float centerX,float centerY,
								 float radius,float offsetAngle,float Angle,
								 float selectedOffset,float initialAngle)
	{
		if(!getListenItemClickStatus())return;
		if(null == mRecordset)mRecordset =  new ArrayList<PlotArcPosition>();	
	
		PlotArcPosition pRecord = new PlotArcPosition();			
		pRecord.savePlotDataID(dataID);		
		pRecord.savePlotCirXY(centerX,centerY);
		pRecord.saveAngle(radius,offsetAngle,Angle,selectedOffset);			
		pRecord.saveInitialAngle(initialAngle);
		mRecordset.add(pRecord);		
	}


	/**
	 * 涓轰簡璁╄Е鍙戞洿鐏垫晱锛屽彲浠ユ墿澶ф寚瀹歱x鐨勭偣鍑荤洃鍚寖鍥�
	 * @param value 鎵╁ぇ澶氬皯鐐瑰嚮鐩戝惉鑼冨洿
	 */
	public void extPointClickRange(int value)
	{	
		mClickRangeExtValue = value;
	}			
	

	/**
	 * 妫�鏌ユ槸鍚︾偣鍑诲湪澶勭悊鑼冨洿鍐�
	 * @param x	褰撳墠鐐瑰嚮鐐筙鍧愭爣
	 * @param y 褰撳墠鐐瑰嚮鐐筜鍧愭爣
	 * @return 鏄惁闇�澶勭悊
	 */
	public boolean isPlotClickArea(float x,float y)
	{						
		if(!getListenItemClickStatus())return false;	
		
		if(Float.compare(x , getPlotArea().getLeft()) == -1 ) return false;
		if(Float.compare(x, getPlotArea().getRight() ) == 1 ) return false;	
		
		if(Float.compare( y , getPlotArea().getTop() ) == -1  ) return false;
		if(Float.compare( y , getPlotArea().getBottom() ) == 1  ) return false;			
				
		return true;
	}
	
	/**
	 * 杩斿洖瀵瑰簲鐨勮褰�
	 * @param x 褰撳墠鐐瑰嚮鐐筙鍧愭爣
	 * @param y 褰撳墠鐐瑰嚮鐐筜鍧愭爣
	 * @return 璁板綍绫�
	 */
	protected ArcPosition getArcRecord(float x,float y)
	{			
		if(!getListenItemClickStatus()) return null;		
		if(!isPlotClickArea(x,y))return null;
		if(!getClikedScaleStatus())return null;
		if(null == mRecordset) return null;
							
		Iterator it = mRecordset.iterator();
		while(it.hasNext())
		{		
			PlotArcPosition record = (PlotArcPosition)it.next();
			if(record.compareF(x, y))
			{
				saveSelected(record.getRecordID(),record.getDataID(),record.getDataChildID());
				return record;
			}
		}		
		clearSelected();
		return null;
	}
	
	protected BarPosition getBarRecord(float x,float y)
	{		
		if(!getListenItemClickStatus()) return null;
		if(!isPlotClickArea(x,y))return null;	
		if(!getClikedScaleStatus())return null;
		if(null == mRecordset) return null;
			
		Iterator it = mRecordset.iterator();
		while(it.hasNext())
		{		
			PlotBarPosition record = (PlotBarPosition)it.next();
			if(record.compareF(x, y))
			{
				saveSelected(record.getRecordID(),record.getDataID(),record.getDataChildID());
				return record;			
			}
		}	
		clearSelected();
		return null;
	}
	
	protected PointPosition getPointRecord(final float x,final float y)
	{					
		if(!getListenItemClickStatus()) return null;
		if(!isPlotClickArea(x,y))return null;	
		if(!getClikedScaleStatus())return null;		
		if(null == mRecordset) return null;
						
		Iterator it = mRecordset.iterator();
		while(it.hasNext())
		{		
			PlotPointPosition record = (PlotPointPosition)it.next();
			if(record.compareF(x,y))
			{
				saveSelected(record.getRecordID(),record.getDataID(),record.getDataChildID()); 
				return record;			
			}
		}			
		clearSelected();
		return null;
	}
	
	protected void initPositionRecord()
	{
		if(null != mRecordset)
		{
			mRecordset.clear();
			mRecordset = null;
		}
	}
	
	/**
	 * 寮�鏀剧劍鐐圭敾绗�	
	 * @return 鐢荤瑪
	 */
	public Paint getFocusPaint()
	{
		if(null == mFocusPaint)mFocusPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		return mFocusPaint;
	}
	
	/**
	 * 鐐圭殑鐒︾偣鍙傛暟
	 * @param point 鐐�
	 * @param radius 鍗婂緞
	 */
	public void showFocusPointF(PointF point,float radius)
	{
		mFocusPoint = point;
		mFocusRadius = radius;
	}
	
	/**
	 * 鏌卞舰绫荤殑鐒︾偣鍙傛暟
	 * @param rect 鏌卞舰
	 */
	public void showFocusRectF(RectF rect)
	{
		mFocusRect = rect;
	}
	
	/**
	 * 鍥撅紝鎵囧舰绫荤殑鐒︾偣鍙傛暟
	 * @param arc 鎵囧舰閫変腑绫�
	 */
	public void showFocusArc(ArcPosition arc)
	{
		showFocusArc(arc,false);
	}
	
	public void showFocusArc(ArcPosition arc,boolean selected)
	{
		mFocusArcPosition = arc;
		mFocusArcSelect = selected;
	}
	
	/**
	 * 寮�鏀炬彁绀轰俊鎭被
	 * @return 淇℃伅鍩虹被
	 */
	public ToolTip getToolTip()
	{
		if(null == mToolTip) mToolTip = new ToolTipRender();
		return mToolTip;
	}
	
	/**
	 * 缁樺埗鎻愮ず淇℃伅
	 * @param canvas 鐢诲竷
	 */
	protected void renderToolTip(Canvas canvas)
	{
		if(null == mToolTip)return;
		mToolTip.renderInfo(canvas);
	}	
	
	protected boolean drawFocusRect(Canvas canvas,int dataID,int childID,
			float left,float top,float right ,float bottom)
	{
		if(!mShowClikedFocus) return true;
		if(-1 == mSelectID) return false;
		if(null == mFocusRect) return false;
		
		//if(null == mFocusPath) mFocusPath = new Path();

		if( mSelectDataID == dataID &&
				mSelectDataChildID == childID	)
		{			
			
			mFocusRect.left = left;
			mFocusRect.top = top;
			mFocusRect.right = right;
			mFocusRect.bottom = bottom;											
			canvas.drawRect(mFocusRect, getFocusPaint());
			mFocusRect.setEmpty();	
			
			/*
			mFocusPath.moveTo(left, bottom);
			mFocusPath.lineTo(left, top);
			mFocusPath.lineTo(right, top);
			mFocusPath.lineTo(right, bottom);
			mFocusPath.close();
			canvas.drawPath(mFocusPath, getFocusPaint());
			mFocusPath.reset();
			*/
			
			clearSelected();
		}		
		
		return true;
	}
	
	/**
	 * 缁樺埗鐒︾偣褰㈢姸
	 * @param canvas 鐢诲竷
	 * @return 鏄惁缁樺埗鎴愬姛
	 */
	protected boolean renderFocusShape(Canvas canvas)
	{				
		if(!mShowClikedFocus) return true;
		XEnum.ChartType ctype = this.getType();		
		if( XEnum.ChartType.BAR == ctype ||  XEnum.ChartType.BAR3D == ctype
			|| XEnum.ChartType.STACKBAR == ctype )
		{
			return true;
		}
		try{
				if(null != mFocusPoint)
				{					
					canvas.drawCircle(mFocusPoint.x, mFocusPoint.y, 
									  mFocusRadius, getFocusPaint());	
					mFocusPoint = null;
					mFocusRadius = 0.0f;
				}else if(null != mFocusRect){	
					/*
					canvas.save();
					canvas.clipRect(plotArea.getLeft(),plotArea.getTop(),
							plotArea.getRight(),plotArea.getBottom());
						canvas.drawRect(mFocusRect, getFocusPaint());
					canvas.restore();
					mFocusRect = null;
					*/
				}else if(null != mFocusArcPosition){												
						PointF pointCir = mFocusArcPosition.getPointF();
						float cirX = pointCir.x,cirY = pointCir.y;
						float radius = mFocusArcPosition.getRadius();
						if(mFocusArcSelect)
						{													
							//鍋忕Щ鍦嗗績鐐逛綅缃�(榛樿鍋忕Щ鍗婂緞鐨�1/10)
					    	float newRadius = div( radius , mFocusArcPosition.getSelectedOffset());												    	
					    	 //璁＄畻鐧惧垎姣旀爣绛�
					    	PointF point = MathHelper.getInstance().calcArcEndPointXY(cirX,cirY,
					    							newRadius,
					    							add(mFocusArcPosition.getStartAngle() , 
					    									mFocusArcPosition.getSweepAngle()/2f)); 					     
					    	cirX = point.x;
					    	cirY = point.y;
						}
						DrawHelper.getInstance().drawPercent(canvas, getFocusPaint(), 
								cirX,cirY, radius,
								mFocusArcPosition.getStartAngle(), mFocusArcPosition.getSweepAngle(), true);							
						mFocusArcPosition = null;
				}else{					
					return false;
				}
		}catch(Exception ex)
		{
			Log.e(TAG,ex.toString());
		}
		return true;
	}
		
	@Override
	protected boolean postRender(Canvas canvas) throws Exception 
	{
		// 缁樺埗鍥捐〃
		try {
			super.postRender(canvas);
								
			//娓呯悊
			initPositionRecord();
			
		} catch (Exception e) {
			throw e;
		}
		return true;
	}

}
