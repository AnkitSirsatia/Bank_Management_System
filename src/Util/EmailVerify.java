package Util;

import com.Ankit.EmailValidator;

public class EmailVerify {

    public static boolean Email_Verify(String email){
        return EmailValidator.emailValidator(email);
    }

}
