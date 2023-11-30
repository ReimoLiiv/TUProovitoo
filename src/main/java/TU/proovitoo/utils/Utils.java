package TU.proovitoo.utils;

import TU.proovitoo.model.Client;
import TU.proovitoo.controller.MainPageController.OperationType;
import org.zkoss.zul.*;

import java.util.List;

public class Utils {
    public static void loadClients(Listbox clientsListbox, List<Client> clients, Button delete, Button modify) {
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

    public static void showSuccessMessage(OperationType operationType) {
        String message;
        switch (operationType) {
            case ADD:
                message = "Client successfully added!";
                break;
            case DELETE:
                message = "Client successfully deleted!";
                break;
            case MODIFY:
                message = "Client details successfully modified!";
                break;
            default:
                throw new IllegalArgumentException("Unknown operation type");
        }

        Messagebox.show(message, "Success", Messagebox.OK, Messagebox.INFORMATION);
    }
}
