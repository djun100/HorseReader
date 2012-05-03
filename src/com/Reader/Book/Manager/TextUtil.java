package com.Reader.Book.Manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.Reader.Book.Book;
import com.Reader.Book.BookView.BookView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.util.Log;

class StringUtils {

	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
}

public class TextUtil {
	private float mTextWidth = 240;// ���ƿ��
	private float mTextHeight = 240;// ���Ƹ߶�
	private int mFontColor = 0;// ������ɫ
	private int mAlpha = 0;// Alphaֵ

	private int mTextSize = 0;// �����С
	private int mPadding = 5;

	private Paint mPaint = null;
	private BookReading bookreading;
	private BookView mBookView;
	public TextUtil(BookView bookView, Book book) {
		mBookView = bookView;
		mPaint = new Paint();
		bookreading = new BookReading(book);
		this.mTextWidth = 240;
		this.mTextHeight = 320;
		this.mFontColor = Color.BLACK;
		this.mAlpha = 0;
		this.mTextSize = 22;
		this.mPadding = 2;
		mPaint.setARGB(this.mAlpha, Color.red(this.mFontColor), Color
				.green(this.mFontColor), Color.blue(this.mFontColor));
		mPaint.setTextSize(this.mTextSize);
		mPaint.setColor(this.mFontColor);
		mPaint.setAntiAlias(true);

		bookreading.mPaint = mPaint;
		bookreading.pageline = this.getLineNum() - 1;
		bookreading.pageWidth = this.mTextWidth;
	}

	public void setSize(int w, int h) {
		this.mTextWidth = w;
		this.mTextHeight = h;
		// update();
	}

	public void setTextSize(int size) {
		this.mTextSize = size;
		// update();
	}

	public void InitText() throws IOException {
		// �Ի������Ե�����
		this.pageString = bookreading.getPageStr(0);
		for (int i=0; i<pageString.size(); i++){
			Log.i("text", pageString.get(i));
		}
	}

	public int getLineHeight() {
		FontMetrics fm = mPaint.getFontMetrics();// �õ�ϵͳĬ����������
		return (int) (Math.ceil(fm.descent - fm.top) + mPadding);
	}

	public int getLineNum() {
		return (int) (mTextHeight / getLineHeight());// ����и߸߶�
	}

	private List<String> pageString = new ArrayList<String>();

	/**
	 * �����ַ���
	 * 
	 * @param canvas
	 * @throws IOException
	 */
	public void DrawText(Canvas canvas) throws IOException {
		Log.i("text", ""+pageString.size());
		for (int j = 0; j < pageString.size(); j++) {
			
			canvas.drawText(pageString.get(j), 0,
					(this.getLineHeight() + mPadding) * j + 30, mPaint);

		}
	}

	public int getCurLocal() {
		return bookreading.getCurLocal();
	}

	public void nextLine() {
		pageString = bookreading.nextLine();
	}

	public void nextPage() {
		pageString = bookreading.nextPage();
		this.mBookView.postInvalidate();
	}

	public void preLine() {
		pageString = bookreading.preLine();
	}

	public void prePage() {
		pageString = bookreading.prePage();
	}

	public void setLocal(int local) {
		if (local < 0)
			return;
		pageString = bookreading.getPageStr(local);
	}
}
