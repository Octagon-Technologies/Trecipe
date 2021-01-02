package com.octagon_technologies.foodie.presentation.ui.recipe.prep_step_item_group

import android.view.View
import com.octagon_technologies.foodie.R
import com.octagon_technologies.foodie.databinding.PrepStepItemLayoutBinding
import com.octagon_technologies.foodie.repo.network.models.instructions.Step
import com.xwray.groupie.viewbinding.BindableItem

class PreparationItemGroup(private val step: Step): BindableItem<PrepStepItemLayoutBinding>() {
    override fun bind(binding: PrepStepItemLayoutBinding, position: Int) {
        binding.stepCount.text = step.number?.toString() ?: "-1"
        binding.stepDescription.text = step.step ?: "--"
    }

    override fun getLayout() = R.layout.prep_step_item_layout
    override fun initializeViewBinding(view: View) =
        PrepStepItemLayoutBinding.bind(view)
}