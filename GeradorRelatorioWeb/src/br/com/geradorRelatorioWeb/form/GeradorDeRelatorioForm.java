package br.com.geradorRelatorioWeb.form;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.SelectItem;

import br.com.geradorRelatorioCore.enumeration.EOperador;
import br.com.geradorRelatorioCore.enumeration.EOperadorLogico;
import br.com.geradorRelatorioCore.interfaces.ICampoDTO;
import br.com.geradorRelatorioCore.interfaces.IEntidadeDTO;
import br.com.geradorRelatorioCore.interfaces.IRelatorioDTO;
import br.com.geradorRelatorioWeb.dto.CampoViewDTO;
import br.com.geradorRelatorioWeb.dto.EntidadeVIiewDTO;
import br.com.geradorRelatorioWeb.dto.ItemFormulaDTO;

public class GeradorDeRelatorioForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public enum EDisabled{
		RELACIONAMENTOS, BTT_CONFIRMAR_SELECAO, BTT_HABILITAR_SELECAO, INPUT_MASCARA, INPUT_LABEL, BTT_ADICIONAR, TXT_VALOR_MUTAVEL_CONSULTA_AVANCADO,
		TXT_CAMPO_CONSULTA_AVANCADO, BTT_ADICIONAR_VALOR, TXT_OPERADOR_CONSULTA_AVANCADO, BTT_OPERADOR_CONSULTA_AVANCADO, CAMPOS_AVANCADOS, BTT_ABRIR_PARENTESES, 
		BTT_FECHAS_PARENTESES, TXT_FUNCOES_CONSULTA_AVANCADO, BTT_FUNCOES_CONSULTA_AVANCADO, BTT_DESFAZER_CONSULTA_AVANCADO, BTT_REFAZER_CONSULTA_AVANCADO, 
		BTT_LIMPAR_CONSULTA_AVANCADO, CAMPOS_SIMPLES, BTT_ADICIONAR_CAMPO_CONDICOES, BTT_OPERADOR_CONSULTA_RETORNO, BTT_OPERADOR_LOGICO_CONSULTA_RETORNO, 
		TXT_VALOR_MUTAVEL_CONSULTA_RETORNO, BTT_VALOR_MUTAVEL_CONSULTA_RETORNO, TXT_CAMPO_CONSULTA_RETORNO, TXT_OPERADOR_CONSULTA_RETORNO, TXT_OPERADOR_LOGICO_CONSULTA_RETORNO, 
		BTT_GERAR_RELATORIO, BTT_BAIXAR_RELATORIO, EXTENSAO, NOME_ARQUIVO
	}
	
	public enum ERendered{
		PANEL_CAMPOS_CONSULTA, PANEL_CAMPOS_SIMPLES, PANEL_CAMPOS_AVANCADO, FORMULA_COM_ERRO, PANEL_CAMPOS, ADICIONAR_CAMPOS_AVANCADOS, PANEL_CONDICOES, PANEL_INFORMACOES_ARQUIVO, 
		PANEL_BOTOES
	}
	
	private List<SelectItem> entidadesOrigem;
	private List<SelectItem> entidadesRelacionadas;
	
	private IEntidadeDTO entidadeOrigem;
	
	private List<EntidadeVIiewDTO> entidadesView;
	
	private List<SelectItem> camposView;
	private List<SelectItem> camposNumericosView;
	
	private List<CampoViewDTO> camposConsulta;
	
	private List<SelectItem> mascarasRetorno;
	
	private boolean abrirParenteses;
	private boolean fecharParenteses;
	
	private List<SelectItem> operadoresConsultaAvancada;
	private List<SelectItem> funcoesConsultaAvancada;
	
	private boolean todosOsCampos;
	private boolean adicionarCamposAvancados;
	
	private List<SelectItem> operadoresLogicos;
	private List<SelectItem> operadores;
	
	private String msgErroformulaCondicaoRetorno;
	
	private List<ItemFormulaDTO> formulaCondicaoRetorno;
	private List<ItemFormulaDTO> formulaCondicaoRetornoDesfazer;
	
	private ICampoDTO campoCondicaoRetorno;
	private EOperadorLogico operadorLogicoCondicaoRetorno;
	private EOperador operadoresCondicaoRetorno;
	
	private String valorMutavelConsultaRetorno;
	
	private boolean agruparResultadoRetorno;
	
	private IRelatorioDTO arquivoDownlod;
	private String nomeArquivo;
	private List<SelectItem> geradores;
	private String fullPathClassGerador;
	
	public List<EntidadeVIiewDTO> getEntidadesView() {
		return entidadesView;
	}

	public void setEntidadesView(List<EntidadeVIiewDTO> entidadesView) {
		this.entidadesView = entidadesView;
	}

	public List<SelectItem> getCamposView() {
		return camposView;
	}

	public void setCamposView(List<SelectItem> camposView) {
		this.camposView = camposView;
	}

	public List<CampoViewDTO> getCamposConsulta() {
		return camposConsulta;
	}

	public void setCamposConsulta(List<CampoViewDTO> camposConsulta) {
		this.camposConsulta = camposConsulta;
	}

	public List<SelectItem> getEntidadesOrigem() {
		return entidadesOrigem;
	}

	public void setEntidadesOrigem(List<SelectItem> entidadeOrigem) {
		this.entidadesOrigem = entidadeOrigem;
	}

	public IEntidadeDTO getEntidadeOrigem() {
		return entidadeOrigem;
	}

	public void setEntidadeOrigem(IEntidadeDTO entidadeOrigem) {
		this.entidadeOrigem = entidadeOrigem;
	}

	public List<SelectItem> getEntidadesRelacionadas() {
		return entidadesRelacionadas;
	}

	public void setEntidadesRelacionadas(List<SelectItem> entidadesRelacionadas) {
		this.entidadesRelacionadas = entidadesRelacionadas;
	}

	public List<SelectItem> getMascarasRetorno() {
		return mascarasRetorno;
	}

	public void setMascarasRetorno(List<SelectItem> mascarasRetorno) {
		this.mascarasRetorno = mascarasRetorno;
	}

	public List<SelectItem> getOperadoresConsultaAvancada() {
		return operadoresConsultaAvancada;
	}

	public void setOperadoresConsultaAvancada(
			List<SelectItem> operadoresConsultaAvancada) {
		this.operadoresConsultaAvancada = operadoresConsultaAvancada;
	}

	public List<SelectItem> getCamposNumericosView() {
		return camposNumericosView;
	}

	public void setCamposNumericosView(List<SelectItem> camposNumericosView) {
		this.camposNumericosView = camposNumericosView;
	}

	public boolean isAbrirParenteses() {
		return abrirParenteses;
	}

	public void setAbrirParenteses(boolean abrirParenteses) {
		this.abrirParenteses = abrirParenteses;
	}

	public boolean isFecharParenteses() {
		return fecharParenteses;
	}

	public void setFecharParenteses(boolean fecharParenteses) {
		this.fecharParenteses = fecharParenteses;
	}

	public List<SelectItem> getFuncoesConsultaAvancada() {
		return funcoesConsultaAvancada;
	}

	public void setFuncoesConsultaAvancada(List<SelectItem> funcoesConsultaAvancada) {
		this.funcoesConsultaAvancada = funcoesConsultaAvancada;
	}

	public boolean isTodosOsCampos() {
		return todosOsCampos;
	}

	public void setTodosOsCampos(boolean todosOsCampos) {
		this.todosOsCampos = todosOsCampos;
	}

	public boolean isAdicionarCamposAvancados() {
		return adicionarCamposAvancados;
	}

	public void setAdicionarCamposAvancados(boolean adicionarCamposAvancados) {
		this.adicionarCamposAvancados = adicionarCamposAvancados;
	}

	public List<SelectItem> getOperadoresLogicos() {
		return operadoresLogicos;
	}

	public void setOperadoresLogicos(List<SelectItem> operadoresLogicos) {
		this.operadoresLogicos = operadoresLogicos;
	}

	public ICampoDTO getCampoCondicaoRetorno() {
		return campoCondicaoRetorno;
	}

	public void setCampoCondicaoRetorno(ICampoDTO campoCondicaoRetorno) {
		this.campoCondicaoRetorno = campoCondicaoRetorno;
	}

	public List<ItemFormulaDTO> getFormulaCondicaoRetorno() {
		return formulaCondicaoRetorno;
	}

	public void setFormulaCondicaoRetorno(
			List<ItemFormulaDTO> formulaCondicaoRetorno) {
		this.formulaCondicaoRetorno = formulaCondicaoRetorno;
	}

	public EOperadorLogico getOperadorLogicoCondicaoRetorno() {
		return operadorLogicoCondicaoRetorno;
	}

	public void setOperadorLogicoCondicaoRetorno(
			EOperadorLogico operadorLogicoCondicaoRetorno) {
		this.operadorLogicoCondicaoRetorno = operadorLogicoCondicaoRetorno;
	}

	public List<SelectItem> getOperadores() {
		return operadores;
	}

	public void setOperadores(List<SelectItem> operadores) {
		this.operadores = operadores;
	}

	public EOperador getOperadoresCondicaoRetorno() {
		return operadoresCondicaoRetorno;
	}

	public void setOperadoresCondicaoRetorno(EOperador operadoresCondicaoRetorno) {
		this.operadoresCondicaoRetorno = operadoresCondicaoRetorno;
	}

	public String getValorMutavelConsultaRetorno() {
		return valorMutavelConsultaRetorno;
	}

	public void setValorMutavelConsultaRetorno(String valorMutavelConsultaRetorno) {
		this.valorMutavelConsultaRetorno = valorMutavelConsultaRetorno;
	}

	public List<ItemFormulaDTO> getFormulaCondicaoRetornoDesfazer() {
		return formulaCondicaoRetornoDesfazer;
	}

	public void setFormulaCondicaoRetornoDesfazer(
			List<ItemFormulaDTO> formulaCondicaoRetornoDesfazer) {
		this.formulaCondicaoRetornoDesfazer = formulaCondicaoRetornoDesfazer;
	}

	public boolean isAgruparResultadoRetorno() {
		return agruparResultadoRetorno;
	}

	public void setAgruparResultadoRetorno(boolean agruparResultadoRetorno) {
		this.agruparResultadoRetorno = agruparResultadoRetorno;
	}

	public String getMsgErroformulaCondicaoRetorno() {
		return msgErroformulaCondicaoRetorno;
	}

	public void setMsgErroformulaCondicaoRetorno(
			String msgErroformulaCondicaoRetorno) {
		this.msgErroformulaCondicaoRetorno = msgErroformulaCondicaoRetorno;
	}

	public IRelatorioDTO getArquivoDownlod() {
		return arquivoDownlod;
	}

	public void setArquivoDownlod(IRelatorioDTO arquivoDownlod) {
		this.arquivoDownlod = arquivoDownlod;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public List<SelectItem> getGeradores() {
		return geradores;
	}

	public void setGeradores(List<SelectItem> geradores) {
		this.geradores = geradores;
	}

	public String getFullPathClassGerador() {
		return fullPathClassGerador;
	}

	public void setFullPathClassGerador(String fullPathClassGerador) {
		this.fullPathClassGerador = fullPathClassGerador;
	}

	

}
