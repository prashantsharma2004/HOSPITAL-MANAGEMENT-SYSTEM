package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {

    private static final String url="jdbc:mysql://127.0.0.1:3306/hospital";

    private static final String username="root";
    private static final String password="root";

   public static void main(String[] args) {

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();

        }
        Scanner scanner = new Scanner(System.in);
        try{
            Connection connection = DriverManager.getConnection(url, username,password);
            Patient patient =new Patient(connection, scanner);
            Doctors doctor = new Doctors(connection);

            while(true){

                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patient");
                System.out.println("2. View  Patient ");
                System.out.println("3. View Doctor ");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter your choice");
                int choice = scanner.nextInt();


                    switch (choice){
                        case 1:
                            // Add Patient
                            patient.addPatient();
                            System.out.println();
                            break;

                        case 2:
                            //view Patient
                            patient.viewPatient();
                            System.out.println();
                            break;

                        case 3:
                            // View Doctors
                            doctor.viewDoctors();
                            System.out.println();
                            break;

                        case 4:
                                // Book Appointment
                            bookAppointment(patient,doctor,connection,scanner);
                            System.out.println();
                            break;

                        case 5:
                            System.out.println("THANK YOU! FOR USING HOSPITAL MANAGEMENT SYSTEM");
                                    return;
                           default:
                               System.out.println(" Enter valid choice...");
                               break;

                }



            }


        }catch (SQLException e){
            e.printStackTrace();
        }

   }

   public static void bookAppointment(Patient patient, Doctors doctor, Connection connection, Scanner scanner){
       System.out.println("Enter Patient ID : ");
       int patientID = scanner.nextInt();
       System.out.println("Enter Doctor ID : ");
       int doctorID = scanner.nextInt();
       System.out.println("Enter appointment date (yyyy-mm-dd) : ");
       String appointmentDate = scanner.next();

       if(patient.getPatientById(patientID) && doctor.getDoctorById(doctorID)){

           if(checkDoctorAvailability(doctorID,appointmentDate , connection)) {
               String appointmentQuery = "insert into APPOINTEMENT(patients_id, doctor_id, appointment_date) values(?,?,?)";

               try {
                   PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                   preparedStatement.setInt(1, patientID);
                   preparedStatement.setInt(2, doctorID);
                   preparedStatement.setString(3, appointmentDate);
                   int rowsAffected = preparedStatement.executeUpdate();
                   if (rowsAffected > 0) {
                       System.out.println("Appointment booked successfully");
                   } else {
                       System.out.println("Appointment not booked");
                   }

               } catch (SQLException e) {
                   e.printStackTrace();

               }
           }else{
               System.out.println("Doctor not available on this date");
           }

       }else{
           System.out.println("Either patient or doctor doesn't exist!!!");
       }

   }

   public static boolean checkDoctorAvailability(int  doctorID,String appointmentDate, Connection connection){
       String query = "select COUNT(*) from APPOINTEMENT where doctor_id = ? and appointment_date = ?";

       try{
           PreparedStatement preparedStatement= connection.prepareStatement(query);
           preparedStatement.setInt(1,doctorID);
           preparedStatement.setString(2,appointmentDate);
           ResultSet resultSet = preparedStatement.executeQuery();

           if(resultSet.next()){
               int count = resultSet.getInt(1);
               if(count==0){
                   return true;
               }
               else{
                   return false;
               }
           }
       }catch(SQLException e){
           e.printStackTrace();
       }
 return false;

   }

}
