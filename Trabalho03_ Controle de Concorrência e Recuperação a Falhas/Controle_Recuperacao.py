# ALUNOS:
#			Andreza Fernandes de Oliveira, 384341
# 			Thiago Fraxe Correa Pessoa, 397796

import re


############## REGEX ############
# Utilizamos o Regex para encontrar números(ids/timestamps) das transações e os dados/objetos do banco

numberRegex = re.compile('\d+') # encontra o número/timestamp da transação nas operações de BT, C, w e r.
dadoRegex   = re.compile('\(\s*([a-z]+)\s*\)') # encontra o dado/objeto do banco.
espacoBranco = re.compile('\s+')


# leitura do arquivo.txt
with open("teste3.txt") as t:
	historicoRaw = t.read()
	historicoRaw = re.sub(espacoBranco, '', historicoRaw)

#print(historicoRaw2)
historico = historicoRaw.split(',')



################################ 1a PARTE: CONTROLE DE CONCORRÊNCIA ################################

#Indicar o estado de cada dadoRegex. 1 caractere só para o dadoRegex 
# <nomeDoDado, timeStampLeitura, timeStampEscrita> 
#TS da transação mais recente que leu/escreveu o dadoRegex
#Algoritmo do timestamp básico (ver no slide fornecido pelo professor)
#Mostrar situação dos dados e os timestamp
#Resultado: todo o scheduler sem FL() e sem CP() (se sucesso). scheduler corrigido (caso falha)

'''
if op = 'Read'
	Verifca TS do dadoRegex. Se TS da transação (que tá fazendo operação) for menor ou igual que o TS de escrita  do dadoRegex, então há problema
	Caso contrário, atualiza o timestamp do dadoRegex

elif op = "Write"
	Verifica TS do dadoRegex. Se ts da transação (que tá fazendo opração) for menor ou igual que TS de leitura ou menor ou igual que TS de escrita do dadoRegex, então há problema
'''

#Usando dicionário e regex para encontrar todos os dados:
dicDados = {}

for dado in dadoRegex.findall(historicoRaw):
	if dado not in dicDados:
		dicDados[dado] = [0, 0, ""] # primeira posição: TS leitura; segunda posição: TS escrita; terceira posição: última operação que utilizou o dado/objeto

condicao = True 

print("ENTRADA: \n")

while(condicao):
	

	for dado in dicDados:
		dicDados[dado] = [0, 0, ""]

	print(*historico)
	print("=" * 101 + "\n")
	
	for operation in historico:
		if (operation[:1] == 'w'):
			
			timestampTransacao = int(numberRegex.search(operation).group())
			dado = dadoRegex.search(operation).group(1)
			timestampRead = dicDados[dado][0]
			timestampWrite = dicDados[dado][1]
			
			if ((timestampTransacao < timestampWrite) or (timestampTransacao < timestampRead)):
				ultimaOperacao = dicDados[dado][2]
				idUltimaOperacao = int(numberRegex.search(ultimaOperacao).group())
				problema = "" 

				if timestampTransacao < timestampRead:
					problema = " JA LEU O DADO " + dado
				elif (timestampTransacao < timestampWrite):
					problema = " JA ESCREVEU O DADO " + dado
				
				print("\nPROBLEMA ENCONTRADO NA OPERACAO " + operation + ", POIS A TRANSAÇÃO " + str(idUltimaOperacao) + problema)
				print("\n" +"=" * 101)
				
				historico.remove(operation)
				ultimaOperacao = dicDados[dado][2]
				indice = historico.index(ultimaOperacao)
				historico.insert(indice, operation)
				condicao = True
				print("\n" + "=" * 101)
				print("\nTENTATIVA: ")
				break;
			
			else:
				
				dicDados[dado][1] =timestampTransacao
				dicDados[dado][2] = operation
				condicao = False
				
				print(operation + ": ")
				for dados in dicDados:
					print("< " + dados + ", r: " + str(dicDados[dados][0]) + ", w: " + str(dicDados[dados][1]) + ">")


		elif (operation[:1] == 'r'):
			
			timestampTransacao = int(numberRegex.search(operation).group())
			dado = dadoRegex.search(operation).group(1)
			timestampWrite = dicDados[dado][1]

			if (timestampTransacao < timestampWrite):
				ultimaOperacao = dicDados[dado][2]
				idUltimaOperacao = int(numberRegex.search(ultimaOperacao).group())

				print("\nPROBLEMA ENCONTRADO NA OPERACAO " + operation + ", POIS A TRANSAÇÃO " + str(idUltimaOperacao) + " JA LEU O DADO " + dado)
				print("=" * 101)

				historico.remove(operation)
				ultimaOperacao = dicDados[dado][2]
				indice = historico.index(ultimaOperacao)
				historico.insert(indice, operation)
				
				condicao = True
				print("\nTENTATIVA: ")
				break;
				
			else:
				dicDados[dado][0] = timestampTransacao
				dicDados[dado][2] = operation
				condicao = False			
				
				print(operation + ": ")
				for dados in dicDados:
					print("< " + dados + ", r: " + str(dicDados[dados][0]) + ", w: " + str(dicDados[dados][1]) + ">")

print("\n" + "=" * 101)
print("\nRESULTADO: ")
print(*historico)
print("\n" + "=" * 101)

################################ 2a parte: RECUPERAÇÃO A FALHAS ################################

# No código utilizamos três listas: 	  - iniciadas: Guarda o número(timestamp) das transações que foram inicializadas. Identificamos isso com o "BT" que sinaliza o início da transação.
#										  - commitadas: Guarda o número(timestamp) das transações que foram commitadas. Identificamos isso com o "CM" que sinaliza que a transação foi commitada.
#												 		Quando a transação é commitada, nós retiramos ela da lista de iniciadas e incluimos na lista de commitadas.
#										  - terminadas:	Guarda o número(timestamp) das transações que foram terminadas. Identificamos isso quando há o checkpoint - "CP", então copiamos toda a lista das transações commitadas e inserimos na lista de terminadas, e então zeramos a lista das transações commitadas.
# Também utilizamos um dicionário para guardar as transações e as operações dessa transação. Em cada if identificamos o tipo da operação ('BT', 'W', 'CM', 'CP') e adicionamos
# a transação respectiva (que está como a chave do dicionário).
# Quanfo um transação é iniciada adicionamos a chave de sua transação(corresponde ao seu tempo) no dicionário.
#
# No final imprimos as transações com a lista de suas respectivas operações iniciadas (através do nosso dicionário de transações)
# como correspondente as transações, e respectivamente suas operações, que devem ser desfeitas (UNDO).
# Imprimos também as transações com a lista de suas respectivas operações commitadas (através do nosso dicionário de transações)
# como correspondente as tranções, e respectivamente suas operações, que devem ser REFEITAS (REDO).

iniciadas = []
commitadas = []
terminadas = []

dicTransacoes = {}

print("="*101)

for operation in historico:

	if operation[:2] == 'BT': # início da transação
		idTransacao = numberRegex.search(operation).group()
		dicTransacoes[idTransacao] = []
		iniciadas.append(idTransacao)

	elif operation[:1] == 'w': # operação de escrita
		idTransacao = numberRegex.search(operation).group()
		dicTransacoes[idTransacao].append(operation)

	elif operation[:2] == 'CM': # operação de commit
		idTransacao = numberRegex.search(operation).group()
		iniciadas.remove(idTransacao)
		commitadas.append(idTransacao)
	
	elif operation[:2] == 'CP': # momento de checkpoint
		for k in commitadas:
			terminadas.append(k)
		commitadas = []
	
	elif operation[:2] == 'FL': # momento de falha
		break;

print('UNDO: ')
for k in iniciadas:
	print("TRANSAÇÃO %s: " % k + str(dicTransacoes[k]))

print('\nREDO: ')
for k in commitadas:
	print("TRANSAÇÃO %s: " % k + str(dicTransacoes[k]))

