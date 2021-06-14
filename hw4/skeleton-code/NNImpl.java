import java.util.*;

/**
 * The main class that handles the entire network
 * Has multiple attributes each with its own use
 */

public class NNImpl {
    private ArrayList<Node> inputNodes; //list of the output layer nodes.
    private ArrayList<Node> hiddenNodes;    //list of the hidden layer nodes
    private ArrayList<Node> outputNodes;    // list of the output layer nodes

    private ArrayList<Instance> trainingSet;    //the training set

    private double learningRate;    // variable to store the learning rate
    private int maxEpoch;   // variable to store the maximum number of epochs
    private Random random;  // random number generator to shuffle the training set
    
    // record the output(O)
    private ArrayList<Double> output;

    /**
     * This constructor creates the nodes necessary for the neural network
     * Also connects the nodes of different layers
     * After calling the constructor the last node of both inputNodes and
     * hiddenNodes will be bias nodes.
     */

    NNImpl(ArrayList<Instance> trainingSet, int hiddenNodeCount, Double learningRate, int maxEpoch, Random random, Double[][] hiddenWeights, Double[][] outputWeights) {
        this.trainingSet = trainingSet;
        this.learningRate = learningRate;
        this.maxEpoch = maxEpoch;
        this.random = random;

        //input layer nodes
        inputNodes = new ArrayList<>();
        int inputNodeCount = trainingSet.get(0).attributes.size();
        int outputNodeCount = trainingSet.get(0).classValues.size();
        for (int i = 0; i < inputNodeCount; i++) {
            Node node = new Node(0);
            inputNodes.add(node);
        }

        //bias node from input layer to hidden
        Node biasToHidden = new Node(1);
        inputNodes.add(biasToHidden);

        //hidden layer nodes
        hiddenNodes = new ArrayList<>();
        for (int i = 0; i < hiddenNodeCount; i++) {
            Node node = new Node(2);
            //Connecting hidden layer nodes with input layer nodes
            for (int j = 0; j < inputNodes.size(); j++) {
                NodeWeightPair nwp = new NodeWeightPair(inputNodes.get(j), hiddenWeights[i][j]);
                node.parents.add(nwp);
            }
            hiddenNodes.add(node);
        }

        //bias node from hidden layer to output
        Node biasToOutput = new Node(3);
        hiddenNodes.add(biasToOutput);

        //Output node layer
        outputNodes = new ArrayList<>();
        for (int i = 0; i < outputNodeCount; i++) {
            Node node = new Node(4);
            //Connecting output layer nodes with hidden layer nodes
            for (int j = 0; j < hiddenNodes.size(); j++) {
                NodeWeightPair nwp = new NodeWeightPair(hiddenNodes.get(j), outputWeights[i][j]);
                node.parents.add(nwp);
            }
            outputNodes.add(node);
        }
    }

    /**
     * Get the prediction from the neural network for a single instance
     * Return the idx with highest output values. For example if the outputs
     * of the outputNodes are [0.1, 0.5, 0.2], it should return 1.
     * The parameter is a single instance
     */

    public int predict(Instance instance) {
        // TODO: add code here
    	// set input values
    	forward_pass(instance); // piazza
    	
    	// get index of the largest one
    	double max = 0.0;
    	int maxi = 0;
    	for(int i = 0; i < instance.classValues.size(); i++) {max = Math.max(outputNodes.get(i).getOutput(), max);
    		if(max == outputNodes.get(i).getOutput())
    			maxi = i;
    	}
    	return maxi;
    }


    /**
     * Train the neural networks with the given parameters
     * <p>
     * The parameters are stored as attributes of this class
     */

    public void train() {
        // TODO: add code here
    	// update weights for maxEpoch times
    	//System.out.println("train()"); //
    	for(int i = 0; i < maxEpoch; i++) {
    		//System.out.println("epoch "+i); //
    		// 1 epoch
    		double totL = 0.0;
    		Collections.shuffle(trainingSet, random); // description
    		for(Instance ex:trainingSet) {
    			forward_pass(ex); // get O for E
//    			for(Node n:outputNodes) {
//                	System.out.println("my-O: "+n.getOutput());
//                }
    			// Back propagation
    			// calc delta for output units
    	    	for(int j = 0; j < ex.classValues.size(); j++) //
    	    		outputNodes.get(j).setDelta(ex.classValues.get(j) - outputNodes.get(j).getOutput());
    	    	// pass sum to hidden units
    	    	for(Node n:outputNodes) {
    	    		n.calculateDelta();
    	    		n.updateWeight(learningRate); // update weights
    	    	}
    	    	// use sum to calc delta
    	    	for(Node n:hiddenNodes) {
    	    		n.calculateDelta();
    	    		n.updateWeight(learningRate); // update weights
    	    	}
    		}
    		//test
//    		for(Node n:hiddenNodes) {
//    			for(NodeWeightPair p:n.parents) {
//    				System.out.println(p.weight);
//    			}
//    		}
    		for(Instance ex:trainingSet)
    			totL += loss(ex);
    		totL /= trainingSet.size();
    		System.out.printf("Epoch: %d, Loss: %.3e\n", i, totL); // description
    	}
    }

    /**
     * Calculate the cross entropy loss from the neural network for
     * a single instance.
     * The parameter is a single instance
     */
    private double loss(Instance instance) {
        // TODO: add code here
    	double l = 0.0;
    	forward_pass(instance); // piazza
    	
    	// for each output node, calc Error
    	for(int i = 0; i < outputNodes.size(); i++) {
    		double y = instance.classValues.get(i);
    		double lng = Math.log(outputNodes.get(i).getOutput()); // output is g(z)
    		l -= y*lng;
    	}
    	return l;
        //return -1.0;
    }
    
    // use current weight go forward and get output
    private void forward_pass(Instance ex) {
    	for(int i = 0; i < ex.attributes.size(); i++) // the last one is bias
    		inputNodes.get(i).setInput(ex.attributes.get(i));
    	// get hidden layer values
    	for(Node h:hiddenNodes)
    		h.calculateOutput();
    	// get output layer values
    	for(Node o:outputNodes)
    		o.calculateOutput();
    	// getting sum of e^z
    	double sum = 0.0;
    	for(Node o:outputNodes)
    		sum += o.getOutput();
    	// set the real output values
    	for(Node o:outputNodes)
    		o.setOutput(o.getOutput()/sum);
    }
}
