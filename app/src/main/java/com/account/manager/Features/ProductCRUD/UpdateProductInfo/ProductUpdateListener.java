/*
 * *
 *  * Created by Bhargav Ghoghari on 11/5/19 5:11 PM
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 9/17/19 2:17 PM
 *
 */

package com.account.manager.Features.ProductCRUD.UpdateProductInfo;

import com.account.manager.Features.ProductCRUD.CreareProduct.Product;

public interface ProductUpdateListener {
    void onProductInfoUpdate(Product product, int position);
}
