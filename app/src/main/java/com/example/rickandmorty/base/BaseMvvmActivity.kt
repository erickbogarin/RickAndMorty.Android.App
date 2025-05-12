package com.example.rickandmorty.base

import android.view.WindowManager
import com.example.rickandmorty.di.ViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

open class BaseMvvmActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var vmFactory: ViewModelFactory

    inline fun <reified VM : BaseViewModel> appViewModel(): ViewModelDelegate<VM> {
        return ViewModelDelegate(VM::class, this) { this.vmFactory }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
    }
}
