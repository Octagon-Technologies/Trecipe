package com.octagon_technologies.trecipe.presentation.ui.search

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.octagon_technologies.trecipe.R
import com.octagon_technologies.trecipe.databinding.FragmentSearchBinding
import com.octagon_technologies.trecipe.models.State
import com.octagon_technologies.trecipe.presentation.ui.search.each_search_item.EachSearchItem
import com.octagon_technologies.trecipe.repo.network.models.suggestions.RecipeSuggestion
import com.octagon_technologies.trecipe.utils.KeyboardUtils
import com.octagon_technologies.trecipe.utils.ResUtils
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(inflater).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpTextWatcher()
        setUpClickListener()
        handleStateChanges()

        binding.searchRecyclerview.also {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = groupAdapter
        }

        viewModel.recipeSuggestions.observe(viewLifecycleOwner) { recipeSuggestions ->
            groupAdapter.clear()
            Timber.d("recipeSuggestions.size is ${recipeSuggestions.size}")
            recipeSuggestions?.forEach { addSuggestionToAdapter(it) }
        }
    }

    private fun addSuggestionToAdapter(recipeSuggestion: RecipeSuggestion) {
        val eachSearchItem = EachSearchItem(recipeSuggestion) { onClickRecipeSuggestion ->
            binding.searchInput.setText(onClickRecipeSuggestion.title)
            val query = binding.searchInput.text.toString()

            findNavController().navigate(
                SearchFragmentDirections
                    .actionSearchFragmentToSearchResultsFragment(onClickRecipeSuggestion, query)
            )
        }

        groupAdapter.add(eachSearchItem)
    }

    private fun handleStateChanges() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it ?: return@observe) {
                State.Loading -> {
                }
                State.Done -> {
                    hideErrorImage()
                    binding.searchRecyclerview.visibility = View.VISIBLE
                }
                State.Empty -> {
                    showErrorImage()
                    binding.errorOccurredImage.setImageResource(R.drawable.empty_inbox)
                    binding.errorOccurredDescription.text = ResUtils.getResString(context, R.string.no_recipes_found_for_search_query)
                }
                else -> showErrorImage()
            }
        }
    }

    fun showErrorImage() {
        binding.errorOccurredImage.visibility = View.VISIBLE
        binding.errorOccurredDescription.visibility = View.VISIBLE
    }
    fun hideErrorImage() {
        binding.errorOccurredImage.visibility = View.GONE
        binding.errorOccurredDescription.visibility = View.GONE
    }

    private fun setUpClickListener() {
        binding.searchBackBtn.setOnClickListener { findNavController().popBackStack() }
    }

    private fun setUpTextWatcher() {
        binding.searchInput.addTextChangedListener {
            countDownTimer.cancel()
            countDownTimer.start()
        }
    }

    private val countDownTimer = object : CountDownTimer(1200, 600) {
        override fun onTick(millisUntilFinished: Long) {}
        override fun onFinish() {
            val query = binding.searchInput.text?.toString()
            if (!query.isNullOrBlank()) {
                viewModel.loadSuggestions(query)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        KeyboardUtils.showKeyboard(binding.searchInput)
    }

    override fun onStop() {
        super.onStop()
        KeyboardUtils.hideKeyboard(binding.searchInput)
    }

}