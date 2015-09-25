package erick.bandeco.controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.app.bandeco.R;

import java.util.ArrayList;

import static android.content.DialogInterface.OnDismissListener;

public class ListDialogFragment extends DialogFragment {

	private OnDismissListener onDismissListener;
	private LinearLayout listLinearLayout;
	private ListWrapper wrapper;

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);

		ArrayList<String> list = (ArrayList<String>) getArguments().getSerializable("list");

		LayoutInflater inflater = getActivity().getLayoutInflater();

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		final View layout = inflater.inflate(R.layout.words_list_layout, null);

		listLinearLayout = (LinearLayout) layout.findViewById(R.id.list);

		wrapper = new ListWrapper(listLinearLayout, inflater, list);

		final Button button = (Button) layout.findViewById(R.id.new_element_button);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				layout.clearFocus();
				wrapper.addNewElement();
			}
		});

		builder.setView(layout);

		Dialog result = builder.create();

		result.setOnDismissListener(onDismissListener);
		setRetainInstance(true);

		return result;
	}

	@Override
	public void onDestroyView() {
		if (getDialog() != null && getRetainInstance())
			getDialog().setOnDismissListener(null);
		super.onDestroyView();
	}

	public void setOnDismissListener(OnDismissListener onDismissListener) {
		this.onDismissListener = onDismissListener;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		if (listLinearLayout != null && onDismissListener != null) {
			listLinearLayout.clearFocus();
			onDismissListener.onDismiss(dialog);
		}
		super.onDismiss(dialog);
	}
}
