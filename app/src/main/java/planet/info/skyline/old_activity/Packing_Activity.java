package planet.info.skyline.old_activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.network.Api;

import static planet.info.skyline.network.SOAP_API_Client.URL_EP1;


public class Packing_Activity extends Activity {


    Spinner s1,s2;
    Button search;
    ListView create,collateral,Exbit_element_list,accet_main_list,Exbit_graphic_list;
  //  JsonParser jsonParser;
    List<HashMap<String,String>> data=new ArrayList<HashMap<String,String>>();

    HashMap<String,String> map=new HashMap<>();
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_packing);

        accet_main_list=(ListView)findViewById(R.id.exp);


        collateral=(ListView)findViewById(R.id.exp3);

        Exbit_element_list=(ListView)findViewById(R.id.exp1);
        Exbit_graphic_list=(ListView)findViewById(R.id.grapic_list);

        img=(ImageView)findViewById(R.id.ex3);
        s1=(Spinner)findViewById(R.id.spinner);
        s2=(Spinner)findViewById(R.id.spinner1);
        search=(Button)findViewById(R.id.button);
        create=(ListView)findViewById(R.id.listView1);
        collateral=(ListView)findViewById(R.id.exp3);
        data.add(map);
        create.setAdapter(new Creates_Adapter(this, data));
        collateral.setAdapter(new Collateral_Adapter(this,data));
        Exbit_element_list.setAdapter(new Exhibit_Element_Adapter(this, data));
        accet_main_list.setAdapter(new Accesser_Adapter(this, data));

        Exbit_graphic_list.setAdapter(new Exhibit_Graphic_Adapter(this, data));

        Utils1.setListViewHeightBasedOnChildren(accet_main_list);

        Utils1.setListViewHeightBasedOnChildren(create);
        Utils1.setListViewHeightBasedOnChildren(collateral);
      Utils1.setListViewHeightBasedOnChildren(Exbit_graphic_list); //issue in this

        Picasso.with(Packing_Activity.this).load(URL_EP1+ Api.API_COLLATERAL_PATH+"1462793388_exhibit3__511.jpg").into(img);


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog d = new Dialog(Packing_Activity.this);

                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.popup);
                ImageView image = (ImageView) d.findViewById(R.id.popup_image);///for open a image
                ImageButton close = (ImageButton) d.findViewById(R.id.imageButton3);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
                Picasso.with(Packing_Activity.this).load(URL_EP1+Api.API_COLLATERAL_PATH+"1462793388_exhibit3__511.jpg").into(image);
                d.show();

            }
        });


        search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {



            }
        });
    }



    ///for fetch the SOAP_API_Client name from the server-

    class download_data_from extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
          //  jsonParser=new JsonParser();
            //jsonParser.makeHttpRequest();

            return null;
        }
    }

    public  class Creates_Adapter  extends BaseAdapter
    {

        String[]status_data={"--Select Status--","Avaliable","Damaged","Missing","Out Of Show"};
        String bookname;
        List<HashMap<String,String>> beanArrayList;
        Context context;
        int count=1;

        public Creates_Adapter(Context context, List<HashMap<String,String>> beanArrayList)
        {
            this.context=context;
            this.beanArrayList =beanArrayList;

        }

        @Override
        public int getCount()
        {
          //  return beanArrayList.size();

            return 1;
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

            if(convertview==null)
            {
                holder= new Holder();
                convertview= LayoutInflater.from(context).inflate(R.layout.creates_list_item,null);

                holder.create_name= (TextView) convertview.findViewById(R.id.e1);
                holder.Crate_Type= (TextView) convertview.findViewById(R.id.des_text);
                holder.Crate_Dims= (TextView) convertview.findViewById(R.id.image_text);
                holder.Description= (TextView) convertview.findViewById(R.id.date_text);
                holder.Location= (TextView) convertview.findViewById(R.id.status_text);
                holder.Return_Date= (TextView) convertview.findViewById(R.id.not_test);
                holder.Status= (Spinner) convertview.findViewById(R.id.sta_text);
                holder.Included= (CheckBox) convertview.findViewById(R.id.inot_text);
                holder.View= (TextView) convertview.findViewById(R.id.view);

                ArrayAdapter<String> adapter=new ArrayAdapter<String>(Packing_Activity.this,android.R.layout.simple_spinner_item, status_data);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                holder.Status.setAdapter(adapter);


                convertview.setTag(holder);
            }
            else
            {
                holder= (Holder) convertview.getTag();
            }
      //      holder.product_no.setText(count);
        //    holder.product_name.setText(Book_title);
         //   holder.parts.setText(Book_Volums);
        //    holder.parts_quentity.setText(Book_quentity);
        //    holder.postage_amount.setText("INR " + postage_Amount);

            return convertview;
        }

        class Holder
        {
            TextView create_name, Crate_Type,Crate_Dims,Description,Location,Return_Date,View;
            CheckBox Included;
            Spinner Status;

        }

    }




    public  class Collateral_Adapter  extends BaseAdapter
    {

        String[]status_data={"--Select Status--","Avaliable","Damaged","Missing","Out Of Show"};
        List<HashMap<String,String>> beanArrayList;
        Context context;
        int count=1;

        public Collateral_Adapter(Context context, List<HashMap<String,String>> beanArrayList)
        {
            this.context=context;
            this.beanArrayList =beanArrayList;

        }

        @Override
        public int getCount()
        {
            //  return beanArrayList.size();

            return 1;
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

            if(convertview==null)
            {
                holder= new Holder();
                convertview= LayoutInflater.from(context).inflate(R.layout.collateral_list_item,null);

                holder.Type= (TextView) convertview.findViewById(R.id.e1);
                holder.Description= (TextView) convertview.findViewById(R.id.des_text);
                holder.Image= (ImageView) convertview.findViewById(R.id.image_text);
                holder.Date= (TextView) convertview.findViewById(R.id.date_text);
                holder.Status= (Spinner) convertview.findViewById(R.id.status_text);
                holder.Included= (CheckBox) convertview.findViewById(R.id.not_test);
                holder.quentity= (TextView) convertview.findViewById(R.id.q_text);

                ArrayAdapter<String> adapter=new ArrayAdapter<String>(Packing_Activity.this,android.R.layout.simple_spinner_item, status_data);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                holder.Status.setAdapter(adapter);


                convertview.setTag(holder);
            }
            else
            {
                holder= (Holder) convertview.getTag();
            }
                      //      holder.product_no.setText(count);
            //    holder.product_name.setText(Book_title);
            //   holder.parts.setText(Book_Volums);
            //    holder.parts_quentity.setText(Book_quentity);
            //    holder.postage_amount.setText("INR " + postage_Amount);

            Picasso.with(Packing_Activity.this).load(URL_EP1+Api.API_COLLATERAL_PATH+"1462793388_exhibit3__511.jpg").into(holder.Image);





            holder.Image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog d = new Dialog(Packing_Activity.this);

                    d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    d.setContentView(R.layout.popup);
                    ImageView image = (ImageView) d.findViewById(R.id.popup_image);///for open a image
                    ImageButton close = (ImageButton) d.findViewById(R.id.imageButton3);
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            d.dismiss();
                        }
                    });
                    Picasso.with(Packing_Activity.this).load(URL_EP1+Api.API_COLLATERAL_PATH+"1462793388_exhibit3__511.jpg").into(image);
                    d.show();

                }
            });

            return convertview;
        }

        class Holder{
            TextView Type,Date,Description,quentity;
            CheckBox Included;
            Spinner Status;
            ImageView Image;

        }

    }


    //for Exbit jobs  list for element




    public  class Exhibit_Element_Adapter  extends BaseAdapter
    {

        String[]status_data={"--Select Status--","Avaliable","Damaged","Missing","Out Of Show"};

        List<HashMap<String,String>> beanArrayList;
        Context context;


        public Exhibit_Element_Adapter(Context context, List<HashMap<String,String>> beanArrayList)
        {
            this.context=context;
            this.beanArrayList =beanArrayList;

        }

        @Override
        public int getCount()
        {
            //  return beanArrayList.size();

            return 1;
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

            if(convertview==null)
            {
                holder= new Holder();
                convertview= LayoutInflater.from(context).inflate(R.layout.exbit_element_list,null);

                holder.create_name= (TextView) convertview.findViewById(R.id.ex);
                holder.date= (TextView) convertview.findViewById(R.id.ex1);
                holder.Status= (Spinner) convertview.findViewById(R.id.ex2);
                holder.Included= (CheckBox) convertview.findViewById(R.id.ex4);
                holder.View= (TextView) convertview.findViewById(R.id.view);


                ArrayAdapter<String> adapter=new ArrayAdapter<String>(Packing_Activity.this,android.R.layout.simple_spinner_item, status_data);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                holder.Status.setAdapter(adapter);


                convertview.setTag(holder);
            }
            else
            {
                holder= (Holder) convertview.getTag();
            }
            //      holder.product_no.setText(count);
            //    holder.product_name.setText(Book_title);
            //   holder.parts.setText(Book_Volums);
            //    holder.parts_quentity.setText(Book_quentity);
            //    holder.postage_amount.setText("INR " + postage_Amount);

            return convertview;
        }

        class Holder{
            TextView create_name, date,View;
            CheckBox Included;
            Spinner Status;

        }

    }


    //Exbit Graphic Element List Adapter-



    public  class Exhibit_Graphic_Adapter  extends BaseAdapter
    {

        String[]status_data={"--Select Status--","Avaliable","Damaged","Missing","Out Of Show"};
        String[]status1={"--Select Reason--","Replaced from client other Inventory","Client Approved shipping without it","Replaced from In-house Rental"};


        List<HashMap<String,String>> beanArrayList;
        Context context;


        public Exhibit_Graphic_Adapter(Context context, List<HashMap<String,String>> beanArrayList)
        {
            this.context=context;
            this.beanArrayList =beanArrayList;

        }

        @Override
        public int getCount()
        {
            //  return beanArrayList.size();

            return 2;
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

            if(convertview==null)
            {
                holder= new Holder();
                convertview= LayoutInflater.from(context).inflate(R.layout.inner_graphic_element,null);

                holder.create_name= (TextView) convertview.findViewById(R.id.ex);
                holder.date= (TextView) convertview.findViewById(R.id.ex1);
                holder.Status= (Spinner) convertview.findViewById(R.id.ex2);
                holder.resiom= (Spinner) convertview.findViewById(R.id.spinner1);
                holder.View= (TextView) convertview.findViewById(R.id.view);
                holder.size= (TextView) convertview.findViewById(R.id.size);
                holder.Included= (CheckBox) convertview.findViewById(R.id.ex4);
                holder.image= (ImageView) convertview.findViewById(R.id.image);


                ArrayAdapter<String> adapter=new ArrayAdapter<String>(Packing_Activity.this,android.R.layout.simple_spinner_item, status_data);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                holder.Status.setAdapter(adapter);


                ArrayAdapter<String> adapter1=new ArrayAdapter<String>(Packing_Activity.this,android.R.layout.simple_spinner_item, status1);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                holder.resiom.setAdapter(adapter1);

             //   holder.resiom.setVisibility(View.INVISIBLE);

                Picasso.with(Packing_Activity.this).load(URL_EP1+Api.API_COLLATERAL_PATH+"1462793388_exhibit3__511.jpg").into(holder.image);





                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog d = new Dialog(Packing_Activity.this);

                        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        d.setContentView(R.layout.popup);
                        ImageView image = (ImageView) d.findViewById(R.id.popup_image);///for open a image
                        ImageButton close = (ImageButton) d.findViewById(R.id.imageButton3);
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                d.dismiss();
                            }
                        });
                        Picasso.with(Packing_Activity.this).load(URL_EP1+Api.API_COLLATERAL_PATH+"1462793388_exhibit3__511.jpg").into(image);
                        d.show();

                    }
                });

                convertview.setTag(holder);
            }
            else
            {
                holder= (Holder) convertview.getTag();
            }
            //      holder.product_no.setText(count);
            //    holder.product_name.setText(Book_title);
            //   holder.parts.setText(Book_Volums);
            //    holder.parts_quentity.setText(Book_quentity);
            //    holder.postage_amount.setText("INR " + postage_Amount);

            return convertview;
        }

        class Holder{
            TextView create_name, date,View,size;
            CheckBox Included;
            Spinner Status,resiom;
            ImageView image;

        }

    }




    //Accesser main list


    public  class Accesser_Adapter  extends BaseAdapter
    {

        String[]status_data={"--Select Status--","Avaliable","Damaged","Missing","Out Of Show"};

        List<HashMap<String,String>> beanArrayList;
        Context context;
        int count=1;

        public Accesser_Adapter(Context context, List<HashMap<String,String>> beanArrayList)
        {
            this.context=context;
            this.beanArrayList =beanArrayList;

        }

        @Override
        public int getCount()
        {
            //  return beanArrayList.size();

            return 2;
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

            if(convertview==null)
            {
                holder= new Holder();
                convertview= LayoutInflater.from(context).inflate(R.layout.accesser_main_list,null);

                holder.Type= (TextView) convertview.findViewById(R.id.exbit0);
                holder.Description= (TextView) convertview.findViewById(R.id.exbit01);
                holder.quentity= (TextView) convertview.findViewById(R.id.exbit02);
                holder.Image= (ImageView) convertview.findViewById(R.id.exbit03);
                holder.in= (TextView) convertview.findViewById(R.id.exbit04);
                holder.l1= (ListView) convertview.findViewById(R.id.exp);
                holder.l2= (ListView) convertview.findViewById(R.id.exp1);

                holder.l1.setAdapter(new Inner_lIST_Accessories_ELEMENT(Packing_Activity.this,data));
                Utils1.setListViewHeightBasedOnChildren(holder.l1);


                holder.l2.setAdapter(new Inner_Graphic_Accessories_ELEMENT(Packing_Activity.this, data));
                Utils1.setListViewHeightBasedOnChildren(holder.l2);

                Picasso.with(Packing_Activity.this).load(URL_EP1+Api.API_COLLATERAL_PATH+"1462793388_exhibit3__511.jpg").into(holder.Image);

                holder.Image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog d = new Dialog(Packing_Activity.this);

                        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        d.setContentView(R.layout.popup);
                        ImageView image = (ImageView) d.findViewById(R.id.popup_image);///for open a image
                        ImageButton close = (ImageButton) d.findViewById(R.id.imageButton3);
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                d.dismiss();
                            }
                        });
                        Picasso.with(Packing_Activity.this).load(URL_EP1+Api.API_COLLATERAL_PATH+"1462793388_exhibit3__511.jpg").into(image);
                        d.show();

                    }
                });

                convertview.setTag(holder);
            }
            else
            {
                holder= (Holder) convertview.getTag();


            }



            return convertview;
        }

        class Holder{
            TextView Type,Description,quentity,in;

            ImageView Image;
            ListView l1,l2;

        }

    }

///new Adapter for Accessories Elements List item.xml file


    public  class Inner_lIST_Accessories_ELEMENT  extends BaseAdapter
    {

        String[]status_data={"--Select Status--","Avaliable","Damaged","Missing","Out Of Show"};
        List<HashMap<String,String>> beanArrayList;
        Context context;

        public Inner_lIST_Accessories_ELEMENT(Context context, List<HashMap<String,String>> beanArrayList)
        {
            this.context=context;
            this.beanArrayList =beanArrayList;

        }

        @Override
        public int getCount()
        {
            return 1;
        }

        @Override
        public Object getItem(int i)
        {
            return i;
        }

        @Override
        public long getItemId(int i)
        {
            return i;
        }

        @Override
        public View getView(final int i, View convertview, ViewGroup viewGroup)
        {
            final Holder holder;

            if(convertview==null)
            {
                holder= new Holder();
                convertview= LayoutInflater.from(context).inflate(R.layout.inner_elemnt_list_accesser,null);

                holder.create_name= (TextView) convertview.findViewById(R.id.ex);
                holder.date= (TextView) convertview.findViewById(R.id.ex1);
                holder.Status= (Spinner) convertview.findViewById(R.id.ex2);
                holder.Included= (CheckBox) convertview.findViewById(R.id.ex4);
                holder.View= (TextView) convertview.findViewById(R.id.view);


                ArrayAdapter<String> adapter=new ArrayAdapter<String>(Packing_Activity.this,android.R.layout.simple_spinner_item, status_data);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                holder.Status.setAdapter(adapter);


                convertview.setTag(holder);
            }
            else
            {
                holder= (Holder) convertview.getTag();
            }



            return convertview;
        }

        class Holder
        {
            TextView create_name, date,View;
            CheckBox Included;
            Spinner Status;

        }

    }


    //iner Accesser Graphic list  Adapter

    public  class Inner_Graphic_Accessories_ELEMENT  extends BaseAdapter
    {

        String[]status_data={"--Select Status--","Avaliable","Damaged","Missing","Out Of Show"};

        String[]status1={"--Select Reason--","Replaced from client other Inventory","Client Approved shipping without it","Replaced from In-house Rental"};


        List<HashMap<String,String>> beanArrayList;
        Context context;

        public Inner_Graphic_Accessories_ELEMENT(Context context, List<HashMap<String,String>> beanArrayList)
        {
            this.context=context;
            this.beanArrayList =beanArrayList;

        }

        @Override
        public int getCount()
        {
            return 1;
        }

        @Override
        public Object getItem(int i)
        {
            return i;
        }

        @Override
        public long getItemId(int i)
        {
            return i;
        }

        @Override
        public View getView(final int i, View convertview, ViewGroup viewGroup)
        {
            final Holder holder;

            if(convertview==null)
            {
                holder= new Holder();
                convertview= LayoutInflater.from(context).inflate(R.layout.inner_graphic_element,null);

                holder.create_name= (TextView) convertview.findViewById(R.id.ex);
                holder.date= (TextView) convertview.findViewById(R.id.ex1);
                holder.Status= (Spinner) convertview.findViewById(R.id.ex2);
                holder.resiom= (Spinner) convertview.findViewById(R.id.spinner1);
                holder.View= (TextView) convertview.findViewById(R.id.view);
                holder.size= (TextView) convertview.findViewById(R.id.size);
                holder.Included= (CheckBox) convertview.findViewById(R.id.ex4);
                holder.image= (ImageView) convertview.findViewById(R.id.image);


                ArrayAdapter<String> adapter=new ArrayAdapter<String>(Packing_Activity.this,android.R.layout.simple_spinner_item, status_data);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                holder.Status.setAdapter(adapter);


                ArrayAdapter<String> adapter1=new ArrayAdapter<String>(Packing_Activity.this,android.R.layout.simple_spinner_item, status1);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                holder.resiom.setAdapter(adapter1);

              //  holder.resiom.setVisibility(View.INVISIBLE);

                Picasso.with(Packing_Activity.this).load(URL_EP1+Api.API_COLLATERAL_PATH+"1462793388_exhibit3__511.jpg").into(holder.image);


                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        final Dialog d = new Dialog(Packing_Activity.this);

                        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        d.setContentView(R.layout.popup);
                        ImageView image = (ImageView) d.findViewById(R.id.popup_image);///for open a image
                        ImageButton close = (ImageButton) d.findViewById(R.id.imageButton3);
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                d.dismiss();
                            }
                        });
                        Picasso.with(Packing_Activity.this).load(URL_EP1+Api.API_COLLATERAL_PATH+"1462793388_exhibit3__511.jpg").into(image);
                        d.show();

                    }
                });
                convertview.setTag(holder);
            }
            else
            {
                holder= (Holder) convertview.getTag();
            }



            return convertview;
        }

        class Holder
        {
            TextView create_name, date,View,size;
            CheckBox Included;
            Spinner Status,resiom;
            ImageView image;

        }

    }
}
