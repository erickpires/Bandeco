package erick.bandeco.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.app.bandeco.R;
import com.nhaarman.listviewanimations.util.Swappable;

import org.w3c.dom.Text;

import erick.bandeco.view.Settings;

/**
 * Created by erick on 17/11/14.
 */
public class MenuEntriesListAdapter extends BaseAdapter implements Swappable {
    Context context;
    String[] entries;

    public MenuEntriesListAdapter(Context context) {
        this.context = context;
        entries = context.getResources().getStringArray(R.array.menu_entries);
    }

    @Override
    public int getCount() {
        return entries.length;
    }

    @Override
    public Object getItem(int position) {
        return entries[position];
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.menu_entry_element, null);
        }

        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.menu_entry_checkbox);

        checkBox.setText(entries[position]);

        return convertView;
    }

    @Override
    public boolean hasStableIds(){
        return true;
    }

    @Override
    public void swapItems(int positionOne, int positionTwo) {
        String str1 = entries[positionOne];
        entries[positionOne] = entries[positionTwo];
        entries[positionTwo] = str1;
    }
}
