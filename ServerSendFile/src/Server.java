import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {
    private JTextField portServer;
    private JTextField ipServer;
    private JTextField txtIsi;
    private JPanel panelServer;
    private JButton btnListen;
    private ServerSocket serverSocket;
    private Socket clientSocket;

    public Server() {
        portServer.setText("5050");
        try {
            ipServer.setText(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        btnListen.addActionListener(e -> {
            try {
                new Thread(this::socketServer).start();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });
    }

    private void socketServer() {
        try {
            int port = Integer.parseInt(portServer.getText());
            InetAddress ip = InetAddress.getByName(ipServer.getText());
            setServerSocket(new ServerSocket(port, 1, ip));
            JOptionPane.showMessageDialog(null, "Server listening");
            btnListen.setEnabled(false);
            portServer.setEnabled(false);
            ipServer.setEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        if (getServerSocket() != null) {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    communication(getServerSocket().accept());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void communication(Socket socket) {
        setClientSocket(socket);
        BufferedReader input = null;
        try {
            input = new BufferedReader(new InputStreamReader(getClientSocket().getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed connect to client: " + e.getMessage());
        }
        System.out.println("Connected to client!");

        while (!Thread.currentThread().isInterrupted()) {
            try {
                assert input != null;
                String read = input.readLine();
                if (read == null) {
                    Thread.interrupted();
                    read = "Offline...";
                    System.out.println("Client: " + read);
                    break;
                }
                txtIsi.setText(read);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        JFrame gui = new JFrame("Socket Server Send File");
        gui.setContentPane(new Server().panelServer);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setLocationRelativeTo(null);
        gui.pack();
        gui.setVisible(true);
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
}
