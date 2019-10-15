package mx.edu.ittepic.myhomwor

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBase(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase?) {
      /*db?.execSQL("CREATE TABLE grupo(" +
                "id_grupo INTEGER PRIMARY KEY NOT NULL AUTOINCREMENT," +
                "nombre_participante VARCHAR(20)," +
                "descripcion_grupo VARCHAR(50)," +
                ")")

        db?.execSQL("CREATE TABLE objetivos(" +
                "id_objetivo INTEGER PRIMARY KEY NOT NULL AUTOINCREMENT," +
                "nombre_objetivo VARCHAR(20)," +
                "descripcion_objetivo VARCHAR(50)," +
                "estao_obejtivo" +
                "participante INTEGER" +
                ")")*/
        db?.execSQL("CREATE TABLE listas(" +
                "id_lista INTEGER PRIMARY KEY NOT NULL," +
                "nombre_lista VARCHAR(20)," +
                "descripcion_lista VARCHAR(50)," +
                "fecha_creacion_lista DATE," +
                "fecha_finalizacion_lista DATE," +
                "estado_lista CHAR," +
                "hora_lista TIME" +
                ")")

        db?.execSQL("CREATE TABLE tareas(" +
                "id_tarea INTEGER PRIMARY KEY NOT NULL, " +
                "nombre_tarea VARCHAR(20)," +
                "descripcion_tarea VARCHAR(50)," +
                "fecha_creacion_tarea DATE," +
                "fecha_finalizacion_tarea DATE," +
                "estado_tarea CHAR," +
                "fk_id_lista INTEGER,"+
                "FOREIGN KEY(fk_id_lista) REFERENCES listas(id_lista)" +
                ");")
        /*db?.execSQL("CREATE TABLE objetivos(" +
                "id_objetivo INTEGER PRIMARY KEY NOT NULL," +
                "nombre_objetivo VARCHAR(20)," +
                "descripcion_objetivo VARCHAR(50),"+
                "fk_id_tarea INTEGER,"+
                "FOREIGN KEY(fk_id_tarea) REFERENCES tareas(id_tarea)" +
                ");")*/

    }



    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }
}

