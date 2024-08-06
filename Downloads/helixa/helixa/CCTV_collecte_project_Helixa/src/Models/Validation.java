/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Sandaruwan
 */
public class Validation {

    public boolean mobileValidation(String x) {

//        int y = 2 + x;
        return x.matches("^07[1,2,5,6,7,8,0][0-9]{7}$");

    }

    public boolean emailValidation(String email) {

        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
        public boolean passwordValidation(String password) {

            //"^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$"
            //Minimum eight characters, at least one letter and one number
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        return password.matches(passwordRegex);
    }

}
