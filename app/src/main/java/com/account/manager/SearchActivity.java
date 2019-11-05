/*
 * *
 *  * Created by Bhargav Ghoghari on 11/5/19 5:08 PM
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 9/17/19 4:24 PM
 *
 */

package com.account.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.account.manager.Database.DatabaseQueryClass;
import com.account.manager.Features.ClientCRUD.CreateClient.Client;
import com.account.manager.Features.ClientCRUD.ShowClientList.ClientAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    private ImageView search_back_btn;
    private EditText search_edt;
    private RecyclerView search_rv;
    private RelativeLayout nodata_rel;
    private TextView tv_nodata_2;
    ClientAdapter clientAdapter;

    private DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(this);
    private List<Client> clientList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search_edt = findViewById(R.id.search_edt);
        search_rv = findViewById(R.id.search_rv);
        search_back_btn = findViewById(R.id.search_back_btn);
        nodata_rel = findViewById(R.id.nodata_rel);
        tv_nodata_2 = findViewById(R.id.tv_nodata_2);

        clientList.addAll(databaseQueryClass.getAllClients());

        search_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        search_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                search_edt.setCursorVisible(true);
            }
        });
        search_edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // hide virtual keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(search_edt.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                return false;
            }
        });

        search_edt.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable editable) {
                // TODO Auto-generated method stub
                filter(editable.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence text, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
                filter(text.toString());
            }
        });
    }

    private void filter(String charText) {
        //new array list that will hold the filtered data
        ArrayList<Client> filterdNames = new ArrayList<>();
        ArrayList<Client> search_unique = new ArrayList<>();

        charText = charText.toLowerCase(Locale.getDefault());

        if (charText.length() == 0) {
            filterdNames.clear();
            search_rv.setVisibility(View.INVISIBLE);
            nodata_rel.setVisibility(View.VISIBLE);
            tv_nodata_2.setText("Please enter your customer name here!");
        }else {
            search_rv.setVisibility(View.VISIBLE);
            nodata_rel.setVisibility(View.GONE);

            try {
                for (Client pack : clientList) {
                    if (pack.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        filterdNames.add(pack);
                    }
                }
                search_unique = removeDuplicates(filterdNames);
                if (search_unique.size()== 0){
                    nodata_rel.setVisibility(View.VISIBLE);
                    tv_nodata_2.setText(getResources().getString(R.string.no_search));
                }

            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        clientAdapter = new ClientAdapter(this, search_unique);
        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        search_rv.setLayoutManager(manager);
        search_rv.setAdapter(clientAdapter);

    }

    private ArrayList<Client> removeDuplicates(ArrayList<Client> filterdNames) {

        // Store unique items in result.
        ArrayList<Client> result = new ArrayList<>();

        // Record encountered Strings in HashSet.
        HashSet<Client> set = new HashSet<>();


        // Loop over argument list.
        for (Client item : filterdNames) {
            // If String is not in set, add it to the list and the set.
            if (!set.contains(item)) {
                set.add(item);
                result.add(item);
            }
        }
        return result;
    }
}
