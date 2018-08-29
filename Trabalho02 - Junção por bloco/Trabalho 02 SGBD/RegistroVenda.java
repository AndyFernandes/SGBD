class RegistroVenda extends Registro{
	int id_ven;
	String nome_produto;
	int qnt;

	RegistroVenda(){}

	RegistroVenda(int id_ven, int id_func, String nome_produto, int qnt){
		super(id_func);
		this.id_ven = id_ven;
		this.nome_produto = nome_produto;
		this.qnt = qnt;
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

	void exibirRegistro(){
		System.out.println("ID_Registro: " + this.idRegistro);
        System.out.println("ID_Paginas: " + this.idPagina);
		System.out.println("ID_Func: " + this.id_func);
        System.out.println("ID_VENDA: " + this.id_ven);
        System.out.println("Nome do Produto: " + this.nome_produto);
        System.out.println("Quantidade:: " + this.qnt);
        System.out.println("--------------------------");
	}


}