Instruction to run the code:
1. Unpack
	- create a new directory and move to the new directory
	- unzip the czamfir_code.zip to the current directory
	- you will see 3 artifacts:
		- ../ml_lib - directory containing required jars
		- ../ml.jar - the code for the assignment
		- ../data - the .arff files for the assignment (it contains 2 files: robot-moves.arff and ...)

2. A java command line interface has been provided. Java 7 or higher is required.
   There are several runnable classes, each of the supporting the following set of parameters:
Command line options:
        -classIndex - last|first - values: What attribute is the class index? - default: last
        -c - Classifier - values: <dt|knn|ann|libsvm|smo|boost> - default: none
        -cs - Classifiers - values: one or more of dt|knn|ann|libsvm|smo|boost - default: dt,knn,ann,libsvm,smo,boost
        -baseLearner - The base learner for Boosting - values: ds|dt|knn|ann|libsvm|smo - default: ds
        -pruning - Use pruning for decision tree? - values: <true|false> - default: true
        -learningRate - For neural nets - values: numeric - default: 0.3
        -momentum - For neural nets - values: numeric - default: 0.2
        -kernel - The kernel function - values: <Liniar|Quadratic|Cubic|Radial|Sigmoid> - default: Quadratic
        -maxIterations - For boosting - values: numeric - default: 10
        -hiddenUnits - For neural nets - values: Comma delimited string of numbers/symbols. Each token -> one hidden layer with so many nodes - default: a
        -kn - The size of k-neighbours - values: numeric - default: 10
        -runs - The number of runs, for looping, e.g. for different training size - values: numeric - default: 20
        -initialSize - The initial size, for looping, e.g. for different training size - values: numeric - default: 100
        -stepSize - The step size, for looping, e.g. for different training size - values: numeric - default: 50
        -dataSet - The name of the data set file. Must be in the classpath - values: string - default: none
        -testSize - The size of the test data set - values: numeric - default: 1000
        -trainingSize - The size of the training data set - values: numeric - default: 1000
        -distanceWeight - The distance weighting function for KNN - values: <1(none)|2(inverse)|4(similarity)| - default: 2
        -crossValidate - Perform cross validation?. Default true - values: true|false - default: true
        -gamma - Gamma parameter for SMO. - values: numeric - default: 1
        -activation - The activation function for ANN - values: sigmoid|tanh - default: sigmoid
        -help - Pring this help screen

- To plot the learning curves of the non-tuned algos on a data set:
	java -cp ml.jar:ml_lib/*.jar:data ml.assignments.assignment1.LearningCurve -dataSet <filename> -initialSize 100 -stepSize 43 -runs 100 -testSize 1000

- To plot the learning curves of the tuned algorithms on one of the data set, run the following command:
  java -cp ml.jar:./ml_lib/*.jar:./data ml.assignments.assignment1.LearningCurve <params>, where params are:
  - for the first data set: -dataSet robot-moves.arff -pruning ... -confidenceFactor ... -bl ... -kn ... -distanceWeight ... -momentum ... -learningRate ... -hiddenUnits ... -kernel ... -initialSize 100 -runs 100 -stepSize 43   
  - for the second data set: -dataSet ... -pruning ... -confidenceFactor ... -bl ... -kn ... -distanceWeight ... -momentum ... -learningRate ... -hiddenUnits ... -kernel ... -initialSize 100 -runs 100 -stepSize 43










- To run once any classifier on one of the 2 data sets 
		java -cp ml.jar:./ml_lib/*.jar:./data ml.assignments.assignment1.Main <parameters>
		Example: run decision tree with pruning on data set robot-moves.arff, trainingSize 4000, testSize 1000, with cross validation:
			java -cp ml.jar:./ml_lib/*.jar:./data ml.assignments.assignment1.Main -dataSet robot-moves.arff -c dt -trainingSize 4000 -testSize 1000 -crossValidate true -pruning true
	This will run the specified classifier on the data set and will output to console: the classifier, the evaluation on the training set, the evaluation on the test set and
	the cross-validation
	
- To compare the accuracy and 3 other statistical indicators (precision, recall, F-measure) for several algorithms as a function of training size:
		java -cp ml.jar:./ml_lib/*.jar:./data ml.assignments.assignment1.StatIndicatorsComparison <parameters>
		Example: run decision tree, boosting, KNN and ANN algorithms on robot-moves.arff, 40 times, starting with training size 100, increasing with 100 at each iteration,
		with the test size 1000
		 	java -cp ml.jar:./ml_lib/*.jar:./data ml.assignments.assignment1.StatIndicatorsComparison -dataSet robot-moves.arff -cs dt,boost,knn,ann -initialSize 100 -stepSize 100 -runs 40 -testSize 1000
		This will create 4 charts (accuracy,precision,recal,F-measure) - each chart contains the stat indicator versus training size
		for all algorithms   	
		 	
- To compare the classification + evaluatin time for several algorithms as a function of training size:
		Example: run decision tree, boosting, KNN and ANN algorithms on robot-moves.arff, 40 times, starting with training size 100, increasing with 100 at each iteration
		 	java -cp ml.jar:./ml_lib/*.jar:./data ml.assignments.assignment1.ExecutionTimeComparison -dataSet robot-moves.arff -cs dt,boost,knn,ann -initialSize 100 -stepSize 100 -runs 40
		 This will create 2 charts : traing time + evaluation time versus training size, each chart containing all algorithms
		  
6. To plot the trainingError + testError versus training size for one algorithm:
		java -cp ml.jar:./ml_lib/*.jar:./data ml.assignments.assignment1.ErrorComparison <parameters>
		Example: run ANN on robot-moves.arff, 40 times, starting with training size 100, increasing with 100 at each iteration, with test size 1000
			java -cp ml.jar:./ml_lib/*.jar:./data ml.assignments.assignment1.ErrorComparison -dataSet robot-moves.arff -c ann -initialSize 100 -stepSize 100 -runs 40 -testSize 1000
		This will plot training error rate + test error rate versus trainng size
		
7. 			