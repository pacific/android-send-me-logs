# Android, send me logs! #
![http://android-send-me-logs.googlecode.com/svn/trunk/sendmelogs.demo/screenshots/forceclose.png](http://android-send-me-logs.googlecode.com/svn/trunk/sendmelogs.demo/screenshots/forceclose.png)

Device log collection and sending that is supposed to be a part of the Android API.
  * Collect and send logs and device information
  * Force close detection


Note: The host app needs to declare the following permission-use:
```
<uses-permission android:name="android.permission.READ_LOGS" />
```
# Usage #
  * [Download](http://code.google.com/p/android-send-me-logs/downloads/list) latest sendmelogs-xxx.jar
  * Place the jar in your project directory
  * Add the jar into build path
  * Follow the instructions below

## Instantiate LogCollector ##
```
LogCollector collector = new LogCollector(context);
```
## Send Logs ##
Programmatically collect phone information and device logs, and send it to a given email address.
```
			new AsyncTask<Void, Void, Boolean>() {
				@Override
				protected Boolean doInBackground(Void... params) {
					return mLogCollector.collect();
				}
				@Override
				protected void onPreExecute() {
					showDialog(DIALOG_PROGRESS_COLLECTING_LOG);
				}
				@Override
				protected void onPostExecute(Boolean result) {
					dismissDialog(DIALOG_PROGRESS_COLLECTING_LOG);
					if (result)
						mLogCollector.sendLog("someone@example.com", "Error Log", "Preface line 1\nPreface line 2");
					else
						showDialog(DIALOG_FAILED_TO_COLLECT_LOGS);
				}
				
			}.execute();
```

## Force Close Detection ##
Detect force-close of the host app that happened last time.  A force-close would be reported only once.
```
	class CheckForceCloseTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			return mLogCollector.hasForceCloseHappened();
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				showDialog(DIALOG_REPORT_FORCE_CLOSE);
			} else
				Toast.makeText(getApplicationContext(), "No force close detected.", Toast.LENGTH_LONG).show();
		}
	}
```

For more information, please check out the [sendmelogs.demo](http://android-send-me-logs.googlecode.com/svn/trunk/sendmelogs.demo/) app.

# Apps #
Are you using sendmelogs in your apps?  Please fill in [this form](https://spreadsheets.google.com/viewform?formkey=dGZSRUVJMGZJc2JZMUVhaWJlQW9ZRGc6MA) and I will list your apps here.

# Acknowledgment #
A portion of the code for this project is based on [Log Collector](http://code.google.com/p/android-log-collector/).