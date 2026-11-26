package br.com.nevez.devops.marketplace;

import java.math.BigDecimal;

import br.com.nevez.devops.entity.Produto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemCarrinhoTest {

    @Test
    void calculaSubtotalMultiplicandoPrecoPelaQuantidade() {
        Produto produto = new Produto();
        produto.setPrecoProduto(new BigDecimal("19.90"));

        ItemCarrinho item = ItemCarrinho.de(produto, 2);

        assertEquals(new BigDecimal("39.80"), item.subtotal());
    }
}
