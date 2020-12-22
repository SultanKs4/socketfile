import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class Client {
    private JTextField portClient;
    private JTextField ipClient;
    private JButton btnOpen;
    private JTextField txtPath;
    private JButton btnKirim;
    private JPanel Client;
    private String isi = "";

    public Client(){
        btnOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileChooser fc = new FileChooser();

                try {
                    fc.Open();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                isi = fc.sb.toString();
            }
        });
    }

    public static void main(String[] args){
        JFrame gui = new JFrame("Socket Client Send File");
        gui.setContentPane(new Client().Client);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setLocationRelativeTo(null);
        gui.pack();
        gui.setVisible(true);
    }

    public class FileChooser {

        JFileChooser fileChooser = new JFileChooser();
        StringBuilder sb = new StringBuilder();

        public void Open() throws Exception{
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
                //get file
                java.io.File file = fileChooser.getSelectedFile();
                txtPath.setText(file.getAbsolutePath());

                //create scanner for the file
                Scanner input = new Scanner(file);

                //read text from file
                while (input.hasNext()){
                    sb.append(input.nextLine());
                    sb.append("\n");
                }
                input.close();
            } else {
                sb.append("No File was selected");
            }
        }
    }

}
