import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpression {

	public static void main(String[] args){
		Pattern pattern = Pattern.compile("\\p{Upper}");
		Matcher matcher = pattern.matcher("D");
		System.out.println(matcher.matches());
//		System.out.println(pattern.pattern());
	}

}
