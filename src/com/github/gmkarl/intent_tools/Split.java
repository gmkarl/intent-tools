package com.github.gmkarl.intent_tools;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;

public class Split extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
	
	Intent intent = getIntent();
	String action = intent.getAction();
	String type = intent.getType();

	if (Intent.ACTION_SEND.equals(action) && type != null) {
		if ("text/plain".equals(type)) {
			String text = intent.getStringExtra(Intent.EXTRA_TEXT);
			int length = 858;
			int offset = text.length() - (text.length() % length);
			/* * 15: 17 * 17: 32 * 19: 53 * 21: 78 * 23: 106 * 25: 134
 * 27: 154 * 29: 192 * 31: 230 * 33: 271 * 35: 321 * 37: 367 * 39: 425 * 41: 458 * 43: 520
 * 45: 586 * 47: 644 * 49: 718 * 51: 792 * 53: 858 * 55: 929 * 57: 1003 * 59: 1091 * 61: 1171 * 63: 1273
 * 65: 1367 * 67: 1465 * 69: 1528 * 71: 1628 * 73: 1732 * 75: 1840 * 77: 1952 */
			if (offset < text.length())
				send(text.substring(offset) );
			while ( (offset-=length) >= 0 )
				send(text.substring(offset, offset + length));
		}
	}
    }
	private void send(String text)
	{
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, text);
		sendIntent.setType("text/plain");
		startActivity(sendIntent);
	}
}
