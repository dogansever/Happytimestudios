package com.sever.android.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * StickyHorizontalScrollView.java - iphonish horzontal scroll view
 * 
 * This View supports sticky positions, where list scrolling stops when swiping.
 * If the list is slowly moved without flinging, it automatically scrolls to the
 * nearest sticky position.
 * 
 * Usage: just like a normal HorizontalScrollView Use addStickyPosition() to set
 * positions the list snaps to when swiping.
 * 
 * @author Robert Becker | mail[at]rbecker.eu | http://www.rbecker.eu
 * 
 *         Feel free to use and modify as long as the @author information is not
 *         removed or altered. You can add a second for yourself, of course, if
 *         you modified anything!
 */
public class StickyHorizontalScrollView extends HorizontalScrollView {

	protected List<Integer> stickyPositions = new ArrayList<Integer>();

	/**
	 * constructor
	 * 
	 * @param context
	 */
	public StickyHorizontalScrollView(Context context) {
		super(context);
	}

	/**
	 * this is required to keep track of flinging actions and determine if a
	 * touch event caused a fling.
	 */
	private boolean flinged = false;

	/**
	 * intercept flinging and change it to always scroll to the next sticky
	 * position using smootScrollTo()
	 */
	@Override
	public void fling(int velocityX) {
		flinged = true;

		// for right-to-left scrolling
		Integer lastSmaller = 0;

		// find sticky position to scroll to
		for (Integer sticky : stickyPositions) {
			// check direction
			if (velocityX > 0 && sticky > getScrollX()) {
				smoothScrollTo(sticky, getScrollY());
				System.out.println("smoothScrollTo:" + sticky + ",getScrollX:" + getScrollX());
				break;
			} else if (velocityX < 0) {
				// scroll to nearest position left of the current one
				if (sticky > getScrollX()) {
					smoothScrollTo(lastSmaller, getScrollY());
					System.out.println("smoothScrollTo:" + lastSmaller + ",getScrollX:" + getScrollX());
					break;
				} else {
					lastSmaller = sticky;
				}
			}
		}
	}

	/**
	 * scrolls to the nearest sticky position
	 */
	public void sanitizeScrollPosition() {
		Integer minDistance = null;
		Integer minStickyPosition = null;
		// scroll to nearest sticky position
		for (Integer sticky : stickyPositions) {
			Integer distance = Math.abs(getScrollX() - sticky);
			if (minDistance == null || minDistance > distance) {
				minStickyPosition = sticky;
				minDistance = distance;
			}
		}
		if (getScrollX() != 0) {
			smoothScrollTo(minStickyPosition, getScrollY());
			System.out.println("smoothScrollTo:" + minStickyPosition + ",getScrollX:" + getScrollX());
		}
	}

	/**
	 * Intercept onTouchEvents to avoid the list hanging between sticky
	 * positions when gesture did not lead to a fling.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		flinged = false; // will be set to true by fling() method

		boolean res = super.onTouchEvent(ev);

		// check if gesture has ended and flinging was not started
		// this means, that the list was manually scrolled
		if (ev.getAction() == MotionEvent.ACTION_UP && !flinged) {
			// make sure, list is in a sticky position
			sanitizeScrollPosition();
		}

		return res;

	}

	/**
	 * add sticky position at the given x coordinate
	 * 
	 * @param x
	 */
	public void addStickyPosition(Integer x) {
		stickyPositions.add(x);
		Collections.sort(stickyPositions);
	}

	/**
	 * remove all sticky positions
	 */
	public void clearStickyPositions() {
		stickyPositions.clear();
	}
}
