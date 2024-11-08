package implementation;

import carrental.config;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class history {
    
    public void showRentHistory(){
        config conf = new config();
        customers c = new customers();
        
        c.viewCostumer();
        
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
