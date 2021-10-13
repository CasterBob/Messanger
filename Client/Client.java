import java.io.*;
import java.net.Socket;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;

class Client extends Thread
{
    protected static Socket clientSocket; //сокет для общения
    private BufferedReader reader; // серверсокет
    protected BufferedReader in; // поток чтения из сокета
    protected BufferedWriter out;
    private static JFrame window;
    //private JTextArea ta;
    private JTextArea ta;
    private JPanel panel;
    private static JFrame frame;

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

        createStartWindow();
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
                //ta.append(messageServer + '\n');
            }
            catch (IOException error)
            {

            }
        }
    }

    public void createStartWindow()
    {
        System.setProperty("sun.java2d.uiScale", "1");
        frame = new JFrame("Chat Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 800);
        panel = new JPanel();// the panel is not visible in output
        JLabel labelStart = new JLabel("Enter your name");
        JTextField tfStart = new JTextField(20);
        
        
        panel.add(labelStart); // Components Added using Flow Layout
        panel.add(tfStart);
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.setVisible(true);
        tfStart.addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10)
                {
                    panel.remove(labelStart);
                    panel.remove(tfStart);
                    frame.setVisible(false);
                    createChatWindow();
                }
            }
        });
    }

    public void createChatWindow()
    {
        JLabel label = new JLabel("Enter Text");
        JTextField tf = new JTextField(20); // accepts upto 10 characters
        tf.addKeyListener(new KeyAdapter() 
        {
            public void keyPressed(KeyEvent e) 
            {
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
        send.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
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
        });
        panel.add(label); // Components Added using Flow Layout
        panel.add(tf);
        panel.add(send);
        ta = new JTextArea();
        ta.setLineWrap(true);
        ta.setEditable(false);
        frame.getContentPane().add(BorderLayout.CENTER, ta);
        frame.setVisible(true);
    }
}
