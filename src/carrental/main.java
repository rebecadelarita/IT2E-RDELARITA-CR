package carrental;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        costumer c = new costumer();
        car cr = new car();
        rental r = new rental();
        config conf = new config();
        
        boolean isSelected = false;
        
        do{
            System.out.println("\nDevice Rental");
            System.out.println("1. Rental");
            System.out.println("2. Car");
            System.out.println("3. Costumer");
            System.out.println("4. View Rental History");
            System.out.println("5. Exit");
            System.out.print("Enter option: ");
            int option = conf.validateInt();
            
            switch(option){
                case 1:
                    r.Rental();
                    break;
                case 2:
                    cr.Car();
                    break;
                case 3:
                    c.Costumer();
                    break;
                case 4:
                    showRentHistory();
                    break;
                case 5:
                    System.out.print("Confirm exit? (yes/no): ");
                    String confirm = sc.next();
                    
                    if(confirm.contains("y"))
                        System.exit(0);
                default:
                    System.out.println("Error, invalid option.");
            }
        } while(!isSelected);
    }
    
    public static void showRentHistory(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Enter Costumer ID: ");
        int cid = conf.validateInt();
        
        while(conf.getSingleValue("SELECT c_id FROM costumer WHERE c_id = ?", cid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            cid = conf.validateInt();
        }
        
        String rentalQuery = "SELECT r_id, c.c_name, cr.car_type, r_due_date, r_rent_date, r_payment_status FROM rental "
                + "INNER JOIN costumer c ON rental.c_id = c.c_id INNER JOIN car cr ON rental.car_id = cr.car_id WHERE rental.c_id = ?";
        
        try{
            PreparedStatement findRow = conf.connectDB().prepareStatement(rentalQuery);
            findRow.setInt(1, cid);
            
            ResultSet result = findRow.executeQuery();
            
            String renterName = result.getString("c_name");
            int renterID = result.getInt("r_id");
            
            System.out.println("\nSelected ID: "+renterID);
            System.out.println("Renter Name: "+renterName);
            System.out.println("Rent history:");
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
        
        String[] rentalHeaders = {"ID", "Costumer", "Car", "Date Rented", "Due Date", "Payment Status"};
        String[] rentalColumns = {"r_id", "c_name", "car_type", "r_rent_date", "r_due_date", "r_payment_status"};
        String[] whereValues = {String.valueOf(cid)};

        conf.viewRecords(rentalQuery, rentalHeaders, rentalColumns, whereValues);
    }
}
