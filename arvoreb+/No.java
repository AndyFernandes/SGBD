/*
	Equipe: 	Andreza Fernandes de Oliveira, 384341
				Thiago Fraxe Correia Pessoa, 397796
*/
				
abstract class No{
	IndexEntry entradaPai;
	No noPai;
	int qtdElementos;
	
	// ------------------------------------- CONSTRUTORES ------------------------------------------- //

	No(){
		this.entradaPai = null;
		this.noPai = null;
		this.qtdElementos = 0;
	}

	No(IndexEntry entradaPai, No noPai){
		this.entradaPai = entradaPai;
		this.noPai = noPai;
		qtdElementos = 0;
	}

	// ------------------------------------- SETS&GETS ------------------------------------------- //

	void setNoPai(No noPai){
		this.noPai = noPai;
	}

	void setEntradaPai(IndexEntry entradaPai){
		this.entradaPai = entradaPai;
	}

	void setNoProximo(No proximoNo) {} // Nó folha

	void setNoAnterior(No noAnterior) {} // Nó folha

	void setElementosNaPosicao(int posicao, int chave) {} // Nó folha

	void setElementosNaPosicao(int posicao, IndexEntry entrada) {} // Nó não-folha

	void setP0(No novoP0) {} // Nó não-folha

	void setArvore(Arvore tree) {} //Nó não-folha

	No getNoPai() {
		return this.noPai;
	}

	IndexEntry getEntradaPai() {
		return this.entradaPai;
	}

	int getQtdElementos() {
		return this.qtdElementos;
	}

	No getNoProximo() {
		return null;
	} //Nó folha

	No getNoAnterior() {
		return null;
	} //Nó folha

	Integer getElementosNaPosicaoFolha(int posicao) {
		return null;
	} //Nó folha

	IndexEntry getElementosNaPosicao(int posicao) {
		return null;
	} //Nó não-folha

	No getP0() {
		return null;
	} //Nó não-folha

	Arvore getArvore() {
		return null;
	} //Nó não-folha


	// ------------------------------------- MÉTODOS ------------------------------------------- //
	
	abstract void excluir(int chave, boolean transferir); 

	void inserir(int chave) {} //Nó folha

	void inserir(IndexEntry novaEntrada) {} //Nó não-folha

	abstract void fundirDireito(No irmaoDireito); //"Transfere" elementos do irmão direito para o nó que chama o método.

	abstract void fundirEsquerdo(No irmaoEsquerdo); //"Transfere" elementos do nó que chama o método para o nó esquerdo. 

	abstract void emprestadoDireito(No irmaoDireito); // O nó que chama o método pega emprestado do nó direito

	abstract void emprestadoEsquerdo(No irmaoEsquerdo); // O nó que chama o método pega emprestado do nó esquerdo.  

	abstract No obterIrmaoEsquerdo(); //Retorna o irmão esquerda de um nó, se houver; se não houver, retorna null

	abstract No obterIrmaoDireito();//Retorna o irmão direito de um nó, se houve; se não houver, retorna null

	IndexEntry split (int chave) {
		return null;
	} // Nó-Folha
	
	IndexEntry split (IndexEntry novaEntrada) {
		return null;
	} //Nó não-folha


}