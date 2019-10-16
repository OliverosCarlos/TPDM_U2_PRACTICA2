package mx.edu.ittepic.tpdm_u2_practica2

import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog

class Cliente : AppCompatActivity() {

    var noTelBus: EditText? = null
    var buscar: ImageButton? = null
    var noTel: EditText? = null
    var nombre: EditText? = null
    var domicilio: EditText? = null
    var idEmpresa: EditText? = null
    var insertar: Button? = null
    var actualizar: Button? = null
    var eliminar: Button? = null
    var etiqueta: TextView? = null
    var regresar: Button? = null
    var basedatos = BaseDatos(this, "practica2", null, 1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente)

        noTelBus = findViewById(R.id.noTelClienteBus)
        buscar = findViewById(R.id.buscarCliente)
        noTel = findViewById(R.id.noTelCliente)
        nombre = findViewById(R.id.nombreCliente)
        domicilio = findViewById(R.id.domicilioCliente)
        idEmpresa = findViewById(R.id.idEmpresaCliente)
        insertar = findViewById(R.id.insertarCliente)
        actualizar = findViewById(R.id.actualizarCliente)
        eliminar = findViewById(R.id.eliminarCliente)
        etiqueta = findViewById(R.id.etiquetaClientes)
        regresar = findViewById(R.id.regresarCliente)

        buscarClientes()

        buscar?.setOnClickListener {
            if(noTelBus?.text.toString().isEmpty()) {
                mensaje("ERROR","Ingresa el número de teléfono del cliente.")
            }else{
                buscarId(noTelBus?.text.toString())
            }
        }
        insertar?.setOnClickListener {
            if (noTel?.text.toString().isEmpty() || nombre?.text.toString().isEmpty() || domicilio?.text.toString().isEmpty() || idEmpresa?.text.toString().isEmpty()) {
                mensaje("ERROR", "Por favor, llena todos los campos.")
            } else {
                insertar(noTel?.text.toString(), nombre?.text.toString(), domicilio?.text.toString(), idEmpresa?.text.toString())
            }
        }
        actualizar?.setOnClickListener {
            if(noTelBus?.text.toString().isEmpty()||noTel?.text.toString().isEmpty()||nombre?.text.toString().isEmpty()||domicilio?.text.toString().isEmpty()||idEmpresa?.text.toString().isEmpty()){
                mensaje("ERROR","Por favor, llena todos los campos.")
            }else {
                actualizar(noTelBus?.text.toString())
            }
        }
        eliminar?.setOnClickListener {
            if(noTelBus?.text.toString().isEmpty()) {
                mensaje("ERROR","Ingresa el número de teléfono del cliente.")
            }else{
                AlertDialog.Builder(this).setTitle("ADVERTENCIA").setMessage("¿Estás seguro que deseas eliminar este cliente?")
                    .setPositiveButton("Sí"){dialog,which->
                        eliminar(noTelBus?.text.toString())
                    }.setNeutralButton("No"){dialog,which->
                        return@setNeutralButton
                    }.show()
            }
        }
        regresar?.setOnClickListener{
            finish()
        }
    }
    fun buscarClientes(){
        try{
            var transaccion = basedatos.readableDatabase
            var SQL = "SELECT * FROM CLIENTE"
            var resultado = transaccion.rawQuery(SQL,null)
            var cadena=""
            while(resultado.moveToNext()){
                cadena = cadena + resultado.getString(0)+"    "+resultado.getString(1)+"       "+resultado.getString(2)+"        "+resultado.getString(3)+"\n"
            }
            etiqueta?.setText(cadena)
        }catch(err: SQLiteException){
            mensaje("ERROR","No se pudo realizar el select.")
        }
    }
    fun insertar(noTel:String, nombre:String, domicilio:String, idEmpresa:String){
        try{
            var transaccion = basedatos.writableDatabase
            var SQL = "INSERT INTO CLIENTE VALUES($noTel,'$nombre','$domicilio',$idEmpresa)"
            transaccion.execSQL(SQL)
            transaccion.close()
            mensaje("ÉXITO","El cliente se insertó correctamente.")
            limpiarCampos()
            buscarClientes()
        }catch(err: SQLiteException){
            mensaje("ERROR","No se pudo insertar el cliente.")
        }
    }
    fun buscarId(num:String){
        try{
            var transaccion = basedatos.readableDatabase
            var SQL = "SELECT * FROM CLIENTE WHERE NOTELEFONO="+num
            var resultado = transaccion.rawQuery(SQL,null)
            if(resultado.moveToFirst()){
                noTel?.setText(resultado.getString(0))
                nombre?.setText(resultado.getString(1))
                domicilio?.setText(resultado.getString(2))
                idEmpresa?.setText(resultado.getString(3))
            }else {
                mensaje("ERROR","No se encontró el id del cliente.")
            }
            transaccion.close()

        }catch(err: SQLiteException){
            mensaje("ERROR","No se pudo realizar el select.")
        }
    }
    fun actualizar(num:String){
        try{
            var transaccion = basedatos.writableDatabase
            var SQL = "UPDATE CLIENTE SET NOTELEFONO='"+noTel?.text.toString()+"', NOMBRE='"+nombre?.text.toString()+"', DOMICILIO='"+domicilio?.text.toString()+"', IDEMPRESA="+idEmpresa?.text.toString()+" WHERE NOTELEFONO="+num
            transaccion.execSQL(SQL)
            transaccion.close()
            mensaje("ÉXITO","El cliente se actualizó correctamente.")
            limpiarCampos()
            buscarClientes()
        }catch(err: SQLiteException){
            mensaje("ERROR","No se pudo actualizar el cliente.")
        }
    }
    fun eliminar(num:String){
        try{
            var transaccion = basedatos.writableDatabase
            var SQL = "DELETE FROM CLIENTE WHERE NOTELEFONO="+num
            transaccion.execSQL(SQL)
            transaccion.close()
            mensaje("ÉXITO","El cliente se eliminó correctamente.")
            limpiarCampos()
            buscarClientes()
        }catch(err: SQLiteException){
            mensaje("ERROR","No se pudo eliminar el cliente.")
        }
    }
    fun limpiarCampos(){
        noTelBus?.setText("")
        noTel?.setText("")
        nombre?.setText("")
        domicilio?.setText("")
        idEmpresa?.setText("")
    }
    fun mensaje(titulo:String, mensaje:String){
        Toast.makeText(this,mensaje, Toast.LENGTH_SHORT).show()
    }
}
