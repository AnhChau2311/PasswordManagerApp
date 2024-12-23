import java.util.Random;

public class PasswordGenerator{
    public static final String small_caps = "abcdefghijklmnopqrstuvwxyz";
    public static final String caps = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String Numeric = "012345679";
    public static final String specific_char = "!@#$%^&*()-_=+[]{};:,.<>/?";

    public PasswordGenerator() {
        random = new Random();
    }

    private final Random random;

    public String generatePassword(int length, boolean includeUppercase, boolean includeLowercase, boolean includeNumbers,
                                   boolean includeSpecialSymbols){
        StringBuilder passwordBuilder = new StringBuilder();

        String validCharacters = "";
        if(includeUppercase){
            validCharacters += caps;
        }
        if(includeLowercase){
            validCharacters += small_caps;
        }
        if(includeNumbers){
            validCharacters += Numeric;
        }
        if(includeSpecialSymbols){
            validCharacters += specific_char;
        }

        for(int i = 0; i < length; i++){
            int randomIndex = random.nextInt(validCharacters.length());
            char randomChar = validCharacters.charAt(randomIndex);
            passwordBuilder.append(randomChar);
        }

        return passwordBuilder.toString();
    }}
