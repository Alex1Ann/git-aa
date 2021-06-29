import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @authors Alex and Anna
 */
public class Main {

    public static String cleanEmail(String email) {
        int index_arroba = email.indexOf("@");
        String clean_email = "";
        if (index_arroba != -1) {
            String first = email.substring(0, index_arroba);
            String reg_exp = "([_A-Za-z0-9+]+(\\.[_A-Za-z0-9-]+)*)";
            Pattern pattern_first = Pattern.compile(reg_exp);
            Matcher match_first = pattern_first.matcher(first);
            while (match_first.find()) {
                int start = match_first.start();
                int end = match_first.end();
                clean_email += first.substring(start, end);
            }
            clean_email += "@";
            String secund = email.substring(index_arroba + 1, email.length());
            //Регулярные выражения для проверки почты
			reg_exp = "([A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})*)|((\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})*)|(\\.[A-Za-z]{2,})*";
            Pattern pattern_sec = Pattern.compile(reg_exp);
            Matcher match_sec = pattern_sec.matcher(secund);
            while (match_sec.find()) {
                int start = match_sec.start();
                int end = match_sec.end();
                clean_email += secund.substring(start, end);
            }
        }
        return clean_email;
    }

    public static String cleanAge(String age) {
        //Проверка возраста
		String clean_age = "";
        String reg_exp = "(^(120|1[0-1][0-9]|[2-9][0-9]|1[0-9]|[1-9])){1}";
        Pattern pattern = Pattern.compile(reg_exp);
        Matcher match = pattern.matcher(age);
        if (match.find()) {
            clean_age = age.substring(match.start(), match.end());
        }
        return clean_age;
    }

    public static String cleanName(String name) {
        String clean_name = "";
        //Регулярные выражения для проверки имен
		String reg_exp = "([А-Я]{1}[а-яё]{1,23})?";
        Pattern pattern = Pattern.compile(reg_exp);
        Matcher match = pattern.matcher(name);
        while (match.find()) {
            clean_name += name.substring(match.start(), match.end()) + " ";
        }
        return clean_name.trim();
    }

    public static String cleanNumber(String number) {
        String clean_number;
		//Регулярные выражения для проверки номеров телефонов
        Pattern pattern = Pattern.compile("^\\+\\d{1}");
        Matcher match = pattern.matcher(number);
        int id = 0;
        if (match.find()) {
            String sub = number.substring(match.start(), match.end());
            number = number.replaceFirst("\\" + sub, "");
            clean_number = sub;
            pattern = Pattern.compile("(\\d{1})");
            match = pattern.matcher(number);
            int cant = 0;
            clean_number += " (";
            while (match.find() && cant < 3) {
                sub = number.substring(match.start(), match.end());
                number = number.replaceFirst(sub, "");
                match = pattern.matcher(number);
                clean_number += sub;
                cant++;
            }
            clean_number += ") ";
            if (cant == 3) {
                cant = 0;
                while (match.find() && cant < 7) {
                    sub = number.substring(match.start(), match.end());
                    number = number.replaceFirst(sub, "");
                    match = pattern.matcher(number);
                    clean_number += sub;
                    cant++;
                }
                if (cant != 7) {
                    return "";
                }
            } else {
                return "";
            }
        } else {
            return "";
        }
        return clean_number.trim();
    }

    public static String[] readData(String archive) throws IOException {
        String text;
        String text_arr = "";
        InputStreamReader f = new InputStreamReader(new FileInputStream(archive), StandardCharsets.UTF_8);
        try (BufferedReader b = new BufferedReader(f)) {
            while ((text = b.readLine()) != null) {
                text_arr += text;
            }
        }
        String[] arr = text_arr.trim().split(Pattern.quote("|"));
        return arr;
    }

    public static void writeData(String text, String rute) throws FileNotFoundException, IOException {
        try {
            try (FileOutputStream fos = new FileOutputStream(rute)) {
                OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
                try (BufferedWriter out = new BufferedWriter(osw)) {
                    out.write(text);
                }
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) throws IOException {

        //Чтение исходных данных из нашего файла
        String[] data = readData("C:/Anna_Alex/data/data.txt");

        // Проверка электронной почты
        String email = data[3];
        String regex_email;
        regex_email = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern_email = Pattern.compile(regex_email);
        Matcher mather_email = pattern_email.matcher(email);
        if (!mather_email.find()) {
            email = cleanEmail(email);
        }

        // Проверка имени
        String nombre = data[0];
        String regex_name = "^([А-Я]{1}[а-яё]{1,23})?((\\s)[А-Я]{1}[а-яё]{1,23})?((\\s)[А-Я]{1}[а-яё]{1,23})?$";
        Pattern pattern_name = Pattern.compile(regex_name);
        Matcher mather_name = pattern_name.matcher(nombre);
        if (!mather_name.find()) {
            nombre = cleanName(nombre);
        }

        // Проверка возраста
        String edad = data[1];
        String regex_age = "^([1-9]|120|1[0-9]|[2-9][0-9])$";
        Pattern pattern_age = Pattern.compile(regex_age);
        Matcher mather_age = pattern_age.matcher(edad);
        if (!mather_age.find()) {
            edad = cleanAge(edad);
        }

        // Проверка номера телефона
        String numero = data[2];
        String regex_numer = "^\\+\\d{1,3}\\s?\\(\\d{3}\\)\\s?\\d{3}([-\\s]\\d{2}){2}$";
        Pattern pattern_numer = Pattern.compile(regex_numer);
        Matcher mather_numer = pattern_numer.matcher(numero);
        if (!mather_numer.find()) {
            numero = cleanNumber(numero);
        }
		//Итоговый файл с исправлением
        writeData(nombre + "|" + edad + "|" + email + "|" + numero, "C:/Anna_Alex/data/data_repo.txt");
    }
}
