import java.io.*;
import java.net.Socket;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;

class Client extends Thread
{
    protected static Socket clientSocket; //сокет для общения
    private BufferedReader reader; // серверсокет
    protected BufferedReader in; // поток чтения из сокета
    protected BufferedWriter out;
    private static JFrame window;
    private JTextArea ta;

    Client()
    {
        try
        {
            clientSocket = new Socket("192.168.1.79", 8080); // этой строкой мы запрашиваем
            //  у сервера доступ на соединение
            reader = new BufferedReader(new InputStreamReader(System.in));
            // читать соообщения с сервера
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            // писать туда же
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            System.out.println(out);
            start();
        }
        catch (IOException error)
        {

        }

        createWindow();
    }
    public static void main(String[] args)
    {
        Client me = new Client();
    }

    public void run()
    {
        while(true)
        {
            try
            {
                String messageServer = in.readLine();
                ta.append(messageServer + '\n');
            }
            catch (IOException error)
            {

            }
        }
    }

    public void createWindow()
    {
              
        JFrame frame = new JFrame("Chat Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        JMenuBar ob = new JMenuBar();
        JMenu ob1 = new JMenu("FILE");
        JMenu ob2 = new JMenu("Help");
        ob.add(ob1);
        ob.add(ob2);
        JMenuItem m11 = new JMenuItem("Open");
        JMenuItem m22 = new JMenuItem("Save as");
        ob1.add(m11);
        ob1.add(m22);
        
        JPanel panel = new JPanel(); // the panel is not visible in output
        JLabel label = new JLabel("Enter Text");
        JTextField tf = new JTextField(20); // accepts upto 10 characters

        tf.addKeyListener(new KeyAdapter() {
     
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10)
                {
                    try
                    {
                        System.out.println(out);
                        out.write(tf.getText() + '\n');
                        out.flush();
                    }
                    catch (IOException error)
                    {
                        System.out.println(error);
                    }
                    tf.setText("");

                }
            }
                     
        });

        JButton send = new JButton("Send");
        panel.add(label); // Components Added using Flow Layout
        panel.add(label); // Components Added using Flow Layout
        panel.add(tf);
        panel.add(send);
        ta = new JTextArea();
        ta.setEditable(false);
        ta.setLineWrap(true);
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.CENTER, ta);
        frame.setVisible(true);
    }
}
