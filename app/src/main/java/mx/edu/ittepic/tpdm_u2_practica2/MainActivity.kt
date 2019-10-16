package mx.edu.ittepic.tpdm_u2_practica2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class MainActivity : AppCompatActivity() {


    var btnEmpresas : Button?=null
    var btnClientes : Button?=null
    var btnAlmacenes : Button?=null
    var btnCompras : Button?=null
    var btnDetalleC : Button?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnEmpresas = findViewById(R.id.btnEmpresas)
        btnClientes = findViewById(R.id.btnClientes)
        btnAlmacenes = findViewById(R.id.btnAlmacenes)
        btnCompras = findViewById(R.id.btnCompras)
        btnDetalleC = findViewById(R.id.btnDetalleC)

        btnEmpresas?.setOnClickListener {
            var activity = Intent(this, Empresa::class.java)
            startActivity(activity)
        }
        btnClientes?.setOnClickListener {
            var activity = Intent(this, Cliente::class.java)
            startActivity(activity)
        }
        btnAlmacenes?.setOnClickListener {
            var activity = Intent(this, Almacen::class.java)
            startActivity(activity)
        }
        btnCompras?.setOnClickListener {
            var activity = Intent(this, Compra::class.java)
            startActivity(activity)
        }
        btnDetalleC?.setOnClickListener {
            var activity = Intent(this, DetalleCompra::class.java)
            startActivity(activity)
        }


    }

}
