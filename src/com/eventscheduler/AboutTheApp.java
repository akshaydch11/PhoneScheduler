package com.eventscheduler;



import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class AboutTheApp extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_the_app);
		WebView wv1 = (WebView) findViewById(R.id.webView);
		wv1.loadUrl("file:///android_asset/aboutApp.html");
	}

	
}
