import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Your implementation of a naive bayes classifier. Please implement all four methods.
 */

public class NaiveBayesClassifier implements Classifier {
	
	// maps for later use? -Shu
	List<Instance> trainData;
	Map<Label, Integer> wordcount;
	Map<Label, Integer> doccount;
	Map<Label, Map> jointwordcount;
	int totaldoc;
	int totalword;

    /**
     * Trains the classifier with the provided training data and vocabulary size
     */
    @Override
    public void train(List<Instance> trainData, int v) {
        // TODO : Implement
        // Hint: First, calculate the documents and words counts per label and store them. 
        // Then, for all the words in the documents of each label, count the number of occurrences of each word.
        // Save these information as you will need them to calculate the log probabilities later.
        //
        // e.g.
        // Assume m_map is the map that stores the occurrences per word for positive documents
        // m_map.get("catch") should return the number of "catch" es, in the documents labeled positive
        // m_map.get("asdasd") would return null, when the word has not appeared before.
        // Use m_map.put(word,1) to put the first count in.
        // Use m_map.replace(word, count+1) to update the value
    	totaldoc = trainData.size();
    	totalword = v;
    	this.trainData = trainData;
    	
    	wordcount = getWordsCountPerLabel(trainData);
    	doccount = getDocumentsCountPerLabel(trainData);
    	// For all words in the docs of each label, count the number of occurences of each word
    	jointwordcount = new HashMap<>();
    	Map<String, Integer> wordP = new HashMap<>();
    	Map<String, Integer> wordN = new HashMap<>();
    	for(Instance i : trainData) {
    		if(i.label == Label.POSITIVE) {
    			for(String w : i.words) {
    				if(wordP.containsKey(w)) {
    					wordP.replace(w, wordP.get(w)+1);
    				} else {
    					wordP.put(w, 1);
    				}
    			}
    		} else {
    			for(String w : i.words) {
    				if(wordN.containsKey(w)) {
    					wordN.replace(w, wordN.get(w)+1);
    				} else {
    					wordN.put(w, 1);
    				}
    			}
    		}
    	}
    	jointwordcount.put(Label.POSITIVE, wordP);
    	jointwordcount.put(Label.NEGATIVE, wordN);
    	
    }

    /*
     * Counts the number of words for each label
     */
    @Override
    public Map<Label, Integer> getWordsCountPerLabel(List<Instance> trainData) {
        // TODO : Implement
    	Map<Label, Integer> hm = new HashMap<Label, Integer>();
    	int wordnumofN = 0, wordnumofP = 0;
    	for(Instance i : trainData) {
    		if(i.label == Label.NEGATIVE) wordnumofN += i.words.size();
    		else wordnumofP += i.words.size();
    	}
    	hm.put(Label.NEGATIVE, wordnumofN);
    	hm.put(Label.POSITIVE, wordnumofP);
        return hm;
    }


    /*
     * Counts the total number of documents for each label
     */
    @Override
    public Map<Label, Integer> getDocumentsCountPerLabel(List<Instance> trainData) {
        // TODO : Implement
    	Map<Label, Integer> hm = new HashMap<Label, Integer>();
    	int docnumofN = 0, docnumofP = 0;
    	for(Instance i : trainData) {
    		if(i.label == Label.NEGATIVE) docnumofN ++;
    		else docnumofP ++;
    	}
    	hm.put(Label.NEGATIVE, docnumofN);
    	hm.put(Label.POSITIVE, docnumofP);
        return hm;
    }


    /**
     * Returns the prior probability of the label parameter, i.e. P(POSITIVE) or P(NEGATIVE)
     */
    private double p_l(Label label) {
        // TODO : Implement
        // Calculate the probability for the label. No smoothing here.
        // Just the number of label counts divided by the number of documents.
        return ((double)doccount.get(label))/totaldoc;
    }

    /**
     * Returns the smoothed conditional probability of the word given the label, i.e. P(word|POSITIVE) or
     * P(word|NEGATIVE)
     */
    private double p_w_given_l(String word, Label label) {
        // TODO : Implement
        // Calculate the probability with Laplace smoothing for word in class(label)
    	// if no word in jointwordcount?
    	int numofword = 0;
    	if(jointwordcount.get(label).containsKey(word))
    		numofword = (int) jointwordcount.get(label).get(word);
    	double result = ((double)numofword+1)/(totalword+wordcount.get(label));
    	//System.out.println("result: "+result);  // always get 0
        return result;
    }

    /**
     * Classifies an array of words as either POSITIVE or NEGATIVE.
     */
    @Override
    public ClassifyResult classify(List<String> words) {
        // TODO : Implement
        // Sum up the log probabilities for each word in the input data, and the probability of the label
        // Set the label to the class with larger log probability
    	double pP = Math.log(p_l(Label.POSITIVE)), pN = Math.log(p_l(Label.NEGATIVE));
    	Map<Label, Double> logProbPerLabel = new HashMap<>();
    	ClassifyResult result = new ClassifyResult();
    	for(String word : words) {
    		//if(jointwordcount.get(Label.POSITIVE).containsKey(word))
    		pP += Math.log(p_w_given_l(word, Label.POSITIVE));
    		//if(jointwordcount.get(Label.NEGATIVE).containsKey(word))
    		pN += Math.log(p_w_given_l(word, Label.NEGATIVE));
    	}
    	logProbPerLabel.put(Label.POSITIVE, pP);
    	logProbPerLabel.put(Label.NEGATIVE, pN);
    	if(pP >= pN) result.label = Label.POSITIVE;
    	else result.label = Label.NEGATIVE;
    	result.logProbPerLabel = logProbPerLabel;
        return result;
    }


}
