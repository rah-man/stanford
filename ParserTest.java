
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

public class ParserTest {

    /**
     * This example shows a few more ways of providing input to a parser.
     * <p>
     * Usage: ParserDemo2 [grammar [textFile]]
     */
    public static void main(String[] args) throws IOException {

        /*
        edu/stanford/nlp/models/lexparser/englishFactored.ser.gz
        edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz
        edu/stanford/nlp/models/lexparser/englishRNN.ser.gz
        */

        String[] grammars = {
//                "edu/stanford/nlp/models/lexparser/englishFactored.ser.gz"
//                "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz"
                "edu/stanford/nlp/models/lexparser/wsjRNN.ser.gz"
        };

        for (String grammar : grammars) {
            processText(grammar, args[0]);
        }
    }

    private static void processText(String grammar, String filePath) {
        System.out.println("GRAMMAR: " + grammar + "\n");
        String[] options = {"-maxLength", "80", "-retainTmpSubcategories"};
        LexicalizedParser lp = LexicalizedParser.loadModel(grammar);
        TreebankLanguagePack tlp = lp.getOp().langpack();
        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();

        Iterable<List<? extends HasWord>> sentences;

        DocumentPreprocessor dp = new DocumentPreprocessor(filePath);
        List<List<? extends HasWord>> tmp = new ArrayList<>();
        for (List<HasWord> sentence : dp) {
            tmp.add(sentence);
        }
        sentences = tmp;


        for (List<? extends HasWord> sentence : sentences) {
            Tree parse = lp.parse(sentence);
            parse.pennPrint();
            System.out.println();
            GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
            List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
            System.out.println(tdl);
            System.out.println();

//            System.out.println("The words of the sentence:");
//            for (Label lab : parse.yield()) {
//                if (lab instanceof CoreLabel) {
//                    System.out.println(((CoreLabel) lab).toString(CoreLabel.OutputFormat.VALUE_MAP));
//                } else {
//                    System.out.println(lab);
//                }
//            }
//            System.out.println();
            System.out.println(parse.taggedYield());
            System.out.println();

        }
    }

    private ParserTest() {
    } // static methods only

}
