package com.github.gmkarl.intent_tools.stream2text;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Stream2Text extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
	
	Intent intent = getIntent();
	String action = intent.getAction();
	Uri uri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);

	if (Intent.ACTION_SEND.equals(action) && uri != null) {

		InputStream iStream;
		try {
			iStream = getContentResolver().openInputStream(uri);
		} catch (FileNotFoundException e) {
			return;
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(iStream));

		StringBuilder stringBuilder = new StringBuilder();

		try { for (;;) {
			String line = reader.readLine();
			if (line == null) break;
			stringBuilder.append(line);
			stringBuilder.append('\n');
		} } catch (IOException e) { }

		String text = intent.getStringExtra(Intent.EXTRA_TEXT);
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());
		sendIntent.setType("text/plain");
		startActivity(sendIntent);
	}
    }
}
