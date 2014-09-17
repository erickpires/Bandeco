package erick.bandeco.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.app.bandeco.R;

import java.util.ArrayList;

import static android.content.DialogInterface.*;

/**
 * Created by erick on 9/3/14.
 */
public class ListDialogFragment extends DialogFragment {

    private ArrayList<String> list;
    private OnDismissListener onDismissListener;
    private LinearLayout listLinearLayout;

    public ListDialogFragment(ArrayList<String> list){
        super();
        
        this.list = list;
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View layout = inflater.inflate(R.layout.list_layout, null);

        listLinearLayout = (LinearLayout) layout.findViewById(R.id.list);

        final ListWrapper wrapper = new ListWrapper(listLinearLayout, inflater, this.list);

        final Button button = (Button) layout.findViewById(R.id.new_element_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.clearFocus();
                wrapper.addNewElement();
            }
        });

        button.setBackgroundColor(layout.getDrawingCacheBackgroundColor());

        builder.setView(layout);

        Dialog result = builder.create();

        result.setOnDismissListener(onDismissListener);

        return result;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener){
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog){
        listLinearLayout.clearFocus();
        onDismissListener.onDismiss(dialog);
        super.onDismiss(dialog);
    }
}
