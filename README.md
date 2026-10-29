# Nevez Campus

Plataforma acadêmica com **loja integrada**, em Java 17, Spring Boot 4, Thymeleaf, Spring Data JPA e Spring Security. O projeto reúne cadastro acadêmico, estoque, produtos, pedidos e marketplace em uma única aplicação web.

## Recursos

- Dashboard administrativo com indicadores, pedidos recentes e produtos em estoque baixo.
- Cadastro e gerenciamento de alunos, professores, cursos, disciplinas e usuários.
- Catálogo de produtos e carrinho de compras por sessão, com validação de estoque.
- Pedidos com itens, total e estados controlados.
- Login com BCrypt, permissões por perfil e proteção CSRF.
- Recuperação de senha por email com token de expiração em 30 minutos.
- Interface customizada e responsiva, com cores violeta e navegação por teclado.
- Testes de integração e CI por GitHub Actions.

## Estrutura

```text
.mvn/                          Maven Wrapper
.github/workflows/             Testes automáticos
src/main/java/br/com/nevez/devops/
  config/                      Configuração e bootstrap opcional
  controller/                  Rotas MVC
  entity/                      Entidades JPA
  exception/                   Regras de erro
  marketplace/                 Carrinho por sessão
  repository/                  Persistência com Spring Data
  security/                    Controle de acesso
  service/                     Regras de negócio
src/main/resources/
  static/                      CSS e JavaScript
  templates/                   Páginas Thymeleaf
src/test/                      Testes e dados de teste isolados
```

## Execução rápida (Windows)

Instale **JDK 17 ou superior**. O Maven Wrapper já faz parte do repositório.

```powershell
git clone https://github.com/Nevez06/DevOps.git
cd DevOps
$env:APP_BOOTSTRAP_ENABLED="true"
$env:APP_ADMIN_PASSWORD="escolha-uma-senha-forte"
.\mvnw.cmd spring-boot:run
```

Abra **http://localhost:8080/login**. O usuário inicial é `admin` e a senha é a definida por `APP_ADMIN_PASSWORD`.

O banco padrão é **H2 local**, salvo em `data/`. As contas só são inicializadas no primeiro início de um banco vazio, quando `APP_BOOTSTRAP_ENABLED=true`. **Desative essa opção após a inicialização**, especialmente fora do ambiente local. As senhas não estão embutidas no aplicativo.

Opcionalmente, configure `APP_PROFESSOR_PASSWORD` e `APP_SECRETARIA_PASSWORD` no primeiro início para criar usuários fictícios com perfis adicionais.

### Linux / macOS

```bash
APP_BOOTSTRAP_ENABLED=true APP_ADMIN_PASSWORD='escolha-uma-senha-forte' ./mvnw spring-boot:run
```

## PostgreSQL (opcional)

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/nevez_campus"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="senha-local"
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=postgres"
```

A alteração de esquema está configurada com `DDL_AUTO=update` para desenvolvimento. Para produção, implemente migrações versionadas e revise as demais configurações de segurança antes de publicar.

## Email e recuperação de senha

Defina `MAIL_USERNAME`, `MAIL_PASSWORD` e `APP_BASE_URL` no ambiente. O sistema utiliza SMTP (Gmail, porta 587). Para Gmail, utilize uma senha de aplicativo, nunca a senha principal da conta.

Sem SMTP configurado, o envio não ocorre. Os tokens de redefinição **não são exibidos no console**. O link enviado expira em 30 minutos.

## Testes

```powershell
.\mvnw.cmd test
```

As contas e senhas previsíveis existentes em `src/test/resources/application.properties` são **apenas para testes automatizados**; não são usadas pelo aplicativo normal. O GitHub Actions executa a suíte em alterações na branch principal e pull requests.

## Segurança e dados

- Não envie senhas ou arquivos `.env` para o Git.
- Evite ativar o bootstrap fora do ambiente de desenvolvimento.
- O banco H2 persistido e logs são ignorados pelo Git.
- Revise configurações de SMTP, banco e autorização antes de hospedar a aplicação publicamente.

## Crédito e evolução

A base funcional foi **adaptada de [Adryan-Francisco/DevOps](https://github.com/Adryan-Francisco/DevOps)**. A versão Nevez Campus reorganiza a estrutura do repositório, personaliza a interface, altera configurações de inicialização e recuperação de senha, e acrescenta documentação e integração contínua. Os autores originais mantêm a autoria das contribuições aproveitadas. O projeto de referência não apresentava uma licença explícita na consulta realizada: confirme a permissão de redistribuição do código com o autor.
