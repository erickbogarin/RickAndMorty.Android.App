package com.example.rickandmorty.commons.baseui

import com.example.rickandmorty.di.DaggerViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

open class BaseMvvmActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var vmFactory: DaggerViewModelFactory

    inline fun <reified VM : BaseViewModel> appViewModel(): ViewModelDelegate<VM> {
        return ViewModelDelegate(VM::class, this) { this.vmFactory }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
    }
}
