package com.github.gmkarl.intent_tools;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.HashMap;

public class Socket extends Activity
{
    class IntentSocket implements Runnable {
	private Intent i;
	private ServerSocket ss;
	private java.net.Socket s;
	private InputStream is;
	private OutputStream os;
	private byte[] buffer;

	IntentSocket() throws IOException
	{
		i = Socket.this.getIntent();
		ss = new ServerSocket();
		ss.bind(null);
	}
	private void accept(byte[] bytes)
	{
		buffer = bytes;
		new Thread(this).start();
	}
	private int getPort()
	{
		return ss.getLocalPort();
	}
	public void write(byte[] bytes) throws IOException
	{
		synchronized(ss) {
			os.write(bytes);
		}
	}
	public boolean writeable()
	{
		synchronized(ss) {
			return !s.isOutputShutdown();
		}
	}
	@Override
	public void run()
	{
		//Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

		try {
			synchronized(ss) {
				s = ss.accept();
				is = s.getInputStream();
				os = s.getOutputStream();

				if (buffer.length > 0)
					os.write(buffer);
				else
					buffer = new byte[64];
			}

			while (!s.isInputShutdown()) {
				int total = 1;
				is.read(buffer, 0, total); Thread.yield();
				try {
					for (int len; is.available() != 0; total += len) {
						len = is.available();
						if (total + len > buffer.length)
							buffer = Arrays.copyOf(buffer, (total + len) * 2);
						is.read(buffer, total, len); Thread.yield();
					}
				} finally {
					i.putExtra(Intent.EXTRA_TEXT, new String(buffer, 0, total));
					startActivity(i);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(Socket.this, "IOException: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
    }
    static private HashMap<Intent.FilterComparison,IntentSocket> sockets = new HashMap<Intent.FilterComparison,IntentSocket>();


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

	Intent.FilterComparison key = new Intent.FilterComparison(getIntent());
	IntentSocket is = Socket.sockets.get(key);
	try { if (is == null) {
		is = new IntentSocket();
		Socket.sockets.put(key, is);
		Toast.makeText(this, "Opening on port " + is.getPort(), Toast.LENGTH_SHORT).show();
		is.accept(Util.getIntentText(this).getBytes());
	} else if (!is.writeable()) {
		Toast.makeText(this, "Reopening on port " + is.getPort(), Toast.LENGTH_SHORT).show();
		is.accept(Util.getIntentText(this).getBytes());
	} else {
		is.write(Util.getIntentText(this).getBytes());
	} } catch (IOException e) {
		e.printStackTrace();
		Toast.makeText(Socket.this, "IOException: " + e.getMessage(), Toast.LENGTH_SHORT).show();
	}
    }
}
