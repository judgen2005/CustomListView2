package com.example.customlistview2

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.util.Locale

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var detailImageView: ImageView
    private lateinit var detailProductName: TextView
    private lateinit var detailProductPrice: TextView
    private lateinit var detailProductDescription: TextView
    private lateinit var toolbar: Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        @Suppress("DEPRECATION") val product = intent.getParcelableExtra<Product>("PRODUCT")
        detailImageView = findViewById(R.id.detailImageView)
        detailProductName = findViewById(R.id.detailProductName)
        detailProductPrice = findViewById(R.id.detailProductPrice)
        detailProductDescription = findViewById(R.id.detailProductDescription)
        toolbar = findViewById(R.id.toolbar)
        toolbar.title = "О продукте"
        setSupportActionBar(toolbar)

        if (product != null) {
            detailImageView.setImageURI(product.imageUri)
            detailProductName.text = product.name
            detailProductPrice.text =
                getString(
                    R.string.product_price_view_detail,
                    String.format(Locale.ROOT, "%.2f", product.price)
                )
            detailProductDescription.text = product.description
        }
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