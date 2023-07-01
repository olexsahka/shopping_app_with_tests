package com.example.testsmitch.ui

import android.annotation.SuppressLint
import android.media.Image
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testsmitch.data.local.ShoppingItem
import com.example.testsmitch.data.remote.responses.ImageResponse
import com.example.testsmitch.other.Constants
import com.example.testsmitch.other.Event
import com.example.testsmitch.other.Resource
import com.example.testsmitch.repository.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val repository: ShoppingRepository
) : ViewModel() {

    val shoppingItem = repository.observeAllItems()

    val totalPrice = repository.observeTotalPrice()

    private val _images = MutableLiveData<Event<Resource<ImageResponse>>>()
    val images: LiveData<Event<Resource<ImageResponse>>> = _images

    private val _curUrl = MutableLiveData<String>()
    val curUrl: LiveData<String> = _curUrl

    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatus: LiveData<Event<Resource<ShoppingItem>>> = _insertShoppingItemStatus

    @SuppressLint("LogNotTimber")
    fun setCurImageUrl(url: String) {
        Log.d("SettingURL",url)
        _curUrl.postValue(url)
    }

    fun addItemToDb(shoppingItem: ShoppingItem) = viewModelScope.launch(Dispatchers.IO) {
        Log.d("insertShoppingItem","insertShoppingItem")

        repository.insertItem(shoppingItem)
    }

    fun deleteItem(shoppingItem: ShoppingItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteItem(shoppingItem)
    }

    fun addItem(named: String, amount: String, price: String) {
        Log.d("insertShoppingItem4","$named  $amount  $price")

        if (named.isEmpty() || amount.isEmpty() || price.isEmpty()){
            Log.d("insertShoppingItem5","insertShoppingItem")
            _insertShoppingItemStatus.postValue(Event(Resource.error("empty data",null)))
            return
        }
        if (named.length> Constants.MAX_NAME_LENGTH){
            Log.d("insertShoppingItem6","insertShoppingItem")
            _insertShoppingItemStatus.postValue(Event(Resource.error("max length name ",null)))
            return
        }
        if (price.length> Constants.MAX_PRICE_LENGTH){
            Log.d("insertShoppingItem7","insertShoppingItem")
            _insertShoppingItemStatus.postValue(Event(Resource.error("max length price ",null)))
            return
        }
        val amount = try {
            Log.d("insertShoppingItem8","insertShoppingItem")
            amount.toInt()
        }
        catch (e: Exception){
            Log.d("insertShoppingItem9","insertShoppingItem")
            _insertShoppingItemStatus.postValue(Event(Resource.error("uncorrectly amount amount ",null)))
            return
        }
        Log.d("insertShoppingItem10","insertShoppingItem")

        val shoppingItem = ShoppingItem(named,amount, price.toFloat(),_curUrl.value?:"")
        addItemToDb(shoppingItem)
        setCurImageUrl("")
        _insertShoppingItemStatus.value =Event(Resource.success(shoppingItem))
    }
    fun searchForImage(imageQuery: String){
        if (imageQuery.isEmpty()){
            return
        }
        _images.value = Event(Resource.loading(null))
        viewModelScope.launch{
            val responses =  repository.searchImage(imageQuery)
            _images.value = Event(responses)
        }
    }

}

