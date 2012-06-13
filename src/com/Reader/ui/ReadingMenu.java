/*
 * QQ:1127082711
 * 
 * xinlangweibo:http://weibo.com/muchenshou
 * 
 * email:muchenshou@gmail.com
 * */
package com.Reader.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemSelectedListener;

import com.Reader.Command.CommandNextChapter;
import com.Reader.Command.CommandPreChapter;
import com.Reader.Main.R;
import com.Reader.Main.ReadingActivity;

public class ReadingMenu {

	private boolean isMore = false;// menu�˵���ҳ����
	PopupWindow menuDialog;// menu�˵�Dialog
	GridView menuGrid;
	View menuView;
	ProgressAlert mTextSizeProress;
	private final int PREPAGE = 0;
	private final int NEXTPAGE = 1;
	private final int TEXTSIZE = 2;
	private final int SETTINGS = 3;

	/** �˵�ͼƬ **/
	int[] menu_image_array = { R.drawable.controlbar_backward_enable,
			R.drawable.controlbar_forward_enable,
			R.drawable.menu_input_pick_inputmethod, R.drawable.menu_syssettings };
	/** �˵����� **/
	String[] menu_name_array = { "��һҳ", "��һҳ", "�����С", "����" };

	Context mContext;

	public ReadingMenu(Context con) {
		this.mContext = con;
	}

	public void Create() {
		menuView = View.inflate(mContext, R.layout.reading_gridview_menu, null);
		// ����AlertDialog

		menuGrid = (GridView) menuView.findViewById(R.id.gridview);
		menuGrid.setAdapter(getMenuAdapter(menu_name_array, menu_image_array));
		/** ����menuѡ�� **/
		menuGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.i("[Reading]", "onitemlistener " + arg2);
				switch (arg2) {
				case PREPAGE://
					new CommandPreChapter((ReadingActivity)ReadingMenu.this.mContext).excute();
				case NEXTPAGE://
					new CommandNextChapter((ReadingActivity)ReadingMenu.this.mContext).excute();
					break;
				case TEXTSIZE://
					mTextSizeProress = new ProgressAlert(mContext);
					mTextSizeProress.showAtLocation(
							((ReadingActivity) mContext).bookView,
							Gravity.CENTER, 0, 0);
					break;
				case SETTINGS://
					break;
				}
			}
		});
	}

	public void show(View pa) {
		menuDialog = new PopupWindow(mContext);// .Builder(this).create();
		menuDialog.setContentView(menuView);
		menuDialog.setFocusable(true);
		menuDialog.setWidth(LayoutParams.FILL_PARENT);
		menuDialog.setHeight(LayoutParams.WRAP_CONTENT);
		menuDialog.showAtLocation(pa, Gravity.FILL_HORIZONTAL | Gravity.BOTTOM,
				0, 0);

	}

	private SimpleAdapter getMenuAdapter(String[] menuNameArray,
			int[] imageResourceArray) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < menuNameArray.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", imageResourceArray[i]);
			map.put("itemText", menuNameArray[i]);
			data.add(map);
		}
		SimpleAdapter simperAdapter = new SimpleAdapter(this.mContext, data,
				R.layout.reading_item_menu, new String[] { "itemImage",
						"itemText" }, new int[] { R.id.item_image,
						R.id.item_text });
		return simperAdapter;
	}

}