package TU.proovitoo.service;

import TU.proovitoo.model.Client;
import TU.proovitoo.model.Country;
import TU.proovitoo.repository.ClientRepository;
import TU.proovitoo.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CountryRepository countryRepository;


    public List<Client> getClientsByUserId(Long userId) {
        return clientRepository.findByUserId(userId);
    }
    public boolean deleteClient(Long clientId) {
        try {
            clientRepository.deleteById(clientId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean addOrModify(Client client) {
        try {
            clientRepository.save(client);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }
}