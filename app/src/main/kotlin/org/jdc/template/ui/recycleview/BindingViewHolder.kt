@file:Suppress("CanBeParameter", "unused", "MemberVisibilityCanBePrivate")

package org.jdc.template.ui.recycleview

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import org.jdc.template.ext.inflater
import com.squareup.picasso.Picasso
import androidx.databinding.BindingAdapter
import org.jdc.template.R


/**
 * RecyclerView ViewHolder which facilitates data binding.
 */
open class BindingViewHolder<out T : ViewDataBinding>
private constructor(val binding: T) : RecyclerView.ViewHolder(binding.root) {

    /**
     * Main constructor
     */
    constructor(@LayoutRes layoutId: Int, parent: ViewGroup) : this(inflate<T>(layoutId, parent))

    companion object {
        /**
         * Inflate a data bound view using it's layoutId and the parent. This does not attach the view to the parent.
         */
        @Suppress("NOTHING_TO_INLINE")
        inline fun <T : ViewDataBinding> inflate(@LayoutRes layoutId: Int, parent: ViewGroup): T {
            return DataBindingUtil.inflate(parent.inflater(), layoutId, parent, false)
        }

        @BindingAdapter("app:imageUrl")
        @JvmStatic
        fun loadImage(imageView: ImageView, url: String) {
            if (url != "") {
                Picasso.get().load(url).placeholder(R.mipmap.ic_launcher).into(imageView)
            }else{
                Picasso.get().load(R.mipmap.ic_launcher).into(imageView)
            }
        }
    }
}
