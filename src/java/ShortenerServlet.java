/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
/**
 *
 * @author eddis
 */
@WebServlet("/shorten")
public class ShortenerServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        String longUrl= request.getParameter("longUrl");
        String customShortCode= request.getParameter("customShortCode");
        System.out.println("Received customShortCode: " + customShortCode);
        response.setContentType("text/html");
        try{
            String shortCode= UrlService.saveUrl(getServletContext(), longUrl, customShortCode);
            request.setAttribute("result","Short URL: http://localhost:8080/UrlShortener/r/"+ shortCode);
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }catch(IllegalArgumentException e){
            request.setAttribute("error", "Error: "+e.getMessage());
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage()); 
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }catch(Exception e){
            request.setAttribute("error", "Server error in shortening");
            System.out.println("Exception: " + e.getMessage()); 
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }
    
}
