package com.example.registerapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.registerapplication.Entity.Data.InterestsItem;
import com.example.registerapplication.R;

import java.util.List;

public class InterestsNoteAdapter extends BaseAdapter {
    private Context context;
    private List<InterestsItem> dataList;
    private int selectedPosition = -1;

    public InterestsNoteAdapter(Context context, List<InterestsItem> dataList) {
        this.context = context;
        this.dataList = dataList;
    }



    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.note_interests_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.imageView);
            holder.textView = convertView.findViewById(R.id.textView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        InterestsItem item = dataList.get(position);
        holder.imageView.setImageResource(item.getImageResId());
        holder.textView.setText(item.getText());

        // 设置选中状态
        if (position == selectedPosition) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.grey9));
        } else {
            convertView.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        }



        return convertView;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return selectedPosition;

    }

    private static class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}