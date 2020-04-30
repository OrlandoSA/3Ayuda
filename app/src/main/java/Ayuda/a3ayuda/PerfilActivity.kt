package Ayuda.a3ayuda

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_perfil.*


class PerfilActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        val bundle = intent.extras
        if(bundle!=null){
            val elNombre= bundle.getString("name")
            val laFoto= bundle.getString("foto")

            nombre.setText(elNombre)
        }

        btn_AccederPerfil.setOnClickListener {
            var intent = Intent(this, ListaPersonalActivity::class.java)
            startActivity(intent)
        }
    }


}
