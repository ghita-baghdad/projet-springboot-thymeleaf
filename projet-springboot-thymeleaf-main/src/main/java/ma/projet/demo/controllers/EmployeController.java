package ma.projet.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ma.projet.demo.entities.Employe;
import ma.projet.demo.repositories.EmployeRepository;

@Controller
@RequestMapping("/employes")
public class EmployeController {

    @Autowired
    private EmployeRepository employeRepository;

    @GetMapping
    public String getAllEmployes(Model model) {
        model.addAttribute("employes", employeRepository.findAll());
        return "employe/list";
    }

    @GetMapping("/create")
    public String createEmployeForm(Model model) {
        model.addAttribute("employe", new Employe());
        return "employe/create";
    }

    @PostMapping("/create")
    public String createEmploye(@ModelAttribute Employe employe) {
        employeRepository.save(employe);
        return "redirect:/employes";
    }

    @GetMapping("/edit/{id}")
    public String editEmployeForm(@PathVariable long id, Model model) {
        Employe employe = employeRepository.findById(id).orElse(null);
        if (employe != null) {
            model.addAttribute("employe", employe);
            return "employe/edit";
        } else {
            return "redirect:/employes";
        }
    }

    @PostMapping("/edit/{id}")
    public String editEmploye(@PathVariable long id, @ModelAttribute Employe updatedEmploye) {
        Employe employe = employeRepository.findById(id).orElse(null);
        if (employe != null) {
            employe.setNom(updatedEmploye.getNom());
            employe.setPrenom(updatedEmploye.getPrenom());
            employe.setSalaire(updatedEmploye.getSalaire());
            // Set other attributes as needed
            employeRepository.save(employe);
        }
        return "redirect:/employes";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmploye(@PathVariable long id) {
        employeRepository.deleteById(id);
        return "redirect:/employes";
    }
}
