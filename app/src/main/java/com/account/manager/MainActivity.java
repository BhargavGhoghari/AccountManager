/*
 * *
 *  * Created by Bhargav Ghoghari on 11/5/19 5:11 PM
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 9/17/19 4:08 PM
 *
 */

package com.account.manager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.account.manager.Database.DatabaseQueryClass;
import com.account.manager.Features.ClientCRUD.CreateClient.Client;
import com.account.manager.Features.ClientCRUD.CreateClient.ClientCreate;
import com.account.manager.Features.ClientCRUD.CreateClient.ClientCreateListener;
import com.account.manager.Features.ClientCRUD.ShowClientList.ClientAdapter;
import com.account.manager.Util.Config;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ClientCreateListener {

    private ImageView delete_alldata,search_alldata;
    private RecyclerView rv_client;
    private TextView empty_list;
    private FloatingActionButton fab;

    private DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(this);
    private List<Client> clientList = new ArrayList<>();

    private ClientAdapter clientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        delete_alldata = findViewById(R.id.delete_alldata);
        search_alldata = findViewById(R.id.search_alldata);
        rv_client = findViewById(R.id.rv_client);
        empty_list = findViewById(R.id.empty_list);
        fab = findViewById(R.id.fab);

        clientList.addAll(databaseQueryClass.getAllClients());

        /*String json = new Gson().toJson(clientList);
        Log.d("arr_strJson", "onCreate: "+json);*/

        clientAdapter = new ClientAdapter(this, clientList);
        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        rv_client.setLayoutManager(manager);
        rv_client.setAdapter(clientAdapter);

        viewVisibility();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStudentCreateDialog();
            }
        });

        search_alldata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        delete_alldata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setMessage("Are you sure, You wanted to delete all customers?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                boolean isAllDeleted = databaseQueryClass.deleteAllClients();
                                if (isAllDeleted) {
                                    clientList.clear();
                                    clientAdapter.notifyDataSetChanged();
                                    viewVisibility();
                                }
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }


    public void viewVisibility() {
        if (clientList.isEmpty())
            empty_list.setVisibility(View.VISIBLE);
        else
            empty_list.setVisibility(View.GONE);
    }

    private void openStudentCreateDialog() {
        ClientCreate clientCreate = ClientCreate.newInstance("Create Customer", this);
        clientCreate.show(getSupportFragmentManager(), Config.CREATE_CLIENT);
    }


    @Override
    public void onClientCreated(Client client) {
        clientList.add(client);
        clientAdapter.notifyDataSetChanged();
        viewVisibility();
        Logger.d(client.getName());
    }
}
