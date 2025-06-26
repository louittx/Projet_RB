package com.example.rb_bluetooth

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class BtAdapteur(context: Context, resource: Int, private val items: List<PostData>) :
    ArrayAdapter<PostData>(context, resource, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.itembtlistview, parent, false)

        val textName = view.findViewById<TextView>(R.id.textView)
        val imageIcon = view.findViewById<ImageView>(R.id.imageView)

        val item = items[position] // permet de connatre la position des iteme pour les afficher
        textName.text = item.name // trouvre le name de l'item
        imageIcon.visibility = if (item.isVisible == true) View.VISIBLE else View.GONE // affiche l'icone sur item est visible

        return view
    }
}