package domein;


import repositories.GebruikerDaoJpa;
import repositories.SessieDaoJpa;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.Base64;

public class InlogController {
    private ITlabGebruiker ITlabGebruiker;

    public boolean login(String gebruikersnaam, String wachtwoord){
        int juisteWachtwoord = 0;

        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:sqlserver://40.118.71.191:1433;database=IT_LAB;encrypt=false;trustServerCertificate=false;loginTimeout=30;","simon.dewilde","2903263C2b.e");
           PreparedStatement  statement = connection.prepareStatement("select * from Javagebruikers where gebruikersnaam = ? ");
            statement.setString(1, gebruikersnaam);

            ResultSet rs = statement.executeQuery();

            while(rs.next())
                juisteWachtwoord = rs.getInt(3);

            connection.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }
        boolean succes = tryToLogin(wachtwoord.hashCode(), juisteWachtwoord);

        if(succes){
            ITlabGebruiker = new GebruikerDaoJpa().getGebruikerByGebruikersnaam(gebruikersnaam);
        }
        return succes;

    }

    private boolean tryToLogin(int w1, int w2){
        if(w1 == w2) return true;
        else return false;
    }

    public void addVerantwoordelijke(ITlabGebruiker gebruiker){
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:sqlserver://40.118.71.191:1433;database=IT_LAB;encrypt=false;trustServerCertificate=false;loginTimeout=30;","simon.dewilde","2903263C2b.e");
            PreparedStatement  statement = connection.prepareStatement("INSERT INTO JavaGebruikers\n" +
                    "VALUES (?,?, ?);");
            statement.setString(1, gebruiker.getNaam());
            statement.setString(2, gebruiker.getGebruikersnaam());
            statement.setInt(3,"P@ssword1".hashCode());

            ResultSet rs = statement.executeQuery();
                connection.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ITlabGebruiker getITlabGebruiker() {
        return ITlabGebruiker;
    }
}
