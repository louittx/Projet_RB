package com.example.rb_bluetooth

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast

class Heart(
    private val heartOnFire: ImageView,
    private val brokenHeart: ImageView,
    private val mendingHeart: ImageView,
    private val heartSuit: ImageView
) {
    // États possibles avec une valeur par défaut "null" pour aucun cœur

    fun updateState(state: Int) {
        // Reset toutes les visibilités
        heartOnFire.visibility = View.GONE
        brokenHeart.visibility = View.GONE
        mendingHeart.visibility = View.GONE
        heartSuit.visibility = View.GONE

        // Affiche seulement l'image correspondante
        when (state) {
            4 -> heartOnFire.visibility = View.VISIBLE
            1 -> brokenHeart.visibility = View.VISIBLE
            2 -> mendingHeart.visibility = View.VISIBLE
            3 -> heartSuit.visibility = View.VISIBLE
            else -> {} // Aucun cœur visible
        }
    }
}

class Body(
    private val robot_avantbras_droit : ImageView,
    private val robot_avantbras_gauche : ImageView,
    private val robot_bras_droit : ImageView,
    private val robot_bras_gauche : ImageView,
    private val robot_mains_droit : ImageView,
    private val robot_mains_gauche : ImageView,
    private val robot_oreille_droit : ImageView,
    private val robot_oreille_gauche : ImageView,
    private val robot_tors_droit : ImageView,
    private val robot_tors_gauche : ImageView,
    private val robot_tete : ImageView,
    private val robot_tors_2 : ImageView,
    private val robot_tors_3 : ImageView,
    private val robot_tors : ImageView
){
    fun updateBody(state: Int) {
        // D'abord cacher toutes les parties
        robot_avantbras_droit.visibility = View.GONE
        robot_avantbras_gauche.visibility = View.GONE
        robot_bras_droit.visibility = View.GONE
        robot_bras_gauche.visibility = View.GONE
        robot_mains_droit.visibility = View.GONE
        robot_mains_gauche.visibility = View.GONE
        robot_oreille_droit.visibility = View.GONE
        robot_oreille_gauche.visibility = View.GONE
        robot_tors_droit.visibility = View.GONE
        robot_tors_gauche.visibility = View.GONE
        robot_tete.visibility = View.GONE
        robot_tors_2.visibility = View.GONE
        robot_tors_3.visibility = View.GONE
        robot_tors.visibility = View.GONE

        // Ensuite afficher seulement les parties activées par les bits
        robot_avantbras_droit.visibility = if((state and 0b1) == 1) View.VISIBLE else View.GONE
        robot_avantbras_gauche.visibility = if((state shr 1 and 0b1) == 1) View.VISIBLE else View.GONE
        robot_bras_droit.visibility = if((state shr 2 and 0b1) == 1) View.VISIBLE else View.GONE
        robot_bras_gauche.visibility = if((state shr 3 and 0b1) == 1) View.VISIBLE else View.GONE
        robot_mains_droit.visibility = if((state shr 4 and 0b1) == 1) View.VISIBLE else View.GONE
        robot_mains_gauche.visibility = if((state shr 5 and 0b1) == 1) View.VISIBLE else View.GONE
        robot_oreille_droit.visibility = if((state shr 6 and 0b1) == 1) View.VISIBLE else View.GONE
        robot_oreille_gauche.visibility = if((state shr 7 and 0b1) == 1) View.VISIBLE else View.GONE
        robot_tors_droit.visibility = if((state shr 8 and 0b1) == 1) View.VISIBLE else View.GONE
        robot_tors_gauche.visibility = if((state shr 9 and 0b1) == 1) View.VISIBLE else View.GONE
        robot_tete.visibility = if((state shr 10 and 0b1) == 1) View.VISIBLE else View.GONE
        robot_tors_2.visibility = if((state shr 11 and 0b1) == 1) View.VISIBLE else View.GONE
        robot_tors_3.visibility = if((state shr 12 and 0b1) == 1) View.VISIBLE else View.GONE
        robot_tors.visibility = if((state shr 13 and 0b1) == 1) View.VISIBLE else View.GONE
    }
}