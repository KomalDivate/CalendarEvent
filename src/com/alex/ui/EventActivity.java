package com.alex.ui;

import java.util.ArrayList;
import java.util.List;

import com.alex.calendar.Event;
import com.alex.calendar.R;

import android.os.Bundle;
import android.app.ListActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

public class EventActivity extends ListActivity {
	private static final long MILLIS_IN_AN_HOUR = 1000 * 60 * 60;
	private static final int NUM_EVENTS = 20;
	private static final String[] ITEMS = {
		"Alex",
		"Luke",
		"Andrew",
		"Cameron",
		"Jessica",
	};

	private List<Event> mEvents = createEvents();
	private ListAdapter mAdapter;
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        // Create a progress bar to display while the list loads
        final ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        progressBar.setIndeterminate(true);
        getListView().setEmptyView(progressBar);

        // Must add the progress bar to the root of the layout
        final ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        root.addView(progressBar);

        mAdapter = new ArrayAdapter<Event>(this,
    			R.layout.list_item, R.id.item_text, mEvents){
    		public View getView(final int position,
    				final View convertView, final ViewGroup parent) {
    			final Object event = (Event) mAdapter.getItem(position);
    			final View v = super.getView(position, convertView, parent);
    			((Button) v.findViewById(R.id.button1)).setOnClickListener(
    					new LaunchEventListener(event));
    			if (event instanceof Event) {
    				((TextView) v.findViewById(R.id.item_text)).setText(
    						((Event) event).getUserFormattedString(EventActivity.this));
    			}
    			return v;
    		}
    	};
        setListAdapter(mAdapter);

	}

	private List<Event> createEvents() {
		long timeRoundedDown = System.currentTimeMillis() / MILLIS_IN_AN_HOUR
				* MILLIS_IN_AN_HOUR;
		final ArrayList<Event> events = new ArrayList<Event>();
		for (int hours = 0; hours < NUM_EVENTS; hours++)
			events.add(Event.builder()
				.name(ITEMS[hours % ITEMS.length] + "'s office hours")
				.description("Come to them")
				.startTime(timeRoundedDown + 2 * MILLIS_IN_AN_HOUR * hours)
				.hourLength(2)
				.build());
		return events;
	}

	/* package */ class LaunchEventListener implements View.OnClickListener {
		private final Event mEvent;

		public LaunchEventListener(final Object item) {
			if (item instanceof Event) {
				mEvent = (Event) item;
			} else mEvent = null;
		}

		public void onClick(final View arg0) {
			if (mEvent != null)
				mEvent.addToUserCalendar(EventActivity.this);
		}
		
	}
}
