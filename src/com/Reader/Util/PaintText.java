package com.Reader.Util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class PaintText {

	/**
	 * ��ȡ��ǰ�ֱ�����ָ����λ��Ӧ�����ش�С�������豸��Ϣ�� px,dip,sp -> px
	 * 
	 * Paint.setTextSize()��λΪpx
	 * 
	 * ����ժ�ԣ�TextView.setTextSize()
	 * 
	 * @param unit
	 *            TypedValue.COMPLEX_UNIT_*
	 * @param size
	 * @return
	 */
	public static float getRawSize(Context con, int unit, float size) {
		Context c = con;
		Resources r;

		if (c == null)
			r = Resources.getSystem();
		else
			r = c.getResources();

		return TypedValue.applyDimension(unit, size, r.getDisplayMetrics());
	}
}
