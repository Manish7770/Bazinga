package com.manish.bazingalnmiit.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.manish.bazingalnmiit.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper{

    private static final String DB_NAME="BazingaDB.db";
    private static final int DB_VER=1;
    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<Order> getCarts(String uid)
    {
        SQLiteDatabase db=getReadableDatabase();
        SQLiteQueryBuilder qb=new SQLiteQueryBuilder();

        String[] sqlSelect={"PackingCharge","Price","ProductId","ProductName","Quantity","UId"};
        String sqlTable="OrderDetail";

        qb.setTables(sqlTable);
        Cursor c=qb.query(db,sqlSelect,"UId=?",new String[]{uid},null,null,null);

        final List<Order> result =new ArrayList<>();

        if(c.moveToFirst())
        {
            do {
                result.add(new Order(Long.parseLong(c.getString(c.getColumnIndex("PackingCharge"))),
                                    Long.parseLong(c.getString(c.getColumnIndex("Price"))),
                                    c.getString(c.getColumnIndex("ProductId")),
                                    c.getString(c.getColumnIndex("ProductName")),
                                    c.getString(c.getColumnIndex("Quantity")),
                                    c.getString(c.getColumnIndex("UId"))
                                    ));
            }while (c.moveToNext());
        }
        return result;
    }

    public void addToCart(Order order)
    {
        SQLiteDatabase db=getReadableDatabase();
        String query= String.format("INSERT OR REPLACE INTO OrderDetail(PackingCharge,Price,ProductId,ProductName,Quantity,UId) VALUES('%s','%s','%s','%s','%s','%s');",
                order.getPackingCharge(),
                order.getPrice(),
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getUid());
        db.execSQL(query);
    }

    public void cleanCart(String uid)
    {
        SQLiteDatabase db=getReadableDatabase();
        String query= String.format("DELETE FROM OrderDetail WHERE UId='%s'",uid);
        db.execSQL(query);
    }


    public void removeFromCart(String productId,String uid) {

        SQLiteDatabase db=getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail WHERE UId='%s' AND ProductId='%s'",uid,productId);
        db.execSQL(query);
    }

    public void updateCart(Order order) {
        SQLiteDatabase db=getReadableDatabase();
        String query = String.format("UPDATE OrderDetail SET Quantity='%s' WHERE UId= '%s' AND ProductId='%s'",order.getQuantity(),order.getUid(),order.getProductId());
        db.execSQL(query);
    }

    public int getCountCart(String uid) {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT COUNT(*) FROM OrderDetail WHERE UId='%s'",uid);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                count = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return count;
    }
}
