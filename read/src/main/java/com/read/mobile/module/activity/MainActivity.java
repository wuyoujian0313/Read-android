package com.read.mobile.module.activity;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.ngc.corelib.systembar.SystemBarTintManager;
import com.ngc.corelib.utils.ActivitysManager;
import com.ngc.corelib.utils.ExitUtils;
import com.read.mobile.R;
import com.read.mobile.module.fragment.BookCityFragment;
import com.read.mobile.module.fragment.BooksFragment;
import com.read.mobile.module.fragment.MemberFragment;
import com.read.mobile.module.fragment.NoteFragment;

public class MainActivity extends FragmentActivity {
	protected static final String TAG = "MainActivity";

	private View currentView;
	private ImageView currentIV;

	private View shuchengView, shumulView, bijiView, huiyuanView;
	private ImageView shuchengIV, shumuIV, bijiIV, huiyuanIV;

	private void setButton(View v, ImageView iv) {
		if (currentView != null && currentView.getId() != v.getId()) {
			currentView.setEnabled(true);
		}
		if (currentIV != null && currentIV.getId() != iv.getId()) {
			currentIV.setEnabled(true);
		}
		v.setEnabled(false);
		iv.setEnabled(false);
		currentView = v;
		currentIV = iv;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivitysManager.addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(getLayoutResID());
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.ngc_color);
		initFragment();
		initViwes();
	}

	protected int getLayoutResID() {
		return R.layout.activity_main;
	}

	protected void initViwes() {

		shuchengView = findViewById(R.id.main_shucheng_ll);
		shuchengIV = (ImageView) findViewById(R.id.main_shucheng_iv);

		shumulView = findViewById(R.id.main_shumu_ll);
		shumuIV = (ImageView) findViewById(R.id.main_shumu_iv);

		bijiView = findViewById(R.id.main_biji_ll);
		bijiIV = (ImageView) findViewById(R.id.main_biji_iv);

		huiyuanView = findViewById(R.id.main_huiyuan_ll);
		huiyuanIV = (ImageView) findViewById(R.id.main_huiyuan_iv);

		shuchengView.setOnClickListener(homeOnClickListener);
		shumulView.setOnClickListener(shumuOnClickListener);
		bijiView.setOnClickListener(bijiOnClickListener);
		huiyuanView.setOnClickListener(huiyuanOnClickListener);

		shuchengView.performClick();
	}

	private BookCityFragment homeFragment;
	private BooksFragment channelFragment;
	private NoteFragment bijiFragment;
	private MemberFragment huiyuanFragment;

	private FragmentManager fm;

	private void initFragment() {
		fm = getFragmentManager();
	}

	private void hideFragments(FragmentTransaction transaction) {
		if (homeFragment != null) {
			transaction.hide(homeFragment);
		}
		if (channelFragment != null) {
			transaction.hide(channelFragment);
		}
		if (bijiFragment != null) {
			transaction.hide(bijiFragment);
		}
		if (huiyuanFragment != null) {
			transaction.hide(huiyuanFragment);
		}
	}

	private OnClickListener homeOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			FragmentTransaction ft = fm.beginTransaction();
			hideFragments(ft);
			if (homeFragment == null) {
				homeFragment = new BookCityFragment();
				ft.add(R.id.fl_content, homeFragment);
			} else {
				ft.show(homeFragment);
			}
			ft.commit();
			setButton(v, shuchengIV);
		}
	};

	private OnClickListener shumuOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			FragmentTransaction ft = fm.beginTransaction();
			hideFragments(ft);
			if (channelFragment == null) {
				channelFragment = new BooksFragment();
				ft.add(R.id.fl_content, channelFragment);
			} else {
				ft.show(channelFragment);
			}
			ft.commit();
			setButton(v, shumuIV);

		}
	};

	private OnClickListener bijiOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			FragmentTransaction ft = fm.beginTransaction();
			hideFragments(ft);
			if (bijiFragment == null) {
				bijiFragment = new NoteFragment();
				ft.add(R.id.fl_content, bijiFragment);
			} else {
				ft.show(bijiFragment);
			}
			ft.commit();
			setButton(v, bijiIV);
		}
	};
	private OnClickListener huiyuanOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			FragmentTransaction ft = fm.beginTransaction();
			hideFragments(ft);
			if (huiyuanFragment == null) {
				huiyuanFragment = new MemberFragment();
				ft.add(R.id.fl_content, huiyuanFragment);
			} else {
				ft.show(huiyuanFragment);
			}
			ft.commit();
			setButton(v, huiyuanIV);
		}
	};

	@Override
	public void onBackPressed() {
		ExitUtils.getInstance().isExit(this);
	}

	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}
}
