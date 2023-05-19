package brokerpolicy.genetic;

import java.text.DecimalFormat;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Random;

//import genetic_algorithm.Constants;

//import genetic_algorithm.Constants;

public class GeneticAlgorithm {

	private static int reqTasks = 5;
	private static int reqVms = 2;
	private static int size = 10;	// initial population size
	
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
	
	public double fitness_function(List<Integer> genome) {
		CloudSimSimulation obj = new CloudSimSimulation(reqTasks, reqVms, genome);
		double totalExecTime = obj.runSimulation();
		System.out.println("====== total time taken ==========");
		DecimalFormat dft = new DecimalFormat("###.##");
		System.out.println(dft.format(totalExecTime));
		return totalExecTime;
	}
	
	// Roulette wheel selection strategy
	public List<List<Integer>> selection_pair(List<List<Integer>> population, List<Double> fitnessValue) {
		
		List<List<Integer>> selection = new ArrayList<>();
		double totalFitnessSum = fitnessValue.stream().mapToDouble(Double::doubleValue).sum();
		System.out.println("totalFitnessSum is " + totalFitnessSum);
		
		/* List<Double> probability = new ArrayList<>();
		for (int i = 0; i < fitnessValue.size(); i++) {
			probability.add(fitnessValue.get(i) / totalFitnessSum);
		}
		System.out.println("Probability list : " + probability.toString()); */
		
		/** selecting 2 parents for next generation */
		Random random = new Random();
		for (int i = 0; i < 2; i++) {								
			double randomValue = totalFitnessSum * random.nextDouble();
            System.out.println("randomValue is " + randomValue);
            
            double valueSum = 0;
            for (int j = 0; j < population.size(); j++) {
            	valueSum += fitnessValue.get(j);
                if (valueSum >= randomValue) {
                	System.out.println("Selected Parent : " + population.get(j).toString());
                    selection.add(population.get(j));
                    break;
                }
            }
        }
		return selection;
	}
	
	public List<List<Integer>> single_point_crossover(List<Integer> genomeA, List<Integer> genomeB) {
		List<List<Integer>> genomePair = new ArrayList<>();
		
		/** Cannot divide the genome */
		if (genomeA.size() < 2) {
			genomePair.add(genomeA);
			genomePair.add(genomeB);
		
		} else {
			int genome_size = genomeA.size();
			Random rand = new Random();
			int p = rand.nextInt(genome_size);
			
			List<Integer> newGenomeA = new ArrayList<>();
			newGenomeA.addAll(genomeA.subList(0, p));
			newGenomeA.addAll(genomeB.subList(p, genome_size));
			
			genomePair.add(newGenomeA);
			
			List<Integer> newGenomeB = new ArrayList<>();
			newGenomeB.addAll(genomeB.subList(0, p));
			newGenomeB.addAll(genomeA.subList(p, genome_size));
			
			genomePair.add(newGenomeB);
			
		}
		return genomePair;
	}
	
	/** There are many mutation strategy - swap, insertion, inversion, scramble and etc.
	 * 	We chose to insert randomVm in random index */
	public List<Integer> mutation(List<Integer> genome, Optional<Integer> num, Optional<Float> probability) {
		
		Random rand = new Random();
		for (int i = 0; i < num.orElse(1); i++) {
			System.out.println(genome.size());
			int index = rand.nextInt(genome.size());
			System.out.println("random index is " + index);
			int randomVm = rand.nextInt(reqVms);
			System.out.println("randomVM is " + randomVm);
			if (rand.nextDouble() < (probability.orElse((float)0.5))) {
				System.out.println("mutating............. ");
				genome.set(index, randomVm);
			}
		}
		return genome;
	}
	
	public List<Double> calculatePopulationFitness(List<List<Integer>> population) {
		List<Double> fitnessValues = new ArrayList<>();
		for(List<Integer> genome: population) {
			fitnessValues.add(fitness_function(genome)); 
		}
		return fitnessValues;
	}
	
	public void printPopulation(List<List<Integer>> population) {
		for(List<Integer> genome: population) {
			System.out.println(genome.toString());
		}
	}
	
	public List<List<Integer>> run_evolution(int generation_limit) {
		List<List<Integer>> currentPopulation = new ArrayList<>();
		List<List<Integer>> result = new ArrayList<>();
		
		// population size 10 : 10 randomly generated genomes */
		currentPopulation.addAll(generate_population(size, reqTasks, reqVms));
		
		System.out.println("=========== Printing current population =========== ");
		printPopulation(currentPopulation);
		
		for (int i = 0; i < generation_limit; i++) {
			
			List<Double> fitnessValue = calculatePopulationFitness(currentPopulation);
			Map<List<Integer>, Double> populationFitnessMap = new LinkedHashMap<>();
			
			for (int j = 0; j < fitnessValue.size(); j++) {
				populationFitnessMap.put(currentPopulation.get(j), fitnessValue.get(j));
			}
			System.out.println("Population size is " + populationFitnessMap.size());
			
			// sort current population based on fitness score in ascending order */
			List<Map.Entry<List<Integer>, Double> > sortList = new ArrayList<>(populationFitnessMap.entrySet());
			
			Collections.sort(sortList, new Comparator<Map.Entry<List<Integer>, Double>>() {
				@Override
				public int compare(Map.Entry<List<Integer>, Double> entry1, Map.Entry<List<Integer>, Double> entry2) {
					return Double.compare(entry1.getValue(), entry2.getValue());
				}
			});
			
			currentPopulation.clear();
			for(int j = 0; j < sortList.size(); j++) {
				currentPopulation.add(sortList.get(j).getKey());
			}
			result.addAll(currentPopulation);
			
			System.out.println("===== after sorting ======");
			System.out.println(currentPopulation.get(0).toString());
			System.out.println(currentPopulation.get(1).toString());
			System.out.println("generation : " + i);
			
			
			//List<List<Integer>> parents = selection_pair(currentPopulation, fitnessValue);
			//print_population(currentPopulation);
			
			List<List<Integer>> nextGeneration = currentPopulation.subList(0, 2);
			
			System.out.println("currentPopulation size is " + currentPopulation.size());
			int totalLoops = currentPopulation.size() / 2 ;
			System.out.println("totalLoops are " + totalLoops);
			
			for(int j = 0; j < (totalLoops - 1); j++) {
				
				System.out.println("generating nextGen with j value : " + j);
				// select 2 parents from currentPopulation
				List<List<Integer>> parents = selection_pair(currentPopulation, fitnessValue);
				System.out.println("parents obtained");
				
				//System.out.println("====== printing parents ========");
				//print_population(parents);
				
				// obtain two offsprings from parents
				System.out.println("executing single_point_crossover for offsprings ");
				List<List<Integer>> offsprings = single_point_crossover(parents.get(0), parents.get(1));
				System.out.println("offsprings obtained");
				
				System.out.println("====== printing offsprings ========");
				printPopulation(offsprings);
				
				// mutation 1
				offsprings.set(0, mutation(offsprings.get(0), Optional.of(1), Optional.of(0.5f)));
				System.out.println("first mutation done");
				System.out.println("after 1st mutation : " + String.join(", ", offsprings.get(0).toString()));
				
				// mutation 2
				offsprings.set(1, mutation(offsprings.get(1), Optional.of(1), Optional.of(0.5f)));
				System.out.println("second mutation done");
				System.out.println("after 2nd mutation : " + String.join(", ", offsprings.get(1).toString()));
				
				// next generation
				nextGeneration.add(offsprings.get(0));
				nextGeneration.add(offsprings.get(1));
			}
			
			System.out.println("nextGeneration obtained");
			List<List<Integer>> newGeneration = new ArrayList<>();
			newGeneration.addAll(nextGeneration);
			currentPopulation.clear();
			currentPopulation.addAll(newGeneration);  
		}
		printPopulation(result);
		return result;
	}
	
	public static void main(String[] args) {
		GeneticAlgorithm obj = new GeneticAlgorithm();
		
		int generation_limit = 1;
		obj.run_evolution(generation_limit);
		
	}
}