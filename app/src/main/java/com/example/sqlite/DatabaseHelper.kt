package com.example.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    "Escuela.db",
    null,
    1
) {

    private val SQL_CREAR_TABLA = """
        CREATE TABLE ${ContratoDB.Estudiantes.TABLA} (
            ${ContratoDB.Estudiantes.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${ContratoDB.Estudiantes.COLUMNA_NOMBRE} TEXT NOT NULL,
            ${ContratoDB.Estudiantes.COLUMNA_EDAD} INTEGER NOT NULL,
            ${ContratoDB.Estudiantes.COLUMNA_CARRERA} TEXT
        )
    """.trimIndent()

    private val SQL_BORRAR_TABLA = """
        DROP TABLE IF EXISTS ${ContratoDB.Estudiantes.TABLA}
    """.trimIndent()

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREAR_TABLA)
        Log.d("SQLiteTest", "TABLA CREADA: ${ContratoDB.Estudiantes.TABLA}")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_BORRAR_TABLA)
        onCreate(db)
        Log.d("SQLiteTest", "BD ACTUALIZADA DE $oldVersion A $newVersion")
    }
}