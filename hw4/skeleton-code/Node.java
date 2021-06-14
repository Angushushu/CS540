import java.util.*;
//
import java.lang.*;

/**
 * Class for internal organization of a Neural Network.
 * There are 5 types of nodes. Check the type attribute of the node for details.
 * Feel free to modify the provided function signatures to fit your own implementation
 */

public class Node {
    private int type = 0; //0=input,1=biasToHidden,2=hidden,3=biasToOutput,4=Output
    public ArrayList<NodeWeightPair> parents = null; //Array List that will contain the parents (including the bias node) with weights if applicable

    private double inputValue = 0.0;
    private double outputValue = 0.0;
    private double outputGradient = 0.0;
    private double delta = 0.0; //input gradient
    
    // stores something might needed
    private double z = 0.0;

    //Create a node with a specific type
    Node(int type) {
        if (type > 4 || type < 0) {
            System.out.println("Incorrect value for node type");
            System.exit(1);

        } else {
            this.type = type;
        }

        if (type == 2 || type == 4) {
            parents = new ArrayList<>();
        }
    }

    //For an input node sets the input value which will be the value of a particular attribute
    public void setInput(double inputValue) {
        if (type == 0) {    //If input node
            this.inputValue = inputValue;
        }
    }

    /**
     * Calculate the output of a node.
     * You can get this value by using getOutput()
     */
    public void calculateOutput() {
        if (type == 2 || type == 4) {   //Not an input or bias node
            // TODO: add code here
        	this.z = 0;
        	for(NodeWeightPair p:parents) {
        		this.z+=p.node.getOutput()*p.weight;
        	}
        	// hidden layer node:
        	if(type == 2) {
//        		for(NodeWeightPair p : parents) {
//        			if(p.node.type == 1 || p.node.type == 3) // bias
//        				this.z += p.weight;
//        			else
//        				this.z += p.weight*p.node.inputValue;
//        		}
            		
        		outputValue = Math.max(0, z);
        	}
        	// output layer node:
        	if(type == 4) {
//        		for(NodeWeightPair p : parents) {
//        			if(p.node.type == 1 || p.node.type == 3) // bias
//        				this.z += p.weight;
//        			else
//        				this.z += p.weight*p.node.outputValue;
//        		}
        		//for(NodeWeightPair p : parents)
            		//this.z += p.weight*p.node.outputValue;
        		// let other function to finish the softmax func
        		outputValue = Math.exp(z);
        	}
        }
    }

    //Gets the output value
    public double getOutput() {

        if (type == 0) {    //Input node
            return inputValue;
        } else if (type == 1 || type == 3) {    //Bias node
            return 1.00;
        } else {
            return outputValue;
        }

    }

    //Calculate the delta value of a node.
    public void calculateDelta() {
        if (type == 2 || type == 4)  {
            // TODO: add code here
            // output layer node
            if(type == 4) {
            	// set delta of hidden nodes
            	for(NodeWeightPair p : parents) {
            		// keep adding wjk*deltak to hidden nodes
            		p.node.setDelta(p.weight*this.delta+p.node.getDelta());
            	}
            }
            // hidden layer node
            if(type == 2) {
            	// g'(zj)*sum <-given by previous layers
            	double gd = this.z <= 0 ? 0 : 1;
            	this.delta = gd*delta; // delta was sum
            }
        }
    }

    //Update the weights between parents node and current node
    public void updateWeight(double learningRate) {
        if (type == 2 || type == 4) {
            // TODO: add code here
        	//parents.forEach(p -> p.weight += learningRate * p.node.getOutput() * delta);
        	for(NodeWeightPair p:parents) {
        		// alpha*ai*deltaj, ai is the output of parents
        		double dweight = learningRate*p.node.getOutput()*delta;
        		p.weight = p.weight + dweight;
        	}
        	this.delta = 0;
        }
        
    }
    
    public void setDelta(double d) {
    	this.delta = d;
    }
    
    public void setOutput(double newoutput) {
    	this.outputValue = newoutput;
    }
    
    public double getDelta() {
    	return delta;
    }
    
    public double getInput() {
    	return inputValue;
    }
}


