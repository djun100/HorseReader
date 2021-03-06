package com.reader.ui;

import java.util.ArrayList;
import java.util.List;

import com.reader.app.R;

import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AdobeView extends FrameLayout implements
		ViewTreeObserver.OnTouchModeChangeListener {

	private static final String TAG = "AdobeView";
	private static final boolean DEBUG = true;
	private boolean mHide = true;

	public void switchView() {
		mHide = !mHide;
		Log.i(TAG, mHide ? "true" : "false");
		this.requestLayout();
		View child, child2;
		TranslateAnimation animation1, animation2;
		if (!mHide) {
			child = this.getChildAt(1);
			animation1 = new TranslateAnimation((0.0f - child.getWidth()),
					0.0f, 0.0f, 0.0f);
			animation1.setDuration(300);
			child2 = this.getChildAt(2);

			animation2 = new TranslateAnimation((0.0f - child.getWidth()),
					0.0f, 0.0f, 0.0f);
			animation2.setInterpolator(new AccelerateInterpolator());
			animation2.setDuration(500);
			child2.startAnimation(animation2);

		} else {
			child = this.getChildAt(1);
			child2 = this.getChildAt(2);
			animation2 = new TranslateAnimation(0.0f + child.getWidth(), 0.0f,
					0.0f, 0.0f);
			animation2.setDuration(300);
			child2.startAnimation(animation2);
		}

		// this.layout(mLeft, mTop, mRight, mBottom);
	}

	private static final int TABWIDGET_LOCATION_LEFT = 0;
	private static final int TABWIDGET_LOCATION_TOP = 1;
	private static final int TABWIDGET_LOCATION_RIGHT = 2;
	private static final int TABWIDGET_LOCATION_BOTTOM = 3;
	private ListView mTabWidget;
	private FrameLayout mTabContent;
	private List<TabSpec> mTabSpecs = new ArrayList<TabSpec>(2);
	/**
	 * This field should be made private, so it is hidden from the SDK. {@hide
	 * 
	 * }
	 */
	protected int mCurrentTab = -1;
	private View mCurrentView = null;
	/**
	 * This field should be made private, so it is hidden from the SDK. {@hide
	 * 
	 * }
	 */
	protected LocalActivityManager mLocalActivityManager = null;
	private OnTabChangeListener mOnTabChangeListener;
	private OnKeyListener mTabKeyListener;

	private int mTabLayoutId;

	public AdobeView(Context context) {
		super(context);
		initTabHost();
	}

	public AdobeView(Context context, AttributeSet attrs) {
		super(context, attrs);

		mTabLayoutId = 0;

		if (mTabLayoutId == 0) {
			// In case the tabWidgetStyle does not inherit from Widget.TabWidget
			// and tabLayout is
			// not defined.
			throw new IllegalArgumentException("��Ҫָ��tabid");
		}

		initTabHost();
	}

	private void initTabHost() {
		setFocusableInTouchMode(true);
		setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);

		mCurrentTab = -1;
		mCurrentView = null;
	}

	/**
	 * Get a new {@link TabSpec} associated with this tab host.
	 * 
	 * @param tag
	 *            required tag of tab.
	 */
	public TabSpec newTabSpec(String tag) {
		return new TabSpec(tag);
	}

	@Override
	public void sendAccessibilityEvent(int eventType) {
		/* avoid super class behavior - TabWidget sends the right events */
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		final ViewTreeObserver treeObserver = getViewTreeObserver();
		treeObserver.addOnTouchModeChangeListener(this);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		final ViewTreeObserver treeObserver = getViewTreeObserver();
		treeObserver.removeOnTouchModeChangeListener(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onTouchModeChanged(boolean isInTouchMode) {
		if (!isInTouchMode) {
			// leaving touch mode.. if nothing has focus, let's give it to
			// the indicator of the current tab

		}
	}

	/**
	 * Add a tab.
	 * 
	 * @param tabSpec
	 *            Specifies how to create the indicator and content.
	 */
	public void addTab(TabSpec tabSpec) {

		if (tabSpec.mIndicatorStrategy == null) {
			throw new IllegalArgumentException(
					"you must specify a way to create the tab indicator.");
		}

		if (tabSpec.mContentStrategy == null) {
			throw new IllegalArgumentException(
					"you must specify a way to create the tab content");
		}
		View tabIndicator = tabSpec.mIndicatorStrategy.createIndicatorView();
		tabIndicator.setOnKeyListener(mTabKeyListener);

		// If this is a custom view, then do not draw the bottom strips for
		// the tab indicators.
		if (tabSpec.mIndicatorStrategy instanceof ViewIndicatorStrategy) {
			// mTabWidget.setStripEnabled(false);
		}

		mTabWidget.addView(tabIndicator);
		mTabSpecs.add(tabSpec);

		if (mCurrentTab == -1) {
			setCurrentTab(0);
		}
	}

	/**
	 * Removes all tabs from the tab widget associated with this tab host.
	 */
	public void clearAllTabs() {
		mTabWidget.removeAllViews();
		initTabHost();
		mTabContent.removeAllViews();
		mTabSpecs.clear();
		requestLayout();
		invalidate();
	}

	public int getCurrentTab() {
		return mCurrentTab;
	}

	public String getCurrentTabTag() {
		if (mCurrentTab >= 0 && mCurrentTab < mTabSpecs.size()) {
			return mTabSpecs.get(mCurrentTab).getTag();
		}
		return null;
	}

	public View getCurrentView() {
		return mCurrentView;
	}

	public void setCurrentTabByTag(String tag) {
		int i;
		for (i = 0; i < mTabSpecs.size(); i++) {
			if (mTabSpecs.get(i).getTag().equals(tag)) {
				setCurrentTab(i);
				break;
			}
		}
	}

	/**
	 * Get the FrameLayout which holds tab content
	 */
	public FrameLayout getTabContentView() {
		return mTabContent;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		final boolean handled = super.dispatchKeyEvent(event);

		// unhandled key events change focus to tab indicator for embedded
		// activities when there is nothing that will take focus from default
		// focus searching
		if (!handled && (event.getAction() == KeyEvent.ACTION_DOWN)
				&& (mCurrentView != null) && /* (mCurrentView.isRootNamespace()) */
				(mCurrentView.hasFocus())) {
			int keyCodeShouldChangeFocus = KeyEvent.KEYCODE_DPAD_UP;
			int directionShouldChangeFocus = View.FOCUS_UP;
			int soundEffect = SoundEffectConstants.NAVIGATION_UP;

		}
		return handled;
	}

	@Override
	public void dispatchWindowFocusChanged(boolean hasFocus) {
		if (mCurrentView != null) {
			mCurrentView.dispatchWindowFocusChanged(hasFocus);
		}
	}

	public void setCurrentTab(int index) {
		if (index < 0 || index >= mTabSpecs.size()) {
			return;
		}

		if (index == mCurrentTab) {
			return;
		}

		// notify old tab content
		if (mCurrentTab != -1) {
			mTabSpecs.get(mCurrentTab).mContentStrategy.tabClosed();
		}

		mCurrentTab = index;
		final TabSpec spec = mTabSpecs.get(index);

		// tab content
		mCurrentView = spec.mContentStrategy.getContentView();

		if (mCurrentView.getParent() == null) {
			mTabContent.addView(mCurrentView, new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT));
		}

		if (!mTabWidget.hasFocus()) {
			// if the tab widget didn't take focus (likely because we're in
			// touch mode)
			// give the current tab content view a shot
			mCurrentView.requestFocus();
		}

		// mTabContent.requestFocus(View.FOCUS_FORWARD);
		invokeOnTabChangeListener();
	}

	/**
	 * Register a callback to be invoked when the selected state of any of the
	 * items in this list changes
	 * 
	 * @param l
	 *            The callback that will run
	 */
	public void setOnTabChangedListener(OnTabChangeListener l) {
		mOnTabChangeListener = l;
	}

	private void invokeOnTabChangeListener() {
		if (mOnTabChangeListener != null) {
			mOnTabChangeListener.onTabChanged(getCurrentTabTag());
		}
	}

	/**
	 * Interface definition for a callback to be invoked when tab changed
	 */
	public interface OnTabChangeListener {
		void onTabChanged(String tabId);
	}

	/**
	 * Makes the content of a tab when it is selected. Use this if your tab
	 * content needs to be created on demand, i.e. you are not showing an
	 * existing view or starting an activity.
	 */
	public interface TabContentFactory {
		/**
		 * Callback to make the tab contents
		 * 
		 * @param tag
		 *            Which tab was selected.
		 * @return The view to display the contents of the selected tab.
		 */
		View createTabContent(String tag);
	}

	/**
	 * A tab has a tab indicator, content, and a tag that is used to keep track
	 * of it. This builder helps choose among these options.
	 * 
	 * For the tab indicator, your choices are: 1) set a label 2) set a label
	 * and an icon
	 * 
	 * For the tab content, your choices are: 1) the id of a {@link View} 2) a
	 * {@link TabContentFactory} that creates the {@link View} content. 3) an
	 * {@link Intent} that launches an {@link android.app.Activity}.
	 */
	public class TabSpec {

		private String mTag;

		private IndicatorStrategy mIndicatorStrategy;
		private ContentStrategy mContentStrategy;

		private TabSpec(String tag) {
			mTag = tag;
		}

		/**
		 * Specify a label as the tab indicator.
		 */
		public TabSpec setIndicator(CharSequence label) {
			mIndicatorStrategy = new LabelIndicatorStrategy(label);
			return this;
		}

		/**
		 * Specify a label and icon as the tab indicator.
		 */
		public TabSpec setIndicator(CharSequence label, Drawable icon) {
			mIndicatorStrategy = new LabelAndIconIndicatorStrategy(label, icon);
			return this;
		}

		/**
		 * Specify a view as the tab indicator.
		 */
		public TabSpec setIndicator(View view) {
			mIndicatorStrategy = new ViewIndicatorStrategy(view);
			return this;
		}

		/**
		 * Specify the id of the view that should be used as the content of the
		 * tab.
		 */
		public TabSpec setContent(int viewId) {
			mContentStrategy = new ViewIdContentStrategy(viewId);
			return this;
		}

		/**
		 * Specify a {@link android.widget.TabHost.TabContentFactory} to use to
		 * create the content of the tab.
		 */
		public TabSpec setContent(TabContentFactory contentFactory) {
			mContentStrategy = new FactoryContentStrategy(mTag, contentFactory);
			return this;
		}

		/**
		 * Specify an intent to use to launch an activity as the tab content.
		 */
		public TabSpec setContent(Intent intent) {
			mContentStrategy = new IntentContentStrategy(mTag, intent);
			return this;
		}

		public String getTag() {
			return mTag;
		}
	}

	/**
	 * Specifies what you do to create a tab indicator.
	 */
	private static interface IndicatorStrategy {

		/**
		 * Return the view for the indicator.
		 */
		View createIndicatorView();
	}

	/**
	 * Specifies what you do to manage the tab content.
	 */
	private static interface ContentStrategy {

		/**
		 * Return the content view. The view should may be cached locally.
		 */
		View getContentView();

		/**
		 * Perhaps do something when the tab associated with this content has
		 * been closed (i.e make it invisible, or remove it).
		 */
		void tabClosed();
	}

	/**
	 * How to create a tab indicator that just has a label.
	 */
	private class LabelIndicatorStrategy implements IndicatorStrategy {

		private final CharSequence mLabel;

		private LabelIndicatorStrategy(CharSequence label) {
			mLabel = label;
		}

		@Override
		public View createIndicatorView() {
			final Context context = getContext();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View tabIndicator = inflater.inflate(mTabLayoutId, mTabWidget, // tab
																			// widget
																			// is
																			// the
																			// parent
					false); // no inflate params

			final TextView tv = (TextView) tabIndicator
					.findViewById(R.id.title);
			tv.setText(mLabel);

			if (context.getApplicationInfo().targetSdkVersion <= Build.VERSION_CODES.DONUT) {
				// Donut apps get old color scheme
				// tabIndicator.setBackgroundResource(R.drawable.tab_indicator_v4);
				// tv.setTextColor(context.getResources().getColorStateList(
				// R.color.tab_indicator_text_v4));
			}

			return tabIndicator;
		}
	}

	/**
	 * How we create a tab indicator that has a label and an icon
	 */
	private class LabelAndIconIndicatorStrategy implements IndicatorStrategy {

		private final CharSequence mLabel;
		private final Drawable mIcon;

		private LabelAndIconIndicatorStrategy(CharSequence label, Drawable icon) {
			mLabel = label;
			mIcon = icon;
		}

		@Override
		public View createIndicatorView() {
			final Context context = getContext();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View tabIndicator = inflater.inflate(mTabLayoutId, mTabWidget, // tab
																			// widget
																			// is
																			// the
																			// parent
					false); // no inflate params

			final TextView tv = (TextView) tabIndicator
					.findViewById(R.id.title);
			final ImageView iconView = (ImageView) tabIndicator
					.findViewById(R.id.icon);

			// when icon is gone by default, we're in exclusive mode
			final boolean exclusive = iconView.getVisibility() == View.GONE;
			final boolean bindIcon = !exclusive || TextUtils.isEmpty(mLabel);

			tv.setText(mLabel);

			if (bindIcon && mIcon != null) {
				iconView.setImageDrawable(mIcon);
				iconView.setVisibility(VISIBLE);
			}

			if (context.getApplicationInfo().targetSdkVersion <= Build.VERSION_CODES.DONUT) {
				// Donut apps get old color scheme
				// tabIndicator.setBackgroundResource(R.drawable.tab_indicator_v4);
				// tv.setTextColor(context.getResources().getColorStateList(
				// R.color.tab_indicator_text_v4));
			}

			return tabIndicator;
		}
	}

	/**
	 * How to create a tab indicator by specifying a view.
	 */
	private class ViewIndicatorStrategy implements IndicatorStrategy {

		private final View mView;

		private ViewIndicatorStrategy(View view) {
			mView = view;
		}

		@Override
		public View createIndicatorView() {
			return mView;
		}
	}

	/**
	 * How to create the tab content via a view id.
	 */
	private class ViewIdContentStrategy implements ContentStrategy {

		private final View mView;

		private ViewIdContentStrategy(int viewId) {
			mView = mTabContent.findViewById(viewId);
			if (mView != null) {
				mView.setVisibility(View.GONE);
			} else {
				throw new RuntimeException(
						"Could not create tab content because "
								+ "could not find view with id " + viewId);
			}
		}

		@Override
		public View getContentView() {
			mView.setVisibility(View.VISIBLE);
			return mView;
		}

		@Override
		public void tabClosed() {
			mView.setVisibility(View.GONE);
		}
	}

	/**
	 * How tab content is managed using {@link TabContentFactory}.
	 */
	private class FactoryContentStrategy implements ContentStrategy {
		private View mTabContent;
		private final CharSequence mTag;
		private TabContentFactory mFactory;

		public FactoryContentStrategy(CharSequence tag,
				TabContentFactory factory) {
			mTag = tag;
			mFactory = factory;
		}

		@Override
		public View getContentView() {
			if (mTabContent == null) {
				mTabContent = mFactory.createTabContent(mTag.toString());
			}
			mTabContent.setVisibility(View.VISIBLE);
			return mTabContent;
		}

		@Override
		public void tabClosed() {
			mTabContent.setVisibility(View.GONE);
		}
	}

	/**
	 * How tab content is managed via an {@link Intent}: the content view is the
	 * decorview of the launched activity.
	 */
	private class IntentContentStrategy implements ContentStrategy {

		private final String mTag;
		private final Intent mIntent;

		private View mLaunchedView;

		private IntentContentStrategy(String tag, Intent intent) {
			mTag = tag;
			mIntent = intent;
		}

		@Override
		public View getContentView() {
			if (mLocalActivityManager == null) {
				throw new IllegalStateException(
						"Did you forget to call 'public void setup(LocalActivityManager activityGroup)'?");
			}
			final Window w = mLocalActivityManager.startActivity(mTag, mIntent);
			final View wd = w != null ? w.getDecorView() : null;
			if (mLaunchedView != wd && mLaunchedView != null) {
				if (mLaunchedView.getParent() != null) {
					mTabContent.removeView(mLaunchedView);
				}
			}
			mLaunchedView = wd;

			// XXX Set FOCUS_AFTER_DESCENDANTS on embedded activities for now so
			// they can get
			// focus if none of their children have it. They need focus to be
			// able to
			// display menu items.
			//
			// Replace this with something better when Bug 628886 is fixed...
			//
			if (mLaunchedView != null) {
				mLaunchedView.setVisibility(View.VISIBLE);
				mLaunchedView.setFocusableInTouchMode(true);
				((ViewGroup) mLaunchedView)
						.setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
			}
			return mLaunchedView;
		}

		@Override
		public void tabClosed() {
			if (mLaunchedView != null) {
				mLaunchedView.setVisibility(View.GONE);
			}
		}
	}
}
