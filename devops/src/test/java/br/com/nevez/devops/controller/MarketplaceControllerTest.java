package br.com.nevez.devops.controller;

import java.math.BigDecimal;
import java.util.Optional;

import br.com.nevez.devops.entity.Produto;
import br.com.nevez.devops.marketplace.CarrinhoCompra;
import br.com.nevez.devops.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MarketplaceControllerTest {

    private ProdutoService produtoService;
    private CarrinhoCompra carrinho;
    private MarketplaceController controller;

    @BeforeEach
    void configurarCarrinho() {
        produtoService = mock(ProdutoService.class);
        carrinho = new CarrinhoCompra();
        controller = new MarketplaceController(produtoService, carrinho);
    }

    @Test
    void retornaTotalZeroQuandoCarrinhoEstaVazio() {
        assertEquals(BigDecimal.ZERO, calcularTotal());
    }

    @Test
    void somaSubtotaisDeProdutosComPrecosEQuantidadesDiferentes() {
        adicionarProduto(1, "19.90", 2);
        adicionarProduto(2, "5.25", 3);

        assertEquals(new BigDecimal("55.55"), calcularTotal());
    }

    @Test
    void recalculaTotalAposMudarQuantidade() {
        adicionarProduto(1, "19.90", 2);
        adicionarProduto(2, "5.25", 3);

        carrinho.atualizar(1, 4, 20);

        assertEquals(new BigDecimal("95.35"), calcularTotal());
    }

    @Test
    void excluiSubtotalDoProdutoRemovidoDoTotal() {
        adicionarProduto(1, "19.90", 4);
        adicionarProduto(2, "5.25", 3);

        carrinho.remover(1);

        assertEquals(new BigDecimal("15.75"), calcularTotal());
    }

    private void adicionarProduto(int id, String preco, int quantidade) {
        Produto produto = new Produto();
        produto.setPrecoProduto(new BigDecimal(preco));
        when(produtoService.buscarPorId(id)).thenReturn(Optional.of(produto));
        carrinho.adicionar(id, quantidade, 20);
    }

    private Object calcularTotal() {
        ExtendedModelMap model = new ExtendedModelMap();
        controller.carrinho(model);
        return model.getAttribute("totalCarrinho");
    }
}
