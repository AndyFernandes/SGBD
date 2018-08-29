class RegistroJuncao extends Registro{
	String nome;
	String sobrenome;
	int idade;
	int id_ven;
	String nome_produto;
	int qnt;

	RegistroJuncao(){}

	RegistroJuncao(Registro registro1, Registro registro2){
		super(registro1.getIdFunc());
		//fazer os negocio
		RegistroFuncionario funcionario = new RegistroFuncionario();
		RegistroVenda vendas = new RegistroVenda();

		
		if(registro1 instanceof RegistroFuncionario){
			funcionario = (RegistroFuncionario) registro1;
			vendas = (RegistroVenda) registro2;
		} else {
			funcionario = (RegistroFuncionario) registro2;
			vendas = (RegistroVenda) registro1;
		}
			this.nome = funcionario.getNome();
			this.sobrenome = funcionario.getSobrenome();
			this.idade = funcionario.getIdade();	
			this.id_ven = vendas.getIdVen();
			this.nome_produto = vendas.getNomeProduto();
			this.qnt = vendas.getQnt();
	}

	void setIdVen(int id_ven){
		this.id_ven = id_ven;
	}

	void setNomeProduto(String nome_produto){
		this.nome_produto = nome_produto;
	}

	void setQnt(int qnt){
		this.qnt = qnt;
	}

	int getIdVen(){
		return this.id_ven;
	}

	String getNomeProduto(){
		return this.nome_produto;
	}

	int getQnt(){
		return this.qnt;
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
		System.out.println("ID_Func: " + this.id_func);
		System.out.println("Nome: " + this.nome);
        System.out.println("Sobrenome: " + this.sobrenome);
        System.out.println("Idade: " + this.idade);
        System.out.println("ID_VENDA: " + this.id_ven);
        System.out.println("Nome do Produto: " + this.nome_produto);
        System.out.println("Quantidade:: " + this.qnt);
        System.out.println("--------------------------");
	}

	public String toString(){
		return this.id_func + ", " + this.nome + ", " + this.sobrenome + ", " + this.idade + ", " + this.id_ven + ", " + this.nome_produto + ", " + this.qnt + "\n";
	}
}