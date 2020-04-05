package Ayuda.a3ayuda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sugerencias.*

class SugerenciasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sugerencias)

        btn_Enviar.setOnClickListener {
            var intent = Intent(this, MensajeActivity::class.java)
            intent.putExtra("mensaje","MENSAJE ENVIADO")
            intent.putExtra("siguiente", Constantes.VENTANA_LISTA_PERSONAL)
            startActivity(intent)
        }

    }
}
