package mx.edu.ittepic.tpdm_u2_practica2

import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog

class Almacen : AppCompatActivity() {

    var id : EditText?=null
    var buscar : ImageButton?=null
    var producto : EditText?=null
    var cantidad : EditText?=null
    var precio : EditText?=null
    var insertar : Button?=null
    var actualizar : Button?=null
    var eliminar : Button?=null
    var etiqueta : TextView?=null
    var regresar : Button?=null
    var basedatos = BaseDatos(this, "practica2",null,1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_almacen)

        id = findViewById(R.id.idAlmacen)
        buscar = findViewById(R.id.buscarAlmacen)
        producto = findViewById(R.id.productoAlmacen)
        cantidad = findViewById(R.id.cantidadAlmacen)
        precio = findViewById(R.id.precioAlmacen)
        insertar = findViewById(R.id.insertarAlmacen)
        actualizar = findViewById(R.id.actualizarAlmacen)
        eliminar = findViewById(R.id.eliminarAlmacen)
        etiqueta = findViewById(R.id.etiquetaAlmacen)
        regresar = findViewById(R.id.regresarAlmacen)

        buscarAlmacen()

        buscar?.setOnClickListener {
            if(id?.text.toString().isEmpty()) {
                mensaje("ERROR","Ingresa el id del almacen")
            }else{
                buscarId(id?.text.toString())
            }
        }
        insertar?.setOnClickListener {
            if(producto?.text.toString().isEmpty()||cantidad?.text.toString().isEmpty()||precio?.text.toString().isEmpty()){
                mensaje("ERROR","Por favor, llena todos los campos.")
            }else {
                insertar(producto?.text.toString(), cantidad?.text.toString(),precio?.text.toString())
            }
        }
        actualizar?.setOnClickListener {
            if(id?.text.toString().isEmpty()||producto?.text.toString().isEmpty()||cantidad?.text.toString().isEmpty()||precio?.text.toString().isEmpty()){
                mensaje("ERROR","Por favor, llena todos los campos.")
            }else {
                actualizar(id?.text.toString())
            }
        }
        eliminar?.setOnClickListener {
            if(id?.text.toString().isEmpty()) {
                mensaje("ERROR","Ingresa el id del almacen.")
            }else{
                AlertDialog.Builder(this).setTitle("ADVERTENCIA").setMessage("¿Estás seguro que deseas eliminar este almacen?")
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
    fun insertar(producto: String, cantidad:String, precio:String){
        try{
            var transaccion = basedatos.writableDatabase
            var SQL = "INSERT INTO ALMACEN VALUES(null,'$producto','$cantidad','$precio')"
            transaccion.execSQL(SQL)
            transaccion.close()
            mensaje("ÉXITO","El almacen se insertó correctamente.")
            limpiarCampos()
            buscarAlmacen()
        }catch(err: SQLiteException){
            mensaje("ERROR","No se pudo insertar el almacen.")
        }
    }
    fun buscarId(id:String){
        try{
            var transaccion = basedatos.readableDatabase
            var SQL = "SELECT * FROM ALMACEN WHERE IDALMACEN="+id
            var resultado = transaccion.rawQuery(SQL,null)
            if(resultado.moveToFirst()){
                producto?.setText(resultado.getString(1))
                cantidad?.setText(resultado.getString(2))
                precio?.setText(resultado.getString(3))
            }else {
                mensaje("ERROR","No se encontró el id del almacen.")
            }
            transaccion.close()

        }catch(err: SQLiteException){
            mensaje("ERROR","No se pudo realizar el select.")
        }
    }
    fun actualizar(id:String){
        try{
            var transaccion = basedatos.writableDatabase
            var SQL = "UPDATE ALMACEN SET PRODUCTO='"+producto?.text.toString()+"', CANTIDAD='"+cantidad?.text.toString()+"', PRECIO='"+precio?.text.toString()+"' WHERE IDALMACEN="+id
            transaccion.execSQL(SQL)
            transaccion.close()
            mensaje("ÉXITO","El almacen se actualizó correctamente.")
            limpiarCampos()
            buscarAlmacen()
        }catch(err: SQLiteException){
            mensaje("ERROR","No se pudo actualizar el almacen.")
        }
    }
    fun eliminar(id:String){
        try{
            var transaccion = basedatos.writableDatabase
            var SQL = "DELETE FROM ALMACEN WHERE IDALMACEN="+id
            transaccion.execSQL(SQL)
            transaccion.close()
            mensaje("ÉXITO","El almacen se eliminó correctamente.")
            limpiarCampos()
            buscarAlmacen()
        }catch(err: SQLiteException){
            mensaje("ERROR","No se pudo eliminar el almacen.")
        }
    }

    fun buscarAlmacen(){
        try{
            var transaccion = basedatos.readableDatabase
            var SQL = "SELECT * FROM ALMACEN"
            var resultado = transaccion.rawQuery(SQL,null)
            var cadena=""
            while(resultado.moveToNext()){
                cadena = cadena + resultado.getString(0)+"       "+resultado.getString(1)+"                 "+resultado.getString(2)+"              "+resultado.getString(3)+"\n"
            }
            etiqueta?.setText(cadena)
        }catch(err: SQLiteException){
            mensaje("ERROR","No se pudo realizar el select.")
        }
    }
    fun limpiarCampos(){
        id?.setText("")
        producto?.setText("")
        cantidad?.setText("")
        precio?.setText("")
    }
    fun mensaje(titulo:String, mensaje:String){
        Toast.makeText(this,mensaje, Toast.LENGTH_SHORT).show()
    }
}
