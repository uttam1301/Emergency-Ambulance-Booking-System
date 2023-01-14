package com.uttamsaha.emergencyambulance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.uttamsaha.emergencyambulance.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var firebaseAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener{
            val email = binding.etLogin.text.toString()
            val pass = binding.etPassword.text.toString()
            if(email.isNotEmpty() && pass.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener{
                    if(it.isSuccessful){
                        val intent = Intent(this,FirstActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this, "Incorrect credentials !!", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this, "Empty fields are not allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnSignUp.setOnClickListener{
            val intent = Intent(this,UserSigninActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onStart(){
        super.onStart()
        if(firebaseAuth.currentUser != null){
            val intent = Intent(this,FirstActivity::class.java)
            startActivity(intent)
        }
    }
}