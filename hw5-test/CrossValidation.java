import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CrossValidation {
    /*
     * Returns the k-fold cross validation score of classifier clf on training data.
     */
    public static double kFoldScore(Classifier clf, List<Instance> trainData, int k, int v) {
        // TODO : Implement
    	// work on k parts of trainData
    	List<Double> scores = new ArrayList<>();
    	
    	for(int j = 0; j < k; j++) {
    		List<Instance> subtrainData = new ArrayList<>();
    		List<Instance> testData = new ArrayList<>();
    		subtrainData.addAll(trainData);
    		for(int i = 0; i < (trainData.size()/k); i++)
    			testData.add(trainData.get(j*(trainData.size()/k)+i));
    		for(Instance ex : testData)
    			subtrainData.remove(ex);
    		
    		//
//    		Set<String> wordset = new HashSet<String>();
//    		for(Instance i:subtrainData) {
//    			for(String s:i.words)
//    				wordset.add(s);
//    		}
    		
    		clf.train(subtrainData, v);//wordset.size());  // v?
    		
    		// get score
    		int numcorrect = 0;
        	for(Instance i : testData) {
        		ClassifyResult res = clf.classify(i.words);
        		if(res.label == i.label)
        			numcorrect++;
        	}
        	scores.add(((double)numcorrect)/testData.size());
    	}
    	// get avg score
    	double avg = 0.0;
    	for(int i = 0; i < scores.size(); i++) {
    		avg += scores.get(i);
    	}
    	avg = (double)avg/k;
        return avg;
    }
}
