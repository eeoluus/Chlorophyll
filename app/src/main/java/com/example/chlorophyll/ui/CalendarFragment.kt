package com.example.chlorophyll.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.applandeo.materialcalendarview.listeners.OnDayLongClickListener
import com.example.chlorophyll.ChlorophyllApplication
import com.example.chlorophyll.R
import com.example.chlorophyll.data.SettingsDataStore
import com.example.chlorophyll.databinding.FragmentCalendarBinding
import com.example.chlorophyll.domain.AlarmScheduler
import com.example.chlorophyll.domain.iconFromEvents
import com.example.chlorophyll.viewmodels.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Calendar

class CalendarFragment : Fragment(), OnDayClickListener, OnDayLongClickListener {

    private lateinit var context: Context

    private lateinit var schedule: Schedule

    private lateinit var settingsDataStore: SettingsDataStore

    private val sharedViewModel: ChlorophyllViewModel by activityViewModels {
        (activity?.application as ChlorophyllApplication).run {
            ChlorophyllViewModelFactory(
                database.plantDao,
                SettingsDataStore(this)
            )
        }
    }
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private fun bind(schedule: Schedule) {
        binding.calendarView.setEvents(
            schedule.map { entry ->
                EventDay(entry.key, iconFromEvents(context, entry.value))
            }
        )
    }
    override fun onDayLongClick(eventDay: EventDay) {
        // Room for navigating to a day view in the future.
    }
    override fun onDayClick(eventDay: EventDay) {
        val start = eventDay.calendar.timeInMillis
        val action = CalendarFragmentDirections
            .actionCalendarFragmentToCreateProfileFragment(
                start = start
            )
        findNavController().navigate(action)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding
            .inflate(inflater, container, false)
        return binding.root
    }
    private fun setupCalendarView() {
        this@CalendarFragment.let {
            binding.calendarView.run {
                setOnDayClickListener(it)
                setOnDayLongClickListener(it)
                setHeaderLabelColor(R.color.black)
                setHeaderColor(R.color.light_gray)
            }
        }
    }
    private fun observeSchedule() {
        sharedViewModel.schedule.observe(viewLifecycleOwner) {
            schedule = it
            bind(schedule)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        context = requireContext()

        settingsDataStore = SettingsDataStore(context)

        binding.nukeButton.setOnClickListener {
            AlarmScheduler(context).cancel()
            sharedViewModel.deleteAll()
            Toast.makeText(
                context,
                getString(R.string.all_deleted),
                Toast.LENGTH_LONG
            ).show()
        }
        setupCalendarView()

        sharedViewModel.spec.observe(viewLifecycleOwner) { half ->
            with(binding.calendarView) {
                setMinimumDate(
                    Calendar.getInstance().apply {
                        add(Calendar.MONTH, - half)
                        set(Calendar.DAY_OF_MONTH, 0)
                    }
                )
                setMaximumDate(
                    Calendar.getInstance().apply {
                        add(Calendar.MONTH, half)
                        set(Calendar.DAY_OF_MONTH, 31)
                    }
                )
            }
            observeSchedule()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}