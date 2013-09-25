package com.alex.calendar;
import org.junit.Test;

import android.test.InstrumentationTestCase;


public class EventTests extends InstrumentationTestCase {
	private static final String TEST_NAME = "MAD Hackathon",
			TEST_DESCRIPTION = "A bunch of people are getting " +
					"together and coding shit";
	private static final long MILLIS_IN_AN_HOUR = 1000 * 60 * 60;
	private static final long TEST_START = System.currentTimeMillis(),
			TEST_END = MILLIS_IN_AN_HOUR + TEST_START;

	@Test
	public void testGoodConstruction_StartEnd() {
		final Event testEvent = Event.builder()
				.name(TEST_NAME)
				.description(TEST_DESCRIPTION)
				.startTime(TEST_START)
				.endTime(TEST_END)
				.build();
		assertEquals(TEST_NAME, testEvent.mName);
		assertEquals(TEST_DESCRIPTION, testEvent.mDescription);
		assertEquals(TEST_START, testEvent.mStart);
		assertEquals(TEST_END, testEvent.mEnd);
	}

	@Test
	public void testGoodConstruction_StartDuration() {
		final Event testEvent = Event.builder()
				.name(TEST_NAME)
				.description(TEST_DESCRIPTION)
				.startTime(TEST_START)
				.duration(MILLIS_IN_AN_HOUR)
				.build();
		assertEquals(TEST_NAME, testEvent.mName);
		assertEquals(TEST_DESCRIPTION, testEvent.mDescription);
		assertEquals(TEST_START, testEvent.mStart);
		assertEquals(TEST_END, testEvent.mEnd);
	}

	@Test
	public void testGoodConstruction_StartHourLength() {
		final Event testEvent = Event.builder()
				.name(TEST_NAME)
				.description(TEST_DESCRIPTION)
				.startTime(TEST_START)
				.hourLength(1)
				.build();
		assertEquals(TEST_NAME, testEvent.mName);
		assertEquals(TEST_DESCRIPTION, testEvent.mDescription);
		assertEquals(TEST_START, testEvent.mStart);
		assertEquals(TEST_END, testEvent.mEnd);
	}

	@Test
	public void testEndBeforeStart() {
		final Event.Builder testBuilder = Event.builder()
				.name(TEST_NAME)
				.description(TEST_DESCRIPTION)
				.startTime(TEST_START)
				.endTime(TEST_START - MILLIS_IN_AN_HOUR);
		try {
			testBuilder.build();
			fail("Event must not be able to have negative length");
		} catch (final IllegalStateException e) {
			assertEquals(e.getMessage(), "An event must have positive length.");
		}
	}

	@Test
	public void testEndBeforeStart_Overflow() {
		final Event.Builder testBuilder = Event.builder()
				.name(TEST_NAME)
				.description(TEST_DESCRIPTION)
				.startTime(Long.MIN_VALUE + MILLIS_IN_AN_HOUR)
				.endTime(Long.MAX_VALUE - MILLIS_IN_AN_HOUR);
		try {
			testBuilder.build();
			fail("Event must not be able to have negative length");
		} catch (final IllegalStateException e) {
			assertEquals(e.getMessage(), "An event must have positive length.");
		}
	}

	@Test
	public void testEndNotBeforeStart_Overflow() {
		final Event testEvent = Event.builder()
				.name(TEST_NAME)
				.description(TEST_DESCRIPTION)
				.startTime(Long.MAX_VALUE - MILLIS_IN_AN_HOUR)
				.endTime(Long.MIN_VALUE + MILLIS_IN_AN_HOUR)
				.build();
		assertEquals(TEST_NAME, testEvent.mName);
		assertEquals(TEST_DESCRIPTION, testEvent.mDescription);
		assertEquals(Long.MAX_VALUE - MILLIS_IN_AN_HOUR, testEvent.mStart);
		assertEquals(Long.MIN_VALUE + MILLIS_IN_AN_HOUR, testEvent.mEnd);
	}

	@Test
	public void testNegativeDuration() {
		final Event.Builder testBuilder = Event.builder()
				.name(TEST_NAME)
				.description(TEST_DESCRIPTION)
				.startTime(TEST_START);
		try {
			testBuilder.duration(-MILLIS_IN_AN_HOUR);
			fail("Event must not be able to have negative duration");
		} catch (final IllegalArgumentException e) {
			assertEquals("Duration must be positive.", e.getMessage());
		}
	}

	@Test
	public void testNegativeHourLength() {
		final Event.Builder testBuilder = Event.builder()
				.name(TEST_NAME)
				.description(TEST_DESCRIPTION)
				.startTime(TEST_START);
		try {
			testBuilder.hourLength(-1);
			fail("Event must not be able to have negative duration");
		} catch (final IllegalArgumentException e) {
			assertEquals("Duration must be positive.", e.getMessage());
		}
	}

	@Test
	public void testNoName() {
		final Event.Builder testBuilder = Event.builder()
				.description(TEST_DESCRIPTION)
				.startTime(TEST_START)
				.duration(MILLIS_IN_AN_HOUR);
		try {
			testBuilder.build();
			fail("Event must have a name");
		} catch (final IllegalStateException e) {
			assertEquals("Must set name and description before building.", e.getMessage());
		}
	}

	@Test
	public void testNoDescription() {
		final Event.Builder testBuilder = Event.builder()
				.name(TEST_NAME)
				.startTime(TEST_START)
				.duration(MILLIS_IN_AN_HOUR);
		try {
			testBuilder.build();
			fail("Event must have a description");
		} catch (final IllegalStateException e) {
			assertEquals("Must set name and description before building.", e.getMessage());
		}
	}

	@Test
	public void testNoNameOrDescription() {
		final Event.Builder testBuilder = Event.builder()
				.startTime(TEST_START)
				.duration(MILLIS_IN_AN_HOUR);
		try {
			testBuilder.build();
			fail("Event must have a name and description");
		} catch (final IllegalStateException e) {
			assertEquals("Must set name and description before building.", e.getMessage());
		}
	}
}
