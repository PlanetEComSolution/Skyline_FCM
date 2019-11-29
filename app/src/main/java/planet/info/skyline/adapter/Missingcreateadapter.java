

package planet.info.skyline.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

//import cn.pedant.SweetAlert.SweetAlertDialog;
import planet.info.skyline.R;
import planet.info.skyline.old_activity.Updatecreatelocation;
import planet.info.skyline.model.Misingcreate;

public class Missingcreateadapter extends BaseAdapter {
private Activity activity;
private LayoutInflater inflater;
private List<Misingcreate> creatmodel;
//private ImageLoader imageloader= AppController.getInstance().getImageloader();
ImageView gomissi;
//String[] gg=new String[10];
String selectcreate="",missingcreate="";
	AlertDialog alertDialog;
public Missingcreateadapter(Activity act, List<Misingcreate> cr){
	this.activity=act;
	this.creatmodel=cr;

	gomissi=(ImageView)activity.findViewById(R.id.gomissi);
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
			//customlistformissing.xml
			//convertView = inflater.inflate(R.layout.customlistforhome,null);
			convertView = inflater.inflate(R.layout.row_missing_crates_selected,null);
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
			
		Misingcreate cc=creatmodel.get(position);
		//viewHolder.creid.setText(cc.getCretateid());
		viewHolder.creid.setText(cc.getUnique_crate_id());
		viewHolder.credesc.setText(cc.getDescription());
		viewHolder.creloca.setText(cc.getLocationcrate());
		viewHolder.index_no.setText(index1);
		//convertView = inflater.inflate(R.layout.activity_list_item, null);
	//	return null;
		viewHolder.Miss.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Misingcreate hh= (Misingcreate)viewHolder.credesc.getTag();
				String ffff=hh.getCretateid();
				missingcreate=missingcreate+ffff+",";
				//viewHolder.selectt.setImageDrawable(R.drawable.)
				viewHolder.Miss.setImageResource(R.drawable.missingreda);
				viewHolder.selectt.setImageResource(R.drawable.selectlighta);
			}
		});
		
		/*gomissi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			//	Toast.makeText(activity,"hello" , 2000).show();
				 Intent ii=new Intent(activity,Selectmissing.class);
			   	 // ii.putExtra("dataa", "http://exhibitpower.com/crate_web_service.php?id=1321");
				 ii.putExtra("selectcreate",selectcreate);
				 ii.putExtra("missingcreate",missingcreate);
			   //	  activity.startActivity(ii);
			}
		});*/
		viewHolder.selectt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Misingcreate hh= (Misingcreate)viewHolder.credesc.getTag();
				viewHolder.selectt.setImageResource(R.drawable.selectgreen);
				viewHolder.Miss.setImageResource(R.drawable.missinglighta);
				
				//	viewHolder.selectt.setImageDrawable(R.drawable.selectgreen);
				/*String ffff=hh.getCretateid();
				String lic=hh.getLocationcrate();
				
			
				Intent ii=new Intent();
				selectcreate=selectcreate+ffff+",";
				//
				
						
				Intent inew=new Intent(activity,Updatecreatelocation.class);
				inew.putExtra("oldlocation",lic );
				inew.putExtra("idi", ffff);
				inew.putExtra("case", "2");
				activity.startActivity(inew);*/
				String ffff=hh.getCretateid();
				String lic=hh.getLocationcrate();
				showscannewbin(ffff,lic);
				//
				//ii.putExtra(name, value)
				//Toast.makeText(activity,ffff , 2000).show();
			}
		});
		
		viewHolder.credesc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		Misingcreate hh= (Misingcreate)viewHolder.credesc.getTag();
		
		String ffff=hh.getDescription();
		
		
		
	//	Toast.makeText(activity,ffff , 2000).show();
		
				//int gg=creatmodel.get()
			//	Toast.makeText(activity, "hello", 2000).show();
			}
			
			
		}); 
		
		viewHolder.creid.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		Misingcreate hh= (Misingcreate)viewHolder.creid.getTag();
		String ff=hh.getCretateid();
		
	//	Toast.makeText(activity,ff , 2000).show();
		
				//int gg=creatmodel.get()
				//Toast.makeText(activity, "hello", 2000).show();
			}
			
			
		}); 
		
		viewHolder.creloca.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		Misingcreate hh= (Misingcreate)viewHolder.creloca.getTag();

		String fff=hh.getLocationcrate();
		
		//Toast.makeText(activity, fff, 2000).show();
				//int gg=creatmodel.get()
			//	Toast.makeText(activity, "hello", 2000).show();
			}
			
			
		}); 
		viewHolder.Miss.setImageResource(R.drawable.missingreda);
		viewHolder.selectt.setImageResource(R.drawable.selectlighta);
		return convertView;
	}
	public void showscannewbin(final String ff, final String lic) {





		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
		LayoutInflater inflater = LayoutInflater.from(activity);
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
		title.setText("");
		message.setText("Scan the bin in which\nyou found the missing crate.");
		positiveBtn.setText("Ok");
		negativeBtn.setText("Cancel");
		negativeBtn.setVisibility(View.GONE);
		positiveBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				alertDialog.dismiss();
				Intent ii=new Intent();
				selectcreate=selectcreate+ff+",";
				//

				Intent inew=new Intent(activity,Updatecreatelocation.class);
				inew.putExtra("oldlocation",lic );
				inew.putExtra("idi", ff);
				inew.putExtra("case", "2");
				activity.startActivity(inew);
			}
		});
		negativeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				alertDialog.dismiss();
			}
		});
		alertDialog = dialogBuilder.create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.setCancelable(false);
		alertDialog.show();







		/*final SweetAlertDialog sDialog = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE);
		sDialog.setTitleText("");
		sDialog.setContentText("Scan the bin in which you found the missing crate.");
		sDialog.setCancelable(false);
		sDialog.setConfirmText("Ok").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				sDialog.dismissWithAnimation();

				Intent ii=new Intent();
				selectcreate=selectcreate+ff+",";
				//

				Intent inew=new Intent(activity,Updatecreatelocation.class);
				inew.putExtra("oldlocation",lic );
				inew.putExtra("idi", ff);
				inew.putExtra("case", "2");
				activity.startActivity(inew);
			}
		}).show();
*/









	/*	final Dialog showd = new Dialog(activity);
		showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
		showd.setContentView(R.layout.alerfinishwithjob);
		showd.setCancelable(false);
		TextView nofo = (TextView) showd.findViewById(R.id.noforlocation);
		TextView yesfo = (TextView) showd.findViewById(R.id.yesforloc);
		ImageView close = (ImageView) showd.findViewById(R.id.close);
		TextView texrtdesc = (TextView) showd.findViewById(R.id.texrtdesc);
		texrtdesc.setText("Scan the bin in which you found the missing crate.");
		nofo.setText("  OK  ");
		yesfo.setText("");
		yesfo.setVisibility(View.GONE);
		TextView textviewheader = (TextView) showd
				.findViewById(R.id.textView1rr);
		textviewheader.setText("Please select");
		nofo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent ii=new Intent();
				selectcreate=selectcreate+ff+",";
				//
				

				Intent inew=new Intent(activity,Updatecreatelocation.class);
				inew.putExtra("oldlocation",lic );
				inew.putExtra("idi", ff);
				inew.putExtra("case", "2");
				activity.startActivity(inew);
				showd.dismiss();
				 
			}
		});
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showd.dismiss();
				
			}
		});
		yesfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			
			}
		});

		showd.show();*/
	}


}

