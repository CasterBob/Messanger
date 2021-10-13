import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

class Server extends Thread
{
    protected Socket clientSocket; //сокет для общения
    protected static ServerSocket server; // серверсокет
    protected BufferedReader in; // поток чтения из сокета
    protected BufferedWriter out;
    public static ArrayList<ClientListener> clients = new ArrayList<ClientListener>();
    public void sendMessage(String message, Server i)
    {

    }
    public static void main(String[] args) throws IOException
    {
            server = new ServerSocket(8080);
            while (true)
            {
                Socket clientSocket = server.accept();
                clients.add(new ClientListener(clientSocket));
            }
    }
}

class ClientListener extends Server
{
    boolean thread = true;
    ClientListener(Socket socket)
    {
        clientSocket = socket;
        System.out.println("New client");
        start();
    }

    public void run()
    {
        try
        {
            in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            // и отправлять
            out = new BufferedWriter(new OutputStreamWriter(this.clientSocket.getOutputStream()));
            readAndWrite();
        }
        catch (IOException error)
        {
            System.out.println(error);
            try
            {
                clientSocket.close();
                // потоки тоже хорошо бы закрыть
                in.close();
                out.close();
            }
            catch (IOException err)
            {
                System.out.println(err);
            }
        }
    }

    public void readAndWrite()
    {
        String word;
        while (thread)
        {
            try
            {
                word = in.readLine();// ждём пока клиент что-нибудь нам напишет
                System.out.println(word);
                if (word != null)
                {
                    for (Iterator <ClientListener> i = Server.clients.iterator(); i.hasNext();)
                    {
                        try
                        {
                            ClientListener temp = i.next();
                            temp.out.write(word + '\n');
                            temp.out.flush();
                        }
                        catch (IOException error)
                        {
                            System.out.println(error);
                            i.remove();
                            i.next().thread = false;
                        }
                        catch(NoSuchElementException error)
                        {
                            System.out.println("Client is left");
                            i.remove();
                            i.next().thread = false;
                        }
                    }
                }
                else
                {
                    clientSocket.close();
                    System.out.println("Client is left");
                    in.close();
                    out.close();
                    Server.clients.remove(this);
                    thread = false;
                }
                
            }
            catch (IOException error)
            {
                try
                {
                    clientSocket.close();
                    System.out.println("Client is left");
                    // потоки тоже хорошо бы закрыть
                    in.close();
                    out.close();
                    Server.clients.remove(this);
                    thread = false;
                }
                catch(IOException err)
                {
                    System.out.println("Client is left");
                    Server.clients.remove(this);
                    thread = false;
                }
                break;
            }
            catch (NoSuchElementException error)
            {
                System.out.println("Client is left");
                Server.clients.remove(this);
                thread = false;
            }
        }
    }

    @Override
    public void sendMessage(String message, Server i)
    {

    }
}
