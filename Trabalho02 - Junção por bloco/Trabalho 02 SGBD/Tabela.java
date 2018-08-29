import java.util.Iterator;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.ListIterator;
import java.io.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

class Tabela{
	LinkedList <Pagina> paginas;

	Tabela(){
		this.paginas = new LinkedList<Pagina>();
	}

	void adicionarRegistro(Registro registro){
		if(this.paginas.size() == 0 || this.paginas.getLast().getTamanho() == 16){
			Pagina nova = new Pagina();
			paginas.add(nova);
			nova.setIdPagina(this.paginas.size());
		}
		this.paginas.getLast().adicionarRegistro(registro);
	}

	Pagina getPagina(int posicao){
		return this.paginas.get(posicao);
	}

	int getTamanho(){
		return this.paginas.size();
	}

	void exibirTabela(){
		Iterator <Pagina> itPaginas= paginas.iterator();
		while(itPaginas.hasNext()) {
			itPaginas.next().exibirPagina();
		}
	}

	void ler(int i, int b, Pagina[] bloco){ // i - i-ésimo bloco de b páginas; b = número de páginas por bloco (tamanho do bloco); bloco - bloco de memória 
		ListIterator <Pagina> interador = this.paginas.listIterator(b*i);
		int cont = 0;
		while((cont < b) && interador.hasNext()){
			bloco[cont] = (Pagina)interador.next();
			cont ++;
		}
	}

	void gravarArquivo(String path) throws IOException{
		PrintWriter writer = new PrintWriter(path, "UTF-8");
		ListIterator <Pagina> interador = this.paginas.listIterator(0);

		while(interador.hasNext()){
			Pagina aux = interador.next();
			for(int i = 0; i < aux.getTamanho(); i ++){
				writer.println(aux.getRegistro(i));
			}
		}
		writer.close();
	}
}
