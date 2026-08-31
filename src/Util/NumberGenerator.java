package Util;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class NumberGenerator {

    static Random random = new Random();
    static Set<Integer> accountNumbers = new HashSet<>();

    public static int generateAccountNumber(){
        int number;
        do {
            number = 1000+ random.nextInt(9000);
        }while(accountNumbers.contains(number));
        accountNumbers.add(number);
        return number;
    }

    static Set<Long> idNumber = new HashSet<>();
    public static long generateIdNumber(){
        long id;
        do {
            id = 1000+ (long) random.nextInt(9000);
        }while(idNumber.contains(id));
        idNumber.add(id);
        return id;
    }
}
