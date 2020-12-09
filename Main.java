import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        File file = new File("CFG-2020.txt"); // Read in file
        Scanner in = new Scanner(file); // Set scanner to read file

        HashMap<Character, ProductionRule> productionRules = new HashMap<>();
        HashSet<Character> terminals = new HashSet<>();

        String V = in.nextLine(); // Read in variables
        for (int i = 3; i < V.length(); i += 3) {
            if (Character.isUpperCase(V.charAt(i))) { // Non-terminals
                productionRules.put(V.charAt(i), new ProductionRule(V.charAt(i)));
            }
        }

        String T = in.nextLine(); // Read in terminals
        for (int i = 3; i < T.length(); i += 3) {
            terminals.add(T.charAt(i));
        }

        Character start = in.nextLine().charAt(3); // Gets start rule

        in.nextLine(); // 4th line of input is not needed for this implementation

        while (in.hasNextLine()) { // Read in all productions
            String rule = in.nextLine();
            ProductionRule productionRule = productionRules.get(rule.charAt(0));

            for (int i = 4; i < rule.length(); i++) {
                char c = rule.charAt(i);
                if (Character.isUpperCase(c)) { // Production rule points to another production rule
                    ProductionRule out = productionRules.get(c);
                    out.addIn(productionRule);                    // Link productions bidirectionally
                    productionRule.addOut(out);                   //
                }
            }

            productionRule.addProduction(rule);

            if (productionRule.getSymbol() == start) {
                productionRule.setStartSymbol();
            }
        }


        ProductionUtils.removeUnreachable(productionRules);
        ProductionUtils.removeUnproductive(productionRules);
        ProductionUtils.removeEpsilonProductions(productionRules);


        /**
         * Write simplified grammar to output file
         */
        File out = new File("out.txt");
        FileWriter fw = new FileWriter(out);

        String v = "";
        for (Character c : productionRules.keySet()) {
            v += c + ", ";
        }

        HashSet<Character> updatedTerminals = ProductionUtils.getUpdatedTerminals(productionRules);

        String t = "";
        for (char c : updatedTerminals) {
            t += c + ", ";
        }

        fw.write("V:: " + v + t + "\n");
        fw.write("T:: " + t + "\n");
        fw.write("S:: " + start + "\n");
        fw.write("P:: " + "\n");

        String p = "";
        for (ProductionRule productionRule : productionRules.values()) {
            p += productionRule.toString();
        }

        fw.write(p + "\n");
        fw.close();

    }
}
