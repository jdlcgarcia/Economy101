package com.bobotosoft.economy101;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
	public static final String TABLE_NAME = "contactos";
	public static final String CN_ID = "_id";
	public static final String CN_NAME = "nombre";
	public static final String CN_PHONE = "telefono";
	
	public static final String CREATE_TABLE = "create table "+TABLE_NAME+ "("
			+CN_ID+" integer primary key autoincrement,"
			+CN_NAME+" text not null,"
			+CN_PHONE+" text);";
	
	private DBHelper helper;
	private SQLiteDatabase db;
	
	public DBManager(Context context)
	{
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
	}
	
	public ContentValues generarContentValues(String nombre, String telefono)
	{
		ContentValues valores = new ContentValues();
		valores.put(CN_NAME, nombre);
		valores.put(CN_PHONE, telefono);
		
		return valores;
	}
	
	public void insertar(String nombre, String telefono){
		db.insert(TABLE_NAME, null, generarContentValues(nombre,telefono));
	}
	
	public void eliminar(String nombre)
	{
			db.delete(TABLE_NAME, CN_NAME+"=?", new String[]{nombre});
	}
	
	public void modificarTelefono(String nombre, String telefono){
		db.update(TABLE_NAME, generarContentValues(nombre, telefono), CN_NAME+"=?", new String[]{nombre});
	}
}
