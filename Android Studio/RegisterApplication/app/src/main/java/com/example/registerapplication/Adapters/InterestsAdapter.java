package com.example.registerapplication.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.registerapplication.Entity.Data.InterestsItem;
import com.example.registerapplication.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InterestsAdapter extends BaseAdapter {

    private Context context;
    private List<InterestsItem> dataList;
    private List<Boolean> selectedItems;
    private Set<String> originalInterests;

    public InterestsAdapter(Context context, List<InterestsItem> dataList) {
        this.context = context;
        this.dataList = dataList;
        selectedItems = new ArrayList<>();
        originalInterests = new HashSet<>();
        // 初始化选中状态列表，默认都为 false
        for (int i = 0; i < dataList.size(); i++) {
            selectedItems.add(false);
        }
    }



    public void setUserInterests(List<String> userInterests) {
        originalInterests = new HashSet<>(userInterests);
        for (int i = 0; i < dataList.size(); i++) {
            InterestsItem item = dataList.get(i);
            if (originalInterests.contains(item.getText())) {
                selectedItems.set(i, true);
            }
        }
        notifyDataSetChanged();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.note_interests_item, parent, false);
        }

        ViewGroup rootLayout = (ViewGroup) convertView;
        // 设置外边距，以下示例将上下左右外边距设为8dp
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(rootLayout.getLayoutParams());
        layoutParams.setMargins(8, 8, 8, 8);
        rootLayout.setLayoutParams(layoutParams);
        // 后续设置图片、文字等逻辑不变
        convertView.requestLayout();


        ImageView imageView = convertView.findViewById(R.id.imageView);
        TextView textView = convertView.findViewById(R.id.textView);

        InterestsItem itemData = dataList.get(position);

        // 设置图片和文字
        imageView.setImageResource(itemData.getImageResId());
        textView.setText(itemData.getText());

        boolean isOriginalInterest = originalInterests.contains(itemData.getText());
        boolean isSelected = selectedItems.get(position);
        Log.d("InterestsAdapter", "Position: " + position + ", isOriginalInterest: " + isOriginalInterest + ", isSelected: " + isSelected);

        Resources res = context.getResources();
        if (isOriginalInterest) {
            convertView.setBackgroundColor(res.getColor(android.R.color.darker_gray));
            textView.setTextColor(ColorStateList.valueOf(res.getColor(android.R.color.darker_gray)));
        } else if (isSelected) {
            convertView.setBackgroundColor(res.getColor(R.color.grey9));
        } else {
            convertView.setBackgroundColor(res.getColor(android.R.color.transparent));
        }

        // 将 convertView 和 textView 赋值给 final 变量
        final View finalConvertView = convertView;
        final TextView finalTextView = textView;

        // 点击事件，存入选中列表
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isOriginal = originalInterests.contains(dataList.get(position).getText());
                selectedItems.set(position,!selectedItems.get(position));
                if (isOriginal) {
                    // 如果是原本的兴趣，点击后取消选中恢复默认
                    selectedItems.set(position, false);
                    finalConvertView.setBackgroundColor(res.getColor(android.R.color.transparent));
                    finalTextView.setTextColor(ColorStateList.valueOf(res.getColor(android.R.color.black)));
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    // 获取选中的项的索引列表
    public List<Integer> getSelectedIndices() {
        List<Integer> selectedIndices = new ArrayList<>();
        for (int i = 0; i < selectedItems.size(); i++) {
            if (selectedItems.get(i)) {
                selectedIndices.add(i);
            }
        }
        return selectedIndices;
    }

    public void setItemSelected(int position, boolean isSelected) {
        if (position >= 0 && position < selectedItems.size()) {
            selectedItems.set(position, isSelected);
        }
    }

    // 获取选中的项的列表
    public List<InterestsItem> getSelectedItems() {
        List<InterestsItem> selectedItemList = new ArrayList<>();
        for (int i = 0; i < selectedItems.size(); i++) {
            if (selectedItems.get(i)) {
                selectedItemList.add(dataList.get(i));
            }
        }
        return selectedItemList;
    }
}