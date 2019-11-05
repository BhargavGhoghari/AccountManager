/*
 * *
 *  * Created by Bhargav Ghoghari on 11/5/19 5:10 PM
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 9/17/19 3:06 PM
 *
 */

package com.account.manager.Features.ProductCRUD.ShowProductList;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.account.manager.Database.DatabaseQueryClass;
import com.account.manager.Features.ProductCRUD.CreareProduct.Product;
import com.account.manager.Features.ProductCRUD.UpdateProductInfo.ProductUpdate;
import com.account.manager.Features.ProductCRUD.UpdateProductInfo.ProductUpdateListener;
import com.account.manager.R;
import com.account.manager.Util.Config;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder>  {

    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        final int listPosition = position;
        final Product product = productList.get(position);

        holder.tv_Product_Name.setText(product.getName());
        holder.tv_Product_Quantity.setText(String.valueOf(product.getQuantity()));
        holder.tv_Product_Price.setText("Rs. "+product.getPrice());

        holder.iv_Product_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Are you sure, You wanted to delete this product?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                deleteProduct(product);
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

        holder.iv_Product_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProduct(product.getId(), listPosition);
            }
        });

    }

    private void editProduct(long id, int listPosition) {
        ProductUpdate productUpdate = ProductUpdate.newInstance(id, listPosition, new ProductUpdateListener() {
            @Override
            public void onProductInfoUpdate(Product product, int position) {
                productList.set(position, product);
                notifyDataSetChanged();
            }
        });
        productUpdate.show(((ProductListActivity) context).getSupportFragmentManager(), Config.UPDATE_PRODUCT);
    }

    private void deleteProduct(Product product) {

        DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(context);
        boolean isDeleted = databaseQueryClass.deleteProductById(product.getId());

        if(isDeleted) {
            productList.remove(product);
            notifyDataSetChanged();
            ((ProductListActivity) context).viewVisibility();
        } else
            Toast.makeText(context, "Cannot delete!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
