/*
	Equipe: 	Andreza Fernandes de Oliveira, 384341
				Thiago Fraxe Correia Pessoa, 397796
*/
				
class NoFolha extends No {
	No proximo;
	No anterior;
	Integer[] elementos;
	Arvore arvore;

	// ------------------------------------- CONSTRUTORES ------------------------------------------- //

	NoFolha(){
		super();
		this.elementos = new Integer[9];
		this.proximo = null;
		this.anterior = null;
	}

	NoFolha(IndexEntry entradaPai, No noPai){
		super(entradaPai, noPai);
		elementos = new Integer[9];
		this.proximo = null;
		this.anterior = null;
	}

	// ------------------------------------- SETS&GETS ------------------------------------------- //

	void setNoProximo(No proximo){
		this.proximo = proximo;
	}

	No getNoProximo(){
		return this.proximo;
	}

	void setArvore(Arvore arvore){
		this.arvore = arvore;
	}

	Arvore getArvore(){
		return this.arvore;
	}

	void setNoAnterior(No anterior){
		this.anterior = anterior;
	}

	No getNoAnterior(){
		return this.anterior;
	}

	void setElementosNaPosicao(int posicao, int chave) {
		Integer chaveInserir = chave;
		this.elementos[posicao] = chaveInserir;
	}

	Integer getElementosNaPosicaoFolha(int posicao) {
		return this.elementos[posicao];
	} 
	
	// ------------------------------------- MÉTODOS ------------------------------------------- //

	void inserir(int chave) {
		/* 
				CASO BASE: 	Se houver espaço no nó, insere a chave de forma ordenada.
							Aqui, achamos a posição que a chave deveria estar (Ki < chave <= Ki+1) e após damos um shift a direita,
							para podermos inserir o elemento.
							No final, inserimos a novaEntrada na posição que ela deveria estar, e setamos o pai do seu ponteiro para o 
							NoFilho como o nó atual.

				SPLIT:		Caso não tenha espaço, fazemos o split no nó folha, e inserimos a entrada de dados retornada pelo split no pai.

		*/

		Integer chaveInserir = chave;
		int posicaoOcupavel = 0;

		if(this.qtdElementos < 9) {  	
			while(elementos[posicaoOcupavel] != null && chave > elementos[posicaoOcupavel]){ 
				posicaoOcupavel++;
			}

			for(int i = this.qtdElementos; i > posicaoOcupavel; i--){
				elementos[i] = elementos[i-1];
			}
			
			elementos[posicaoOcupavel] = chaveInserir;
			this.qtdElementos++;
		}

		else {
			IndexEntry novaEntrada = this.split(chave);  
			No noPai = this.noPai;
			noPai.inserir(novaEntrada); 
		}

	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	IndexEntry split(int chave) {
		/* 
			ESTUDO DE CASO: 	No split, criaremos um novo nó folha;
								Informamos que esse novo nó pertence a árvore do nó atual;
								Daí pegamos o elemento do meio do nosso nó atual, que no nosso caso é a 4 (o 5o elemento);
								Daí no novo nó iremos colocar os elementos após o elemento do meio, e retiramos eles no nó atual;
								Verificamos se o novo elemento é menor que o elemento do meio. Se for inserimos ela no nó atual. Se não inserimos no novo nó alocado.
								Cria-se uma nova entrada de índice cujo valor da chave é igual à chave na primeira posição do novo nó. Essa entrada aponta para o novo nó criado.
								Excluimos o elemento do meio do nó original.

								Atualizamos os ponteiros de próximo e anterior do novo nó folha e do atual, e retornamos a nova entrada.

		*/
		
		Integer chaveInserir = chave;
		NoFolha novoNo = new NoFolha();
		novoNo.setArvore(this.getArvore()); 
		
		for(int i = 4; i < 9; i++) { 
			novoNo.inserir(this.elementos[4]);
			this.excluir(this.elementos[4], true);  
		}

		
		if (chave < novoNo.getElementosNaPosicaoFolha(0)) this.inserir(chaveInserir); 
		else novoNo.inserir(chaveInserir); 

		IndexEntry novaEntrada = new IndexEntry(novoNo.getElementosNaPosicaoFolha(0), novoNo); 

		if(this.getNoProximo() != null)
			this.getNoProximo().getNoAnterior().setNoAnterior(novoNo);
		novoNo.setNoProximo(this.getNoProximo());
		novoNo.setNoAnterior(this);
		this.setNoProximo(novoNo);

		if(this == this.getArvore().getRaiz()) {
			No novaRaiz = new NoNaoFolha();
			novaRaiz.setP0(this);
			this.getArvore().setRaiz(novaRaiz);
			novaRaiz.setArvore(this.getArvore());
		}
		return novaEntrada;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	void excluir(int chave, boolean transferir) {
		/* 
			ESTUDO DE CASO:		Aqui, consideramos 5 casos:
								
								CASE BASE:

									Primeiro encontramos a posição no nó que se encontra o elemento. 
									Caso o elemento não esteja, anunciamos que a chave não foi encontrada.
									Caso o elemento esteja, iremos dar um shift a esquerda a todos os elementos posteriores a entrada a ser excluida.
									Depois setamos como null a posição onde estava o último elemento (já que movemos todos para a esquerda).
									Depois diminuimos a quantidade de elementos.
								
								Em seguida, verificamos se o número de elementos do nó atual após a exclusão ficou abaixo da ordem(4).
								Caso tenha ficado verificamos os 4 casos restantes, na seguinte ordem:

								EMPRESTA IRMÃO ESQUERDO:
									
									Aqui verificamos se o irmão esquerdo pode emprestar elementos (caso sua quantidade de elementos seja maior que a ordem).
									Caso ela possa chamaremos a função emprestaEsquerdo.

								EMPRESTA IRMÃO DIREITO:

									Aqui verificamos se o irmão direito pode emprestar elementos (caso sua quantidade de elementos seja maior que a ordem).
									Caso ela possa chamaremos a função emprestaDireito.

								MERGE IRMÃO ESQUERDO:

									Aqui verificamos se o irmão esquerdo possui sua quantidade de elementos menor que a ordem.
									Caso possua chamaremos a função fundirEsquerdo.

								MERGE IRMÃO DIREITO:
									Aqui verificamos se o irmão direito possui sua quantidade de elementos menor que a ordem.
									Caso possua chamaremos a função fundirDireito.

							 	O booleano "transferir" serve para o seguinte o caso: às vezes, queremos excluir os elementos de um nó apenas para transferir esses 
							 	elementos para outro nó. Assim, nessa situação, não queremos que o merge ou o empréstimo seja ativado, caso o número de elementos seja menor que 4.
		*/
		int i = 0;
		int pos = -1;

		while(i < this.qtdElementos && pos == -1) {
			if (chave == this.elementos[i])
				pos = i;
			else
				i++;
		}

		if (pos == -1){
			System.out.println("Erro na exclusão: chave não encontrada!");
			return;	
		} 
		else {
			for(int j = pos+1; j < this.qtdElementos; j++) 
				this.elementos[j-1] = this.elementos[j];
			this.elementos[this.qtdElementos-1] = null;
			this.qtdElementos--;
		}

		if(!transferir){
			if(this.qtdElementos < 4) {
				No irmaoEsquerdo = this.obterIrmaoEsquerdo();
				No irmaoDireito = this.obterIrmaoDireito();
		
				if(irmaoEsquerdo != null && irmaoEsquerdo.qtdElementos > 4)
					this.emprestadoEsquerdo(irmaoEsquerdo);
				else if(irmaoDireito != null && irmaoDireito.qtdElementos > 4)
					this.emprestadoDireito(irmaoDireito);
				else if(irmaoEsquerdo != null && irmaoEsquerdo.qtdElementos <= 4)
					this.fundirEsquerdo(irmaoEsquerdo);
				else if(irmaoDireito != null && irmaoDireito.qtdElementos <= 4)
					this.fundirDireito(irmaoDireito);
			} else {
				System.out.println("Exclusão bem sucedida!");
				return;
			}
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	void fundirDireito(No irmaoDireito) {
		/*
			ESTUDO DE CASO:		Essa operação realiza o merge entre o nó atual e o seu irmão Direito.
								Primeiro obtemos o número de elementos a serem copiados para o nó atual, e inserimos eles no nó atual e excluimos eles do nó irmão.
								Atualizamos os ponteiros de próximo e anterior de ambos os nós
								Excluimos a refrência do nó irmão direito em seu nó pai.
		*/

		int quantidadeTransferencias = irmaoDireito.getQtdElementos();
		for(int i = 0; i < quantidadeTransferencias; i++){
			this.inserir(irmaoDireito.getElementosNaPosicaoFolha(0));
			irmaoDireito.excluir(irmaoDireito.getElementosNaPosicaoFolha(0), true);
		}

		this.setNoProximo(irmaoDireito.getNoProximo());
		if(irmaoDireito.getNoProximo() != null) irmaoDireito.getNoProximo().setNoAnterior(this);

		irmaoDireito.setNoAnterior(null);
		irmaoDireito.setNoProximo(null);
		irmaoDireito.getNoPai().excluir(irmaoDireito.getEntradaPai().getChave(), false);
	} 

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	void fundirEsquerdo(No irmaoEsquerdo) {
		/*
			ESTUDO DE CASO:		Essa operação realiza o merge entre o nó atual e o seu Irmão Esquerdo.
								Primeiro obtemos o número de elementos do nó atual a serem copiados para o Irmão Esquerdo, e inserimos eles no nó Irmão Esquerdo e excluimos eles do nó atual.
								Atualizamos os ponteiros de próximo e null de ambos os nós.
								Excluimos a refrência do nó atual em seu nó pai.
		*/

		int quantidadeTransferencias = this.getQtdElementos();
		for(int i = 0; i < quantidadeTransferencias; i++){
			irmaoEsquerdo.inserir(this.elementos[0]);
			this.excluir(this.elementos[0], true);
		}

		irmaoEsquerdo.setNoProximo(this.proximo);
		if(this.proximo != null) this.proximo.setNoAnterior(irmaoEsquerdo);

		this.anterior = null;
		this.proximo = null;
		this.getNoPai().excluir(this.getEntradaPai().getChave(), false);
	} 

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	void emprestadoDireito(No irmaoDireito) {
		/*
			ESTUDO DE CASO:		Essa operação realiza o empréstimo de elementos no nó Irmão Direito para o nó atual.
								O primeiro elemento do Irmão Direito é copiada para o nó atual.
								Excluímos o primeiro elemento do irmão Direito.
								Mudamos a entrada pai do irmão direito para o atual elemento da posição 0.
				
		*/

		this.inserir(irmaoDireito.getElementosNaPosicaoFolha(0));
		irmaoDireito.excluir(irmaoDireito.getElementosNaPosicaoFolha(0), true);

		irmaoDireito.getEntradaPai().setChave( irmaoDireito.getElementosNaPosicaoFolha(0) );

	} 

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	void emprestadoEsquerdo(No irmaoEsquerdo) {
		/*
			ESTUDO DE CASO:		Essa operação realiza o empréstimo de elementos no nó Irmão Esquerdo para o nó atual.
								O valor da chave dá última entrada do Irmão Esquerdo é copiada para entradaPai deste nó.
								Excluímos a última entrada do Irmão Esquerdo.
				
		*/
		this.inserir(irmaoEsquerdo.getElementosNaPosicaoFolha(irmaoEsquerdo.getQtdElementos() - 1) );
		irmaoEsquerdo.excluir(irmaoEsquerdo.getElementosNaPosicaoFolha(irmaoEsquerdo.getQtdElementos() - 1), true);

		this.entradaPai.setChave(this.elementos[0]);

	}   

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	No obterIrmaoEsquerdo() {
		if(this.anterior != null && this.anterior.noPai == this.noPai)
			return this.anterior;
		return null;
	} 

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	No obterIrmaoDireito() {
		if(this.proximo != null && this.proximo.noPai == this.noPai)
			return this.proximo;
		return null;
	}

}
