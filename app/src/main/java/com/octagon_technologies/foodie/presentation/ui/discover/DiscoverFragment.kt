package com.octagon_technologies.foodie.presentation.ui.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.octagon_technologies.foodie.databinding.FragmentDiscoverBinding
import com.octagon_technologies.foodie.presentation.ui.discover.ad_discover_item.AdDiscoverItem
import com.octagon_technologies.foodie.presentation.ui.discover.each_discover_item.EachDiscoverItemGroup
import com.octagon_technologies.foodie.utils.adHelpers
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DiscoverFragment : Fragment() {

    private val viewModel: DiscoverViewModel by viewModels()
    private val adHelper by adHelpers()
    private val groupAdapter = GroupAdapter<GroupieViewHolder>()
    private lateinit var binding: FragmentDiscoverBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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

        viewModel.listOfRecipe.observe(viewLifecycleOwner) { listOfRecipe ->
            Timber.i("listOfRecipe observed.")
            listOfRecipe?.forEach {
                val eachDiscoverItemGroup = EachDiscoverItemGroup(it) { clickRecipe ->
                    viewModel.getSelectedRecipeFromRecipe(clickRecipe)
                }
                groupAdapter.add(eachDiscoverItemGroup)

                // Adds an ad after every 8 recipes, ensuring that the
                // first recipe doesn't have an ad below it
                if (listOfRecipe.indexOf(it).coerceAtLeast(1) % 8 == 0) {
                    val discoverAd = AdDiscoverItem(adHelper) { failurePosition ->
                        groupAdapter.removeGroupAtAdapterPosition(failurePosition)
                    }
                    groupAdapter.add(discoverAd)
                }
            }
        }
        viewModel.selectedRecipe.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(
                    DiscoverFragmentDirections.actionNavigationDiscoverToRecipeFragment(it)
                )
                viewModel.resetSelectedRecipe()
            }
        }

        binding.discoverRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    val linearLayoutManager = LinearLayoutManager(context)
                    val lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition()
                    val recipesSize = viewModel.listOfRecipe.value?.size ?: return
                    val diff = recipesSize - lastVisiblePosition
                    Timber.d("RecyclerView.diff is $diff")

                    if (diff < 5) {
                        viewModel.loadData()
                    }
                }
            }
        })

    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop called")
        groupAdapter.clear()
    }
}