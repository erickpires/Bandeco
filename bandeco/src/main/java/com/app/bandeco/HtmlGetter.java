package com.app.bandeco;

import html.Html;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class HtmlGetter extends AsyncTask<String, Void, Html> {
	
	private Exception exception = null;
	private ProgressDialog dialog;
	private Context context;
	
	public HtmlGetter(Context context) {
		this.context = context;
	}
	
	@Override
    protected void onPreExecute()
    {
        dialog = ProgressDialog.show(context, null, "Downloading page...");
    }
	
	@Override
	protected Html doInBackground(String... params) {
		Html result = null;
		
		try {
			URL url = new URL(params[0]);
			
			URLConnection connection = url.openConnection();
			//InputStreamReader isr = new InputStreamReader(connection.getInputStream());
			
			result = new Html(connection);
			
		} catch (Exception e) {
			exception = e;
		}
		return result;
	}
	
	 protected void onPostExecute(Html html) {
	        // TODO: check this.exception 
	        // TODO: do something with the html
		 	dialog.dismiss();
	    }

}
