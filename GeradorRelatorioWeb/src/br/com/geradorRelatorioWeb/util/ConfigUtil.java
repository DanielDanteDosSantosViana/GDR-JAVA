package br.com.geradorRelatorioWeb.util;

import java.io.File;
import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import br.com.geradorRelatorioCore.exception.InitException;
import br.com.geradorRelatorioWeb.dto.ConfigDTO;

public class ConfigUtil {
	
	private static final String NODE_RAIZ = "geradorRelatorio";
	private static final String NODE_ENTIDADES_PROPERTIES = "entidadesProperties";
	private static final String NODE_CALL_BACK = "callBack";
	
	public static ConfigDTO parse(String urlConfigu) throws InitException{
		try {
			ConfigDTO ret = new ConfigDTO();
			File configFile = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath(urlConfigu));
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = factory.newDocumentBuilder();
			
			Document doc = dBuilder.parse(configFile);
			
			NodeList nRaiz = doc.getElementsByTagName(NODE_RAIZ);
			
			ret.setEntidadesProperties(getTextNodeRaiz(nRaiz, NODE_ENTIDADES_PROPERTIES));
			ret.setCallBack(getTextNodeRaiz(nRaiz, NODE_CALL_BACK));
			
			return ret;
		} catch (ParserConfigurationException e) {
			throw new InitException(e);
		} catch (SAXException e) {
			throw new InitException(e);
		} catch (IOException e) {
			throw new InitException(e);
		}
	}

	private static String getTextNodeRaiz(NodeList nRaiz, String node) {
		return ((Element)nRaiz.item(0)).getElementsByTagName(node).item(0).getTextContent();
	}
}
