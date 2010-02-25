/*
 * Copyright (C) 2010 The SendMeLogs for Android project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.focuser.sendmelogs.demo;


import org.focuser.sendmelogs.LogCollector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class DemoActivity extends Activity implements OnClickListener, android.content.DialogInterface.OnClickListener {
    public static final int DIALOG_SEND_LOG = 345350;
    protected static final int DIALOG_PROGRESS_COLLECTING_LOG = 3255;
    protected static final int DIALOG_FAILED_TO_COLLECT_LOGS = 3535122;
	private static final int DIALOG_REPORT_FORCE_CLOSE = 3535788;

    private View mBtnCrashMe;
	private LogCollector mLogCollector;
	private View mBtnSendLog;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mBtnCrashMe = findViewById(R.id.crash_me);
        mBtnCrashMe.setOnClickListener(this);
        
        mBtnSendLog = findViewById(R.id.send_logs);
        mBtnSendLog.setOnClickListener(this);
        
        mLogCollector = new LogCollector(this);
        
        CheckForceCloseTask task = new CheckForceCloseTask();
        task.execute();
    }

	private void throwException() {
		throw new NullPointerException();
	}

	public void onClick(View v) {
		if (v==mBtnCrashMe)
			throwException();
		else if (v==mBtnSendLog) {
			showDialog(DIALOG_SEND_LOG);
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case DIALOG_SEND_LOG:
		case DIALOG_REPORT_FORCE_CLOSE:
			Builder builder = new AlertDialog.Builder(this);
			String message;
			if (id==DIALOG_SEND_LOG)
				message = "Do you want to send me your logs?";
			else 
				message = "It appears this app has been force-closed, do you want to report it to me?";
			builder.setTitle("Warning")
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setMessage(message)
			.setPositiveButton("Yes", this)
			.setNegativeButton("No", this);
			dialog = builder.create();
			break;
		case DIALOG_PROGRESS_COLLECTING_LOG:
			ProgressDialog pd = new ProgressDialog(this);
			pd.setTitle("Progress");
			pd.setMessage("Collecting logs...");
			pd.setIndeterminate(true);
			dialog = pd;			
			break;
		case DIALOG_FAILED_TO_COLLECT_LOGS:
			builder = new AlertDialog.Builder(this);
			builder.setTitle("Error")
			.setMessage("Failed to collect logs.")
			.setNegativeButton("OK", null);
			dialog = builder.create();
		}
		return dialog;
	}
	
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

	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE:
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
						mLogCollector.sendLog("lintonye@gmail.com", "Error Log", "Preface\nPreface line 2");
					else
						showDialog(DIALOG_FAILED_TO_COLLECT_LOGS);
				}
				
			}.execute();
		}
		dialog.dismiss();
	}
}