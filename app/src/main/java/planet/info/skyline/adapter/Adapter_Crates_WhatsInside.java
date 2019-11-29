package planet.info.skyline.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;


import planet.info.skyline.R;
import planet.info.skyline.tech.whats_inside.ShowWhatsInside_Activity;
import planet.info.skyline.util.Utility;


public class Adapter_Crates_WhatsInside extends BaseAdapter {
    List<HashMap<String, String>> beanArrayList;
    Context context;
    int count = 1;
    AlertDialog alertDialog;
    public Adapter_Crates_WhatsInside(Context context, List<HashMap<String, String>> beanArrayList) {
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
        final String index1 = String.valueOf(i+1);
        final String crateId = beanArrayList.get(i).get("id");
        final String config = beanArrayList.get(i).get("crates_configure");
        final String crateNumber = beanArrayList.get(i).get("crate_number");

        final String IsCrateEmpty = beanArrayList.get(i).get(Utility.KEY_IS_EMPTY);

        if (convertview == null) {
            holder = new Holder();

            convertview = LayoutInflater.from(context).inflate(R.layout.row_crates_new, null);


            holder.index_no = (Button) convertview.findViewById(R.id.serial_no);
            holder.crateId = (TextView) convertview.findViewById(R.id.crateId);
            holder.config = (TextView) convertview.findViewById(R.id.config);

            holder.view_file = (Button) convertview.findViewById(R.id.view_file);
            holder.download_file = (Button) convertview.findViewById(R.id.download_file);
            //nks
            holder.row_jobFile = (LinearLayout) convertview.findViewById(R.id.row_jobFile);
            //nks

            convertview.setTag(holder);

        } else {
            holder = (Holder) convertview.getTag();


        }
        holder.index_no.setText(index1);


        if (IsCrateEmpty.equalsIgnoreCase("true")) {
            holder.view_file.setVisibility(View.GONE);

        } else {
            holder.view_file.setVisibility(View.VISIBLE);

        }


        if(crateId.equals(null) || crateId.trim().equals("")){
            holder.crateId.setText("Not available");
        }
        else
        {
            holder.crateId.setText(crateNumber.trim());
        }

        if(config.equals(null) || config.trim().equals("")){
            holder.config.setText("Not available");
        }
        else
        {
            holder.config.setText(config.trim());
        }



        holder.view_file.setText(" View ");
        holder.view_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(context, ShowWhatsInside_Activity.class);
                    intent.putExtra(Utility.KEY_CRATE_ID, crateId);
                    context.startActivity(intent);
                }
                catch (Exception e)
                {
                    e.getMessage();
                }
                }
        });
        holder.row_jobFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (IsCrateEmpty.equalsIgnoreCase("true")) {
                    Toast.makeText(context,"This crate is empty!", Toast.LENGTH_SHORT).show();

                } else {




                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = LayoutInflater.from(context);
                    final View dialogView = inflater.inflate(R.layout.dialog_yes_no, null);
                    dialogView.setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialogBuilder.setView(dialogView);
                    final TextView title = dialogView.findViewById(R.id.textView1rr);
                    final  TextView message = dialogView.findViewById(R.id.texrtdesc);

                    final Button positiveBtn = dialogView.findViewById(R.id.Btn_Yes);
                    final Button negativeBtn = dialogView.findViewById(R.id.Btn_No);
                    ImageView close = (ImageView) dialogView.findViewById(R.id.close);
                    close.setVisibility(View.INVISIBLE);
                    // dialogBuilder.setTitle("Device Details");
                    title.setText("View What's Inside ?");
                    message.setText("Press Back to cancel");
                    positiveBtn.setText("View");
                    negativeBtn.setText("Cancel");
                    negativeBtn.setVisibility(View.VISIBLE);
                    positiveBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                            Intent intent = new Intent(context, ShowWhatsInside_Activity.class);
                            intent.putExtra(Utility.KEY_CRATE_ID, crateId);
                            context.startActivity(intent);
                        }
                    });
                    negativeBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog = dialogBuilder.create();
                    alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.setCancelable(true);
                    alertDialog.show();










                 /*   new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("View What's Inside ?")
                            .setContentText("Press Back to cancel")
                            .setConfirmText("View")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    Intent intent = new Intent(context, ShowWhatsInside_Activity.class);
                                    intent.putExtra(Utility.KEY_CRATE_ID, crateId);
                                    context.startActivity(intent);
                                }
                            })
                            .setCancelText("Cancel")
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();

                                }
                            })
                            .show();*/


                }



            }
        });

        return convertview;
    }


    class Holder {
        TextView crateId, config;

        //nks
        Button index_no;
        Button view_file, download_file;
        LinearLayout row_jobFile;

        //nks
    }

}