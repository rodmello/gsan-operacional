package br.gov.pa.cosanpa.gopera.managedBean;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.gov.pa.cosanpa.gopera.fachada.IMacroMedidor;
import br.gov.pa.cosanpa.gopera.model.MacroMedidor;
import br.gov.pa.cosanpa.gopera.model.MacroMedidorAfericao;

@ManagedBean
@SessionScoped
public class MacroMedidorBean extends BaseBean<MacroMedidor> {

	@EJB
	private IMacroMedidor fachada;	
	private MacroMedidorAfericao afericao;
	private String dataAfericao;
	
	public MacroMedidorBean() {
		
	}
	
	public MacroMedidorAfericao getAfericao() {
		return afericao;
	}

	public void setAfericao(MacroMedidorAfericao afericao) {
		this.afericao = afericao;
	}

	public String getDataAfericao() {
		return dataAfericao;
	}

	public void setDataAfericao(String dataAfericao) {
		this.dataAfericao = dataAfericao;
	}

	@Override
	public String iniciar() {
		// Fachada do EJB
		this.setFachada(this.fachada);
		// Cria uma nova instância do registro para um novo cadastro
		this.registro = new MacroMedidor();
		// Páginas de mudança de estados
		this.getPaginasRetorno().put("iniciar", "MacroMedidor.jsf");
		// Página inicial do managedBean
		return this.getPaginasRetorno().get("iniciar");		
	}

	@Override
	public String novo() {
		this.registro = new MacroMedidor();
		return super.novo();
	}	
	
	@Override
	public String consultar() {
		carregar();
		return super.consultar();
	}

	@Override
	public String alterar() {
		carregar();
		return super.alterar();
	}
	
	public void carregar(){
		try {
			this.registro = fachada.obterMacroMedidor(this.registro.getCodigo());
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Carregar");
		}
	}
	
	@Override
	public String confirmar() {
		try {
			registro.setUsuario(usuarioProxy);
			registro.setUltimaAlteracao(new Date());
			return super.confirmar();
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Salvar");
		}
		return null;
	}

	public void incluirAfericao() {
		try{
			MacroMedidorAfericao medidorAfericao = new MacroMedidorAfericao();
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			medidorAfericao.setDataAfericao(format.parse(dataAfericao));
			medidorAfericao.setMedidor(this.registro);
			this.registro.getAfericao().add(medidorAfericao);
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Incluir Aferição");
		}
	}	

	public void excluirAfericao() {
		try{		
			for (MacroMedidorAfericao medidorAfericao : this.registro.getAfericao()) {
				if (medidorAfericao.equals(afericao)) {
					this.registro.getAfericao().remove(medidorAfericao);
					break;
				}
			}
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Excluir Aferição");
			e.printStackTrace();
		}
	}
	
}
