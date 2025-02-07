package com.example.dusuncepaylas

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dusuncepaylas.databinding.ActivityMainBinding
import com.example.dusuncepaylas.databinding.ActivityPaylasimBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PaylasimActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaylasimBinding
    val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_paylasim)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun paylas(view: View) {
        val paylasilan_yorum = binding.paylasimText.text.toString()
        val kullanici_adi = auth.currentUser?.email.toString()
        val tarih = Timestamp.now()

        val paylasim = hashMapOf(
            "paylasilan_yorum" to paylasilan_yorum,
            "kullanici_adi" to kullanici_adi,
            "tarih" to tarih,
        )
        //   val paylasimMap= hashMapOf<String,Any>()   böyle de olur
        //   paylasimMap.put("paylasilan_yorum",paylasimMap)


        db.collection("Paylasimlar").add(paylasim).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_SHORT).show()
        }

    }

}