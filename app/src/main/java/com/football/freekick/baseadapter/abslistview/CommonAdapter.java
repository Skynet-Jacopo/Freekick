package com.football.freekick.baseadapter.abslistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.football.freekick.baseadapter.ViewHolder;

import java.util.List;


public abstract class CommonAdapter<T> extends BaseAdapter
{
	protected Context        mContext;
	protected List<T>        mDatas;
	protected LayoutInflater mInflater;
	private   int            layoutId;

	public CommonAdapter(Context context, int layoutId, List<T> datas)
	{
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mDatas = datas;
		this.layoutId = layoutId;
	}

	@Override
	public int getCount()
	{
		return mDatas.size();
	}

	@Override
	public T getItem(int position)
	{
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	protected void onConvertViewCreated(View convertView)
	{

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		if (convertView == null)
		{
			View itemView = LayoutInflater.from(mContext).inflate(layoutId, parent,
					false);
			onConvertViewCreated(itemView);
			holder = new ViewHolder(mContext, itemView, parent, position);
			holder.setLayoutId(layoutId);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
			holder.updatePosition(position);
		}
		convert(holder, getItem(position));
		return holder.getConvertView();
	}

	public abstract void convert(ViewHolder holder, T t);

}
