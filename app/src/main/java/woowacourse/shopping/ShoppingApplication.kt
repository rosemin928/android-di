package woowacourse.shopping

import android.app.Application
import androidx.annotation.VisibleForTesting
import dagger.hilt.android.HiltAndroidApp
import woowacourse.bibi.di.core.Container
import woowacourse.bibi.di.core.ContainerBuilder
import woowacourse.shopping.di.installAllBindings

@HiltAndroidApp
class ShoppingApplication : Application() {
    lateinit var container: Container
        private set

    override fun onCreate() {
        super.onCreate()
        val builder = ContainerBuilder()
        installAllBindings(builder, this)
        container = builder.build()
    }

    @VisibleForTesting
    fun overrideContainerForTest(testContainer: Container) {
        container = testContainer
    }
}
