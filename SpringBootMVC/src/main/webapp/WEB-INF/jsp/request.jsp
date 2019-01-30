<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.sql.PreparedStatement" %><%
    String data = (String)request.getParameter("input");
    out.println(data);
    int dataInt = Integer.parseInt(data);

    String url = "jdbc:postgresql://localhost:5432/postgres";
    String user = "notandi";
    String pass = "12345";

    Connection conn = null;

    try {
        conn = DriverManager.getConnection(url, user, pass);

        PreparedStatement st = conn.prepareStatement("INSERT INTO player_stats(id,two_pmades,two_pmisses) VALUES (?,?,?)");

        st.setInt(1, dataInt);
        st.setInt(2, dataInt);
        st.setInt(3, dataInt);


        st.executeUpdate();
        st.close();


    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
%>