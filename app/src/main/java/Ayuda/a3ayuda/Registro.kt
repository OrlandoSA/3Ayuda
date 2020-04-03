package Ayuda.a3ayuda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_registro.*

class Registro : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        btn_Acceder.setOnClickListener {
            var intent = Intent(this, Perfil::class.java)
            startActivity(intent)
        }
    }
}
