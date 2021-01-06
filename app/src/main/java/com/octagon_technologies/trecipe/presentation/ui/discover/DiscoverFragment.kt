package com.octagon_technologies.trecipe.presentation.ui.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.octagon_technologies.trecipe.R
import com.octagon_technologies.trecipe.databinding.FragmentDiscoverBinding
import com.octagon_technologies.trecipe.models.State
import com.octagon_technologies.trecipe.presentation.ui.discover.each_discover_item.EachDiscoverItemGroup
import com.octagon_technologies.trecipe.repo.network.models.random_recipes.RandomRecipe
import com.octagon_technologies.trecipe.utils.BottomNavUtils
import com.octagon_technologies.trecipe.utils.SelectedRecipeUtils
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DiscoverFragment : Fragment() {

    @Inject
    lateinit var selectedRecipeUtils: SelectedRecipeUtils
    private val recipesInRecyclerView = ArrayList<RandomRecipe>()
    private var isLoading = false

    private val viewModel: DiscoverViewModel by viewModels()

    //    private val adHelper by adHelpers()
    private val groupAdapter = GroupAdapter<GroupieViewHolder>()
    private lateinit var binding: FragmentDiscoverBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDiscoverBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.discoverRecyclerview.also {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = groupAdapter
        }

        setUpRecyclerViewScrollListener()
        setUpClickListeners()
        handleStateChanges()
        selectedRecipeUtils.handleSelectedStateChanges(this)

        viewModel.listOfRandomRecipe.observe(viewLifecycleOwner) { listOfRecipe ->
            Timber.i("listOfRecipe observed.")
            loadRecipesIntoRecyclerView(listOfRecipe)
            isLoading = false
        }

        selectedRecipeUtils.selectedRecipe.observe(viewLifecycleOwner) {
            findNavController().navigate(
                DiscoverFragmentDirections.actionNavigationDiscoverToRecipeFragment(it
                    ?: return@observe)
            )
            selectedRecipeUtils.resetSelectedRecipe()

        }
    }

    private fun handleStateChanges() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it ?: return@observe) {
                State.Empty -> {
                    hideLoadingBarAndShowError()
                    binding.errorOccurredImage.setImageResource(R.drawable.empty_inbox)
                    binding.errorOccurredDescription.text = getString(R.string.no_recipe_found)
                }
                State.Loading -> {
                }
                State.Done -> {
                    binding.discoverRecyclerview.visibility = View.VISIBLE
                    binding.loadingProgressBar.visibility = View.GONE
                }
                else -> {
                    hideLoadingBarAndShowError()
                }
            }
        }
    }

    private fun hideLoadingBarAndShowError() {
        binding.errorOccurredImage.visibility = View.VISIBLE
        binding.errorOccurredDescription.visibility = View.VISIBLE
        binding.loadingProgressBar.visibility = View.GONE
    }

    private fun loadRecipesIntoRecyclerView(listOfRandomRecipe: List<RandomRecipe>?) {
        val diffList = listOfRandomRecipe?.filter { it !in recipesInRecyclerView }
        Timber.d("diffList.size is ${diffList?.size}")

        diffList?.forEach {
            recipesInRecyclerView.add(it)
            val eachDiscoverItemGroup = EachDiscoverItemGroup(it) { clickRecipe ->
                selectedRecipeUtils.getSelectedRecipeFromRecipe(clickRecipe.id)
            }

            groupAdapter.add(eachDiscoverItemGroup)
        }
    }

    private fun setUpClickListeners() {
        binding.searchForRecipesBtn.setOnClickListener {
            findNavController().navigate(
                DiscoverFragmentDirections.actionNavigationDiscoverToSearchFragment()
            )
        }
    }

    private fun setUpRecyclerViewScrollListener() {
        binding.discoverRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition()
                    val recipesSize = viewModel.listOfRandomRecipe.value?.size ?: return
                    val diff = recipesSize - lastVisiblePosition
                    Timber.d("RecyclerView.diff is $diff")

                    if (diff < 5 && !isLoading) {
                        viewModel.loadData()
                        isLoading = true
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Timber.d("dy is $dy in DiscoverFragment")

                // if dy > 0, the user is scrolling down
                if (dy > 0)
                    BottomNavUtils.hideBottomNavView(activity)
                else if (dy < 0)
                    BottomNavUtils.showBottomNavView(activity)
            }
        })
    }
}