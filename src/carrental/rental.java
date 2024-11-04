package carrental;
    
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.time.LocalDate;

public class rental {
    public void Rental(){
        Scanner sc = new Scanner(System.in);
        
        boolean isSelected = false;
        
        do{
            System.out.println("\nRental:");
            System.out.println("1. Add Rent");
            System.out.println("2. Edit Rent");
            System.out.println("3. Remove Rent");
            System.out.println("4. View Rent");
            System.out.println("5. Select Rent");
            System.out.println("6. Exit");
            System.out.print("Enter option: ");
            int option = sc.nextInt();

            switch(option){
                case 1:
                    addRent();
                    break;
                case 2:
                    editRent();
                    break;
                case 3:
                    removeRent();
                    break;
                case 4:
                    viewRent();
                    break;
                case 5:
                    viewIndivReport();
                    break;
                case 6:
                    isSelected = true;
                    break;
                default: 
                    System.out.println("Error, invalid option");
            }
        } while(!isSelected);
    }
    
    public void addRent(){
        LocalDate date = LocalDate.now();
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Select costumer id: ");
        int cid = sc.nextInt();
        
        while(conf.getSingleValue("SELECT c_id FROM costumer WHERE c_id = ?", cid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            cid = sc.nextInt();
        }
        
        System.out.print("Enter Car ID: ");
        int car_id = sc.nextInt();
        
        while(conf.getSingleValue("SELECT car_id FROM car WHERE car_id = ?", car_id) == 0){
            System.out.print("ID doesn't exist, try again: ");
            car_id = sc.nextInt();
        }
        
        System.out.print("Enter Due Date (format: yyyy-mm-dd): ");
        String due_date = sc.next();
        
        System.out.print("Enter Payment Status: ");
        String status = sc.next();
       
        String sql = "INSERT INTO rental (c_id, car_id, r_rent_date, r_due_date, r_payment_status) VALUES ( ?, ?, ?, ?, ?)";
        
        conf.addRecord(sql, cid, car_id, date.toString(), due_date, status);
    }
    
    public void editRent(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Enter rent ID: ");
        int rid = sc.nextInt();
        
        while(conf.getSingleValue("SELECT r_id FROM rental WHERE r_id = ?", rid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            rid = sc.nextInt();
        }
        
        System.out.print("Enter New Due Date (format: yyyy-mm-dd): ");
        sc.nextLine();
        String newDue = sc.nextLine();
        
        System.out.print("Enter New Payment Status: ");
        String newStatus = sc.nextLine();
        
        String sql = "UPDATE rental SET r_due_date = ?, r_payment_status = ? WHERE r_id = ?";
        
        conf.updateRecord(sql, newDue, newStatus, rid);
    }
    
    public void removeRent(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Enter rent ID to remove: ");
        int rid = sc.nextInt();
        
        while(conf.getSingleValue("SELECT r_id FROM rental WHERE r_id = ?", rid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            rid = sc.nextInt();
        }
        
        String sqlDelete = "DELETE FROM rental WHERE r_id = ?";
        
        conf.deleteRecord(sqlDelete, rid);
    }
    
    public void viewRent(){
        config conf = new config();
        
        String rentalQuery = "SELECT r_id, c.c_name, cr.car_type, r_rent_date, r_due_date, r_payment_status FROM rental "
                + "INNER JOIN costumer c ON rental.c_id = c.c_id INNER JOIN car cr ON rental.car_id = cr.car_id";
        
        String[] rentalHeaders = {"ID", "Costumer", "Car", "Date Rented", "Due Date", "Payment Status"};
        String[] rentalColumns = {"r_id", "c_name", "car_type", "r_rent_date", "r_due_date", "r_payment_status"};
        String[] whereValues = null;
        
        conf.viewRecords(rentalQuery, rentalHeaders, rentalColumns, whereValues);
    }
    
    public void viewIndivReport(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Enter Rent ID: ");
        int rid = sc.nextInt();
        
        while(conf.getSingleValue("SELECT r_id FROM rental WHERE r_id = ?", rid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            rid = sc.nextInt();
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
