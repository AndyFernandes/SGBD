class RegistroFuncionario extends Registro{
	String nome;
	String sobrenome;
	int idade;

	RegistroFuncionario(){}

	RegistroFuncionario (int id_func, String nome, String sobrenome, int idade){
		super(id_func);
		this.nome = nome;
		this.sobrenome = sobrenome;
		this.idade = idade;
	}

	void setNome(String nome){
		this.nome = nome;
	}

	void setSobrenome(String sobrenome){
		this.sobrenome = sobrenome;
	}

	void setIdade(int idade){
		this.idade = idade;
	}

	String getNome(){
		return this.nome;
	}

	String getSobrenome(){
		return this.sobrenome;
	}

	int getIdade(){
		return this.idade;
	}

	void exibirRegistro(){
		System.out.println("ID_Registro: " + this.idRegistro);
        System.out.println("ID_Paginas: " + this.idPagina);
		System.out.println("ID: " + this.id_func);
        System.out.println("Nome: " + this.nome);
        System.out.println("Sobrenome: " + this.sobrenome);
        System.out.println("Idade: " + this.idade);
        System.out.println("--------------------------");
	}
}