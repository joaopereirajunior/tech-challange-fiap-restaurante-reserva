# language: pt
Funcionalidade: API - Cadastro de Clientes

Cenário: Registrar um novo cliente
  Quando submeter um novo cliente
  Então o cliente é registrado com sucesso

Cenário: Buscar cliente existente
  Dado que um cliente já foi registrado
  Quando requisitar a busca do cliente
  Então o cliente é exibido com sucesso
  
Cenário: Listar clientes existentes
  Dado que um cliente já foi registrado
  Quando requisitar a lista de clientes
  Então os clientes são exibidos com sucesso

Cenário: Alterar um cliente existente
  Dado que um cliente já foi registrado
  Quando requisitar a alteração do cliente
  Então o cliente é atualizado com sucesso

Cenário: Excluir um cliente existente
  Dado que um cliente já foi registrado
  Quando requisitar a exclusão do cliente
  Então o cliente é removido com sucesso

Cenário: Registrar um novo restaurante
  Quando submeter um novo restaurante
  Então o restaurante é registrado com sucesso

Cenário: Buscar restaurante existente
  Dado que um restaurante já foi registrado
  Quando requisitar a busca do restaurante
  Então o restaurante é exibido com sucesso
  
Cenário: Listar restaurantes existentes
  Dado que um restaurante já foi registrado
  Quando requisitar a lista de restaurantes
  Então os restaurantes são exibidos com sucesso

Cenário: Alterar um restaurante existente
  Dado que um restaurante já foi registrado
  Quando requisitar a alteração do restaurante
  Então o restaurante é atualizado com sucesso

Cenário: Excluir um restaurante existente
  Dado que um restaurante já foi registrado
  Quando requisitar a exclusão do restaurante
  Então o restaurante é removido com sucesso

Cenário: Registrar uma nova reserva
  Quando submeter uma nova reserva
  Então a reserva é registrada com sucesso

Cenário: Buscar reserva existente
  Dado que uma reserva já foi registrada
  Quando requisitar a busca da reserva
  Então a reserva é exibida com sucesso
  
Cenário: Listar reservas existentes
  Dado que uma reserva já foi registrada
  Quando requisitar a lista de reservas
  Então as reservas são exibidas com sucesso

Cenário: Alterar uma reserva existente
  Dado que uma reserva já foi registrada
  Quando requisitar a alteração da reserva
  Então a reserva é atualizada com sucesso

Cenário: Excluir uma reserva existente
  Dado que uma reserva já foi registrada
  Quando requisitar a exclusão da reserva
  Então a reserva é removida com sucesso

  