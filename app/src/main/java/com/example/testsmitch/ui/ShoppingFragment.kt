package com.example.testsmitch.ui

import android.icu.lang.UCharacter.IndicPositionalCategory.LEFT
import android.icu.lang.UCharacter.IndicPositionalCategory.RIGHT
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testsmitch.R
import com.example.testsmitch.adapters.ShoppingItemAdapter
import com.example.testsmitch.databinding.FragmentShoppingBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShoppingFragment @Inject constructor(
    val shoppingItemAdapter: ShoppingItemAdapter,
    var viewModel: ShoppingViewModel? = null
) : BaseFragment<FragmentShoppingBinding>( FragmentShoppingBinding ::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = viewModel ?: ViewModelProvider(requireActivity())[ShoppingViewModel::class.java]
        subscribeObservers()
        setUpRecycleView()

        binding.fabAddShoppingItem.setOnClickListener {
            findNavController().navigate(
                ShoppingFragmentDirections.actionShoppingFragmentToAddShoppingItemFragment()
            )
        }

    }

    private val itemTouchCallback = @RequiresApi(Build.VERSION_CODES.Q)
    object : ItemTouchHelper.SimpleCallback(
        0,LEFT or RIGHT
    ){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val pos  = viewHolder.layoutPosition
            val item = shoppingItemAdapter.shoppingItems[pos]
            viewModel?.deleteItem(item)
            Snackbar.make(requireView(),"delete Item",Snackbar.LENGTH_LONG).apply {
                setAction("undo"){
                    viewModel?.addItemToDb(item)
                }
                show()
            }
        }
    }

    private fun subscribeObservers(){
        viewModel?.shoppingItem?.observe(viewLifecycleOwner){
            shoppingItemAdapter.shoppingItems = it
        }
        viewModel?.totalPrice?.observe(viewLifecycleOwner){
            val price = it?:0f
            val priceText = "Total price : $price"
            binding.tvShoppingItemPrice.text = priceText
        }
    }


    private fun setUpRecycleView(){
        binding.rvShoppingItems.apply {
            adapter = shoppingItemAdapter
            layoutManager = LinearLayoutManager(requireContext())
            ItemTouchHelper(itemTouchCallback).attachToRecyclerView(this)
        }
    }
}