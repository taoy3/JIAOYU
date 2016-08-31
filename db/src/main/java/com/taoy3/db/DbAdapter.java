package com.taoy3.db;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by taoy3 on 16/8/22.
 */
public class DbAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private List<Person> list;
    private String TAG = "DbAdapter";

    public DbAdapter(Context context, List<Person> list){
        super();
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(null==convertView){
           convertView = inflater.inflate(
                    R.layout.db_item_layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.etName.setText(list.get(position).getName());
        viewHolder.etSex.setText(list.get(position).getSex());
        viewHolder.etAge.setText(list.get(position).getAge()+"");
        return convertView;
    }

    private static class ViewHolder{
        private final TextView etName;
        private final TextView etSex;
        private final TextView etAge;

        private ViewHolder(View view){
            etName = (TextView) view.findViewById(R.id.name);
             etSex = (TextView) view.findViewById(R.id.sex);
             etAge = (TextView) view.findViewById(R.id.age);
        }
    }
}
