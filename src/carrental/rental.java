package carrental;
    
import implementation.rentals;
import java.util.Scanner;

public class rental {
    public void Rental(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        rentals r = new rentals();
        
        boolean isSelected = false;
        
        do{
            r.viewRent();
            
            System.out.println("\nRental:");
            System.out.println("1. Add Rent");
            System.out.println("2. Edit Rent");
            System.out.println("3. Remove Rent");
            System.out.println("4. Select Rent");
            System.out.println("5. Exit");
            System.out.print("Enter option: ");
            int option = conf.validateInt();

            switch(option){
                case 1:
                    r.addRent();
                    break;
                case 2:
                    r.editRent();
                    break;
                case 3:
                    r.removeRent();
                    break;
                case 4:
                    r.viewIndivReport();
                    break;
                case 5:
                    isSelected = true;
                    break;
                default: 
                    System.out.println("Error, invalid option");
            }
        } while(!isSelected);
    }
}
