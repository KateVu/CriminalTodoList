package com.katevu.criminalintent

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class CrimeListFragment: Fragment() {
    private val TAG = "CrimeListFrag"
    //associate with viewModel
    private val crimeListViewModel: CrimeListViewModel by lazy {ViewModelProvider(this).get(CrimeListViewModel::class.java)}


}