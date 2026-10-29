# Arquitetura — Nevez Campus

## Camadas

1. **Controller:** recebe requisições web e retorna páginas do Thymeleaf.
2. **Service:** concentra regras de negócio, validações e transações.
3. **Repository:** abstrai consultas e persistência com Spring Data JPA.
4. **Entity:** mapeia os dados do domínio acadêmico e da loja.
5. **Security:** define as rotas permitidas e os perfis autorizados.
6. **Marketplace:** mantém um carrinho independente por sessão HTTP.

## Áreas de acesso

- `/login`, `/recuperar-senha`, `/redefinir-senha`: autenticação e recuperação.
- `/home`, `/aluno/**`, `/curso/**`, `/professor/**`, `/disciplina/**`: administração.
- `/produto/**`, `/pedido/**`, `/item-pedido/**`, `/usuario/**`: administração.
- `/loja/**`, `/carrinho/**`: área de compras para usuários autenticados.

## Rotina de qualidade

`./mvnw test` deve passar antes de fazer merge. Não copie o banco local, segredos ou registros da aplicação para o repositório.

## Trabalhos futuros

- Auditoria de autenticação e eventos administrativos.
- Migrações de banco (Flyway/Liquibase) antes de produção.
- Testes de autorização por perfil e validação de pedidos concorrentes.
- Substituir a persistência de fotos no banco por serviço de arquivos caso cresça o volume.
