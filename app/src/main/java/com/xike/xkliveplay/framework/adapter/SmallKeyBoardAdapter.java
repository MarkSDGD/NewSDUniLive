package com.xike.xkliveplay.framework.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xike.xkliveplay.R;

import java.util.ArrayList;
import java.util.List;

public class SmallKeyBoardAdapter extends BaseAdapter {

    private final List<String> labels;
    private Context context;

    public SmallKeyBoardAdapter(Context context) {
        this.context = context;
        this.labels = new ArrayList();
        labels.add("1");
        labels.add("2");
        labels.add("3");
        labels.add("4");
        labels.add("5");
        labels.add("6");
        labels.add("7");
        labels.add("8");
        labels.add("9");
        labels.add("0");
        labels.add("删除");
        labels.add("确定");
    }
    @Override
    public int getCount() {
        return labels.size();
    }

    @Override
    public Object getItem(int position) {
        return labels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.small_key_text_layout, null);
            myViewHolder = new MyViewHolder(convertView);
            convertView.setTag(myViewHolder);
        }else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }
        myViewHolder.textView.setText(labels.get(position) + "");
        return convertView;
    }

    public class MyViewHolder {
        public TextView textView;
        public MyViewHolder(final View itemView) {
            textView = (TextView) itemView.findViewById(R.id.key_text);
        }

        @Override
        public String toString() {
            return textView.getText() + "";
        }
    }
}
