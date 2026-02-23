package com.jiraphat.myinventory

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddEditProductActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etPrice: EditText
    private lateinit var etQuantity: EditText
    private lateinit var btnSave: Button
    private lateinit var dbHelper: DatabaseHelper
    private var product: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_product)

        // กำหนดค่าเริ่มต้นให้กับ View
        etName = findViewById(R.id.etProductName)
        etPrice = findViewById(R.id.etProductPrice)
        etQuantity = findViewById(R.id.etProductQuantity)
        btnSave = findViewById(R.id.btnSave)
        dbHelper = DatabaseHelper(this)

        // ตรวจสอบว่าเป็นโหมดแก้ไขหรือเพิ่มข้อมูล
        product = intent.getSerializableExtra("PRODUCT") as? Product
        if (product != null) {
            supportActionBar?.title = "Edit Product"
            etName.setText(product!!.name)
            etPrice.setText(product!!.price.toString())
            etQuantity.setText(product!!.quantity.toString())
            btnSave.text = "Update Product"
        } else {
            supportActionBar?.title = "Add Product"
            // โฟกัสไปที่ช่องชื่อสินค้าเพื่อให้พร้อมพิมพ์ทันที
            etName.requestFocus()
        }

        btnSave.setOnClickListener {
            saveProduct()
        }
    }

    private fun saveProduct() {
        val name = etName.text.toString().trim()
        val priceStr = etPrice.text.toString().trim()
        val quantityStr = etQuantity.text.toString().trim()

        if (name.isEmpty() || priceStr.isEmpty() || quantityStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val price = priceStr.toDoubleOrNull() ?: 0.0
        val quantity = quantityStr.toIntOrNull() ?: 0

        if (product == null) {
            val newProduct = Product(name = name, price = price, quantity = quantity)
            val id = dbHelper.addProduct(newProduct)
            if (id > -1) {
                Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to add product", Toast.LENGTH_SHORT).show()
            }
        } else {
            val updatedProduct = Product(id = product!!.id, name = name, price = price, quantity = quantity)
            val rows = dbHelper.updateProduct(updatedProduct)
            if (rows > 0) {
                Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to update product", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
