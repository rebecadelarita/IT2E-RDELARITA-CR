package implementation;

import carrental.config;
import java.time.LocalDate;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class rentals {
    
    public void addRent(){
        LocalDate date = LocalDate.now();
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Select costumer id: ");
        int cid = conf.validateInt();
        
        while(conf.getSingleValue("SELECT c_id FROM costumer WHERE c_id = ?", cid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            cid = conf.validateInt();
        }
        
        System.out.print("Enter Car ID: ");
        int car_id = conf.validateInt();
        
        while(conf.getSingleValue("SELECT car_id FROM car WHERE car_id = ?", car_id) == 0){
            System.out.print("ID doesn't exist, try again: ");
            car_id = conf.validateInt();
        }
        
        System.out.print("Enter Due Date (format: yyyy-mm-dd): ");
        String due_date = sc.nextLine();
        
        System.out.print("Enter Payment Status: ");
        String status = sc.nextLine();
       
        String sql = "INSERT INTO rental (c_id, car_id, r_rent_date, r_due_date, r_payment_status) "
                + "VALUES ( ?, ?, ?, ?, ?)";
        
        conf.addRecord(sql, cid, car_id, date.toString(), due_date, status);
    }
    
    public void editRent(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Enter rent ID: ");
        int rid = conf.validateInt();
        
        while(conf.getSingleValue("SELECT r_id FROM rental WHERE r_id = ?", rid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            rid = conf.validateInt();
        }
        
        System.out.print("Enter New Due Date (format: yyyy-mm-dd): ");
        String newDue = sc.nextLine();
        
        System.out.print("Enter New Payment Status: ");
        String newStatus = sc.nextLine();
        
        String sql = "UPDATE rental SET r_due_date = ?, r_payment_status = ? WHERE r_id = ?";
        
        conf.updateRecord(sql, newDue, newStatus, rid);
    }
    
    public void removeRent(){
        config conf = new config();
        
        System.out.print("Enter rent ID to remove: ");
        int rid = conf.validateInt();
        
        while(conf.getSingleValue("SELECT r_id FROM rental WHERE r_id = ?", rid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            rid = conf.validateInt();
        }
        
        String sqlDelete = "DELETE FROM rental WHERE r_id = ?";
        
        conf.deleteRecord(sqlDelete, rid);
    }
    
    public void viewRent(){
        config conf = new config();
        
        String rentalQuery = "SELECT r_id, c.c_name, cr.car_type, r_rent_date, r_due_date, r_payment_status "
                + "FROM rental "
                + "INNER JOIN costumer c ON rental.c_id = c.c_id "
                + "INNER JOIN car cr ON rental.car_id = cr.car_id";
        
        String[] rentalHeaders = {"ID", "Costumer", "Car", "Date Rented", "Due Date", "Payment Status"};
        String[] rentalColumns = {"r_id", "c_name", "car_type", "r_rent_date", "r_due_date", "r_payment_status"};
        String[] whereValues = null;
        
        conf.viewRecords(rentalQuery, rentalHeaders, rentalColumns, whereValues);
    }
    
    public void viewIndivReport(){
        config conf = new config();
        
        System.out.print("Enter Rent ID: ");
        int rid = conf.validateInt();
        
        while(conf.getSingleValue("SELECT r_id FROM rental WHERE r_id = ?", rid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            rid = conf.validateInt();
        }
        
        try{
            String rentalQuery = "SELECT r_id, c.c_name, cr.car_type, r_rent_date, r_due_date, r_payment_status FROM rental "
                + "INNER JOIN costumer c ON rental.c_id = c.c_id INNER JOIN car cr ON rental.car_id = cr.car_id WHERE r_id = ?";
            
            PreparedStatement findRow = conf.connectDB().prepareStatement(rentalQuery);
            findRow.setInt(1, rid);
            
            try(ResultSet result = findRow.executeQuery()){
                int rentID = result.getInt("r_id");
                String costumerName = result.getString("c_name");
                String carType = result.getString("car_type");
                String rentDate = result.getString("r_rent_date");
                String dueDate = result.getString("r_due_date");
                String rentStatus = result.getString("r_payment_status");
                
                System.out.println("\nRECEIPT: ");
                System.out.println("--------------------------------");
                System.out.println("Transacton Details: ");
                System.out.println("\nSelected ID: "+rentID);
                System.out.println("Name: "+costumerName);
                System.out.println("Car Type: "+carType);
                System.out.println("Date Rented: "+rentDate);;
                System.out.println("Due Date: "+dueDate);
                System.out.println("Date: "+rentDate);
                System.out.println("--------------------------------");
                System.out.println("Payment Status: "+rentStatus);
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
}
