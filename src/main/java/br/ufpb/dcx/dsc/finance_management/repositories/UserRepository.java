package br.ufpb.dcx.dsc.finance_management.repositories;

import br.ufpb.dcx.dsc.finance_management.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
