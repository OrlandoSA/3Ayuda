package Ayuda.a3ayuda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_Acceder.setOnClickListener {
            var intent = Intent(this, Registro::class.java)
            startActivity(intent)
        }
    }

}
