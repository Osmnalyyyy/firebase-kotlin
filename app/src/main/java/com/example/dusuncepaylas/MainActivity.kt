package com.example.dusuncepaylas

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.dusuncepaylas.databinding.ActivityMainBinding
import com.example.dusuncepaylas.ui.theme.DusuncePaylasTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding // Binding'i sınıf seviyesinde tanımla


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // Burada inflate et
        setContentView(binding.root)

        auth = Firebase.auth

        val guncelKullanici = auth.currentUser

        if (guncelKullanici != null) {
         //   val intent = Intent(this, DusunceActivity::class.java)
         //   startActivity(intent)
         //   finish()
        }
    }

        fun kayitOl(view: View) {
            val email = binding.emailText.text.toString()
            val password = binding.passwordText.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                    //   val intent = Intent(
                    //       this,
                    //       DusunceActivity::class.java
                    //   )// istenilen sayfaya yönlendirme yapıyoruz
                    //   startActivity(intent);
                    //   finish()
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }

                }.addOnFailureListener { exception ->
                    Toast.makeText(
                        applicationContext,
                        exception.localizedMessage,
                        Toast.LENGTH_SHORT
                    )
                        .show();
                }
        }

        fun girisYap(view: View) { // View parametresi ekle
            val email = binding.emailText.text.toString()
            val password = binding.passwordText.text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser?.email.toString()
                        Toast.makeText(applicationContext, "Hoşgeldin :${user}", Toast.LENGTH_SHORT)
                            .show()

                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(
                        applicationContext,
                        exception.localizedMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DusuncePaylasTheme {
        Greeting("Android")
    }
}