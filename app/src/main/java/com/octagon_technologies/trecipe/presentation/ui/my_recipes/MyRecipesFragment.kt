package com.octagon_technologies.trecipe.presentation.ui.my_recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.octagon_technologies.trecipe.R
import com.octagon_technologies.trecipe.databinding.FragmentMyRecipesBinding
import com.octagon_technologies.trecipe.models.BasicRoomRecipe
import com.octagon_technologies.trecipe.models.RecipeType
import com.octagon_technologies.trecipe.presentation.ui.my_recipes.mini_recipe_item.MiniRecipeItem
import com.octagon_technologies.trecipe.repo.network.models.selected_recipe.SelectedRecipe
import com.octagon_technologies.trecipe.utils.ResUtils
import com.octagon_technologies.trecipe.utils.SelectedRecipeUtils
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MyRecipesFragment : Fragment() {

    @Inject
    lateinit var selectedRecipeUtils: SelectedRecipeUtils

    private val recentRecipeGroupAdapter = GroupAdapter<GroupieViewHolder>()
    private val likedRecipeGroupAdapter = GroupAdapter<GroupieViewHolder>()
    private val downloadRecipeGroupAdapter = GroupAdapter<GroupieViewHolder>()

    private val viewModel: MyRecipesViewModel by viewModels()
    private lateinit var binding: FragmentMyRecipesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMyRecipesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerViews()
        selectedRecipeUtils.handleSelectedStateChanges(this)

        viewModel.likedRecipesList.observe(viewLifecycleOwner) {
            handleLiveDataRecipes(it, likedRecipeGroupAdapter, RecipeType.Liked)
        }
        viewModel.recentRecipesList.observe(viewLifecycleOwner) {
            handleLiveDataRecipes(it, recentRecipeGroupAdapter, RecipeType.Recent)
        }
        viewModel.downloadRecipeList.observe(viewLifecycleOwner) {
            handleLiveDataRecipes(it, downloadRecipeGroupAdapter, RecipeType.Download)
        }

        selectedRecipeUtils.selectedRecipe.observe(viewLifecycleOwner) {
            navigateToRecipeFragment(it)
            selectedRecipeUtils.resetSelectedRecipe()
        }

    }

    private fun setUpRecyclerViews() {
        val listOfRecyclerView = listOf(
            binding.likesRecyclerView,
            binding.recentlyViewedRecyclerView,
            binding.downloadRecyclerView
        )

        listOfRecyclerView.forEach {
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        binding.likesRecyclerView.adapter = likedRecipeGroupAdapter
        binding.recentlyViewedRecyclerView.adapter = recentRecipeGroupAdapter
        binding.downloadRecyclerView.adapter = downloadRecipeGroupAdapter
    }

    private fun handleLiveDataRecipes(
        listOfBasicRoomRecipe: List<Any>?,
        groupAdapter: GroupAdapter<GroupieViewHolder>,
        recipeType: RecipeType,
    ) {
        if (listOfBasicRoomRecipe.isNullOrEmpty()) {
            getErrorTextAndBelowHeader(groupAdapter)
            return
        }

        listOfBasicRoomRecipe.forEach {
            val onClickLambda: (Any) -> Unit = { onClickAny -> handleMiniRecipeClick(onClickAny) }
            val miniRecipeItem = MiniRecipeItem(it, onClickLambda, handleRemoveBtnClick(recipeType))
            groupAdapter.add(miniRecipeItem)
        }
    }

    private fun navigateToRecipeFragment(selectedRecipe: SelectedRecipe?) {
        findNavController().navigate(
            MyRecipesFragmentDirections
                .actionNavigationMyRecipesToRecipeFragment(selectedRecipe ?: return)
        )
    }

    private fun handleRemoveBtnClick(recipeType: RecipeType): (MiniRecipeItem) -> Unit =
        when (recipeType) {
            RecipeType.Recent -> {
                {
                    viewModel.deleteRecentViewedRecipe(it.any as BasicRoomRecipe)
                    removeGroupAndInvalidateAdapterPositions(recentRecipeGroupAdapter,
                        it.currentPosition)
                }
            }
            RecipeType.Liked -> {
                {
                    viewModel.deleteLikedRecipe(it.any as BasicRoomRecipe)
                    removeGroupAndInvalidateAdapterPositions(likedRecipeGroupAdapter,
                        it.currentPosition)
                }
            }
            RecipeType.Download -> {
                {
                    viewModel.deleteDownloadRecipe(it.any as SelectedRecipe)
                    removeGroupAndInvalidateAdapterPositions(downloadRecipeGroupAdapter,
                        it.currentPosition)
                }
            }
        }

    private fun removeGroupAndInvalidateAdapterPositions(
        groupAdapter: GroupAdapter<GroupieViewHolder>,
        currentPosition: Int,
    ) {
        Timber.d("currentPosition is $currentPosition")
        groupAdapter.removeGroupAtAdapterPosition(currentPosition)
        (0 until groupAdapter.itemCount).forEach { newPosition ->
            val miniRecipeItem =
                groupAdapter.getGroupAtAdapterPosition(newPosition) as MiniRecipeItem
            miniRecipeItem.notifyChanged(newPosition)
        }
    }

    private fun getErrorTextAndBelowHeader(groupAdapter: GroupAdapter<GroupieViewHolder>) {
        when (groupAdapter) {
            recentRecipeGroupAdapter -> {
                handleEmptyAndNullList(
                    binding.recentlyViewedRecyclerView,
                    binding.recentlyViewedError,
                    binding.likesPlain)
            }
            likedRecipeGroupAdapter -> {
                handleEmptyAndNullList(
                    binding.likesRecyclerView,
                    binding.likedError,
                    binding.downloadPlainText)
            }
            downloadRecipeGroupAdapter -> {
                handleEmptyAndNullList(
                    binding.downloadRecyclerView,
                    binding.downloadError,
                    null)
            }
        }
    }

    private fun handleEmptyAndNullList(
        recyclerView: RecyclerView,
        errorTextView: TextView,
        belowHeader: TextView?,
    ) {
        val params = belowHeader?.layoutParams as ConstraintLayout.LayoutParams?
        params?.topToBottom = errorTextView.id
        params?.topMargin = ResUtils.getPixelsFromSdp(requireContext(), R.dimen._34sdp)
        belowHeader?.layoutParams = params

        recyclerView.visibility = View.GONE
        errorTextView.visibility = View.VISIBLE
    }

    private fun handleMiniRecipeClick(onClickAny: Any) {
        if (onClickAny is BasicRoomRecipe) {
            // This is for recent and liked recipes
            selectedRecipeUtils.getSelectedRecipeFromRecipe(onClickAny.id)
        } else {
            // This is for download recipe which automatically contains a selected recipe. Forgive the complex code but the
            // extra size brought by adding another class is too much. Also, fetching downloaded recipes does not make sense since
            // the user is offline
            val selectedRecipe = (onClickAny as SelectedRecipe)
            navigateToRecipeFragment(selectedRecipe)
        }
    }
}