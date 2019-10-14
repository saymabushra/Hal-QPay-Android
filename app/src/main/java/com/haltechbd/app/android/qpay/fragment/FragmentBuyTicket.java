package com.haltechbd.app.android.qpay.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.haltechbd.app.android.qpay.ComingSoon;
import com.haltechbd.app.android.qpay.R;
import com.haltechbd.app.android.qpay.Recharge_Menu;
import com.haltechbd.app.android.qpay.utils.CustomGrid;


public class FragmentBuyTicket extends Fragment implements View.OnClickListener {
    private ListView mListView;
    private ImageButton mImgBtn;
    private GridView mGridView;
    public FragmentBuyTicket() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_buy, container, false);
//        mListView = rootView.findViewById(R.id.listViewBuy);
//        mImgBtn=rootView.findViewById(R.id.imgBtnAddNewBuy);
        mGridView =rootView.findViewById(R.id.gridViewBuyTicketmenu);
//        mImgBtn.setOnClickListener(this);
//        String[] items = new String[] {"Item 1", "Item 2", "Item 3"};
//        ArrayAdapter<String> adapter =
//                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);
//
//        mListView.setAdapter(adapter);
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                // When clicked, show a toast with the TextView text
//                Toast.makeText(getActivity(), ((TextView) view).getText(),
//                        Toast.LENGTH_SHORT).show();
//            }
//        });

        initServiceMenu();
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v==mImgBtn){
            Toast.makeText(getActivity(),"Buy Ticket",Toast.LENGTH_SHORT).show();
        }
    }

    private void initServiceMenu()
    {
        final String[] mStrArrayGridViewItemLabel = {
                "Air Ticket",
                "Bus Ticket"
        };
        int[] mIntGridViewItemImgId = {
                R.drawable.buyairticket,
                R.drawable.buybusticket

        };
        CustomGrid gridAdapter = new CustomGrid(getActivity(), mStrArrayGridViewItemLabel,
                mIntGridViewItemImgId);

        mGridView.setAdapter(gridAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mStrArrayGridViewItemLabel[+position].equals("Air Ticket")) {
                    startActivity(new Intent(getActivity(), ComingSoon.class));
                }

                else if (mStrArrayGridViewItemLabel[+position].equals("Bus Ticket"))
                {
                    startActivity(new Intent(getActivity(), ComingSoon.class));
                }



            }

        });
    }
}
