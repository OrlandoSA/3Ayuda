package Ayuda.a3ayuda

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import android.widget.ImageView;
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.android.synthetic.main.activity_perfil.*


class PerfilActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            nombre.setText(acct.givenName + " " + acct.familyName)
            val fotoPerfil: Uri? = acct?.photoUrl
            if(fotoPerfil!=null)
                Picasso.get().load(fotoPerfil).into(foto)
        }

        btn_AccederPerfil.setOnClickListener {
            var intent = Intent(this, ListaPersonalActivity::class.java)
            startActivity(intent)
        }
    }


}
