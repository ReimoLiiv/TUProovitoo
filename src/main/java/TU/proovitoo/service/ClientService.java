package TU.proovitoo.service;

import TU.proovitoo.model.Client;
import TU.proovitoo.repository.ClientRepository;
import TU.proovitoo.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public void loadClients(Listbox clientsListbox, List<Client> clients, Button delete, Button modify) {
        clientsListbox.getItems().clear();
        for (Client client : clients) {
            Listitem listItem = new Listitem();
            listItem.appendChild(new Listcell(client.getFirstName()));
            listItem.appendChild(new Listcell(client.getLastName()));
            listItem.appendChild(new Listcell(client.getUsername()));
            listItem.appendChild(new Listcell(client.getEmail()));
            listItem.appendChild(new Listcell(client.getAddress()));
            listItem.appendChild(new Listcell(client.getCountry()));
            listItem.setValue(client);
            clientsListbox.appendChild(listItem);
            delete.setDisabled(true);
            modify.setDisabled(true);
        }
    }
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
        if (!isValidClient(client)) {
            return false;
        }
        try {
            clientRepository.save(client);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidClient(Client client) {
        return isValidName(client.getFirstName()) &&
                isValidName(client.getLastName()) &&
                isValidUsername(client.getUsername()) &&
                (client.getEmail() != null || client.getEmail().matches("^[a-zA-Z]+@[a-zA-Z]+\\.[a-zA-Z]{2,}$")) &&
                isValidAddress(client.getAddress()) &&
                client.getCountry() != null;
    }

    private boolean isValidName(String name) {
        return name != null && name.trim().length() >= 2 && name.matches("[a-zA-ZäöüõÄÖÜÕ]+");
    }

    private boolean isValidUsername(String username) {
        return username != null && username.trim().length() >= 5;
    }

    private boolean isValidAddress(String address) {
        return address != null && address.trim().length() > 0;
    }
}