package Ayuda.a3ayuda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_ubicacion.*

class UbicacionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ubicacion)

        btn_Acceder.setOnClickListener {
            var intent = Intent(this, MensajeActivity::class.java)
            intent.putExtra("mensaje","UBICACIÓN ENVIADA")
            intent.putExtra("siguiente", Constantes.VENTANA_PERFIL_ADULTO)
            startActivity(intent)
        }
    }
}
