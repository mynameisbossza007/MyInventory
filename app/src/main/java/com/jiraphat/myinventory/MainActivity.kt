package com.jiraphat.myinventory

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var rvProducts: RecyclerView
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvProducts = findViewById(R.id.rvProducts)
        fabAdd = findViewById(R.id.fabAdd)
        dbHelper = DatabaseHelper(this)

        adapter = ProductAdapter(
            emptyList(),
            onItemClick = { product ->
                val intent = Intent(this, AddEditProductActivity::class.java)
                intent.putExtra("PRODUCT", product)
                startActivity(intent)
            },
            onDeleteClick = { product ->
                showDeleteConfirmation(product)
            }
        )

        rvProducts.layoutManager = LinearLayoutManager(this)
        rvProducts.adapter = adapter

        fabAdd.setOnClickListener {
            val intent = Intent(this, AddEditProductActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        refreshProductList()
    }

    private fun refreshProductList() {
        val products = dbHelper.getAllProducts()
        adapter.updateData(products)
    }

    private fun showDeleteConfirmation(product: Product) {
        AlertDialog.Builder(this)
            .setTitle("Delete Product")
            .setMessage("Are you sure you want to delete ${product.name}?")
            .setPositiveButton("Delete") { _, _ ->
                val rows = dbHelper.deleteProduct(product.id)
                if (rows > 0) {
                    Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show()
                    refreshProductList()
                } else {
                    Toast.makeText(this, "Failed to delete product", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
