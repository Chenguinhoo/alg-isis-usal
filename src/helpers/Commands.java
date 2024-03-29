package helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import isis.Isis;

/*
 * La clase de apoyo Commands tiene los metodos para crear un
 * script temporal que compruebe si los ficheros del equipo
 * son iguales (es decir, que se ha realizado bien el algoritmo
 * de multidifusion ordenada)
 */

public class Commands {
	
	//Metodo que ejecuta el script temporal creado y posteriormente lo borra
	public void Exec() {
		
		File temp = MakeTempScript();
		
		try {
			
			ProcessBuilder pb = new ProcessBuilder("bash", temp.toString());
			pb.inheritIO();
			
			Process p = pb.start();
			p.waitFor();
			
		} catch (IOException | InterruptedException e) {
			
			e.printStackTrace();
			
		} finally {
			
			temp.delete();
			
		}
		
	}
	
	
	/*
	 * Creacion del script temporal
	 * 
	 * Escribira un script bash que haga un diff de los log del equipo
	 */
	public File MakeTempScript() {
		
		try {
			
			File f = File.createTempFile("script", null);
			
			Writer w = new OutputStreamWriter(new FileOutputStream(f));
			PrintWriter pw = new PrintWriter(w);
			
			pw.println("#!/bin/bash");
			pw.println("cd ~/isis");
			pw.println("echo 'Utilizamos comando diff para comprobar si los ficheros son iguales...'");
			
			for(int i = 1; i < 2; i++) {
				
				pw.println("echo 'diff de " 
						+ Isis.listaProcesos.get(0).GetFileLog().GetFileName()
						+ " y "
						+ Isis.listaProcesos.get(i).GetFileLog().GetFileName()
						+ "'");
				
				pw.println("diff -c "
					+ Isis.listaProcesos.get(0).GetFileLog().GetFileName() 
					+ " "
					+ Isis.listaProcesos.get(i).GetFileLog().GetFileName());
				
			}
			
			pw.close();
			
			return f;
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
			return null;
			
		}
		
	}
	
}
