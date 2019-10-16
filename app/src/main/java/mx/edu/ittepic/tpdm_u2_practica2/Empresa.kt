package mx.edu.ittepic.tpdm_u2_practica2

import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import mx.edu.ittepic.tpdm_u2_practica2.BaseDatos

class Empresa : AppCompatActivity() {

    //EMPRESA
    var id : EditText?=null
    var buscar : ImageButton?=null
    var descripcion : EditText?=null
    var domicilio : EditText?=null
    var insertar : Button?=null
    var actualizar : Button?=null
    var eliminar : Button?=null
    var etiqueta : TextView?=null
    var regresar : Button?=null
    var basedatos = BaseDatos(this, "practica2",null,1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empresa)

        id = findViewById(R.id.idEmpresa)
        buscar = findViewById(R.id.buscarEmpresa)
        descripcion = findViewById(R.id.descripcionEmpresa)
        domicilio = findViewById(R.id.domicilioEmpresa)
        insertar = findViewById(R.id.insertarEmpresa)
        actualizar = findViewById(R.id.actualizarEmpresa)
        eliminar = findViewById(R.id.eliminarEmpresa)
        etiqueta = findViewById(R.id.etiquetaEmpresas)
        regresar = findViewById(R.id.regresarEmpresa)

        buscarEmpresas()

        buscar?.setOnClickListener {
            if(id?.text.toString().isEmpty()) {
                mensaje("ERROR","Ingresa el id de la empresa.")
            }else{
                buscarId(id?.text.toString())
            }
        }
        insertar?.setOnClickListener {
            if(descripcion?.text.toString().isEmpty()||domicilio?.text.toString().isEmpty()){
                mensaje("ERROR","Por favor, llena todos los campos.")
            }else {
                insertar(descripcion?.text.toString(), domicilio?.text.toString())
            }
        }
        actualizar?.setOnClickListener {
            if(id?.text.toString().isEmpty()||descripcion?.text.toString().isEmpty()||domicilio?.text.toString().isEmpty()){
                mensaje("ERROR","Por favor, llena todos los campos.")
            }else {
                actualizar(id?.text.toString())
            }
        }
        eliminar?.setOnClickListener {
            if(id?.text.toString().isEmpty()) {
                mensaje("ERROR","Ingresa el id de la empresa.")
            }else{
                AlertDialog.Builder(this).setTitle("ADVERTENCIA").setMessage("¿Estás seguro que deseas eliminar esta empresa?")
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
    fun insertar(descripcion: String, domicilio:String){
        try{
            var transaccion = basedatos.writableDatabase
            var SQL = "INSERT INTO EMPRESA VALUES(null,'$descripcion','$domicilio')"
            transaccion.execSQL(SQL)
            transaccion.close()
            mensaje("ÉXITO","La empresa se insertó correctamente.")
            limpiarCampos()
            buscarEmpresas()
        }catch(err: SQLiteException){
            mensaje("ERROR","No se pudo insertar la empresa.")
        }
    }
    fun buscarId(id:String){
        try{
            var transaccion = basedatos.readableDatabase
            var SQL = "SELECT * FROM EMPRESA WHERE IDEMPRESA="+id
            var resultado = transaccion.rawQuery(SQL,null)
            if(resultado.moveToFirst()){
                descripcion?.setText(resultado.getString(1))
                domicilio?.setText(resultado.getString(2))
            }else {
                mensaje("ERROR","No se encontró el id de la empresa.")
            }
            transaccion.close()

        }catch(err: SQLiteException){
            mensaje("ERROR","No se pudo realizar el select.")
        }
    }
    fun actualizar(id:String){
        try{
            var transaccion = basedatos.writableDatabase
            var SQL = "UPDATE EMPRESA SET DESCRIPCION='"+descripcion?.text.toString()+"', DOMICILIO='"+domicilio?.text.toString()+"' WHERE IDEMPRESA="+id
            transaccion.execSQL(SQL)
            transaccion.close()
            mensaje("ÉXITO","La empresa se actualizó correctamente.")
            limpiarCampos()
            buscarEmpresas()
        }catch(err: SQLiteException){
            mensaje("ERROR","No se pudo actualizar la empresa.")
        }
    }
    fun eliminar(id:String){
        try{
            var transaccion = basedatos.writableDatabase
            var SQL = "DELETE FROM EMPRESA WHERE IDEMPRESA="+id
            transaccion.execSQL(SQL)
            transaccion.close()
            mensaje("ÉXITO","La empresa se eliminó correctamente.")
            limpiarCampos()
            buscarEmpresas()
        }catch(err: SQLiteException){
            mensaje("ERROR","No se pudo eliminar la empresa.")
        }
    }

    fun buscarEmpresas(){
        try{
            var transaccion = basedatos.readableDatabase
            var SQL = "SELECT * FROM EMPRESA"
            var resultado = transaccion.rawQuery(SQL,null)
            var cadena=""
            while(resultado.moveToNext()){
                cadena = cadena + resultado.getString(0)+"        "+resultado.getString(1)+"                 "+resultado.getString(2)+"\n"
            }
            etiqueta?.setText(cadena)
        }catch(err: SQLiteException){
            mensaje("ERROR","No se pudo realizar el select.")
        }
    }
    fun limpiarCampos(){
        id?.setText("")
        descripcion?.setText("")
        domicilio?.setText("")
    }
    fun mensaje(titulo:String, mensaje:String){
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show()
    }
}
