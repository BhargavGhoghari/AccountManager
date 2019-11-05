/*
 * *
 *  * Created by Bhargav Ghoghari on 11/5/19 5:10 PM
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 9/10/19 9:59 AM
 *
 */

package com.account.manager.Features.ClientCRUD.ShowClientList;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.account.manager.R;


public class CustomViewHolder extends RecyclerView.ViewHolder {


    TextView client_re_no;
    TextView client_name;
    TextView client_phone_no;
    ImageView client_edit;
    ImageView client_delete;
    ImageView client_call;

    public CustomViewHolder(View itemView) {
        super(itemView);

        client_re_no = itemView.findViewById(R.id.client_re_no);
        client_name = itemView.findViewById(R.id.client_name);
        client_phone_no = itemView.findViewById(R.id.client_phone_no);
        client_edit = itemView.findViewById(R.id.client_edit);
        client_delete = itemView.findViewById(R.id.client_delete);
        client_call = itemView.findViewById(R.id.client_call);
    }
}
