package com.example.penguinpay.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import com.example.penguinpay.R
import com.example.penguinpay.databinding.MainFragmentBinding
import com.example.penguinpay.presentation.MainViewModel
import com.example.penguinpay.utils.*
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment) {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels()
    private val binding: MainFragmentBinding by viewBinding(MainFragmentBinding::bind)

    private var debouncePeriod: Long = 500
    private val coroutineScope = lifecycle.coroutineScope

    private var searchJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        listenForStateChanges()
    }

    private fun initViews() {
        val countries = resources.getStringArray(R.array.countries)
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, countries)
        binding.countryTypeDropdown.setAdapter(adapter)
        binding.countryTypeDropdown.setText(countries[0], false)
        binding.countryTypeDropdown.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                viewModel.currencyCode = countries[position]
            }

        binding.amountText.doAfterTextChanged {
            searchJob?.cancel()
            searchJob = coroutineScope.launch {
                it?.let {
                    delay(debouncePeriod)
                    if (it.isNotEmpty()) {
                        viewModel.fetchExchangeRate()
                    }
                }
            }
        }
    }

    private fun listenForStateChanges() {
        viewModel.viewState.onEach {
            handleViewStateChanges(it)
        }.launchIn(viewLifecycleScope)
    }

    private fun handleViewStateChanges(viewState: MainViewModel.Companion.ViewState) {
        when (viewState.loadState) {
            LoadingState.Error -> {
                hideLoadingDialog()
                viewState.error?.message?.let {
                    showSnackbar(it, Snackbar.LENGTH_SHORT)
                }
            }
            LoadingState.Idle -> {
            }
            LoadingState.Success -> {
                hideLoadingDialog()
                displayUserSentAmount(viewState)
                displayUserReceivedAmount(viewState)


            }
            LoadingState.Working -> {
                showLoadingDialog()
            }
        }
    }

    private fun displayUserReceivedAmount(viewState: MainViewModel.Companion.ViewState) {
        val amountSentInDec = convertBinaryToDecimal(binding.amountText.text.toString().toLong())
        val multipliedAmountSent = amountSentInDec * viewState.exchangeRate
        val formattedAmount = "${convertDecimalToBinary(multipliedAmountSent.toInt())}${viewModel.currencyCode}"
        binding.recipientAmountMessage.text = formattedAmount

    }

    private fun displayUserSentAmount(viewState: MainViewModel.Companion.ViewState){
        val amountSentInDec = convertBinaryToDecimal(binding.amountText.text.toString().toLong())
        val formattedAmount = "${amountSentInDec}USD"
        binding.userAmountMessage.text = formattedAmount
    }

}