package com.haltechbd.app.android.qpay.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.haltechbd.app.android.qpay.R;

import java.util.ArrayList;

/**
 * @author Mr. Nazmuzzaman, Umme Sayma Bushra, Muhammad Sadat Al-Jony
 * @version 1.0
 * @company Bangladesh Microtechnology Limited
 * @since 2015-02-01
 */

public class CustomListForLinkedAccout extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> array01;
    private final ArrayList<String> array02;
    private final ArrayList<String> array03;

    public CustomListForLinkedAccout(Activity context,
                                     ArrayList<String> arr01,
                                     ArrayList<String> arr02,
                                     ArrayList<String> arr03) {
        super(context, R.layout.list_single_item_linked_account, arr01);
        this.context = context;
        this.array01 = arr01;
        this.array02 = arr02;
        this.array03 = arr03;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_single_item_linked_account, parent, false);
        TextView textView01 = rowView.findViewById(R.id.txt01);
        textView01.setText(array01.get(position));
        TextView textView02 = rowView.findViewById(R.id.txt02);
        textView02.setText(array02.get(position));
        TextView textView03 = rowView.findViewById(R.id.txt03);
        textView03.setText(array03.get(position));
        return rowView;
    }

}
