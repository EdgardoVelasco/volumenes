package com.netec.appvolumenesdocker.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.netec.appvolumenesdocker.entities.Comentario;

@Service
public class ComentarioServiceImpl implements IComentarioService {
	private static final Logger LOG=LoggerFactory.getLogger(ComentarioServiceImpl.class);
	
	private static Path pathTemporal=Paths.get("./carpetaT");
	private static Path pathPermanente=Paths.get("./carpetaP");
	
	private static void crearDirectorios(){
		try{
			Files.createDirectories(pathTemporal);
			Files.createDirectories(pathPermanente);
			LOG.info("Carpetas creadas correctamente");

		}catch(IOException ex){
           LOG.error("ERROR CREAR CARPETAS: "+ex);
		}	
	}


	@Override
	public void comentarioTemporal(Comentario comentario) {
		var pathF=pathTemporal.resolve("archivoT.txt");
		if(!Files.exists(pathF)) {
		 try {
            
			Files.createFile(pathF);
			insertarTexto(comentario, pathF);
		
		 }catch(IOException ex) {
			 LOG.error("ErrorCrearArchivo: "+ex);
		 }
		 
		}else {
			insertarTexto(comentario,pathF);
		}
		
	}
	
	private static boolean insertarTexto(Comentario comentario, Path direccion) {
		try {
			Files.write(direccion, (comentario.toString()+System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
			return true;
		}catch(IOException ex) {
			LOG.error("Algo paso: "+ex);
		}
		return false;
	}
	
	@Override
	public void comentarioPermanente(Comentario comentario) {
		var pathF=pathPermanente.resolve("archivoP.txt");
		if(!Files.exists(pathF)) {
			try{
				Files.createFile(pathF);
				insertarTexto(comentario, pathF);
			}catch(IOException ex) {
				LOG.error("ErrorCrearArchivo: "+ex);
			}
		}else {
			insertarTexto(comentario, pathF);
		}
		
	}
	@Override
	public List<Comentario> getTemporales() {
		crearDirectorios();
		try(Stream<String> archivo=Files.lines(pathTemporal.resolve("archivoT.txt"))){
			return archivo.map(t->Comentario.toObject(t))
					.collect(Collectors.toList());
		}catch(IOException ex) {
			LOG.error("errorArchivosTemporales: "+ex);
		}
		return null;
	}
	@Override
	public List<Comentario> getPermanentes() {
		try(Stream<String>archivo=Files.lines(pathPermanente.resolve("archivoP.txt"))){
			return archivo.map(t->Comentario.toObject(t))
					.collect(Collectors.toList());
		}catch(IOException ex) {
			LOG.error("errorArchivosPermanentes: "+ex);
		}
		return null;
	}
	
	
	
 
	

}
