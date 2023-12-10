package ma.projet.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.projet.demo.entities.Employe;

public interface EmployeRepository extends JpaRepository<Employe,Long> {
    
}
