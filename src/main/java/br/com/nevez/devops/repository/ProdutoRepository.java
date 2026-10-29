package br.com.nevez.devops.repository;

import br.com.nevez.devops.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    List<Produto> findAllByOrderByNomeProduto();

    List<Produto> findByQuantidadeEstoqueGreaterThanOrderByNomeProduto(Integer quantidade);

    List<Produto> findByQuantidadeEstoqueLessThanEqualOrderByQuantidadeEstoque(Integer limite);
}
