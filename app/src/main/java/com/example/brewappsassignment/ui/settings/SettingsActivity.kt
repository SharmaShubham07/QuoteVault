package com.example.brewappsassignment.ui.settings

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.brewappsassignment.databinding.ActivitySettingsBinding
import com.example.brewappsassignment.data.local.SessionManager
import com.example.brewappsassignment.domain.repository.AuthRepository
import com.example.brewappsassignment.ui.theme.ThemeManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var session: SessionManager
    private lateinit var authRepo: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        loadSavedSettings()
        setupListeners()
    }

    private fun loadSavedSettings() {
        binding.switchTheme.isChecked = session.getTheme() == "dark"

        when (session.getAccent()) {
            "red" -> binding.rgAccent.check(binding.rbRed.id)
            "green" -> binding.rgAccent.check(binding.rbGreen.id)
            else -> binding.rgAccent.check(binding.rbBlue.id)
        }

        val size = session.getFontSize()
        binding.fontSeekBar.progress = size
        binding.tvFontPreview.textSize = size.toFloat()
    }

    private fun setupListeners() {

        binding.switchTheme.setOnCheckedChangeListener { _, isDark ->
            val newTheme = if (isDark) "dark" else "light"
            session.saveTheme(newTheme)

            ThemeManager.applyTheme(newTheme, session.getAccent())
            recreate() // update UI immediately
        }

        binding.rgAccent.setOnCheckedChangeListener { _, id ->
            val accent = when (id) {
                binding.rbRed.id -> "red"
                binding.rbGreen.id -> "green"
                else -> "blue"
            }

            session.saveAccent(accent)
            ThemeManager.applyTheme(session.getTheme(), accent)
            recreate()
        }

        binding.fontSeekBar.setOnSeekBarChangeListener(
            object : android.widget.SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: android.widget.SeekBar?, value: Int, fromUser: Boolean) {
                    binding.tvFontPreview.textSize = value.toFloat()
                    session.saveFontSize(value)
                }

                override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {}
            }
        )

        binding.btnSaveCloud.setOnClickListener { saveToCloud() }
    }

    private fun saveToCloud() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                authRepo.updateUserPreferences(
                    theme = session.getTheme(),
                    accent = session.getAccent(),
                    fontSize = session.getFontSize()
                )

                runOnUiThread {
                    Toast.makeText(this@SettingsActivity, "Settings synced to cloud!", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@SettingsActivity, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
