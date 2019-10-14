package com.haltechbd.app.android.qpay.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.haltechbd.app.android.qpay.R;


public class FragmentExchangeTicket extends Fragment implements View.OnClickListener {
    private ListView mListView;
    private ImageButton mImgBtn;

    public FragmentExchangeTicket() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exchange, container, false);
        mListView = rootView.findViewById(R.id.listViewExchange);
        mImgBtn=rootView.findViewById(R.id.imgBtnAddNewExchange);
        mImgBtn.setOnClickListener(this);
        String[] items = new String[] {"Item 1", "Item 2", "Item 3"};
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);

        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                Toast.makeText(getActivity(), ((TextView) view).getText(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getActivity(),"Exchange Ticket",Toast.LENGTH_SHORT).show();
    }
}
