package com.github.gmkarl.intent_tools;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Util
{
	public static String getIntentText(Activity activity) {
		Intent intent = activity.getIntent();
		Uri uri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
		if (uri != null) try {
			InputStream iStream = activity.getContentResolver().openInputStream(uri);
			BufferedReader reader = new BufferedReader(new InputStreamReader(iStream));
			StringBuilder stringBuilder = new StringBuilder();
			try { for (;;) {
				String line = reader.readLine();
				if (line == null) break;
				stringBuilder.append(line);
				stringBuilder.append('\n');
			} } catch (IOException e) { }
			return stringBuilder.toString();
		} catch (FileNotFoundException e) { }

		String text = intent.getStringExtra(Intent.EXTRA_TEXT);
		if (text != null) {
			return text;
		}
		return null;
	}
};
