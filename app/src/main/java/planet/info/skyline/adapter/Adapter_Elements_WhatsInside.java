package planet.info.skyline.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import planet.info.skyline.network.Api;
import planet.info.skyline.network.SOAP_API_Client;
import planet.info.skyline.tech.fullscreenview.FullscreenImageView;
import planet.info.skyline.R;
import planet.info.skyline.tech.whats_inside.ShowWhatsInside_sub;

import static planet.info.skyline.network.SOAP_API_Client.URL_EP1;


public class Adapter_Elements_WhatsInside extends BaseAdapter {
    List<HashMap<String, String>> beanArrayList;
    Context context;
    int count = 1;


    public Adapter_Elements_WhatsInside(Context context, List<HashMap<String, String>> beanArrayList) {
        this.context = context;
        this.beanArrayList = beanArrayList;

    }

    @Override
    public int getCount() {
        return beanArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertview, ViewGroup viewGroup) {

        final Holder holder;
        final String index1 = String.valueOf(i + 1);
        final String name = beanArrayList.get(i).get("ename");
        final String desc = beanArrayList.get(i).get("descr");
        final String quantity = beanArrayList.get(i).get("quan");
        final String typeName = beanArrayList.get(i).get("typename");
        final String count = beanArrayList.get(i).get("cnt");
        final String mainAcc = beanArrayList.get(i).get("main_acc");//
        final String img_url = beanArrayList.get(i).get("ufile");

        if (convertview == null) {
            holder = new Holder();

            convertview = LayoutInflater.from(context).inflate(R.layout.row_elements_new, null);


            holder.index_no = (Button) convertview.findViewById(R.id.serial_no);
            holder.name = (TextView) convertview.findViewById(R.id.name);
            holder.desc = (TextView) convertview.findViewById(R.id.desc);
            holder.quantity = (TextView) convertview.findViewById(R.id.quantity);
            holder.quantity_text = (TextView) convertview.findViewById(R.id.quantity_text);
            holder.count = (TextView) convertview.findViewById(R.id.count);//
            holder.btn_explore = (ImageView) convertview.findViewById(R.id.btn_explore);
            holder.btn_explore.setVisibility(View.GONE);
            holder.view_file = (Button) convertview.findViewById(R.id.view_file);




            convertview.setTag(holder);
        } else {
            holder = (Holder) convertview.getTag();
        }


        //Start data filling

        if (img_url == null || img_url.trim().equalsIgnoreCase("null") || img_url.trim().equals("")) {
            holder.view_file.setVisibility(View.GONE);
        } else {
            holder.view_file.setVisibility(View.VISIBLE);
        }


        try {
            int quantity_ = Integer.parseInt(quantity);
            int count_ = Integer.parseInt(count);
            if (quantity_ > count_) {
                holder.btn_explore.setVisibility(View.VISIBLE);
            } else {
                holder.btn_explore.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            e.getMessage();
        }


        holder.index_no.setText(index1);

        if (name.equals(null) || name.trim().equals("")) {
            holder.name.setText("Not available");
        } else {
            holder.name.setText(name.trim());
        }

        if (desc.equals(null) || desc.trim().equals("")) {
            holder.desc.setText("Not available");
        } else {
            String descr = desc.replaceAll(Pattern.quote("&nbsp;"), " ");
            holder.desc.setText(descr.trim());
        }


        //nks

        if (typeName.equals(null) || quantity.trim().equals("")) {
            holder.quantity_text.setText("Quantity for this Exhibit/Accessory ");
        } else {
            holder.quantity_text.setText("Quantity for this " + typeName + " ");
        }

        //nks

        if (quantity.equals(null) || quantity.trim().equals("")) {
            holder.quantity.setText("Not available");
        } else {
            holder.quantity.setText(quantity.trim());
        }

        if (count.equals(null) || count.trim().equals("")) {
            holder.count.setText("Not available");
        } else {
            holder.count.setText(count);
        }

        holder.btn_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "";
                if (typeName.equalsIgnoreCase("Exhibit")) {
                 //   url = URL_EP1 + "/element_other_crate.php?id=" + mainAcc;
                    url = URL_EP1 + Api.API_ELEMENT_OTHER_CRATE + mainAcc;

                } else if (typeName.equalsIgnoreCase("Accessory")) {
                   // url = URL_EP1 + "/element_other_crate_acc.php?id=" + mainAcc;
                    url = URL_EP1 + Api.API_ELEMENT_OTHER_CRATE_ACC + mainAcc;

                }
                Intent i = new Intent(context, ShowWhatsInside_sub.class);
                i.putExtra("url", url);
                context.startActivity(i);

            }
        });


        holder.view_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = img_url;
                url = url.replaceAll(Pattern.quote(".."), "");
                url = SOAP_API_Client.URL_EP1 +Api.API_ELEMENT_PATH + url;

                Intent i = new Intent(context, FullscreenImageView.class);
                i.putExtra("url", url);
                context.startActivity(i);
            }
        });


        return convertview;
    }


    class Holder {

        TextView name, desc, quantity, count, quantity_text;

        //nks
        Button index_no, view_file;
        ImageView btn_explore;
        LinearLayout row_jobFile;

        //nks
    }


}