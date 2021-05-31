package ru.skillbranch.skillarticles.ui.dialogs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_category_dialog.view.*
import ru.skillbranch.skillarticles.R

class CategoryAdapter(private val listener: (String, Boolean) -> Unit) : ListAdapter<CategoryDataItem, CategoryVH>(CategoryDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
        return CategoryVH(LayoutInflater.from(parent.context).inflate(R.layout.item_category_dialog, parent, false), listener)
    }

    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
        holder.bind(getItem(position))
    }
}

class CategoryVH(
    override val containerView: View,
    val listener: (String, Boolean) -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: CategoryDataItem) {
        containerView.ch_select.setOnCheckedChangeListener(null)
        containerView.ch_select.isChecked = item.isChecked
        Glide.with(containerView.context)
            .load(item.icon)
            .apply(RequestOptions.circleCropTransform())
            .override(containerView.iv_icon.width)
            .into(containerView.iv_icon)
        containerView.tv_category.text = item.title
        containerView.tv_count.text = "${item.articlesCount}"
        containerView.ch_select.setOnCheckedChangeListener { _, checked -> listener(item.categoryId, checked) }
        itemView.setOnClickListener { containerView.ch_select.toggle() }
    }
}


class CategoryDiffCallback : DiffUtil.ItemCallback<CategoryDataItem>() {
    override fun areItemsTheSame(oldItem: CategoryDataItem, newItem: CategoryDataItem): Boolean {
        return oldItem.categoryId == newItem.categoryId
    }

    override fun areContentsTheSame(oldItem: CategoryDataItem, newItem: CategoryDataItem): Boolean {
        return oldItem == newItem
    }
}

data class CategoryDataItem(
    val categoryId: String,
    val icon: String,
    val title: String,
    val articlesCount: Int = 0,
    val isChecked: Boolean = false
)