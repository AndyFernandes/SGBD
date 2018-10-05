/*
	Equipe: 	Andreza Fernandes de Oliveira, 384341
				Thiago Fraxe Correia Pessoa, 397796
*/

class NoNaoFolha extends No {
	No p0;
	IndexEntry[] elementos;
	Arvore arvore;
	
	// ------------------------------------- CONSTRUTORES ------------------------------------------- //
	
	NoNaoFolha(){
		super();
		p0 = null;
		elementos = new IndexEntry[9];
	}

	NoNaoFolha(IndexEntry entradaPai, No noPai, No p0){
		super(entradaPai, noPai);
		this.p0 = p0;
		elementos = new IndexEntry[9];
	}

	// ------------------------------------- SETS&GETS ------------------------------------------- //
	
	void setArvore(Arvore tree) {
		this.arvore = tree;
	}

	Arvore getArvore() {
		return this.arvore;
	} 

	void setElementosNaPosicao(int posicao, IndexEntry entrada) { 
		this.elementos[posicao] = entrada;
	}

	IndexEntry getElementosNaPosicao(int posicao) {
		return this.elementos[posicao];
	} 

	void setP0(No novoP0) { 
		this.p0 = novoP0;
		novoP0.setNoPai(this);
		novoP0.setEntradaPai(null); 
	}
	
	No getP0() {
		return this.p0;
	} 
	
	// ------------------------------------- MÉTODOS ------------------------------------------- //

	 void inserir(IndexEntry novaEntrada) {
		if(this.qtdElementos < 9) { 
			/* 
						CASO BASE: 	Se houver espaço no nó, insere a novaEntrada de forma ordenada.
									Aqui, achamos a posição que o elemento deveria estar (Ki < chave <= Ki+1) e após damos um shift a direita,
									para podermos inserir o elemento.
									No final, inserimos a novaEntrada na posição que ela deveria estar, e setamos o pai do seu ponteiro para o 
									NoFilho como o nó atual.
									A entradaPai do nó para o qual a novaEntrada aponta é "setado" como a novaEntrada.

				CASO ALTERNATIVO:	Se não houver espaço no nó a ser inserido, devemos realizar o Split do nó.
									O método split retorna uma Entrada de Índice, a qual iremos inserir no nó Pai do nó atual.
			 */

			int posicaoOcupavel = 0;
			
			while(elementos[posicaoOcupavel] != null && novaEntrada.chave > elementos[posicaoOcupavel].getChave()){ 
				posicaoOcupavel++;
			}

			for(int i = this.qtdElementos; i > posicaoOcupavel; i--){  
				elementos[i] = elementos[i - 1];
			}
			
			elementos[posicaoOcupavel] = novaEntrada;
			this.qtdElementos++;
			novaEntrada.getNoFilho().setNoPai(this);
			novaEntrada.getNoFilho().setEntradaPai(novaEntrada); 
			 
		} else {  
			IndexEntry splitEntrada = this.split(novaEntrada); 
			No noPai = this.noPai;
			noPai.inserir(splitEntrada);
		}	
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	IndexEntry split(IndexEntry novaEntrada){ 
		/* 
			ESTUDO DE CASO: 	No split, criaremos um novo nó não folha;
								Informamos que esse novo nó pertence a árvore do nó atual;
								Daí pegamos a  entrada do meio do nosso nó atual, que no nosso caso é a 4 (o 5o elemento);
								O P0 do novo nó vai apontar para onde a entrada do meio aponta;
								A Entrada de Índice (o elemento) do meio vai apontar para o novo nó. Essa Entrada irá subir para o nó pai do nó atual, como ja já veremos;
								Daí no novo nó iremos colocar os elementos após a Entrada de Índice do meio, e retiramos eles no nó atual;
								Verificamos se a Nova Entrada é menor que a Entrada do meio. Se for inserimos ela no nó atual. Se não inserimos no novo nó alocado.
								Excluimos o elemento do meio do nó original.

			
			SPLIT NA RAIZ:		Caso o nó que sofreu Split era o nó raiz, devemos alocar um novo nó não folha;
								Neste novo nó setamos o ponteiro P0 como o nó atual(a raiz atual);
								Setamos essa como a nova raiz da árvore atual, e setamos a árvore a qual este novo nó raiz pertence;
								

								Por fim, retornamos essa Entrada do Meio para ser inserida no nó pai do nó atual.
		*/
		
		NoNaoFolha novoNo = new NoNaoFolha(); 
		novoNo.setArvore(this.getArvore());
		IndexEntry entradaMeio = this.elementos[4];

		novoNo.setP0(entradaMeio.getNoFilho());  
		entradaMeio.setNoFilho(novoNo);  
		
		
		for(int i = 5; i < 9; i++){  
			novoNo.inserir(this.elementos[5]);
			this.excluir(getElementosNaPosicao(5).getChave(), true);
		}

		if(novaEntrada.chave < entradaMeio.getChave()){ 
			this.inserir(novaEntrada);
		} else {
			novoNo.inserir(novaEntrada); 
		}

		this.excluir(entradaMeio.getChave(), true); 

		if (this == this.getArvore().getRaiz()) {
			NoNaoFolha novaRaiz = new NoNaoFolha();
			novaRaiz.setP0(this.getArvore().getRaiz());
			this.getArvore().setRaiz(novaRaiz);
			novaRaiz.setArvore(this.getArvore());
		}

		return entradaMeio; 
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	void excluir(int chave, boolean transferir) {
		
		/* 
			ESTUDO DE CASO:		Aqui, consideramos 5 casos:
								
								CASE BASE:

									Primeiro encontramos a posição no nó que se encontra a chave da Entrada de Índice. 
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

		int i = 0, posicao = -1;

		while(i < this.qtdElementos && posicao == -1) {
			if (chave == this.elementos[i].chave)
				posicao = i;
			else
				i++;
		}

		if (posicao == -1)
			System.out.println("Erro na exclusão: chave não encontrada!");	 
		else {
			for(int j = posicao + 1; j < this.qtdElementos; j++) 
				this.elementos[j - 1] = this.elementos[j];
			this.elementos[this.qtdElementos - 1] = null;
			this.qtdElementos--;
		}
		
		No noRaiz = this.getArvore().getRaiz();
		
		if(!transferir && this != noRaiz) {
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
			}
		} else if(this == noRaiz && this.qtdElementos == 0) 
			this.getArvore().setRaiz(this.p0);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	void fundirDireito(No irmaoDireito) {
		/*
			ESTUDO DE CASO:		Essa operação realiza o merge entre o nó atual e o seu irmão Direito.
								Primeiro obtemos o número de elementos a serem copiados para o nó atual, e inserimos eles no nó atual e excluimos eles do nó irmão.
								Após isso, instanciamos uma nova Entrada de Índice, referente a entrada pai do irmão direito com noFilho = P0 do irmão direito.
								Inserimos essa nova entrada no nó atual.
								Excluimos a refrência do nó irmão direito em seu nó pai.
		*/

		int quantidadeTransferencias = irmaoDireito.getQtdElementos();
		
		for(int i = 0; i < quantidadeTransferencias; i++){ 
			this.inserir(irmaoDireito.getElementosNaPosicao(0));
			this.excluir(irmaoDireito.getElementosNaPosicao(0).getChave(), true);
		}

		IndexEntry novaEntrada = new IndexEntry(irmaoDireito.getEntradaPai().getChave(), irmaoDireito.getP0()); 
		this.inserir(novaEntrada); 
		irmaoDireito.noPai.excluir(irmaoDireito.entradaPai.chave, false); 
	} 

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	void fundirEsquerdo(No irmaoEsquerdo) {
		/*
			ESTUDO DE CASO:		Essa operação realiza o merge entre o nó atual e o seu Irmão Esquerdo.
								Primeiro obtemos o número de elementos do nó atual a serem copiados para o Irmão Esquerdo, e inserimos eles no nó Irmão Esquerdo e excluimos eles do nó atual.
								Após isso, instanciamos uma nova Entrada de Índice, referente a entrada pai do nó atual com noFilho = P0 do nó atual.
								Inserimos essa nova entrada no Irmão Esquerdo.
								Excluimos a refrência do nó atual em seu nó pai.
		*/

		int quantidadeTransferencias = this.getQtdElementos();

		for(int i = 0; i < quantidadeTransferencias; i++){ 
			irmaoEsquerdo.inserir(this.elementos[0]); 
			this.excluir(this.elementos[0].chave, true);
		}

		IndexEntry novaEntrada = new IndexEntry(this.entradaPai.getChave(), this.p0);
		irmaoEsquerdo.inserir(novaEntrada); 
		this.noPai.excluir(this.entradaPai.chave, false); 
	} 

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	void emprestadoDireito(No irmaoDireito) {
		/*
			ESTUDO DE CASO:		Essa operação realiza o empréstimo de elementos no nó Irmão Direito para o nó atual.
								Criamos uma nova Entrada de Índice que possui como chave a Entrada Pai do Irmão Direito e como Nó Filho o P0 do Irmão Direito.
								Inserimos essa nova entrada no nó atual.
								Setamos o P0 do irmão direito como o Filho do ponteiro da primeira entrada no nó Irmão Direito.
								Chave do primeiro elemento do Irmão Direito é copiada para a chave da entrada Pai do Irmão Direito.
								Excluímos o primeiro elemento do irmão Direito.
				
		*/

		IndexEntry novaEntrada = new IndexEntry(irmaoDireito.getEntradaPai().getChave(), irmaoDireito.getP0());
		this.inserir(novaEntrada);
		irmaoDireito.setP0(irmaoDireito.getElementosNaPosicao(0).getNoFilho());
		irmaoDireito.getEntradaPai().setChave(irmaoDireito.getElementosNaPosicao(0).getChave()); 
		irmaoDireito.excluir(irmaoDireito.getElementosNaPosicao(0).getChave(), false);
	} 

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	void emprestadoEsquerdo(No irmaoEsquerdo) {
		/*
			ESTUDO DE CASO:		Essa operação realiza o empréstimo de elementos no nó Irmão Esquerdo para o nó atual.
								Criamos uma nova Entrada de Índice que possui como chave a Entrada Pai do nó atual e como Nó Filho o P0 do nó atual.
								Inserimos essa nova entrada no nó atual.
								O valor da chave dá última entrada do Irmão Esquerdo é copiada para entradaPai deste nó.
								P0 deste nó <- nó filho da última entrada do Irmão Esquerdo.
								Excluímos a última entrada do Irmão Esquerdo.
				
		*/

		IndexEntry novaEntrada = new IndexEntry(this.entradaPai.getChave(), this.getP0());
		this.inserir(novaEntrada);
		this.entradaPai.setChave(irmaoEsquerdo.getElementosNaPosicao(irmaoEsquerdo.getQtdElementos() - 1).getChave());
		this.setP0(irmaoEsquerdo.getElementosNaPosicao(irmaoEsquerdo.getQtdElementos() - 1).getNoFilho());
		irmaoEsquerdo.excluir(irmaoEsquerdo.getElementosNaPosicao(irmaoEsquerdo.getQtdElementos() - 1).chave, false);
	}  

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	No obterIrmaoEsquerdo() {
		/*
			ESTUDO DE CASO:		Essa operação retorna o irmão esquerdo do nó que estamos chamando.
								Primeiro vemos a entrada pai do nó atual, e buscamos a posição dela no nó pai do nosso nó atual. 
								No caso, pegaremos o noFilho da entrada na posição anterior a ela.
								Retornamos o irmão Esquerdo.		
		*/

		IndexEntry entradaPaiDoNo = this.getEntradaPai();
		No pai = this.getNoPai();
		int posicao = -1;

		if(entradaPaiDoNo == null){
			return null;
		} else {
			for(int i = 0; i < pai.getQtdElementos(); i++){
				if(entradaPaiDoNo.getChave() == pai.getElementosNaPosicao(i).getChave()){
					posicao = i;
				}
			}
		}
		return pai.getElementosNaPosicao(posicao - 1).getNoFilho();
	} 

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	No obterIrmaoDireito() {
		/*
			ESTUDO DE CASO:		Essa operação retorna o irmão direito do nó que estamos chamando.
								Primeiro vemos a entrada pai do nó atual, e buscamos a posição dela no nó pai do nosso nó atual. 
								No caso, pegaremos o noFilho da entrada na posição posterior a ela.
								Retornamos o irmão Direito.		
		*/

		IndexEntry entradaPaiDoNo = this.getEntradaPai();
		No pai = this.getNoPai();
		int posicao = -1;

		for(int i = 0; i < pai.getQtdElementos(); i++){
			if(entradaPaiDoNo.getChave() == pai.getElementosNaPosicao(i).getChave()){
				posicao = i;
			}
		}

		if(posicao == (pai.getQtdElementos() - 1))
			return null;
		return pai.getElementosNaPosicao(posicao + 1).getNoFilho();
	}
}
