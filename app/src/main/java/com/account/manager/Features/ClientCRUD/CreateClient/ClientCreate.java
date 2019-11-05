/*
 * *
 *  * Created by Bhargav Ghoghari on 11/5/19 5:10 PM
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 9/17/19 3:10 PM
 *
 */

package com.account.manager.Features.ClientCRUD.CreateClient;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.account.manager.Database.DatabaseQueryClass;
import com.account.manager.R;
import com.account.manager.Util.Config;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

public class ClientCreate extends DialogFragment {

    private static ClientCreateListener clientCreateListener;

    private EditText nameEditText;
    private EditText registrationEditText;
    private EditText phoneEditText;
    private Button createButton;
    private Button cancelButton;

    private String nameString = "";
    private long registrationNumber = -1;
    private String phoneString = "";

    //defining AwesomeValidation object
    private AwesomeValidation awesomeValidation;

    public ClientCreate() {
        // Required empty public constructor
    }

    public static ClientCreate newInstance(String title, ClientCreateListener listener){
        clientCreateListener = listener;
        ClientCreate clientCreate = new ClientCreate();
        Bundle args = new Bundle();
        args.putString("title", title);
        clientCreate.setArguments(args);

        clientCreate.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return clientCreate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.client_create_dialog, container, false);

        //initializing awesomevalidation object
        /*
         * The library provides 3 types of validation
         * BASIC
         * COLORATION
         * UNDERLABEL
         * */
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        nameEditText = view.findViewById(R.id.studentNameEditText);
        registrationEditText = view.findViewById(R.id.registrationEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
        createButton = view.findViewById(R.id.createButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        String title = getArguments().getString(Config.TITLE);
        getDialog().setTitle(title);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(registrationEditText.getText().length()>0 ||nameEditText.getText().length()>0 ||
                        phoneEditText.getText().length()>0)
                {
                    nameString = nameEditText.getText().toString();
                    registrationNumber = Integer.parseInt(registrationEditText.getText().toString());
                    phoneString = phoneEditText.getText().toString();

                    if(nameEditText.getText().length()==0)
                    {
                        nameEditText.setError("Field cannot be left blank.");
                    }else if(registrationEditText.getText().length()==0)
                    {
                        registrationEditText.setError("Field cannot be left blank.");
                    }else if(phoneEditText.getText().length()==0)
                    {
                        phoneEditText.setError("Field cannot be left blank.");
                    }

                    awesomeValidation.addValidation(phoneEditText,"^[2-9]{2}[0-9]{8}$", phoneString);

                    if (awesomeValidation.validate()) {
                        Client client = new Client(-1, nameString, registrationNumber,phoneString);

                        DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(getContext());

                        long id = databaseQueryClass.insertClient(client);

                        if(id>0){
                            client.setId(id);
                            clientCreateListener.onClientCreated(client);
                            getDialog().dismiss();
                        }
                    }
                    else {
                        phoneEditText.setError("Please enter valid number.");
                    }


                }else {
                    nameEditText.setError("Field cannot be left blank.");
                    registrationEditText.setError("Field cannot be left blank.");
                    phoneEditText.setError("Field cannot be left blank.");
                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

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
