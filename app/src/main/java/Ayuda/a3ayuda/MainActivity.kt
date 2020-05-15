package Ayuda.a3ayuda

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_AccederInicio.setOnClickListener {
            var intent = Intent(this, RegistroGoogle::class.java)
            startActivity(intent)
        }

}}
