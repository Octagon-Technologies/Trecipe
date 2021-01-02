package com.octagon_technologies.foodie.presentation.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.octagon_technologies.foodie.databinding.FragmentRecipeBinding
import com.octagon_technologies.foodie.presentation.ui.recipe.ingredients_item_group.IngredientItemGroup
import com.octagon_technologies.foodie.presentation.ui.recipe.prep_step_item_group.PreparationItemGroup
import com.octagon_technologies.foodie.repo.network.models.selected_recipe.SelectedRecipe
import com.octagon_technologies.foodie.utils.ViewUtils
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
        savedInstanceState: Bundle?
    ): View {
        selectedRecipe = args.selectedRecipe
        binding = FragmentRecipeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewUtils.loadGlideImage(binding.recipeImage, selectedRecipe.image)
        viewModel.loadRecipeInstructions(selectedRecipe)

        setUpRecyclerView()
        setClickListeners()

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
            recipeInstructions?.first()?.steps?.forEach { step ->
                preparationGroupAdapter.add(PreparationItemGroup(step))
            }
        }

        selectedRecipe.extendedIngredients?.forEach { extendedIngredient ->
            ingredientsGroupAdapter.add(IngredientItemGroup(extendedIngredient))
        }
    }

    private fun setClickListeners() {
        binding.backBtn.setOnClickListener { findNavController().popBackStack() }
        binding.downloadRecipeBtn.setOnClickListener { viewModel.downloadRecipe(selectedRecipe) }
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
}