package qiwi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.support.SessionStatus;
import qiwi.model.Bank;
import qiwi.model.Client;
import qiwi.model.CreditOffer;
import qiwi.repository.BankRepository;
import qiwi.util.CreditOfferValidator;
import qiwi.util.StringUtil;

import java.util.List;
import java.util.UUID;

@Service
public class BankService {
    @Autowired
    private BankRepository repository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private CreditOfferService creditOfferService;

    private void setUpModel(Model model) {
        model.addAttribute("banks", findAll());
        model.addAttribute("stringUtil", new StringUtil());
    }

    public void add(Bank bank) {
        repository.save(bank);
    }

    public boolean add(Bank bank, BindingResult result, Model model) {
        if (result.hasErrors()) {
            setUpModel(model);
            return false;
        }

        if (existsByName(bank.getName())) {
            setUpModel(model);
            result.reject("alreadyExists", "This bank already exists.");
            return false;
        }

        repository.save(bank);

        return true;
    }

    public boolean edit(Bank bank, BindingResult result) {
        if (result.hasErrors()) {
            return false;
        }

        if (existsByName(bank.getName())) {
            result.reject("alreadyExists", "This bank already exists.");
            return false;
        }

        update(getBankById(bank.getId()), bank);

        return true;
    }

    public boolean addClient(Client client, BindingResult result,
                            UUID id, SessionStatus status) {
        if (result.hasErrors()) {
            return false;
        }

        if (clientService.existsByPassport(client.getPassport())) {
            Bank bank = getBankById(id);

            if (existsClientByPassport(bank.getName(), client.getPassport())) {
                result.reject("alreadyExists", "This person is already a client of this bank.");
                return false;
            }

            bank.addClient(clientService.getClientByPassport(client.getPassport()));
            repository.save(bank);
        } else {
            result.reject("nonExistentClient", "This person doesn't exist.");
            return false;
        }

        status.setComplete();

        return true;
    }

    public boolean addCreditOffer(CreditOffer creditOffer, BindingResult result,
                                 Integer months, UUID id, String passport,
                                 Model model, SessionStatus status) {

        CreditOfferValidator validator = new CreditOfferValidator();
        validator.validate(creditOffer, result, months);

        if (result.hasErrors()) {
            setUpModel(model);
            model.addAttribute("bank", getBankById(id));

            return false;
        }

        creditOffer.calculatePayments(months);

        creditOffer.setClient(clientService.getClientByPassport(passport));
        creditOffer.setBank(getBankById(id));

        if (creditOfferService.exists(creditOffer)) {
            setUpModel(model);
            model.addAttribute("bank", getBankById(id));

            result.reject("alreadyExists", "This credit offer already exists.");

            return false;
        }

        creditOfferService.add(creditOffer);

        status.setComplete();

        return true;
    }

    public void deleteById(UUID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
    }

    public void deleteClientById(UUID bankId, UUID id) {
        Bank bank = getBankById(bankId);

        for (Client client : bank.getClients()) {
            if (client.getId().equals(id)) {
                bank.getCreditOffers().removeIf(creditOffer -> creditOffer.getClient().equals(client));
                bank.deleteClient(client);
                repository.save(bank);
                return;
            }
        }
    }

    public void showAllBanks(Model model) {
        setUpModel(model);
        model.addAttribute("bank", new Bank());
    }

    public Bank getBankByName(String name) {
        return repository.getBankByName(name);
    }

    public Bank getBankById(UUID id) {
        return repository.getBankById(id);
    }

    public boolean existsClientByPassport(String bankName, String passport) {
        for (Client client : getBankByName(bankName).getClients()) {
            if (client.getPassport().equals(passport)) {
                return true;
            }
        }

        return false;
    }

    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }

    public void update(Bank oldBank, Bank newBank) {
        if (getBankById(newBank.getId()) != null) {
            oldBank.setName(newBank.getName());
        }

        repository.save(oldBank);
    }

    public List<Bank> findAll() {
        return repository.findAll();
    }
}
