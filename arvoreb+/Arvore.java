/*
	Equipe: 	Andreza Fernandes de Oliveira, 384341
				Thiago Fraxe Correia Pessoa, 397796

*/

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

class Arvore{
	No raiz;

	// ------------------------------------- CONSTRUTORES ------------------------------------------- //

	Arvore(){
		this.raiz = new NoNaoFolha(null, null, null);
		this.raiz.setArvore(this);
	}

	// ------------------------------------- SETS&GETS ------------------------------------------- //

	void setRaiz(No noRaiz) {
		raiz = noRaiz;
	}

	No getRaiz() {
		return this.raiz;
	}

	// ------------------------------------- MÉTODOS ------------------------------------------- //

	No retornarFolha(int chave) {
		/*
			ESTUDO DE CASO: 	Essa função tem como objetivo encontrar o nó a qual uma chave deveria pertencer. Essa busca é feita a partir da raiz e então descemos no nível da árvore
								de acordo com a propriedade (Ki < chave <= Ki+1).
		*/

		int chaveaux, i;  
		No noaux = this.raiz;
		
		while(noaux instanceof NoNaoFolha){
			i = 0;
	        chaveaux = noaux.getElementosNaPosicao(0).getChave(); 
			
			while (i < noaux.qtdElementos && chave >= chaveaux ){
				i++;
				if (i < noaux.qtdElementos) 
					chaveaux = noaux.getElementosNaPosicao(i).getChave();
			}

			if (i == 0)
				noaux = noaux.getP0();
			else 
				noaux = noaux.getElementosNaPosicao(i - 1).getNoFilho();	
		}
		return noaux;
	} 

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

	Boolean inserir(int chave) { 
		/*
			ESTUDO DE CASO: 	Insere entrada de dados na árvore. Primeiro, usa método retornarFolha; depois, usa o metodo inserir no nó folha retornado.
		*/

		No noInserir = this.retornarFolha(chave);
		noInserir.inserir(chave);

		return true;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	Boolean remover(int chave) {
		/*
			ESTUDO DE CASO: 	Remove nó da árvore. Primeiro, usa métoda retornarFolha; depois, usa o método remover no nó folha retornado.
		*/

		No noExcluir = this.retornarFolha(chave);
		noExcluir.excluir(chave, false);
		return false;
	} 
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	Boolean bulkLoad(Integer[] entrada) { 
		/*
			ESTUDO DE CASO: 	Primeiro ordenamos o vetor de elementos passados, utilizando o bubble sort.
								Depois criamos nós e agrupamos de 9 em 9 os elementos do vetor.
								Aí, no caso, o primeiro nó fica sendo apontado pelo P0 da raiz, e os demais nós, pegamos  o primeiro elemento do nó,
								aí fazemos uma Entrada de Índice e inserimos na raiz, aí o nó Filho dessa entrada de Índice é esse nó do qual pegamos a chave.
								Aí eventualmente, a raiz poderá sofrer o split, e isso é tratado na inserção.
		*/

		int aux;
		for(int i = 0; i < entrada.length; i++){
			for(int j = 0; j < i; j++){
				if(entrada[i] < entrada[j]){
					aux = entrada[j];
					entrada[j] = entrada[i];
					entrada[i] = aux;
				}
			}
		} 

		NoFolha no = new NoFolha();

		this.getRaiz().setP0(no);
		no.setArvore(this);

		for (int i = 0; i < entrada.length; i++){
			if(i > 0 && i%9 == 0){ 
				NoFolha noAnterior = no;
			    no = new NoFolha();
			    no.setArvore(this);
			    noAnterior.setNoProximo(no);
			    no.setNoAnterior(noAnterior);
				
				if(i >= 9){
					IndexEntry novo = new IndexEntry(entrada[i], no)
					No noRaiz = this.getRaiz();
					
					if(i <= 90) noRaiz.inserir(novo);
					else{
						No noProximo = noRaiz;
						No noInserir;

						do{
							noInserir = noProximo;
							int numElementos = noInserir.getQtdElementos();
							IndexEntry ultimoElemento = noInserir.getElementosNaPosicao(numElementos-1);
							noProximo = ultimoElemento.getNoFilho();
						
						}while(noProximo instanceof NoNaoFolha);
						noInserir.inserir(novo);
					}
					
				}

			}
			no.inserir(entrada[i]);
		}
		return true;
	} 
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	void imprimirArvore() {
		/*
			ESTUDO DE CASO: 	Utilizamos o conceito de percurso em largura para imprimirmos nossa árvore.
								Utilizamos duas filas: a fila de impressão e a fila de armazenamento. A fila de impressão é os nós que serão percorridos e mostrados seus valores. Ao mesmo tempo
								que se é percorrido, os seus nós filhos são armazenados na fila_armazenamento.
								No final do laço, todos os componentes da fila_armazenamento são passados para fila_impressão e assim continua o processo. 
								O j é a variável de nível, ele indica em que nível aquele nó está.
		*/

        Queue <No> fila_impressao = new LinkedList<No> ();
        Queue <No> fila_armazenamento = new LinkedList<No>();

        No raiz = this.getRaiz();
        fila_impressao.add(raiz);

        int j = 0; 

        while (!fila_impressao.isEmpty()){
            raiz = fila_impressao.poll();
            if(raiz instanceof NoNaoFolha)
                fila_armazenamento.add(raiz.getP0());
            System.out.print(j + ".  ");
            for(int i = 0; i < raiz.getQtdElementos(); i ++){
                if(raiz instanceof NoNaoFolha){
                    System.out.print("|" + raiz.getElementosNaPosicao(i).getChave()); 
                    fila_armazenamento.add(raiz.getElementosNaPosicao(i).getNoFilho());
                } else {
                    System.out.print("|" + raiz.getElementosNaPosicaoFolha(i));
                }
            }

            System.out.print("      ");

            if(fila_impressao.isEmpty()) {
                while(!fila_armazenamento.isEmpty()){
                    fila_impressao.add(fila_armazenamento.poll());
                }
                j++;
                System.out.println();
            }       
        }
    }

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	boolean buscaIgualdade(int chave) {
		/*
			ESTUDO DE CASO: 	Percorre árvore e retorna verdadeiro se chave estiver no nó folha correspondente; retorna falso caso contrário. 
		*/

		No no = retornarFolha(chave);
		int i = 0, j = 0;

		while(i < no.getQtdElementos()){
			if(no.getElementosNaPosicaoFolha(i) == chave){
				return true;
			}
			i++;
		}
		return false;
	} 

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	LinkedList <Integer> buscaIntervalo(int valorInicial, int valorFinal) {
		/*
			ESTUDO DE CASO: 	Dado um intervalo, cujos limites são valorInicial e valorFinal, retornar vetor de inteiros com as chaves da árvores que satisfazem o intervalo.
								Primeiro pegamos o nó folha que o vlaor inicial deveria estar, e então percorremos a folha (ou as demais folhas) atrás do valor que é < = ao valor final.
								No caso, dentro do primeiro laço procuramos o valor que é >= ao valor inicial, e a partir daí procuramos o valor final ou o menor que ele.
								Aí, caso o valor esteja em outra folha, é verificado para que o nó de interação atualize pro próximo nó do nó atual. E aí por diante.
		*/

		No no = retornarFolha(valorInicial);
		int i = 0;
		LinkedList <Integer> intervalo = new LinkedList <Integer>();

		while(no != null && i < no.getQtdElementos()){
			if(no.getElementosNaPosicaoFolha(i) >= valorInicial){
				 int j = i;
				while (j !=9 && no != null && valorFinal >= no.getElementosNaPosicaoFolha(j)){
					if (j < 8){
						intervalo.add(no.getElementosNaPosicaoFolha(j));
						j++;	
					} else {
						intervalo.add(no.getElementosNaPosicaoFolha(j));
						j = 0;
						no = no.getNoProximo();
					}
				}
			}
			
			i++;
		}
		return intervalo; 
	} 
}

