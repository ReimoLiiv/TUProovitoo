package TU.proovitoo.service;

import TU.proovitoo.model.Client;
import TU.proovitoo.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<Client> getClientsByUserId(Long userId) {
        return clientRepository.findByUserId(userId);
    }
}