package com.app.childlocationadmin

import androidx.recyclerview.widget.RecyclerView
import com.app.childlocationadmin.databinding.ItemBinding
import com.xwray.groupie.Item
import com.xwray.groupie.databinding.BindableItem

class ItemRecylerview(private val item:Location):BindableItem<ItemBinding>() {
    override fun getLayout(): Int {
        return R.layout.item
    }

    override fun bind(viewBinding: ItemBinding, position: Int)
    {
        viewBinding.item=item

    }

}