package domein;

import javafx.collections.transformation.FilteredList;

public class VerantwoordelijkeDomeinController extends HoofdDomeinController {

	public VerantwoordelijkeDomeinController(InlogController lg) {
		super(lg);

	}

	@Override
	public FilteredList<Sessie> geefSessies() {
		return super.geefSessies().filtered(s -> s	.getVerantwoordelijkeProperty()
													.getValue()
													.equals(super.ingelogdegebGebruiker.getNaam()));
	}

}
