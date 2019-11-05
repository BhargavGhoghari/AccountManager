/*
 * *
 *  * Created by Bhargav Ghoghari on 11/5/19 5:10 PM
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 9/9/19 5:36 PM
 *
 */

package com.account.manager.Features.ClientCRUD.UpdateClientInfo;

import com.account.manager.Features.ClientCRUD.CreateClient.Client;

public interface ClientUpdateListener {
    void onClientInfoUpdated(Client client, int position);
}
