/*
	Equipe: 	Andreza Fernandes de Oliveira, 384341
				Thiago Fraxe Correia Pessoa, 397796
*/
				
class IndexEntry {
	int chave;
	No noFilho;

	// ------------------------------------- CONSTRUTORES ------------------------------------------- //

	IndexEntry(int chave, No noFilho){
		this.chave = chave;
		this.noFilho = noFilho;
	}

	// ------------------------------------- SETS&GETS ------------------------------------------- //

	void setChave(int chave) {
		this.chave = chave;
	}

	int getChave() {
		return chave;
	}

	void setNoFilho (No novoFilho) {
		noFilho = novoFilho;
	}

	No getNoFilho() {
		return noFilho;
	}
}