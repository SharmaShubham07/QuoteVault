package com.example.brewappsassignment.ui.share

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import com.example.brewappsassignment.databinding.ActivityShareCardBinding
import java.io.File
import java.io.FileOutputStream
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.brewappsassignment.R

class ShareCardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShareCardBinding

    private var styleName = "Modern Blue"
    private var quoteText = ""
    private var quoteAuthor = ""
    private var quoteCategory = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShareCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        receiveDataFromIntent()
        setInitialValues()
        setupListeners()
    }

    // ---------------------------------------------------------------------
    // GET QUOTE DATA FROM HOME SCREEN
    // ---------------------------------------------------------------------
    private fun receiveDataFromIntent() {
        quoteText = intent.getStringExtra("QUOTE_TEXT") ?: ""
        quoteAuthor = intent.getStringExtra("QUOTE_AUTHOR") ?: ""
        quoteCategory = intent.getStringExtra("QUOTE_CATEGORY") ?: ""
    }

    private fun setInitialValues() {
        binding.tvQuote.text = quoteText
        binding.tvAuthor.text = "— $quoteAuthor"
        binding.tvCategory.text = quoteCategory.uppercase()
        applyStyleModernBlue()  // default
    }

    // ---------------------------------------------------------------------
    // STYLE CLICK LISTENERS
    // ---------------------------------------------------------------------
    private fun setupListeners() {

        // Main buttons
        binding.btnChooseStyle.setOnClickListener { openStyleSelector() }
        binding.btnSave.setOnClickListener { saveCardAsImage() }
        binding.btnShare.setOnClickListener { shareCardImage() }

        // Preview 1: Modern Blue
        binding.stylePreview1.setOnClickListener {
            applyStyleModernBlue()
            showToast("Style Applied: Modern Blue")
        }

        // Preview 2: Sunset Gradient
        binding.stylePreview2.setOnClickListener {
            applyStyleSunset()
            showToast("Style Applied: Sunset")
        }

        // Preview 3: Minimal Dark
        binding.stylePreview3.setOnClickListener {
            applyStyleDark()
            showToast("Style Applied: Minimal Dark")
        }

        // Preview 4: Nature Green
        binding.stylePreview4.setOnClickListener {
            applyStyleNature()
            showToast("Style Applied: Nature")
        }
    }

    // ---------------------------------------------------------------------
    // STYLE METHODS
    // ---------------------------------------------------------------------
    private fun applyStyleModernBlue() {
        styleName = "Modern Blue"
        binding.cardPreview.setCardBackgroundColor(getColor(R.color.card_style_1))
    }

    private fun applyStyleSunset() {
        styleName = "Sunset"
        binding.cardPreview.setBackgroundResource(R.drawable.bg_gradient_sunset)
    }

    private fun applyStyleDark() {
        styleName = "Minimal Dark"
        binding.cardPreview.setCardBackgroundColor(getColor(R.color.card_style_dark))
    }

    private fun applyStyleNature() {
        styleName = "Nature"
        binding.cardPreview.setBackgroundResource(R.drawable.bg_gradient_nature)
    }

    // ---------------------------------------------------------------------
    // BOTTOMSHEET STYLE SELECTOR (OPTIONAL)
    // ---------------------------------------------------------------------
    private fun openStyleSelector() {
        val sheet = ShareStyleBottomSheet(this) { style ->
            when (style) {
                1 -> applyStyleModernBlue()
                2 -> applyStyleSunset()
                3 -> applyStyleDark()
            }
        }
        sheet.show()
    }

    // ---------------------------------------------------------------------
    // GENERATE BITMAP OF QUOTE CARD
    // ---------------------------------------------------------------------
    private fun createBitmapFromView(): Bitmap {
        val view = binding.cardPreview

        val bmp = Bitmap.createBitmap(
            view.width,
            view.height,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bmp)
        view.draw(canvas)
        return bmp
    }

    // ---------------------------------------------------------------------
    // SAVE IMAGE TO STORAGE
    // ---------------------------------------------------------------------
    private fun saveCardAsImage() {
        val bitmap = createBitmapFromView()

        val dir = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "QuoteCards")
        dir.mkdirs()

        val file = File(dir, "quote_${System.currentTimeMillis()}.jpg")

        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
        }

        showToast("Saved to: ${file.path}")
    }

    // ---------------------------------------------------------------------
    // SHARE IMAGE
    // ---------------------------------------------------------------------
    private fun shareCardImage() {
        val bitmap = createBitmapFromView()

        val file = File(cacheDir, "share_quote.jpg")
        FileOutputStream(file).use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, it)
        }

        val uri = FileProvider.getUriForFile(
            this,
            "$packageName.provider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/jpeg"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        startActivity(Intent.createChooser(intent, "Share Quote Card"))
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
