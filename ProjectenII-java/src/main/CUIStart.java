package main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Formatter;

public class CUIStart {

	public static void main(String[] args) {

//		DomeinController dc = new DomeinController();
//		dc.filtreerSessies("c");
//		FilteredList<Sessie> kalender = dc.geefSessies();

//		System.out.println("Sessies:");
//		kalender.forEach(s -> System.out.println(s.getTitel()));

//		List<Sessie> sessies = dc.geefSessies();
//
//		sessies	.stream()
//				.forEach(s -> System.out.println(s.toString()));

//		Desktop desktop;
//		if (Desktop.isDesktopSupported() && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL)) {
//			try {
//				String mailTo = "test@domain.com"; // deze moet ingevuld zijn
//				String cc = "test2@domain.com;test3@domain.com"; // hier moeten ;s tussen
//				String subject = changeToURICode("first Email");
//				String body = changeToURICode("the java message \nazerazerazer \n\nazerazer\n");
//
//				final String mailURIStr = String.format("mailto:%s?subject=%s&cc=%s&body=%s", mailTo, subject, cc, body);
//				final URI mailURI = new URI(mailURIStr);
//
//				desktop.mail(mailURI);
//			} catch (Exception e) {
//				System.out.println(e.getMessage());
//			}
//		} else {
//			// TODO fallback to some Runtime.exec(..) voodoo?
//			throw new RuntimeException("desktop doesn't support mailto; mail is dead anyway ;)");
//		}
//
//		if (Desktop.isDesktopSupported()) {
//			try {
//				Desktop	.getDesktop()
//						.browse(new URI("https://powerbi.microsoft.com/"));
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			} catch (URISyntaxException e1) {
//				e1.printStackTrace();
//			}
//		}

		File directory = new File("Statistieken/Sessies");
		directory.mkdirs();

		LocalDate now = LocalDate.now();
		String dateStr = String.format("%d-%d-%d", now.getYear(), now.getMonthValue(), now.getDayOfMonth());

		try (Formatter output = new Formatter(Files.newOutputStream(
			Paths.get(String.format("Statistieken/Sessies/%s-%s.txt", "test", dateStr))))) {

			output.format(String.format("Statistieken voor sessie %s: %n%n", "titel"));
			output.format("%-30s%s%n", "Sessietitel:", "titel");
			output.format("%-30s%s%n", "Verantwoordelijke:", "verantwoordelijke");
			output.format("%-30s%s%n", "Start:", "startuur");
			output.format("%-30s%s%n", "Einde:", "einduur");
			output.format("%-30s%s%n", "Inschrijvingen:", "50");
			output.format("%-30s%s%n", "Aanwezigheden:", "30");
			output.format("%-30s%s%n", "Percentueel:", "60%");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
//
//	private static String changeToURICode(String subject) {
//		return subject	.replaceAll(" ", "%20")
//						.replaceAll("\n", "%0D"); // in de URI moeten de spaties in %20 veranderen
//	}
}
