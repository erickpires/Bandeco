package erick.bandeco.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.app.bandeco.Constants;
import com.app.bandeco.R;
import com.app.bandeco.Utils;

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
				if (Constants.SITE_URL == Constants.SITE_URL_OFICIAL)
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
				(new LicensesDialogFragment()).show(getSupportFragmentManager(), "licenses");
			}
		});
	}

	private class LicensesDialogFragment extends DialogFragment {
		@NonNull
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			setRetainInstance(true);
			AlertDialog.Builder builder = new AlertDialog.Builder(About.this);
			LayoutInflater inflater = getActivity().getLayoutInflater();

			View rootView =  inflater.inflate(R.layout.open_source_view, null);
			WebView openSourceLicensesWebView = (WebView) rootView.findViewById(R.id.licenses_web_view);
			String licensesData = Utils.readAssetAndClose(getAssets(), "licenses.html");

			View backButton = rootView.findViewById(R.id.back_button);
			backButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					LicensesDialogFragment.this.dismiss();
				}
			});

			openSourceLicensesWebView.loadDataWithBaseURL(Constants.ASSETS_FOLDER, licensesData, "text/html", "utf-8", null);
			builder.setView(rootView);

			return builder.create();
		}

		@Override
		public void onDestroyView() {
			if (getDialog() != null && getRetainInstance())
				getDialog().setOnDismissListener(null);
			super.onDestroyView();
		}
	}
}
