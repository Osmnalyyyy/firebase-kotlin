package com.example.dusuncepaylas.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dusuncepaylas.R
import com.example.dusuncepaylas.model.Paylasim
import com.squareup.picasso.Picasso

class DusunceAdapter(val paylasimListesi: ArrayList<Paylasim>) :
    RecyclerView.Adapter<DusunceAdapter.PaylasimHolder>() {


    class PaylasimHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaylasimHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_row, parent, false)
        return PaylasimHolder(view)
    }

    override fun getItemCount(): Int {
        return paylasimListesi.size
    }


    override fun onBindViewHolder(holder: PaylasimHolder, position: Int) {
        //  holder.itemView.recycler_row_kullanici_adi.text=paylasimListesi[position].kullaniciAdi
        //  holder.itemView.recycler_row_paylasim_mesaji.text=paylasimListesi[position].kullaniciYorum

        if (paylasimListesi[position].gorselUrl != null) {
         //   holder.itemView.recycler_row_imageview.visibility=View.VISIBLE
          //  Picasso.get().load(paylasimListesi[position].gorselUrl).into(holder.itemView.recycler_row_imageview)   resmi burda gösterdik
        }
    }

}