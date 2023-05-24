package com.example.chlorophyll.ui

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.chlorophyll.ChlorophyllApplication
import com.example.chlorophyll.data.SettingsDataStore
import com.example.chlorophyll.databinding.FragmentCreateProfileBinding
import com.example.chlorophyll.viewmodels.ChlorophyllViewModel
import com.example.chlorophyll.viewmodels.ChlorophyllViewModelFactory
import java.util.*
import kotlin.properties.Delegates
import kotlin.random.Random

class CreateProfileFragment : Fragment(), OnClickListener, OnFocusChangeListener {

    companion object {
        const val START = "start"
    }
    private val sharedViewModel: ChlorophyllViewModel by activityViewModels {
        (activity?.application as ChlorophyllApplication).run {
            ChlorophyllViewModelFactory(
                database.plantDao,
                SettingsDataStore(this)
            )
        }
    }
    private var _binding: FragmentCreateProfileBinding? = null
    private val binding get() = _binding!!

    private var start by Delegates.notNull<Long>()

    private fun randomRgb(): String {
        return "#%06x".format(Random.nextInt(0xFFFFFF))
    }

    override fun onClick(v: View?) {
        val event = Calendar.getInstance().apply {
            timeInMillis = start
        }
        sharedViewModel.addNewPlant(
            name = binding.nameText.text.toString(),
            start = event,
            interval = binding.intervalPicker.value,
            color = randomRgb()
        )
        val action = CreateProfileFragmentDirections
            .actionCreateProfileFragmentToCalendarFragment()
        findNavController().navigate(action)
    }
    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (!hasFocus) {
            val inputMethodManager = activity?.getSystemService(
                Activity.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            val flags = 0
            inputMethodManager.hideSoftInputFromWindow(v!!.windowToken, flags)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            start = it.getLong(START)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateProfileBinding
            .inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.nameText.onFocusChangeListener = this
        binding.infoText.onFocusChangeListener = this
        binding.intervalPicker.apply {
            onFocusChangeListener = this@CreateProfileFragment
            minValue = 1
            maxValue = 30
            wrapSelectorWheel = true
        }
        binding.nextButton.setOnClickListener(this)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}