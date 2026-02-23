package com.jiraphat.myinventory

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "Inventory.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_PRODUCTS = "products"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_PRICE = "price"
        private const val COLUMN_QUANTITY = "quantity"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE " + TABLE_PRODUCTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_PRICE + " REAL,"
                + COLUMN_QUANTITY + " INTEGER" + ")")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        onCreate(db)
    }

    fun addProduct(product: Product): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, product.name)
        values.put(COLUMN_PRICE, product.price)
        values.put(COLUMN_QUANTITY, product.quantity)
        val id = db.insert(TABLE_PRODUCTS, null, values)
        db.close()
        return id
    }

    fun getAllProducts(): List<Product> {
        val productList = mutableListOf<Product>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_PRODUCTS", null)

        if (cursor.moveToFirst()) {
            do {
                val product = Product(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY))
                )
                productList.add(product)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return productList
    }

    fun updateProduct(product: Product): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, product.name)
        values.put(COLUMN_PRICE, product.price)
        values.put(COLUMN_QUANTITY, product.quantity)

        val success = db.update(TABLE_PRODUCTS, values, "$COLUMN_ID=?", arrayOf(product.id.toString()))
        db.close()
        return success
    }

    fun deleteProduct(id: Int): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_PRODUCTS, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
        return success
    }
}
