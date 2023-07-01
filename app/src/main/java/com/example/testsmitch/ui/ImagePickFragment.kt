package com.example.testsmitch.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.testsmitch.adapters.ImageAdapter
import com.example.testsmitch.databinding.FragmentImagePickBinding
import com.example.testsmitch.other.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class ImagePickFragment @Inject constructor(
    val  imageAdapter: ImageAdapter) : BaseFragment<FragmentImagePickBinding>(FragmentImagePickBinding::inflate) {
    lateinit var viewModel: ShoppingViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ShoppingViewModel::class.java]
        setupRecycleView()
        imageAdapter.images = listOf("Test")


        imageAdapter.setOnclickListener {
            viewModel.setCurImageUrl(it)
            findNavController().popBackStack()
        }
    }

    private fun setupRecycleView(){
        binding.rvImages.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(requireContext(),Constants.GRID_SPAN_COUNT)
        }
    }
}