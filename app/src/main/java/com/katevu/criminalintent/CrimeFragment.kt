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

class CrimeFragment: Fragment() {
    private val TAG = "CrimeFragment"
    private  lateinit var crime: Crime
    private lateinit var crimeTitle: EditText
    private lateinit var dataButton: Button
    private lateinit var solvedCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView called")
        val view =  inflater.inflate(R.layout.fragment_crime, container, false)
        crimeTitle = view.findViewById(R.id.crimeTitle)
        dataButton = view.findViewById(R.id.crimeDate)
        solvedCheckBox = view.findViewById(R.id.crimeSolved)

        return view
    }

    override fun onStart() {
        Log.d(TAG, "onStart called")

        super.onStart()

        //init CrimeObject for test
        crime = Crime()
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
        crimeTitle.addTextChangedListener(crimeTitleWatcher)
        dataButton.apply {
            text = crime.date.toString()
            isEnabled = false//do not let the user interact with the button
        }

        solvedCheckBox.apply {
           setOnCheckedChangeListener { _, isChecked ->
               crime.isSolved = isChecked
           }
        }
    }
}