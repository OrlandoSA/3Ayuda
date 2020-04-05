package Ayuda.a3ayuda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_perfil_personal.*
import kotlinx.android.synthetic.main.activity_perfil_personal.tv_Edad
import kotlinx.android.synthetic.main.activity_perfil_personal.tv_Nombre
import kotlinx.android.synthetic.main.personal.*

class PerfilPersonalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_personal)
        var bundle=intent.extras
        if(bundle!=null){
            iv_imagenPersonal.setImageResource(bundle.getInt("imagen"))
            tv_Nombre.setText(bundle.getString("nombre"))
            tv_Edad.setText(bundle.getInt("edad").toString())
            tv_telefono.setText(bundle.getString("telefono"))
        }
    }
}
