package com.example.brewappsassignment.ui.share

import android.content.Context
import android.os.Bundle
import com.example.brewappsassignment.databinding.BottomSheetShareStyleBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShareStyleBottomSheet(
    context: Context,
    private val onStyleSelected: (Int) -> Unit
) : BottomSheetDialog(context) {

    private lateinit var binding: BottomSheetShareStyleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BottomSheetShareStyleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.style1.setOnClickListener { onStyleSelected(1); dismiss() }
        binding.style2.setOnClickListener { onStyleSelected(2); dismiss() }
        binding.style3.setOnClickListener { onStyleSelected(3); dismiss() }
    }
}
