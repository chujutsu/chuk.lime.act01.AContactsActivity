package chuk.lime.act01;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContactAdapter extends ArrayAdapter<Contact>{
	
	private Context context;
	Contact[] values = null;
	private char type;
	
	public ContactAdapter(Context context, ArrayList<Contact> contactList, char type) {
		super(context, R.layout.list_item, contactList);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.values = contactList.toArray(new Contact[contactList.size()]);
		this.type = type;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if(convertView == null || type == 'D'){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_item, parent, false);
		}
		
		TextView tvCName = (TextView)convertView.findViewById(R.id.tvContactName);
		TextView tvCNum = (TextView)convertView.findViewById(R.id.tvContactNumber);
		TextView tvCEmail = (TextView)convertView.findViewById(R.id.tvContactEmail);
		tvCName.setText(values[position].name);
		tvCNum.setText(values[position].number);
		tvCEmail.setText(values[position].email);
		
		convertView.setTag(values[position]);
		
		
		
		return convertView;
	}

}
