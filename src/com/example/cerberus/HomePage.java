package com.example.cerberus;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class HomePage extends Activity {

	private static final int REQUEST_CODE = 1234;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);

		Button speakButton = (Button) findViewById(R.id.speakButton);

		// disable button if no recognition service is present
		PackageManager pm = getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (activities.size() == 0) {
			speakButton.setEnabled(false);
			speakButton.setText("Recognizer not present");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Handle the action of the button being clicked
	 */
	public void speakButtonClicked(View v) {
		startVoiceRecognitionActivity();
	}

	/**
	 * fire an intent to start the voice recognition activity
	 */
	public void startVoiceRecognitionActivity() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
				"I hope someone is writing this down :/");
		startActivityForResult(intent, REQUEST_CODE);
	}

	/**
	 * Handle the results from the voice recognition activity
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (/* requestCode == REQUEST_CODE && */resultCode == RESULT_OK) {
			// get the values the recognition engine thought it heard
			ArrayList<String> matches = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

			Log.i("speech", "match: " + matches.get(0).toString());

			if (matches.size() > 0) {
				Log.i("command", "creating command for: " + matches.get(0));
				new CreateCommandClass().execute(matches.get(0));
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
