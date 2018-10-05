/*
	Equipe: 	Andreza Fernandes de Oliveira, 384341
				Thiago Fraxe Correia Pessoa, 397796

*/

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;


class Menu {
	
	public static void main(String[] args) {
		Integer[] chaves = new Integer[18];
		Scanner s = new Scanner(System.in);

		int opcao;
		int chave;

		
		Arvore arvore = new Arvore();
		do{
			System.out.println("\n\n##################################################################################################################");
			System.out.println("##################################################################################################################");


			System.out.println("\n\n1 - I  N  S  E  R  I  R    C  H  A  V  E");
			System.out.println("2 - E  X  C  L  U  S  Ã  O");
			System.out.println("3 - B  U  L  K  L  O  A   D  ");
			System.out.println("4 - P  E  S  Q  U  I  S  A   P  O  R  I  G  U  A  L  D  A  D  E");
			System.out.println("5 - P  E  S  Q  U  I  S  A   P  O  R   I  N  T  E  R  V  A  L  O");
			System.out.println("6 - I  M  P  R  I  M  I  R   Á  R  V  O  R  E");
			System.out.println("7 - S  A  I  R");


			System.out.println("\n\n##################################################################################################################");
			System.out.println("##################################################################################################################");

			opcao = s.nextInt();	

			

			switch(opcao){
				case 1:
					System.out.println("\n\n##################################################################################################################");
					System.out.println("##################################################################################################################");
					System.out.println("Informe o valor desejado a ser inserido: ");
					chave = s.nextInt();

					arvore.inserir(chave);
					break;
	
				case 2:
					System.out.println("\n\n##################################################################################################################");
					System.out.println("##################################################################################################################");
					System.out.println("Informe o valor desejado a ser excluído: ");
					chave = s.nextInt();

					arvore.remover(chave);
					break;
				case 3:
					System.out.println("\n\n##################################################################################################################");
					System.out.println("##################################################################################################################");
					int num = 0;

					System.out.println("Informe o número de valores desejados a serem inseridos: ");
					num = s.nextInt();

					chaves = new Integer[num];

					for(int i = 0; i < num; i ++){
						System.out.println("Informe o valor desejado a ser inserido na posição" + i + " : ");
						chave = s.nextInt();
						chaves[i] = chave;
					}

					arvore.bulkLoad(chaves);
					break;

				case 4:

					System.out.println("\n\n##################################################################################################################");
					System.out.println("##################################################################################################################");
					System.out.println("Informe o valor desejado para ser buscado: ");
					chave = s.nextInt();

					if(arvore.buscaIgualdade(chave)) System.out.println("Chave encontrada!");
					else System.out.println("Chave não encontrada!");
					break;

				case 5:
					int chaveFinal;

					System.out.println("\n\n##################################################################################################################");
					System.out.println("##################################################################################################################");
					System.out.println("Informe o valor desejado da chave inicial a ser buscada: ");
					chave = s.nextInt();
					System.out.println("Informe o valor desejado da chave final a ser buscada: ");
					chaveFinal = s.nextInt();

					LinkedList <Integer> valoresSelecao = arvore.buscaIntervalo(chave, chaveFinal);
					Iterator <Integer> itValoresSelecao= valoresSelecao.iterator();

					System.out.println("| ");
					while(itValoresSelecao.hasNext()) {
						System.out.println(" | " + itValoresSelecao.next());
					}
						break;

				case 6:
					System.out.println("\n\n##################################################################################################################");
					System.out.println("##################################################################################################################");
					if(arvore.getRaiz().getQtdElementos() != 0)
						arvore.imprimirArvore();
					else
						System.out.println("Árvore vazia");
					break;
				
				case 7:
					opcao = 0;
					s.close();
					break;

				default:
					System.out.println("Comando inválido! Tente novamente.");
					break;
					
			}

		} while(opcao != 0);

	}
}