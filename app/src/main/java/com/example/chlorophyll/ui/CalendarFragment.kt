package com.example.chlorophyll.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.applandeo.materialcalendarview.listeners.OnDayLongClickListener
import com.example.chlorophyll.ChlorophyllApplication
import com.example.chlorophyll.R
import com.example.chlorophyll.databinding.FragmentCalendarBinding
import com.example.chlorophyll.domain.AlarmScheduler
import com.example.chlorophyll.domain.iconFromEvents
import com.example.chlorophyll.viewmodels.*

class CalendarFragment : Fragment(), OnDayClickListener, OnDayLongClickListener, OnCalendarPageChangeListener {

    private lateinit var schedule: Schedule

    private val sharedViewModel: ChlorophyllViewModel by activityViewModels {
        ChlorophyllViewModelFactory(
            (activity?.application as ChlorophyllApplication).database.plantDao,
        )
    }
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private fun bind(schedule: Schedule) {
        val events = schedule.map {
            EventDay(it.key, iconFromEvents(requireContext(), it.value))
        }
        binding.calendarView.setEvents(events)
    }
    override fun onDayLongClick(eventDay: EventDay) {
        // Room for navigating to a day view in the future.
    }
    override fun onDayClick(eventDay: EventDay) {
        val start = eventDay.calendar.timeInMillis
        val action = CalendarFragmentDirections
            .actionCalendarFragmentToCreateProfileFragment(start = start)
        findNavController().navigate(action)
    }
    override fun onChange() {
        // To do: implement a scroll listener.
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.calendarView.let {
            it.setOnDayClickListener(this)
            it.setOnDayLongClickListener(this)
            it.setHeaderColor(R.color.light_gray)
            it.setHeaderLabelColor(R.color.black)

            /* it.setOnForwardPageChangeListener(this)
            it.setOnPreviousPageChangeListener(this) */
        }
        binding.nukeButton.setOnClickListener {
            with(AlarmScheduler(requireContext())) {
                cancel()
            }
            sharedViewModel.deleteAll()
            Toast.makeText(
                requireContext(),
                getString(R.string.all_deleted),
                Toast.LENGTH_LONG
            ).show()
        }
        sharedViewModel.schedule.observe(viewLifecycleOwner) {
            schedule = it
            bind(schedule)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}