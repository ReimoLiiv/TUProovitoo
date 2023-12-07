package TU.proovitoo.utils;

import TU.proovitoo.model.Client;
import TU.proovitoo.controller.MainPageController.OperationType;
import org.zkoss.zul.*;

import java.util.List;

public class Utils {


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
