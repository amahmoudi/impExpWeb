package com.bluescale.web.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;
import javax.mail.Message.RecipientType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bluescale.business.mail.Email;
import com.bluescale.business.mail.Mailer;
import com.bluescale.business.mail.TransportStrategy;
import com.bluescale.business.model.User;
import com.bluescale.business.service.UserService;

@RestController
public class loadFileRestController {
    static Logger logger = LoggerFactory.getLogger(loadFileRestController.class);
    
    final String RDSTRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    
    @Resource(name="userService")
    UserService  userService;
    @Autowired
    private Environment environment;
    
	/* L'adresse IP de votre serveur SMTP */
	String smtpServer = "10.48.000.00";

	/* L'adresse de l'expéditeur */
	String from = "monAdresse@monDomaine.fr";

	/* L'adresse du destinataire */
	String to = "adresseDestinataire@domaine.fr";

	/* L'objet du message */
	String objet = "Objet";

	/* Le corps du mail */
	String texte = "Texte du mail";
    
    
    @RequestMapping(value = "/fileUpload", method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody RestResponse  uploadFileHandler(@RequestParam("filename") String filename,@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
 
                // Creating the directory to store file
                User findUserFromSession = userService.findUserFromSession();
                String rootPath = environment.getRequiredProperty("upload.path");
                File dir = new File(rootPath + File.separator + findUserFromSession.getUserEmail()+"_"+"test");
                if (!dir.exists())
                    dir.mkdirs();
 
                // Create the file on server
                File serverFile = new File(dir.getAbsolutePath()+ File.separator + filename);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();
                findUserFromSession.setDateUploadFile(new Date());
                findUserFromSession.setUserEnabled(false);
                userService.updateUser(findUserFromSession);
                logger.debug("Server File Location=" + serverFile.getAbsolutePath());
                return new RestResponse(true, "file is uploaded"); 
            } catch (Exception e) {
                logger.debug("The upload file is failed");
                return new RestResponse(true, "The upload file is failed");
            }
        } else {
            logger.debug("The file is empty");
            return new RestResponse(true, "The upload file is failed"); 
        }}
    

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(@RequestParam ("name") String name, final HttpServletRequest request, final HttpServletResponse response) {
    	logger.trace("name : {}", name);

        File file = new File (environment.getRequiredProperty("download.path")+File.separator + name);
        logger.trace("Write response...");
        try (InputStream fileInputStream = new FileInputStream(file);
                OutputStream output = response.getOutputStream();) {

            response.reset();

            response.setContentType("application/octet-stream");
            response.setContentLength((int) (file.length()));

            response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

            IOUtils.copyLarge(fileInputStream, output);
            output.flush();
        } catch (IOException e) {
        	logger.error(e.getMessage(), e);
        }

    }
    
    
    @RequestMapping(value="/listFileUploaded",method = RequestMethod.GET,headers="Accept=application/json")
    public List<User> listFileUploaded() {  
        final List<User> allUsers = userService.findAllTestPassed();
        return allUsers;
        
    }
    
    @RequestMapping(value = "/createUser", method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody RestResponse createUser(@RequestParam("eMail") String eMail) {
    	String password;
    	try {
    		StringBuilder pswrd= new StringBuilder();
    		pswrd.append(randomString(4));
    		pswrd.append("BlueScale");
			User user = new  User();
			user.setUserEmail(eMail);
			user.setUserEnabled(true);
			password = pswrd.toString();
			user.setUserPassword(password);
			userService.saveUser(user);
		} catch (Exception e) {
			return new RestResponse(true, "The User access is't created");
		}
    	final Email email = new Email();
		email.setFromAddress("FROM", environment.getRequiredProperty("mail.from"));
		email.addRecipient("TO", environment.getRequiredProperty("mail.to"), RecipientType.TO);
		email.setText(environment.getRequiredProperty("mail.text"));
		email.setTextHTML("<b>"+environment.getRequiredProperty("mail.text")+"</b><br>Login:"+eMail+"<br>Password:"+password);
		email.setSubject(environment.getRequiredProperty("mail.text"));
		sendMail(email);
    	return new RestResponse(true, "The User access is created");
	}


    private  void sendMail(final Email email) {
		new Mailer(environment.getRequiredProperty("mail.smtp.host"),Integer.valueOf(environment.getRequiredProperty("mail.smtp.port")), 
				environment.getRequiredProperty("mail.from"), environment.getRequiredProperty("mail.from.password"), TransportStrategy.SMTP_SSL).sendMail(email);
	}
    
    String randomString( int len ){
    	Random rnd = new Random();
       StringBuilder sb = new StringBuilder( len );
       for( int i = 0; i < len; i++ ) 
          sb.append( RDSTRING.charAt( rnd.nextInt(RDSTRING.length()) ) );
       return sb.toString();
    }

}
