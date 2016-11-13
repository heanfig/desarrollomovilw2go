package com.dz.notas.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dz.notas.ChatMessage;
import com.dz.notas.R;
import com.dz.notas.models.Contact;
import com.dz.notas.models.User;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by herman on 12/11/2016.
 */
public class ContactAdapter extends BaseAdapter  {

    private final List<Contact> userslist;
    private Context context;

    public ContactAdapter(Context context, List<Contact> userslist){
        this.userslist = userslist;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (userslist != null) {
            return userslist.size();
        } else {
            return 0;
        }
    }

    @Override
    public Contact getItem(int position) {
        if (userslist != null) {
            return userslist.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Contact contact = getItem(position);
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = vi.inflate(R.layout.contact_list_item, null);
            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtMessage.setText(contact.getID());
        holder.id.setText(contact.getName());
        holder.phone.setText(contact.getPhone());
        return convertView;
    }

    private ViewHolder createViewHolder(View v) {
        ViewHolder holder = new ViewHolder();
        holder.txtMessage = (TextView) v.findViewById(R.id.text1);
        holder.id = (TextView) v.findViewById(R.id.hiddent);
        holder.phone = (TextView) v.findViewById(R.id.text3);
        return holder;
    }

    private static class ViewHolder {
        public TextView txtMessage;
        public TextView phone;
        public TextView id;
    }
}
