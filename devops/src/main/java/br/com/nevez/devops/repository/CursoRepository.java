package br.com.nevez.devops.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.nevez.devops.entity.Curso;
import java.util.List;

public interface CursoRepository extends JpaRepository<Curso, Integer> {

    List<Curso> findAllByOrderByNomeCurso();
}
