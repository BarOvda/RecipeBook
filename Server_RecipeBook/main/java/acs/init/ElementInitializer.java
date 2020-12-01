package acs.init;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import acs.boundary.ElementBoundary;
import acs.logic.EnchancedElementService;

@Component
@Profile("manualTests")
public class ElementInitializer implements CommandLineRunner {
	
	private EnchancedElementService elementService;

	public ElementInitializer() {
	}

	
	@Autowired
	public void setElementService(EnchancedElementService elementService) {
		
		this.elementService = elementService;
		
	}

	
	@Override
	public void run(String... args) throws Exception {
		List<ElementBoundary> allElement = IntStream.range(0, 3).mapToObj(i -> {
			ElementBoundary newElement = new ElementBoundary();
			newElement.setName("name" + i);

			return newElement;
		}).map(element -> this.elementService.create("a", element)).peek(System.err::println)
				.collect(Collectors.toList());

		this.elementService.addChildElementToElement("a", allElement.get(0).getElementId(),
				allElement.get(1).getElementId());

		this.elementService.addChildElementToElement("a", allElement.get(0).getElementId(),
				allElement.get(2).getElementId());

	}
}
