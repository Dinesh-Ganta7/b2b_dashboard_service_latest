package com.stg.b2b.smtp;

import com.stg.b2b.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mail")
public class MailController {

    @Autowired
    MailUtil  mailUtil;
    @GetMapping("{to}")
    public String sendMail(@PathVariable("to") String to)  {
        String out="";
        try {
            mailUtil.sendHtmlEmail(to,"***IGNORE***","This is a test mail please ignore it!".toUpperCase());
            out="Message sent successfully";
        }catch (Exception e){
            e.printStackTrace();
            out="Failed to send the message";
        }
        return out;
    }
}
