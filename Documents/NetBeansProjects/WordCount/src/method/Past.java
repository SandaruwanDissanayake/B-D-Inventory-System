package method;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class Past {

    public String past() {

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents(this);
        if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                String pastedText = (String) contents.getTransferData(DataFlavor.stringFlavor);
//                jTextArea1.append(pastedText);
                return pastedText;
            } catch (UnsupportedFlavorException | IOException ex) {
                ex.printStackTrace();

            }
        }

        return null;
    }
}
