package planet.info.skyline.tech.whats_inside;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import planet.info.skyline.R;
import planet.info.skyline.adapter.Adapter_Crates_WhatsInside;
import planet.info.skyline.adapter.Adapter_Elements_WhatsInside;
import planet.info.skyline.adapter.Adapter_Graphics_WhatsInside;
import planet.info.skyline.util.Utility;


public class ShowWhatsInside_Fragment extends Fragment {

    ListView jobs_list_View;
    View rootView;
    String jobtxt_id, tab;
    TextView tv_msg;


    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> map;

    ArrayList<HashMap<String, String>> data1;
    HashMap<String, String> map1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_whats_inside, container, false);
        jobs_list_View = (ListView) rootView.findViewById(R.id.cart_listview);
        tv_msg = (TextView) rootView.findViewById(R.id.tv_msg);
        tv_msg.setVisibility(View.VISIBLE);
        tv_msg.setText("");
        jobs_list_View.setAdapter(null);

        Bundle bundle = this.getArguments();
        if (bundle != null) {

            String tabId = bundle.getString(Utility.KEY_WHATSINSIDE_TAB_ID, null);
            try {
                if (tabId.equals("0")) {

                    ArrayList<HashMap<String, String>> al_element1 = (ArrayList<HashMap<String, String>>) bundle.getSerializable(Utility.KEY_BUNDLE_ELEMENT_1);
                    ArrayList<HashMap<String, String>> al_element2 = (ArrayList<HashMap<String, String>>) bundle.getSerializable(Utility.KEY_BUNDLE_ELEMENT_2);

                    ArrayList<HashMap<String, String>> al_element = new ArrayList<>();
                    if(al_element1 !=null && al_element1.size()>0) {
                        al_element.addAll(al_element1);
                    }
                    if(al_element2 !=null && al_element2.size()>0) {
                        al_element.addAll(al_element2);
                    }
                    if (al_element.size() < 1) {
                        tv_msg.setVisibility(View.VISIBLE);
                        tv_msg.setText("No Elements to show!");
                    } else {
                        tv_msg.setVisibility(View.GONE);
                        Adapter_Elements_WhatsInside ad = new Adapter_Elements_WhatsInside(getActivity(), al_element);
                        jobs_list_View.setAdapter(ad);
                    }

                }
            } catch (Exception e) {
                e.getMessage();
            }
            try {
                if (tabId.equals("1")) {

                    ArrayList<HashMap<String, String>> al_graphics1 = (ArrayList<HashMap<String, String>>) bundle.getSerializable(Utility.KEY_BUNDLE_GRAPHICS_1);
                    ArrayList<HashMap<String, String>> al_graphics2 = (ArrayList<HashMap<String, String>>) bundle.getSerializable(Utility.KEY_BUNDLE_GRAPHICS_2);

                    ArrayList<HashMap<String, String>> al_graphics = new ArrayList<>();
                    if(al_graphics1 !=null && al_graphics1.size()>0) {
                        al_graphics.addAll(al_graphics1);
                    }
                    if(al_graphics2 !=null && al_graphics2.size()>0) {
                        al_graphics.addAll(al_graphics2);
                    }

                    if (al_graphics.size() < 1) {
                        tv_msg.setVisibility(View.VISIBLE);
                        tv_msg.setText("No Graphics to show!");
                    } else {
                        tv_msg.setVisibility(View.GONE);
                        Adapter_Graphics_WhatsInside ad = new Adapter_Graphics_WhatsInside(getActivity(), al_graphics);
                        jobs_list_View.setAdapter(ad);
                    }

                }
            } catch (Exception e) {
                e.getMessage();
            }

            try {
                if (tabId.equals("2")) {


                    ArrayList<HashMap<String, String>> al_crates1 = (ArrayList<HashMap<String, String>>) bundle.getSerializable(Utility.KEY_BUNDLE_CRATES_1);
                    ArrayList<HashMap<String, String>> al_crates2 = (ArrayList<HashMap<String, String>>) bundle.getSerializable(Utility.KEY_BUNDLE_CRATES_2);

                    ArrayList<HashMap<String, String>> al_crates = new ArrayList<>();
                    if(al_crates1 !=null && al_crates1.size()>0) {
                        al_crates.addAll(al_crates1);
                    }
                    if(al_crates2 !=null && al_crates2.size()>0) {
                        al_crates.addAll(al_crates2);
                    }

                    if (al_crates.size() < 1) {
                        tv_msg.setVisibility(View.VISIBLE);
                        tv_msg.setText("No Crates to show!");
                    } else {
                        tv_msg.setVisibility(View.GONE);
                        Adapter_Crates_WhatsInside ad = new Adapter_Crates_WhatsInside(getActivity(), al_crates);
                        jobs_list_View.setAdapter(ad);

                    }

                }
            } catch (Exception e) {
                e.getMessage();
            }

        }


        return rootView;
    }



}
