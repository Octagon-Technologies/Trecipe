package com.octagon_technologies.foodie.presentation.ui.my_recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.octagon_technologies.foodie.databinding.FragmentMyRecipesBinding
import com.octagon_technologies.foodie.models.BasicRoomRecipe
import com.octagon_technologies.foodie.presentation.ui.my_recipes.mini_recipe_item.MiniRecipeItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyRecipesFragment : Fragment() {

    private val recentRecipeGroupAdapter = GroupAdapter<GroupieViewHolder>()
    private val likedRecipeGroupAdapter = GroupAdapter<GroupieViewHolder>()

    private val viewModel: MyRecipesViewModel by viewModels()
    private lateinit var binding: FragmentMyRecipesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyRecipesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.likesRecyclerView.also {
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            it.adapter = likedRecipeGroupAdapter
        }
        binding.recentlyViewedRecyclerView.also {
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            it.adapter = recentRecipeGroupAdapter
        }

        viewModel.likedRecipesList.observe(viewLifecycleOwner) {
            it.handleListOfBasicRoomRecipe(likedRecipeGroupAdapter)
        }
        viewModel.recentRecipesList.observe(viewLifecycleOwner) {
            it.handleListOfBasicRoomRecipe(recentRecipeGroupAdapter)
        }

        viewModel.selectedRecipe.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(
                    MyRecipesFragmentDirections.actionNavigationMyRecipesToRecipeFragment(it)
                )
            }
        }

    }

    private fun List<BasicRoomRecipe>?.handleListOfBasicRoomRecipe(groupAdapter: GroupAdapter<GroupieViewHolder>): Unit? {
        if (this == null) return null
        groupAdapter.setOnItemClickListener { item, _ ->
            val miniRecipeItem = item as MiniRecipeItem
            viewModel.getSelectedRecipeFromRecipe(miniRecipeItem.basicRoomRecipe.id)
        }
        forEach {
            groupAdapter.add(MiniRecipeItem(it))
        }

        return Unit
    }

}