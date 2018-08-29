import java.util.Iterator;
import java.util.LinkedList;

class Pagina{
	int idPagina;
	int qnt_Registros;
	Registro registros[];

	Pagina(){
		this.registros = new Registro[16];
		this.qnt_Registros = 0; 
	}

	void adicionarRegistro(Registro registro){
		this.registros[this.qnt_Registros] = registro;
		registro.setIdRegistro(this.qnt_Registros);
		registro.setIdPagina(this.idPagina);
		this.qnt_Registros ++;
	}

	int getTamanho(){
		return this.qnt_Registros;
	}

	void setIdPagina(int idPagina){
		this.idPagina = idPagina;
	}

	Registro getRegistro(int posicao){
		return this.registros[posicao];
	}
	
	void exibirPagina(){
		for(int i = 0; i < this.qnt_Registros; i++){
			this.registros[i].exibirRegistro();
		}
	}

	LinkedList<RegistroJuncao> check(Pagina outra){ 
		LinkedList<RegistroJuncao> registros = new LinkedList<RegistroJuncao>();

		for(int i = 0; i < this.getTamanho(); i++){
			for(int j = 0; j < outra.getTamanho(); j++){
				if (this.getRegistro(i).checkRegistro(outra.getRegistro(j))){
					RegistroJuncao novo = new RegistroJuncao(this.getRegistro(i), outra.getRegistro(j));
					registros.add(novo);
				}
			}
		}

		return registros;
	}

}