package org.focuser.sendmelogs.test;

import org.focuser.sendmelogs.LogCollector;

import android.content.Context;
import android.test.AndroidTestCase;

public class LogCollectorTest extends AndroidTestCase {

	public void test_should_always_successfully_collect_logs() {
		Context context = getContext();
		LogCollector collector = new LogCollector(context);
		assertTrue(collector.collect());
	}
}
