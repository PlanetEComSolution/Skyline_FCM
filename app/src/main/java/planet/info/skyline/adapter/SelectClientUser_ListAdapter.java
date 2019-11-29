package planet.info.skyline.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import planet.info.skyline.R;
import planet.info.skyline.client.ClientHomeActivity;
import planet.info.skyline.model.ClientModel;
import planet.info.skyline.util.Utility;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by agent on 12/22/16.
 */

public class SelectClientUser_ListAdapter extends BaseAdapter {
    Activity activity;

    ArrayList<ClientModel> list_ClientUser;
    SharedPreferences sp;

    public SelectClientUser_ListAdapter(Activity activity, ArrayList<ClientModel> list_ClientUser) {
        // TODO Auto-generated constructor stub
        this.activity = activity;


        this.list_ClientUser = list_ClientUser;
        sp = activity.getSharedPreferences("skyline", MODE_PRIVATE);

        //      this.icon=icon;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list_ClientUser.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        try {
            final ViewHolder holder;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.client_user_item, null);

                holder = new ViewHolder();
                holder.ename = (TextView) convertView.findViewById(R.id.ename);
                holder.Category = (TextView) convertView.findViewById(R.id.Category);
                holder.CompanyName = (TextView) convertView.findViewById(R.id.CompanyName);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();

            }

            holder.ename.setText(list_ClientUser.get(position).getUserName());
            holder.Category.setText(list_ClientUser.get(position).getUserCategory());
            holder.CompanyName.setText(list_ClientUser.get(position).getCompName());


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String userID = list_ClientUser.get(position).getUserID();
                    String txt_Mail = list_ClientUser.get(position).getTxtMail();
                    String CompID = list_ClientUser.get(position).getCompID();
                    String CompName = list_ClientUser.get(position).getCompName();
                    String UserName = list_ClientUser.get(position).getUserName();
                    String UserCategory = list_ClientUser.get(position).getUserCategory();
                    String CaType = list_ClientUser.get(position).getCaType();
                    String DealerID = list_ClientUser.get(position).getDealerID();
                    String dtype = list_ClientUser.get(position).getDtype();
                    String Login_Email = list_ClientUser.get(position).getLoginEmail();
                    String dealer_name = list_ClientUser.get(position).getDealerName();
                    String status = list_ClientUser.get(position).getStatus();
                    String Imagepath = list_ClientUser.get(position).getImagepath();
                    String Masterstatus = list_ClientUser.get(position).getMasterstatus();

/*
                    SharedPreferences.Editor ed;
                    ed = sp.edit();
                    ed.putString(Utility.CLIENT_LOGIN_userID, userID);
                    ed.putString(Utility.CLIENT_LOGIN_txt_Mail, txt_Mail);
                    ed.putString(Utility.CLIENT_LOGIN_CompID, CompID);
                    ed.putString(Utility.CLIENT_LOGIN_CompName, CompName);
                    ed.putString(Utility.CLIENT_LOGIN_UserName, UserName);
                    ed.putString(Utility.CLIENT_LOGIN_UserCategory, UserCategory);
                    ed.putString(Utility.CLIENT_LOGIN_CaType, CaType);
                    ed.putString(Utility.CLIENT_LOGIN_DealerID, DealerID);
                    ed.putString(Utility.CLIENT_LOGIN_dtype, dtype);
                    ed.putString(Utility.CLIENT_LOGIN_Login_Email, Login_Email);
                    ed.putString(Utility.CLIENT_LOGIN_dealer_name, dealer_name);
                    ed.putString(Utility.CLIENT_LOGIN_status, status);
                    ed.putString(Utility.CLIENT_LOGIN_Imagepath, Imagepath);
                    ed.putString(Utility.CLIENT_LOGIN_Masterstatus, Masterstatus);
                    ed.apply();*/
                    Utility.SaveClientLoginData(activity.getApplicationContext(), userID, txt_Mail, CompID, CompName, UserName, UserCategory,
                            CaType, DealerID, dtype, Login_Email, dealer_name, status, Imagepath, Masterstatus

                    );


                    Utility.setLoginTrue(activity, Utility.LOGIN_TYPE_CLIENT);
                    Intent i = new Intent(activity, ClientHomeActivity.class);
                    activity.startActivity(i);
                    activity.finish();

                }
            });


        } catch (Exception e) {
            e.getMessage();
        }
        return convertView;
    }

    final public class ViewHolder {
        private TextView ename;
        private TextView Category;
        private TextView CompanyName;


    }


}
