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

public class CustomList extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> arraySourceAcc;
    private final ArrayList<String> arrayDestinationAcc;
    private final ArrayList<String> arrayName;
    private final ArrayList<String> arrayAmount;
    private final ArrayList<String> arrayCaption;
    private final ArrayList<String> arrayFunctionType;

    public CustomList(Activity context,
                      ArrayList<String> sourceAcc,
                      ArrayList<String> destinationAcc,
                      ArrayList<String> name,
                      ArrayList<String> amount,
                      ArrayList<String> caption,
                      ArrayList<String> functionType) {
        super(context, R.layout.list_single_item_new_v2, sourceAcc);
        this.context = context;
        this.arraySourceAcc = sourceAcc;
        this.arrayDestinationAcc = destinationAcc;
        this.arrayName = name;
        this.arrayAmount = amount;
        this.arrayCaption = caption;
        this.arrayFunctionType = functionType;

    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_single_item_new_v2, null, true);
        TextView textViewSourceWallet = rowView.findViewById(R.id.textViewSourceAccount);
        textViewSourceWallet.setText(arraySourceAcc.get(position));
        TextView textViewDestinationWallet = rowView.findViewById(R.id.textViewDestinationAccount);
        textViewDestinationWallet.setText(arrayDestinationAcc.get(position));
        TextView textViewDestinationWalletHolderName = rowView.findViewById(R.id.textViewName);
        textViewDestinationWalletHolderName.setText(arrayName.get(position));
        TextView textViewAmount = rowView.findViewById(R.id.textViewAmount);
        textViewAmount.setText(arrayAmount.get(position));
        TextView textViewCaption = rowView.findViewById(R.id.textViewCaption);
        textViewCaption.setText(arrayCaption.get(position));
        TextView textViewTransactionType = rowView.findViewById(R.id.textViewFunctionType);
        if (arrayFunctionType.get(position).equalsIgnoreCase("CFT")) {
            textViewTransactionType.setText("Customer Fund Transfer");
        }
        else if (arrayFunctionType.get(position).equalsIgnoreCase("CMP")) {
            textViewTransactionType.setText("Customer Merchant Payment");
        }
        else if (arrayFunctionType.get(position).equalsIgnoreCase("MFM")) {
            textViewTransactionType.setText("Merchant Fund Management");
        }

        return rowView;
    }

}
