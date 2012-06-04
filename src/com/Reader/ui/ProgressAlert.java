package com.Reader.ui;

import com.Reader.Main.R;
import com.Reader.Main.ReadingActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

public class ProgressAlert extends PopupWindow implements
		SeekBar.OnSeekBarChangeListener {

	private TextView mProgressNumber;
	private SeekBar mProgress;
	private int mOldBrightness;
	private int mOldAutomatic;

	private boolean mAutomaticAvailable;

	private static final int MINIMUM_BACKLIGHT = 0;
	private static final int MAXIMUM_BACKLIGHT = 100;
	private ReadingActivity mContext;

	public ProgressAlert(Context context) {
		super(context);
		mContext = (ReadingActivity) context;
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.progressalert, null);
		mProgress = (SeekBar) view.findViewById(R.id.progress);
		mProgress.setOnSeekBarChangeListener(this);
		mProgress.setMax(MAXIMUM_BACKLIGHT - MINIMUM_BACKLIGHT);
		mProgress.setProgress(mContext.bookView.getPageConfig().getTextSize());
		this.setContentView(view);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
	}

	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
			
	}

	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		ReadingActivity ac = (ReadingActivity)mContext;
		mContext.bookView.getPageConfig().setTextSize(seekBar.getProgress());
		mContext.bookView.getPageConfig().saveConfig();
		ac.bookmanager.getBookView().update();
	}
}