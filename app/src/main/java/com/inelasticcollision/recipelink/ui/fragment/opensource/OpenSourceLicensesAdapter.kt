package com.inelasticcollision.recipelink.ui.fragment.opensource

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView
import com.inelasticcollision.recipelink.databinding.ItemOpenSourceLicensesBinding

class OpenSourceLicensesAdapter : RecyclerView.Adapter<OpenSourceLicensesAdapter.ViewHolder>() {

    private var licenses: List<OpenSourceLicense> = emptyList()

    fun submitOpenSourceLicense(licenses: List<OpenSourceLicense>) {
        this.licenses = licenses
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemOpenSourceLicensesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindOpenSourceLicenses(licenses[position])
    }

    override fun getItemCount(): Int {
        return licenses.size
    }

    // ViewHolder
    class ViewHolder(
        private val binding: ItemOpenSourceLicensesBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindOpenSourceLicenses(license: OpenSourceLicense?) {
            binding.titleTextView.text = license?.title
            binding.typeTextView.text = license?.type

            binding.clickContainer.setOnClickListener { view ->
                // Open browser
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(license?.url)
                view.findFragment<OpenSourceLicensesFragment>().startActivity(intent)
            }
        }
    }
}