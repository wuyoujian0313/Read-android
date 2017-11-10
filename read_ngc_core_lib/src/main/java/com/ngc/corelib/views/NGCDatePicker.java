package com.ngc.corelib.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

public class NGCDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

	private OnSelectListener listener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
		pickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
		return pickerDialog;
	}

	public void setListener(OnSelectListener listener) {
		this.listener = listener;
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		Log.d("OnDateSet", "select year:" + year + ";month:" + month + ";day:" + day);
		listener.onDateSetListener(year, month, day);
	}

	public static interface OnSelectListener {
		public void onDateSetListener(int year, int month, int day);
	}

}
