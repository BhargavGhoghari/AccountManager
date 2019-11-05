/*
 * *
 *  * Created by Bhargav Ghoghari on 11/5/19 5:10 PM
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 9/17/19 3:08 PM
 *
 */

package com.account.manager.Features.ProductCRUD.CreareProduct;

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

public class ProductCreate extends DialogFragment {

    private static long clientRegistrationNumber;
    private static ProductCreateListener productCreateListener;

    private EditText productName;
    private EditText productQuantity;
    private EditText productPrice;
    private Button createProduct;
    private Button cancelProduct;

    public ProductCreate() {
    }

    public static ProductCreate newInstance(long clientRegNO, ProductCreateListener productListner){
        clientRegistrationNumber = clientRegNO;
        productCreateListener = productListner;

        ProductCreate productCreate = new ProductCreate();

        productCreate.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return productCreate;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_create_dialog, container, false);
        getDialog().setTitle(getResources().getString(R.string.add_new_product));

        productName = view.findViewById(R.id.productName);
        productQuantity = view.findViewById(R.id.productQuantity);
        productPrice = view.findViewById(R.id.productPrice);
        createProduct = view.findViewById(R.id.createProduct);
        cancelProduct = view.findViewById(R.id.cancelProduct);

        createProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(productName.getText().length()>0 && productQuantity.getText().length()>0 &&
                        productPrice.getText().length()>0)
                {
                    if(productName.getText().length()==0)
                    {
                        productName.setError("Field cannot be left blank.");
                    }else if(productQuantity.getText().length()==0)
                    {
                        productQuantity.setError("Field cannot be left blank.");
                    }else if(productQuantity.getText().length()==0)
                    {
                        productPrice.setError("Field cannot be left blank.");
                    }
                    String pName = productName.getText().toString();
                    int pQuantity = Integer.parseInt(productQuantity.getText().toString());
                    String pPrice = productPrice.getText().toString();

                    Product product = new Product(-1, pName, pQuantity, pPrice);

                    DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(getContext());

                    long id = databaseQueryClass.insertProduct(product, clientRegistrationNumber);

                    if(id>0){
                        product.setId(id);
                        productCreateListener.onProductCreated(product);
                        getDialog().dismiss();
                    }
                }else {
                    productName.setError("Field cannot be left blank.");
                    productQuantity.setError("Field cannot be left blank.");
                    productPrice.setError("Field cannot be left blank.");
                }
            }
        });

        cancelProduct.setOnClickListener(new View.OnClickListener() {
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
