//kullandıgım kutuphaneler
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.IndexOutOfBoundsException;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class Main {
    public static void main(String[] args)throws java.io.IOException  {
        Scanner scan = new Scanner(System.in);
        //ana menu ve secenegi okuyup diger menulere yonlendirne
        System.out.println("1.Elit Üye ekle:\n");
        System.out.println("2.Genel Üye ekle\n");
        System.out.println("3.Mail Gönder\n");
        int flag = 0;
        switch(scan.nextInt()){
            case 1 :
                //ilk menuye geçiş-- matches ile girilen degerlerin uygunlugu kontrol edip dosyayaekle fonksiyonu cagrılıyor
                while(flag == 0){
                    String doruMail = "^(.+)@(.+)$";
                    String doruIsim = "^(.+)";
                    System.out.println("Elit Üyenin ismi:\n");
                    String isim = scan.next();
                    System.out.println("Elit Üyenin soyismi:\n");
                    String soyisim = scan.next();
                    System.out.println("Elit Üyenin mail adresi:\n");
                    String mail = scan.next();
                    if(isim.matches(doruIsim) == true && soyisim.matches(doruIsim) == true && mail.matches(doruMail) == true){
                        flag = 1;
                        dosya.dosyayaekle(isim+"    "+soyisim+ "    "+mail,"Elit");
                    }
                    else System.out.println("Lütfen kriterlere uygun değerler giriniz!!");
                }break;
            case 2:
                //ikinci menuye geçiş-- matches ile girilen degerlerin uygunlugu kontrol edip dosyayaekle fonksiyonu cagrılıyor

                while(flag == 0){
                    String doruMail = "^(.+)@(.+)$";
                    String doruIsim = "^(.+)";
                    System.out.println("Genel Üyenin ismi:\n");
                    String isim = scan.next();
                    System.out.println("Genel Üyenin soyismi:\n");
                    String soyisim = scan.next();
                    System.out.println("Genel Üyenin mail adresi:\n");
                    String mail = scan.next();
                    if(isim.matches(doruIsim) == true && soyisim.matches(doruIsim) == true && mail.matches(doruMail) == true){
                        flag = 1;
                        dosya.dosyayaekle(isim+"    "+soyisim+"    "+mail,"Genel");
                    }
                    else System.out.println("Lütfen kriterlere uygun değerler giriniz!!");
                }break;
            case 3:
                //3.menuye gecis-- yollayan mail bilgileri alınıp kime yollanacagı seciliyor--secimlere gore mailgonder ve basarı fonkları calıstırılıyor
                System.out.println("Mail adresiniz:");
                String ma = scan.next();
                System.out.println("Sifreniz:");
                String pass = scan.next();
                System.out.println("Mailinizin baslıgı:");
                String bas = scan.next();
                System.out.println("Mailinizin icerigi: ");
                String ic = scan.next();
                System.out.println("1.Elitlere Mail:\n");
                System.out.println("2.Genellere Mail:\n");
                System.out.println("3.Herkese Mail:\n");

                int i = scan.nextInt();
                if(i==1){
                    String mailler[] = dosya.mailayir("Elit");
                    mail.mailgonder(mailler,bas,ic,ma,pass);
                    elitmail.basari();
                }
                else if(i==2){
                    String mailler[] = dosya.mailayir("Genel");
                    mail.mailgonder(mailler,bas,ic,ma,pass);
                    genelmail.basari();
                }
                else if(i==3){
                    String mailler[] = dosya.mailayir("all");
                    mail.mailgonder(mailler,bas,ic,ma,pass);
                }
                break;



        }
    }
}
class dosya{
    //bir txt dosyası olusturup baslıkların olup olmadıgı kontrol ediliyor--varsa devam ediliyor yoksa yazılıyor
    public static void dosyayaekle(String eklenecek,String tip) throws java.io.IOException {
        File dosya = new File("kullanici.txt");
        FileWriter dosyayazici = new FileWriter("kullanici.txt",true);

        BufferedReader reader = new BufferedReader(new FileReader("kullanici.txt"));
        BufferedReader reader1 = new BufferedReader(new FileReader("kullanici.txt"));

        int lines = 0;
        while (reader.readLine() != null) lines++;


        String kontrol = reader1.readLine();
        if (kontrol == null){
            dosyayazici.write("#Elitler\n");
            dosyayazici.write("#Geneller\n");
        }

        dosyayazici.close();
        reader1.close();
        reader.close();

        switch (tip){
            //eger tip elitse baslık olan ilk satır alınıp bir stringe konuluyor sonra geri kalanı baska bir stringe konuluyor sonra istenilen string araya yazılıp tekrar dosyaya yazdırılıyor
            case "Elit":
                String parca1 = Files.readAllLines(Paths.get("kullanici.txt"), StandardCharsets.UTF_8).get(0)+"\n";
                String parca2 = Files.readAllLines(Paths.get("kullanici.txt"), StandardCharsets.UTF_8).get(1)+ "\n";

                for(int i=2;i<lines;i++){
                    parca2 = parca2 + Files.readAllLines(Paths.get("kullanici.txt"), StandardCharsets.UTF_8).get(i)+"\n";
                }
                String finalparca = parca1 + eklenecek + "\n" + parca2;
                dosya.delete();
                File dosya1 = new File("Kullanici.txt");
                FileWriter dosya1w = new FileWriter("Kullanici.txt");
                dosya1w.write(finalparca);
                dosya1w.close();
                break;
            case "Genel":
                // eger tip genelse direk dosyanın sonuna ekliyor
                FileWriter dosya1wg = new FileWriter("Kullanici.txt",true);
                dosya1wg.write(eklenecek + "\n");
                dosya1wg.close();
                break;
        }
    }
    public static String[] mailayir(String tip) throws java.io.IOException{
        //oncelikle tum satırları okuyup mail arrayın içine yazılıyor sonra donguler ve split fonku kullanılarak icinden mailler ayrılıyor sonra baslıkların konumları kullanılarak elit ve genel olmasına göre istenilen mailler ayrılıyor
        BufferedReader reader1 = new BufferedReader(new FileReader("kullanici.txt"));
        int lines = 0;
        while (reader1.readLine() != null) lines++;
        int flag2=0;
        reader1.close();
        if(lines<3){
            System.out.println("Lütfen önce birkaç üye oluşturun");
            return null;
        }
        else {
            String mail[]= new String[lines-2];
            int j=0;

            for(int i=0;i<lines;i++){
                if(Files.readAllLines(Paths.get("kullanici.txt"), StandardCharsets.UTF_8).get(i).equals("#Elitler")||Files.readAllLines(Paths.get("kullanici.txt"), StandardCharsets.UTF_8).get(i).equals("#Geneller")){
                    flag2 = i;


                }
                else {
                    String TempArray[]=new String[3];
                    TempArray= Files.readAllLines(Paths.get("kullanici.txt"), StandardCharsets.UTF_8).get(i).split("    ");

                    mail[j]= TempArray[2];
                    j=j+1;}
            }
            switch (tip){
                case "Elit":
                    String son[]=new String[flag2-1];
                    for(int i =0;i<flag2-1;i++){

                        son[i]=mail[i];


                    }
                    return son;
                case "Genel":
                    int k =0;

                    String son1[] = new String[lines-2 -flag2+1];
                    for(int i = flag2-1 ;i<lines-2;i=i+1){


                        son1[k]= mail[i];
                        k=k+1;

                    }
                    return son1;
                case "all":
                    return mail;
            }

        }

        return null;
    }



}


class mail{
    //mailin durumuna yönelik belirti veren fonksiyon
    public static void basari(){
        System.out.println("Mail başarıyla gönderildi");
    }

    //mail gönderme fonku ,yollanacakları konu ve baslıgı ve yollayan email ve sifreyi alır
    public static void mailgonder(String[] recipients,String subject, String body,String email,String sifre ) {

        Properties properties = System.getProperties();

        properties.setProperty("mail.smtp.host", "smtp.office365.com");
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, sifre);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(email));

            InternetAddress[] toAddresses = new InternetAddress[recipients.length];
            for (int i = 0; i < recipients.length; i++) {
                toAddresses[i] = new InternetAddress(recipients[i]);
            }
            message.setRecipients(Message.RecipientType.TO, toAddresses);

            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
            mail.basari();
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
//elitlere yönelik mail durumunu belirten basarı fonku
class elitmail extends mail{
    public static void basari(){
        System.out.println("Elitlere gönderildi");
    }
}
//genelllere yönelik mail durumunu belirten basarı fonku
class genelmail extends mail{
    public static void basari(){
        System.out.println("Genellere gönderildi");
    }
}


