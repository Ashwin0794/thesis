package brokerpolicy.genetic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TestBroker {
	
	// genomeLength : reqTasks
	public List<Integer> generate_genome(int genomeLength, int reqVms) {
			List<Integer> genome = new ArrayList<>();
			Random rand = new Random();
			for (int k = 0; k < genomeLength; k++) {
				genome.add(rand.nextInt(reqVms));
			}
			return genome;
		}
		
	public List<List<Integer>> generate_population(int size, int genomeLength, int reqVms) {
			List<List<Integer>> population = new ArrayList<>();
			for (int i = 0; i < size; i++) {
				population.add(generate_genome(genomeLength, reqVms));
			}
			return population;
		}

	public static void main(String[] args) {
		System.out.println(" hello ");
		TestBroker broker = new TestBroker();

		List<List<Integer>> population = broker.generate_population(20, 5,3);
		for(List<Integer> genome : population) {
			System.out.println("genome : " + Arrays.toString(genome.toArray()));
		}	
	}
}
