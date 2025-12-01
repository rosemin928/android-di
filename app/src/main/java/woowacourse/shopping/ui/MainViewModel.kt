package woowacourse.shopping.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.model.Product
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val productRepository: ProductRepository,
        private val cartRepository: CartRepository,
    ) : ViewModel() {
        private val _products: MutableLiveData<List<Product>> = MutableLiveData(emptyList())
        val products: LiveData<List<Product>> get() = _products

        private val _onProductAdded: MutableLiveData<Boolean> = MutableLiveData(false)
        val onProductAdded: LiveData<Boolean> get() = _onProductAdded

        private val _errorMessage = MutableLiveData<String?>()
        val errorMessage: LiveData<String?> get() = _errorMessage

        private val errorHandler =
            CoroutineExceptionHandler { _, t ->
                _onProductAdded.postValue(false)
                _errorMessage.postValue(t.message ?: "작업 중 오류가 발생했어요.")
            }

        fun addCartProduct(product: Product) {
            viewModelScope.launch(errorHandler) {
                cartRepository.addCartProduct(product)
                _onProductAdded.value = true
            }
        }

        fun getAllProducts() {
            viewModelScope.launch(errorHandler) {
                _products.value = productRepository.getAllProducts()
            }
        }

        fun consumeError() {
            _errorMessage.value = null
        }
    }
