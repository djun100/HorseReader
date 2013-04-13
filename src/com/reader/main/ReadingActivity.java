/*
 * QQ:1127082711
 * 
 * xinlangweibo:http://weibo.com/muchenshou
 * 
 * email:muchenshou@gmail.com
 * */
package com.reader.main;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import com.reader.book.bookview.BookView;
import com.reader.book.bookview.NoTurnAnimation;
import com.reader.book.bookview.SimulateTurnPage;
import com.reader.book.manager.BookManager;
import com.reader.preference.ReadingSetting;
import com.reader.record.BookHistory;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class ReadingActivity extends Activity {
	public BookView bookView;
	public BookManager bookmanager;
	private String mBookName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		String bookName = getIntent().getStringExtra("bookname");
		this.mBookName = bookName;
		int position = 0;
		BookHistory history = new BookHistory(this);
		if (history.exist(bookName)) {
			position = history.getPosition(bookName);
		} else {
			history.updateHistory(bookName, 0);
		}

		try {
			if (!new File(bookName).exists()) {
				Toast.makeText(this, "文件不存在", Toast.LENGTH_LONG).show();
				return;
			}
			bookmanager = new BookManager(ReadingActivity.this, new File(
					bookName));
			bookView = bookmanager.getBookView();
			setLookingBookView();
			bookmanager.openBook(position);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onStop() {
		BookHistory history = new BookHistory(this);
		Log.i("ReadingOnStop",
				"" + this.mBookName + this.bookmanager.getReadingPosition());
		history.updateHistory(this.mBookName,
				this.bookmanager.getReadingPosition());
		float fPercent = (float) bookmanager.getReadingPosition()
				/ (float) bookmanager.getBookSize();
		DecimalFormat df = new DecimalFormat("#0.0");
		String strPercent = df.format(fPercent * 100) + "%";
		history.updateHistoryPro(this.mBookName, strPercent);// bookmanager.getReadingContent()
		super.onStop();
	}

	private void setLookingBookView() {
		setContentView(bookmanager.getBookView());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// 按下键盘上返回按钮
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.putExtra("BookReading", bookmanager.getReadingPosition());
			setResult(RESULT_OK, intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");// 必须创建一项
		return super.onCreateOptionsMenu(menu);
	}

	public static int TURN_SETTING = 1;

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		startActivityForResult(new Intent(this, ReadingSetting.class),
				TURN_SETTING);
		return false;// 返回为true 则显示系统menu
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("songlog",""+resultCode+"onActivityresult"+requestCode);
		SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(this);
		String str = spf.getString("turn_page", "none");
		Log.i("songlog",""+resultCode+"onActivityresult"+requestCode+str);
		if (str.equals("none")) {
			bookView.setTurnAnimation(new NoTurnAnimation(this));
		}
		if (str.equals("real")) {
			bookView.setTurnAnimation(new SimulateTurnPage(this));
		}
	}

}