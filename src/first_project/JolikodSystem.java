package first_project;
import java.util.Scanner;

public class JolikodSystem {
  
	static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
         	
		boolean shouldContinue;
   
    	do {
    		
    		int optionChoice;
    		
    		System.out.println("\n----------JOLIKOD SYSTEM----------"); 
        	System.out.println("\n[1] Login\n[2] Sign up\n[3] Exit Application\n"); 
        	System.out.print("Choose option: ");
        	
        	// Check if the next token is NOT an integer
        	if (!scanner.hasNextInt()) {
        		System.out.println("❌ Please choose a valid number (1, 2, or 3)!!!\n");
        		scanner.next(); 
        		shouldContinue = true; 
        		continue; 
        	}
        	
        	optionChoice = scanner.nextInt();
        	scanner.nextLine(); // Consume the newline character AFTER nextInt()
        		
        	shouldContinue = true; 
        		
        	switch (optionChoice) {
        	
        	case 1:
        		logIn();
        		// Loop continues (shouldContinue is true) to show main menu again
        		break;
        	case 2:
        		signUp();
        		// Loop continues
        		break;
            case 3:
                System.out.println("Thank you for using Jolikod Systems. Goodbye!");
                shouldContinue = false; // <-- Only place we set it to false
                break;
        	default:
        		System.out.println("❌ Please choose the correct command (1, 2, or 3)!!!\n");
        		// shouldContinue is true, so loop continues
    	    	break;
        	}

    	} while (shouldContinue);
    	
    	scanner.close();
    }
    
    
    public static void logIn() {
    	
    	String usernameInput;
    	String passwordInput;
    	
    	System.out.println("\n---------Log in----------"); 
    	System.out.print("Enter your username: ");
    		usernameInput = scanner.nextLine();
    	System.out.print("Enter your password: ");
    		passwordInput = scanner.nextLine();
    	
    	
    	if (DatabaseManager.validateCredentials(usernameInput, passwordInput)) {
    		System.out.println("\n✅ Successful Login!");
    		SystemInterface system = new SystemInterface(scanner);
    		system.runInterface();
    	} else {
    		System.out.println("❌ Incorrect credentials!");
    	}    
    }
    
    public static void signUp() {
    	
    	System.out.println("\n----------Sign up----------");
    	
    	String createUsername;
    	String createPassword;
    		
    	System.out.print("Create username: ");
    		createUsername = scanner.nextLine();
    	System.out.print("Create password: ");
    		createPassword = scanner.nextLine();
    		
    	if (DatabaseManager.registerUser(createUsername, createPassword)) {
    		System.out.println("\n🎉 Registration successful! You can now log in.");
    	} else {
    		System.out.println("\n❌ Registration failed! username and password may be empty!!!");
    	}	
    }
}
