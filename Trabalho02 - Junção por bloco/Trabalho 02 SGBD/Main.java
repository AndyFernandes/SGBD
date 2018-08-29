import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Iterator;
import java.util.LinkedList;
import java.lang.Math;
/*
DUPLA:		Andreza Fernandes de Oliveira
			Thiago Fraxe Correa Pessoa

TEMA: 		Nested Loop Block Join:
            	ENTRADA: Usuário informa a quantidade de blocos a serem usados no algoritmo.
             	SAIDA: O tabelaResultante com a junção de todos os atributos, salvos em um arquivo.

ESTRUTURA: 	Organizamos o trabalho em classes:
				
				- REGISTROS: 	Classe que contém o id_func (já que é comum a todas), id_registro (posicao na pagina) e o id_pagina a qual pertence.
								Além disso possui a funcionalidade checkRegistro() que recebe outro registro e compara se possuem o mesmo id_func.
						- REGISTROFUNCIONARIO:	Classe que extende a classe Registro, possui os dados do registro de um funcionario (nome, sobrenome, idade).
						- REGISTROVENDA:		Classe que extende a classe Registro, possui os dados do registro de um funcionario (id_venda, nome_produto, qnt).
						- REGISTROJUNCAO:		Classe que extende a classe Registro, possui os dados das duas classes acima (REGISTROFUNCIONARIO e REGISTROVENDA).
				
				- PAGINA:		Classe que contém um vetor de registro com 16 posições(registro[]), posicao na tabela (idPagina) e quantidade de registro na página (qnt_Registros).
								Nesta classe também há o método check(), que recebe outra página e chama a função checkRegistro(), que retorna se os registros possuem o mesmo id_func. Caso retorne true, 
								fazemos um novo REGISTROJUNCAO com os dados de ambos os registros e então armazenamos os registros junção em uma lista encadeada e retornamos essa lista.
				
				- TABELA: 	Classe que possui uma lista encadeada de páginas. Nesta classe há o método de exibição de uma tabela (que chama os métodos de exibição de uma página e de um registro, consequentemente),
							Há o método ler(), a qual recebe o início e o fim de páginas que devem ser armazenadas no bloco daquela tabela.
							Há também o método gravarArquivo() que grava todos os dados de uma tabela em um arquivo.
							E por fim, os métodos de acesso (sets&gets) dos atributos e o método adicionarRegistro().
				
				- MAIN:		Aqui é onde está o nosso programa principal: Nele descarregamos os arquivos textos em tabelas, criamos os blocos e fazemos a operação Nested Loop Block Join.
							Armazenamos os registros em uma tabela resultante e então gravamos a tabela resultante em um arquivo resultado.txt.
*/

class Main {
    private static final String VIRGULA = ",";
    
    public static void main(String[] args) throws Exception{
        
        //INSIRA O ARQUIVO DOS FUNCIONARIOS AQUI    
        BufferedReader readerDadosFuncionario = new BufferedReader(new InputStreamReader(new FileInputStream("data_Funcionario.txt")));
        String linha = null;
        Tabela funcionarios = new Tabela();
        Tabela vendas = new Tabela();

        while ((linha = readerDadosFuncionario.readLine()) != null) {
            String[] dadosFuncionario = linha.split(VIRGULA);
            Registro novo = new RegistroFuncionario(Integer.parseInt(dadosFuncionario[0]), dadosFuncionario[1], dadosFuncionario[2], Integer.parseInt(dadosFuncionario[3]));
            funcionarios.adicionarRegistro(novo);
        }
        
        readerDadosFuncionario.close();
        
        //INSIRA O ARQUIVO DAS VENDAS AQUI
        BufferedReader readerDadosVendas = new BufferedReader(new InputStreamReader(new FileInputStream("data_Venda.txt")));
        linha = null;
        while ((linha = readerDadosVendas.readLine()) != null) {
            String[] dadosVenda = linha.split(VIRGULA);
            Registro novo = new RegistroVenda(Integer.parseInt(dadosVenda[0]), Integer.parseInt(dadosVenda[1]), dadosVenda[2], Integer.parseInt(dadosVenda[3]));
            vendas.adicionarRegistro(novo);
        }
        
        readerDadosVendas.close();

        /* EXIBIÇÃO DAS TABELAS 
        System.out.println("--------------------------------- FUNCIONARIOS ------------------------------");
        funcionarios.exibirTabela();
        System.out.println("--------------------------------- VENDAS ------------------------------");
        vendas.exibirTabela();
        */
        
        // 1o - Devemos checar o tamanho das tabelas e pegar a tabela de menor tamanho para particionar em blocos.
        Tabela menor;   
        Tabela maior;

        if (funcionarios.getTamanho() < vendas.getTamanho()){
            menor = funcionarios;
            maior = vendas;
        } else {
            menor = vendas;
            maior = funcionarios;
        }
        
        Scanner input = new Scanner(System.in);
        System.out.println("---------------------- Digite o numero de blocos desejados:  ----------------------");
        int numBlocos = input.nextInt();
        
        if(numBlocos <= menor.getTamanho()){
            int qntPaginasBloco;
            Tabela tabelaResultante = new Tabela();
            LinkedList<RegistroJuncao> registroResultante;

            qntPaginasBloco = (int) Math.ceil(menor.getTamanho() / numBlocos);
            Pagina[] bloco = new Pagina[qntPaginasBloco];   
            
            for(int i = 0; i < numBlocos; i ++){	//interação onde formamos os blocos necessários.
                menor.ler(i, qntPaginasBloco, bloco); // armazena as páginas da menor tabela no bloco de acordo com a quantidade de página por bloco e suas devidas páginas. 
                for(int j = 0; j < qntPaginasBloco; j++){  
                    for(int k = 0; k < maior.getTamanho(); k++){
                        registroResultante = bloco[j].check(maior.getPagina(k)); // Realiza a checagem entre duas páginas, para verificar  se há registros com o id_func igual, e então armazena os registros em uma lista de RegistroJuncao
                        for (RegistroJuncao a: registroResultante) {
                            tabelaResultante.adicionarRegistro(a); //Adiciona os registrosjuncao(retornados pela função check()) na tabela resultado.
                        }
                    }
                }
            }

            //tabelaResultante.exibirTabela();
            tabelaResultante.gravarArquivo("resultado.txt");

        } else{
            System.out.println("Numero de blocos maior que o numero de paginas da menor relação!");
        }       
    }
}