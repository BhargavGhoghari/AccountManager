/*
 * *
 *  * Created by Bhargav Ghoghari on 11/5/19 5:11 PM
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 9/17/19 2:39 PM
 *
 */

package com.account.manager.Features.ProductCRUD.ShowProductList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.account.manager.Database.DatabaseQueryClass;
import com.account.manager.Features.ProductCRUD.CreareProduct.Product;
import com.account.manager.Features.ProductCRUD.CreareProduct.ProductCreate;
import com.account.manager.Features.ProductCRUD.CreareProduct.ProductCreateListener;
import com.account.manager.R;
import com.account.manager.Util.Config;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity implements ProductCreateListener {

    private long clientRegNo;
    private DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(this);
    private List<Product> productList = new ArrayList<>();
    private RecyclerView productRecyclerView;
    private TextView pEmpty,ptv_client_name;
    private FloatingActionButton productFB;
    private ImageView pdelete_alldata;
    private ProductAdapter productAdapter;
    private String clientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        ptv_client_name = findViewById(R.id.ptv_client_name);
        pdelete_alldata = findViewById(R.id.pdelete_alldata);
        pEmpty = findViewById(R.id.pEmpty);
        productRecyclerView = findViewById(R.id.productRecyclerView);
        productFB = findViewById(R.id.productFB);

        clientRegNo = getIntent().getLongExtra(Config.CLIENT_REGISTRATION, -1);
        clientName = getIntent().getStringExtra(Config.COLUMN_CLIENT_NAME);
        ptv_client_name.setText(clientName);

        productList.addAll(databaseQueryClass.getAllProductsByRegNo(clientRegNo));

        productAdapter = new ProductAdapter(this, productList);
        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        productRecyclerView.setLayoutManager(manager);
        productRecyclerView.setAdapter(productAdapter);

        viewVisibility();

        productFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProductCreateDialog();
            }
        });

        pdelete_alldata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProductListActivity.this);
                alertDialogBuilder.setMessage("Are you sure, You wanted to delete all products?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                boolean isAllDeleted = databaseQueryClass.deleteAllProductsByRegNum(clientRegNo);
                                if(isAllDeleted){
                                    productList.clear();
                                    productAdapter.notifyDataSetChanged();
                                    viewVisibility();
                                }
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
    }

    private void openProductCreateDialog() {
        ProductCreate productCreate = ProductCreate.newInstance(clientRegNo, this);
        productCreate.show(getSupportFragmentManager(), Config.CREATE_PRODUCT);
    }

    @Override
    public void onProductCreated(Product product) {
        productList.add(product);
        productAdapter.notifyDataSetChanged();
        viewVisibility();
    }

    public void viewVisibility() {
        if(productList.isEmpty())
            pEmpty.setVisibility(View.VISIBLE);
        else
            pEmpty.setVisibility(View.GONE);
    }
}
