package com.example.dusuncepaylas.view

import android.Manifest
import android.app.ComponentCaller
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dusuncepaylas.R
import com.example.dusuncepaylas.databinding.ActivityPaylasimBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class PaylasimActivity : AppCompatActivity() {
    var storage = Firebase.storage
    private lateinit var binding: ActivityPaylasimBinding
    val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    var secilenGorsel: Uri? = null
    var secilenBitmap: Bitmap? = null


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

        if (secilenGorsel != null) {
            val reference = storage.reference

            val uuid = UUID.randomUUID()
            var gorselIsmi = "${uuid}.jpg"

            val gorselReferansi = reference.child("gorseller").child(gorselIsmi)

            gorselReferansi.putFile(secilenGorsel!!).addOnSuccessListener { task ->
                //   url alınacak
                val yuklenenGorselReferansi = reference.child("gorseller").child(gorselIsmi)
                yuklenenGorselReferansi.downloadUrl.addOnSuccessListener { uri ->
                    val gorselUrl = uri.toString()

                    veritabaninaKaydet(gorselUrl)
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_SHORT)
                    .show()
            }
        } else {

            veritabaninaKaydet(null)

        }
    }

    private fun veritabaninaKaydet(downloadUrl: String?) {
        val paylasilan_yorum = binding.paylasimText.text.toString()
        val kullanici_adi = auth.currentUser?.email.toString()
        val tarih = Timestamp.now()

        val paylasim = hashMapOf(
            "paylasilan_yorum" to paylasilan_yorum,
            "kullanici_adi" to kullanici_adi,
            "tarih" to tarih,
        )
        if (downloadUrl != null) {
            paylasim.put("gorselUrl", downloadUrl)
        }
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


    fun gorselEkle(view: View) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // izin verilmemiş , izin istememiz gerekiyor
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )


        } else {
            //izin verilmiş,direk gelriye gidebiliriz

            val galeriIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriIntent, 2)

        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val galeriIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent, 2)
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
    }

    override fun onActivityResult(// resmi burada alıyoruz
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        caller: ComponentCaller
    ) {
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            secilenGorsel = data.data
            //     imageView.visibility = View.VISIBLE
            if (secilenGorsel != null) {
                if (Build.VERSION.SDK_INT >= 28) {
                    val source = ImageDecoder.createSource(this.contentResolver, secilenGorsel!!)
                    secilenBitmap = ImageDecoder.decodeBitmap(source)
                    //   imageView.setImageBitmap(secilenBitmap)

                } else {
                    secilenBitmap =
                        MediaStore.Images.Media.getBitmap(this.contentResolver, secilenGorsel)
                    //   imageView.setImageBitmap(secilenBitmap)
                }
            }

        }

        super.onActivityResult(requestCode, resultCode, data, caller)
    }
}