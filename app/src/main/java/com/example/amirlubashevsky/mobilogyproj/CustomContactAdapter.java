package com.example.amirlubashevsky.mobilogyproj;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

public class CustomContactAdapter extends  RecyclerView.Adapter<CustomContactAdapter.ViewHolder> {
	private DatabaseHandler db;
	private Context mContext;
	private LayoutInflater inflater;
	private int defaultColor;
	private List<Contact> lstContactsSMS;

	public CustomContactAdapter(Context context,  DatabaseHandler databaseHandler, List<Contact> lstContactsSMS) {
		this.mContext = context;
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.defaultColor = ContextCompat.getColor(context, R.color.colorPrimary);
		this.db = databaseHandler;
		this.lstContactsSMS = lstContactsSMS;
	}



	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemLayoutView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.contact_list_element, null);
		ViewHolder viewHolder = new ViewHolder(itemLayoutView);

		return viewHolder;
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {

		Contact contactLst = lstContactsSMS.get(position);
		holder.tvContactName.setText(contactLst.getName());

		int[] minuts = Utils.splitToComponentTimes(contactLst.getDurationTime());

		holder.contactSMS.setText(""+contactLst.getSmsCount() );
		holder.durationCallTime.setText("hours :" +minuts[0] + " minuts: "+minuts[1] + " seconds :" + minuts[2]);
		holder.tvContactNumber.setText(contactLst.getPhoneNumber());

		String imageUri = contactLst.getImageUri();
		if (imageUri != null) {
			Uri u = Uri.parse(imageUri);
				holder.ivContactImage.setImageURI(u);
		} else {
				holder.ivContactImage.setImageResource(R.drawable.contactdemo);
		}

		String dataID = contactLst.getID();

		Contact contact = db.getContact(dataID);

		if(contact != null){

			int color = contact.get_contact_color();
			holder.contact_list_element_main_rl.setBackgroundColor(color);

		}else{
			holder.contact_list_element_main_rl.setBackgroundColor(Color.WHITE);
		}


		holder.pick_color_btn.setTag(dataID );
		holder.pick_color_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				String tag = view.getTag().toString();
				OpenColorPickerDialog(false, holder.contact_list_element_main_rl, tag);
			}
		});

	}

	private void OpenColorPickerDialog(boolean AlphaSupport, final RelativeLayout contact_list_element_main_rl,final String tag) {

		AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(mContext, defaultColor, AlphaSupport, new AmbilWarnaDialog.OnAmbilWarnaListener() {
			@Override
			public void onOk(AmbilWarnaDialog ambilWarnaDialog, int color) {

				defaultColor = color;

				Contact contact = new Contact(tag.toString(), color);

				int isupdate = db.updateContact(contact);
				System.out.println("Kate isupdate =" + isupdate);
				if(isupdate == 0) {
					db.addContact(contact);
				}
				contact_list_element_main_rl.setBackgroundColor(color);
			}

			@Override
			public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {

				Toast.makeText(mContext, "Color Picker Closed", Toast.LENGTH_SHORT).show();
			}
		});
		ambilWarnaDialog.show();

	}



	@Override
	public int getItemViewType(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {

		if(lstContactsSMS != null){
			return lstContactsSMS.size();
		}

		return 0;
	}


	public class ViewHolder extends RecyclerView.ViewHolder {

		private TextView tvContactName;
		private TextView tvContactNumber;
		private TextView callslayoutitem_contact_duration;
		private  TextView contactSMS;
		private ImageView ivContactImage;
		private Button pick_color_btn;
		private RelativeLayout contact_list_element_main_rl;
		private  TextView durationCallTime;


		public ViewHolder(View itemView) {
			super(itemView);
			initializeItems(itemView);
		}

		private void initializeItems(View itemLayoutView ){
			durationCallTime = (TextView)itemLayoutView.findViewById(R.id.durationCallTime);
			contactSMS = (TextView)itemLayoutView.findViewById(R.id.contactSMS);
			tvContactName = (TextView)itemLayoutView.findViewById(R.id.tvContactName);
			tvContactNumber = (TextView)itemLayoutView.findViewById(R.id.tvContactNumber);
			ivContactImage = (ImageView) itemLayoutView.findViewById(R.id.ivContactImage);
			pick_color_btn = (Button)itemLayoutView.findViewById(R.id.pick_color);
			contact_list_element_main_rl = (RelativeLayout)itemLayoutView.findViewById(R.id.contact_list_element_main_rl);;


		}

	}

}
