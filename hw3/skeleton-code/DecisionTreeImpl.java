import java.util.ArrayList;
import java.util.List;

/**
 * Fill in the implementation details of the class DecisionTree using this file. Any methods or
 * secondary classes that you want are fine but we will only interact with those methods in the
 * DecisionTree framework.
 */
public class DecisionTreeImpl {
	public DecTreeNode root;
	public List<List<Integer>> trainData;
	public int maxPerLeaf;
	public int maxDepth;
	public int numAttr;

	// Build a decision tree given a training set
	DecisionTreeImpl(List<List<Integer>> trainDataSet, int mPerLeaf, int mDepth) {
		this.trainData = trainDataSet;
		this.maxPerLeaf = mPerLeaf;
		this.maxDepth = mDepth;
		if (this.trainData.size() > 0) this.numAttr = trainDataSet.get(0).size() - 1;
		this.root = buildTree();
	}
	
	private DecTreeNode buildTree() {
		// TODO: add code here	
		int default_label = getMajorLabel(trainData);
		List<Integer> attrs = new ArrayList<Integer>();
		for(int i = 0; i < numAttr; i++) {
			attrs.add(i);
		}
		return buildTree(trainData, attrs, default_label, 0);
	}
	
	private DecTreeNode buildTree(List<List<Integer>> examples, List<Integer> attrs, int majorClass, int depth) {
		DecTreeNode retNode = new DecTreeNode(0, 0, 0);
		// if empty(examples)
		if(examples.isEmpty()) {
			retNode.classLabel = majorClass; // default-label
			return retNode;
		// if examples all have same label
		}else if(allSameLabel(examples)) {
			retNode.classLabel = examples.get(0).get(numAttr);
			return retNode;
		// if empty(attrib) ?
		}else if(numAttr == 0) {
			retNode.classLabel = getMajorLabel(examples);
			return retNode;
		}
		
		// if depth == maxdepth
		if(depth >= maxDepth) {
			retNode.classLabel = getMajorLabel(examples);
			return retNode;
		}
		
		// if the number of instances belonging to that node is ?less? than or equal to the "maximum instances per leaf"
		if(examples.size() <= maxPerLeaf) {
			retNode.classLabel = getMajorLabel(examples);
			return retNode;
		}
		
		// get best attribute
		int best_attribute = 0;
		int threshold = 0;
		double highest_infogain = 0;
		for(int attr:attrs) {
			for(int v = 1; v <= 9; v++) {
				double tmpinfogain = calcInfoGainOfV(examples, attr, v);
				if(tmpinfogain > highest_infogain) {
					highest_infogain = tmpinfogain;
					best_attribute = attr;
					threshold = v;
				}
			}
		}
		
		//  if the maximum information gain is 0
		if(highest_infogain == 0) {
			retNode.classLabel = getMajorLabel(examples);
			return retNode;
		}
		
		retNode.attribute = best_attribute;  // create a tree
		retNode.threshold = threshold;
		List<List<Integer>> vex = getvex(best_attribute, threshold, examples);
		List<List<Integer>> nonvex = getnonvex(best_attribute, threshold, examples);
		retNode.left = buildTree(vex, attrs, getMajorLabel(examples), depth+1);
		retNode.right = buildTree(nonvex, attrs, getMajorLabel(examples), depth+1);
		return retNode;
	}
	
	// based on description
	private double calcInfoGainOfV(List<List<Integer>> examples, int attr, int v) {
		double exsize = (double)examples.size();
		double numlab0 = (double)getni(0, examples);
		double numlab1 = (double)getni(1, examples);
		List<List<Integer>> vse = getvex(attr, v, examples);
		List<List<Integer>> nonvse = getnonvex(attr, v, examples);
		double numvse = (double)vse.size();
		double numvg = (double)nonvse.size();
		
		// calc H(Y)
		double HY = 0;
		double p0 = numlab0/examples.size();
		if(p0 != 0)
			HY -= p0*(Math.log(p0)/Math.log(2));
		double p1 = 1-p0;
		if(p1 != 0)
			HY -= p1*(Math.log(p1)/Math.log(2));
		// calc H(Y|X)
		double HYX = 0;
		double HYX1 = 0;
		double HYX2 = 0;
		double HYX3 = 0;
		double HYX4 = 0;
		
		// Y0|X<=threshold
		double p0se = numvse!=0 ? getni(0, vse)/numvse : 0;
		if(p0se != 0)
			HYX1 = p0se*(Math.log(p0se)/Math.log(2));
		// Y0|X>threshold
		double p0g = numvg!=0 ? getni(0, nonvse)/numvg : 0;
		if(p0g != 0)
			HYX2 = p0g*(Math.log(p0g)/Math.log(2));
		// Y1|X<=threshold
		double p1se = numvse!=0 ? getni(1, vse)/numvse : 0;
		if(p1se != 0)
			HYX3 = p1se*(Math.log(p1se)/Math.log(2));
		// Y1|X>threshold
		double p1g = numvg!=0 ? getni(1, nonvse)/numvg : 0;
		if(p1g != 0)
			HYX4 = p1g*(Math.log(p1g)/Math.log(2));
		HYX += (numvse/exsize)*(HYX1+HYX3);
		HYX += (numvg/exsize)*(HYX2+HYX4);
		
		return HY+HYX;
	}
	
	private int getni(int label, List<List<Integer>> examples) {
		int cnt = 0;
		for(List<Integer> example:examples) {
			if(example.get(numAttr) == label)
				cnt++;
		}
		return cnt;
	}
	
	private List<List<Integer>> getvex(int attrib, int threshold, List<List<Integer>> examples) {
		List<List<Integer>> subset = new ArrayList<List<Integer>>();
		for(List<Integer> example:examples) {
			if(example.get(attrib) <= threshold)
				subset.add(example);
		}
		return subset;
	}
	
	private List<List<Integer>> getnonvex(int attrib, int threshold, List<List<Integer>> examples) {
		List<List<Integer>> subset = new ArrayList<List<Integer>>();
		for(List<Integer> example:examples) {
			if(example.get(attrib) > threshold)
				subset.add(example);
		}
		return subset;
	}
	
	private int getMajorLabel(List<List<Integer>> examples) {
		int cnt2 = 0, cnt4 = 0;
		for(int i = 0; i < examples.size(); i++) {
			// getting labels?
			if(examples.get(i).get(numAttr) == 0)
				cnt2++;
			if(examples.get(i).get(numAttr) == 1)
				cnt4++;
		}
		// If there are an equal number of instances labeled 0 and 1, then the label 1 is assigned to the leaf
		return cnt2 > cnt4 ? 0 : 1;
	}
	
	private boolean allSameLabel(List<List<Integer>> examples) {
		for(int i = 1; i < examples.size(); i++) {
			if(examples.get(i).get(numAttr) != examples.get(i-1).get(numAttr))
				return false;
		}
		return true;
	}
	
	public int classify(List<Integer> instance) {
		// TODO: add code here
		// Note that the last element of the array is the label.
		return classify(instance, root);
	}
	
	private int classify(List<Integer> instance, DecTreeNode node) {
		if(node.isLeaf())
			return node.classLabel;
		if(instance.get(node.attribute) <= node.threshold) {
			return classify(instance, node.left);
		} else {
			return classify(instance, node.right);
		}
	}
	
	// Print the decision tree in the specified format
	public void printTree() {
		printTreeNode("", this.root);
	}

	public void printTreeNode(String prefixStr, DecTreeNode node) {
		String printStr = prefixStr + "X_" + node.attribute;
		System.out.print(printStr + " <= " + String.format("%d", node.threshold));
		if(node.left.isLeaf()) {
			System.out.println(" : " + String.valueOf(node.left.classLabel));
		}
		else {
			System.out.println();
			printTreeNode(prefixStr + "|\t", node.left);
		}
		System.out.print(printStr + " > " + String.format("%d", node.threshold));
		if(node.right.isLeaf()) {
			System.out.println(" : " + String.valueOf(node.right.classLabel));
		}
		else {
			System.out.println();
			printTreeNode(prefixStr + "|\t", node.right);
		}
	}
	
	public double printTest(List<List<Integer>> testDataSet) {
		int numEqual = 0;
		int numTotal = 0;
		for (int i = 0; i < testDataSet.size(); i ++)
		{
			int prediction = classify(testDataSet.get(i));
			int groundTruth = testDataSet.get(i).get(testDataSet.get(i).size() - 1);
			System.out.println(prediction);
			if (groundTruth == prediction) {
				numEqual++;
			}
			numTotal++;
		}
		double accuracy = numEqual*100.0 / (double)numTotal;
		System.out.println(String.format("%.2f", accuracy) + "%");
		return accuracy;
	}
}
