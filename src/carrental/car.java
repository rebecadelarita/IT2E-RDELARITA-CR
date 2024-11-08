package carrental;

import implementation.cars;
import java.util.Scanner;

public class car {
    public void Car(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        cars c = new cars();
        
        boolean isSelected = false;
        
        do{
            c.viewCar();
            
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
                    c.addCar();
                    break;
                case 2:
                    c.editCar();
                    break;
                case 3:
                    c.removeCar();
                    break;
                case 4:
                    c.viewIndivReport();
                    break;
                case 5:
                    isSelected = true;
                    break;
                default:
                    System.out.println("Error, invalid option.");
            }
        } while(!isSelected);
    }
}
