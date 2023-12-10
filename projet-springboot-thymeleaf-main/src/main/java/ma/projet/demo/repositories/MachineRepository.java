package ma.projet.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.projet.demo.entities.Employe;
import ma.projet.demo.entities.Machine;

public interface MachineRepository extends JpaRepository<Machine,Long> {
List<Machine> findByEmploye(Employe employe);

}
