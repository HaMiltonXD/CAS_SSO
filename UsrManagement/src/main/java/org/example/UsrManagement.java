package org.example;

import jdk.nashorn.internal.ir.debug.ClassHistogramElement;

import java.sql.*;
import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UsrManagement {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/db_sso";
    private static final String USER = "root";
    private static final String PASS = "123456";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        Scanner scanner = new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            // Create table
            String createTableSql = "CREATE TABLE IF NOT EXISTS db_sso.t_cas (id INT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(50), password VARCHAR(50))";
            stmt.executeUpdate(createTableSql);
            boolean running = true;
            do {
                running = true;
                System.out.println("请问需要执行什么操作？输入1进行增加用户数据，输入2删除用户数据，输入3查询用户数据，输入4更新用户密码，输入quit退出");
                String option = scanner.next();
                switch (option) {
                    case "1":
                        System.out.println("请输入要添加的账号，以账号、密码格式键入:");
                        String newUsername = scanner.next();
                        // 检查数据库中是否已存在相同用户名的用户
                        ResultSet checkRs = stmt.executeQuery("SELECT * FROM t_cas WHERE username = '" + newUsername + "'");
                        if (checkRs.next()) {
                            System.out.println("用户名已经存在，是否要更新该用户信息？(y/n)");
                            String updateChoice = scanner.next();
                            if (updateChoice.equalsIgnoreCase("y")) {
                                System.out.println("请输入新的密码");
                                String updatePwd = getMD5(scanner.next()); // 获取新的密码，并加密
                                String updateSql = "UPDATE t_cas SET password = '" + updatePwd + "' WHERE username = '" + newUsername + "'";
                                stmt.executeUpdate(updateSql);
                                System.out.println("用户信息更新成功");
                            } else {
                                System.out.println("系统退出");
                            }
                        } else {
                            // 用户不存在，执行插入操作
                            String newPassword = getMD5(scanner.next());
                            String insertSql = "INSERT INTO t_cas (username, password) VALUES ('" + newUsername + "', '" + newPassword + "')";
                            stmt.executeUpdate(insertSql);
                            System.out.println("用户信息插入成功");
                        }
                        break;
                    case "2":
                        System.out.println("请输入要删除的账户名称:");
                        String deleteUsername = scanner.next();
                        ResultSet deleteRs = stmt.executeQuery("SELECT * FROM t_cas WHERE username = '" + deleteUsername + "'");
                        if (deleteRs.next()) {
                            System.out.println("是否确认删除用户 " + deleteUsername + "？(y/n)");
                            char confirmDelete = scanner.next().charAt(0);
                            if (confirmDelete == 'y') {
                                String deleteSql = "DELETE FROM t_cas WHERE username = '" + deleteUsername + "'";
                                stmt.executeUpdate(deleteSql);
                                System.out.println("用户信息删除成功!");
                            } else {
                                System.out.println("用户信息未删除!");
                            }
                        } else {
                            System.out.println("不存在对应用户名的用户:(");
                        }
                        break;
                    case "3":
                        System.out.println("请输入要查询的账户名称:");
                        String queryUsername = scanner.next();
                        ResultSet queryRs = stmt.executeQuery("SELECT * FROM t_cas WHERE username = '" + queryUsername + "'");
                        while (queryRs.next()) {
                            String username = queryRs.getString("username");
                            String password = queryRs.getString("password");
                            System.out.println("Username: " + username + ", Password: " + password);
                        }
                        break;
                    case "4":
                        System.out.println("请输入要更新密码的账户名和新密码，格式为：用户名 新密码:");
                        String updateUsername = scanner.next();
                        String newPwd = getMD5(scanner.next());
                        String updateSql = "UPDATE t_cas SET password = '" + newPwd + "' WHERE username = '" + updateUsername + "'";
                        stmt.executeUpdate(updateSql);
                        System.out.println("用户密码更新成功!");
                        break;
                    case "quit":
                        System.out.println("系统退出!");
                        running = false;
                        break;
                    default:
                        System.out.println("无效的选项:(");
                }
            }while(running);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
            scanner.close();
        }
    }

    private static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}