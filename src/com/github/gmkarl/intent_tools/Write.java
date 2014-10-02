package com.github.gmkarl.intent_tools;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class Write extends Activity
{
    private String text;
    private Uri uri;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
	
	text = Util.getIntentText(this);

	Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
	intent.setData(uri);
	intent.setType("*/*");
	startActivityForResult(intent, 0);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData)
    {
	if (resultCode != Activity.RESULT_OK)
		return;
	if (resultData == null)
		return;

	uri = resultData.getData();
	File file = new File(uri.getPath());
	uri = Uri.fromFile(file);
	FileWriter fw = null;
	try {
		fw = new FileWriter(file, true);
		fw.write(text);
	}
	catch (FileNotFoundException fnfe) {
		Toast.makeText(this, "File not found " + uri.getPath(), Toast.LENGTH_SHORT).show();
	}
	catch (IOException ioe) {
		Toast.makeText(this, "IOException", Toast.LENGTH_SHORT).show();
	}
	if (fw != null) {
		try {
			fw.close();
		} catch (IOException ioe) { }
	}
    }
}
