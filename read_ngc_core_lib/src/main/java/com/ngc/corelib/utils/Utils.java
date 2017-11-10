package com.ngc.corelib.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 工具类
 */

public class Utils {
	/**
	 * 获取AppName
	 * 
	 * @param mContext
	 * @return
	 */
	public static String getAppPackageName(Context mContext) {
		try {
			PackageManager pManager = mContext.getPackageManager();
			PackageInfo pInfo = pManager.getPackageInfo(mContext.getPackageName(), 0);
			String packageName = pInfo.packageName;
			if (packageName != null) {
				return packageName;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "NGC";
	}

	public static int setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return 0;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);

		return params.height;
	}

}
