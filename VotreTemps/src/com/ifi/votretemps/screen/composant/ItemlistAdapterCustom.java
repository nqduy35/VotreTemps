package com.ifi.votretemps.screen.composant;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ifi.votretemps.model.bo.BOTask;
import com.ifi.votretemps.screen.InfoActivity;
import com.ifi.votretemps.screen.R;
import com.ifi.votretemps.utils.Global;

public class ItemlistAdapterCustom extends BaseAdapter {
	private Context mContext;
	private List<BOTask> mTasks;

	public ItemlistAdapterCustom(Context mContext, List<BOTask> mTasks) {
		super();
		this.mContext = mContext;
		this.mTasks = mTasks;
	}

	@Override
	public int getCount() {
		// return the number of task
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
		view = inflater.inflate(R.layout.item_task, null);

		// get the reference of textViews
		TextView textTaskName = (TextView) view.findViewById(R.id.txtTaskName);
		TextView textDataStart = (TextView) view
				.findViewById(R.id.txtDateStart);
		TextView textDataEnd = (TextView) view.findViewById(R.id.txtDateEnd);
		Button buttonEdit = (Button) view.findViewById(R.id.btnEdit);
		Button buttonDelete = (Button) view.findViewById(R.id.btnDelete);

		// Set the data
		textTaskName.setText(mTasks.get(position).getTaskName());
		textDataStart.setText(mTasks.get(position).getStartDate());
		textDataEnd.setText(mTasks.get(position).getEndDate());

		buttonEdit.setTag(position);
		buttonDelete.setTag(position);

		buttonEdit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Activity activity = (Activity) mContext;
				Intent newActivity = new Intent(activity, InfoActivity.class);
				activity.startActivity(newActivity);
			}
		});

		buttonDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Global.database.deleteTask(Global.tasks.get(
						(Integer) v.getTag()).getTaskId());
				Global.tasks = Global.database.getTask(Global.userIDNow);
				Activity activity = (Activity) mContext;
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// mTasks.clear();
						// mTasks = Global.tasks;
						// notifyDataSetChanged();

					}
				});
			}
		});

		return view;
	}

}
