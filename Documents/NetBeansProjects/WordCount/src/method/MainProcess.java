package method;

import java.util.Arrays;
import java.util.List;

public class MainProcess {

    public Object[] longestWord(String sentens) {
        String longestWord = "";
        int wordLength = 0;
        int letterCount = 0;

        String[] words = sentens.split("\\s+");
        System.out.println(words.length);
        String[] wordArray = new String[words.length];
        System.arraycopy(words, 0, wordArray, 0, words.length);

        // ArrayList:
        List<String> wordList = Arrays.asList(words);

        System.out.println("Words:");
        for (String word : words) {

            if (wordLength < word.length()) {
                wordLength = word.length();
                longestWord = word;
                letterCount = word.length();
//                System.out.println(word);
            }

        }

        System.out.println(longestWord);
        return new Object[]{words.length, longestWord, letterCount};
    }
}
