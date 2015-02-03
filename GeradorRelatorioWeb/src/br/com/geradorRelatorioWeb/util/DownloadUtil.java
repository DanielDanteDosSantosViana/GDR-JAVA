package br.com.geradorRelatorioWeb.util;

import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

public class DownloadUtil {
	
	public static void download(byte[] arquivo, String nomeArquivo, String extensao) throws IOException{
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();  
        HttpServletResponse response =  (HttpServletResponse)context.getResponse();
        
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + nomeArquivo + "."  + extensao);
        
        ServletOutputStream outStream = null;  
        try {  
            outStream = response.getOutputStream();  
            outStream.write(arquivo);             
        } finally {  
            outStream.flush();  
            outStream.close();  
        }  
        
        FacesContext.getCurrentInstance().responseComplete();
	}
}
