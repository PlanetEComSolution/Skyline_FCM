
package planet.info.skyline.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.network.Api;
import planet.info.skyline.old_activity.Selectmissing;
import planet.info.skyline.model.Allmissing;

import static planet.info.skyline.network.SOAP_API_Client.URL_EP1;


public class Allmissingadapt extends BaseAdapter {

private Activity activity;
private LayoutInflater inflater;
private List<Allmissing> creatmodel;

//private ImageLoader imageloader = AppController.getInstance().getImageloader();
ImageView gomissi;
String urlforselect=URL_EP1+ Api.API_UPDATE_CRATE;
String urlformiss="&mis=";
//TextView change;
//LinearLayout change;
ImageView change;
TextView stopp;
//String[] gg=new String[10];
String selectcreate="",missingcreate="";
public Allmissingadapt(Activity act, List<Allmissing> cr){
	this.activity=act;
	this.creatmodel=cr;
	//gomissi=(ImageView)activity.findViewById(R.id.gomissi);
	//change =(LinearLayout)activity.findViewById(R.id.sourceforok);//description
	change =(ImageView)activity.findViewById(R.id.fornext);
	stopp =(TextView)activity.findViewById(R.id.createnotfound);
	/*stopp.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Intent ii=new Intent(activity,Foundscaneract.class);
			Intent ii=new Intent(activity,Updatecreatelocation.class);
			activity.startActivity(ii);	
		}
	});*/
}
static class ViewHolder {
	protected TextView creid;
	protected TextView credesc;
	protected TextView creloca;
	protected ImageView selectt;
	protected ImageView Miss;
	protected Button index_no;
//	protected TextView text4;
//	protected ImageView useronlineof;
//	protected CheckBox checkbox;
//	protected ImageView img_url;
//	protected RelativeLayout r2;

	
	//protected Button button;
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
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			//int sdszzz=planet.info.skyline.R.layout.customlistforhome;
			//
			convertView = inflater.inflate(R.layout.row_missing_crates_1,null);
			final ViewHolder viewHolder = new ViewHolder();
		
			viewHolder.creid=(TextView)convertView.findViewById(R.id.createid);
			viewHolder.credesc=(TextView)convertView.findViewById(R.id.description);
			viewHolder.creloca=(TextView)convertView.findViewById(R.id.locationss);
			
			viewHolder.Miss=(ImageView)convertView.findViewById(R.id.missss);
			viewHolder.selectt=(ImageView)convertView.findViewById(R.id.selects);
		viewHolder.index_no = (Button) convertView.findViewById(R.id.serial_no);
			viewHolder.creid.setTag(creatmodel.get(position));
			viewHolder.credesc.setTag(creatmodel.get(position));
			viewHolder.creloca.setTag(creatmodel.get(position));
			convertView.setTag(viewHolder);

		Allmissing cc=creatmodel.get(position);
		//viewHolder.creid.setText(cc.getCretateid());
/*		viewHolder.creid.setText(cc.getUnique_crate_id());
		viewHolder.credesc.setText(cc.getDescription());
		//viewHolder.creloca.setText(cc.getLocationcrate());
		viewHolder.creloca.setText("");*/
		viewHolder.creid.setText(cc.getName());
		viewHolder.credesc.setText(cc.getUnique_crate_id());
		//viewHolder.creloca.setText(cc.getLocationcrate());
		viewHolder.creloca.setText(cc.getDescription());

		viewHolder.index_no.setText(index1);

		//convertView = inflater.inflate(R.layout.activity_list_item, null);
	//	return null;
		viewHolder.selectt.setImageBitmap(null);//(R.drawable.missinglighta);
		viewHolder.Miss.setImageBitmap(null);
		viewHolder.selectt.setImageResource(R.drawable.missinglighta);
		viewHolder.Miss.setImageResource(R.drawable.selectlighta);
		viewHolder.Miss.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Allmissing hh= (Allmissing)viewHolder.credesc.getTag();
				String ffff=hh.getCretateid();
				String not="";
				//if(selectcreate.conta)
//				missingcreate=missingcreate+ffff+",";
				//viewHolder.selectt.setImageDrawable(R.drawable.)
				if(selectcreate.contains(ffff)){
					
				String llk=ffff+",";
				 
					if(selectcreate.contains(llk)){
						selectcreate=selectcreate.replace(llk, "");
					}
					else{
						selectcreate=selectcreate.replace(ffff, "");
					}
					missingcreate=missingcreate+ffff+",";
				}
				else if((missingcreate.contains(ffff))){
					String llk=ffff+",";
					if(missingcreate.contains(llk)){
						missingcreate=missingcreate.replace(llk, "");
					}
					else{
						missingcreate=missingcreate.replace(ffff, "");
					}
					viewHolder.Miss.setImageResource(R.drawable.missingdarka);
					viewHolder.selectt.setImageResource(R.drawable.selecta);
					not="not";
				}
				else{
					hh.setSelectim(1);
					hh.setMissim(1);
					missingcreate=missingcreate+ffff+",";
				}


				int realli=hh.getMissim();
				if(realli==0){
					if(not.equalsIgnoreCase("")){
					hh.setSelectim(2);
					hh.setMissim(2);
					viewHolder.Miss.setImageResource(R.drawable.missingreda);
					viewHolder.selectt.setImageResource(R.drawable.selectlighta);
					}else{
						
					}
				}
				else if(realli==1){
					hh.setSelectim(2);
					hh.setMissim(2);
					viewHolder.Miss.setImageResource(R.drawable.missingreda);
					viewHolder.selectt.setImageResource(R.drawable.selectlighta);
					
				}
				else
				{
					viewHolder.Miss.setImageResource(R.drawable.missingdarka);
					viewHolder.selectt.setImageResource(R.drawable.selecta);
				}

			}
		});
		

	change.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				
				Intent ii=new Intent(activity,Selectmissing.class);
				String ssd=urlforselect+selectcreate+urlformiss+missingcreate;
				 ssd=ssd.replace(",&", "&");
				 ssd=ssd.substring(0, ssd.length()-1);
				ii.putExtra("selectcreate",selectcreate);
				 ii.putExtra("missingcreate",missingcreate);
				Allmissing hh= (Allmissing)viewHolder.credesc.getTag();
				 String loccc=hh.getLocationcrate();
				 ii.putExtra("loccc",loccc);
				 String ds=ssd;
				 ii.putExtra("uuu",ds);
			   	  activity.startActivity(ii);
			}
		});
viewHolder.selectt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String not="";
				Allmissing hh= (Allmissing)viewHolder.credesc.getTag();
				viewHolder.selectt.setImageResource(R.drawable.selectgreen);
				String ffff=hh.getCretateid();
				if(missingcreate.contains(ffff))
				{
					String llk=ffff+",";
					if(missingcreate.contains(llk)){
							missingcreate=missingcreate.replace(llk, "");
						}
						else{
							missingcreate=missingcreate.replace(ffff, "");
						}
						selectcreate=selectcreate+ffff+",";
					}
				else if((selectcreate.contains(ffff))){
					String llk=ffff+",";
					if(selectcreate.contains(llk)){
						selectcreate=selectcreate.replace(llk, "");
					}
					else{
						selectcreate=selectcreate.replace(ffff, "");
					}
					viewHolder.Miss.setImageResource(R.drawable.missingdarka);
					viewHolder.selectt.setImageResource(R.drawable.selecta);
					not="not";
				}
				else{
					selectcreate=selectcreate+ffff+",";
					hh.setSelectim(2);
					hh.setMissim(2);
				}

				int realli=hh.getSelectim();
				if(realli==0){
					if(not.equalsIgnoreCase("")){
					hh.setSelectim(1);
					hh.setMissim(1);
					viewHolder.Miss.setImageResource(R.drawable.missinglighta);
					viewHolder.selectt.setImageResource(R.drawable.selectgreena);
					}else{
						
					}
				}
				else if(realli==1){
					hh.setSelectim(0);
					hh.setMissim(0);
					viewHolder.Miss.setImageResource(R.drawable.missingdarka);
					viewHolder.selectt.setImageResource(R.drawable.selecta);

					
				}
				else{
					hh.setSelectim(1);
					hh.setMissim(1);
					viewHolder.Miss.setImageResource(R.drawable.missinglighta);
					viewHolder.selectt.setImageResource(R.drawable.selectgreena);
	
				}

			}
		});
		
		viewHolder.credesc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		Allmissing hh= (Allmissing)viewHolder.credesc.getTag();
		
		String ffff=hh.getDescription();
		
		
		

			}
			
			
		}); 
		
		viewHolder.creid.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		Allmissing hh= (Allmissing)viewHolder.creid.getTag();
		String ff=hh.getCretateid();

			}
			
			
		}); 
		
		viewHolder.creloca.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		Allmissing hh= (Allmissing)viewHolder.creloca.getTag();

		String fff=hh.getLocationcrate();

			}
			
			
		}); 
		Allmissing hh= (Allmissing)viewHolder.credesc.getTag();
		int realli=hh.getSelectim();
		if(realli==0){
			
			viewHolder.Miss.setImageResource(R.drawable.missingdarka);
			viewHolder.selectt.setImageResource(R.drawable.selecta);
		}
		else if(realli==1){
			
			viewHolder.Miss.setImageResource(R.drawable.missinglighta);
			viewHolder.selectt.setImageResource(R.drawable.selectgreena);
			
		}
		else{
			
			viewHolder.Miss.setImageResource(R.drawable.missingreda);
			viewHolder.selectt.setImageResource(R.drawable.selectlighta);

		}
			
		return convertView;
	}

}

