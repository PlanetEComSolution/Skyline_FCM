package planet.info.skyline.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.model.CreateDetails;


public class HomeCreateAdapter extends BaseAdapter {
    Context context;
    //SharedPreferences sp;
    //Editor ed;
    private Activity activity;
    private LayoutInflater inflater;
    private List<CreateDetails> creatmodel;

    public HomeCreateAdapter(Activity act, List<CreateDetails> cr) {
        this.activity = act;
        this.creatmodel = cr;
      //  sp = activity.getApplicationContext().getSharedPreferences("skyline", activity.getApplicationContext().MODE_PRIVATE);
       // ed = sp.edit();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return creatmodel.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return creatmodel.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final String index1 = String.valueOf(position + 1);
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)

            convertView = inflater.inflate(R.layout.row_missing_crates_new, null);
        final ViewHolder viewHolder = new ViewHolder();

        viewHolder.creid = (TextView) convertView.findViewById(R.id.createid);
        viewHolder.credesc = (TextView) convertView.findViewById(R.id.description);
        viewHolder.creloca = (TextView) convertView.findViewById(R.id.locationss);

        //   viewHolder.Miss = (ImageView) convertView.findViewById(R.id.missss);
        viewHolder.selectt = (CheckBox) convertView.findViewById(R.id.selects);
        viewHolder.index_no = (Button) convertView.findViewById(R.id.serial_no);

        viewHolder.creid.setTag(creatmodel.get(position));
        viewHolder.credesc.setTag(creatmodel.get(position));
        viewHolder.creloca.setTag(creatmodel.get(position));
        convertView.setTag(viewHolder);
        CreateDetails cc = creatmodel.get(position);
        viewHolder.creid.setText(cc.getUnique_crate_id());
        viewHolder.credesc.setText(cc.getDescription());
        viewHolder.index_no.setText(index1);
        String location = cc.getLocationcrate();
        if (location == null || location.equalsIgnoreCase("null") || location.equalsIgnoreCase("")) {
            viewHolder.creloca.setText("N/A");
        } else {
            viewHolder.creloca.setText(location);
        }



        viewHolder.selectt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });



        return convertView;

    }

    static class ViewHolder {
        protected TextView creid;
        protected TextView credesc;
        protected TextView creloca;
        protected CheckBox selectt;

        protected Button index_no;

    }


}

