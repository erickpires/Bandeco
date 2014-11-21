package erick.bandeco.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.app.bandeco.Constants;
import com.app.bandeco.R;
import com.app.bandeco.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class About extends ActionBarActivity {

    private static final Uri myTwitter = Uri.parse("https://twitter.com/ericktpires");
    private static final Uri designerDribbble = Uri.parse("https://dribbble.com/aPronsky");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.about_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        View parentLayout = findViewById(R.id.parent_layout_about);
        Utils.changeStatusColor(this, parentLayout);

        //TODO This should not be here in the final version
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constants.SITE_URL == Constants.SITE_URL_OFICIAL)
                    Constants.SITE_URL = Constants.SITE_URL_BACKUP;
                else
                    Constants.SITE_URL = Constants.SITE_URL_OFICIAL;
            }
        });

        TextView textViewVersionName = (TextView) findViewById(R.id.textVersionName);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
            textViewVersionName.setText(pInfo.versionName);
        } catch (PackageManager.NameNotFoundException ignored) {

        }

        TextView textViewAuthor = (TextView) findViewById(R.id.textViewAuthor);

        textViewAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openUrl = new Intent(Intent.ACTION_VIEW, myTwitter);
                startActivity(openUrl);
            }
        });

        TextView textViewIconDesigner = (TextView) findViewById(R.id.textViewIconDesigner);

        textViewIconDesigner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openUrl = new Intent(Intent.ACTION_VIEW, designerDribbble);
                startActivity(openUrl);
            }
        });

        TextView textViewOpenSourceLicenses = (TextView) findViewById(R.id.textViewopenSourceLicenses);

        textViewOpenSourceLicenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(About.this);
                LayoutInflater inflater = getLayoutInflater();
                WebView openSourceLicensesWebView = (WebView) inflater.inflate(R.layout.open_source_view, null);
                String licensesData = Utils.readAssetAndClose(getAssets(), "licenses.html");

                openSourceLicensesWebView.loadDataWithBaseURL(Constants.ASSETS_FOLDER, licensesData, "text/html", "utf-8", null);
                builder.setTitle(getString(R.string.open_source_licenses));
                builder.setView(openSourceLicensesWebView);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}
