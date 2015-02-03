package br.com.geradorRelatorio.builders;

import java.io.Reader;
import java.io.StringReader;

import br.com.geradorRelatorio.enumerations.ETag;

public class HtmlBuilder {
	
	private final StringBuffer html;

	public HtmlBuilder() {
		this.html = new StringBuffer();
	}
	

	public Reader construir() {
		return new StringReader(html.toString());
	}


	public HtmlBuilder criar() {
		appendTag(ETag.HTML);
		return this;
	}


	private void appendTag(ETag tag) {
		this.html.append("<");
		this.html.append(tag.getTag());
		this.html.append(">");
	}


	public HtmlBuilder criarCorpo() {
		appendTag(ETag.BODY);
		return this;
	}


	public HtmlBuilder criarTabela() {
		appendTag(ETag.TABLE);
		return this;
	}


	public HtmlBuilder criarLinhaTabela() {
		this.html.append("<");
		this.html.append(ETag.TR);
		this.html.append(" border=\"1\"");
		this.html.append(">");
		return this;
	}


	public HtmlBuilder criarColunaTabela() {
		appendTag(ETag.TD);
		return this;
	}


	public HtmlBuilder addValor(String valor) {
		if(valor != null){
			this.html.append(valor.trim());
		}
		
		return this;
	}


	public HtmlBuilder fecharColunaTabela() {
		appendFecharTag(ETag.TD);
		return this;
	}


	private void appendFecharTag(ETag tag) {
		this.html.append("</");
		this.html.append(tag);
		this.html.append(">");
		
	}


	public HtmlBuilder fecharLinhaTabela() {
		appendFecharTag(ETag.TR);
		return this;
	}


	public HtmlBuilder fecharTabela() {
		appendFecharTag(ETag.TABLE);
		return this;
	}


	public HtmlBuilder fecharCorpo() {
		appendFecharTag(ETag.BODY);
		return this;
	}


	public HtmlBuilder fechar() {
		appendFecharTag(ETag.HTML);
		return this;
	}
	
	
}
