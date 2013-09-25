package com.alex.calendar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;

/**
 * Can be used to create a simple event to add to a user's calendar.
 * Launching an Intent to modify the calendar relies only on this
 * information.
 * @author alexpinkus
 */
public class Event {
	private static final long MILLIS_IN_AN_HOUR = 1000 * 60 * 60;
	private static final String 
			CALENDAR_EVENT_URI = "vnd.android.cursor.item/event",
			TITLE_TAG = "title",
			DESCRIPTION_TAG = "description",
			START_TIME_TAG = "beginTime",
			END_TIME_TAG = "endTime";

	/* package */ String mName, mDescription;
	/* package */ long mStart, mEnd;
	/* package */ Event() {
		
	}

  /**
   * Fires an Intent that will open the user's
   * Calendar to add in this Event.
   * @param context is the activity in which this
   * code is executing.
   */
	public void addToUserCalendar(final Context context) {
		final Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType(CALENDAR_EVENT_URI);
		intent.putExtra(TITLE_TAG, mName);
		intent.putExtra(DESCRIPTION_TAG, mDescription);
		intent.putExtra(START_TIME_TAG, mStart);
		intent.putExtra(END_TIME_TAG, mEnd);
		context.startActivity(intent);
	}

  /**
   * Returns a String in the following format:
   * [Title]
   * [Description]
   * Start time - end time
   *
   * The start and end times are formatted using the
   * default Locale and the String resource at
   * R.string.time_format.
   *
   * TODO: Wrap everything in a formatString() so that
   * we can use resources and it's localized.
   * @param context is the activity in which this
   * code is executing.
   * @throws IllegalArgumentException if the given
   * Context has no resource at R.string.time_format.
   */
	public String getUserFormattedString(final Context context) {
		final SimpleDateFormat df = new SimpleDateFormat(
				context.getString(R.string.time_format),
				Locale.getDefault());
		final Date start = new Date(mStart), end = new Date(mEnd);
		return mName + "\n" + mDescription + "\n"
				+ df.format(start) + " - " + df.format(end);
	}
	/**
	 * Returns a Builder that can be used to construct instances of Event.
	 * The Builder allows for a number of configurations and ensures
	 * that an event fits within necessary constraints.
	 * @return
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Can be used to create an instance of Event.
	 * @author alexpinkus
	 *
	 */
	public static class Builder {
		private Event mEvent = new Event();
		private long mDuration = -1;
		private Builder() {
			// Yeah that's right
		}

		/**
		 * @param name is the intended event title.
		 * @return the same Builder, for chaining.
		 */
		public Builder name(final String name) {
			mEvent.mName = name;
			return this;
		}

		/**
		 * @param description is a description of the event.
		 * @return the same Builder, for chaining.
		 */
		public Builder description(final String description) {
			mEvent.mDescription = description;
			return this;
		}

		/**
		 * @param startTime is the start time, in milliseconds since
		 * the epoch, for the event.
		 * @return the same Builder, for chaining.
		 */
		public Builder startTime(final long startTime) {
			mEvent.mStart = startTime;
			return this;
		}

		/**
		 * @param endTime is the end time, in milliseconds since the
		 * epoch, for the event.
		 * @return the same Builder, for chaining.
		 */
		public Builder endTime(final long endTime) {
			mEvent.mEnd = endTime;
			return this;
		}

		/**
		 * @param duration is the length, in milliseconds, the event
		 * lasts. This is an alternative to using endTime; if both
		 * are used, this takes precedence.
		 * @return the same Builder, for chaining.
		 * @throws IllegalArgumentException if duration is negative.
		 */
		public Builder duration(final long duration) {
			if (duration < 0)
				throw new IllegalArgumentException("Duration must be positive.");
			mDuration = duration;
			return this;
		}

		/**
		 * @param duration is the length, in hours, the event lasts.
		 * This is provided as a convenience method over duration(); if
		 * both are called, the one that is called later has precedence.
		 * @return the same Builder, for chaining.
		 * @throws IllegalArgumentException if duration is negative.
		 */
		public Builder hourLength(final float duration) {
			return duration(Math.round(MILLIS_IN_AN_HOUR * duration));
		}

		/**
		 * Concludes the building process and returns an object with
		 * the specified properties.
		 * @return the desired Event object.
		 * @throws IllegalStateException if name and description are
		 * not set before build() is called, or if the start time is
		 * after the end time.
		 */
		public Event build() {
			if (mDuration > 0)
				mEvent.mEnd = mEvent.mStart + mDuration;
			else if (mEvent.mEnd - mEvent.mStart < 0)
				// Using subtraction handles overflow issues.
				throw new IllegalStateException(
						"An event must have positive length.");

			if (mEvent.mName == null || mEvent.mDescription == null)
				throw new IllegalStateException(
						"Must set name and description before building.");

			return mEvent;
		}
	}
}
