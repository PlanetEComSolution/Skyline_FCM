package planet.info.skyline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import planet.info.skyline.R;

/**
 * Created by Admin on 7/7/2017.
 */

public class JobNameAdapter extends ArrayAdapter<HashMap<String,String>> {
    Context context;
    int  textViewResourceId;
    List<HashMap<String,String>> items, tempItems, suggestions;

    public JobNameAdapter(Context context, int textViewResourceId, List<HashMap<String,String>> items) {
        super(context,  textViewResourceId, items);
        this.context = context;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<>(items); // this makes the difference.
        suggestions = new ArrayList<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_textview, parent, false);
        }
        String people = items.get(position).get("jobName");
        if (people != null) {
            TextView lblName = (TextView) view.findViewById(R.id.textView);
            if (lblName != null)
                lblName.setText(people);
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            HashMap<String,String> str = ((HashMap<String,String>) resultValue);
            return str.get("jobName");
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (HashMap<String,String> people : tempItems) {
                   /* if (people.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }*/

                    if (people.get("jobName").toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            try {
                List<HashMap<String, String>> filterList = (ArrayList<HashMap<String, String>>) results.values;
                if (results != null && results.count > 0) {
                    clear();
                    for (HashMap<String, String> people : filterList) {
                        add(people);
                        notifyDataSetChanged();
                    }
                }
            }
            catch (Exception e)
            {
                e.getMessage();
            }


        }
    };
}
