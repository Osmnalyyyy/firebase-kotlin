package com.example.dusuncepaylas.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dusuncepaylas.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Locale

class DusunceActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = getMenuInflater()
        menuInflater.inflate(R.menu.ana_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.cikis_yap) {
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else if (item.itemId == R.id.paylasim_yap) {
            val intent = Intent(this, PaylasimActivity::class.java)
            startActivity(intent)
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dusunce)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth

        // Listener'ı daha sonra kaldırmak için değişkene atayalım
        var paylasimListener: ListenerRegistration? = null

        paylasimListener = db.collection("Paylasimlar")
            .addSnapshotListener { querySnapshot, firestoreError ->
                // Hata kontrolü
                if (firestoreError != null) {
                    Log.e("Firestore", "Dinleme hatası: ${firestoreError.localizedMessage}")
                    Toast.makeText(this, "Veri alınamadı", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                // Null kontrolü
                val documents = querySnapshot?.documents ?: run {
                    Toast.makeText(this, "Veri bulunamadı", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                // Verileri işleme
                for (document in documents) {
                    // Güvenli veri çekme
                    val kullaniciAdi =
                        document.getString("kullanici_adi")?.takeIf { it.isNotEmpty() }
                            ?: "Bilinmiyor"
                    val paylasilanYorum = document.getString("paylasilan_yorum") ?: "Yorum yok"
                    val tarih =
                        document.getDate("tarih")?.toString() ?: "Tarih yok" // Tarih için Date tipi

                    // Verileri kullanma
                    Log.d(
                        "Paylasim", """
                Kullanıcı: $kullaniciAdi
                Yorum: $paylasilanYorum
                Tarih: ${
                            SimpleDateFormat(
                                "dd/MM/yyyy HH:mm",
                                Locale.getDefault()
                            ).format(document.getDate("tarih"))
                        }
            """.trimIndent()
                    )
                }
            }
    }

}


