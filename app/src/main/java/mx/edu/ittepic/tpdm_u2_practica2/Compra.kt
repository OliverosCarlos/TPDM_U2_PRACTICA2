package mx.edu.ittepic.tpdm_u2_practica2

import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import mx.edu.ittepic.tpdm_u2_practica2.BaseDatos

class Compra : AppCompatActivity() {

    var id : EditText?=null
    var buscar : ImageButton?=null
    var fecha : EditText?=null
    var noTel : EditText?=null
    var total : EditText?=null
    var insertar : Button?=null
    var actualizar : Button?=null
    var eliminar : Button?=null
    var etiqueta : TextView?=null
    var regresar : Button?=null
    var basedatos = BaseDatos(this, "practica2",null,1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compra)

        id = findViewById(R.id.idCompra)
        buscar = findViewById(R.id.buscarCompra)
        fecha = findViewById(R.id.fechaCompra)
        noTel = findViewById(R.id.noTelClienteCompra)
        total = findViewById(R.id.totalCompra)
        insertar = findViewById(R.id.insertarCompra)
        actualizar = findViewById(R.id.actualizarCompra)
        eliminar = findViewById(R.id.eliminarCompra)
        etiqueta = findViewById(R.id.etiquetaCompra)
        regresar = findViewById(R.id.regresarCompra)

        buscarCompra()

        buscar?.setOnClickListener {
            if(id?.text.toString().isEmpty()) {
                mensaje("ERROR","Ingresa el id de la compra")
            }else{
                buscarId(id?.text.toString())
            }
        }
        insertar?.setOnClickListener {
            if(fecha?.text.toString().isEmpty()||noTel?.text.toString().isEmpty()||total?.text.toString().isEmpty()){
                mensaje("ERROR","Por favor, llena todos los campos.")
            }else {
                insertar(fecha?.text.toString(), noTel?.text.toString(),total?.text.toString())
            }
        }
        actualizar?.setOnClickListener {
            if(id?.text.toString().isEmpty()||fecha?.text.toString().isEmpty()||noTel?.text.toString().isEmpty()||total?.text.toString().isEmpty()){
                mensaje("ERROR","Por favor, llena todos los campos.")
            }else {
                actualizar(id?.text.toString())
            }
        }
        eliminar?.setOnClickListener {
            if(id?.text.toString().isEmpty()) {
                mensaje("ERROR","Ingresa el id de la compra..")
            }else{
                AlertDialog.Builder(this).setTitle("ADVERTENCIA").setMessage("¿Estás seguro que deseas eliminar esta compra?")
                    .setPositiveButton("Sí"){dialog,which->
                        eliminar(id?.text.toString())
                    }.setNeutralButton("No"){dialog,which->
                        return@setNeutralButton
                    }.show()
            }
        }
        regresar?.setOnClickListener{
            finish()
        }
    }
    fun insertar(fecha: String, noTel:String, total:String){
        try{
            var transaccion = basedatos.writableDatabase
            var SQL = "INSERT INTO COMPRA VALUES(null,'$fecha','$noTel',$total)"
            transaccion.execSQL(SQL)
            transaccion.close()
            mensaje("ÉXITO","La compra se insertó correctamente.")
            limpiarCampos()
            buscarCompra()
        }catch(err: SQLiteException){
            mensaje("ERROR","No se pudo insertar la compra.")
        }
    }
    fun buscarId(id:String){
        try{
            var transaccion = basedatos.readableDatabase
            var SQL = "SELECT * FROM COMPRA WHERE IDCOMPRA="+id
            var resultado = transaccion.rawQuery(SQL,null)
            if(resultado.moveToFirst()){
                fecha?.setText(resultado.getString(1))
                noTel?.setText(resultado.getString(2))
                total?.setText(resultado.getString(3))
            }else {
                mensaje("ERROR","No se encontró el id de la compra.")
            }
            transaccion.close()

        }catch(err: SQLiteException){
            mensaje("ERROR","No se pudo realizar el select.")
        }
    }
    fun actualizar(id:String){
        try{
            var transaccion = basedatos.writableDatabase
            var SQL = "UPDATE COMPRA SET FECHA='"+fecha?.text.toString()+"', IDCLIENTE='"+noTel?.text.toString()+"', TOTAL="+total?.text.toString()+" WHERE IDCOMPRA="+id
            transaccion.execSQL(SQL)
            transaccion.close()
            mensaje("ÉXITO","La compra se actualizó correctamente.")
            limpiarCampos()
            buscarCompra()
        }catch(err: SQLiteException){
            mensaje("ERROR","No se pudo actualizar la compra.")
        }
    }
    fun eliminar(id:String){
        try{
            var transaccion = basedatos.writableDatabase
            var SQL = "DELETE FROM COMPRA WHERE IDCOMPRA="+id
            transaccion.execSQL(SQL)
            transaccion.close()
            mensaje("ÉXITO","La compra se eliminó correctamente.")
            limpiarCampos()
            buscarCompra()
        }catch(err: SQLiteException){
            mensaje("ERROR","No se pudo eliminar la compra.")
        }
    }

    fun buscarCompra(){
        try{
            var transaccion = basedatos.readableDatabase
            var SQL = "SELECT * FROM COMPRA"
            var resultado = transaccion.rawQuery(SQL,null)
            var cadena=""
            while(resultado.moveToNext()){
                cadena = cadena + resultado.getString(0)+"       "+resultado.getString(1)+"       "+resultado.getString(2)+"         "+resultado.getString(3)+"\n"
            }
            etiqueta?.setText(cadena)
        }catch(err: SQLiteException){
            mensaje("ERROR","No se pudo realizar el select.")
        }
    }
    fun limpiarCampos(){
        id?.setText("")
        fecha?.setText("")
        noTel?.setText("")
        total?.setText("")
    }
    fun mensaje(titulo:String, mensaje:String){
        Toast.makeText(this,mensaje, Toast.LENGTH_SHORT).show()
    }
}
