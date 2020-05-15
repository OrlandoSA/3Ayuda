package Ayuda.a3ayuda

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_msg.*
import kotlin.reflect.KClass


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MensajeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_msg)
        var mostrarMensaje= intent.getStringExtra("mensaje")
        var siguienteVentana= intent.getStringExtra("siguiente")
        mensaje_texto.text=mostrarMensaje

        btn_Aceptar.setOnClickListener {
            if(siguienteVentana.equals(Constantes.VENTANA_BUZON_SUGERENCIAS)) {
                var intent = Intent(this, SugerenciasActivity::class.java)
                startActivity(intent)

            }else if(siguienteVentana.equals(Constantes.VENTANA_LISTA_PERSONAL)){
                var intent = Intent(this, ListaPersonalActivity::class.java)
                startActivity(intent)
            }else if(siguienteVentana.equals(Constantes.VENTANA_PERFIL_ADULTO)){
                var intent = Intent(this, PerfilActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
