import java.util.*;

public class ProductionUtils {

    /**
     * Removes unreachable productions from the grammar
     *
     * @param rules map of non-terminals to their ProductionRule
     */
    public static void removeUnreachable(HashMap<Character, ProductionRule> rules) {
        for (Iterator<ProductionRule> it = rules.values().iterator(); it.hasNext(); ) {
            ProductionRule productionRule = it.next();
            if (productionRule.isUnreachable()) {
                it.remove();
            }
        }
    }

    /**
     * Removes unproductive productions from the grammar
     *
     * @param rules map of non-terminals to their ProductionRule
     */
    public static void removeUnproductive(HashMap<Character, ProductionRule> rules) {
        List<Character> nonTerminalsToBeRemoved = new LinkedList<>();
        for (Iterator<ProductionRule> it = rules.values().iterator(); it.hasNext(); ) {
            ProductionRule productionRule = it.next();
            for (ProductionRule mapsTo : productionRule.out) {
                if (mapsTo.isUnproductive()) {
                    for (String production : productionRule.productions) {
                        if (production.contains(mapsTo.getSymbol() + "")) {
                            productionRule.productions.remove(production);
                        }
                    }
                    productionRule.out.remove(mapsTo.getSymbol());
                    nonTerminalsToBeRemoved.add(mapsTo.getSymbol());
                }
            }
        }

        for (Character c : nonTerminalsToBeRemoved) {
            rules.remove(c);
        }
    }

    /**
     * Removes epsilon productions from the grammar and updates associated productions
     *
     * @param rules map of non-terminals to their ProductionRule
     */
    public static void removeEpsilonProductions(HashMap<Character, ProductionRule> rules) {
        for (Iterator<ProductionRule> outer = rules.values().iterator(); outer.hasNext(); ) {
            ProductionRule productionRule = outer.next();
            if (productionRule.containsEpsilon()) {
                for (Iterator<ProductionRule> inner = rules.values().iterator(); inner.hasNext(); ) {
                    ProductionRule mapsToProductionRule = inner.next();
                    LinkedList<String> productionsToBeAdded = new LinkedList<>();
                    for (Iterator<String> prods = mapsToProductionRule.productions.iterator(); prods.hasNext(); ) {
                        String s = prods.next();
                        if (s.contains(productionRule.getSymbol() + "")) {
                            productionsToBeAdded.add(s.replace(productionRule.getSymbol(), ' '));
                        }
                    }
                    for (String s : productionsToBeAdded) {
                        mapsToProductionRule.productions.add(s);
                    }
                }
                productionRule.productions.remove("$");
            }
        }
    }


    /**
     * Finds the set of all terminals in the simplified grammar
     *
     * @param rules map of non-terminals to their ProductionRule
     * @return set of all terminals
     */
    public static HashSet<Character> getUpdatedTerminals(HashMap<Character, ProductionRule> rules) {
        HashSet<Character> updatedTerminals = new HashSet<>();
        for (Iterator<ProductionRule> it = rules.values().iterator(); it.hasNext(); ) {
            ProductionRule productionRule = it.next();
            for (String production : productionRule.productions) {
                for (char c : production.toCharArray()) {
                    if (Character.isLowerCase(c)) { // Terminal character
                        updatedTerminals.add(c);
                    }
                }
            }
        }
        return updatedTerminals;
    }
}
