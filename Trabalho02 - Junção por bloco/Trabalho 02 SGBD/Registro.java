class Registro{
	int idRegistro;
	int  idPagina; 
	int id_func;

	Registro(){}

	Registro(int id_func){
		this.id_func = id_func;
	}

	void setIdPagina(int idPagina){
		this.idPagina = idPagina;
	}

	int getIdPagina(){
		return this.idPagina;
	}

	void setIdRegistro(int id){
		this.idRegistro = id;
	}

	int getIdRegistro(){
		return this.idRegistro;
	}

	void setIdFunc(int id_func){
		this.id_func = id_func;
	}

	int getIdFunc(){
		return this.id_func;
	}
	
	void exibirRegistro(){}
	// precisa de select essas coisas?

	boolean checkRegistro(Registro outro){
		if (this.getIdFunc() == outro.getIdFunc()){
			return true;
		}
		return false;
	}

}