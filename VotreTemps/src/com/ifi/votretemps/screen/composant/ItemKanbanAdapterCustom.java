package com.ifi.votretemps.screen.composant;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ifi.votretemps.model.bo.BOTask;
import com.ifi.votretemps.screen.R;

public class ItemKanbanAdapterCustom extends BaseAdapter {
	private Context mContext;
	private List<BOTask> mTasks;

	public ItemKanbanAdapterCustom(Context mContext, List<BOTask> mTasks) {
		super();
		this.mContext = mContext;
		this.mTasks = mTasks;
	}

	@Override
	public int getCount() {
		return mTasks.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// inflate the layout for each item of listView
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.item_kanban, null);

		// get the reference of textViews
		TextView textTaskKanban = (TextView) view
				.findViewById(R.id.txtTaskKanban);

		// Set the data
		textTaskKanban.setText(mTasks.get(position).getTaskName());

		return view;
	}

}
