package carrental;

import implementation.history;
import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        customer c = new customer();
        car cr = new car();
        rental r = new rental();
        config conf = new config();
        history h = new history();
        
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
                    c.Customer();
                    break;
                case 4:
                    h.showRentHistory();
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
}
