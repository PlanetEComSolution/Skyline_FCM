package planet.info.skyline.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import planet.info.skyline.network.Api;
import planet.info.skyline.network.SOAP_API_Client;
import planet.info.skyline.tech.fullscreenview.FullscreenImageView;
import planet.info.skyline.R;
import planet.info.skyline.tech.whats_inside.ShowWhatsInside_sub;

import static planet.info.skyline.network.SOAP_API_Client.URL_EP1;


public class Adapter_Graphics_WhatsInside extends BaseAdapter {
    List<HashMap<String, String>> beanArrayList;
    Context context;
    //  int count = 1;
    // String img_url;

    public Adapter_Graphics_WhatsInside(Context context, List<HashMap<String, String>> beanArrayList) {
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
        final String name = beanArrayList.get(i).get("grap_name");
        final String desc = beanArrayList.get(i).get("description");
        final String qnty_in_this_crate = beanArrayList.get(i).get("cnt2");

        final String type = beanArrayList.get(i).get("TYPE");
        final String configure_id = beanArrayList.get(i).get("configure_id");
        final String gtype = beanArrayList.get(i).get("gtype");
        final String img_url = beanArrayList.get(i).get("ImageBig");
        final String Total_Quantity = beanArrayList.get(i).get("mquantity");
        final String mainAcc = beanArrayList.get(i).get("main_acc");

        if (convertview == null) {
            holder = new Holder();

            convertview = LayoutInflater.from(context).inflate(R.layout.row_graphics_new, null);

            holder.index_no = (Button) convertview.findViewById(R.id.serial_no);
            holder.name = (TextView) convertview.findViewById(R.id.name);
            holder.desc = (TextView) convertview.findViewById(R.id.desc);
            holder.count = (TextView) convertview.findViewById(R.id.count);

            holder.txtvw_name = (TextView) convertview.findViewById(R.id.txtvw_name);
            holder.type = (TextView) convertview.findViewById(R.id.type);
            holder.view_file = (Button) convertview.findViewById(R.id.view_file);
            holder.qnty_packed_in_this_crate = (TextView) convertview.findViewById(R.id.qnty_packed_in_this_crate);

            holder.btn_explore = (ImageView) convertview.findViewById(R.id.btn_explore);
            holder.btn_explore.setVisibility(View.GONE);
            holder.count_text = (TextView) convertview.findViewById(R.id.count_text);

            convertview.setTag(holder);
        } else {
            holder = (Holder) convertview.getTag();
        }
        holder.index_no.setText(index1);


        if (img_url == null || img_url.trim().equalsIgnoreCase("null") || img_url.trim().equals("")) {
            holder.view_file.setVisibility(View.GONE);
        } else {
            holder.view_file.setVisibility(View.VISIBLE);
        }


        if (type.equals("null")||type.equals(null) || type.trim().equals("")) {
            holder.txtvw_name.setText("Exhibit/Accessory ");
        } else {
            holder.txtvw_name.setText(type.trim() + "      ");
        }

        if (configure_id.equals("null")|| configure_id.equals(null) || configure_id.trim().equals("")) {
            holder.name.setText("Not available");
        } else {

            String config[] = configure_id.split("-");
            String config_id = config[1] + " " + config[2];

            holder.name.setText(config_id);
        }
//
        if (name.equals("null")||name.equals(null) || gtype.equals(null)) {
            holder.type.setText("Not available");
        } else {
            holder.type.setText(name.trim() + " " + gtype.trim());

        }
        //
        if (desc.equals("null")||desc.equals(null) || desc.trim().equals("") || desc.equals("null")) {
            holder.desc.setText("Not available");
        } else {
            String descr = desc.replaceAll(Pattern.quote("&nbsp;"), " ");
            holder.desc.setText(descr.trim());
        }


        //nks


        if(type.equals("null")||type.equals(null) || type.trim().equals("")){
            holder.count_text .setText("Quantity for this Exhibit/Accessory ");
        }
        else
        {
            holder.count_text .setText("Quantity for this "+type+" ");
        }


        if (Total_Quantity.equals("null")||Total_Quantity.equals(null) || Total_Quantity.trim().equals("")) {
            holder.count.setText("Not available");
        } else {
            holder.count.setText(Total_Quantity.trim());
        }

        if (qnty_in_this_crate.equals("null")||qnty_in_this_crate.equals(null) || qnty_in_this_crate.trim().equals("")) {
            holder.qnty_packed_in_this_crate.setText("Not available");
        } else {
            holder.qnty_packed_in_this_crate.setText(qnty_in_this_crate.trim());
        }

        int qnty_pcked_in_this_crate = 0;
        int total_qnty = 0;
        try {
            qnty_pcked_in_this_crate = Integer.parseInt(qnty_in_this_crate);
            total_qnty = Integer.parseInt(Total_Quantity);
        } catch (Exception e) {
            e.getMessage();
        }
        if(total_qnty>qnty_pcked_in_this_crate)
        {
            holder.btn_explore.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.btn_explore.setVisibility(View.INVISIBLE);
        }


        holder.btn_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="";
                if(type.equalsIgnoreCase("Exhibit"))
                {
                    url=URL_EP1+ Api.API_FETCH_EXHIBIT+mainAcc;

                }
                else if(type.equalsIgnoreCase("Accessory"))
                {
                    url=URL_EP1+Api.API_FETCH_ACCESSORY+mainAcc;
                }

                Intent i=new Intent(context, ShowWhatsInside_sub.class);
                i.putExtra("url",url);
                context.startActivity(i);

            }
        });


        holder.view_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_url == null || img_url.equalsIgnoreCase("null")|| img_url.trim().equals("")) {
                    Toast.makeText(context, "Image not found!", Toast.LENGTH_SHORT).show();
                } else {

                    String url = img_url;
                    url = url.replaceAll(Pattern.quote(".."), "");
                    url = SOAP_API_Client.URL_EP1 + "/admin" + url;
                    Intent i = new Intent(context, FullscreenImageView.class);
                    i.putExtra("url", url);
                    context.startActivity(i);
                }
            }
        });

        //nks

        return convertview;
    }


    class Holder {
        TextView name, desc, count, qnty_packed_in_this_crate;

        TextView txtvw_name,count_text;
        TextView type;
        Button index_no, view_file;
        ImageView btn_explore;

    }


}