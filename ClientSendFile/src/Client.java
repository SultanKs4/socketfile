import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private JTextField portClient;
    private JTextField ipClient;
    private JButton btnOpen;
    private JTextField txtPath;
    private JButton btnKirim;
    private JPanel Client;
    private String isi = null;

    public Client() {
        btnOpen.addActionListener(e -> {
            try {
                isi = textFile();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        btnKirim.addActionListener(e -> {
            String host = ipClient.getText();
            int port = Integer.parseInt(portClient.getText());
            if (host != null && port != 0 && isi != null) {
                sendToServer(host, port, isi);
            }
        });
    }

    private String textFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.txt", "txt");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            txtPath.setText(selectedFile.getAbsolutePath());
            return readFile(selectedFile);
        } else if (result == JFileChooser.CANCEL_OPTION) {
            txtPath.setText("");
        }
        return null;
    }

    private String readFile(File selectedFile) {
        StringBuilder textTmp = new StringBuilder();
        try {
            Scanner input = new Scanner(selectedFile);
            while (input.hasNext()) {
                textTmp.append(input.nextLine());
                textTmp.append("\n");
            }
            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return textTmp.toString();
    }

    private void sendToServer(String host, int port, final String message) {
        try {
            Socket socket = new Socket(host, port);

            if (socket.isBound()) {
                System.out.println("Connected to Server IP: " + host + " Port: " + port);
            }

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(message);
            out.flush();
            socket.close();
            if (socket.isClosed()) {
                System.out.println("Disconnected from Server");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        JFrame gui = new JFrame("Socket Client Send File");
        gui.setContentPane(new Client().Client);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setLocationRelativeTo(null);
        gui.pack();
        gui.setVisible(true);
    }
}
