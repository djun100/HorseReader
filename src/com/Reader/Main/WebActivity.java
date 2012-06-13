/*
 * QQ:1127082711
 * 
 * xinlangweibo:http://weibo.com/muchenshou
 * 
 * email:muchenshou@gmail.com
 * */
package com.Reader.Main;

import java.net.URLEncoder;
import android.app.Activity;

import android.os.Bundle;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends Activity {

	WebView webView;

	final String mimeType = "text/html";
	final String encoding = "utf-8";

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weblayout);

		webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient(){
			public boolean shouldOverrideUrlLoading(WebView view, String url){
				view.loadUrl(url);
				return true;
			}
		});
		webHtml();
		// webImage();
		// localHtmlZh();
		// localHtmlBlankSpace();
		// localHtml();
		// localImage();
		//localHtmlImage();
	}

	/**
	 * 
	 * ֱ����ҳ��ʾ
	 */

	private void webHtml() {
		try {
			webView.loadUrl("http://www.baidu.com");
		} catch (Exception ex) {

			ex.printStackTrace();

		}

	}

	/**
	 * 
	 * ֱ������ͼƬ��ʾ
	 */

	private void webImage() {

		try {

			webView

			.loadUrl("http://www.gstatic.com/codesite/ph/images/code_small.png");

		} catch (Exception ex) {

			ex.printStackTrace();

		}

	}

	/**
	 * 
	 * ������ʾ
	 */

	private void localHtmlZh() {

		try {

			String data = "���Ժ��� ���ĵ�Html����";

			// utf-8���봦��(��SDK1.5ģ��������ʵ�豸�϶�����������,SDK1.6����������ʾ)

			// webView.loadData(data, mimeType, encoding);

			// �����ݽ��б��봦��(SDK1.5�汾)

			webView.loadData(URLEncoder.encode(data, encoding), mimeType,

			encoding);

		} catch (Exception ex) {

			ex.printStackTrace();

		}

	}

	/**
	 * 
	 * ������ʾ(�ո�Ĵ���)
	 */

	private void localHtmlBlankSpace() {

		try {

			String data = " ���Ժ��пո��Html���� ";

			// ���Կո�������

			webView.loadData(URLEncoder.encode(data, encoding), mimeType,

			encoding);

			// webView.loadData(data, mimeType, encoding);

			// �Կո�������(��SDK1.5�汾��)

			webView.loadData(
					URLEncoder.encode(data, encoding).replaceAll("+", " "),
					mimeType, encoding);

		} catch (Exception ex) {

			ex.printStackTrace();

		}

	}

	/**
	 * 
	 * ��ʾ����ͼƬ�ļ�
	 */

	private void localImage() {

		try {

			// �����ļ�����(����ļ������пո���Ҫ��+�����)

			webView.loadUrl("file:///android_asset/icon.png");

		} catch (Exception ex) {

			ex.printStackTrace();

		}

	}

	/**
	 * 
	 * ��ʾ������ҳ�ļ�
	 */

	private void localHtml() {

		try {

			// �����ļ�����(����ļ������пո���Ҫ��+�����)

			webView.loadUrl("file:///android_asset/test.html");

		} catch (Exception ex) {

			ex.printStackTrace();

		}

	}

	/**
	 * 
	 * ��ʾ����ͼƬ�����ֻ�ϵ�Html����
	 */

	private void localHtmlImage() {

		try {

			String data = "���Ա���ͼƬ�����ֻ����ʾ,����APK���ͼƬ";

			// SDK1.5�����ļ�����(������ʾͼƬ)

			// webView.loadData(URLEncoder.encode(data, encoding), mimeType,

			// encoding);

			// SDK1.6���Ժ�汾

			// webView.loadData(data, mimeType, encoding);

			// �����ļ�����(����ʾͼƬ)

			webView.loadDataWithBaseURL("about:blank", data, mimeType,

			encoding, "");

		} catch (Exception ex) {

			ex.printStackTrace();

		}

	}

}