package br.ufpb.dcx.dsc.finance_management.repositories;

import br.ufpb.dcx.dsc.finance_management.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserId(Long userId);
    Optional<Category> findByName(String name);
}
