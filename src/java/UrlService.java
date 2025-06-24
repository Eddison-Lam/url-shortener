/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author eddis
 */
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
public class UrlService {
    private static final Logger LOGGER= Logger.getLogger(UrlService.class.getName());
    private static final String POOL= "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int MINCODELENGTH= 6, MAXCODELENGTH= 12;
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found: " + e.getMessage());
        }
    }
    public static Connection getConnection(ServletContext context)throws Exception{
        Properties props= new Properties();
        try{
            String path= context.getRealPath("/WEB-INF/database.properties");
            props.load(new FileInputStream(path));
        }catch(FileNotFoundException e){
            throw new SQLException("Database properties file not found: " + e.getMessage());
        }
        String url= props.getProperty("db.url");
        String user= props.getProperty("db.user");
        String password= props.getProperty("db.password");
        if(url==null||user==null||password==null){
            throw new SQLException("Missing database configuration in properties file");
        }
        try{
            return DriverManager.getConnection(url, user, password);
        }catch(SQLException e){
            throw new SQLException("Failed to connect to database: " + e.getMessage());
        }

    }
    
    public static String generateUniqueShortCode(Connection conn){
        Random rand= new Random();
        int collision= 0;
        while(collision++<20){
            int length= MINCODELENGTH+rand.nextInt(MAXCODELENGTH-MINCODELENGTH+1);
            StringBuilder code= new StringBuilder();
            for(int i=0;i<length;i++){
                code.append(POOL.charAt(rand.nextInt(POOL.length())));
            }
            String shortCode= code.toString();
            if(isShortCodeUnique(conn, shortCode))
                return shortCode;      
        }
        LOGGER.severe("Too many collision.");
        return null;
    }
    
    public static boolean isShortCodeUnique(Connection conn, String shortCode){
        try{
            String sql= "SELECT short_code FROM url_mapping WHERE short_code = ?";
            PreparedStatement statement= conn.prepareStatement(sql);
            statement.setString(1, shortCode);
            ResultSet rs= statement.executeQuery();
            boolean isUnique= !rs.next();
            rs.close();
            statement.close();
            return isUnique;
        }catch(Exception e){
            LOGGER.severe("Error checking short code uniqueness: "+e.getMessage());
            return false;
        }    
    }
    
    public static String saveUrl(ServletContext context, String longUrl, String customShortCode) throws Exception{
        System.out.println("Processing customShortCode: " + customShortCode);
        if(longUrl==null||longUrl.isEmpty())
            throw new IllegalArgumentException("URL cannot be empty");
        if(!longUrl.matches("^https?://.*$"))
            throw new IllegalArgumentException("URL must start with http:// or https://");
        if(!longUrl.matches("^(https?://)[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(/.*)?$")){
            if (!longUrl.matches("^(https?://)[a-zA-Z0-9.-]+.*$"))
                throw new IllegalArgumentException("Invalid domain name");
            else
                throw new IllegalArgumentException("Invalid top-level domain");
        }
        Connection conn= getConnection(context);
        String shortCode=(customShortCode !=null && !customShortCode.isEmpty())? customShortCode.trim(): null;
        System.out.println("After trim, shortCode: " + shortCode);
        if(shortCode!=null){
            if(!shortCode.matches("^[a-zA-Z0-9-.]{1,12}")){
                throw new IllegalArgumentException("Short code must be between 1-12 characters and contain only letters, number, - or .");
            }
            if(!isShortCodeUnique(conn, shortCode)){
                throw new IllegalArgumentException("Short code already exists");
            }
        }else{
            shortCode=generateUniqueShortCode(conn);
        }
        
        String sql="INSERT INTO url_mapping(short_code, long_url) VALUES (?,?)";
        PreparedStatement statement= conn.prepareStatement(sql);
        statement.setString(1,shortCode);
        statement.setString(2, longUrl);
        statement.executeUpdate();
        statement.close();
        conn.close();
        return shortCode;
    }
    
    public static String getLongUrl(ServletContext context, String shortCode) throws Exception{
        Connection conn= getConnection(context);
        String sql= "SELECT long_url FROM url_mapping WHERE short_code=?";
        PreparedStatement statement= conn.prepareStatement(sql);
        statement.setString(1,shortCode);
        ResultSet rs= statement.executeQuery();
        String longUrl= rs.next()? rs.getString("long_url"): null;
        rs.close();
        statement.close();
        conn.close();
        return longUrl;
    }
}
