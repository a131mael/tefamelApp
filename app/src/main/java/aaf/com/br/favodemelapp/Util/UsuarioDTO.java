package aaf.com.br.favodemelapp.Util;

import java.io.Serializable;

public class UsuarioDTO implements Serializable{

	private static final long serialVersionUID = 1L;

    private Long id;
    private String name;

    private long pontosGeral;
    
    private long pontosMes;
    
    private long pontosSemana;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public long getPontosGeral() {
		return pontosGeral;
	}

	public void setPontosGeral(long pontosGeral) {
		this.pontosGeral = pontosGeral;
	}

	public long getPontosMes() {
		return pontosMes;
	}

	public void setPontosMes(long pontosMes) {
		this.pontosMes = pontosMes;
	}

	public long getPontosSemana() {
		return pontosSemana;
	}

	public void setPontosSemana(long pontosSemana) {
		this.pontosSemana = pontosSemana;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 11 * hash + (this.id != null ? this.id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		final UsuarioDTO other = (UsuarioDTO) obj;
		if (this.getId() != other.getId() && (this.id == null || !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

}
