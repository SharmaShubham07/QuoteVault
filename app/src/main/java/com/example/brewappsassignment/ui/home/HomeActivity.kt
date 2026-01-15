package com.example.brewappsassignment.ui.home

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.brewappsassignment.R
import com.example.brewappsassignment.data.local.SessionManager
import com.example.brewappsassignment.data.remote.FavoritesApi
import com.example.brewappsassignment.data.remote.QuotesApi
import com.example.brewappsassignment.data.remote.SupabaseClient
import com.example.brewappsassignment.data.remote.models.QuoteDto
import com.example.brewappsassignment.databinding.ActivityHomeBinding
import com.example.brewappsassignment.domain.repository.QuoteRepository
import com.example.brewappsassignment.ui.favorites.FavoritesActivity
import com.example.brewappsassignment.ui.profile.ProfileActivity
import com.example.brewappsassignment.ui.share.ShareCardActivity
import com.example.brewappsassignment.widget.QuoteWidget

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var adapter: QuoteAdapter

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            QuoteRepository(
                SupabaseClient.instance.create(QuotesApi::class.java),
                SupabaseClient.instance.create(FavoritesApi::class.java),
                SessionManager(this)
            )
        )
    }

    private var selectedCategory: String? = null
    private var showingFiltered = false  // View All reset flag

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecycler()
        setupSearch()
        setupCategories()
        setupObservers()

        setupShareQOTD()
        setupViewAllButton()
        setupFavoritesButton()
        setupBottomNav()

        viewModel.loadQOTD()
        viewModel.loadQuotes(refresh = true)
    }

    // ---------------------------------------------------------------------
    // RecyclerView
    // ---------------------------------------------------------------------
    private fun setupRecycler() {
        adapter = QuoteAdapter(
            onFavClick = { quote -> viewModel.toggleFavorite(quote) },
            onShareCard = { quote -> showCardStyleSelector(quote) }
        )

        binding.rvQuotes.layoutManager = LinearLayoutManager(this)
        binding.rvQuotes.adapter = adapter

        binding.swipeRefresh.setOnRefreshListener {
            selectedCategory = null
            showingFiltered = false
            binding.etSearch.text?.clear()
            highlightSelectedCategory("")
            viewModel.loadQuotes(refresh = true)
            QuoteWidget.forceUpdate(this)
            binding.swipeRefresh.isRefreshing = false
        }
    }

    // ---------------------------------------------------------------------
    // Search
    // ---------------------------------------------------------------------
    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s?.toString() ?: ""

                if (text.isNotEmpty()) {
                    selectedCategory = null
                    showingFiltered = true
                    viewModel.search(text)
                } else {
                    showingFiltered = false
                    viewModel.loadQuotes(refresh = true)
                }
            }
        })
    }

    // ---------------------------------------------------------------------
    // Categories
    // ---------------------------------------------------------------------
    private fun setupCategories() {
        val categories = listOf("Motivation", "Love", "Success", "Wisdom", "Humor")

        categories.forEach { cat ->
            val chip = layoutInflater.inflate(
                R.layout.item_category_chip,
                binding.categoryContainer,
                false
            ) as androidx.appcompat.widget.AppCompatTextView

            chip.text = cat

            chip.setOnClickListener {
                selectedCategory = cat
                showingFiltered = true
                highlightSelectedCategory(cat)
                viewModel.filterByCategory(cat)
            }

            binding.categoryContainer.addView(chip)
        }
    }

    // ---------------------------------------------------------------------
    // View All
    // ---------------------------------------------------------------------
    private fun setupViewAllButton() {
        binding.tvViewAll.setOnClickListener {
            selectedCategory = null
            showingFiltered = false

            binding.etSearch.text?.clear()
            highlightSelectedCategory("")
            viewModel.loadQuotes(refresh = true)
        }
    }

    // ---------------------------------------------------------------------
    // Highlight category chip
    // ---------------------------------------------------------------------
    private fun highlightSelectedCategory(category: String) {
        for (i in 0 until binding.categoryContainer.childCount) {
            val chip =
                binding.categoryContainer.getChildAt(i) as androidx.appcompat.widget.AppCompatTextView

            if (chip.text.toString() == category) {
                chip.setBackgroundResource(R.drawable.bg_chip_selected)
                chip.setTextColor(getColor(android.R.color.white))
            } else {
                chip.setBackgroundResource(R.drawable.bg_chip_unselected)
                chip.setTextColor(getColor(R.color.black))
            }
        }
    }

    // ---------------------------------------------------------------------
    // Share QOTD
    // ---------------------------------------------------------------------
    private fun setupShareQOTD() {
        binding.btnShareQotd.setOnClickListener {
            val quote = binding.tvQotdText.text.toString()
            val author = binding.tvQotdAuthor.text.toString()

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, "$quote\n$author")

            startActivity(Intent.createChooser(shareIntent, "Share Quote"))
        }
    }

    // ---------------------------------------------------------------------
    // Favorites Screen
    // ---------------------------------------------------------------------
    private fun setupFavoritesButton() {
        binding.ivSearch.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }
    }

    // ---------------------------------------------------------------------
    // Bottom Navigation Bar
    // ---------------------------------------------------------------------
    private fun setupBottomNav() {

        findViewById<View>(R.id.navHome).setOnClickListener {
            // Already on Home
        }

//        findViewById<View>(R.id.navDiscover).setOnClickListener {
//            startActivity(Intent(this, DiscoverActivity::class.java))
//            overridePendingTransition(0, 0)
//        }

        findViewById<View>(R.id.navSaved).setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
            overridePendingTransition(0, 0)
        }

        findViewById<View>(R.id.navProfile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
            overridePendingTransition(0, 0)
        }

//        findViewById<View>(R.id.fabAdd).setOnClickListener {
//            startActivity(Intent(this, AddQuoteActivity::class.java))
//        }
    }

    // ---------------------------------------------------------------------
    // LiveData Observers
    // ---------------------------------------------------------------------
    private fun setupObservers() {

        viewModel.qotd.observe(this) { q ->
            binding.tvQotdText.text = q.text
            binding.tvQotdAuthor.text = "— ${q.author}"
        }

        viewModel.quotes.observe(this) { list ->
            adapter.submitList(list)
        }

        viewModel.loading.observe(this) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    private fun showCardStyleSelector(quote: QuoteDto) {
        val intent = Intent(this, ShareCardActivity::class.java)
        intent.putExtra("QUOTE_TEXT", quote.text)
        intent.putExtra("QUOTE_AUTHOR", quote.author)
        intent.putExtra("QUOTE_CATEGORY", quote.category)
        startActivity(intent)
    }

}
