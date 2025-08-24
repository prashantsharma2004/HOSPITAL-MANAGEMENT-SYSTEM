package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {


     private Connection connection;

     private Scanner scanner;

     public Patient(Connection connection, Scanner scanner){
         this.connection=connection;
         this.scanner=scanner;

     }

     public void addPatient(){

         System.out.println("Enter Patient name:");
         String name=scanner.next();
         System.out.println("Enter Patient Age:");
         String age=scanner.next();
          System.out.println("Enter Patient Gender:");
           String gender=scanner.next();

     try{

         String query="Insert into Patients(name,age, gender) values(?,?,?)";
         PreparedStatement preparedStatement = connection.prepareStatement(query);
         preparedStatement.setString(1,name);
         preparedStatement.setString(2,age);
         preparedStatement.setString(3,gender);

         int affectedRows=preparedStatement.executeUpdate();

         if(affectedRows>0){
             System.out.println("Patient has been added Successfully");
         }
         else{
             System.out.println("Patient has NOT been added");
         }

     }catch(SQLException e){
         e.printStackTrace();
     }
      }

      public void viewPatient(){
         String query ="select *from patients";

         try{
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet=preparedStatement.executeQuery();

             System.out.println("Patients: ");
             System.out.println("+---------------+---------------+-------------+----------------+");
             System.out.println("| Patients ID   |  Name         | Age         | Gender         |");
             System.out.println("+---------------+---------------+-------------+----------------+");

             while(resultSet.next()){
                 int id =resultSet.getInt("id");
                 String name =resultSet.getString("name");
                 String age =resultSet.getString("age");
                 String gender =resultSet.getString("gender");
                 System.out.printf("| %-16s | %-10s | %-9s | %-9s |\n",id,name,age,gender);
                 System.out.println("+---------------+---------------+-------------+----------------+");

             }

         }
         catch(SQLException e){
             e.printStackTrace();
         }
      }

      public boolean getPatientById(int id){

         String query="select *from patients where id = ?";
         try{
             PreparedStatement preparedStatement= connection.prepareStatement(query);
             preparedStatement.setInt(1,id);
             ResultSet resultSet = preparedStatement.executeQuery();

             if(resultSet.next()){
                 return true;

             }else{
                 return false;
             }
         }
         catch(SQLException e){
             e.printStackTrace();
         }
     return false;
      }


}
