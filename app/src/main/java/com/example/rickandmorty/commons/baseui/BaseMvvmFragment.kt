package com.example.rickandmorty.commons.baseui

import com.example.rickandmorty.di.DaggerViewModelFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

open class BaseMvvmFragment : DaggerFragment() {
    @Inject
    lateinit var factory: DaggerViewModelFactory

    /**
     * Create a Activity View Model, a shared view model between Activity and Fragment
     */
    inline fun <reified VM : BaseViewModel> appActivityViewModel(): ActivityVMFragmentDelegate<VM> =
        ActivityVMFragmentDelegate(VM::class) {
            this.requireBaseActivity().vmFactory
        }

    /**
     * Create a Fragment own View Model
     */
    inline fun <reified VM : BaseViewModel> appViewModel(): FragmentViewModelDelegate<VM> =
        FragmentViewModelDelegate(VM::class, this) { factory }

    fun requireBaseActivity() = (this.requireActivity() as BaseMvvmActivity)
}