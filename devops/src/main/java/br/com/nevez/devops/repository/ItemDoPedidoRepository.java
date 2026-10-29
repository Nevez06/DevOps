package br.com.nevez.devops.repository;

import br.com.nevez.devops.entity.ItemDoPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemDoPedidoRepository extends JpaRepository<ItemDoPedido, Integer> {

    boolean existsByProdutoIdProduto(Integer idProduto);
}
