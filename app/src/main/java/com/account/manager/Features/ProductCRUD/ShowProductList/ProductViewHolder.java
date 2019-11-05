/*
 * *
 *  * Created by Bhargav Ghoghari on 11/5/19 5:11 PM
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 9/17/19 2:10 PM
 *
 */

package com.account.manager.Features.ProductCRUD.ShowProductList;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.account.manager.R;

public class ProductViewHolder extends RecyclerView.ViewHolder  {

    TextView tv_Product_Name;
    TextView tv_Product_Quantity;
    TextView tv_Product_Price;
    ImageView iv_Product_Edit;
    ImageView iv_Product_Delete;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        tv_Product_Name = itemView.findViewById(R.id.tv_Product_Name);
        tv_Product_Quantity = itemView.findViewById(R.id.tv_Product_Quantity);
        tv_Product_Price = itemView.findViewById(R.id.tv_Product_Price);
        iv_Product_Edit = itemView.findViewById(R.id.iv_Product_Edit);
        iv_Product_Delete = itemView.findViewById(R.id.iv_Product_Delete);
    }
}
