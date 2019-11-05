/*
 * *
 *  * Created by Bhargav Ghoghari on 11/5/19 5:10 PM
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 9/10/19 10:20 AM
 *
 */

package com.account.manager.Features.ClientCRUD.UpdateClientInfo;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.account.manager.Database.DatabaseQueryClass;
import com.account.manager.Features.ClientCRUD.CreateClient.Client;
import com.account.manager.R;
import com.account.manager.Util.Config;

public class ClientUpdate extends DialogFragment {

    private static long clientRegNo;
    private static int clientItemPosition;
    private static ClientUpdateListener clientUpdateListener;

    private Client client;

    private EditText edt_update_name;
    private EditText edt_update_no;
    private EditText edt_update_phone;
    private Button btn_update;
    private Button btn_update_cancel;

    private String nameString = "";
    private long registrationNumber = -1;
    private String phoneString = "";

    private DatabaseQueryClass databaseQueryClass;

    public ClientUpdate() {
        // Required empty public constructor
    }

    public static ClientUpdate newInstance(long registrationNumber, int position, ClientUpdateListener listener){
        clientRegNo = registrationNumber;
        clientItemPosition = position;
        clientUpdateListener = listener;
        ClientUpdate clientUpdate = new ClientUpdate();
        Bundle args = new Bundle();
        args.putString("title", "Update customer information");
        clientUpdate.setArguments(args);

        clientUpdate.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return clientUpdate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.client_update_dialog, container, false);

        databaseQueryClass = new DatabaseQueryClass(getContext());

        edt_update_name = view.findViewById(R.id.edt_update_name);
        edt_update_no = view.findViewById(R.id.edt_update_no);
        edt_update_phone = view.findViewById(R.id.edt_update_phone);
        btn_update = view.findViewById(R.id.btn_update);
        btn_update_cancel = view.findViewById(R.id.btn_update_cancel);

        String title = getArguments().getString(Config.TITLE);
        getDialog().setTitle(title);

        client = databaseQueryClass.getClientByRegNum(clientRegNo);

        if(client!=null){
            edt_update_name.setText(client.getName());
            edt_update_no.setText(String.valueOf(client.getRegistrationNumber()));
            edt_update_phone.setText(client.getPhoneNumber());

            btn_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nameString = edt_update_name.getText().toString();
                    registrationNumber = Integer.parseInt(edt_update_no.getText().toString());
                    phoneString = edt_update_phone.getText().toString();

                    client.setName(nameString);
                    client.setRegistrationNumber(registrationNumber);
                    client.setPhoneNumber(phoneString);

                    long id = databaseQueryClass.updateClientInfo(client);

                    if(id>0){
                        clientUpdateListener.onClientInfoUpdated(client, clientItemPosition);
                        getDialog().dismiss();
                    }
                }
            });

            btn_update_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getDialog().dismiss();
                }
            });

        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            //noinspection ConstantConditions
            dialog.getWindow().setLayout(width, height);
        }
    }
}
