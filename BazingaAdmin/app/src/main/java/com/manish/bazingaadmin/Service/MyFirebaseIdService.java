package com.manish.bazingaadmin.Service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.manish.bazingaadmin.Model.Token;

public class MyFirebaseIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String tokenRefreshed = FirebaseInstanceId.getInstance().getToken();
        updateTokenToFirebase(tokenRefreshed);
    }

    private void updateTokenToFirebase(String tokenRefreshed) {

        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference tokens=database.getReference("Tokens");

        Token token=new Token(tokenRefreshed,true);
        //false is client side
        tokens.child("ADMIN").setValue(token);
    }
}
