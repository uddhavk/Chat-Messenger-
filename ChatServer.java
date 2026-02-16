//SocketConnection
import java.net.*;
// Input,Output
import java.io.*;
// Frame
import javax.swing.*;
import java.awt.event.*;
// Font
import java.awt.Font;
// Date,Time
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


class ChatServer
{
    public static void main(String A[]) throws Exception
    {
        /////////////////////////////////
        // Client Connection
        /////////////////////////////////
        
        ServerSocket sock = new ServerSocket(5100);
        System.out.println("Server is waiting at port 5100");

        Socket sockobj = sock.accept();
        System.out.println("Client gets connected with server successfully ");

        // Send Message to Client
        PrintStream sendmsg = new PrintStream(sockobj.getOutputStream());

        // Messsage From Client
        BufferedReader buffer = new BufferedReader(new InputStreamReader(sockobj.getInputStream()));

        /////////////////////////////////
        // Creating Frame
        /////////////////////////////////
    
        JFrame Frame = new JFrame("Server");

        JLabel label1 = new JLabel("Message");
        label1.setBounds(15,10,120,30);

        JLabel label2 = new JLabel("Client Says : ");
        label2.setBounds(15,100,350,30);

        JTextField TextSection = new JTextField();
        TextSection.setBounds(120,10,200,30);

        Font font = new Font("Arial",Font.BOLD,18);        

        JButton Button = new JButton("Send");
        Button.setBounds(140,60,100,30);

        /////////////////////////////////
        // Server Login File
        /////////////////////////////////
        
        // Data will be not over written
        FileWriter logFile = new FileWriter("Server_log.txt",true);

        /////////////////////////////////
        // Date and Time 
        /////////////////////////////////

        DateTimeFormatter DTF =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        /////////////////////////////////
        // Display Frame
        /////////////////////////////////

        Frame.add(label1);
        Frame.add(label2);
        Frame.add(TextSection);
        Frame.add(Button);

        label1.setFont(font);
        label2.setFont(font);

        Frame.setSize(400,200);
        Frame.setLayout(null);
        Frame.setVisible(true);
        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ///////////////////////////////////////
        // Reading Data Action
        ///////////////////////////////////////
    
        new Thread(()->
        {
                    try
                    {
                        String str;
                        while((str = buffer.readLine()) != null)
                        {
                            String time = LocalDateTime.now().format(DTF);
                            label2.setText("Client Says : "+ str);
                            logFile.write(time+" Client message : " +str+"\n");
                            logFile.flush();
                        }
                    }
                    catch(Exception eobj)
                    {}
        }).start();

        ///////////////////////////////////////
        // Sending Data Action
        ///////////////////////////////////////

        Button.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent eobj)
            {
                try
                {
                    // Message to be send
                    String msg = TextSection.getText();
                    sendmsg.println(msg);

                    // saves the details
                    String time = LocalDateTime.now().format(DTF);
                    logFile.write(time+" Server : "+msg+"\n");
                    logFile.flush();

                    TextSection.setText("");                    
                }
                catch(Exception aobj)
                {}
            }
        });
    }       
}