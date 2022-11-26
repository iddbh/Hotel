package com.sym.hotel.Util;


import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.internet.MimeMessage.RecipientType;


public class MailTest
{
    public static String sendMail(String to) throws MessagingException, IOException
    {
        // read properties
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get("src/main/resources/static/mail.properties")))
        {
            props.load(in);
        }

        // read message info
        List<String> lines = Files.readAllLines(Paths.get("src/main/resources/static/message.txt"), StandardCharsets.UTF_8);

        String from = lines.get(0);

        String subject = lines.get(2);

        StringBuilder builder = new StringBuilder();
        String code=null;
        Random r = new Random();
        for (int i = 3; i < lines.size(); i++)
        {
            builder.append(lines.get(i));
            if(i==5){
                code=String.valueOf(r.nextInt(100000)+100000);
                builder.append(code);
            }
            builder.append("\n");
        }


        // read password for your email account
        String password = "sus16920Tech";


        Session mailSession = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(mailSession);
        // TODO 1: check the MimeMessage API to figure out how to set the sender, receiver, subject, and email body

        message.setFrom(new InternetAddress(from));
        message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
        message.setSubject(subject);
        message.setText(builder.toString());
        // TODO 2: check the Session API to figure out how to connect to the mail server and send the message
        Transport transport= mailSession.getTransport();
        transport.connect(from,password);
        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
        return code;
    }
}