package com.ifi.votretemps.screen;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ifi.votretemps.model.bo.BOUser;
import com.ifi.votretemps.utils.Global;
import com.ifi.votretemps.utils.UtilityDatabase;

public class LoginActivity extends Activity {

	private EditText editUser;
	private EditText editPassword;
	private Button buttonLogin;
	private Button buttonDemo;
	private TextView textAnnonce;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_login);

		initData();
		initUI();

		Global.users = Global.database.getAllUsers();

		buttonLogin.setOnClickListener(onClickListener);
		buttonDemo.setOnClickListener(onClickListener);
		editPassword.setOnFocusChangeListener(onFocusChangeListener);
		editUser.setOnFocusChangeListener(onFocusChangeListener);
	}

	OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View v) {
			if (Global.language == 0) {
				textAnnonce.setText(getResources().getString(
						R.string.en_annonce));
			} else if (Global.language == 1) {
				textAnnonce.setText(getResources().getString(
						R.string.fr_annonce));
			}

			if (v == buttonLogin) {
				if (editUser.getText().toString().compareTo("duy") == 0
						&& editPassword.getText().toString().compareTo("4444") == 0) {
					textAnnonce.setVisibility(View.INVISIBLE);
					Global.userIDNow = 4444;
					Global.users = Global.database.getAllUsers();

					boolean checkExist = false;
					for (int i = 0; i < Global.users.size(); i++) {
						if (Global.users.get(i).getUserId() == 4444) {
							checkExist = true;
							break;
						}
					}
					if (checkExist == false) {
						Global.database
								.addUser(new BOUser(4444, "duy", "4444"));
					}

					Global.tasks = Global.database.getTask(Global.userIDNow);

					SharedPreferences preferences = getSharedPreferences(
							"save", MODE_PRIVATE);
					SharedPreferences.Editor edit = preferences.edit();
					edit.putInt("saveUserid", Global.userIDNow);
					edit.commit();

					Intent newIntent = new Intent(LoginActivity.this,
							MainActivity.class);
					LoginActivity.this.startActivity(newIntent);
					finish();
				} else if (editUser.getText().toString().compareTo("nvtho") == 0
						&& editPassword.getText().toString().compareTo("123") == 0) {
					textAnnonce.setVisibility(View.INVISIBLE);
					Global.userIDNow = 333;
					Global.users = Global.database.getAllUsers();

					boolean checkExist = false;
					for (int i = 0; i < Global.users.size(); i++) {
						if (Global.users.get(i).getUserId() == 333) {
							checkExist = true;
							break;
						}
					}
					if (checkExist == false) {
						Global.database
								.addUser(new BOUser(333, "nvtho", "123"));
					}
					Global.tasks = Global.database.getTask(Global.userIDNow);

					SharedPreferences preferences = getSharedPreferences(
							"save", MODE_PRIVATE);
					SharedPreferences.Editor edit = preferences.edit();
					edit.putInt("saveUserid", Global.userIDNow);
					edit.commit();

					Intent newIntent = new Intent(LoginActivity.this,
							MainActivity.class);
					LoginActivity.this.startActivity(newIntent);
					finish();
				} else if (UtilityDatabase.checkIndentifyUser(editUser
						.getText().toString(), editPassword.getText()
						.toString(), Global.users) == 0) {
					textAnnonce.setVisibility(View.VISIBLE);
				} else {
					Global.userIDNow = UtilityDatabase.checkIndentifyUser(
							editUser.getText().toString(), editPassword
									.getText().toString(), Global.users);
					textAnnonce.setVisibility(View.INVISIBLE);
					Global.tasks = Global.database.getTask(Global.userIDNow);

					SharedPreferences preferences = getSharedPreferences(
							"save", MODE_PRIVATE);
					SharedPreferences.Editor edit = preferences.edit();
					edit.putInt("saveUserid", Global.userIDNow);
					edit.commit();

					Intent newIntent = new Intent(LoginActivity.this,
							MainActivity.class);
					LoginActivity.this.startActivity(newIntent);
					finish();

				}
			} else if (v == buttonDemo) {
				Global.userIDNow = 0;
				Global.tasks = Global.database.getTask(Global.userIDNow);

				SharedPreferences preferences = getSharedPreferences("save",
						MODE_PRIVATE);
				SharedPreferences.Editor edit = preferences.edit();
				edit.putInt("saveUserid", Global.userIDNow);
				edit.commit();

				Intent newIntent = new Intent(LoginActivity.this,
						MainActivity.class);
				LoginActivity.this.startActivity(newIntent);
				finish();
			}
		}
	};

	OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (v == editUser || v == editPassword) {
				textAnnonce.setVisibility(View.INVISIBLE);
			}
		}
	};

	private void initUI() {
		editUser = (EditText) findViewById(R.id.edtUser);
		editPassword = (EditText) findViewById(R.id.edtPassword);
		buttonLogin = (Button) findViewById(R.id.btnLogin);
		buttonDemo = (Button) findViewById(R.id.btnDemo);
		textAnnonce = (TextView) findViewById(R.id.txtAnnonce);
	}

	private void initData() {
		Global.userIDNow = 99999;
		SharedPreferences preferences = getSharedPreferences("save",
				MODE_PRIVATE);
		SharedPreferences.Editor edit = preferences.edit();
		edit.putInt("saveUserid", Global.userIDNow);
		edit.commit();
	}
}
