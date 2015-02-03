package br.com.geradorRelatorioWeb.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import br.com.geradorRelatorioCore.enumeration.EContextParam;
import br.com.geradorRelatorioCore.enumeration.EFuncoes;
import br.com.geradorRelatorioCore.enumeration.EOperador;
import br.com.geradorRelatorioCore.enumeration.EOperadorAritmetico;
import br.com.geradorRelatorioCore.enumeration.EOperadorLogico;
import br.com.geradorRelatorioCore.enumeration.EParenteses;
import br.com.geradorRelatorioCore.exception.EntidadeConsumerException;
import br.com.geradorRelatorioCore.exception.GeracaoException;
import br.com.geradorRelatorioCore.exception.InitException;
import br.com.geradorRelatorioCore.interfaces.ICampoDTO;
import br.com.geradorRelatorioCore.interfaces.ICampoViewDTO;
import br.com.geradorRelatorioCore.interfaces.IEntidadeDTO;
import br.com.geradorRelatorioCore.interfaces.IEntidadeVIiewDTO;
import br.com.geradorRelatorioCore.interfaces.IGeradorDeRelatoriCallBack;
import br.com.geradorRelatorioCore.interfaces.IItemFormulaDTO;
import br.com.geradorRelatorioCore.interfaces.ISelectItemWrapper;
import br.com.geradorRelatorioCore.service.IGeradorDeRelatorioServiceLocal;
import br.com.geradorRelatorioWeb.dto.CampoViewDTO;
import br.com.geradorRelatorioWeb.dto.ConfigDTO;
import br.com.geradorRelatorioWeb.dto.EntidadeVIiewDTO;
import br.com.geradorRelatorioWeb.dto.ItemFormulaDTO;
import br.com.geradorRelatorioWeb.form.GeradorDeRelatorioForm;
import br.com.geradorRelatorioWeb.form.GeradorDeRelatorioForm.EDisabled;
import br.com.geradorRelatorioWeb.form.GeradorDeRelatorioForm.ERendered;
import br.com.geradorRelatorioWeb.util.ConfigUtil;
import br.com.geradorRelatorioWeb.util.DownloadUtil;
import br.com.geradorRelatorioWeb.util.Util;
import br.com.geradorRelatorioWeb.wrapper.SelectItemWrapper;

@ViewScoped
@ManagedBean(name = "geradorDeRelatorio")
public class GeradorDeRelatorioController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 917890402913026388L;
	
	@EJB
	private IGeradorDeRelatorioServiceLocal gerador;
	
	private final GeradorDeRelatorioForm form;
	
	private IGeradorDeRelatoriCallBack callBack;
	private ConfigDTO config;
	
	public GeradorDeRelatorioController() {
		this.form = new GeradorDeRelatorioForm();
	}

	@PostConstruct
	public void load(){

		try {
			loadConfig();
			loadCallBack();
			loadEntidades();
			loadMascaras();
			loadOperadoresConsultaAvancada();
			loadFuncoesConsultaAvancada();
			loadOperadosLogicos();
			loadOperadores();
			loadGeradores();
		} catch (InitException e) {
			e.printStackTrace();
		} catch (EntidadeConsumerException e) {
			e.printStackTrace();
		}
			
	}

	private void loadGeradores() {
		getForm().setGeradores(new ArrayList<SelectItem>());
		
		getForm().getGeradores().add(new SelectItem(null, ""));
		getForm().getGeradores().add(new SelectItem("br.com.geradorRelatorio.geradores.GeradorPDF", "PDF"));
		getForm().getGeradores().add(new SelectItem("br.com.geradorRelatorio.geradores.GeradorXls", "XLS"));
		
	}

	private void loadOperadosLogicos() {
		getForm().setOperadoresLogicos(new ArrayList<SelectItem>());
		
		getForm().getOperadoresLogicos().add(new SelectItem(null, ""));
		
		for (EOperadorLogico funcao : EOperadorLogico.values()) {
			getForm().getOperadoresLogicos().add(new SelectItem(funcao, funcao.getLabel()));
		}
		
	}
	
	private void loadOperadores() {
		getForm().setOperadores(new ArrayList<SelectItem>());
		
		getForm().getOperadores().add(new SelectItem(null, ""));
		
		for (EOperador funcao : EOperador.values()) {
			getForm().getOperadores().add(new SelectItem(funcao, funcao.getLabel()));
		}
		
	}

	private void loadFuncoesConsultaAvancada() {
		getForm().setFuncoesConsultaAvancada(new ArrayList<SelectItem>());
		
		getForm().getFuncoesConsultaAvancada().add(new SelectItem(null, ""));
		
		for (EFuncoes funcao : EFuncoes.values()) {
			getForm().getFuncoesConsultaAvancada().add(new SelectItem(funcao, funcao.getLabel()));
		}
		
	}

	private void loadOperadoresConsultaAvancada() {
		getForm().setOperadoresConsultaAvancada(new ArrayList<SelectItem>());
		
		getForm().getOperadoresConsultaAvancada().add(new SelectItem(null, ""));
		
		for (EOperadorAritmetico operador : EOperadorAritmetico.values()) {
			getForm().getOperadoresConsultaAvancada().add(new SelectItem(operador, operador.getSimbolo()));
		}
	}

	private void loadMascaras() {
		getForm().setMascarasRetorno(Util.MASCARAS);
	}

	private void loadConfig() throws InitException {
		String callBackFullPath = Util.recuperaContextParam(EContextParam.PATH_CONFIG);
		this.config = ConfigUtil.parse(callBackFullPath);
	}

	private void loadCallBack() throws InitException {
		String callBackFullPath = this.config.getCallBack();
		
		if(callBackFullPath != null){
			try {
				callBack = (IGeradorDeRelatoriCallBack) Class.forName(callBackFullPath).newInstance();
			} catch (Exception e) {
				throw new InitException(e);
			}
		}
		
	}

	private void loadEntidades() throws EntidadeConsumerException {
		getForm().setEntidadesOrigem(new ArrayList<SelectItem>());
		
		getForm().getEntidadesOrigem().add(new SelectItem(null, ""));
		for (IEntidadeDTO entidade : gerador.loadEntidades(this.config.getEntidadesProperties()).values()) {
			getForm().getEntidadesOrigem().add(new SelectItem(entidade, entidade.getLabel()));
		}
		
	}

	public GeradorDeRelatorioForm getForm() {
		return form;
	}
	
	
	public void adicionarRelacionamento(){
		IEntidadeDTO entidade = getForm().getEntidadeOrigem();
		getForm().setEntidadesView(new ArrayList<EntidadeVIiewDTO>());
		
		if(entidade != null && entidade.hasRelacionamento()){
			getForm().getEntidadesView().add(new EntidadeVIiewDTO());
			getForm().getEntidadesView().get(0).setEntidades(new ArrayList<ISelectItemWrapper>());
			getForm().getEntidadesView().get(0).getEntidades().add(new SelectItemWrapper(null, ""));
			getForm().getEntidadesView().get(0).getEntidades().addAll(Util.converteRelacionamentos(entidade));
			
		}
	}
	
	public void adicionarRelacionamento(Integer index){
		EntidadeVIiewDTO entidadeAtual = getForm().getEntidadesView().get(index);
		
		List<EntidadeVIiewDTO> listaRemover = getForm().getEntidadesView().subList(index + 1, getForm().getEntidadesView().size());
		getForm().getEntidadesView().removeAll(listaRemover);
		
		if(entidadeAtual.getEntidadeSelecionada() != null && (entidadeAtual.getEntidadeSelecionada().hasRelacionamento() || 
				getForm().getEntidadesView().get(getForm().getEntidadesView().size()-1).getEntidades().size() >= getForm().getEntidadesView().size())){
			
			getForm().getEntidadesView().add(new EntidadeVIiewDTO());
			tratarRelacionamentos();
		}
		
		
	}
	
	private void tratarRelacionamentos() {
		
		List<ISelectItemWrapper> relacionamentos = new ArrayList<ISelectItemWrapper>(Util.converteRelacionamentos(getForm().getEntidadeOrigem()));
		List<IEntidadeDTO> relacionamentosUsados = new ArrayList<IEntidadeDTO>();
		
		for (EntidadeVIiewDTO relacionamento : getForm().getEntidadesView()) {
			if(relacionamento.getEntidadeSelecionada() != null){
				relacionamentos.addAll(Util.converteRelacionamentos(relacionamento.getEntidadeSelecionada()));
				relacionamentosUsados.add(relacionamento.getEntidadeSelecionada());
			}
		}
		
		relacionamentos.removeAll(relacionamentosUsados);
		
		if(!relacionamentos.isEmpty()){
			relacionamentos.add(0,new SelectItemWrapper(null, ""));
			getForm().getEntidadesView().get(getForm().getEntidadesView().size()-1).setEntidades(relacionamentos);
		}
		else{
			getForm().getEntidadesView().remove(getForm().getEntidadesView().size()-1);
		}
	}

	public void confirmarSelecao(){
		getForm().setCamposView(new ArrayList<SelectItem>());
		getForm().setCamposNumericosView(new ArrayList<SelectItem>());
		getForm().setCamposConsulta(new ArrayList<CampoViewDTO>());
		getForm().setFormulaCondicaoRetorno(new ArrayList<ItemFormulaDTO>());
		getForm().setFormulaCondicaoRetornoDesfazer(new ArrayList<ItemFormulaDTO>());
		
		getForm().getCamposView().add(new SelectItem(null, ""));
		getForm().getCamposView().addAll(Util.converteCampos(getForm().getEntidadeOrigem()));
		
		getForm().getCamposNumericosView().add(new SelectItem(null, ""));
		getForm().getCamposNumericosView().addAll(Util.converteCamposNumericos(getForm().getEntidadeOrigem()));
		
		for (EntidadeVIiewDTO entidade : getForm().getEntidadesView()) {
			if(entidade.getEntidadeSelecionada() != null){
				getForm().getCamposView().addAll(Util.converteCampos(entidade.getEntidadeSelecionada()));
				getForm().getCamposNumericosView().addAll(Util.converteCamposNumericos(entidade.getEntidadeSelecionada()));
			}
		}
		
		getForm().getCamposConsulta().add(new CampoViewDTO());
	}
	
	public void habilitarSelecao(){
		getForm().setCamposView(new ArrayList<SelectItem>());
		getForm().setTodosOsCampos(false);
		getForm().setAdicionarCamposAvancados(false);
		getForm().setArquivoDownlod(null);
		
		this.limparCamposConsultas(null);
	}
	
	public boolean disabled(EDisabled param){
		switch (param) {
		case RELACIONAMENTOS:
			return desabilitarSelecao();
		case BTT_CONFIRMAR_SELECAO:
			return !isConfirmarSelecao();
		case BTT_HABILITAR_SELECAO:
			return !desabilitarSelecao();
		case CAMPOS_AVANCADOS:
			return !hasCamposNumericos();
		case CAMPOS_SIMPLES:
			return getForm().isTodosOsCampos();
		case BTT_ADICIONAR_CAMPO_CONDICOES:
			return !hasCampoCondicao();
		case BTT_OPERADOR_CONSULTA_RETORNO:
			return !hasOperadorConsultaRetorno();
		case BTT_OPERADOR_LOGICO_CONSULTA_RETORNO:
			return !hasOperadorLogicoConsultaRetorno();
		case TXT_CAMPO_CONSULTA_RETORNO:
			return disabledCampoConsultaRetorno(getForm().getFormulaCondicaoRetorno()) || !disabledOperadoresConsulta(getForm().getFormulaCondicaoRetorno(), true);
		case BTT_VALOR_MUTAVEL_CONSULTA_RETORNO:
			return !hasValorConsultaRetorno();
		case TXT_OPERADOR_CONSULTA_RETORNO:
			return disabledOperadoresConsulta(getForm().getFormulaCondicaoRetorno(), false);
		case BTT_ABRIR_PARENTESES:
			return disabledAbrirParenteses(getForm().getFormulaCondicaoRetorno());
		case BTT_FECHAS_PARENTESES:
			return disabledFecharParenteses(getForm().getFormulaCondicaoRetorno(), true);
		case TXT_VALOR_MUTAVEL_CONSULTA_RETORNO:
			return disabledValorMutavel(getForm().getFormulaCondicaoRetorno());
		case TXT_OPERADOR_LOGICO_CONSULTA_RETORNO:
			return disabledOperadorLogico(getForm().getFormulaCondicaoRetorno());
		case BTT_DESFAZER_CONSULTA_AVANCADO:
		case BTT_LIMPAR_CONSULTA_AVANCADO:
			return !hasItemFormula(getForm().getFormulaCondicaoRetorno());
		case BTT_REFAZER_CONSULTA_AVANCADO:
			return !hasItemDesfeitoFormula(getForm().getFormulaCondicaoRetornoDesfazer());
		case EXTENSAO:
		case NOME_ARQUIVO:
			return disabledNomeArquivo();
		case BTT_GERAR_RELATORIO:
			return disabledNomeArquivo() || Util.isEmpty(getForm().getFullPathClassGerador()) || Util.isEmpty(getForm().getNomeArquivo());
		case BTT_BAIXAR_RELATORIO:
			return getForm().getArquivoDownlod() == null;
		default:
			return false;
		}
	}

	private boolean disabledNomeArquivo() {
		return ((!getForm().isTodosOsCampos()  || getForm().isAdicionarCamposAvancados() ) && hasCamposRetornoComErro())  || renderedFormaulaComErro();
	}


	private boolean hasCamposRetornoComErro() {
		if(getForm().getCamposConsulta() != null){
			for (CampoViewDTO campo : getForm().getCamposConsulta()) {
				if((!campo.isAvancado() && campo.getCamposSelecionado() == null) || Util.isEmpty(campo.getApelido()) 
						|| (campo.isAvancado() && (campo.getFormula().isEmpty() || renderedFormulaComErro(campo)))){
					return true;
				}
			}
			
			return false;
		}
		
		return true;
	}

	private boolean disabledOperadorLogico(List<ItemFormulaDTO> formula) {
		if(formula != null && !formula.isEmpty()){
			ItemFormulaDTO ultimoItem = utlimoItemDaFormula(formula);
			
			if(ultimoItem.getValor() instanceof String || EParenteses.FECHAR.equals(ultimoItem.getValor())){
				return false;
			}
		}
		
		return true;
	}

	private boolean disabledCampoConsultaRetorno(List<ItemFormulaDTO> formula) {
		if(formula != null && !formula.isEmpty()){
			ItemFormulaDTO ultimoItem = utlimoItemDaFormula(formula);
			
			if(ultimoItem.getValor() instanceof String || ultimoItem.getValor() instanceof EOperador){
				return true;
			}
		}
		
		return false;
	}

	private boolean disabledValorMutavel(List<ItemFormulaDTO> formula) {
		
		if(formula != null && !formula.isEmpty()){
			ItemFormulaDTO ultimoItem = utlimoItemDaFormula(formula);
			
			if(ultimoItem.getValor() instanceof EOperador){
				return false;
			}
		}
		
		return true;
	}

	private boolean hasValorConsultaRetorno() {
		return getForm().getValorMutavelConsultaRetorno() != null;
	}

	private boolean hasOperadorLogicoConsultaRetorno() {
		return getForm().getOperadorLogicoCondicaoRetorno() != null;
	}

	private boolean hasOperadorConsultaRetorno() {
		return getForm().getOperadoresCondicaoRetorno() != null;
	}

	private boolean hasCampoCondicao() {
		return getForm().getCampoCondicaoRetorno() != null;
	}

	private boolean hasCamposNumericos() {
		return getForm().getCamposNumericosView() != null && getForm().getCamposNumericosView().size() > 1;
	}

	private boolean hasCampoConsultaAvancada(CampoViewDTO campo) {
		return campo != null && campo.getCampoConsultaAvancada() != null;
	}

	private boolean hasValorMutavelConsultaAvancada(CampoViewDTO campo) {
		return campo != null && campo.getValorMutavelConsultaAvancado() != null;
	}
	
	public boolean rendered(ERendered param){
		switch (param) {
		case PANEL_CAMPOS_CONSULTA:
		case PANEL_CONDICOES:
		case PANEL_INFORMACOES_ARQUIVO:
		case PANEL_BOTOES:
			return desabilitarSelecao();
		case PANEL_CAMPOS:
			return showPanelCampos();
		case ADICIONAR_CAMPOS_AVANCADOS:
			return getForm().isTodosOsCampos() && hasCamposNumericos();
		case FORMULA_COM_ERRO:
			return renderedFormaulaComErro();
		default:
			return true;
		}
	}
	
	private boolean renderedFormaulaComErro() {
		getForm().setMsgErroformulaCondicaoRetorno(null);
		
		if(getForm().getFormulaCondicaoRetorno() != null && !getForm().getFormulaCondicaoRetorno().isEmpty()){
			ItemFormulaDTO ultimoItem = utlimoItemDaFormula(getForm().getFormulaCondicaoRetorno());
			
			if(!qtdParentesIguasi(getForm().getFormulaCondicaoRetorno())){
				getForm().setMsgErroformulaCondicaoRetorno("Quantidade de parenteses errada.");
				return true;
			}
			else if (ultimoItem.getValor() instanceof ICampoDTO){
				getForm().setMsgErroformulaCondicaoRetorno("Ultimo item não pode ser um campo.");
				return true;
			}
			else if(ultimoItem.getValor() instanceof EOperadorLogico){
				getForm().setMsgErroformulaCondicaoRetorno("Ultimo item não pode ser um operador logico.");
				return true;
			}
			else if(ultimoItem.getValor() instanceof EOperador){
				getForm().setMsgErroformulaCondicaoRetorno("Ultimo item não pode ser um operador.");
				return true;
			}
		}
		
		return false;
	}

	private boolean showPanelCampos() {
		boolean ret = true;
		
		if(getForm().isTodosOsCampos()){
			ret = hasCamposNumericos() && getForm().isAdicionarCamposAvancados();
		}
		
		return ret;
	}

	public boolean rendered(ERendered param, CampoViewDTO campo){
		switch (param) {
		case PANEL_CAMPOS_SIMPLES:
			return !campo.isAvancado();
		case PANEL_CAMPOS_AVANCADO:
			return campo.isAvancado();
		case FORMULA_COM_ERRO:
			return renderedFormulaComErro(campo);
		default:
			return true;
		}
	}

	private boolean renderedFormulaComErro(CampoViewDTO campo) {
		campo.setMsgErroFormulaConsultaAvancado(null);
		List<ItemFormulaDTO> formula = campo.getFormula();
		if(!formula.isEmpty()){
			ItemFormulaDTO ultimoItem = utlimoItemDaFormula(formula);
			
			if(!qtdParentesIguasi(campo.getFormula())){
				campo.setMsgErroFormulaConsultaAvancado("Quantidade de parenteses errada.");
				return true;
			}
			else if(!disabledAbrirParenteses(campo)){
				campo.setMsgErroFormulaConsultaAvancado("Ultimo item não pode ser um opedaror.");
				return true;
			}
			else if(ultimoItem.getValor() instanceof EFuncoes){
				campo.setMsgErroFormulaConsultaAvancado("Ultimo item não pode ser uma função.");
				return true;
			}
		}
		return false;
	}
	
	public boolean disabled(EDisabled param, CampoViewDTO campo){
		switch (param) {
		case INPUT_MASCARA:
		case INPUT_LABEL:
		case BTT_ADICIONAR:
			return campo.getCamposSelecionado() == null && !hasItemFormula(campo.getFormula());
		case TXT_VALOR_MUTAVEL_CONSULTA_AVANCADO:
			return disabledValorMutavel(campo) ;
		case TXT_CAMPO_CONSULTA_AVANCADO:
			return hasValorMutavelConsultaAvancada(campo) || !disabledOperadoresConsultaAvancada(campo) || (hasCampoFormulaConsultaAvancada(campo) && hasFormulaFuncao(campo)); 
		case BTT_ADICIONAR_VALOR:
			return (campo.getCampoConsultaAvancada() == null && campo.getValorMutavelConsultaAvancado() == null) || !disabledOperadoresConsultaAvancada(campo);
		case TXT_OPERADOR_CONSULTA_AVANCADO:
			return disabledOperadoresConsultaAvancada(campo);
		case BTT_OPERADOR_CONSULTA_AVANCADO:
			return !hasOperador(campo);
		case BTT_ABRIR_PARENTESES:
			return disabledAbrirParenteses(campo);
		case BTT_FECHAS_PARENTESES:
			return disabledFecharParenteses(campo);
		case TXT_FUNCOES_CONSULTA_AVANCADO:
			return disabledFuncoes(campo) || hasCampoFormulaConsultaAvancada(campo);
		case BTT_FUNCOES_CONSULTA_AVANCADO:
			return !hasFuncao(campo);
		case BTT_DESFAZER_CONSULTA_AVANCADO:
		case BTT_LIMPAR_CONSULTA_AVANCADO:
			return !hasItemFormula(campo.getFormula());
		case BTT_REFAZER_CONSULTA_AVANCADO:
			return !hasItemDesfeitoFormula(campo.getFormulaDesfazer());
		default:
			return true;
		}
	}

	private boolean hasFormulaFuncao(CampoViewDTO campo) {
		return hasItemConsultaAvancada(campo, EFuncoes.class);
	}

	private boolean hasCampoFormulaConsultaAvancada(CampoViewDTO campo) {
		return hasItemConsultaAvancada(campo, ICampoDTO.class);
	}
	
	private boolean hasItemConsultaAvancada(CampoViewDTO campo, Class<?> clazz) {
		List<ItemFormulaDTO> formula = campo.getFormula();
		
		if(!formula.isEmpty()){
			
			for (ItemFormulaDTO item : formula) {
				if(clazz.isInstance(item.getValor())){
					return true;
				}
			}
			
		}
		
		return false;
	}

	private boolean hasItemFormula(List<ItemFormulaDTO> formula) {
		return formula != null && !formula.isEmpty();
	}
	
	private boolean hasItemDesfeitoFormula(List<ItemFormulaDTO> formulaDesfazer) {
		return formulaDesfazer != null && !formulaDesfazer.isEmpty();
	}

	private boolean disabledValorMutavel(CampoViewDTO campo) {
		List<ItemFormulaDTO> formula = campo.getFormula();
		
		if(!formula.isEmpty()){
			ItemFormulaDTO ultimoItem = utlimoItemDaFormula(formula);
			
			if(ultimoItem.getValor() instanceof EFuncoes){
				return true;
			}
			
		}
		
		return hasCampoConsultaAvancada(campo) || !disabledOperadoresConsultaAvancada(campo);
	}	
	
	private boolean disabledFuncoes(CampoViewDTO campo) {
		List<ItemFormulaDTO> formula = campo.getFormula();
		
		if(!formula.isEmpty()){
			ItemFormulaDTO ultimoItem = utlimoItemDaFormula(formula);
			
			if(ultimoItem.getValor() instanceof EOperadorAritmetico || EParenteses.ABRIR.equals(ultimoItem.getValor())){
				return false;
			}
			else{
				return true;
			}
		}
		
		
		return false;
	}
	
	private boolean disabledFecharParenteses(CampoViewDTO campo) {
		return disabledFecharParenteses(campo.getFormula(), false);
	}
	
	private boolean disabledFecharParenteses(List<ItemFormulaDTO> formula, boolean desabilitaComCampoDTO) {
		
		if(formula != null && !formula.isEmpty()){
			ItemFormulaDTO ultimoItem = utlimoItemDaFormula(formula);
			
			if(!qtdParentesIguasi(formula) && (ultimoItem.getValor() instanceof Double || (!desabilitaComCampoDTO && ultimoItem.getValor() instanceof ICampoDTO) || 
					EParenteses.FECHAR.equals(ultimoItem.getValor()) || ultimoItem.getValor() instanceof String)){
				return false;
			}
		}
		
		
		return true;
	}

	private boolean qtdParentesIguasi(List<ItemFormulaDTO> formula) {
		int totalAbrir = 0;
		int totalFechar = 0;
		
		for (ItemFormulaDTO item : formula) {
			if(EParenteses.ABRIR.equals(item.getValor())){
				totalAbrir++;
			}
			else if(EParenteses.FECHAR.equals(item.getValor())){
				totalFechar++;
			}
		}
		
		return totalAbrir == totalFechar;
	}

	private boolean disabledAbrirParenteses(CampoViewDTO campo) {
		return disabledAbrirParenteses(campo.getFormula());
	}
	
	private boolean disabledAbrirParenteses(List<ItemFormulaDTO> formula) {
		
		if(formula != null && !formula.isEmpty()){
			ItemFormulaDTO ultimoItem = utlimoItemDaFormula(formula);
			if(ultimoItem.getValor() instanceof EOperadorAritmetico || ultimoItem.getValor() instanceof EOperadorLogico){
				return false;
			}
			else{
				return true;
			}
		}
		
		
		return false;
	}

	private boolean desabilitarSelecao() {
		return getForm().getCamposView() != null && !getForm().getCamposView().isEmpty();
	}

	private boolean isConfirmarSelecao() {
		if(getForm().getEntidadesView() != null){
			for (EntidadeVIiewDTO entidade : getForm().getEntidadesView()) {
				if(entidade.getEntidadeSelecionada() != null){
					return true && !desabilitarSelecao();
				}
			}
		}
		
		
		return getForm().getEntidadeOrigem() != null && !desabilitarSelecao();
	}
	
	public void selecionarCampo(Integer index){
		ICampoDTO campo = getForm().getCamposConsulta().get(index).getCamposSelecionado();
		if(campo != null){
			getForm().getCamposConsulta().get(index).setMascara(campo.getMascara());
		}
	}
	
	public void addCampoConsulta(){
		getForm().getCamposConsulta().add(new CampoViewDTO(getForm().isTodosOsCampos()));
	}
	
	public void removerCampoConsulta(int index){
		getForm().getCamposConsulta().remove(index);
	}
	
	public void selecionarTipoCampo(int index){
		getForm().getCamposConsulta().set(index, new CampoViewDTO(getForm().getCamposConsulta().get(index).isAvancado()));
		limparCamposConsultas(null);
	}

	private void limparCamposConsultas(CampoViewDTO campo) {
		
		getForm().setAbrirParenteses(false);
		getForm().setFecharParenteses(false);
		getForm().setCampoCondicaoRetorno(null);
		getForm().setOperadoresCondicaoRetorno(null);
		getForm().setOperadorLogicoCondicaoRetorno(null);
		getForm().setOperadoresCondicaoRetorno(null);
		getForm().setValorMutavelConsultaRetorno(null);
		getForm().setOperadorLogicoCondicaoRetorno(null);
		
		if(campo != null){
			campo.setValorMutavelConsultaAvancado(null);
			campo.setCampoConsultaAvancada(null);
			campo.setOperadorCaonsultaAvancado(null);
			campo.setFuncaoConsultaAvancado(null);
		}
	}
	
	public void adicionarItemFormulaConsultaAvencada(CampoViewDTO campo){
		this.adicionarItemFormula(campo, campo.getFormula());
		
		campo.setFormulaDesfazer(new ArrayList<ItemFormulaDTO>());
		this.limparCamposConsultas(campo);
	}
	
	private boolean hasFuncao(CampoViewDTO campo) {
		return campo != null &&  campo.getFuncaoConsultaAvancado() != null;
	}

	private boolean hasOperador(CampoViewDTO campo) {
		return campo != null && campo.getOperadorCaonsultaAvancado() != null;
	}
	
	private boolean disabledOperadoresConsultaAvancada(CampoViewDTO campo){
		return disabledOperadoresConsulta(campo.getFormula(), true);
	}
	
	private boolean disabledOperadoresConsulta(List<ItemFormulaDTO> formula, boolean utilizaFechar){

		if(formula != null && !formula.isEmpty()){
			ItemFormulaDTO ultimoItem = utlimoItemDaFormula(formula);
			if(ultimoItem.getValor() instanceof Double || ultimoItem.getValor() instanceof ICampoDTO || 
					(utilizaFechar && EParenteses.FECHAR.equals(ultimoItem.getValor()))){
				return false;
			}
		}
		
		return true;
	}

	private ItemFormulaDTO utlimoItemDaFormula(List<ItemFormulaDTO> formula) {
		return formula.get(formula.size()-1);
	}
	
	public void desfazerConsultaAvancada(CampoViewDTO campo){
		this.desfazer(campo.getFormula(), campo.getFormulaDesfazer());
	}
	
	public void desfazerCondicaoRetorno(){
		this.desfazer(getForm().getFormulaCondicaoRetorno(), getForm().getFormulaCondicaoRetornoDesfazer());
	}
	
	private void desfazer(List<ItemFormulaDTO> formula, List<ItemFormulaDTO> formulaDesfazer){
		ItemFormulaDTO ultimoItem = utlimoItemDaFormula(formula);
		formula.remove(ultimoItem);
		formulaDesfazer.add(ultimoItem);
	}
	
	public void refazerCondicaoRetorno(){
		this.refazer(getForm().getFormulaCondicaoRetorno(), getForm().getFormulaCondicaoRetornoDesfazer());
	}
	
	public void refazerConsultaAvancada(CampoViewDTO campo){
		this.refazer(campo.getFormula(), campo.getFormulaDesfazer());
	}
	
	private void refazer(List<ItemFormulaDTO> formula, List<ItemFormulaDTO> formulaDesfazer){
		List<ItemFormulaDTO> itensDefeitos = formulaDesfazer;
		ItemFormulaDTO itemDesfeito = itensDefeitos.get(itensDefeitos.size()-1);
		
		formula.add(itemDesfeito);
		formulaDesfazer.remove(itemDesfeito);
	}
	
	public void limparConsultaAvancada(CampoViewDTO campo){
		campo.getFormula().clear();
		campo.setFormulaDesfazer(new ArrayList<ItemFormulaDTO>());
	}
	
	public void limparCondicaoRetorno(){
		getForm().getFormulaCondicaoRetorno().clear();
		getForm().setFormulaCondicaoRetorno(new ArrayList<ItemFormulaDTO>());
	}
	
	
	public void selecionarTodosOsCampos(){
		getForm().setAdicionarCamposAvancados(false);
		getForm().getCamposConsulta().get(0).setAvancado(getForm().isTodosOsCampos());
		
		iniciarCamposConsulta();
	}

	public void iniciarCamposConsulta() {
		if(getForm().getCamposConsulta().size() > 1){
			getForm().setCamposConsulta(new ArrayList<CampoViewDTO>(getForm().getCamposConsulta().subList(0, 1)));
		}
		
		this.selecionarTipoCampo(0);
	}
	
	public void adicionarItemFormulaCondicaoRetorno(){
		this.adicionarItemFormula(null, getForm().getFormulaCondicaoRetorno());
		
		getForm().setFormulaCondicaoRetornoDesfazer(new ArrayList<ItemFormulaDTO>());
		this.limparCamposConsultas(null);
	}
	
	private void adicionarItemFormula(CampoViewDTO campo, List<ItemFormulaDTO> formula){
		if(hasValorMutavelConsultaAvancada(campo)){
			formula.add(new ItemFormulaDTO(campo.getValorMutavelConsultaAvancado(), true));
		}
		else if(hasCampoConsultaAvancada(campo)){
			formula.add(new ItemFormulaDTO(campo.getCampoConsultaAvancada()));
		}
		else if(hasOperador(campo)){
			formula.add(new ItemFormulaDTO(campo.getOperadorCaonsultaAvancado()));
		}
		else if(getForm().isAbrirParenteses()){
			formula.add(new ItemFormulaDTO(EParenteses.ABRIR));
		}
		else if(getForm().isFecharParenteses()){
			formula.add(new ItemFormulaDTO(EParenteses.FECHAR));
		}
		else if(hasFuncao(campo)){
			formula.add(new ItemFormulaDTO(campo.getFuncaoConsultaAvancado()));
		}
		if(getForm().getCampoCondicaoRetorno() != null){
			formula.add(new ItemFormulaDTO(getForm().getCampoCondicaoRetorno()));
		}
		else if(getForm().getOperadorLogicoCondicaoRetorno() != null){
			formula.add(new ItemFormulaDTO(getForm().getOperadorLogicoCondicaoRetorno()));
		}
		else if(getForm().getOperadoresCondicaoRetorno() != null){
			formula.add(new ItemFormulaDTO(getForm().getOperadoresCondicaoRetorno()));
		}
		else if(getForm().getValorMutavelConsultaRetorno() != null){
			formula.add(new ItemFormulaDTO(getForm().getValorMutavelConsultaRetorno(), true));
		}
	}
	
	public void gerarRelatorio(){
		List<CampoViewDTO>  amposConsulta = getForm().isTodosOsCampos() ? popularCamposRetornoComTodosOsCampos() : getForm().getCamposConsulta();
		List<EntidadeVIiewDTO> relacionamteos = hasRelacionamento() ? getForm().getEntidadesView() : null;
		List<ItemFormulaDTO> formulaCondicaoRetorno = hasformulaCondicaoRetorno() ? getForm().getFormulaCondicaoRetorno() : null;
		
		try {
			getForm().setArquivoDownlod(gerador.gerarRelatorio(getForm().getEntidadeOrigem(), 
										  Util.castList(relacionamteos, IEntidadeVIiewDTO.class),
										  Util.castList(amposConsulta, ICampoViewDTO.class), 
										  Util.castList(formulaCondicaoRetorno,IItemFormulaDTO.class), 
										  getForm().isAgruparResultadoRetorno(),
										  getForm().getNomeArquivo(),
										  getForm().getFullPathClassGerador()));
			
			callBack.gerarRelatorio();
			
		} catch (GeracaoException e) {
			e.printStackTrace();
		}
	}

	private boolean hasformulaCondicaoRetorno() {
		for (ItemFormulaDTO item : getForm().getFormulaCondicaoRetorno()) {
			if(item.getValor() != null){
				return true;
			}
		}
		
		return false;
	}

	private List<CampoViewDTO> popularCamposRetornoComTodosOsCampos() {
		List<CampoViewDTO> ret = new ArrayList<CampoViewDTO>();
		
		for (SelectItem campo : getForm().getCamposView()) {
			ICampoDTO campoDTO = (ICampoDTO) campo.getValue();
			if(campoDTO != null){
				CampoViewDTO campoView = new CampoViewDTO(campoDTO, campoDTO.getLabelSimples(), campoDTO.getMascara());
				ret.add(campoView);
			}
		}
		
		if(getForm().isAdicionarCamposAvancados()){
			ret.addAll(getForm().getCamposConsulta());
		}
		
		return ret;
	}
	
	public void cancelar(){
		getForm().setEntidadeOrigem(null);
		getForm().getEntidadesView().clear();
		habilitarSelecao();
	}
	
	private boolean hasRelacionamento(){
		for (EntidadeVIiewDTO relacionamento : getForm().getEntidadesView()) {
			if(relacionamento.getEntidadeSelecionada() != null){
				return true;
			}
		}
		
		return false;
	}
	
	public void download(){
		try {
			DownloadUtil.download(getForm().getArquivoDownlod().getArquivo(), getForm().getNomeArquivo(), getForm().getArquivoDownlod().getExtensao());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
