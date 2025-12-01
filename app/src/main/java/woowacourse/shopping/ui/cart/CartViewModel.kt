package woowacourse.shopping.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.model.Product
import javax.inject.Inject

@HiltViewModel
class CartViewModel
    @Inject
    constructor(
        private val cartRepository: CartRepository,
    ) : ViewModel() {
        private val _cartProducts: MutableLiveData<List<Product>> =
            MutableLiveData(emptyList())
        val cartProducts: LiveData<List<Product>> get() = _cartProducts

        private val _onCartProductDeleted: MutableLiveData<Boolean> = MutableLiveData(false)
        val onCartProductDeleted: LiveData<Boolean> get() = _onCartProductDeleted

        private val _errorMessage = MutableLiveData<String?>()
        val errorMessage: LiveData<String?> get() = _errorMessage

        private val errorHandler =
            CoroutineExceptionHandler { _, t ->
                _onCartProductDeleted.postValue(false)
                _errorMessage.postValue(t.message ?: "작업 중 오류가 발생했어요.")
            }

        fun getAllCartProducts() {
            viewModelScope.launch(errorHandler) {
                _cartProducts.value = cartRepository.getAllCartProducts()
            }
        }

        fun deleteCartProduct(id: Long) {
            viewModelScope.launch(errorHandler) {
                cartRepository.deleteCartProduct(id)
                _onCartProductDeleted.value = true
                _cartProducts.value = cartRepository.getAllCartProducts()
            }
        }

        fun consumeError() {
            _errorMessage.value = null
        }
    }
