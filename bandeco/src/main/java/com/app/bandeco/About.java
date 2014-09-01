package com.app.bandeco;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class About extends ActionBarActivity {

    private static final Uri uri = Uri.parse("https://twitter.com/ericktpires");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView textViewVersionName = (TextView) findViewById(R.id.textVersionName);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
            textViewVersionName.setText(pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {

        }

        TextView textViewAuthor = (TextView) findViewById(R.id.textViewAuthor);

        textViewAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openUrl = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(openUrl);
            }
        });
    }
}
