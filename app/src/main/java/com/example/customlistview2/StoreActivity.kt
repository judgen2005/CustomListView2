package com.example.customlistview2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.util.Locale

class StoreActivity : AppCompatActivity() {

    private lateinit var productName: EditText
    private lateinit var productPrice: EditText
    private lateinit var productDescription: EditText
    private lateinit var productImage: ImageView
    private lateinit var addProductButton: Button
    private lateinit var toolbar: Toolbar
    private lateinit var productListView: ListView
    private val productList = ArrayList<Product>()
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var selectedImageUri: Uri
    private var isImageSelected: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)
        productName = findViewById(R.id.productName)
        productPrice = findViewById(R.id.productPrice)
        productDescription = findViewById(R.id.productDescription)
        productImage = findViewById(R.id.productImage)
        addProductButton = findViewById(R.id.addProductButton)
        productListView = findViewById(R.id.productListView)
        val adapter = ProductAdapter(this, productList)
        productListView.adapter = adapter
        addProductButton.setOnClickListener {
            addProduct(adapter)
        }
        toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Добавление продукта"
        setSupportActionBar(toolbar)
        initActivityResultLauncher()
        productListView.setOnItemClickListener { parent, view, position, id ->
            val selectedProduct = productList[position]
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("PRODUCT", selectedProduct)
            startActivity(intent)
        }
    }

    private fun addProduct(adapter: ArrayAdapter<Product>) {
        val name = productName.text.toString()
        val price = productPrice.text.toString()
        val description = productDescription.text.toString()
        if (name.isNotEmpty() && price.isNotEmpty() && description.isNotEmpty() && isImageSelected) {
            val product = Product(name, price.toDouble(), selectedImageUri, description)
            productList.add(product)
            adapter.notifyDataSetChanged()
            productName.text.clear()
            productPrice.text.clear()
            productDescription.text.clear()
            productImage.setImageResource(R.drawable.ic_add_photo)
            isImageSelected = false
        } else {
            Toast.makeText(
                this,
                "Введите все данные и выберите изображение",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun initActivityResultLauncher() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.data?.let { uri ->
                        productImage.setImageURI(uri)
                        selectedImageUri = uri
                        isImageSelected = true
                    }
                }
            }
    }

    fun selectImage(@Suppress("UNUSED_PARAMETER")view: View) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activityResultLauncher.launch(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_store, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_exit -> {
                finishAffinity()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}

class ProductAdapter(context: Context, dataSource: ArrayList<Product>) :
    ArrayAdapter<Product>(context, R.layout.list_item_product, dataSource) {
    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: inflater.inflate(R.layout.list_item_product, parent, false)
        val productName = view.findViewById<TextView>(R.id.productNameTextView)
        val productPrice = view.findViewById<TextView>(R.id.productPriceTextView)
        val productImage = view.findViewById<ImageView>(R.id.productImageView)
        val product = getItem(position)
        productName.text = product?.name
        productPrice.text = String.format(Locale.ROOT, "%.2f", product?.price)
        productImage.setImageURI(product?.imageUri)
        return view
    }
}