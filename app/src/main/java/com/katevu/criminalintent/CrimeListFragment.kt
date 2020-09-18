package com.katevu.criminalintent

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class CrimeListFragment: Fragment() {
    private val TAG = "CrimeListFrag"

    interface Callbacks {
        fun onCrimeSelected(crimeId: UUID)
    }

    //init RecyclerView, adapter, viewModel and callBacks

    private var callbacks: Callbacks? = null

    private lateinit var crimeRecylerView: RecyclerView
    private var adapter: CrimeAdapter? = CrimeAdapter(emptyList())

    //associate with viewModel
    private val crimeListViewModel: CrimeListViewModel by lazy {ViewModelProvider(this).get(CrimeListViewModel::class.java)}

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_crime_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_crime -> {
                val crime = Crime()
                crimeListViewModel.addCrime(crime)
                callbacks?.onCrimeSelected(crime.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView called")
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        crimeRecylerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView
        crimeRecylerView.layoutManager = LinearLayoutManager(context)
        crimeRecylerView.adapter = adapter

        Log.d(TAG, "onCreateView ends")
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        crimeListViewModel.crimesListLiveData.observe (
            viewLifecycleOwner,
            Observer { crimes ->
                crimes?.let {
                    Log.d(TAG, ".onViewCreated called")
                    Log.d(TAG, ".onViewCreated data: $crimes")
                    updateUI(crimes)
                }
            }
        )
    }

    private fun updateUI(crimes: List<Crime>) {
        adapter = CrimeAdapter(crimes)
        crimeRecylerView.adapter = adapter
    }

    /**
     * CrimeHolder
     */
    private inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view),  View.OnClickListener{
        private lateinit var crime: Crime

        private val titleTextView = itemView.findViewById<TextView>(R.id.crime_title)
        private val dateTextView = itemView.findViewById<TextView>(R.id.crime_date)
        private val solvedImageView = itemView.findViewById<ImageView>(R.id.crime_solved)

        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = this.crime.title

            val myFormat = "EEE, MMM d, yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)

            dateTextView.setText(sdf.format(this.crime.date))

            solvedImageView.visibility = if (this.crime.isSolved) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            callbacks?.onCrimeSelected(crime.id)
        }
    }

    /**
     * CrimeHolder for Serious crime
     */
//    private inner class SeriousCrimeHolder(view: View) : RecyclerView.ViewHolder(view),  View.OnClickListener{
//        private lateinit var crime: Crime
//
//        private val titleTextView = itemView.findViewById<TextView>(R.id.serious_crime_title)
//        private val dateTextView = itemView.findViewById<TextView>(R.id.serious_crime_date)
//        private val contactPoliceButton = itemView.findViewById<Button>(R.id.serious_crime_contact_police)
//
//        fun bind(crime: Crime) {
//            this.crime = crime
//            titleTextView.text = this.crime.title
//            dateTextView.text = this.crime.date.toString()
//        }
//
//        init {
//            itemView.setOnClickListener(this)
//        }
//
//        override fun onClick(view: View?) {
//            Toast.makeText(context, "Serious crime with title: ${crime.title} pressed", Toast.LENGTH_LONG).show()
//        }
//    }

    /**
     * CrimeAdapter
     */
    private inner class CrimeAdapter (var crimes: List<Crime>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            private val TYPE_NORMAL = 0
            private val TYPE_SERIOUS = 1

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//            return when (viewType) {
//                TYPE_NORMAL -> {
//                    val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
//                    CrimeHolder(view)
//                }
//                TYPE_SERIOUS -> {
//                    val view = layoutInflater.inflate(R.layout.list_item_serious_crime, parent, false)
//                    SeriousCrimeHolder(view)
//                }
//                else -> throw IllegalArgumentException("Invalid view type")
//            }
            val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
            return CrimeHolder(view)

        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val crime = crimes[position]
            holder as CrimeHolder
            holder.bind(crime)
//            when (crime.requiresPolice) {
//                true -> {
//                    holder as SeriousCrimeHolder
//                    holder.bind(crime)
//                }
//                false -> {
//                    holder as CrimeHolder
//                    holder.bind(crime)
//                }
//                else -> throw  java.lang.IllegalArgumentException("Invalid view type")
//            }
        }


        override fun getItemCount(): Int {
            Log.d(TAG, "getItemCount: ${crimes.size}")
            return crimes.size
        }

//        override fun getItemViewType(position: Int): Int {
//            val crime = crimes[position]
//            return when(crime.requiresPolice) {
//                true -> TYPE_SERIOUS
//                false -> TYPE_NORMAL
//                else -> throw IllegalArgumentException("Invalid view type")
//            }
//        }

    }

    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }
}