package erick.bandeco.view;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.app.bandeco.R;

import java.util.ArrayList;

public class ListWrapper {

    private ArrayList<String> wordsList;
    private ArrayList<View> viewsList;
    private LayoutInflater inflater;
    private LinearLayout view;

    public ListWrapper(LinearLayout view, LayoutInflater inflater, ArrayList<String> list) {
        this.view = view;
        this.inflater = inflater;
        this.viewsList = new ArrayList<View>();

        this.wordsList = list;
        for (String ignored : wordsList) createListViewElement();
    }

    private void createListViewElement(){
        final View elementLayout = inflater.inflate(R.layout.list_element, view, false);

        final EditText editText = (EditText) elementLayout.findViewById(R.id.list_element_text);
        final ImageButton deleteButton = (ImageButton) elementLayout.findViewById(R.id.delete_button);

        viewsList.add(elementLayout);

        editText.setText(wordsList.get(viewsList.indexOf(elementLayout)));

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    wordsList.set(viewsList.indexOf(elementLayout), editText.getText().toString().trim());
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAt(viewsList.indexOf(elementLayout));
            }
        });

        view.addView(elementLayout);

        editText.requestFocus();
    }

    public void addNewElement(){
        if(wordsList.size() != 0 && wordsList.get(wordsList.size() - 1).equals(""))
            return;

        wordsList.add("");
        createListViewElement();
    }

    private void removeAt(int i) {
        view.clearFocus();
        if(wordsList.size() == 1 && wordsList.get(0).equals(""))
            return;

        view.removeView(viewsList.get(i));

        viewsList.remove(i);
        wordsList.remove(i);

        if(wordsList.isEmpty())
            addNewElement();
    }
}
