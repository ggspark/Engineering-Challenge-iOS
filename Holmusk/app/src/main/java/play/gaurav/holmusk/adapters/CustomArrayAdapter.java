package play.gaurav.holmusk.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gaurav Gupta <gaurav@thegauravgupta.com>
 * @since 27/May/2015
 */
public class CustomArrayAdapter extends ArrayAdapter<String> {

    List<String> items;

    public CustomArrayAdapter(Context context, int id) {
        super(context, id);
        items = new ArrayList<String>();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults result = new FilterResults();
                result.values = items;
                result.count = items.size();
                return result;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public void clear() {
        super.clear();
        items.clear();
    }

    @Override
    public void add(String object) {
        super.add(object);
        items.add(object);
    }
}
