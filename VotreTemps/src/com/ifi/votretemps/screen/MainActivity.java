package com.ifi.votretemps.screen;

import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ifi.votretemps.dal.DataGateway;
import com.ifi.votretemps.model.bo.BOTask;
import com.ifi.votretemps.model.bo.BOUser;
import com.ifi.votretemps.utils.Global;
import com.ifi.votretemps.utils.UtilityMethod;

public class MainActivity extends Activity {

	private LinearLayout buttonHome;
	private LinearLayout buttonAdd;
	private RelativeLayout buttonGantt;
	private RelativeLayout buttonKanban;
	private RelativeLayout buttonEdit;
	private RelativeLayout buttonSetting;

	private LinearLayout linearBody;
	private RelativeLayout relativeHeader;

	// Annonce
	private LinearLayout linearAnnonceKanban;
	private TextView textAnnonceKanban;
	private Button buttonTodo;
	private Button buttonDoing;
	private Button buttonDone;
	private LinearLayout linearAnnonceDelete;
	private TextView textAnnonceDelete;
	private Button buttonYesDelete;
	private Button buttonNonDelete;
	private LinearLayout linearAnnonceEdit;
	private EditText editTaskEdit;
	private EditText editDescriptionEdit;
	private DatePicker datepickerStartEdit;
	private DatePicker datepickerEndEdit;
	private Button buttonConfirmEdit;
	private TextView textAnnonceTaskNew;
	private TextView textAnnonceTaskDescriptionNew;
	private TextView textAnnonceTaskStartNew;
	private TextView textAnnonceTaskEndNew;
	private LinearLayout linearError;
	private LinearLayout linearErrorAdd;
	private TextView textError;
	private TextView textErrorAdd;

	private List<BOTask> profils;

	// Setting
	private Button buttonAbout;
	private Button buttonLogout;
	private ToggleButton buttonAlarme;
	private TextView textUserName;
	private Spinner spinnerLimit;
	private Spinner spinnerLanguague;
	private ArrayAdapter<String> adapterSpinnerLimit;
	private ArrayAdapter<String> adapterSpinnerLanguage;
	private TextView textSettingsLimit;
	private TextView textSettingsAlarm;
	private TextView textSettingsLanguage;
	private TextView textSettingsAccount;

	// Welcome
	private TextView textHomeInfo;
	private TextView textTitle;

	// Edit
	ListView listTask;
	ItemlistAdapterCustom adapterListTask;

	// Gantt
	ListView listTaskGantt;
	ItemlistAdapterCustomGantt adapterListTaskGantt;

	// Add
	EditText editTaskName;
	EditText editTaskInfo;
	DatePicker datepickerStart;
	DatePicker datepickerEnd;
	Button buttonOkAdd;
	TextView textTaskNew;
	TextView textTaskDescriptionNew;
	TextView textTaskStartNew;
	TextView textTaskEndNew;

	// Kanban
	ListView listTodo;
	ListView listDoing;
	ListView listDone;
	ItemKanbanAdapterCustom adapterTodo;
	ItemKanbanAdapterCustom adapterDoing;
	ItemKanbanAdapterCustom adapterDone;
	List<BOTask> todos;
	List<BOTask> doings;
	List<BOTask> dones;

	private LayoutInflater inflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_main);
		Global.setupGlobal();
		initDB();

		SharedPreferences getting = getSharedPreferences("save", MODE_PRIVATE);
		Global.userIDNow = getting.getInt("saveUserid", 99999);
		SharedPreferences gettingDetail = getSharedPreferences(Global.userIDNow
				+ "", MODE_PRIVATE);
		if (Global.userIDNow == 0) {
			Global.language = 0;
			Global.alarm = 0;
			Global.limit = 0;
		} else {
			Global.language = gettingDetail.getInt("saveLanguage", 0);
			Global.alarm = gettingDetail.getInt("saveAlarm", 0);
			Global.limit = gettingDetail.getInt("saveLimit", 0);
		}

		if (Global.userIDNow == 99999) {
			// First time login
			Intent newIntent = new Intent(MainActivity.this,
					LoginActivity.class);
			MainActivity.this.startActivity(newIntent);
			Global.tasks = Global.database.getTask(Global.userIDNow);
			init();
			Global.users = Global.database.getAllUsers();
			if (Global.users.size() < 2) {
				Global.database.addUser(new BOUser(0, "demo", ""));
				Global.database.addUser(new BOUser(1, "nqduy", "123"));
				Global.database.addUser(new BOUser(2, "htvinh", "ifi"));
				Global.database.addUser(new BOUser(3, "nvsang", "ifi"));
			}
			finish();
		} else {
			Global.tasks = Global.database.getTask(Global.userIDNow);
			// If not demo account playSound
			if (Global.userIDNow != 0 && Global.alarm == 1
					&& UtilityMethod.checkAlarme() == true) {
				playSound();
			}
			init();
		}

		buttonHome.setOnClickListener(onClickListener);
		buttonAdd.setOnClickListener(onClickListener);
		buttonGantt.setOnClickListener(onClickListener);
		buttonKanban.setOnClickListener(onClickListener);
		buttonEdit.setOnClickListener(onClickListener);
		buttonSetting.setOnClickListener(onClickListener);

	}

	private void playSound() {
		try {
			Uri notification = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
					notification);
			r.play();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	OnItemSelectedListener onItemSelectedListener = new SpinnerCustom() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			linearError.setVisibility(View.INVISIBLE);
			int tempsLimit = Global.limit;
			Global.language = spinnerLanguague.getSelectedItemPosition();
			Global.limit = spinnerLimit.getSelectedItemPosition();
			if ((Global.limit + 2) < UtilityMethod.separateTaskKanban(2).size()) {
				if (Global.language == 0) {
					textError.setText(getResources().getString(
							R.string.en_Error));
				} else {
					textError.setText(getResources().getString(
							R.string.fr_Error));
				}
				linearError.setVisibility(View.VISIBLE);
				Global.limit = tempsLimit;
			}

			if (Global.language == 0) {
				textTitle.setText(getResources()
						.getString(R.string.en_Settings));
				textSettingsAccount.setText(getResources().getString(
						R.string.en_SettingsAccount));
				textSettingsAlarm.setText(getResources().getString(
						R.string.en_SettingsAlarm));
				textSettingsLimit.setText(getResources().getString(
						R.string.en_SettingsLimit));
				textSettingsLanguage.setText(getResources().getString(
						R.string.en_SettingsLanguage));
				buttonAbout.setText(getResources().getString(
						R.string.en_SettingsAbout));
			} else {
				textTitle.setText(getResources()
						.getString(R.string.fr_Settings));
				textSettingsAccount.setText(getResources().getString(
						R.string.fr_SettingsAccount));
				textSettingsAlarm.setText(getResources().getString(
						R.string.fr_SettingsAlarm));
				textSettingsLimit.setText(getResources().getString(
						R.string.fr_SettingsLimit));
				textSettingsLanguage.setText(getResources().getString(
						R.string.fr_SettingsLanguage));
				buttonAbout.setText(getResources().getString(
						R.string.fr_SettingsAbout));
			}
			SharedPreferences preferences = getSharedPreferences(
					Global.userIDNow + "", MODE_PRIVATE);
			SharedPreferences.Editor edit = preferences.edit();
			edit.putInt("saveLimit", Global.limit);
			edit.putInt("saveLanguage", Global.language);
			edit.commit();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	};

	OnTouchListener onTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v == editTaskName) {
				linearErrorAdd.setVisibility(View.INVISIBLE);
			} else if (v == editTaskInfo) {
				linearErrorAdd.setVisibility(View.INVISIBLE);
			} else if (v == buttonOkAdd) {
				linearErrorAdd.setVisibility(View.INVISIBLE);
			}
			return false;
		}
	};

	OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View v) {
			if (v == buttonHome) {
				linearAnnonceDelete.setVisibility(View.INVISIBLE);
				linearAnnonceKanban.setVisibility(View.INVISIBLE);
				linearAnnonceEdit.setVisibility(View.INVISIBLE);
				linearError.setVisibility(View.INVISIBLE);
				linearErrorAdd.setVisibility(View.INVISIBLE);
				changeToHome();
			} else if (v == buttonAdd) {
				linearAnnonceDelete.setVisibility(View.INVISIBLE);
				linearAnnonceKanban.setVisibility(View.INVISIBLE);
				linearAnnonceEdit.setVisibility(View.INVISIBLE);
				linearError.setVisibility(View.INVISIBLE);
				linearErrorAdd.setVisibility(View.INVISIBLE);
				changeToAdd();
			} else if (v == buttonGantt) {
				linearAnnonceDelete.setVisibility(View.INVISIBLE);
				linearAnnonceKanban.setVisibility(View.INVISIBLE);
				linearAnnonceEdit.setVisibility(View.INVISIBLE);
				linearError.setVisibility(View.INVISIBLE);
				linearErrorAdd.setVisibility(View.INVISIBLE);
				changeToGantt();
			} else if (v == buttonKanban) {
				linearAnnonceDelete.setVisibility(View.INVISIBLE);
				linearAnnonceKanban.setVisibility(View.INVISIBLE);
				linearAnnonceEdit.setVisibility(View.INVISIBLE);
				linearError.setVisibility(View.INVISIBLE);
				linearErrorAdd.setVisibility(View.INVISIBLE);
				changeToKanban();
			} else if (v == buttonEdit) {
				linearAnnonceDelete.setVisibility(View.INVISIBLE);
				linearAnnonceKanban.setVisibility(View.INVISIBLE);
				linearAnnonceEdit.setVisibility(View.INVISIBLE);
				linearError.setVisibility(View.INVISIBLE);
				linearErrorAdd.setVisibility(View.INVISIBLE);
				changeToEdit();
			} else if (v == buttonSetting) {
				linearAnnonceDelete.setVisibility(View.INVISIBLE);
				linearAnnonceKanban.setVisibility(View.INVISIBLE);
				linearAnnonceEdit.setVisibility(View.INVISIBLE);
				linearError.setVisibility(View.INVISIBLE);
				linearErrorAdd.setVisibility(View.INVISIBLE);
				changeToSetting();
			} else if (v == buttonAbout) {
				Intent newIntent = new Intent(MainActivity.this,
						InfoActivity.class);
				MainActivity.this.startActivity(newIntent);
				linearError.setVisibility(View.INVISIBLE);
				linearErrorAdd.setVisibility(View.INVISIBLE);
			} else if (v == buttonLogout) {
				Intent newIntent = new Intent(MainActivity.this,
						LoginActivity.class);
				MainActivity.this.startActivity(newIntent);
				linearError.setVisibility(View.INVISIBLE);
				linearErrorAdd.setVisibility(View.INVISIBLE);
				finish();
			} else if (v == buttonOkAdd) {
				linearErrorAdd.setVisibility(View.INVISIBLE);

				// If bad input, can't push button
				Calendar dateStart = Calendar.getInstance();
				dateStart.set(datepickerStart.getYear(),
						datepickerStart.getMonth() + 1,
						datepickerStart.getDayOfMonth());
				Calendar dateEnd = Calendar.getInstance();
				dateEnd.set(datepickerEnd.getYear(),
						datepickerEnd.getMonth() + 1,
						datepickerEnd.getDayOfMonth());

				if ((editTaskName.getText().toString().compareTo("") != 0)
						&& (UtilityMethod.betweenTime(dateStart, dateEnd) > (1000 * 60))) {
					Global.database.addTask(new BOTask(
							UtilityMethod.randomID(), editTaskName.getText()
									.toString(), editTaskInfo.getText()
									.toString(), datepickerStart.getYear()
									+ "-" + (datepickerStart.getMonth() + 1)
									+ "-" + datepickerStart.getDayOfMonth(),
							datepickerEnd.getYear() + "-"
									+ (datepickerEnd.getMonth() + 1) + "-"
									+ datepickerEnd.getDayOfMonth(), 1, 1,
							Global.userIDNow));
					Global.tasks = Global.database.getTask(Global.userIDNow);
					changeToHome();
				} else {
					linearErrorAdd.setVisibility(View.VISIBLE);
					if (Global.language == 0) {
						textErrorAdd.setText(getResources().getString(
								R.string.en_AddError));
					} else {
						textErrorAdd.setText(getResources().getString(
								R.string.fr_AddError));
					}
				}
			} else if (v == buttonTodo) {
				BOTask tempTask = Global.database.getATask(Global.tempsItem);
				tempTask.setState(1);
				Global.database.updateTask(tempTask);
				Global.tasks = Global.database.getTask(Global.userIDNow);
				adapterTodo.mTasks.clear();
				adapterTodo.mTasks = UtilityMethod.separateTaskKanban(1);
				adapterTodo.notifyDataSetChanged();

				adapterDoing.mTasks.clear();
				adapterDoing.mTasks = UtilityMethod.separateTaskKanban(2);
				adapterDoing.notifyDataSetChanged();

				adapterDone.mTasks.clear();
				adapterDone.mTasks = UtilityMethod.separateTaskKanban(3);
				adapterDone.notifyDataSetChanged();

				linearAnnonceKanban.setVisibility(View.INVISIBLE);
			} else if (v == buttonDoing) {
				BOTask tempTask = Global.database.getATask(Global.tempsItem);
				tempTask.setState(2);
				Global.database.updateTask(tempTask);
				Global.tasks = Global.database.getTask(Global.userIDNow);
				adapterTodo.mTasks.clear();
				adapterTodo.mTasks = UtilityMethod.separateTaskKanban(1);
				adapterTodo.notifyDataSetChanged();

				adapterDoing.mTasks.clear();
				adapterDoing.mTasks = UtilityMethod.separateTaskKanban(2);
				adapterDoing.notifyDataSetChanged();

				adapterDone.mTasks.clear();
				adapterDone.mTasks = UtilityMethod.separateTaskKanban(3);
				adapterDone.notifyDataSetChanged();

				linearAnnonceKanban.setVisibility(View.INVISIBLE);
			} else if (v == buttonDone) {
				BOTask tempTask = Global.database.getATask(Global.tempsItem);
				tempTask.setState(3);
				Global.database.updateTask(tempTask);
				Global.tasks = Global.database.getTask(Global.userIDNow);
				adapterTodo.mTasks.clear();
				adapterTodo.mTasks = UtilityMethod.separateTaskKanban(1);
				adapterTodo.notifyDataSetChanged();

				adapterDoing.mTasks.clear();
				adapterDoing.mTasks = UtilityMethod.separateTaskKanban(2);
				adapterDoing.notifyDataSetChanged();

				adapterDone.mTasks.clear();
				adapterDone.mTasks = UtilityMethod.separateTaskKanban(3);
				adapterDone.notifyDataSetChanged();

				linearAnnonceKanban.setVisibility(View.INVISIBLE);
			} else if (v == buttonYesDelete) {
				Global.database.deleteTask(Global.tasks.get(Global.tempsItem)
						.getTaskId());
				Global.tasks = Global.database.getTask(Global.userIDNow);
				adapterListTask.mTasks.clear();
				adapterListTask.mTasks = Global.tasks;
				adapterListTask.notifyDataSetChanged();
				linearAnnonceDelete.setVisibility(View.INVISIBLE);
			} else if (v == buttonNonDelete) {
				linearAnnonceDelete.setVisibility(View.INVISIBLE);
			} else if (v == buttonConfirmEdit) {
				BOTask tempTask = Global.database.getATask(Global.tasks.get(
						Global.tempsItem).getTaskId());
				tempTask.setTaskName(editTaskEdit.getText().toString());
				tempTask.setInfo(editDescriptionEdit.getText().toString());
				tempTask.setStartDate(datepickerStartEdit.getYear() + "-"
						+ (datepickerStartEdit.getMonth() + 1) + "-"
						+ datepickerStartEdit.getDayOfMonth());
				tempTask.setEndDate(datepickerEndEdit.getYear() + "-"
						+ (datepickerEndEdit.getMonth() + 1) + "-"
						+ datepickerEndEdit.getDayOfMonth());
				Global.database.updateTask(tempTask);
				Global.tasks = Global.database.getTask(Global.userIDNow);
				adapterListTask.mTasks.clear();
				adapterListTask.mTasks = Global.tasks;
				adapterListTask.notifyDataSetChanged();
				linearAnnonceEdit.setVisibility(View.INVISIBLE);
			} else if (v == buttonAlarme) {
				if (buttonAlarme.isChecked()) {
					Global.alarm = 1;
				} else {
					Global.alarm = 0;
				}

				SharedPreferences preferences = getSharedPreferences(
						Global.userIDNow + "", MODE_PRIVATE);
				SharedPreferences.Editor edit = preferences.edit();
				edit.putInt("saveAlarm", Global.alarm);
				edit.commit();
			}
		}
	};

	private void initDB() {
		Global.dataGateway = new DataGateway();
		Global.database = Global.dataGateway
				.connectToDatabase(MainActivity.this);
	}

	private void init() {
		textTitle = (TextView) findViewById(R.id.txtTitle);
		buttonHome = (LinearLayout) findViewById(R.id.btnInfo);
		buttonAdd = (LinearLayout) findViewById(R.id.btnAdd);
		buttonGantt = (RelativeLayout) findViewById(R.id.btnGantt);
		buttonKanban = (RelativeLayout) findViewById(R.id.btnKanban);
		buttonEdit = (RelativeLayout) findViewById(R.id.btnEdit);
		buttonSetting = (RelativeLayout) findViewById(R.id.btnSetting);

		// Annonce
		linearAnnonceKanban = (LinearLayout) findViewById(R.id.lnrAnnonceKanban);
		textAnnonceKanban = (TextView) findViewById(R.id.txtAnnonceKanban);
		buttonTodo = (Button) findViewById(R.id.btnTodo);
		buttonTodo.setOnClickListener(onClickListener);
		buttonDoing = (Button) findViewById(R.id.btnDoing);
		buttonDoing.setOnClickListener(onClickListener);
		buttonDone = (Button) findViewById(R.id.btnDone);
		buttonDone.setOnClickListener(onClickListener);

		linearAnnonceDelete = (LinearLayout) findViewById(R.id.lnrAnnonceDelete);
		textAnnonceDelete = (TextView) findViewById(R.id.txtAnnonceDetlete);
		buttonYesDelete = (Button) findViewById(R.id.btnAcceptDelete);
		buttonYesDelete.setOnClickListener(onClickListener);
		buttonNonDelete = (Button) findViewById(R.id.btnRefuseDelete);
		buttonNonDelete.setOnClickListener(onClickListener);

		textAnnonceTaskNew = (TextView) findViewById(R.id.txtAnnonceTaskNew);
		textAnnonceTaskDescriptionNew = (TextView) findViewById(R.id.txtAnnonceTaskDescriptionNew);
		textAnnonceTaskStartNew = (TextView) findViewById(R.id.txtAnnonceTaskStartNew);
		textAnnonceTaskEndNew = (TextView) findViewById(R.id.txtAnnonceTaskEndNew);

		linearAnnonceEdit = (LinearLayout) findViewById(R.id.lnrEdit);
		editTaskEdit = (EditText) findViewById(R.id.edtTaskEdit);
		editDescriptionEdit = (EditText) findViewById(R.id.edtDescriptionEdit);
		datepickerStartEdit = (DatePicker) findViewById(R.id.dpkStartEdit);
		datepickerEndEdit = (DatePicker) findViewById(R.id.dpkEndEdit);
		buttonConfirmEdit = (Button) findViewById(R.id.btnAddEdit);
		buttonConfirmEdit.setOnClickListener(onClickListener);

		linearAnnonceDelete.setVisibility(View.INVISIBLE);
		linearAnnonceKanban.setVisibility(View.INVISIBLE);
		linearAnnonceKanban.setVisibility(View.INVISIBLE);

		linearError = (LinearLayout) findViewById(R.id.lnrCaution);
		textError = (TextView) findViewById(R.id.txtCaution);
		linearError.setVisibility(View.INVISIBLE);

		linearErrorAdd = (LinearLayout) findViewById(R.id.lnrCautionAdd);
		textErrorAdd = (TextView) findViewById(R.id.txtCautionAdd);
		linearErrorAdd.setVisibility(View.INVISIBLE);

		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		linearBody = (LinearLayout) findViewById(R.id.lnr_body);
		relativeHeader = (RelativeLayout) findViewById(R.id.rltHeader);

		// Make welcome main layout work
		LinearLayout firstChildLinear = new LinearLayout(this);
		linearBody.addView(firstChildLinear);
		View view = inflater.inflate(R.layout.sub_welcome, firstChildLinear,
				true);
		TextView textCount = (TextView) view
				.findViewById(R.id.txtNumberOfTasks);
		textHomeInfo = (TextView) view.findViewById(R.id.txtHomeInfo);

		if (Global.language == 0) {
			textTitle.setText(getResources().getString(R.string.en_Home));
			textHomeInfo
					.setText(getResources().getString(R.string.en_HomeInfo));
		} else {
			textTitle.setText(getResources().getString(R.string.fr_Home));
			textHomeInfo
					.setText(getResources().getString(R.string.fr_HomeInfo));
		}

		int total = UtilityMethod.separateTaskKanban(1).size()
				+ UtilityMethod.separateTaskKanban(2).size();
		// textCount.setText(Global.tasks.size() + " ");
		textCount.setText(total + " ");
	}

	@SuppressLint("ResourceAsColor")
	private void changeToHome() {
		// Main layout
		linearBody.removeAllViews();
		LinearLayout childLinear = new LinearLayout(this);
		linearBody.addView(childLinear);
		inflater.inflate(R.layout.sub_welcome, childLinear, true);

		// Text Title
		textTitle.setText("HOME");
		textTitle.setTextColor(getResources().getColor(R.color.cl_white));
		relativeHeader.setBackgroundColor(getResources().getColor(
				R.color.cl_black));

		View view = inflater.inflate(R.layout.sub_welcome, childLinear, true);
		textHomeInfo = (TextView) view.findViewById(R.id.txtHomeInfo);
		TextView textCount = (TextView) view
				.findViewById(R.id.txtNumberOfTasks);
		int total = UtilityMethod.separateTaskKanban(1).size()
				+ UtilityMethod.separateTaskKanban(2).size();
		textCount.setText(total + " ");

		if (Global.language == 0) {
			textTitle.setText(getResources().getString(R.string.en_Home));
			textHomeInfo
					.setText(getResources().getString(R.string.en_HomeInfo));
		} else {
			textTitle.setText(getResources().getString(R.string.fr_Home));
			textHomeInfo
					.setText(getResources().getString(R.string.fr_HomeInfo));
		}
	}

	private void changeToAdd() {
		// Main layout
		linearBody.removeAllViews();
		LinearLayout childLinear = new LinearLayout(this);
		linearBody.addView(childLinear);
		inflater.inflate(R.layout.sub_add, childLinear, true);

		// TextTitle
		textTitle.setText("NEW");
		textTitle.setTextColor(getResources().getColor(R.color.cl_black));
		relativeHeader.setBackgroundColor(getResources().getColor(
				R.color.cl_yellow_android));

		View view = inflater.inflate(R.layout.sub_welcome, childLinear, true);
		editTaskName = (EditText) view.findViewById(R.id.edtTask);
		editTaskInfo = (EditText) view.findViewById(R.id.edtDescription);
		datepickerStart = (DatePicker) view.findViewById(R.id.dpkStart);
		datepickerEnd = (DatePicker) view.findViewById(R.id.dpkEnd);
		buttonOkAdd = (Button) view.findViewById(R.id.btnAdd);
		buttonOkAdd.setOnClickListener(onClickListener);
		textTaskNew = (TextView) view.findViewById(R.id.txtTaskNew);
		textTaskDescriptionNew = (TextView) view
				.findViewById(R.id.txtTaskDesciptionNew);
		textTaskStartNew = (TextView) view.findViewById(R.id.txtTaskStartNew);
		textTaskEndNew = (TextView) view.findViewById(R.id.txtTaskEndNew);

		editTaskName.setOnTouchListener(onTouchListener);
		editTaskInfo.setOnTouchListener(onTouchListener);
		buttonOkAdd.setOnTouchListener(onTouchListener);

		if (Global.language == 0) {
			textTitle.setText(getResources().getString(R.string.en_Add));
			textTaskNew.setText(getResources().getString(R.string.en_AddTask));
			textTaskDescriptionNew.setText(getResources().getString(
					R.string.en_AddDescription));
			textTaskStartNew.setText(getResources().getString(
					R.string.en_AddStart));
			textTaskEndNew
					.setText(getResources().getString(R.string.en_AddEnd));
			buttonOkAdd.setText(getResources().getString(R.string.en_AddAdd));
		} else {
			textTitle.setText(getResources().getString(R.string.fr_Add));
			textTaskNew.setText(getResources().getString(R.string.fr_AddTask));
			textTaskDescriptionNew.setText(getResources().getString(
					R.string.fr_AddDescription));
			textTaskStartNew.setText(getResources().getString(
					R.string.fr_AddStart));
			textTaskEndNew
					.setText(getResources().getString(R.string.fr_AddEnd));
			buttonOkAdd.setText(getResources().getString(R.string.fr_AddAdd));
		}
	}

	private void changeToGantt() {
		// Main layout
		linearBody.removeAllViews();
		LinearLayout childLinear = new LinearLayout(this);
		linearBody.addView(childLinear);
		inflater.inflate(R.layout.sub_gantt, childLinear, true);
		// Text Tile
		textTitle.setTextColor(getResources().getColor(R.color.cl_black));
		relativeHeader.setBackgroundColor(getResources().getColor(
				R.color.cl_blue_android));

		View view = inflater.inflate(R.layout.sub_welcome, childLinear, true);
		listTaskGantt = (ListView) view.findViewById(R.id.lstItemTaskGantt);

		adapterListTaskGantt = new ItemlistAdapterCustomGantt(this,
				Global.tasks);
		listTaskGantt.setAdapter(adapterListTaskGantt);

		if (Global.language == 0) {
			textTitle.setText(getResources().getString(R.string.en_Statistic));
		} else {
			textTitle.setText(getResources().getString(R.string.fr_Statistic));
		}
	}

	private void changeToKanban() {
		// Main layout
		linearBody.removeAllViews();
		LinearLayout childLinear = new LinearLayout(this);
		linearBody.addView(childLinear);
		inflater.inflate(R.layout.sub_kanban, childLinear, true);

		// Text Tile
		textTitle.setText("KANBAN");
		textTitle.setTextColor(getResources().getColor(R.color.cl_black));
		relativeHeader.setBackgroundColor(getResources().getColor(
				R.color.cl_green_android));
		View view = inflater.inflate(R.layout.sub_welcome, childLinear, true);
		listTodo = (ListView) view.findViewById(R.id.lstColumnTodo);
		todos = UtilityMethod.separateTaskKanban(1);
		adapterTodo = new ItemKanbanAdapterCustom(this, todos);
		listTodo.setAdapter(adapterTodo);

		listDoing = (ListView) view.findViewById(R.id.lstColumnDoing);
		doings = UtilityMethod.separateTaskKanban(2);
		adapterDoing = new ItemKanbanAdapterCustom(this, doings);
		listDoing.setAdapter(adapterDoing);

		listDone = (ListView) view.findViewById(R.id.lstColumnDone);
		dones = UtilityMethod.separateTaskKanban(3);
		adapterDone = new ItemKanbanAdapterCustom(this, dones);
		listDone.setAdapter(adapterDone);
	}

	private void changeToEdit() {
		// Main layout
		linearBody.removeAllViews();
		LinearLayout childLinear = new LinearLayout(this);
		linearBody.addView(childLinear);
		inflater.inflate(R.layout.sub_edit, childLinear, true);

		// Text Tile
		textTitle.setTextColor(getResources().getColor(R.color.cl_black));
		relativeHeader.setBackgroundColor(getResources().getColor(
				R.color.cl_red_android));

		View view = inflater.inflate(R.layout.sub_welcome, childLinear, true);
		listTask = (ListView) view.findViewById(R.id.lstItemTask);
		adapterListTask = new ItemlistAdapterCustom(this, Global.tasks);
		listTask.setAdapter(adapterListTask);

		if (Global.language == 0) {
			textTitle.setText(getResources().getString(R.string.en_Edit));
		} else {
			textTitle.setText(getResources().getString(R.string.fr_Edit));
		}
	}

	private void changeToSetting() { // Main layout
		linearBody.removeAllViews();
		LinearLayout childLinear = new LinearLayout(this);
		linearBody.addView(childLinear);
		inflater.inflate(R.layout.sub_setting, childLinear, true);

		// Text Tile
		textTitle.setTextColor(getResources().getColor(R.color.cl_black));
		relativeHeader.setBackgroundColor(getResources().getColor(
				R.color.cl_violet_android));

		View view = inflater.inflate(R.layout.sub_welcome, childLinear, true);
		buttonAbout = (Button) view.findViewById(R.id.btnAbout);
		buttonAbout.setOnClickListener(onClickListener);
		buttonLogout = (Button) view.findViewById(R.id.btnLogout);
		buttonLogout.setOnClickListener(onClickListener);
		textUserName = (TextView) view.findViewById(R.id.txtUsername);
		textUserName.setText(Global.database.getUser(Global.userIDNow)
				.getUserName());

		buttonAlarme = (ToggleButton) view.findViewById(R.id.btnAlarme);
		buttonAlarme.setOnClickListener(onClickListener);
		if (Global.alarm == 1) {
			buttonAlarme.setChecked(true);
		} else if (Global.alarm == 0) {
			buttonAlarme.setChecked(false);
		}

		textSettingsAccount = (TextView) view
				.findViewById(R.id.textSettingsAccount);
		textSettingsAlarm = (TextView) view
				.findViewById(R.id.textSettingsAlarm);
		textSettingsLanguage = (TextView) view
				.findViewById(R.id.textSettingsLanguage);
		textSettingsLimit = (TextView) view
				.findViewById(R.id.textSettingsLimit);

		adapterSpinnerLimit = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, Global.LIMITS);
		adapterSpinnerLimit
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerLimit = (Spinner) view.findViewById(R.id.spnLimit);
		spinnerLimit.setAdapter(adapterSpinnerLimit);
		adapterSpinnerLimit.notifyDataSetChanged();
		if (Global.limit == 0) {
			spinnerLimit.setSelection(0, true);
		} else if (Global.limit == 1) {
			spinnerLimit.setSelection(1, true);
		} else if (Global.limit == 2) {
			spinnerLimit.setSelection(2, true);
		}
		spinnerLimit.setOnItemSelectedListener(onItemSelectedListener);

		adapterSpinnerLanguage = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, Global.LANGUAGES);
		adapterSpinnerLanguage
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerLanguague = (Spinner) view.findViewById(R.id.spnLangague);
		spinnerLanguague.setAdapter(adapterSpinnerLanguage);
		adapterSpinnerLanguage.notifyDataSetChanged();
		if (Global.language == 0) {
			spinnerLanguague.setSelection(0, true);
		} else {
			spinnerLanguague.setSelection(1, true);
		}
		spinnerLanguague.setOnItemSelectedListener(onItemSelectedListener);

		if (Global.userIDNow == 0) {
			buttonAlarme.setEnabled(false);
			spinnerLanguague.setEnabled(false);
			spinnerLimit.setEnabled(false);
		} else {
			buttonAlarme.setEnabled(true);
			spinnerLanguague.setEnabled(true);
			spinnerLimit.setEnabled(true);
		}

		if (Global.language == 0) {
			textTitle.setText(getResources().getString(R.string.en_Settings));
			textSettingsAccount.setText(getResources().getString(
					R.string.en_SettingsAccount));
			textSettingsAlarm.setText(getResources().getString(
					R.string.en_SettingsAlarm));
			textSettingsLimit.setText(getResources().getString(
					R.string.en_SettingsLimit));
			textSettingsLanguage.setText(getResources().getString(
					R.string.en_SettingsLanguage));
			buttonAbout.setText(getResources().getString(
					R.string.en_SettingsAbout));
		} else {
			textTitle.setText(getResources().getString(R.string.fr_Settings));
			textSettingsAccount.setText(getResources().getString(
					R.string.fr_SettingsAccount));
			textSettingsAlarm.setText(getResources().getString(
					R.string.fr_SettingsAlarm));
			textSettingsLimit.setText(getResources().getString(
					R.string.fr_SettingsLimit));
			textSettingsLanguage.setText(getResources().getString(
					R.string.fr_SettingsLanguage));
			buttonAbout.setText(getResources().getString(
					R.string.fr_SettingsAbout));
		}
	}

	// Setter and Getter
	public List<BOTask> getProfils() {
		return profils;
	}

	public void setProfils(List<BOTask> profils) {
		this.profils = profils;
	}

	// InnerClass for edit
	class ItemlistAdapterCustom extends BaseAdapter {
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
			TextView textTaskName = (TextView) view
					.findViewById(R.id.txtTaskName);
			TextView textDataStart = (TextView) view
					.findViewById(R.id.txtDateStart);
			TextView textDataEnd = (TextView) view
					.findViewById(R.id.txtDateEnd);
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
				public void onClick(final View v) {
					Activity activity = (Activity) mContext;
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Global.tempsItem = (Integer) v.getTag();
							linearAnnonceEdit.setVisibility(View.VISIBLE);
							BOTask tempTask = Global.database
									.getATask(Global.tasks
											.get(Global.tempsItem).getTaskId());
							editTaskEdit.setText(tempTask.getTaskName()
									.toString());
							editDescriptionEdit.setText(tempTask.getInfo()
									.toString());
							datepickerStartEdit.updateDate(UtilityMethod
									.getYearFromDatepicker(tempTask
											.getStartDate()), UtilityMethod
									.getMonthFromDatepicker(tempTask
											.getStartDate()) - 1, UtilityMethod
									.getDayFromDatepicker(tempTask
											.getStartDate()));
							datepickerEndEdit.updateDate(UtilityMethod
									.getYearFromDatepicker(tempTask
											.getEndDate()), UtilityMethod
									.getMonthFromDatepicker(tempTask
											.getEndDate()) - 1,
									UtilityMethod.getDayFromDatepicker(tempTask
											.getEndDate()));
						}
					});
					if (Global.language == 0) {
						textAnnonceTaskNew.setText(getResources().getString(
								R.string.en_AddTask));
						textAnnonceTaskDescriptionNew.setText(getResources()
								.getString(R.string.en_AddDescription));
						textAnnonceTaskStartNew.setText(getResources()
								.getString(R.string.en_AddStart));
						textAnnonceTaskEndNew.setText(getResources().getString(
								R.string.en_AddEnd));
						buttonConfirmEdit.setText(getResources().getString(
								R.string.en_AddComfirm));
					} else {
						textAnnonceTaskNew.setText(getResources().getString(
								R.string.fr_AddTask));
						textAnnonceTaskDescriptionNew.setText(getResources()
								.getString(R.string.fr_AddDescription));
						textAnnonceTaskStartNew.setText(getResources()
								.getString(R.string.fr_AddStart));
						textAnnonceTaskEndNew.setText(getResources().getString(
								R.string.fr_AddEnd));
						buttonConfirmEdit.setText(getResources().getString(
								R.string.fr_AddConfirm));
					}
				}
			});

			buttonDelete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(final View v) {
					Activity activity = (Activity) mContext;
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Global.tempsItem = (Integer) v.getTag();
							linearAnnonceDelete.setVisibility(View.VISIBLE);
						}
					});

					if (Global.language == 0) {
						textAnnonceDelete.setText(getResources().getString(
								R.string.en_AnnonceDelete));
						buttonYesDelete.setText(getResources().getString(
								R.string.en_Yes));
						buttonNonDelete.setText(getResources().getString(
								R.string.en_Non));
					} else {
						textAnnonceDelete.setText(getResources().getString(
								R.string.fr_AnnonceDelete));
						buttonYesDelete.setText(getResources().getString(
								R.string.fr_Yes));
						buttonNonDelete.setText(getResources().getString(
								R.string.fr_Non));
					}
				}
			});

			return view;
		}
	}

	// InnerClass for Gantt
	class ItemlistAdapterCustomGantt extends BaseAdapter {
		private Context mContext;
		private List<BOTask> mTasks;

		public ItemlistAdapterCustomGantt(Context mContext, List<BOTask> mTasks) {
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
			view = inflater.inflate(R.layout.item_gantt, null);

			if (UtilityMethod.getWorkDay(position) < 0
					|| UtilityMethod.getAllDayInt(position) < 0
					|| UtilityMethod.getWorkDay(position) > UtilityMethod
							.getAllDayInt(position)
					|| UtilityMethod.getLeftDay(position) < 0) {
				view = inflater.inflate(R.layout.item_null, null);
				return view;
			}

			// get the reference of textViews
			TextView textTaskNameGantt = (TextView) view
					.findViewById(R.id.txtTaskNameGantt);
			RelativeLayout relativeParentTimeBar = (RelativeLayout) view
					.findViewById(R.id.rltParentTimeBar);
			LinearLayout linearTimeBarBehind = (LinearLayout) view
					.findViewById(R.id.lnrTimeBar);

			// Set the data
			textTaskNameGantt.setText(mTasks.get(position).getTaskName());
			textTaskNameGantt.setTextColor(Color.BLACK);

			LinearLayout linearTime = new LinearLayout(mContext);
			linearTime.setBackgroundResource(R.color.cl_white);
			relativeParentTimeBar.addView(
					linearTime,
					new LinearLayout.LayoutParams(UtilityMethod
							.changeTimeToIntGantt(position, 323), 60));

			LinearLayout linearTimeBarReal = new LinearLayout(mContext);
			relativeParentTimeBar.addView(linearTimeBarReal,
					linearTimeBarBehind.getLayoutParams());
			TextView textTimeBar = new TextView(mContext);
			textTimeBar.setText(UtilityMethod.getWorkDay(position) + "/"
					+ UtilityMethod.getAllDayInt(position));
			textTimeBar.setTextSize(15);
			textTimeBar.setTextColor(Color.BLACK);
			linearTimeBarReal.addView(textTimeBar,
					new LinearLayout.LayoutParams(100, 40));
			return view;
		}
	}

	class ItemKanbanAdapterCustom extends BaseAdapter {
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
			textTaskKanban.setTag(position);
			textTaskKanban.setText(mTasks.get(position).getTaskName());
			textTaskKanban.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(final View v) {
					Activity activity = (Activity) mContext;
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Global.tempsItem = mTasks.get((Integer) v.getTag())
									.getTaskId();
							linearAnnonceKanban.setVisibility(View.VISIBLE);
							if ((Global.limit + 2) == UtilityMethod
									.separateTaskKanban(2).size()) {
								buttonDoing.setEnabled(false);
							} else {
								buttonDoing.setEnabled(true);
							}
						}
					});

					if (Global.language == 0) {
						textAnnonceKanban.setText(getResources().getString(
								R.string.en_AnnonceKanbanChose));
					} else {
						textAnnonceKanban.setText(getResources().getString(
								R.string.fr_AnnonceKanbanChose));
					}
				}
			});
			return view;
		}
	}

	// Limit date
	// From : http://www.appstraxion.fr/date-range-datepickerdialog-android/
	// Thanks
	class LimitedRangeDatePickerDialog extends DatePickerDialog {
		private Calendar minDate;
		private Calendar maxDate;
		private java.text.DateFormat mTitleDateFormat;

		public LimitedRangeDatePickerDialog(Context context,
				DatePickerDialog.OnDateSetListener callBack, int year,
				int monthOfYear, int dayOfMonth, Calendar minDate,
				Calendar maxDate) {
			super(context, callBack, year, monthOfYear, dayOfMonth);
			this.minDate = minDate;
			this.maxDate = maxDate;
			mTitleDateFormat = java.text.DateFormat
					.getDateInstance(java.text.DateFormat.FULL);
		}

		public void onDateChanged(DatePicker view, int year, int month, int day) {
			Calendar newDate = Calendar.getInstance();
			newDate.set(year, month, day);

			if (minDate != null && minDate.after(newDate)) {
				view.init(minDate.get(Calendar.YEAR),
						minDate.get(Calendar.MONTH),
						minDate.get(Calendar.DAY_OF_MONTH), this);
				setTitle(mTitleDateFormat.format(minDate.getTime()));
			} else if (maxDate != null && maxDate.before(newDate)) {
				view.init(maxDate.get(Calendar.YEAR),
						maxDate.get(Calendar.MONTH),
						maxDate.get(Calendar.DAY_OF_MONTH), this);
				setTitle(mTitleDateFormat.format(maxDate.getTime()));
			} else {
				view.init(year, month, day, this);
				setTitle(mTitleDateFormat.format(newDate.getTime()));
			}
		}
	}

	class SpinnerCustom implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	}
}
