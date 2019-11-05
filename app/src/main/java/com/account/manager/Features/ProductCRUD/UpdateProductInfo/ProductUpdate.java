/*
 * *
 *  * Created by Bhargav Ghoghari on 11/5/19 5:11 PM
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 9/17/19 3:01 PM
 *
 */

package com.account.manager.Features.ProductCRUD.UpdateProductInfo;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.account.manager.Database.DatabaseQueryClass;
import com.account.manager.Features.ProductCRUD.CreareProduct.Product;
import com.account.manager.R;
import com.account.manager.Util.Config;

public class ProductUpdate extends DialogFragment {

    private EditText productNameUp;
    private EditText productQuantityUp;
    private EditText productPriceUp;
    private Button pupdateButton;
    private Button pcancelButton;

    private static ProductUpdateListener productUpdateListener;
    private static long productId;
    private static int position;

    private DatabaseQueryClass databaseQueryClass;

    public ProductUpdate() {
    }

    public static ProductUpdate newInstance(long subId, int pos, ProductUpdateListener listener){
        productId = subId;
        position = pos;
        productUpdateListener = listener;

        ProductUpdate productUpdate = new ProductUpdate();
        Bundle args = new Bundle();
        args.putString("title", "Update product information");
        productUpdate.setArguments(args);

        productUpdate.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return productUpdate;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_update_dialog, container, false);

        productNameUp = view.findViewById(R.id.productNameUp);
        productQuantityUp = view.findViewById(R.id.productQuantityUp);
        productPriceUp = view.findViewById(R.id.productPriceUp);
        pupdateButton = view.findViewById(R.id.pupdateButton);
        pcancelButton = view.findViewById(R.id.pcancelButton);

        databaseQueryClass = new DatabaseQueryClass(getContext());

        String title = getArguments().getString(Config.TITLE);
        getDialog().setTitle(title);

        Product product = databaseQueryClass.getProductById(productId);

        productNameUp.setText(product.getName());
        productQuantityUp.setText(String.valueOf(product.getQuantity()));
        productPriceUp.setText(String.valueOf(product.getPrice()));

        pupdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pName = productNameUp.getText().toString();
                int pQuantity = Integer.parseInt(productQuantityUp.getText().toString());
                String pPrice = productPriceUp.getText().toString();

                Product product1 = new Product(productId, pName, pQuantity, pPrice);

                long rowCount = databaseQueryClass.updateProductInfo(product1);

                if(rowCount>0){
                    productUpdateListener.onProductInfoUpdate(product1, position);
                    getDialog().dismiss();
                }
            }
        });
        pcancelButton.setOnClickListener(new View.OnClickListener() {
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
