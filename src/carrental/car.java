package carrental;

import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class car {
    public void Car(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        boolean isSelected = false;
        
        do{
            viewCar();
            
            System.out.println("\nCar:");
            System.out.println("1. Add Car");
            System.out.println("2. Edit Car");
            System.out.println("3. Remove Car");
            System.out.println("4. Select Car");
            System.out.println("5. Exit");
            System.out.print("Enter option: ");
            int option = conf.validateInt();
            
            switch(option){
                case 1:
                    addCar();
                    break;
                case 2:
                    editCar();
                    break;
                case 3:
                    removeCar();
                    break;
                case 4:
                    viewIndivReport();
                    break;
                case 5:
                    isSelected = true;
                    break;
                default:
                    System.out.println("Error, invalid option.");
            }
        } while(!isSelected);
    }
    
    public void addCar(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Car Type: ");
        String ctype = sc.nextLine();
        
        System.out.print("Car Price: ");
        double cprice = conf.validateDouble();
        
        System.out.print("Car Status: ");
        String cstatus = sc.next();
        
        System.out.print("Car Condition: ");
        String ccondition = sc.next();
        
        String sql = "INSERT INTO car (car_type, car_price, car_status, car_condition) VALUES (?, ?, ?, ?)";
        
        conf.addRecord(sql, ctype, cprice, cstatus, ccondition);
    }
    
    public void editCar(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Enter Car ID to edit: ");
        int car_id = conf.validateInt();
        
        while(conf.getSingleValue("SELECT car_id FROM car WHERE car_id = ?", car_id) == 0){
            System.out.print("ID doesn't exist, try again: ");
            car_id = conf.validateInt();
        }
        
        System.out.print("Enter new Type: ");
        sc.nextLine();
        String newType = sc.nextLine();
        
        System.out.print("Enter new Price: ");
        double newPrice = conf.validateDouble();
        
        System.out.print("Enter new Status: ");
        String newStatus = sc.next();
        
        System.out.print("Enter new Condition: ");
        String newCondition = sc.next();
        
        String sqlUpdate = "UPDATE car SET car_type = ?, car_price = ?, car_status = ?, car_condition = ? WHERE car_id = ?";
        
        conf.updateRecord(sqlUpdate, newType, newPrice, newStatus, newCondition, car_id);
    }
    
    public void removeCar(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Enter Car ID to delete: ");
        int car_id = conf.validateInt();
        
        while(conf.getSingleValue("SELECT car_id FROM car WHERE car_id = ?", car_id) == 0){
            System.out.print("ID doesn't exist, try again: ");
            car_id = conf.validateInt();
        }
        
        String sqlDelete = "DELETE FROM car WHERE car_id = ?";
        
        conf.deleteRecord(sqlDelete, car_id);
    }
    
    public void viewCar(){
        config conf = new config();
        
        String carQuery = "SELECT * FROM car";
        String[] carHeaders = {"ID", "Type", "Price", "Status", "Condition"};
        String[] carColumns = {"car_id", "car_type", "car_price", "car_status", "car_condition"};
        String[] whereValues = null;

        conf.viewRecords(carQuery, carHeaders, carColumns, whereValues);
    }
    
    public void viewIndivReport(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Enter Car ID: ");
        int car_id = conf.validateInt();
        
        while(conf.getSingleValue("SELECT car_id FROM car WHERE car_id = ?", car_id) == 0){
            System.out.print("ID doesn't exist, try again: ");
            car_id = conf.validateInt();
        }
        
        try{
            PreparedStatement findRow = conf.connectDB().prepareStatement("SELECT * FROM car WHERE car_id = ?");
            findRow.setInt(1, car_id);
            
            try(ResultSet result = findRow.executeQuery()){
                int carID = result.getInt("car_id");
                String carType = result.getString(("car_type"));
                double carPrice = result.getDouble("car_price");
                String carStatus = result.getString("car_status");
                String carCondition =result.getString("car_condition");
                
                System.out.println("\nSelected ID: "+carID);
                System.out.println("Type: "+carType);
                System.out.println("Price: "+carPrice);
                System.out.println("Status: "+carStatus);
                System.out.println("Condition: "+carCondition);
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
}
