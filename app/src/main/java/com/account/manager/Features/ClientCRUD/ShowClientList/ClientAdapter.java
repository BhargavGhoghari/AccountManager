/*
 * *
 *  * Created by Bhargav Ghoghari on 11/5/19 5:10 PM
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 9/17/19 3:10 PM
 *
 */

package com.account.manager.Features.ClientCRUD.ShowClientList;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.account.manager.Database.DatabaseQueryClass;
import com.account.manager.Features.ClientCRUD.CreateClient.Client;
import com.account.manager.Features.ClientCRUD.UpdateClientInfo.ClientUpdate;
import com.account.manager.Features.ClientCRUD.UpdateClientInfo.ClientUpdateListener;
import com.account.manager.Features.ProductCRUD.ShowProductList.ProductListActivity;
import com.account.manager.MainActivity;
import com.account.manager.R;
import com.account.manager.Util.Config;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.List;

public class ClientAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    private Context context;
    private List<Client> clientList;
    private DatabaseQueryClass databaseQueryClass;

    public ClientAdapter(Context context, List<Client> clientList) {
        this.context = context;
        this.clientList = clientList;
        databaseQueryClass = new DatabaseQueryClass(context);
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_client, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final int itemPosition = position;
        final Client student = clientList.get(position);

        holder.client_name.setText(student.getName());
        holder.client_re_no.setText(String.valueOf(student.getRegistrationNumber()));
        holder.client_phone_no.setText("+91 "+student.getPhoneNumber());

        holder.client_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Are you sure, You wanted to delete this student?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                deleteStudent(itemPosition);
                            }
                        });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        holder.client_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClientUpdate clientUpdate = ClientUpdate.newInstance(student.getRegistrationNumber(), itemPosition, new ClientUpdateListener() {
                    @Override
                    public void onClientInfoUpdated(Client student, int position) {
                        clientList.set(position, student);
                        notifyDataSetChanged();
                    }
                });
                clientUpdate.show(((MainActivity) context).getSupportFragmentManager(), Config.UPDATE_CLIENT);
            }
        });

        holder.client_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_CALL);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setData(Uri.parse("tel:"+student.getPhoneNumber()));
                if (ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                context.startActivity(i);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Config.CLIENT_REGISTRATION, student.getRegistrationNumber());
                intent.putExtra(Config.COLUMN_CLIENT_NAME, student.getName());
                context.startActivity(intent);
            }
        });
    }

    private void deleteStudent(int position) {
        Client student = clientList.get(position);
        long count = databaseQueryClass.deleteClientByRegNum(student.getRegistrationNumber());

        if(count>0){
            clientList.remove(position);
            notifyDataSetChanged();
            ((MainActivity) context).viewVisibility();
            Toast.makeText(context, "Customer deleted successfully", Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(context, "Customer not deleted. Something wrong!", Toast.LENGTH_LONG).show();

    }

    @Override
    public int getItemCount() {
        return clientList.size();
    }
}
