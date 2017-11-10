package com.read.mobile.module.activity;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.ngc.corelib.http.AsyncTaskListener;
import com.ngc.corelib.http.NetAsyncTask;
import com.ngc.corelib.http.bean.BaseResult;
import com.ngc.corelib.utils.Logger;
import com.ngc.corelib.utils.Tips;
import com.ngc.corelib.views.NGCDatePicker;
import com.read.mobile.R;
import com.read.mobile.beans.ChildAddRequest;
import com.read.mobile.beans.ChildAddResult;
import com.read.mobile.beans.ChildInfoResult;
import com.read.mobile.constants.Contacts;
import com.read.mobile.env.BaseActivity;
import com.read.mobile.utils.SaveUtils;
import com.read.mobile.views.ChangeNameDialog;
import com.read.mobile.views.ChangeNameDialog.OnInputOkListener;

/**
 * 
 * @Title: ChildInfoActivity.java
 * @Package: com.read.mobile.module.activity
 * @Description: 孩子信息
 */
public class ChildInfoActivity extends BaseActivity {

	private ChildInfoResult infoResult;

	private TextView nameTV;
	private RadioGroup rg;
	private RadioButton nvRB;
	private RadioButton nanRB;
	private TextView birthTV;
	private TextView aihaoTV;

	private Button btn;

	private String sexStr = "0";

	@Override
	protected void initData() {
		super.initData();
		infoResult = (ChildInfoResult) getIntent().getSerializableExtra("ChildInfoResult");
	}

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_child_info;
	}

	@Override
	protected void initViwes() {
		showLeftBack(null);
		nameTV = (TextView) findViewById(R.id.child_name_tv);
		nvRB = (RadioButton) findViewById(R.id.child_sex_nv_rb);
		nanRB = (RadioButton) findViewById(R.id.child_sex_nan_rb);
		birthTV = (TextView) findViewById(R.id.child_birth_tv);
		aihaoTV = (TextView) findViewById(R.id.child_aihao_tv);
		btn = (Button) findViewById(R.id.child_btn);
		btn.setOnClickListener(this);
		rg = (RadioGroup) findViewById(R.id.child_sex_rg);

		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				if (arg1 == R.id.child_sex_nv_rb) {
					sexStr = "0";
				} else {
					sexStr = "1";
				}
			}
		});
		setInfo();
		if (infoResult != null && infoResult.getData() != null && !TextUtils.isEmpty(infoResult.getData().getName())) {
			btn.setText("确认修改");
			btn.setVisibility(View.GONE);
			showRightTV("修改", new OnClickListener() {
				@Override
				public void onClick(View paramView) {
					btn.setVisibility(View.VISIBLE);
					findViewById(R.id.child_name_ll).setOnClickListener(ChildInfoActivity.this);
					findViewById(R.id.child_birth_ll).setOnClickListener(ChildInfoActivity.this);
					findViewById(R.id.child_aihao_ll).setOnClickListener(ChildInfoActivity.this);
				}
			});
		} else {
			btn.setText("确认添加");
			findViewById(R.id.child_name_ll).setOnClickListener(this);
			findViewById(R.id.child_birth_ll).setOnClickListener(this);
			findViewById(R.id.child_aihao_ll).setOnClickListener(this);
		}

	}

	private void setInfo() {
		if (infoResult != null && TextUtils.isEmpty(infoResult.getData().getTag())) {
			nameTV.setText(infoResult.getData().getName());
			if (infoResult.getData().getSex().equals(Contacts.SEX_NAN)) {
				nanRB.setSelected(true);
				Logger.e("woshinan");
				nvRB.setSelected(false);
				rg.check(nanRB.getId());
			} else {
				nvRB.setSelected(true);
				Logger.e("woshinv");
				nanRB.setSelected(false);
				rg.check(nvRB.getId());
			}
			birthTV.setText(infoResult.getData().getBirthday());
			aihaoTV.setText(infoResult.getData().getIntrest());
		}
	}

	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		switch (arg0.getId()) {
		case R.id.child_name_ll: {
			ChangeNameDialog dialog = new ChangeNameDialog();
			dialog.setOnInputOkListener(new OnInputOkListener() {
				@Override
				public void inputOk(String str) {
					nameTV.setText(str);
				}
			});
			dialog.setCancelable(false);
			dialog.show(getSupportFragmentManager(), "dialog");
			break;
		}
		case R.id.child_birth_ll:
			NGCDatePicker datePicker = new NGCDatePicker();
			datePicker.setListener(new NGCDatePicker.OnSelectListener() {
				@Override
				public void onDateSetListener(int year, int month, int day) {
					birthTV.setText(year + "-" + (month + 1) + "-" + day);
				}
			});
			datePicker.show(getFragmentManager(), "sdatePicker");
			break;
		case R.id.child_aihao_ll: {
			ChangeNameDialog dialog = new ChangeNameDialog();
			dialog.setOnInputOkListener(new OnInputOkListener() {
				@Override
				public void inputOk(String str) {
					aihaoTV.setText(str);
				}
			});
			dialog.setCancelable(false);
			dialog.show(getSupportFragmentManager(), "dialog");
			break;
		}
		case R.id.child_btn:
			addChild();
			break;
		}
	}

	/**
	 * 添加小孩信息
	 */
	private void addChild() {
		String nameStr = nameTV.getText().toString();
		String birthStr = birthTV.getText().toString();
		String aihaoStr = aihaoTV.getText().toString();
		if (TextUtils.isEmpty(nameStr)) {
			Tips.tipLong(this, "请输入名字");
			return;
		}
		if (TextUtils.isEmpty(birthStr)) {
			Tips.tipLong(this, "请选择生日");
			return;
		}
		if (TextUtils.isEmpty(sexStr)) {
			Tips.tipLong(this, "请选择性别");
			return;
		}
		if (TextUtils.isEmpty(aihaoStr)) {
			Tips.tipLong(this, "请输入爱好");
			return;
		}
		ChildAddRequest request = new ChildAddRequest();
		request.setBirthday(birthStr);
		request.setIntrest(aihaoStr);
		request.setName(nameStr);
		request.setSex(sexStr);
		request.setU(SaveUtils.getUserId());
		NetAsyncTask asyncTask = new NetAsyncTask(ChildAddResult.class, new AsyncTaskListener() {
			@Override
			public void onTaskSuccess(int requestId, BaseResult result) {
				dismissProgressDialog();
				if (result.getHead().getCode().equals(Contacts.RESULT_CODE_OK)) {
					if (btn.getText().equals("确认修改")) {
						Tips.tipLong(ChildInfoActivity.this, "修改成功");
					} else {
						Tips.tipLong(ChildInfoActivity.this, "添加成功");
					}
				} else {
					Tips.tipLong(ChildInfoActivity.this, result.getHead().getMessage());
				}
			}

			@Override
			public void onTaskStart(int requestId) {
				showProgressDialog("正在请求~");
			}

			@Override
			public void onTaskFail(int requestId, Exception e) {
				dismissProgressDialog();
				Tips.tipLong(ChildInfoActivity.this, e.getLocalizedMessage());
			}
		}, request);
		asyncTask.execute(Contacts.URL_CHILD_ADDCHILD);
	}
}
