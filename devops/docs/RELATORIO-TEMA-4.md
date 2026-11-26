# Tema 4 — Testes unitários do cálculo do carrinho

Nevez Campus · Integração e Entrega Contínua (DevOps) · 26 de novembro de 2026

## Conceito e objetivo

Um teste unitário executa uma parte do programa em condições controladas e compara o resultado com um valor conhecido. No carrinho, o subtotal de cada produto é o preço unitário multiplicado pela quantidade. O total é a soma desses subtotais. Testar essas operações ajuda a identificar regressões que poderiam fazer a loja exibir valores incorretos.

## Ferramentas e implementação

- **JUnit Jupiter** executa os testes e compara os valores monetários com `assertEquals`. Os preços são representados com `BigDecimal`, a mesma classe usada na aplicação. [Guia do JUnit 5](https://docs.junit.org/5.13.4/user-guide/)
- **Mockito 5.20.0** simula somente a busca de produtos no `ProdutoService`. Os testes exercitam as classes reais do carrinho e do controlador, sem iniciar o servidor ou acessar o banco.
- [ItemCarrinhoTest](../src/test/java/br/com/nevez/devops/marketplace/ItemCarrinhoTest.java) verifica a multiplicação do preço pela quantidade. [MarketplaceControllerTest](../src/test/java/br/com/nevez/devops/controller/MarketplaceControllerTest.java) verifica o total que o controlador envia à página do carrinho.

## Procedimento e resultados

A partir da raiz do repositório, execute no PowerShell com JDK 17 ou posterior. O primeiro comando Maven roda apenas os testes do Tema 4 com JUnit 5, sugerido no enunciado. O segundo usa o JUnit 6.0.3 padrão do projeto, exigido pela extensão do Spring Framework 7 usada nos testes de integração. [Documentação do Spring Framework](https://docs.spring.io/spring/reference/7.0/testing/testcontext-framework/support-classes.html#springextension)

```powershell
cd devops
.\mvnw.cmd -B -ntp '-Djunit-jupiter.version=5.13.4' '-Dtest=ItemCarrinhoTest,MarketplaceControllerTest' test
.\mvnw.cmd -B -ntp package
```

| Cenário | Valor esperado | Valor obtido |
| --- | ---: | ---: |
| Subtotal de 2 cadernos a R$ 19,90 | R$ 39,80 | R$ 39,80 |
| Carrinho vazio | R$ 0,00 | R$ 0,00 |
| Total de 2 cadernos a R$ 19,90 e 3 canetas a R$ 5,25 | R$ 55,55 | R$ 55,55 |
| Total após aumentar a quantidade de cadernos para 4 | R$ 95,35 | R$ 95,35 |
| Total após remover os cadernos e manter as 3 canetas | R$ 15,75 | R$ 15,75 |

### Saída do Maven

```text
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS

[INFO] Tests run: 19, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

O Maven também gera os resultados por classe em `devops/target/surefire-reports/`. Na aplicação executada localmente com H2 em memória, a página do carrinho exibiu os totais de R$ 55,55, R$ 95,35 e R$ 15,75 nos mesmos cenários de inclusão, atualização e remoção.

Em uma cópia temporária, retirar a multiplicação pela quantidade fez o teste de subtotal falhar com R$ 19,90 no lugar de R$ 39,80. Substituir a soma por subtração fez o teste do total falhar com −R$ 55,55 no lugar de R$ 55,55.
