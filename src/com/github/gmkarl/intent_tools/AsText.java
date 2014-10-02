package com.github.gmkarl.intent_tools;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class AsText extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
	
	Intent sendIntent = new Intent();
	sendIntent.setAction(Intent.ACTION_SEND);
	sendIntent.putExtra(Intent.EXTRA_TEXT, Util.getIntentText(this));
	sendIntent.setType("text/plain");
	startActivity(sendIntent);
    }
}
