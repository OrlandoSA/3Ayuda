package Ayuda.a3ayuda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_perfil_personal.*
import kotlinx.android.synthetic.main.activity_perfil_personal.tv_Edad
import kotlinx.android.synthetic.main.activity_perfil_personal.tv_Nombre

class PerfilPersonalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_personal)
        var bundle=intent.extras
        if(bundle!=null){
            var idimagen=bundle.getString("imagen")
            tv_Nombre.text = bundle.getString("nombre")
            tv_Edad.text = bundle.getInt("edad").toString()+" años"

            if(idimagen!="") {
                var algo= FirebaseStorage.getInstance().reference
                val equis=algo.child(idimagen.toString())

                equis.downloadUrl.addOnSuccessListener {Uri->
                    val imageURL = Uri.toString()
                    Glide.with(this)
                        .load(imageURL)
                        .into(iv_imagenPersonal)
                }
            }
        }
        btn_AccederPersonal.setOnClickListener {
            var intent = Intent(this, UbicacionGPSActivity::class.java)
            if (bundle != null) {
                intent.putExtra("idServicio",bundle.getString("idServicio"))
            }
            startActivity(intent)
        }

        btn_Cancelar.setOnClickListener {
            var intent = Intent(this, ListaPersonalActivity::class.java)
            startActivity(intent)
        }
    }
}
