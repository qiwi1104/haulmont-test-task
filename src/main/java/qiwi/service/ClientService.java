package qiwi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import qiwi.model.Bank;
import qiwi.model.Client;
import qiwi.repository.ClientRepository;
import qiwi.util.ClientValidator;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Service
public class ClientService {
    @Autowired
    private ClientRepository repository;

    public void add(Client client) {
        repository.save(client);
    }

    public boolean add(Client client, BindingResult result, Model model) {
        ClientValidator validator = new ClientValidator();
        validator.validate(client, result);

        if (result.hasErrors()) {
            model.addAttribute("clients", findAll());
            return false;
        }

        if (existsByPassport(client.getPassport())) {
            model.addAttribute("clients", findAll());
            result.reject("alreadyExists", "This person already exists.");
            return false;
        }

        for (Client c : findAll()) {
            if (c.equalsPhone(client.getPhone()) || c.getMail().equals(client.getMail())) {

                if (c.equalsPhone(client.getPhone())) {
                    result.reject("phoneOccupied", "This phone is occupied.");
                }

                if (c.getMail().equals(client.getMail())) {
                    result.reject("mailOccupied", "This mail is occupied.");
                }

                model.addAttribute("clients", findAll());
                return false;
            }
        }

        repository.save(client);

        return true;
    }

    public boolean edit(Client client, BindingResult result, Model model) {
        ClientValidator validator = new ClientValidator();
        validator.validate(client, result);

        if (result.hasErrors()) {
            model.addAttribute("clients", findAll());
            return false;
        }

        if (existsByPassport(client.getPassport())) {
            if (getClientByPassport(client.getPassport()).getId().compareTo(client.getId()) != 0) {
                model.addAttribute("clients", findAll());
                result.reject("alreadyExists", "This person already exists.");
                return false;
            }
        }

        for (Client c : findAll()) {
            if (c.getId().compareTo(client.getId()) != 0) {
                if (c.equalsPhone(client.getPhone()) || c.getMail().equals(client.getMail())) {
                    if (c.equalsPhone(client.getPhone())) {
                        result.reject("phoneOccupied", "This phone is occupied.");
                    }

                    if (c.getMail().equals(client.getMail())) {
                        result.reject("mailOccupied", "This mail is occupied.");
                    }

                    model.addAttribute("clients", findAll());
                    return false;
                }
            }
        }

        update(getClientById(client.getId()), client);

        return true;
    }

    public void deleteById(UUID id) {
        if (repository.existsById(id)) {
            Iterator<Bank> iterator = repository.getOne(id).getBanks().iterator();
            while (iterator.hasNext()) {
                iterator.next().deleteClient(repository.getOne(id));
                iterator.remove();
            }

            repository.deleteById(id);
        }
    }

    public void showAllClients(Model model) {
        model.addAttribute("clients", findAll());
        model.addAttribute("client", new Client());
    }

    public Client getClientByPassport(String passport) {
        return repository.getClientByPassport(passport);
    }

    public Client getClientById(UUID id) {
        return repository.getClientById(id);
    }

    public boolean existsByPassport(String passport) {
        return repository.existsByPassport(passport);
    }

    public void update(Client oldClient, Client newClient) {
        oldClient = getClientById(oldClient.getId());

        oldClient.setFirstName(newClient.getFirstName());
        oldClient.setMiddleName(newClient.getMiddleName());
        oldClient.setLastName(newClient.getLastName());
        oldClient.setPhone(newClient.getPhone());
        oldClient.setMail(newClient.getMail());
        oldClient.setPassport(newClient.getPassport());

        repository.save(oldClient);
    }

    public List<Client> findAll() {
        return repository.findAll();
    }
}
