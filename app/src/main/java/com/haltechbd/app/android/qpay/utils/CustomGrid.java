package com.haltechbd.app.android.qpay.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haltechbd.app.android.qpay.R;

public class CustomGrid extends BaseAdapter {
    private Context mContext;
    private final int[] gridViewImgId;
    private final String[] gridViewLabel;

    public CustomGrid(Context con, String[] label, int[] imgId) {
        mContext = con;
        this.gridViewImgId = imgId;
        this.gridViewLabel = label;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return gridViewLabel.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_single_item_new_with_card_view, null);
            ImageView imageView = grid.findViewById(R.id.gridViewImg);
            TextView textView = grid.findViewById(R.id.gridViewTextLabel);
            imageView.setImageResource(gridViewImgId[position]);
            textView.setText(gridViewLabel[position]);
        } else {
            grid = convertView;
        }

        return grid;
    }
}
