package br.com.geradorRelatorioCore.exception;

public class EntidadeConsumerException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public enum EError{
		
		PROPERTIES_NOT_FOUND, CLASS_NOT_FOUND, CLASS_NOT_VALID, RELACIONAMENTO_IVALIDO, MASCARA_INVALIDA;
		
	}
	
	public EntidadeConsumerException(Exception ex, EError error) {
		super(getMessage(error), ex);
	}
	
	public EntidadeConsumerException(EError error) {
		super(getMessage(error));
	}

	private static String getMessage(EError error) {
		
		switch (error) {
		case PROPERTIES_NOT_FOUND:
			return "Arquivo de configuração não encontrado!";
		case CLASS_NOT_FOUND:
				return "Classe não encontrada!"; 
		case CLASS_NOT_VALID:
				return "Classe não valida!";
		case RELACIONAMENTO_IVALIDO:
			return "Relacionamento invalido";
		case MASCARA_INVALIDA:
			return "Mascara invalida";
		default:
			return null;
		}
	}
	
}
