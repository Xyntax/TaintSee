package com.tiantfolw.listview_adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.taintsee.R;
import com.taintflow.infos.Infos;

public class ListViewAdapter extends BaseAdapter {
	private Context context;
	private List<Infos> data;
	Boolean showed = Boolean.valueOf(false);

	public ListViewAdapter(Context paramContext, List<Infos> paramList) {
		this.context = paramContext;
		this.data = paramList;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.everyline_list, null);
			holder = new ViewHolder();
			holder.tv_number = (TextView) view.findViewById(R.id.tv_number);
			holder.ib_no = (ImageButton) view.findViewById(R.id.ib_app_no);
			holder.ib_ok = (ImageButton) view.findViewById(R.id.ib_app_ok);
			holder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
			holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.ib_no.setTag(position);
		holder.ib_ok.setTag(position);
		
		if (this.data.get(position).getIsEnable()) {
			holder.ib_ok.setVisibility(View.VISIBLE);
			holder.ib_no.setVisibility(View.GONE);
		} else {
			holder.ib_ok.setVisibility(View.GONE);
			holder.ib_no.setVisibility(View.VISIBLE);
		}
		Infos infos = data.get(position);
		if (infos != null) {
			holder.iv_icon.setImageDrawable(infos.getIcon());
			holder.tv_name.setText(infos.getName());
			holder.tv_number.setText(Integer.toString(infos.getNumber()));
		}

		return view;
	}

	class ViewHolder {
		TextView tv_number, tv_name;
		ImageButton ib_ok, ib_no;
		ImageView iv_icon;
	}
}