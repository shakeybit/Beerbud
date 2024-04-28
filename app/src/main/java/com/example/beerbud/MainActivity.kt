package com.example.beerbud


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.beerbud.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private lateinit var firebaseReference : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseReference = FirebaseDatabase.getInstance("https://beerbud-ca341-default-rtdb.europe-west1.firebasedatabase.app/").getReference("test")

        binding.tvSendData.setOnClickListener {

            firebaseReference.setValue("beers")
                .addOnCompleteListener {
                    Toast.makeText(this, " data stored successfully", Toast.LENGTH_SHORT).show()
                }

        }
    }
}