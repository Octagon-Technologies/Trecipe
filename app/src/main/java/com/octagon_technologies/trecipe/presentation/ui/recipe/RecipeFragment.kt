package com.octagon_technologies.trecipe.presentation.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.octagon_technologies.trecipe.R
import com.octagon_technologies.trecipe.databinding.FragmentRecipeBinding
import com.octagon_technologies.trecipe.models.State
import com.octagon_technologies.trecipe.presentation.ui.recipe.ingredients_item_group.IngredientItemGroup
import com.octagon_technologies.trecipe.presentation.ui.recipe.prep_step_item_group.PreparationItemGroup
import com.octagon_technologies.trecipe.repo.network.models.selected_recipe.SelectedRecipe
import com.octagon_technologies.trecipe.utils.BottomNavUtils
import com.octagon_technologies.trecipe.utils.ResUtils
import com.octagon_technologies.trecipe.utils.ViewUtils
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class RecipeFragment : Fragment() {

    private val ingredientsGroupAdapter = GroupAdapter<GroupieViewHolder>()
    private val preparationGroupAdapter = GroupAdapter<GroupieViewHolder>()
    private val viewModel: RecipeViewModel by viewModels()
    private val args: RecipeFragmentArgs by navArgs()

    private lateinit var binding: FragmentRecipeBinding
    private lateinit var selectedRecipe: SelectedRecipe

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        selectedRecipe = args.selectedRecipe
        binding = FragmentRecipeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewUtils.loadGlideImage(binding.recipeImage, selectedRecipe.image)

        viewModel.addToRecentRecipes(selectedRecipe)
        viewModel.loadRecipeInstructions(selectedRecipe)
        viewModel.getLikedRecipes(selectedRecipe)
        viewModel.getDownloadRecipes(selectedRecipe)

        setUpRecyclerView()
        setClickListeners()
        setUpScrollViewListener()
        setUpMinorLiveData()
        handleStateChanges()

        binding.toolbarRecipeName.text = selectedRecipe.title ?: "--"
        binding.recipeName.text = selectedRecipe.title ?: "--"
        binding.preparationTime.text = "Ready in ${selectedRecipe.readyInMinutes} minutes"
        binding.servingsAmount.text = "for ${selectedRecipe.servings} servings"

        viewModel.snackBarMessage.observe(viewLifecycleOwner) {
            it?.let {
                ViewUtils.showShortSnackBar(view, it)
                viewModel.resetSnackBarMessage()
            }
        }
        viewModel.recipeInstructions.observe(viewLifecycleOwner) { recipeInstructions ->
            Timber.d("recipeInstructions is $recipeInstructions")

            // Since two recipe instructions may exist, opt for the first one
            recipeInstructions?.firstOrNull()?.steps?.forEach { step ->
                preparationGroupAdapter.add(PreparationItemGroup(step))
            }
        }

        selectedRecipe.extendedIngredients?.forEach { extendedIngredient ->
            ingredientsGroupAdapter.add(IngredientItemGroup(extendedIngredient))
        }
    }

    private fun handleStateChanges() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it ?: return@observe) {
                State.Empty -> {
                    hideLoadingBarAndShowError()
                    binding.errorOccurredImage.setImageResource(R.drawable.filled_heart)
                    binding.errorOccurredDescription.text = getString(R.string.no_recipe_found)
                }
                State.Loading -> {
                }
                State.Done -> {
                    binding.scrollView.visibility = View.VISIBLE
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

    private fun setUpScrollViewListener() {
        binding.scrollView.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { scrollView, scrollX, scrollY, oldScrollX, oldScrollY ->
                // If diffY > 0, the user has scrolled down.
                val diffY = scrollY - oldScrollY
                val requiredScroll = ResUtils.getPixelsFromSdp(requireContext(), R.dimen._34sdp)

                if (diffY > 0) BottomNavUtils.hideBottomNavView(activity)
                else if (diffY < 0) BottomNavUtils.showBottomNavView(activity)

                if (scrollY > requiredScroll)
                    binding.toolbarRecipeName.visibility = View.VISIBLE
                else
                    binding.toolbarRecipeName.visibility = View.GONE

                Timber.d("oldScrollY is $oldScrollY, scrollY is $scrollY and diffY is $diffY")
            }
        )
    }

    private fun setClickListeners() {
        binding.backBtn.setOnClickListener { findNavController().popBackStack() }
        binding.downloadRecipeBtn.setOnClickListener { viewModel.downloadOrDeleteRecipe(selectedRecipe) }
        binding.likeBtn.setOnClickListener { viewModel.likeOrDislikeRecipe() }
    }

    private fun setUpRecyclerView() {
        binding.ingredientsRecyclerView.also {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = ingredientsGroupAdapter
        }
        binding.preparationRecyclerView.also {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = preparationGroupAdapter
        }
    }

    private fun setUpMinorLiveData() {
        viewModel.isLiked.observe(viewLifecycleOwner) {
            binding.likeBtn.setImageResource(
                if (it == true) R.drawable.filled_heart else R.drawable.empty_heart
            )
        }

        viewModel.isDownloaded.observe(viewLifecycleOwner) {
            binding.downloadRecipeBtn.setImageResource(
                if (it == true) R.drawable.delete_recipe else R.drawable.ic_download
            )
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveRecipeLikeChanges(selectedRecipe)
    }
}