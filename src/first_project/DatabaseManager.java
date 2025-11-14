package first_project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class DatabaseManager {
	
	private static final String DATABASE_FILE = "user_credentials.txt";
	private static final String SEPARATOR = ",";
	
	public static boolean registerUser(String username, String password) {
		if (username.isEmpty() || password.isEmpty()) {
			System.out.println("Registartion Failed: username and password cannot be empty!!!");
			return false;
		}
		
		try (FileWriter writer = new FileWriter(DATABASE_FILE, true)) {
			writer.write(username + SEPARATOR + password + "\n");
			return true;
		} catch (IOException e) {
			System.out.println("Error occured during file writting: " + e.getMessage());
			return false;
		}
	}
	
	public static boolean validateCredentials(String username, String password) {
		File file = new File(DATABASE_FILE);
		if(!file.exists()) {
			return false;
		}
		
		try(Scanner fileScanner = new Scanner(file)) {
			String targetLine = username + SEPARATOR + password;
			
			while(fileScanner.hasNextLine()) {
				String line = fileScanner.nextLine().trim();
				
			if (line.equals(targetLine)) {
				return true;
			}
		}
	}	catch (IOException e) {
		System.out.println("Error occured during file reading: " + e.getMessage());
	}	
	return false;
	
	}
}