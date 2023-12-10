package ma.projet.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ma.projet.demo.entities.Employe;
import ma.projet.demo.entities.Machine;
import ma.projet.demo.repositories.EmployeRepository;
import ma.projet.demo.repositories.MachineRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/machines")
public class MachineController {
    @Autowired
    private MachineRepository machineRepository;
    @Autowired
    private EmployeRepository employeRepository;

    @GetMapping
    public String listMachines(Model model) {
        List<Machine> machines = machineRepository.findAll();
        model.addAttribute("machines", machines);
        model.addAttribute("employes", employeRepository.findAll());

        return "machine/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("machine", new Machine());
        model.addAttribute("employes", employeRepository.findAll());
        return "machine/create";
    }

    @PostMapping("/create")
    public String createMachine(@ModelAttribute Machine machine) {
        machineRepository.save(machine);
        return "redirect:/machines";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Machine machine = machineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid machine Id: " + id));

        model.addAttribute("machine", machine);
        model.addAttribute("employes", employeRepository.findAll());
        return "machine/edit";
    }

    @PostMapping("/update/{id}")
    public String updateMachine(@PathVariable String id, @ModelAttribute Machine machine) {
        // Convert the id to Long
        Long machineId = Long.parseLong(id);

        // Retrieve the existing machine from the repository
        Optional<Machine> existingMachineOptional = machineRepository.findById(machineId);

        Machine existingMachine = existingMachineOptional.get();

        // Update the properties of the existing machine
        existingMachine.setReference(machine.getReference());
        existingMachine.setMarque(machine.getMarque());
        existingMachine.setDateAchat(machine.getDateAchat());
        existingMachine.setPrix(machine.getPrix());
        existingMachine.setEmploye(machine.getEmploye());

        // Save the updated machine back to the repository
        machineRepository.save(existingMachine);

        // Redirect to the list of machines after successful update
        return "redirect:/machines";

    }

    @GetMapping("/delete/{id}")
    public String deleteMachine(@PathVariable long id) {
        machineRepository.deleteById(id);
        return "redirect:/machines";
    }

    @GetMapping("/filtres")
    public String listMachines(
            @RequestParam(name = "employeId", required = false) Long employeId,
            @RequestParam(name = "sortByDate", defaultValue = "false") boolean sortByDate,
            Model model) {

        List<Machine> machines;

        if (employeId != null) {
            Optional<Employe> employeOptional = employeRepository.findById(employeId);
            if (employeOptional.isPresent()) {
                Employe employe = employeOptional.get();
                machines = machineRepository.findByEmploye(employe);
            } else {
                machines = new ArrayList<>();
            }
        } else {
            machines = machineRepository.findAll();
        }

        if (sortByDate) {
            // Use the Comparator.reverseOrder() to sort in descending order
            machines.sort(Comparator.comparing(Machine::getDateAchat, Comparator.reverseOrder()));
        }

        model.addAttribute("machines", machines);
        model.addAttribute("employes", employeRepository.findAll());
        return "machine/list";
    }

    @GetMapping("/sortByDate")
    public String sortByDate(
            @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder,
            Model model) {

        List<Machine> machines = machineRepository.findAll();

        Comparator<Machine> dateComparator = Comparator.comparing(Machine::getDateAchat);

        if ("desc".equalsIgnoreCase(sortOrder)) {
            // Tri décroissant
            dateComparator = dateComparator.reversed();
        }

        machines.sort(dateComparator);

        model.addAttribute("machines", machines);
        model.addAttribute("employes", employeRepository.findAll());
        return "machine/list";
    }

    @GetMapping("/machinesByEmploye")
    public String machinesByEmploye(@RequestParam(name = "employeId", required = false) Long employeId, Model model) {
        List<Employe> employees = employeRepository.findAll(); // Retrieve the list of employees
        model.addAttribute("employees", employees);
        Optional<Employe> employeOptional = employeRepository.findById(employeId);

        if (employeOptional.isPresent()) {
            Employe employe = employeOptional.get();
            List<Machine> machines = machineRepository.findByEmploye(employe);

            model.addAttribute("employe", employe);
            model.addAttribute("machines", machines);

            return "machine/machinesByEmploye"; // Assuming you have a Thymeleaf template named "machinesByEmploye.html"
        } else {
            // Handle the case where the employee is not found
            return "redirect:/machines"; // Redirect to machine list page or handle accordingly
        }
    }

 

}
