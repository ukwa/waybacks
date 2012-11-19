/**
 * 
 */
package uk.bl.wa.wayback;

import java.text.DateFormatSymbols;
import java.util.Locale;

/**
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class LocaleUtils {

	/**
	 * Example showing how to pick the months up from the Locale.
	 * NOT CURRENTLY IN USE.
	 * 
	 * @return
	 */
	public static String[] listMonthsShort() {
		String[] shortMonths = new DateFormatSymbols().getShortMonths();
        for (int i = 0; i < shortMonths.length; i++) {
            String shortMonth = shortMonths[i];
            System.out.println("shortMonth = " + shortMonth);
        }
        return shortMonths;
	}
	
	
	public static void main(String[] args) {
		String[] months = new DateFormatSymbols().getMonths();
		for (int i = 0; i < months.length; i++) {
			String month = months[i];
			System.out.println("month = " + month);
		}

		String[] shortMonths = new DateFormatSymbols().getShortMonths();
		for (int i = 0; i < shortMonths.length; i++) {
			String shortMonth = shortMonths[i];
			System.out.println("shortMonth = " + shortMonth);
		}

		String[] germanyMonths = new DateFormatSymbols(Locale.GERMANY).getMonths();
		for (int i = 0; i < germanyMonths.length; i++) {
			String germanyMonth = germanyMonths[i];
			System.out.println("germanyMonth = " + germanyMonth);
		}

		String[] germanyShortMonths = new DateFormatSymbols(Locale.GERMANY).getShortMonths();
		for (int i = 0; i < germanyShortMonths.length; i++) {
			String germanyShortMonth = germanyShortMonths[i];
			System.out.println("germanyShortMonth = " + germanyShortMonth);
		}
	}
	
}
