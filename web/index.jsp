<%-- 
    Document   : index
    Created on : 2025年6月18日, 下午11:28:14
    Author     : eddis
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>URL Shortener</title>
        <style>
            .error{color: red;}
            .result{color: green;}
            .form-container { margin: 20px; }
            input[type="text"] { width: 300px; margin: 5px; }
            button { padding: 8px 15px; margin: 5px; }
        </style>
    </head>
    <body>
        <div class="form-container">
            <h1>URL Shortener</h1>
            <form action="shorten" method="post">
                <label>LongURL:<input type="text" name="longUrl" required></label><br>
                <label>Custom Short Code (optional,1-12 chars): <input type="text" name="customShortCode"></label><br>
                <input type="submit" value="Shorten">
            </form>
            <% if(request.getAttribute("error")!=null){%>
            <p class="error"><%= request.getAttribute("error") %></p>
            <% } %>
            <% if(request.getAttribute("result")!=null){%>
            <p class="result"><%= request.getAttribute("result") %></p>
            <% } %>
        </div>
    </body>
</html>
