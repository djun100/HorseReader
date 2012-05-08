package com.Reader.Book.Manager;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.Reader.Main.R;
import com.Reader.Main.ReadingActivity;
import com.Reader.Book.Book;
import com.Reader.Book.BookFactory;
import com.Reader.Book.BookView.BookView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class BookManager implements View.OnKeyListener, View.OnTouchListener {

	public static final int OPENBOOK = 7;
	public static final int BUFSIZE = 512;
	private Book book;
	private BookView bookView;
	private TextUtil textUtil;
	public ReadingActivity bookActivity;
	// private PageWidget mPageWidget;
	
	int bufferlocal = -1;// �ı����λ��
	int BUFLEN = 4 * 1024;

	int position = 0;

	public BookManager(Context con, File file) throws IOException {
		bookActivity = (ReadingActivity) con;
		book = BookFactory.createBook(file);
		bookView = new BookView(con);
		bookView.setFocusable(true);
		bookView.setOnKeyListener(this);
		bookView.setOnTouchListener(this);

		textUtil = new TextUtil(bookView, book);
		bookView.setTextUtil(textUtil);

	}

	public BookView getBookView() {
		return bookView;
	}

	public void openBook() throws IOException {
		book.openBook();
		textUtil.InitText();
		this.textUtil.setBgBitmap(BitmapFactory.decodeResource(
				bookActivity.getResources(), R.drawable.bg));
		
	}

	public void closeBook() {
		book.closeBook();
	}

	public int getBookSize() {
		return book.size();
	}

	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
				&& event.getAction() == KeyEvent.ACTION_UP) {
			;
			this.textUtil.nextPage();
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP
				&& event.getAction() == KeyEvent.ACTION_UP) {

		}
		return false;
	}

	public boolean onTouch(View v, MotionEvent event) {
		/*
		 * Rect rect = new Rect(0, 0, v.getWidth(), v.getHeight());
		 * 
		 * if (event.getX() < rect.exactCenterX() + 40 && event.getX() >
		 * rect.exactCenterX() - 40 && event.getY() > rect.exactCenterY() - 20
		 * && event.getY() < rect.exactCenterY() + 20) { // gridview
		 * bookActivity.setBookSet(); return false; }
		 * 
		 * if (event.getY() > rect.exactCenterY()) { this.textUtil.nextPage(); }
		 * else { this.textUtil.prePage(); } this.bookView.postInvalidate();
		 * return false;
		 */

		// TODO Auto-generated method stub
		boolean ret = false;
		try {
			if (v == bookView) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					bookView.abortAnimation();
					bookView.calcCornerXY(event.getX(), event.getY());

					textUtil.DrawText(bookView.mCurPageCanvas);
					if (bookView.DragToRight()) {
						this.textUtil.nextPage();

						textUtil.DrawText(bookView.mNextPageCanvas);

					} else {
						this.textUtil.nextPage();

						textUtil.DrawText(bookView.mNextPageCanvas);

					}
					bookView.setBitmaps(bookView.mCurPageBitmap, bookView.mNextPageBitmap);
				}

				ret = bookView.doTouchEvent(event);
				return ret;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public Book getBook() {
		return book;
	}

	public int getReadingPosition() {
		return this.textUtil.getCurLocal();
	}

}
