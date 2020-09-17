package com.katevu.criminalintent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import java.util.*

private const val ARG_CRIME_ID = "crime_id"
private const val DIALOG_DATE = "DialogDate"

class CrimeFragment: Fragment() {
    private val TAG = "CrimeFragment"

    private val crimeDetailViewModel: CrimeDetailViewModel by lazy {ViewModelProvider(this).get(CrimeDetailViewModel::class.java) }
    private  lateinit var crime: Crime
    private lateinit var crimeTitle: EditText
    private lateinit var dataButton: Button
    private lateinit var solvedCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called")
        super.onCreate(savedInstanceState)
        crime = Crime()
        val crimeId: UUID = arguments?.getSerializable(ARG_CRIME_ID) as UUID
        crimeDetailViewModel.loadCrime(crimeId)
        Log.d(TAG, ".onCreated with crimeID: $crimeId")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView called")
        val view =  inflater.inflate(R.layout.fragment_crime, container, false)
        crimeTitle = view.findViewById(R.id.crimeTitle)
        dataButton = view.findViewById(R.id.crimeDateButton)
        solvedCheckBox = view.findViewById(R.id.crimeSolved)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeDetailViewModel.crimeLiveData.observe(
            viewLifecycleOwner,
            Observer { crime ->
                crime?.let {
                    this.crime = crime
                    Log.d(TAG, "Crime title: ${this.crime.title}")
                    updateUI()
                }
            }
        )
    }

    override fun onStart() {
        Log.d(TAG, "onStart called")

        super.onStart()

        //declare listener for crimeTitle
        var crimeTitleWatcher = object : TextWatcher {
            override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                TODO("Not yet implemented")
            }

            override fun afterTextChanged(sequence: Editable?) {
                TODO("Not yet implemented")
            }
        }
        //init
        //crimeTitle.addTextChangedListener(crimeTitleWatcher)
        dataButton.setOnClickListener {
            DatePickerFragment().apply {
                show(this@CrimeFragment.getParentFragmentManager(), DIALOG_DATE)
            }
        }

        solvedCheckBox.apply {
           setOnCheckedChangeListener { _, isChecked ->
               crime.isSolved = isChecked
           }
        }
    }

    override fun onStop() {
        super.onStop()
        crimeDetailViewModel.saveCrime(crime)
    }

    fun updateUI() {
        crimeTitle.setText(crime.title)
        dataButton.text = crime.date.toString()
        solvedCheckBox.apply {
            isChecked = crime.isSolved
            jumpDrawablesToCurrentState()
        }
    }

    companion object {
        fun newInstance(crimeId: UUID): CrimeFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CRIME_ID, crimeId)
            }
            return CrimeFragment().apply {
                arguments = args
            }
        }
    }
}